package org.example.util;


import org.example.dto.AssignmentViewDTO;
import org.example.dto.PackageViewDTO;
import org.example.dto.RiderViewDTO;
import org.example.model.DeliveryPackage;
import org.example.model.PackagePriority;
import org.example.model.PackageStatus;
import org.example.service.DispatchCenter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuHandler {
    private final DispatchCenter dispatchCenter;
    private final Scanner scanner;

    public MenuHandler(DispatchCenter dispatchCenter, Scanner scanner) {
        this.dispatchCenter = dispatchCenter;
        this.scanner = scanner;
    }

    public void displayMenu() {
        System.out.println("\n--- Chronos Couriers Menu ---");
        System.out.println("1. Place Order");
        System.out.println("2. Register Rider");
        System.out.println("3. Update Rider Availability Status");
        System.out.println("4. Update Package Status");
        System.out.println("5. View All Packages");
        System.out.println("6. View All Riders");
        System.out.println("7. View All Assignments");
        System.out.println("8. View Cancelled Orders");
        System.out.println("9. Reassign Cancelled Package");
        System.out.println("10. Get Package Status");
        System.out.println("11. Get Rider Status");
        System.out.println("12. Audit Missed Express Deliveries");
        System.out.println("13. Get Full Package Info");
        System.out.println("14. Get Rider Delivery History");
        System.out.println("15. Exit");
        System.out.print("Choose an option: ");
    }

    public boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                placeOrder();
                break;
            case 2:
                registerRider();
                break;
            case 3:
                updateRiderStatus();
                break;
            case 4:
                updatePackageStatus();
                break;
            case 5:
                DispatchCenter.printTable(dispatchCenter.getAllPackages().stream().map(PackageViewDTO::new).collect(Collectors.toList()));
                break;
            case 6:
                DispatchCenter.printTable(dispatchCenter.getAllRiders().stream().map(RiderViewDTO::new).collect(Collectors.toList()));
                break;
            case 7:
                DispatchCenter.printTable(dispatchCenter.getAssignments().stream().map(AssignmentViewDTO::new).collect(Collectors.toList()));
                break;
            case 8:
                DispatchCenter.printTable(dispatchCenter.getCancelledPackages().stream().map(PackageViewDTO::new).collect(Collectors.toList()));
                break;
            case 9:
                System.out.print("Enter Cancelled Package ID: ");
                dispatchCenter.reassignPackage(scanner.nextLine());
                System.out.println("Reassignment triggered if possible.");
                break;
            case 10:
                System.out.print("Package ID: ");
                System.out.println(dispatchCenter.getPackageStatus(scanner.nextLine()));
                break;
            case 11:
                System.out.print("Rider ID: ");
                System.out.println(dispatchCenter.getRiderStatus(scanner.nextLine()));
                break;
            case 12:
                DispatchCenter.printTable(dispatchCenter.getMissedExpressDeliveries().stream().map(PackageViewDTO::new).collect(Collectors.toList()));
                break;
            case 13:
                System.out.print("Package ID: ");
                DispatchCenter.printTable(List.of(new PackageViewDTO(dispatchCenter.getPackageInfo(scanner.nextLine()))));
                break;
            case 14:
                System.out.print("Rider ID: ");
                DispatchCenter.printTable(dispatchCenter.getPackagesDeliveredByRider(scanner.nextLine()).stream().map(PackageViewDTO::new).collect(Collectors.toList()));
                break;
            case 15:
                System.out.println("Exiting...");
                return false;
            default:
                System.out.println("Invalid option.");
        }
        return true;
    }

    private void placeOrder() {
        PackagePriority priority = InputValidator.readValidPriority(scanner);
        long deadline = InputValidator.readValidDeadline(scanner);
        boolean fragile = InputValidator.readBoolean(scanner, "Is Fragile");
        String packageId = dispatchCenter.placeOrder(priority, deadline, fragile);
        System.out.println("Package created successfully.");
        DispatchCenter.printTable(List.of(new PackageViewDTO(dispatchCenter.getPackageInfo(packageId))));
    }

    private void registerRider() {
        System.out.print("Rider Name: ");
        String name = scanner.nextLine();

        boolean canFragile = InputValidator.readBoolean(scanner, "Can Handle Fragile");

        double reliability = InputValidator.readReliabilityRating(scanner);

        String riderId = dispatchCenter.registerRider(name, canFragile, reliability);
        System.out.println("Rider registered successfully.");

        DispatchCenter.printTable(List.of(
                new RiderViewDTO(dispatchCenter.getAllRiders().stream()
                        .filter(r -> r.getId().equals(riderId))
                        .findFirst()
                        .get())
        ));
    }


    private void updateRiderStatus() {
        System.out.print("Rider ID: ");
        String riderId = scanner.nextLine();
        boolean isAvailable = InputValidator.readBoolean(scanner, "Set Availability (true = Available, false = Offline)");
        dispatchCenter.updateRiderStatus(riderId, isAvailable);
        System.out.println("Rider status updated. Reassignments triggered if needed.");
    }

    private void updatePackageStatus() {
        System.out.print("Package ID: ");
        String packageId = scanner.nextLine();
        DeliveryPackage deliveryPackage = dispatchCenter.getPackageInfo(packageId);
        if (deliveryPackage == null) {
            System.out.println("Package not found.");
            return;
        }
        if (deliveryPackage.getStatus() == PackageStatus.ASSIGNED) {
            System.out.println("1. Pick Up\n2. Cancel");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) dispatchCenter.simulatePickup(packageId);
            else dispatchCenter.cancelPackage(packageId);
        } else if (deliveryPackage.getStatus() == PackageStatus.PICKED_UP) {
            System.out.println("1. Deliver\n2. Cancel");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) dispatchCenter.simulateDelivery(packageId);
            else dispatchCenter.cancelPackage(packageId);
        } else {
            System.out.println("Status update not allowed in current state.");
        }
    }
}

