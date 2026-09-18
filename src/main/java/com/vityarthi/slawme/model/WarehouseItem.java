package com.vityarthi.slawme.model;

/**
 * Interface defining contract for items stored in warehouse.
 */
public interface WarehouseItem extends Auditable {
    String getName();
    String getCategory();
    double getUnitPrice();
    int getQuantity();
    void setQuantity(int quantity);
    int getReorderLevel();
    boolean isLowStock();
    double calculateInventoryValue();
    String getItemDetails();
}
