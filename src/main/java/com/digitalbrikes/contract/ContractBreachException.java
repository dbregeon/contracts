package com.digitalbrikes.contract;

import java.lang.reflect.Method;

public final class ContractBreachException extends ContractException {
    private static final long serialVersionUID = 1L;

    private static String fullMethodName(final Method method) {
        final StringBuilder result = new StringBuilder(method.getDeclaringClass().getName());
        result.append(".");
        result.append(method.getName());
        return result.toString();
    }

    ContractBreachException(final ErrorType type, final Method method) {
        this(type, method.getName(), null);
    }

    ContractBreachException(final ErrorType type, final Method method, final Throwable e) {
        this(type, fullMethodName(method), e);
    }

    ContractBreachException(final ErrorType type, final String methodName, final Throwable e) {
        super(type.message(methodName), e);
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

        public String message(final String methodName) {
            final StringBuilder result = new StringBuilder(messageBase);
            result.append(" ");
            result.append(methodName);
            return result.toString();
        }
    }
}
