package com.vityarthi.slawme.service;

import com.vityarthi.slawme.exception.WarehouseException;
import com.vityarthi.slawme.model.PerishableItem;
import com.vityarthi.slawme.model.WarehouseItem;
import com.vityarthi.slawme.util.AuditLogger;
import com.vityarthi.slawme.util.DataManager;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Inventory Management & Stream Analytics Service.
 */
public class InventoryService {
    private final Map<String, WarehouseItem> inventoryMap = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public InventoryService() {
        // Load initial state
        List<WarehouseItem> loaded = DataManager.loadInventory();
        for (WarehouseItem item : loaded) {
            inventoryMap.put(item.getId(), item);
        }
    }

    public void addItem(WarehouseItem item, String actor) throws WarehouseException {
        rwLock.writeLock().lock();
        try {
            if (inventoryMap.containsKey(item.getId())) {
                throw new WarehouseException("Item with ID " + item.getId() + " already exists!");
            }
            inventoryMap.put(item.getId(), item);
            AuditLogger.getInstance().log("INFO", actor, "Added Item: " + item.getAuditSummary());
            persistData();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public WarehouseItem getItem(String itemId) {
        rwLock.readLock().lock();
        try {
            return inventoryMap.get(itemId);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void updateStock(String itemId, int newQuantity, String actor) throws WarehouseException {
        rwLock.writeLock().lock();
        try {
            WarehouseItem item = inventoryMap.get(itemId);
            if (item == null) {
                throw new WarehouseException("Item with ID " + itemId + " not found!");
            }
            int oldQty = item.getQuantity();
            item.setQuantity(newQuantity);
            AuditLogger.getInstance().log("INFO", actor,
                    String.format("Updated Stock for [%s]: %d -> %d", itemId, oldQty, newQuantity));
            persistData();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void removeItem(String itemId, String actor) throws WarehouseException {
        rwLock.writeLock().lock();
        try {
            WarehouseItem removed = inventoryMap.remove(itemId);
            if (removed == null) {
                throw new WarehouseException("Item with ID " + itemId + " not found!");
            }
            AuditLogger.getInstance().log("INFO", actor, "Removed Item: " + removed.getAuditSummary());
            persistData();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public List<WarehouseItem> getAllItems() {
        rwLock.readLock().lock();
        try {
            return new ArrayList<>(inventoryMap.values());
        } finally {
            rwLock.readLock().unlock();
        }
    }

    // Java Stream API Analytics Methods
    public List<WarehouseItem> getLowStockItems() {
        rwLock.readLock().lock();
        try {
            return inventoryMap.values().stream()
                    .filter(WarehouseItem::isLowStock)
                    .sorted(Comparator.comparingInt(WarehouseItem::getQuantity))
                    .collect(Collectors.toList());
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public List<PerishableItem> getExpiredItems() {
        rwLock.readLock().lock();
        try {
            return inventoryMap.values().stream()
                    .filter(item -> item instanceof PerishableItem)
                    .map(item -> (PerishableItem) item)
                    .filter(PerishableItem::isExpired)
                    .collect(Collectors.toList());
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public double calculateTotalInventoryValue() {
        rwLock.readLock().lock();
        try {
            return inventoryMap.values().stream()
                    .mapToDouble(WarehouseItem::calculateInventoryValue)
                    .sum();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public Map<String, DoubleSummaryStatistics> getCategoryStatistics() {
        rwLock.readLock().lock();
        try {
            return inventoryMap.values().stream()
                    .collect(Collectors.groupingBy(
                            WarehouseItem::getCategory,
                            Collectors.summarizingDouble(WarehouseItem::calculateInventoryValue)
                    ));
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public List<WarehouseItem> searchByNameOrCategory(String query) {
        String lq = query.toLowerCase();
        rwLock.readLock().lock();
        try {
            return inventoryMap.values().stream()
                    .filter(i -> i.getName().toLowerCase().contains(lq) || i.getCategory().toLowerCase().contains(lq))
                    .collect(Collectors.toList());
        } finally {
            rwLock.readLock().unlock();
        }
    }

    private void persistData() {
        try {
            DataManager.saveInventory(inventoryMap.values());
        } catch (IOException e) {
            AuditLogger.getInstance().log("ERROR", "SYSTEM", "Failed to persist inventory: " + e.getMessage());
        }
    }
}
