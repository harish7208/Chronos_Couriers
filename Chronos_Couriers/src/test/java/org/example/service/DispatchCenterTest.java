package org.example.service;


import org.example.model.DeliveryPackage;
import org.example.model.PackagePriority;
import org.example.model.PackageStatus;
import org.example.model.Rider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DispatchCenterTest {

    private DispatchCenter dispatchCenter;

    @BeforeEach
    void setup() {
        dispatchCenter = new DispatchCenter();
    }

    @Test
    void testPlaceOrderAndAutoAssign() {
        String packageId = dispatchCenter.placeOrder(PackagePriority.EXPRESS, System.currentTimeMillis() + 5000, false);
        DeliveryPackage deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        assertNotNull(deliveryPackage);
        assertEquals(PackageStatus.ASSIGNED, deliveryPackage.getStatus());
        assertNotNull(deliveryPackage.getAssignedRiderId());
    }

    @Test
    void testRegisterRiderAndAssignPendingPackage() {
        String newRiderId = dispatchCenter.registerRider("TestRider", true, 10.0);
        assertNotNull(newRiderId);
        String packageId = dispatchCenter.placeOrder(PackagePriority.EXPRESS, System.currentTimeMillis() + 5000, true);
        DeliveryPackage deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        assertEquals(PackageStatus.ASSIGNED, deliveryPackage.getStatus());
    }

    @Test
    void testUpdateRiderStatusOffline() {
        String riderId = dispatchCenter.registerRider("Temp", false, 7.5);
        dispatchCenter.updateRiderStatus(riderId, false);
        String status = dispatchCenter.getRiderStatus(riderId);
        assertEquals("OFFLINE", status);
    }

    @Test
    void testSimulatePickupAndDelivery() {
        String packageId = dispatchCenter.placeOrder(PackagePriority.STANDARD, System.currentTimeMillis() + 10000, false);
        dispatchCenter.simulatePickup(packageId);
        DeliveryPackage deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        assertEquals(PackageStatus.PICKED_UP, deliveryPackage.getStatus());

        dispatchCenter.simulateDelivery(packageId);
        assertEquals(PackageStatus.DELIVERED, deliveryPackage.getStatus());
    }

    @Test
    void testCancelPackage() {
        String packageId = dispatchCenter.placeOrder(PackagePriority.EXPRESS, System.currentTimeMillis() + 10000, false);
        dispatchCenter.cancelPackage(packageId);
        DeliveryPackage deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        assertEquals(PackageStatus.FAILED, deliveryPackage.getStatus());
    }

    @Test
    void testReassignFailedPackage() {
        String packageId = dispatchCenter.placeOrder(PackagePriority.STANDARD, System.currentTimeMillis() + 5000, false);
        dispatchCenter.cancelPackage(packageId);
        dispatchCenter.reassignPackage(packageId);
        DeliveryPackage deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        assertEquals(PackageStatus.ASSIGNED, deliveryPackage.getStatus());
    }

    @Test
    void testGetMissedExpressDeliveries() throws InterruptedException {
        String latePackageId = dispatchCenter.placeOrder(PackagePriority.EXPRESS, System.currentTimeMillis() + 10, false);
        Thread.sleep(20);
        List<DeliveryPackage> packages = dispatchCenter.getMissedExpressDeliveries();
        assertTrue(packages.stream().anyMatch(p -> p.getId().equals(latePackageId)));
    }

    @Test
    void testGetDeliveredPackagesByRider() {
        String packageId = dispatchCenter.placeOrder(PackagePriority.STANDARD, System.currentTimeMillis() + 10000, false);
        DeliveryPackage deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        String riderId = deliveryPackage.getAssignedRiderId();

        dispatchCenter.simulatePickup(packageId);
        dispatchCenter.simulateDelivery(packageId);

        List<DeliveryPackage> packages = dispatchCenter.getPackagesDeliveredByRider(riderId);
        assertTrue(packages.stream().anyMatch(p -> p.getId().equals(packageId)));
    }

    @Test
    void testGetAllRidersAndPackages() {
        List<Rider> riders = dispatchCenter.getAllRiders();
        List<DeliveryPackage> packages = dispatchCenter.getAllPackages();
        assertFalse(riders.isEmpty());
        assertTrue(packages.isEmpty());
    }
}
