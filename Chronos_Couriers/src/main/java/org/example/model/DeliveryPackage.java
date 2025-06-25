package org.example.model;

public class DeliveryPackage {
    private final String id;
    private final PackagePriority priority;
    private final long deadline;
    private final long orderTime;
    private final boolean fragile;
    private PackageStatus status;
    private Long pickupTime;
    private Long deliveryTime;
    private String assignedRiderId;

    public DeliveryPackage(String id, PackagePriority priority, long deadline, long orderTime, boolean fragile) {
        this.id = id;
        this.priority = priority;
        this.deadline = deadline;
        this.orderTime = orderTime;
        this.fragile = fragile;
        this.status = PackageStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public PackagePriority getPriority() {
        return priority;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public boolean isFragile() {
        return fragile;
    }

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    public Long getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Long pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getAssignedRiderId() {
        return assignedRiderId;
    }

    public void setAssignedRiderId(String assignedRiderId) {
        this.assignedRiderId = assignedRiderId;
    }
}