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

    private final Queue<DeliveryPackage> pendingQueue;
    private final RiderService riderService;

    public AssignmentService(Queue<DeliveryPackage> pendingQueue, RiderService riderService) {
        this.pendingQueue = pendingQueue;
        this.riderService = riderService;
    }

    public void assignPackages(Map<String, DeliveryPackage> packageMap) {
        List<DeliveryPackage> assigned = new ArrayList<>();

        for (DeliveryPackage p : pendingQueue) {
            List<Rider> sortedRiders = new ArrayList<>(riderService.getAvailableRidersSorted());
            Rider selected = null;

            if (p.isFragile()) {
                selected = sortedRiders.stream()
                        .filter(Rider::canHandleFragile)
                        .findFirst()
                        .orElse(null);
            } else {
                selected = sortedRiders.stream()
                        .filter(r -> !r.canHandleFragile())
                        .findFirst()
                        .orElse(
                                sortedRiders.stream()
                                        .filter(Rider::canHandleFragile)
                                        .findFirst()
                                        .orElse(null));
            }

            if (selected != null) {
                p.setAssignedRiderId(selected.getId());
                p.setStatus(PackageStatus.ASSIGNED);
                selected.setStatus(RiderStatus.BUSY);
                assigned.add(p);
            }
        }

        pendingQueue.removeAll(assigned);
    }
}
