# Smart Logistics & Automated Warehouse Management Engine (SLAWME)

> **Student Name**: Aryan Dev  
> **Registration Number**: 24BEC10070  
> **Course**: Programming in Java  
> **Platform**: VITyarthi Flipped Course Evaluation — Build Your Own Project  
> **Repository Type**: Public  
> **Executability**: 100% Terminal CLI Executable (Zero GUI Dependencies)

---

## 📌 Project Overview
**SLAWME** is an enterprise-grade, lightweight, high-performance Java Command Line application designed by **Aryan Dev (Reg No: 24BEC10070)** to solve critical challenges in modern supply chain management and automated warehousing. It provides robust inventory tracking for perishable and electronic goods, role-based security access control (RBAC), multithreaded asynchronous order fulfillment, real-time threshold alert monitoring, and Java 22 Stream API analytical reporting.

---

## ✨ Key Features

1. **Role-Based Access Control (RBAC) & Security**:
   - Authentication system with **SHA-256 salted password hashing**.
   - Three distinct security roles: **Administrator (ADMIN)**, **Inventory Manager (MANAGER)**, and **Compliance Auditor (AUDITOR)**.
   - Enforces privilege restrictions across system operations.

2. **Polymorphic Inventory Management**:
   - Built on Object-Oriented principles (**Abstraction**, **Inheritance**, **Encapsulation**, **Polymorphism**).
   - Domain subclasses for **`PerishableItem`** (temperature control, expiration date tracking) and **`ElectronicItem`** (voltage ratings, warranty months).
   - Real-time low-stock threshold alerting (`isLowStock()`) and expiration detection (`isExpired()`).

3. **Asynchronous Multithreaded Order Dispatch Engine**:
   - Order fulfillment managed by a dedicated **`ExecutorService` thread pool** (4 concurrent worker threads).
   - Non-blocking order processing with thread-safe data synchronization using **`ReentrantReadWriteLock`**.

4. **Java Stream API Analytics & Reporting**:
   - Dynamic real-time calculation of total warehouse valuation.
   - Category-wise statistical summary (`DoubleSummaryStatistics`) including stock count, total valuation, and average item value.

5. **Data Persistence & Audit Logging**:
   - Automatic local storage saving/loading via structured `CSV`/`JSON` file managers (`DataManager`).
   - Thread-safe **Singleton Audit Logger** (`AuditLogger`) logging all system events with timestamps and active user identifiers.

---

## 🛠️ Technologies & Tools Used
- **Language**: Java 22 / Java 17+ (Core Java Standard Library)
- **Architecture**: Object-Oriented Design (OOD), SOLID Principles, Design Patterns (Singleton, Factory, Strategy)
- **Concurrency**: `java.util.concurrent` (`ExecutorService`, `CopyOnWriteArrayList`, `ConcurrentHashMap`, `ReentrantReadWriteLock`)
- **Streams API**: `java.util.stream.Collectors`, `DoubleSummaryStatistics`
- **Build & Scripting**: Command-line batch (`build.bat`), Shell script (`build.sh`), Maven (`pom.xml`)
- **Testing**: Standalone Java Assertions & JUnit 5 structure (`TestRunner.java`)

---

## 🚀 Steps to Install & Run the Project

### Prerequisites
- Java JDK 17 or higher (Java 22 recommended) installed on your system.
- Terminal environment (Windows PowerShell / Command Prompt, Linux Terminal, or macOS Terminal).

### Execution Steps (Command Line)

#### Option A: Single-Command Windows Batch Script (Recommended)
```cmd
# 1. Clone the repository
git clone https://github.com/Aryan1208ak/vityarthi-java-slawme.git
cd vityarthi-java-slawme

# 2. Compile and run interactive CLI application
build.bat run

# 3. Or run the automated showcase demo
build.bat demo
```

#### Option B: Single-Command Linux / macOS Shell Script
```bash
# 1. Give execution permission to shell script
chmod +x build.sh

# 2. Run interactive CLI application
./build.sh run

# 3. Or run automated showcase demo
./build.sh demo
```

#### Option C: Standard Java Commands (Without Scripts)
```bash
# 1. Compile source files into bin directory
javac -d bin src/main/java/com/vityarthi/slawme/*.java src/main/java/com/vityarthi/slawme/exception/*.java src/main/java/com/vityarthi/slawme/model/*.java src/main/java/com/vityarthi/slawme/service/*.java src/main/java/com/vityarthi/slawme/util/*.java

# 2. Launch interactive CLI
java -cp bin com.vityarthi.slawme.Main
```

---

## 🔑 Default Login Credentials

| Role | Username | Password | Privileges |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | Full system control, user management, audit logs |
| **Manager** | `manager` | `manager123` | Stock intake, stock updates, order dispatching |
| **Auditor** | `auditor` | `auditor123` | Read-only analytics, valuation reports, audit logs |

---

## 🧪 Instructions for Testing

To run the automated unit test suite covering authentication, stock filters, streams, and role permissions:

```cmd
# On Windows
build.bat test

# On Linux / macOS
./build.sh test

# Via Direct Java Command
javac -cp bin -d bin src/test/java/com/vityarthi/slawme/*.java
java -ea -cp bin com.vityarthi.slawme.TestRunner
```

### Expected Test Output
```text
==================================================
  SLAWME AUTOMATED UNIT TEST SUITE RUNNER         
==================================================
Running AuthServiceTest suite...
✓ All AuthService Tests PASSED!
Running InventoryServiceTest suite...
✓ All InventoryService Tests PASSED!

==================================================
  SUMMARY: ALL UNIT TESTS COMPLETED SUCCESSFULLY!  
==================================================
```

---

## 📊 Sample CLI Output & Showcase

```text
=========================================================================="
    Smart Logistics & Automated Warehouse Management Engine (SLAWME)     "
               VITyarthi Java Flipped Course CLI Application              "
               Developed by: Aryan Dev (Reg No: 24BEC10070)               "
=========================================================================="

==========================================================
 Active User: admin        | Role: Administrator         
==========================================================
 1. View All Warehouse Inventory
 2. Add New Inventory Item (Perishable / Electronic)
 3. Update Item Stock Quantity
 4. Place Customer Order (Asynchronous Dispatch)
 5. View Low-Stock & Expiration Threshold Alerts
 6. Run Stream API Analytical Summary Reports
 7. View System Audit Logs
 8. Logout Current User
 9. Exit System
==========================================================
```
