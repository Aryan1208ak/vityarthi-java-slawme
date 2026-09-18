package com.vityarthi.slawme.exception;

public class InsufficientStockException extends WarehouseException {
    private final String itemId;
    private final int requested;
    private final int available;

    public InsufficientStockException(String itemId, int requested, int available) {
        super(String.format("Insufficient stock for Item [%s]: Requested %d, Available %d", itemId, requested, available));
        this.itemId = itemId;
        this.requested = requested;
        this.available = available;
    }

    public String getItemId() { return itemId; }
    public int getRequested() { return requested; }
    public int getAvailable() { return available; }
}
