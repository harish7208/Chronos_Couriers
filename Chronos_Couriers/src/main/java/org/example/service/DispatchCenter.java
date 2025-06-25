package org.example.service;

import org.example.model.DeliveryPackage;
import org.example.model.PackagePriority;
import org.example.model.Rider;
import org.example.model.TableView;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class DispatchCenter {

    private final Queue<DeliveryPackage> pendingQueue;
    private final RiderService riderService;
    private final PackageService packageService;
    private final AssignmentService assignmentService;
    private final AuditService auditService;

    public DispatchCenter() {
        this.pendingQueue = new PriorityQueue<>(
                Comparator.comparing(DeliveryPackage::getPriority).reversed()
                        .thenComparing(DeliveryPackage::getDeadline)
                        .thenComparing(DeliveryPackage::getOrderTime));

        this.riderService = new RiderService();
        this.packageService = new PackageService(pendingQueue);
        this.assignmentService = new AssignmentService(pendingQueue, riderService);
        this.auditService = new AuditService();

        riderService.seedPredefinedRiders();
    }

    public static void printTable(List<? extends TableView> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        list.get(0).printHeader();
        for (TableView row : list) {
            row.printRow();
        }
    }

    public String placeOrder(PackagePriority priority, long deadline, boolean fragile) {
        String id = packageService.placeOrder(priority, deadline, fragile);
        assignmentService.assignPackages(packageService.getPackageMap());
        return id;
    }

    public String registerRider(String name, boolean canHandleFragile, double reliability) {
        String id = riderService.registerRider(name, canHandleFragile, reliability);
        assignmentService.assignPackages(packageService.getPackageMap());
        return id;
    }

    public void updateRiderStatus(String riderId, boolean available) {
        riderService.updateRiderStatus(riderId, available,
                packageService.getPackageMap(), pendingQueue);
        assignmentService.assignPackages(packageService.getPackageMap());
    }

    public void simulatePickup(String packageId) {
        packageService.simulatePickup(packageId);
    }

    public void simulateDelivery(String packageId) {
        packageService.simulateDelivery(packageId, riderService);
    }

    public void cancelPackage(String packageId) {
        packageService.cancelPackage(packageId, riderService);
    }

    public void reassignPackage(String packageId) {
        packageService.reassignPackage(packageId);
        assignmentService.assignPackages(packageService.getPackageMap());
    }

    public List<DeliveryPackage> getAllPackages() {
        return packageService.getAllPackages();
    }

    public List<DeliveryPackage> getCancelledPackages() {
        return packageService.getCancelledPackages();
    }

    public List<Rider> getAllRiders() {
        return riderService.getAllRiders();
    }

    public List<DeliveryPackage> getAssignments() {
        return packageService.getAssignments();
    }

    public DeliveryPackage getPackageInfo(String packageId) {
        return packageService.getPackageInfo(packageId);
    }

    public String getPackageStatus(String packageId) {
        return packageService.getPackageStatus(packageId);
    }

    public String getRiderStatus(String riderId) {
        return riderService.getRiderStatus(riderId);
    }

    public List<DeliveryPackage> getMissedExpressDeliveries() {
        return auditService.getMissedExpressDeliveries(packageService.getAllPackages());
    }

    public List<DeliveryPackage> getPackagesDeliveredByRider(String riderId) {
        return auditService.getRiderDeliveryHistory(packageService.getAllPackages(), riderId);
    }
}
