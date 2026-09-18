package com.vityarthi.slawme.model;

import java.util.Objects;

/**
 * Abstract Base Class for Warehouse Items demonstrating Abstraction and Encapsulation.
 */
public abstract class AbstractItem implements WarehouseItem {
    protected final String id;
    protected String name;
    protected String category;
    protected double unitPrice;
    protected int quantity;
    protected int reorderLevel;

    public AbstractItem(String id, String name, String category, double unitPrice, int quantity, int reorderLevel) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.id = id.trim();
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    @Override
    public String getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getCategory() { return category; }

    @Override
    public double getUnitPrice() { return unitPrice; }

    @Override
    public int getQuantity() { return quantity; }

    @Override
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    @Override
    public int getReorderLevel() { return reorderLevel; }

    @Override
    public boolean isLowStock() { return this.quantity <= this.reorderLevel; }

    @Override
    public double calculateInventoryValue() { return this.unitPrice * this.quantity; }

    @Override
    public String getAuditSummary() {
        return String.format("Item[%s - %s] Stock: %d, Value: $%.2f", id, name, quantity, calculateInventoryValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractItem)) return false;
        AbstractItem item = (AbstractItem) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
