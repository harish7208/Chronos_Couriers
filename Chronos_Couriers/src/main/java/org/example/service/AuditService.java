package org.example.service;

import org.example.model.DeliveryPackage;

import java.util.List;
import java.util.stream.Collectors;

public class AuditService {

    public List<DeliveryPackage> getMissedExpressDeliveries(List<DeliveryPackage> allPackages) {
        long now = System.currentTimeMillis();
        return allPackages.stream().filter(p -> p.getPriority().name().equals("EXPRESS") && p.getDeadline() < now && p.getStatus().name() != "DELIVERED").collect(Collectors.toList());
    }

    public List<DeliveryPackage> getCancelledPackages(List<DeliveryPackage> allPackages) {
        return allPackages.stream().filter(p -> p.getStatus().name().equals("FAILED")).collect(Collectors.toList());
    }

    public List<DeliveryPackage> getRiderDeliveryHistory(List<DeliveryPackage> allPackages, String riderId) {
        long twentyFourHoursAgo = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        return allPackages.stream().filter(p -> riderId.equals(p.getAssignedRiderId()) && p.getStatus().name().equals("DELIVERED") && p.getDeliveryTime() != null && p.getDeliveryTime() >= twentyFourHoursAgo).collect(Collectors.toList());
    }
}
