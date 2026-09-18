package com.vityarthi.slawme.util;

import com.vityarthi.slawme.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Persistence Data Manager handling file loading & export operations.
 */
public class DataManager {
    private static final String DATA_DIR = "data";
    private static final String INVENTORY_FILE = "data/inventory.csv";

    public static void initializeStorage() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void saveInventory(Collection<WarehouseItem> items) throws IOException {
        initializeStorage();
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            writer.println("TYPE,ID,NAME,CATEGORY,PRICE,QTY,REORDER,PARAM1,PARAM2");
            for (WarehouseItem item : items) {
                if (item instanceof PerishableItem) {
                    PerishableItem p = (PerishableItem) item;
                    writer.printf("PERISHABLE,%s,%s,%s,%.2f,%d,%d,%s,%.1f%n",
                            p.getId(), p.getName(), p.getCategory(), p.getUnitPrice(),
                            p.getQuantity(), p.getReorderLevel(), p.getExpiryDate(), p.getStorageTempCelsius());
                } else if (item instanceof ElectronicItem) {
                    ElectronicItem e = (ElectronicItem) item;
                    writer.printf("ELECTRONIC,%s,%s,%s,%.2f,%d,%d,%d,%d%n",
                            e.getId(), e.getName(), e.getCategory(), e.getUnitPrice(),
                            e.getQuantity(), e.getReorderLevel(), e.getWarrantyMonths(), e.getVoltageRating());
                }
            }
        }
    }

    public static List<WarehouseItem> loadInventory() {
        initializeStorage();
        List<WarehouseItem> items = new ArrayList<>();
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) {
            return getSeedData();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 9) continue;

                String type = parts[0];
                String id = parts[1];
                String name = parts[2];
                String category = parts[3];
                double price = Double.parseDouble(parts[4]);
                int qty = Integer.parseInt(parts[5]);
                int reorder = Integer.parseInt(parts[6]);

                if ("PERISHABLE".equalsIgnoreCase(type)) {
                    LocalDate expiry = LocalDate.parse(parts[7]);
                    double temp = Double.parseDouble(parts[8]);
                    items.add(new PerishableItem(id, name, category, price, qty, reorder, expiry, temp));
                } else if ("ELECTRONIC".equalsIgnoreCase(type)) {
                    int warranty = Integer.parseInt(parts[7]);
                    int voltage = Integer.parseInt(parts[8]);
                    items.add(new ElectronicItem(id, name, category, price, qty, reorder, warranty, voltage));
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Loading fallback seed inventory data due to: " + e.getMessage());
            return getSeedData();
        }
        return items.isEmpty() ? getSeedData() : items;
    }

    public static List<WarehouseItem> getSeedData() {
        List<WarehouseItem> seed = new ArrayList<>();
        seed.add(new PerishableItem("P101", "Organic Milk", "Dairy", 3.49, 120, 20, LocalDate.now().plusDays(7), 4.0));
        seed.add(new PerishableItem("P102", "Fresh Apples", "Produce", 1.99, 45, 50, LocalDate.now().plusDays(12), 10.0));
        seed.add(new PerishableItem("P103", "Artisan Cheese", "Dairy", 8.99, 15, 20, LocalDate.now().plusDays(3), 2.5));
        seed.add(new ElectronicItem("E201", "Dell Monitor 27in", "Hardware", 249.99, 30, 10, 24, 220));
        seed.add(new ElectronicItem("E202", "Logitech MX Mouse", "Peripherals", 99.50, 8, 15, 12, 5));
        seed.add(new ElectronicItem("E203", "Cisco Router X1", "Networking", 399.00, 12, 5, 36, 110));
        return seed;
    }
}
