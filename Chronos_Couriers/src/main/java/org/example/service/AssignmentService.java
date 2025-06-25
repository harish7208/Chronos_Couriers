package org.example.service;

import org.example.model.DeliveryPackage;
import org.example.model.PackageStatus;
import org.example.model.Rider;
import org.example.model.RiderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AssignmentService {

    private final Queue<DeliveryPackage> pendingPackages;
    private final RiderService riderService;

    public AssignmentService(Queue<DeliveryPackage> pendingPackages, RiderService riderService) {
        this.pendingPackages = pendingPackages;
        this.riderService = riderService;
    }

    public void assignPackages(Map<String, DeliveryPackage> packageMap) {
        List<DeliveryPackage> assignedPackages = new ArrayList<>();

        for (DeliveryPackage deliveryPackage : pendingPackages) {
            List<Rider> sortedRiders = new ArrayList<>(riderService.getAvailableRidersSorted());
            Rider rider = null;

            if (deliveryPackage.isFragile()) {
                rider = sortedRiders.stream()
                        .filter(Rider::canHandleFragile)
                        .findFirst()
                        .orElse(null);
            } else {
                rider = sortedRiders.stream()
                        .filter(r -> !r.canHandleFragile())
                        .findFirst()
                        .orElse(
                                sortedRiders.stream()
                                        .filter(Rider::canHandleFragile)
                                        .findFirst()
                                        .orElse(null));
            }

            if (rider != null) {
                deliveryPackage.setAssignedRiderId(rider.getId());
                deliveryPackage.setStatus(PackageStatus.ASSIGNED);
                rider.setStatus(RiderStatus.BUSY);
                assignedPackages.add(deliveryPackage);
            }
        }

        pendingPackages.removeAll(assignedPackages);
    }
}
