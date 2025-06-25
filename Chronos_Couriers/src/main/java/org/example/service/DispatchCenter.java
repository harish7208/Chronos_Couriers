package org.example.service;

import org.example.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class DispatchCenter {

    private static int packageCounter = 1;
    private static int riderCounter = 1;
    private final Map<String, DeliveryPackage> packageMap = new HashMap<>();
    private final Map<String, Rider> riderMap = new HashMap<>();
    private final PriorityQueue<DeliveryPackage> pendingPackages = new PriorityQueue<>(Comparator.comparing(DeliveryPackage::getPriority).reversed().thenComparing(DeliveryPackage::getDeadline).thenComparing(DeliveryPackage::getOrderTime));
    public DispatchCenter() {

        registerRider("Alice", true, 0.95);
        registerRider("Bob", false, 0.85);
        registerRider("Charlie", true, 0.90);
        registerRider("Diana", false, 0.80);
    }

    public static void printTable(List<? extends TableView> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        list.get(0).printHeader();
        for (TableView row : list) {
            row.printRow();
        }
    }

    public String placeOrder(PackagePriority priority, long deadline, boolean fragile) {
        String packageId = "PKG" + (packageCounter++);
        DeliveryPackage dp = new DeliveryPackage(packageId, priority, deadline, System.currentTimeMillis(), fragile);
        packageMap.put(packageId, dp);
        pendingPackages.offer(dp);
        assignPackages();
        return packageId;
    }

    public String registerRider(String name, boolean canHandleFragile, double reliability) {
        String riderId = "RID" + (riderCounter++);
        Rider rider = new Rider(riderId, name, canHandleFragile, reliability);
        riderMap.put(riderId, rider);
        assignPackages();
        return riderId;
    }

    public void updateRiderStatus(String riderId, boolean available) {
        Rider rider = riderMap.get(riderId);
        if (rider != null) {
            rider.updateStatus(available, packageMap, pendingPackages);
            assignPackages();
        }
    }

    private void assignPackages() {
        List<DeliveryPackage> assigned = new ArrayList<>();
        for (DeliveryPackage p : pendingPackages) {
            List<Rider> sortedRiders = riderMap.values().stream().filter(r -> r.getStatus() == RiderStatus.AVAILABLE).sorted(Comparator.comparingDouble(Rider::getReliabilityRating).reversed()).collect(Collectors.toList());

            Rider selected = null;

            if (p.isFragile()) {
                selected = sortedRiders.stream().filter(Rider::canHandleFragile).findFirst().orElse(null);
            } else {
                selected = sortedRiders.stream().filter(r -> !r.canHandleFragile()).findFirst().orElse(sortedRiders.stream().filter(Rider::canHandleFragile).findFirst().orElse(null));
            }

            if (selected != null) {
                p.setAssignedRiderId(selected.getId());
                p.setStatus(PackageStatus.ASSIGNED);
                selected.setStatus(RiderStatus.BUSY);
                assigned.add(p);
            }
        }
        pendingPackages.removeAll(assigned);
    }

    public void simulatePickup(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() == PackageStatus.ASSIGNED) {
            dp.setStatus(PackageStatus.PICKED_UP);
            dp.setPickupTime(System.currentTimeMillis());
        }
    }

    public void simulateDelivery(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() == PackageStatus.PICKED_UP) {
            dp.setStatus(PackageStatus.DELIVERED);
            dp.setDeliveryTime(System.currentTimeMillis());
            Rider rider = riderMap.get(dp.getAssignedRiderId());
            if (rider != null) rider.setStatus(RiderStatus.AVAILABLE);
        }
    }

    public void cancelPackage(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() != PackageStatus.DELIVERED) {
            dp.setStatus(PackageStatus.FAILED);
            Rider rider = riderMap.get(dp.getAssignedRiderId());
            if (rider != null) rider.setStatus(RiderStatus.AVAILABLE);
        }
    }

    public List<DeliveryPackage> getCancelledPackages() {
        return packageMap.values().stream().filter(p -> p.getStatus() == PackageStatus.FAILED).collect(Collectors.toList());
    }

    public void reassignPackage(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() == PackageStatus.FAILED) {
            dp.setStatus(PackageStatus.PENDING);
            dp.setAssignedRiderId(null);
            dp.setPickupTime(null);
            pendingPackages.offer(dp);
            assignPackages();
        }
    }

    public List<Rider> getAllRiders() {
        return new ArrayList<>(riderMap.values());
    }

    public List<DeliveryPackage> getAllPackages() {
        return new ArrayList<>(packageMap.values());
    }

    public List<DeliveryPackage> getAssignments() {
        return packageMap.values().stream().filter(p -> p.getAssignedRiderId() != null).collect(Collectors.toList());
    }

    public DeliveryPackage getPackageInfo(String packageId) {
        return packageMap.get(packageId);
    }

    public String getPackageStatus(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        return dp != null ? dp.getStatus().name() : "Package not found";
    }

    public String getRiderStatus(String riderId) {
        Rider rider = riderMap.get(riderId);
        return rider != null ? rider.getStatus().name() : "Rider not found";
    }

    public List<DeliveryPackage> getMissedExpressDeliveries() {
        long now = System.currentTimeMillis();
        return packageMap.values().stream().filter(p -> p.getPriority() == PackagePriority.EXPRESS && p.getDeadline() < now && p.getStatus() != PackageStatus.DELIVERED).collect(Collectors.toList());
    }

    public List<DeliveryPackage> getPackagesDeliveredByRider(String riderId) {
        long twentyFourHoursAgo = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        return packageMap.values().stream().filter(p -> riderId.equals(p.getAssignedRiderId()) && p.getStatus() == PackageStatus.DELIVERED && p.getDeliveryTime() != null && p.getDeliveryTime() >= twentyFourHoursAgo).collect(Collectors.toList());
    }
}
