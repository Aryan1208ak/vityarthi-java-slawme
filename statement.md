# Project Statement: Smart Enterprise Warehouse & Supply Chain Management CLI System (SLAWME)

## 1. Problem Statement
Modern logistics centers, distribution hubs, and enterprise warehouses face critical operational challenges in tracking fast-moving inventory, preventing perishable stock wastage, ensuring role-based access control, and executing high-throughput order dispatches concurrently. Traditional inventory tracking methods suffer from human data-entry errors, lack real-time auditability, and fail to provide automated reorder alerts or stream-based multi-category analytics.

SLAWME addresses these challenges by offering a lightweight, zero-dependency, 100% terminal-based Java command-line application that automates warehouse inventory operations, enforces strict access control, processes customer orders asynchronously using multi-threading, and generates comprehensive operational reports.

## 2. Scope of the Project
The scope of SLAWME encompasses end-to-end management of warehouse items, user access permissions, transaction logs, and order dispatches:

- **Authentication & RBAC Scope**: Secure authentication for system administrators, inventory managers, and compliance auditors.
- **Inventory Lifecycle Scope**: Registration, updating, categorizing, low-stock threshold alerting, and expiration tracking for perishable items and electronic hardware.
- **Order Queue & Processing Scope**: Asynchronous priority order dispatching, stock verification, and dynamic total calculation with real-time audit trail recording.
- **Analytics & Export Scope**: Dynamic Streams API aggregation, CSV/JSON report exports, and financial valuation metrics.

## 3. Target Users
1. **Warehouse Administrators (ADMIN)**: Full system privilege to add/remove users, configure reorder thresholds, inspect audit logs, and trigger batch operations.
2. **Inventory Managers (MANAGER)**: Operational privileges to adjust stock levels, register new perishable/electronic items, process customer orders, and view analytics reports.
3. **Compliance Auditors (AUDITOR)**: Read-only access to transaction history, security audit logs, stock valuation summaries, and compliance verification reports.

## 4. High-Level Features
- **Role-Based Access Control (RBAC)**: SHA-256 password hashing with individual salt values and session management.
- **Polymorphic Item Management**: Object-oriented models for `PerishableItem` (temperature requirements, expiration dates) and `ElectronicItem` (voltage, warranty period).
- **Asynchronous Multithreaded Order Dispatch**: Concurrent queue handling order processing with thread-safe stock locks (`ReentrantLock`).
- **Stream API Statistical Analytics**: Fast analytical queries (category stock totals, average prices, top high-value inventory items).
- **File Persistence Engine**: Local JSON/CSV state saving and recovery across sessions.
- **Audit Logging Engine**: Thread-safe logging of all critical system events with timestamps and user identifiers.
