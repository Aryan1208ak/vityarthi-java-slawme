package com.vityarthi.slawme;

import com.vityarthi.slawme.exception.WarehouseException;
import com.vityarthi.slawme.model.ElectronicItem;
import com.vityarthi.slawme.model.PerishableItem;
import com.vityarthi.slawme.model.WarehouseItem;
import com.vityarthi.slawme.service.InventoryService;

import java.time.LocalDate;
import java.util.List;

/**
 * Unit Test Suite for Inventory Service & Stream API functions.
 */
public class InventoryServiceTest {

    public static void runAllTests() {
        System.out.println("Running InventoryServiceTest suite...");
        testAddAndRetrieveItem();
        testLowStockFiltering();
        testStreamAnalyticsValuation();
        testPerishableExpiryFilter();
        System.out.println("✓ All InventoryService Tests PASSED!");
    }

    private static void testAddAndRetrieveItem() {
        InventoryService service = new InventoryService();
        ElectronicItem item = new ElectronicItem("TEST-01", "Test Laptop", "Hardware", 999.99, 10, 2, 12, 220);
        try {
            service.addItem(item, "TEST-RUNNER");
            WarehouseItem fetched = service.getItem("TEST-01");
            assert fetched != null : "Fetched item should not be null";
            assert "Test Laptop".equals(fetched.getName()) : "Item name mismatch";
        } catch (WarehouseException e) {
            throw new RuntimeException("Test Failed: " + e.getMessage());
        }
    }

    private static void testLowStockFiltering() {
        InventoryService service = new InventoryService();
        ElectronicItem lowStockItem = new ElectronicItem("TEST-LOW", "Low Stock Item", "Hardware", 50.0, 2, 5, 6, 110);
        try {
            service.addItem(lowStockItem, "TEST-RUNNER");
            List<WarehouseItem> lowStock = service.getLowStockItems();
            boolean found = lowStock.stream().anyMatch(i -> "TEST-LOW".equals(i.getId()));
            assert found : "Low stock item should be present in low stock alerts list";
        } catch (WarehouseException e) {
            throw new RuntimeException("Test Failed: " + e.getMessage());
        }
    }

    private static void testStreamAnalyticsValuation() {
        InventoryService service = new InventoryService();
        double val = service.calculateTotalInventoryValue();
        assert val > 0 : "Calculated inventory valuation must be greater than zero";
    }

    private static void testPerishableExpiryFilter() {
        InventoryService service = new InventoryService();
        PerishableItem expired = new PerishableItem("TEST-EXP", "Expired Cheese", "Dairy", 5.0, 10, 2, LocalDate.now().minusDays(2), 4.0);
        try {
            service.addItem(expired, "TEST-RUNNER");
            List<PerishableItem> list = service.getExpiredItems();
            boolean found = list.stream().anyMatch(i -> "TEST-EXP".equals(i.getId()));
            assert found : "Expired item must be returned by getExpiredItems() filter";
        } catch (WarehouseException e) {
            throw new RuntimeException("Test Failed: " + e.getMessage());
        }
    }
}
