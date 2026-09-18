package com.vityarthi.slawme.model;

/**
 * Concrete subclass representing electronic hardware inventory items.
 */
public class ElectronicItem extends AbstractItem {
    private int warrantyMonths;
    private int voltageRating;

    public ElectronicItem(String id, String name, String category, double unitPrice, int quantity, int reorderLevel, int warrantyMonths, int voltageRating) {
        super(id, name, category, unitPrice, quantity, reorderLevel);
        this.warrantyMonths = warrantyMonths;
        this.voltageRating = voltageRating;
    }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    public int getVoltageRating() { return voltageRating; }
    public void setVoltageRating(int voltageRating) { this.voltageRating = voltageRating; }

    @Override
    public String getItemDetails() {
        return String.format("[ELECTRONIC] ID: %s | Name: %-15s | Cat: %-10s | Price: $%-7.2f | Qty: %-4d | Warranty: %dm | Voltage: %dV",
                id, name, category, unitPrice, quantity, warrantyMonths, voltageRating);
    }
}
