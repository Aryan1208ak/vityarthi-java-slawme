package com.vityarthi.slawme.model;

import java.time.LocalDate;

/**
 * Concrete subclass representing perishable inventory items.
 */
public class PerishableItem extends AbstractItem {
    private LocalDate expiryDate;
    private double storageTempCelsius;

    public PerishableItem(String id, String name, String category, double unitPrice, int quantity, int reorderLevel, LocalDate expiryDate, double storageTempCelsius) {
        super(id, name, category, unitPrice, quantity, reorderLevel);
        this.expiryDate = expiryDate;
        this.storageTempCelsius = storageTempCelsius;
    }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public double getStorageTempCelsius() { return storageTempCelsius; }
    public void setStorageTempCelsius(double storageTempCelsius) { this.storageTempCelsius = storageTempCelsius; }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    @Override
    public String getItemDetails() {
        return String.format("[PERISHABLE] ID: %s | Name: %-15s | Cat: %-10s | Price: $%-7.2f | Qty: %-4d | Temp: %.1f°C | Expires: %s %s",
                id, name, category, unitPrice, quantity, storageTempCelsius, expiryDate, (isExpired() ? "(EXPIRED!)" : ""));
    }
}
