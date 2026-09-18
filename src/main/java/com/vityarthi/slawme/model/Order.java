package com.vityarthi.slawme.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Domain entity representing customer order for warehouse items.
 */
public class Order implements Auditable {
    public enum Status { PENDING, PROCESSING, COMPLETED, FAILED }

    private final String orderId;
    private final String customerName;
    private final Map<String, Integer> items; // ItemId -> Quantity
    private final LocalDateTime timestamp;
    private Status status;
    private double totalAmount;

    public Order(String orderId, String customerName, Map<String, Integer> items) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.timestamp = LocalDateTime.now();
        this.status = Status.PENDING;
        this.totalAmount = 0.0;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public Map<String, Integer> getItems() { return Collections.unmodifiableMap(items); }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    @Override
    public String getId() { return orderId; }

    @Override
    public String getAuditSummary() {
        return String.format("Order[%s] Customer: %s, Items: %d, Total: $%.2f, Status: %s",
                orderId, customerName, items.size(), totalAmount, status);
    }
}
