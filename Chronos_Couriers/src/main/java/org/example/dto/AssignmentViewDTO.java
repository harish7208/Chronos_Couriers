package org.example.dto;

import org.example.model.DeliveryPackage;
import org.example.model.TableView;

public class AssignmentViewDTO implements TableView {
    private final DeliveryPackage pkg;

    public AssignmentViewDTO(DeliveryPackage pkg) {
        this.pkg = pkg;
    }

    @Override
    public void printHeader() {
        System.out.printf("%-10s %-15s %-15s %-10s\n",
                "Pkg ID", "AssignedRider", "Status", "Priority");
        System.out.println("---------------------------------------------------------");
    }

    @Override
    public void printRow() {
        System.out.printf("%-10s %-15s %-15s %-10s\n",
                pkg.getId(),
                pkg.getAssignedRiderId(),
                pkg.getStatus(),
                pkg.getPriority());
    }
}

