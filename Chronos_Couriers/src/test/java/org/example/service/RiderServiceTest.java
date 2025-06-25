package org.example.service;


import org.example.model.DeliveryPackage;
import org.example.model.Rider;
import org.example.model.RiderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RiderServiceTest {

    private RiderService riderService;
    private Map<String, DeliveryPackage> packages;
    private Queue<DeliveryPackage> pendingPackages;

    @BeforeEach
    void setup() {
        riderService = new RiderService();
        packages = new HashMap<>();
        pendingPackages = new LinkedList<>();
    }

    @Test
    void testRegisterRider() {
        String riderId = riderService.registerRider("Test", true, 7.5);
        assertNotNull(riderId);
        Rider rider = riderService.getRider(riderId);
        assertEquals("Test", rider.getName());
        assertTrue(rider.canHandleFragile());
        assertEquals(7.5, rider.getReliabilityRating(), 0.01);
        assertEquals(RiderStatus.AVAILABLE, rider.getStatus());
    }

    @Test
    void testGetRiderStatus() {
        String riderId = riderService.registerRider("Test", true, 8.0);
        assertEquals("AVAILABLE", riderService.getRiderStatus(riderId));
        assertEquals("Rider not found", riderService.getRiderStatus("invalid-id"));
    }

    @Test
    void testGetAllRiders() {
        riderService.registerRider("A", true, 5.0);
        riderService.registerRider("B", false, 6.0);
        assertEquals(2, riderService.getAllRiders().size());
    }

    @Test
    void testGetAvailableRidersSorted() {
        String firstRider = riderService.registerRider("Fast", true, 9.0);
        String secondRider = riderService.registerRider("Slow", false, 3.0);
        Rider rider = riderService.getRider(secondRider);
        rider.setStatus(RiderStatus.BUSY);

        List<Rider> sorted = new ArrayList<>(riderService.getAvailableRidersSorted());
        assertEquals(1, sorted.size());
        assertEquals(firstRider, sorted.get(0).getId());
    }

    @Test
    void testSeedPredefinedRiders() {
        riderService.seedPredefinedRiders();
        assertEquals(4, riderService.getAllRiders().size());
    }

    @Test
    void testUpdateRiderStatusToOfflineGracefully() {
        String riderId = riderService.registerRider("Grace", true, 7.0);
        Rider rider = riderService.getRider(riderId);
        rider.setStatus(RiderStatus.BUSY);

        DeliveryPackage deliveryPackage = new DeliveryPackage("PKG1", org.example.model.PackagePriority.EXPRESS, System.currentTimeMillis() + 1000, System.currentTimeMillis(), false);
        deliveryPackage.setAssignedRiderId(riderId);
        deliveryPackage.setStatus(org.example.model.PackageStatus.ASSIGNED);
        packages.put(deliveryPackage.getId(), deliveryPackage);

        riderService.updateRiderStatus(riderId, false, packages, pendingPackages);

        assertEquals(RiderStatus.OFFLINE, rider.getStatus());
        assertEquals(org.example.model.PackageStatus.FAILED, deliveryPackage.getStatus());
        assertTrue(pendingPackages.contains(deliveryPackage));
    }
}

