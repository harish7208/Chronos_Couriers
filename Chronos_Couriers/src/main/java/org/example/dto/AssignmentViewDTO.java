package org.example.dto;

import org.example.model.DeliveryPackage;

public class AssignmentViewDTO implements TableView {
    private final DeliveryPackage deliveryPackage;

    public AssignmentViewDTO(DeliveryPackage deliveryPackage) {
        this.deliveryPackage = deliveryPackage;
    }

    @Override
    public void printHeader() {
        System.out.printf("%-10s %-15s %-15s %-10s\n", "Pkg ID", "AssignedRider", "Status", "Priority");
        System.out.println("---------------------------------------------------------");
    }

    @Override
    public void printRow() {
        System.out.printf("%-10s %-15s %-15s %-10s\n", deliveryPackage.getId(), deliveryPackage.getAssignedRiderId(), deliveryPackage.getStatus(), deliveryPackage.getPriority());
    }
}

