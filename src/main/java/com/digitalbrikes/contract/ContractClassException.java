package com.digitalbrikes.contract;

/**
 * Signals an error with the contract implementation class declared by a contracted type.
 *
 * It can be the inability to instantiate the implementation or even the inability to access
 * the class.
 */
public final class ContractClassException extends ContractException {
    private static final long serialVersionUID = 1L;

    ContractClassException(final ErrorType type, final Class<?> clazz, final Throwable cause) {
        super(type.message(clazz), cause);
    }

    static enum ErrorType {
        CONTRACT_INSTANTIATION("Could not instantiate contract"),
        CONTRACT_ACCESS("Could not access contract");

        private final String messageBase;

        private ErrorType(final String s) {
            messageBase = s;
        }

        protected String message(final Class<?> clazz) {
            final StringBuilder result = new StringBuilder(messageBase);
            result.append(" ");
            result.append(clazz.getName());
            return result.toString();
        }
    }

}
