package com.vityarthi.slawme.exception;

/**
 * Base custom exception for SLAWME application errors.
 */
public class WarehouseException extends Exception {
    public WarehouseException(String message) {
        super(message);
    }

    public WarehouseException(String message, Throwable cause) {
        super(message, cause);
    }
}
