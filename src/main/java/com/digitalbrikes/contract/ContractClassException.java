package com.digitalbrikes.contract;


public final class ContractClassException extends ContractException {
    private static final long serialVersionUID = 1L;

    ContractClassException(final ErrorType type, final Class<?> clazz, final Throwable cause) {
        super(type.message(clazz), cause);
    }

    public static enum ErrorType {
        CONTRACT_INSTANTIATION("Could not instantiate contract"),
        CONTRACT_ACCESS("Could not access contract");

        private final String messageBase;

        private ErrorType(final String s) {
            messageBase = s;
        }

        public String message(final Class<?> clazz) {
            final StringBuilder result = new StringBuilder(messageBase);
            result.append(" ");
            result.append(clazz.getName());
            return result.toString();
        }
    }

}
