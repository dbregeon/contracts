package com.digitalbrikes.contract;

abstract class ContractException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected ContractException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
