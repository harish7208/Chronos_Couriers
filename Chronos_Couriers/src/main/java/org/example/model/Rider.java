package org.example.model;

public class Rider {
    private String id;
    private String name;
    private boolean available;
    private double reliabilityRating;
    private boolean canHandleFragile;
    private long lastUpdatedTime;

    public Rider(String id, String name, boolean canHandleFragile, double reliabilityRating) {
        this.id = id;
        this.name = name;
        this.available = true;
        this.canHandleFragile = canHandleFragile;
        this.reliabilityRating = reliabilityRating;
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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
}