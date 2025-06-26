package org.example.dto;

import org.example.model.DeliveryPackage;

public class PackageViewDTO implements TableView {
    private final DeliveryPackage deliveryPackage;

    public PackageViewDTO(DeliveryPackage deliveryPackage) {
        this.deliveryPackage = deliveryPackage;
    }

    @Override
    public void printHeader() {
        System.out.printf("%-10s %-10s %-10s %-15s %-15s %-15s %-10s\n", "ID", "Priority", "Status", "Deadline", "OrderTime", "PickupTime", "DeliveryTime");
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    @Override
    public void printRow() {
        System.out.printf("%-10s %-10s %-10s %-15d %-15d %-15s %-10s\n", deliveryPackage.getId(), deliveryPackage.getPriority(), deliveryPackage.getStatus(), deliveryPackage.getDeadline(), deliveryPackage.getOrderTime(), deliveryPackage.getPickupTime() != null ? deliveryPackage.getPickupTime().toString() : "-", deliveryPackage.getDeliveryTime() != null ? deliveryPackage.getDeliveryTime().toString() : "-");
    }
}
