package org.example.service;

import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackageServiceTest {

    private PackageService packageService;
    private RiderService riderService;
    private Queue<DeliveryPackage> packages;

    @BeforeEach
    void setup() {
        packages = new PriorityQueue<>(
                (a, b) -> b.getPriority().compareTo(a.getPriority())); // simplified comparator
        packageService = new PackageService(packages);
        riderService = mock(RiderService.class);
    }

    @Test
    void testPlaceOrder() {
        String id = packageService.placeOrder(PackagePriority.EXPRESS, System.currentTimeMillis() + 100000, false);
        assertNotNull(id);
        assertTrue(packageService.getPackages().containsKey(id));
    }

    @Test
    void testSimulatePickupAndDeliveryBeforeDeadline() {
        long deadline = System.currentTimeMillis() + 100000;
        String packageId = packageService.placeOrder(PackagePriority.STANDARD, deadline, false);
        DeliveryPackage deliveryPackage = packageService.getPackageInfo(packageId);
        deliveryPackage.setStatus(PackageStatus.ASSIGNED);

        Rider rider = new Rider("RID1", "Test Rider", false, 5.0);
        deliveryPackage.setAssignedRiderId(rider.getId());

        when(riderService.getRider("RID1")).thenReturn(rider);

        packageService.simulatePickup(packageId);
        assertEquals(PackageStatus.PICKED_UP, deliveryPackage.getStatus());

        packageService.simulateDelivery(packageId, riderService);
        assertEquals(PackageStatus.DELIVERED, deliveryPackage.getStatus());
        assertEquals(RiderStatus.AVAILABLE, rider.getStatus());
        assertEquals(7.5, rider.getReliabilityRating(), 0.1);
    }

    @Test
    void testSimulateDeliveryAfterDeadline() {
        long pastDeadline = System.currentTimeMillis() - 1000;
        String packageId = packageService.placeOrder(PackagePriority.STANDARD, pastDeadline, false);
        DeliveryPackage deliveryPackage = packageService.getPackageInfo(packageId);
        deliveryPackage.setStatus(PackageStatus.PICKED_UP);
        deliveryPackage.setAssignedRiderId("RID2");

        Rider rider = new Rider("RID2", "Late Rider", false, 8.0);
        when(riderService.getRider("RID2")).thenReturn(rider);

        packageService.simulateDelivery(packageId, riderService);
        assertEquals(PackageStatus.DELIVERED, deliveryPackage.getStatus());
        assertTrue(rider.getReliabilityRating() <= 8.0);
    }

    @Test
    void testCancelPackage() {
        String packageId = packageService.placeOrder(PackagePriority.STANDARD, System.currentTimeMillis() + 100000, false);
        DeliveryPackage deliveryPackage = packageService.getPackageInfo(packageId);
        deliveryPackage.setStatus(PackageStatus.ASSIGNED);
        deliveryPackage.setAssignedRiderId("RID3");

        Rider rider = new Rider("RID3", "Cancel Rider", false, 6.0);
        when(riderService.getRider("RID3")).thenReturn(rider);

        packageService.cancelPackage(packageId, riderService);
        assertEquals(PackageStatus.FAILED, deliveryPackage.getStatus());
        assertEquals(RiderStatus.AVAILABLE, rider.getStatus());
        assertTrue(rider.getReliabilityRating() <= 6.0);
    }
}

