package org.example.service;

import org.example.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class PackageService {

    private static int packageCounter = 1;
    private final Map<String, DeliveryPackage> packageMap = new HashMap<>();
    private final Queue<DeliveryPackage> pendingPackages;

    public PackageService(Queue<DeliveryPackage> pendingQueue) {
        this.pendingPackages = pendingQueue;
    }

    public String placeOrder(PackagePriority priority, long deadline, boolean fragile) {
        String packageId = "PKG" + (packageCounter++);
        DeliveryPackage dp = new DeliveryPackage(packageId, priority, deadline, System.currentTimeMillis(), fragile);
        packageMap.put(packageId, dp);
        pendingPackages.offer(dp);
        return packageId;
    }

    public void simulatePickup(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() == PackageStatus.ASSIGNED) {
            dp.setStatus(PackageStatus.PICKED_UP);
            dp.setPickupTime(System.currentTimeMillis());
        }
    }

    public void simulateDelivery(String packageId, RiderService riderService) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() == PackageStatus.PICKED_UP) {
            dp.setStatus(PackageStatus.DELIVERED);
            dp.setDeliveryTime(System.currentTimeMillis());
            Rider rider = riderService.getRider(dp.getAssignedRiderId());
            if (rider != null) rider.setStatus(RiderStatus.AVAILABLE);
        }
    }

    public void cancelPackage(String packageId, RiderService riderService) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() != PackageStatus.DELIVERED) {
            dp.setStatus(PackageStatus.FAILED);
            Rider rider = riderService.getRider(dp.getAssignedRiderId());
            if (rider != null) rider.setStatus(RiderStatus.AVAILABLE);
        }
    }

    public void reassignPackage(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        if (dp != null && dp.getStatus() == PackageStatus.FAILED) {
            dp.setStatus(PackageStatus.PENDING);
            dp.setAssignedRiderId(null);
            dp.setPickupTime(null);
            pendingPackages.offer(dp);
        }
    }

    public DeliveryPackage getPackageInfo(String packageId) {
        return packageMap.get(packageId);
    }

    public String getPackageStatus(String packageId) {
        DeliveryPackage dp = packageMap.get(packageId);
        return dp != null ? dp.getStatus().name() : "Package not found";
    }

    public List<DeliveryPackage> getAllPackages() {
        return new ArrayList<>(packageMap.values());
    }

    public List<DeliveryPackage> getCancelledPackages() {
        return packageMap.values().stream().filter(p -> p.getStatus() == PackageStatus.FAILED).collect(Collectors.toList());
    }

    public List<DeliveryPackage> getAssignments() {
        return packageMap.values().stream().filter(p -> p.getAssignedRiderId() != null).collect(Collectors.toList());
    }

    public List<DeliveryPackage> getMissedExpressDeliveries() {
        long now = System.currentTimeMillis();
        return packageMap.values().stream().filter(p -> p.getPriority() == PackagePriority.EXPRESS && p.getDeadline() < now && p.getStatus() != PackageStatus.DELIVERED).collect(Collectors.toList());
    }

    public List<DeliveryPackage> getPackagesDeliveredByRider(String riderId) {
        long twentyFourHoursAgo = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        return packageMap.values().stream().filter(p -> riderId.equals(p.getAssignedRiderId()) && p.getStatus() == PackageStatus.DELIVERED && p.getDeliveryTime() != null && p.getDeliveryTime() >= twentyFourHoursAgo).collect(Collectors.toList());
    }

    public Map<String, DeliveryPackage> getPackageMap() {
        return packageMap;
    }
}
