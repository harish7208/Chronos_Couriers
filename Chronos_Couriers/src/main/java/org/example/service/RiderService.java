package org.example.service;

import org.example.model.DeliveryPackage;
import org.example.model.Rider;
import org.example.model.RiderStatus;

import java.util.*;
import java.util.stream.Collectors;

public class RiderService {

    private static int riderCounter = 1;
    private final Map<String, Rider> riderMap = new HashMap<>();

    public String registerRider(String name, boolean canHandleFragile, double reliability) {
        String riderId = "RID" + (riderCounter++);
        Rider rider = new Rider(riderId, name, canHandleFragile, reliability);
        riderMap.put(riderId, rider);
        return riderId;
    }

    public void updateRiderStatus(String riderId, boolean available, Map<String, DeliveryPackage> packageMap, Queue<DeliveryPackage> pendingQueue) {
        Rider rider = riderMap.get(riderId);
        if (rider != null) {
            rider.updateStatus(available, packageMap, pendingQueue);
        }
    }

    public List<Rider> getAllRiders() {
        return new ArrayList<>(riderMap.values());
    }

    public Rider getRider(String riderId) {
        return riderMap.get(riderId);
    }

    public String getRiderStatus(String riderId) {
        Rider rider = riderMap.get(riderId);
        return rider != null ? rider.getStatus().name() : "Rider not found";
    }

    public Collection<Rider> getAvailableRidersSorted() {
        return riderMap.values().stream().filter(r -> r.getStatus() == RiderStatus.AVAILABLE).sorted(Comparator.comparingDouble(Rider::getReliabilityRating).reversed()).collect(Collectors.toList());
    }

    public void seedPredefinedRiders() {
        registerRider("Alice", true, 0.95);
        registerRider("Bob", false, 0.85);
        registerRider("Charlie", true, 0.90);
        registerRider("Diana", false, 0.80);
    }
}

