package org.example.model;

import java.util.Map;
import java.util.Queue;

public class Rider {
    private String id;
    private String name;
    private RiderStatus status;
    private double reliabilityRating;
    private boolean canHandleFragile;
    private long lastUpdatedTime;

    public Rider(String id, String name, boolean canHandleFragile, double reliabilityRating) {
        this.id = id;
        this.name = name;
        this.canHandleFragile = canHandleFragile;
        this.reliabilityRating = reliabilityRating;
        this.status = RiderStatus.AVAILABLE;
        this.lastUpdatedTime = System.currentTimeMillis();
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

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void updateStatus(boolean available, Map<String, DeliveryPackage> packageMap, Queue<DeliveryPackage> pendingQueue) {
        if (!available) {
            goOfflineGracefully(packageMap, pendingQueue);
        } else {
            setStatus(RiderStatus.AVAILABLE);
        }
    }

    public void goOfflineGracefully(Map<String, DeliveryPackage> packageMap, Queue<DeliveryPackage> pendingQueue) {
        for (DeliveryPackage p : packageMap.values()) {
            if (this.id.equals(p.getAssignedRiderId()) && (p.getStatus() == PackageStatus.ASSIGNED || p.getStatus() == PackageStatus.PICKED_UP)) {
                p.setStatus(PackageStatus.FAILED);
                p.setAssignedRiderId(null);
                pendingQueue.offer(p);
            }
        }
        setStatus(RiderStatus.OFFLINE);
    }

}