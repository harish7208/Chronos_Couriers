package org.example.service;

import org.example.model.DeliveryPackage;
import org.example.model.PackagePriority;
import org.example.model.PackageStatus;
import org.example.model.RiderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentServiceTest {

    private AssignmentService assignmentService;
    private RiderService riderService;
    private Queue<DeliveryPackage> pendingPackages;
    private Map<String, DeliveryPackage> packages;

    @BeforeEach
    void setup() {
        riderService = new RiderService();
        pendingPackages = new LinkedList<>();
        packages = new HashMap<>();
        assignmentService = new AssignmentService(pendingPackages, riderService);
    }

    @Test
    void testAssignNonFragilePackageToNonFragileRider() {

        String riderId = riderService.registerRider("NonFragileRider", false, 9.0);
        DeliveryPackage deliveryPackage = new DeliveryPackage("PKG1", PackagePriority.STANDARD, System.currentTimeMillis() + 10000, System.currentTimeMillis(), false);
        pendingPackages.add(deliveryPackage);
        packages.put(deliveryPackage.getId(), deliveryPackage);

        assignmentService.assignPackages(packages);

        assertEquals(PackageStatus.ASSIGNED, deliveryPackage.getStatus());
        assertEquals(riderId, deliveryPackage.getAssignedRiderId());
        assertEquals(RiderStatus.BUSY, riderService.getRider(riderId).getStatus());
    }

    @Test
    void testAssignFragilePackageToCapableRider() {
        String riderId = riderService.registerRider("FragileRider", true, 8.5);
        DeliveryPackage deliveryPackage = new DeliveryPackage("PKG2", PackagePriority.EXPRESS, System.currentTimeMillis() + 10000, System.currentTimeMillis(), true);
        pendingPackages.add(deliveryPackage);
        packages.put(deliveryPackage.getId(), deliveryPackage);

        assignmentService.assignPackages(packages);

        assertEquals(PackageStatus.ASSIGNED, deliveryPackage.getStatus());
        assertEquals(riderId, deliveryPackage.getAssignedRiderId());
    }

    @Test
    void testNoAssignmentIfNoAvailableRider() {
        DeliveryPackage deliveryPackage = new DeliveryPackage("PKG3", PackagePriority.STANDARD, System.currentTimeMillis() + 10000, System.currentTimeMillis(), false);
        pendingPackages.add(deliveryPackage);
        packages.put(deliveryPackage.getId(), deliveryPackage);
        assignmentService.assignPackages(packages);
        assertNull(deliveryPackage.getAssignedRiderId());
        assertEquals(PackageStatus.PENDING, deliveryPackage.getStatus());
    }

    @Test
    void testFallbackToFragileCapableRiderIfNonFragileNotFound() {
        String riderId = riderService.registerRider("FallbackRider", true, 9.0); // can handle fragile
        DeliveryPackage deliveryPackage = new DeliveryPackage("PKG4", PackagePriority.STANDARD, System.currentTimeMillis() + 10000, System.currentTimeMillis(), false);
        pendingPackages.add(deliveryPackage);
        packages.put(deliveryPackage.getId(), deliveryPackage);

        assignmentService.assignPackages(packages);

        assertEquals(PackageStatus.ASSIGNED, deliveryPackage.getStatus());
        assertEquals(riderId, deliveryPackage.getAssignedRiderId());
    }

    @Test
    void testPendingQueueIsClearedForAssignedPackages() {
        String riderId = riderService.registerRider("Cleaner", false, 9.0);
        DeliveryPackage deliveryPackage = new DeliveryPackage("PKG5", PackagePriority.STANDARD, System.currentTimeMillis() + 10000, System.currentTimeMillis(), false);
        pendingPackages.add(deliveryPackage);
        packages.put(deliveryPackage.getId(), deliveryPackage);

        assignmentService.assignPackages(packages);

        assertTrue(pendingPackages.isEmpty());
    }
}

