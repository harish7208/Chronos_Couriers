package org.example.service;

import org.example.model.DeliveryPackage;
import org.example.model.Rider;
import org.example.model.RiderStatus;

import java.util.*;
import java.util.stream.Collectors;

public class RiderService {

    private static int riderCounter = 1;
    private final Map<String, Rider> riders = new HashMap<>();

    public String registerRider(String name, boolean canHandleFragile, double reliability) {
        String riderId = "RID" + (riderCounter++);
        Rider rider = new Rider(riderId, name, canHandleFragile, reliability);
        riders.put(riderId, rider);
        return riderId;
    }

    public void updateRiderStatus(String riderId, boolean available, Map<String, DeliveryPackage> packageMap, Queue<DeliveryPackage> pendingQueue) {
        Rider rider = riders.get(riderId);
        if (rider != null) {
            rider.updateStatus(available, packageMap, pendingQueue);
        }
    }

    public List<Rider> getAllRiders() {
        return new ArrayList<>(riders.values());
    }

    public Rider getRider(String riderId) {
        return riders.get(riderId);
    }

    public String getRiderStatus(String riderId) {
        Rider rider = riders.get(riderId);
        return rider != null ? rider.getStatus().name() : "Rider not found";
    }

    public Collection<Rider> getAvailableRidersSorted() {
        return riders.values().stream().filter(r -> r.getStatus() == RiderStatus.AVAILABLE).sorted(Comparator.comparingDouble(Rider::getReliabilityRating).reversed()).collect(Collectors.toList());
    }

    public void seedPredefinedRiders() {
        registerRider("Karan", true, 5.0);
        registerRider("Raj", false, 5.0);
        registerRider("Parth", true, 5.0);
        registerRider("Naman", false, 5.0);
    }
}

