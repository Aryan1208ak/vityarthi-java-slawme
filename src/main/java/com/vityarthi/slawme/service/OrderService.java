package com.vityarthi.slawme.service;

import com.vityarthi.slawme.exception.InsufficientStockException;
import com.vityarthi.slawme.exception.WarehouseException;
import com.vityarthi.slawme.model.Order;
import com.vityarthi.slawme.model.WarehouseItem;
import com.vityarthi.slawme.util.AuditLogger;

import java.util.*;
import java.util.concurrent.*;

/**
 * Multithreaded Order Dispatch & Queue Processing Service.
 */
public class OrderService {
    private final InventoryService inventoryService;
    private final ExecutorService executorService;
    private final List<Order> processedOrders = new CopyOnWriteArrayList<>();

    public OrderService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        // Multithreaded pool with 4 worker threads for order fulfillment
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public synchronized Order placeOrder(String orderId, String customerName, Map<String, Integer> itemsMap, String actor) throws WarehouseException {
        // Validate stock availability before queuing
        double calculatedTotal = 0.0;
        for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
            String itemId = entry.getKey();
            int reqQty = entry.getValue();
            WarehouseItem item = inventoryService.getItem(itemId);

            if (item == null) {
                throw new WarehouseException("Order failed: Item [" + itemId + "] does not exist");
            }
            if (item.getQuantity() < reqQty) {
                throw new InsufficientStockException(itemId, reqQty, item.getQuantity());
            }
            calculatedTotal += item.getUnitPrice() * reqQty;
        }

        Order order = new Order(orderId, customerName, itemsMap);
        order.setTotalAmount(calculatedTotal);
        processedOrders.add(order);

        AuditLogger.getInstance().log("INFO", actor, "Order Placed: " + order.getAuditSummary());

        // Dispatch order processing to background thread pool
        executorService.submit(() -> processOrderTask(order, actor));

        return order;
    }

    private void processOrderTask(Order order, String actor) {
        order.setStatus(Order.Status.PROCESSING);
        AuditLogger.getInstance().log("INFO", "THREAD-WORKER", "Processing Order ID: " + order.getOrderId());

        try {
            // Deduct stock for each item in order
            for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) {
                WarehouseItem item = inventoryService.getItem(entry.getKey());
                if (item != null) {
                    int newQty = item.getQuantity() - entry.getValue();
                    inventoryService.updateStock(item.getId(), newQty, "ORDER-FULFILLMENT");
                }
            }
            // Simulate dispatch latency
            Thread.sleep(300);
            order.setStatus(Order.Status.COMPLETED);
            AuditLogger.getInstance().log("INFO", "THREAD-WORKER", "Successfully Dispatched Order ID: " + order.getOrderId());
        } catch (Exception e) {
            order.setStatus(Order.Status.FAILED);
            AuditLogger.getInstance().log("ERROR", "THREAD-WORKER", "Failed Order ID: " + order.getOrderId() + " Reason: " + e.getMessage());
        }
    }

    public List<Order> getProcessedOrders() {
        return new ArrayList<>(processedOrders);
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
