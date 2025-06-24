package org.example;

import org.example.dto.PackageViewDTO;
import org.example.dto.AssignmentViewDTO;
import org.example.dto.RiderViewDTO;
import org.example.model.DeliveryPackage;
import org.example.model.PackagePriority;
import org.example.model.PackageStatus;
import org.example.model.Rider;
import org.example.service.DispatchCenter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        DispatchCenter center = new DispatchCenter();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Chronos Couriers Menu ---");
            System.out.println("1. Place Order");
            System.out.println("2. Register Rider");
            System.out.println("3. Update Rider Status");
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

            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Priority (EXPRESS/STANDARD): ");
                    PackagePriority priority = PackagePriority.valueOf(scanner.nextLine().toUpperCase());
                    System.out.print("Deadline (timestamp): ");
                    long deadline = Long.parseLong(scanner.nextLine());
                    System.out.print("Is Fragile (true/false): ");
                    boolean fragile = Boolean.parseBoolean(scanner.nextLine());
                    String packageId = center.placeOrder(priority, deadline, fragile);
                    System.out.println("Package created successfully.");
                    DispatchCenter.printTable(List.of(new PackageViewDTO(center.getPackageInfo(packageId))));
                    break;

                case 2:
                    System.out.print("Rider Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Can Handle Fragile (true/false): ");
                    boolean canFragile = Boolean.parseBoolean(scanner.nextLine());
                    System.out.print("Reliability Rating (0.0 to 1.0): ");
                    double reliability = Double.parseDouble(scanner.nextLine());
                    String riderId = center.registerRider(name, canFragile, reliability);
                    System.out.println("Rider registered successfully.");
                    DispatchCenter.printTable(List.of(new RiderViewDTO(center.getAllRiders().stream()
                            .filter(r -> r.getId().equals(riderId)).findFirst().get())));
                    break;

                case 3:
                    System.out.print("Rider ID: ");
                    String rid = scanner.nextLine();
                    System.out.print("Available (true/false): ");
                    boolean avail = Boolean.parseBoolean(scanner.nextLine());
                    center.updateRiderStatus(rid, avail);
                    break;

                case 4:
                    System.out.print("Package ID: ");
                    String pid = scanner.nextLine();
                    DeliveryPackage dp = center.getPackageInfo(pid);
                    if (dp == null) {
                        System.out.println("Package not found.");
                        break;
                    }
                    if (dp.getStatus() == PackageStatus.ASSIGNED) {
                        System.out.println("1. Pick Up\n2. Cancel");
                        int opt = Integer.parseInt(scanner.nextLine());
                        if (opt == 1) center.simulatePickup(pid);
                        else center.cancelPackage(pid);
                    } else if (dp.getStatus() == PackageStatus.PICKED_UP) {
                        System.out.println("1. Deliver\n2. Cancel");
                        int opt = Integer.parseInt(scanner.nextLine());
                        if (opt == 1) center.simulateDelivery(pid);
                        else center.cancelPackage(pid);
                    } else {
                        System.out.println("Status update not allowed in current state.");
                    }
                    break;

                case 5:
                    DispatchCenter.printTable(center.getAllPackages().stream()
                            .map(PackageViewDTO::new).collect(Collectors.toList()));
                    break;

                case 6:
                    DispatchCenter.printTable(center.getAllRiders().stream()
                            .map(RiderViewDTO::new).collect(Collectors.toList()));
                    break;

                case 7:
                    DispatchCenter.printTable(center.getAssignments().stream()
                            .map(AssignmentViewDTO::new).collect(Collectors.toList()));
                    break;

                case 8:
                    DispatchCenter.printTable(center.getCancelledPackages().stream()
                            .map(PackageViewDTO::new).collect(Collectors.toList()));
                    break;

                case 9:
                    System.out.print("Enter Cancelled Package ID: ");
                    String reid = scanner.nextLine();
                    center.reassignPackage(reid);
                    System.out.println("Reassignment triggered if possible.");
                    break;

                case 10:
                    System.out.print("Package ID: ");
                    String statusPid = scanner.nextLine();
                    String status = center.getPackageStatus(statusPid);
                    DispatchCenter.printTable(List.of(new PackageViewDTO(center.getPackageInfo(statusPid))));
                    break;

                case 11:
                    System.out.print("Rider ID: ");
                    String ridStatus = scanner.nextLine();
                    String riderStatus = center.getRiderStatus(ridStatus);
                    DispatchCenter.printTable(center.getAllRiders().stream()
                            .filter(r -> r.getId().equals(ridStatus))
                            .map(RiderViewDTO::new).collect(Collectors.toList()));
                    break;

                case 12:
                    DispatchCenter.printTable(center.getMissedExpressDeliveries().stream()
                            .map(PackageViewDTO::new).collect(Collectors.toList()));
                    break;

                case 13:
                    System.out.print("Package ID: ");
                    String fullPid = scanner.nextLine();
                    DispatchCenter.printTable(List.of(new PackageViewDTO(center.getPackageInfo(fullPid))));
                    break;

                case 14:
                    System.out.print("Rider ID: ");
                    String historyRid = scanner.nextLine();
                    DispatchCenter.printTable(center.getPackagesDeliveredByRider(historyRid).stream()
                            .map(PackageViewDTO::new).collect(Collectors.toList()));
                    break;

                case 15:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 15);
    }
}
