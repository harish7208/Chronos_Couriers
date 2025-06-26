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

## 📁 Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── org/
│   │       └── example/
│   │           ├── model/           → Rider, DeliveryPackage, Enums
│   │           ├── dto/             → TableView DTOs
│   │           ├── service/         → RiderService, PackageService, DispatchCenter, AssignmentService, AuditService
│   │           └── util/            → Main.java, MenuHandler.java, InputValidator.java
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
 ```


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


---

## 📷 Screenshots

Couple of Sample Outputs 

To View All available Riders : 

![image (1)](https://github.com/user-attachments/assets/a14c5cfb-1052-4189-a483-89ac50bdf13c)

To Place Order : 

![image](https://github.com/user-attachments/assets/64828e9a-4bbf-4549-bd70-962035875fb9)

To check Package Assignment : 

![image (1)](https://github.com/user-attachments/assets/b2990509-8ea3-44ff-9a7a-695882f3d89c)

To check Rider Status : 

![image (2)](https://github.com/user-attachments/assets/8efca1dc-e12e-4834-895d-c288c5893036)

![image (2)](https://github.com/user-attachments/assets/56450109-6c32-4b5f-959d-d0e1cf006521)

To Update Package Status : 

![image (3)](https://github.com/user-attachments/assets/1a71341a-8ceb-4d35-b8bc-5df06a577508)

![image (4)](https://github.com/user-attachments/assets/b51bc108-c1e2-4900-886a-2b3d36969641)

![image (5)](https://github.com/user-attachments/assets/fbc323b1-25f8-4bd5-9e5b-9535e7dd800c)

![image (3)](https://github.com/user-attachments/assets/324624e2-67be-43dd-9956-006686cf4b9b)

After Successful Delivery, Rating of Rider will update : 

![image (6)](https://github.com/user-attachments/assets/e0d41dbe-f4f3-4521-b2c9-7e0b818e609a)

To Update Rider Availability Status : 

![image (4)](https://github.com/user-attachments/assets/8ccd2a26-7a72-495f-a539-a678a4601b6d)

![image (5)](https://github.com/user-attachments/assets/c622e767-dae3-408a-a476-79a083f7d334)

To Register Rider : 

![image (7)](https://github.com/user-attachments/assets/a3f371db-f19d-4060-84f4-04f81ca54c49)



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

