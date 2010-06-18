package com.digitalbrikes.contract;

public abstract class ContractException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ContractException(final String message) {
        this(message, null);
    }

    public ContractException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
