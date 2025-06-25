# Chronos Couriers - Java CLI Courier Dispatch System

Chronos Couriers is a console-based Java application designed to simulate a package dispatch and delivery system. It features rider registration, intelligent rider assignment, package tracking, reliability scoring, and more—all with a clean, modular architecture.

---

## 🚀 Features

- 📦 **Auto-generated Package IDs**
- 🧍‍♂️ **Auto-generated Rider IDs**
- 🛵 Assign riders based on:
  - Fragile support
  - Reliability rating (1.0–10.0 scale)
- 🔁 Rider reliability updated automatically:
  - +10.0: Before deadline
  - +7.0: After deadline
  - −2.0: Cancellation
- 🧠 Menu-driven CLI with 15 clear options
- 📊 Tabular CLI output for all entities
- ✅ Unit-tested services with JUnit 5

---

<details> <summary><strong>📁 Project Structure</strong> (click to expand)</summary>

src/
├── main/
│   ├── java/
│   │   └── org/
│   │       └── example/
│   │           ├── model/           # Rider, DeliveryPackage, enums
│   │           ├── dto/             # DTOs for views
│   │           ├── service/         # RiderService, PackageService, DispatchCenter, AssignmentService, AuditService
│   │           └── util/            # Main.java, MenuHandler.java, InputValidator.java
│   └── resources/
│
└── test/
    └── java/
        └── org/
            └── example/
                └── service/
                    ├── AssignmentServiceTest.java
                    ├── DispatchCenterTest.java
                    ├── PackageServiceTest.java
                    └── RiderServiceTest.java
</details>


---

## 🖥️ Menu Options

The CLI provides the following:
1. Place Order
2. Register Rider
3. Update Rider Availability Status
4. Update Package Status (Pickup/Deliver/Cancel)
5. View All Packages
6. View All Riders
7. View All Assignments
8. View Cancelled Orders
9. Reassign Cancelled Package
10. Get Package Status
11. Get Rider Status
12. Audit Missed Express Deliveries
13. Get Full Package Info
14. Get Rider Delivery History (24h)
15. Exit


## 🛠️ How to Run

1. Open in IntelliJ, Eclipse, or any Java IDE.
2. Run `Main.java` located in `org.example.util`.
3. Use the terminal-based menu to interact with the system.

---

## 🧪 Running Tests

Ensure you have JUnit 5 configured. Then run all tests using:

```bash
mvn test
# or
gradle test

