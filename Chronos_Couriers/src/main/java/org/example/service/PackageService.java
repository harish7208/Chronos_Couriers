package org.example.service;

import org.example.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class PackageService {

    private final Map<String, DeliveryPackage> packages = new HashMap<>();
    private static int packageCounter = 1;
    private final Queue<DeliveryPackage> pendingPackages;

    public PackageService(Queue<DeliveryPackage> pendingPackages) {
        this.pendingPackages = pendingPackages;
    }

    public String placeOrder(PackagePriority priority, long deadline, boolean fragile) {
        String packageId = "PKG" + (packageCounter++);
        DeliveryPackage deliveryPackage = new DeliveryPackage(packageId, priority, deadline,
                System.currentTimeMillis(), fragile);
        packages.put(packageId, deliveryPackage);
        pendingPackages.offer(deliveryPackage);
        return packageId;
    }

    public void simulatePickup(String packageId) {
        DeliveryPackage deliveryPackage = packages.get(packageId);
        if (deliveryPackage != null && deliveryPackage.getStatus() == PackageStatus.ASSIGNED) {
            deliveryPackage.setStatus(PackageStatus.PICKED_UP);
            deliveryPackage.setPickupTime(System.currentTimeMillis());
        }
    }

    public void simulateDelivery(String packageId, RiderService riderService) {
        DeliveryPackage deliveryPackage = packages.get(packageId);
        if (deliveryPackage != null && deliveryPackage.getStatus() == PackageStatus.PICKED_UP) {
            deliveryPackage.setStatus(PackageStatus.DELIVERED);
            deliveryPackage.setDeliveryTime(System.currentTimeMillis());
            Rider rider = riderService.getRider(deliveryPackage.getAssignedRiderId());
            if (rider != null) {
                double rating = deliveryPackage.getDeliveryTime() <= deliveryPackage.getDeadline() ? 10.0 : 7.0;
                rider.updateReliability(rating);
                rider.setStatus(RiderStatus.AVAILABLE);
            }
        }
    }

    public void cancelPackage(String packageId, RiderService riderService) {
        DeliveryPackage deliveryPackage = packages.get(packageId);
        if (deliveryPackage != null && deliveryPackage.getStatus() != PackageStatus.DELIVERED) {
            deliveryPackage.setStatus(PackageStatus.FAILED);
            Rider rider = riderService.getRider(deliveryPackage.getAssignedRiderId());
            if (rider != null) {
                rider.penalizeReliability(-2.0);
                rider.setStatus(RiderStatus.AVAILABLE);
            }
        }
    }

    public void reassignPackage(String packageId) {
        DeliveryPackage deliveryPackage = packages.get(packageId);
        if (deliveryPackage != null && deliveryPackage.getStatus() == PackageStatus.FAILED) {
            deliveryPackage.setStatus(PackageStatus.PENDING);
            deliveryPackage.setAssignedRiderId(null);
            deliveryPackage.setPickupTime(null);
            pendingPackages.offer(deliveryPackage);
        }
    }

    public DeliveryPackage getPackageInfo(String packageId) {
        return packages.get(packageId);
    }

    public String getPackageStatus(String packageId) {
        DeliveryPackage deliveryPackage = packages.get(packageId);
        return deliveryPackage != null ? deliveryPackage.getStatus().name() : "Package not found";
    }

    public List<DeliveryPackage> getAllPackages() {
        return new ArrayList<>(packages.values());
    }

    public List<DeliveryPackage> getCancelledPackages() {
        return packages.values().stream()
                .filter(p -> p.getStatus() == PackageStatus.FAILED)
                .collect(Collectors.toList());
    }

    public List<DeliveryPackage> getAssignments() {
        return packages.values().stream()
                .filter(p -> p.getAssignedRiderId() != null)
                .collect(Collectors.toList());
    }

    public List<DeliveryPackage> getMissedExpressDeliveries() {
        long now = System.currentTimeMillis();
        return packages.values().stream()
                .filter(p -> p.getPriority() == PackagePriority.EXPRESS
                        && p.getDeadline() < now
                        && p.getStatus() != PackageStatus.DELIVERED)
                .collect(Collectors.toList());
    }

    public List<DeliveryPackage> getPackagesDeliveredByRider(String riderId) {
        long twentyFourHoursAgo = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        return packages.values().stream()
                .filter(p -> riderId.equals(p.getAssignedRiderId())
                        && p.getStatus() == PackageStatus.DELIVERED
                        && p.getDeliveryTime() != null
                        && p.getDeliveryTime() >= twentyFourHoursAgo)
                .collect(Collectors.toList());
    }

    public Map<String, DeliveryPackage> getPackages() {
        return packages;
    }
}
