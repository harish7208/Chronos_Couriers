package org.example.dto;

import org.example.model.DeliveryPackage;
import org.example.model.TableView;

public class PackageViewDTO implements TableView {
    private final DeliveryPackage pkg;

    public PackageViewDTO(DeliveryPackage pkg) {
        this.pkg = pkg;
    }

    @Override
    public void printHeader() {
        System.out.printf("%-10s %-10s %-10s %-15s %-15s %-15s %-10s\n",
                "ID", "Priority", "Status", "Deadline", "OrderTime", "PickupTime", "DeliveryTime");
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    @Override
    public void printRow() {
        System.out.printf("%-10s %-10s %-10s %-15d %-15d %-15s %-10s\n",
                pkg.getId(),
                pkg.getPriority(),
                pkg.getStatus(),
                pkg.getDeadline(),
                pkg.getOrderTime(),
                pkg.getPickupTime() != null ? pkg.getPickupTime().toString() : "-",
                pkg.getDeliveryTime() != null ? pkg.getDeliveryTime().toString() : "-");
    }
}
