package com.digitalbrikes.contract;

import java.lang.reflect.Method;

public final class ContractBreachException extends ContractException {
    private static final long serialVersionUID = 1L;

    public ContractBreachException(final ErrorType type, final Method method) {
        this(type, method, null);
    }

    public ContractBreachException(final ErrorType type, final Method method, final Throwable e) {
        super(type.message(method), e);
    }

    public static enum ErrorType {
        INACCESSIBLE_CONDITION("Could not access the condition"),
        MISSING_CONDITION("Could not find the condition"),
        INVALID_CONDITION("Could not invoke the condition"),
        BROKEN_CONDITION("Condition invocation failed"),
        BREACHED_CONTRACT("Contract was breached");

        private final String messageBase;

        private ErrorType(final String s) {
            messageBase = s;
        }

        public String message(final Method breachMethod) {
            final StringBuilder result = new StringBuilder(messageBase);
            result.append(" ");
            result.append(breachMethod.getDeclaringClass().getName());
            result.append(".");
            result.append(breachMethod.getName());
            return result.toString();
        }
    }
}
