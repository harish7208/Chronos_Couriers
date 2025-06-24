package org.example.dto;

import org.example.model.Rider;
import org.example.model.TableView;

public class RiderViewDTO implements TableView {
    private final Rider rider;

    public RiderViewDTO(Rider rider) {
        this.rider = rider;
    }

    @Override
    public void printHeader() {
        System.out.printf("%-10s %-15s %-10s %-10s %-15s\n",
                "ID", "Name", "Available", "Fragile", "Rating");
        System.out.println("--------------------------------------------------------------");
    }

    @Override
    public void printRow() {
        System.out.printf("%-10s %-15s %-10s %-10s %-15.2f\n",
                rider.getId(),
                rider.getName(),
                rider.isAvailable(),
                rider.canHandleFragile(),
                rider.getReliabilityRating());
    }
}