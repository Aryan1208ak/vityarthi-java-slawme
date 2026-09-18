package com.vityarthi.slawme;

import com.vityarthi.slawme.exception.AuthenticationException;
import com.vityarthi.slawme.exception.WarehouseException;
import com.vityarthi.slawme.model.*;
import com.vityarthi.slawme.service.AuthService;
import com.vityarthi.slawme.service.InventoryService;
import com.vityarthi.slawme.service.OrderService;
import com.vityarthi.slawme.util.AuditLogger;

import java.time.LocalDate;
import java.util.*;

/**
 * Main Command Line Interface (CLI) Entry Point for SLAWME.
 */
public class Main {
    // ANSI Color Codes for Rich Terminal Display
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";
    public static final String BOLD = "\u001B[1m";

    private static final AuthService authService = new AuthService();
    private static final InventoryService inventoryService = new InventoryService();
    private static final OrderService orderService = new OrderService(inventoryService);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Handle non-interactive automated CLI flags
        if (args.length > 0) {
            handleCliFlags(args);
            orderService.shutdown();
            return;
        }

        printBanner();
        loginFlow();

        boolean running = true;
        while (running) {
            if (!authService.isAuthenticated()) {
                loginFlow();
                if (!authService.isAuthenticated()) {
                    break;
                }
            }

            printMenu();
            System.out.print(BOLD + CYAN + "Select Option [1-9]: " + RESET);
            String input = scanner.nextLine().trim();

            try {
                switch (input) {
                    case "1":
                        viewAllInventory();
                        break;
                    case "2":
                        addNewItem();
                        break;
                    case "3":
                        updateItemStock();
                        break;
                    case "4":
                        placeCustomerOrder();
                        break;
                    case "5":
                        viewLowStockAlerts();
                        break;
                    case "6":
                        viewStreamAnalytics();
                        break;
                    case "7":
                        viewAuditLogs();
                        break;
                    case "8":
                        authService.logout();
                        System.out.println(GREEN + "✓ Logged out successfully." + RESET);
                        loginFlow();
                        break;
                    case "9":
                        running = false;
                        break;
                    default:
                        System.out.println(RED + "Invalid option. Please enter a number from 1 to 9." + RESET);
                }
            } catch (AuthenticationException e) {
                System.out.println(RED + "❌ Security Exception: " + e.getMessage() + RESET);
            } catch (WarehouseException e) {
                System.out.println(RED + "❌ Warehouse Exception: " + e.getMessage() + RESET);
            } catch (Exception e) {
                System.out.println(RED + "❌ Unexpected Error: " + e.getMessage() + RESET);
            }
        }

        orderService.shutdown();
        System.out.println(BOLD + GREEN + "\nThank you for using SLAWME. System Shutdown Complete." + RESET);
    }

    private static void handleCliFlags(String[] args) {
        String flag = args[0];
        System.out.println(BOLD + CYAN + "=== SLAWME Automated CLI Mode ===" + RESET);
        if ("--demo".equalsIgnoreCase(flag) || "-d".equalsIgnoreCase(flag)) {
            runAutomatedDemo();
        } else if ("--status".equalsIgnoreCase(flag)) {
            System.out.println(GREEN + "System Status: OPERATIONAL" + RESET);
            System.out.printf("Total Inventory Items: %d%n", inventoryService.getAllItems().size());
            System.out.printf("Total Inventory Valuation: $%.2f%n", inventoryService.calculateTotalInventoryValue());
        } else {
            System.out.println("Available CLI flags: --demo (Runs automated showcase), --status (Displays health overview)");
        }
    }

    private static void runAutomatedDemo() {
        try {
            System.out.println(YELLOW + "--> Authenticating as Admin..." + RESET);
            authService.login("admin", "admin123");
            System.out.println(GREEN + "✓ Authenticated as ADMIN" + RESET);

            System.out.println(YELLOW + "\n--> Adding new Electronic Item [E999]..." + RESET);
            ElectronicItem item = new ElectronicItem("E999", "Automated Drone", "Robotics", 1299.99, 10, 3, 24, 24);
            inventoryService.addItem(item, "admin");
            System.out.println(GREEN + "✓ Added Item: " + item.getItemDetails() + RESET);

            System.out.println(YELLOW + "\n--> Executing Asynchronous Customer Order..." + RESET);
            Map<String, Integer> orderMap = new HashMap<>();
            orderMap.put("E999", 2);
            orderMap.put("P101", 5);
            Order order = orderService.placeOrder("ORD-DEMO-001", "Acme Corp", orderMap, "admin");
            System.out.println(GREEN + "✓ Order Placed: " + order.getAuditSummary() + RESET);

            Thread.sleep(500); // Wait for thread dispatch

            System.out.println(YELLOW + "\n--> Running Stream API Category Valuation Analytics..." + RESET);
            inventoryService.getCategoryStatistics().forEach((cat, stat) -> {
                System.out.printf("Category: %-15s | Count: %2d | Total Value: $%.2f%n", cat, stat.getCount(), stat.getSum());
            });

            System.out.println(BOLD + GREEN + "\n✓ Automated Demo Execution Completed Successfully!" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "Demo Failed: " + e.getMessage() + RESET);
        }
    }

    private static void printBanner() {
        System.out.println(BOLD + CYAN + "==========================================================================" + RESET);
        System.out.println(BOLD + PURPLE + "    Smart Logistics & Automated Warehouse Management Engine (SLAWME)     " + RESET);
        System.out.println(BOLD + CYAN + "               VITyarthi Java Flipped Course CLI Application              " + RESET);
        System.out.println(BOLD + YELLOW + "              Student: Aryan Dev | Reg No: 24BEC10070                     " + RESET);
        System.out.println(BOLD + CYAN + "==========================================================================" + RESET);
    }

    private static void loginFlow() {
        System.out.println(BOLD + YELLOW + "\n=== SYSTEM LOGIN REQUIRED ===" + RESET);
        System.out.println("Default Demo Credentials:");
        System.out.println(" - Admin:   username: " + BOLD + "admin" + RESET + "    | password: " + BOLD + "admin123" + RESET);
        System.out.println(" - Manager: username: " + BOLD + "manager" + RESET + "  | password: " + BOLD + "manager123" + RESET);
        System.out.println(" - Auditor: username: " + BOLD + "auditor" + RESET + "  | password: " + BOLD + "auditor123" + RESET);

        int attempts = 0;
        while (!authService.isAuthenticated() && attempts < 3) {
            attempts++;
            System.out.print("\nEnter Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();

            try {
                User user = authService.login(username, password);
                System.out.println(BOLD + GREEN + "\n✓ Welcome back, " + user.getUsername() + "! Role: [" + user.getRole().getDisplayName() + "]" + RESET);
                return;
            } catch (AuthenticationException e) {
                System.out.println(RED + "❌ " + e.getMessage() + " (Attempt " + attempts + "/3)" + RESET);
            }
        }

        if (!authService.isAuthenticated()) {
            System.out.println(RED + "Maximum login attempts exceeded. Exiting." + RESET);
            System.exit(1);
        }
    }

    private static void printMenu() {
        User u = authService.getCurrentUser();
        System.out.println(BOLD + CYAN + "\n==========================================================" + RESET);
        System.out.printf(" Active User: " + BOLD + GREEN + "%-12s" + RESET + " | Role: " + BOLD + YELLOW + "%-20s" + RESET + "%n",
                u.getUsername(), u.getRole().getDisplayName());
        System.out.println(BOLD + CYAN + "==========================================================" + RESET);
        System.out.println(" 1. View All Warehouse Inventory");
        System.out.println(" 2. Add New Inventory Item (Perishable / Electronic)");
        System.out.println(" 3. Update Item Stock Quantity");
        System.out.println(" 4. Place Customer Order (Asynchronous Dispatch)");
        System.out.println(" 5. View Low-Stock & Expiration Threshold Alerts");
        System.out.println(" 6. Run Stream API Analytical Summary Reports");
        System.out.println(" 7. View System Audit Logs");
        System.out.println(" 8. Logout Current User");
        System.out.println(" 9. Exit System");
        System.out.println(BOLD + CYAN + "==========================================================" + RESET);
    }

    private static void viewAllInventory() {
        System.out.println(BOLD + YELLOW + "\n--- CURRENT WAREHOUSE INVENTORY LIST ---" + RESET);
        List<WarehouseItem> items = inventoryService.getAllItems();
        if (items.isEmpty()) {
            System.out.println("No items in warehouse.");
            return;
        }
        for (WarehouseItem i : items) {
            System.out.println(i.getItemDetails());
        }
        System.out.printf(BOLD + GREEN + "Total Items: %d | Total Inventory Valuation: $%.2f%n" + RESET,
                items.size(), inventoryService.calculateTotalInventoryValue());
    }

    private static void addNewItem() throws WarehouseException {
        authService.requireRole(Role.MANAGER);
        System.out.println(BOLD + YELLOW + "\n--- ADD NEW WAREHOUSE ITEM ---" + RESET);
        System.out.print("Select Item Type (1: Perishable, 2: Electronic): ");
        String typeChoice = scanner.nextLine().trim();

        System.out.print("Enter Item ID (e.g. P200 / E300): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Item Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine().trim();
        System.out.print("Enter Unit Price ($): ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter Initial Stock Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Reorder Threshold Level: ");
        int reorder = Integer.parseInt(scanner.nextLine().trim());

        if ("1".equals(typeChoice)) {
            System.out.print("Enter Expiration Date (YYYY-MM-DD): ");
            LocalDate expiry = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("Enter Storage Temp (°C): ");
            double temp = Double.parseDouble(scanner.nextLine().trim());
            PerishableItem p = new PerishableItem(id, name, category, price, qty, reorder, expiry, temp);
            inventoryService.addItem(p, authService.getCurrentUser().getUsername());
        } else if ("2".equals(typeChoice)) {
            System.out.print("Enter Warranty Period (Months): ");
            int warranty = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Voltage Rating (V): ");
            int voltage = Integer.parseInt(scanner.nextLine().trim());
            ElectronicItem e = new ElectronicItem(id, name, category, price, qty, reorder, warranty, voltage);
            inventoryService.addItem(e, authService.getCurrentUser().getUsername());
        } else {
            System.out.println(RED + "Invalid item type choice." + RESET);
            return;
        }
        System.out.println(BOLD + GREEN + "✓ Item added successfully!" + RESET);
    }

    private static void updateItemStock() throws WarehouseException {
        authService.requireRole(Role.MANAGER);
        System.out.println(BOLD + YELLOW + "\n--- UPDATE ITEM STOCK QUANTITY ---" + RESET);
        System.out.print("Enter Item ID to Update: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter New Stock Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine().trim());

        inventoryService.updateStock(id, qty, authService.getCurrentUser().getUsername());
        System.out.println(BOLD + GREEN + "✓ Stock updated successfully!" + RESET);
    }

    private static void placeCustomerOrder() throws WarehouseException {
        authService.requireRole(Role.MANAGER);
        System.out.println(BOLD + YELLOW + "\n--- PLACE ASYNCHRONOUS CUSTOMER ORDER ---" + RESET);
        System.out.print("Enter Customer Name: ");
        String cust = scanner.nextLine().trim();
        String orderId = "ORD-" + System.currentTimeMillis() % 10000;

        Map<String, Integer> itemMap = new HashMap<>();
        boolean adding = true;
        while (adding) {
            System.out.print("Enter Item ID: ");
            String itemId = scanner.nextLine().trim();
            System.out.print("Enter Quantity Needed: ");
            int req = Integer.parseInt(scanner.nextLine().trim());
            itemMap.put(itemId, req);

            System.out.print("Add another item to order? (y/n): ");
            String ans = scanner.nextLine().trim();
            if (!ans.equalsIgnoreCase("y")) {
                adding = false;
            }
        }

        Order order = orderService.placeOrder(orderId, cust, itemMap, authService.getCurrentUser().getUsername());
        System.out.println(BOLD + GREEN + "✓ Order Placed and Dispatched to Thread Queue! Order Summary: " + order.getAuditSummary() + RESET);
    }

    private static void viewLowStockAlerts() {
        System.out.println(BOLD + YELLOW + "\n--- LOW-STOCK & EXPIRATION ALERTS ---" + RESET);
        List<WarehouseItem> lowStock = inventoryService.getLowStockItems();
        System.out.println(BOLD + RED + "Low Stock Items (Qty <= Reorder Threshold):" + RESET);
        if (lowStock.isEmpty()) {
            System.out.println(GREEN + "  None! All inventory levels healthy." + RESET);
        } else {
            lowStock.forEach(i -> System.out.println("  ⚠️ " + i.getItemDetails()));
        }

        List<PerishableItem> expired = inventoryService.getExpiredItems();
        System.out.println(BOLD + RED + "\nExpired Perishable Items:" + RESET);
        if (expired.isEmpty()) {
            System.out.println(GREEN + "  None! All perishable items unexpired." + RESET);
        } else {
            expired.forEach(i -> System.out.println("  ❌ " + i.getItemDetails()));
        }
    }

    private static void viewStreamAnalytics() {
        System.out.println(BOLD + YELLOW + "\n--- JAVA STREAM API ANALYTICAL REPORT ---" + RESET);
        System.out.printf("Overall Total Inventory Valuation: $%.2f%n%n", inventoryService.calculateTotalInventoryValue());

        System.out.println(BOLD + CYAN + "Valuation Statistics Grouped by Category:" + RESET);
        inventoryService.getCategoryStatistics().forEach((cat, stats) -> {
            System.out.printf("  Category: %-15s | Count: %2d | Total Value: $%-10.2f | Avg Value/Item: $%.2f%n",
                    cat, stats.getCount(), stats.getSum(), stats.getAverage());
        });
    }

    private static void viewAuditLogs() {
        System.out.println(BOLD + YELLOW + "\n--- RECENT SYSTEM AUDIT LOGS ---" + RESET);
        List<String> logs = AuditLogger.getInstance().getRecentLogs(15);
        if (logs.isEmpty()) {
            System.out.println("No audit logs recorded.");
        } else {
            logs.forEach(System.out::println);
        }
    }
}
