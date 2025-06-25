package org.example.model;


public class Rider {
    private final String id;
    private final String name;
    private RiderStatus status;
    private double reliabilityRating;
    private final boolean canHandleFragile;
    private long lastUpdatedTime;
    private int completedOrders;

    public Rider(String id, String name, boolean canHandleFragile, double reliabilityRating) {
        this.id = id;
        this.name = name;
        this.canHandleFragile = canHandleFragile;
        this.reliabilityRating = reliabilityRating;
        this.status = RiderStatus.AVAILABLE;
        this.lastUpdatedTime = System.currentTimeMillis();
        this.completedOrders = 1;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RiderStatus getStatus() {
        return status;
    }

    public void setStatus(RiderStatus status) {
        this.status = status;
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    public boolean canHandleFragile() {
        return canHandleFragile;
    }

    public double getReliabilityRating() {
        return reliabilityRating;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void updateReliability(double newRating) {
        this.reliabilityRating = (this.reliabilityRating * this.completedOrders + newRating) / (this.completedOrders + 1);
        if (this.reliabilityRating < 0) this.reliabilityRating = 0;
        this.completedOrders++;
    }

    public void penalizeReliability(double penalty) {
        this.reliabilityRating = (this.reliabilityRating * this.completedOrders + penalty) / (this.completedOrders + 1);
        if (this.reliabilityRating < 0) this.reliabilityRating = 0;
        this.completedOrders++;
    }

    public void updateRiderStatusOffline(java.util.Map<String, DeliveryPackage> packageMap, java.util.Queue<DeliveryPackage> pendingQueue) {
        for (DeliveryPackage deliveryPackage : packageMap.values()) {
            if (this.id.equals(deliveryPackage.getAssignedRiderId()) && (deliveryPackage.getStatus() == PackageStatus.ASSIGNED || deliveryPackage.getStatus() == PackageStatus.PICKED_UP)) {
                deliveryPackage.setStatus(PackageStatus.FAILED);
                deliveryPackage.setAssignedRiderId(null);
                pendingQueue.offer(deliveryPackage);
            }
        }
        setStatus(RiderStatus.OFFLINE);
    }

    public void updateStatus(boolean available, java.util.Map<String, DeliveryPackage> packageMap, java.util.Queue<DeliveryPackage> pendingQueue) {
        if (!available) {
            updateRiderStatusOffline(packageMap, pendingQueue);
        } else {
            setStatus(RiderStatus.AVAILABLE);
        }
    }
}
