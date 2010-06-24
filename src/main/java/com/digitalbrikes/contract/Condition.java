package com.digitalbrikes.contract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.digitalbrikes.contract.ContractBreachException.ErrorType;

abstract class Condition {
    private final Method conditionMethod;
    private final Object implementation;

    public Condition(final Method m, final Object i) {
        implementation = i;
        conditionMethod = conditionMethod(m);
    }

    protected final Boolean invoke(final Object[] args) {
        try {
            return (Boolean) conditionMethod.invoke(implementation, args);
        } catch (IllegalArgumentException e) {
            throw new ContractBreachException(ErrorType.INVALID_CONDITION, conditionMethod, e);
        } catch (IllegalAccessException e) {
            throw new ContractBreachException(ErrorType.INACCESSIBLE_CONDITION, conditionMethod, e);
        } catch (InvocationTargetException e) {
            throw new ContractBreachException(ErrorType.BROKEN_CONDITION, conditionMethod, e);
        }
    }

    protected abstract Class<?>[] parameterTypes(final Method method);
    protected abstract String methodName(final Method method);

    private String fullMethodName(final Method method) {
        final StringBuilder result = new StringBuilder(implementation.getClass().getName());
        result.append(".");
        result.append(methodName(method));
        return result.toString();
    }

    private Method conditionMethod(final Method method) {
        try {
            return implementation.getClass().getDeclaredMethod(methodName(method), parameterTypes(method));
        } catch (SecurityException e) {
            throw new ContractBreachException(ErrorType.INACCESSIBLE_CONDITION, fullMethodName(method), e);
        } catch (NoSuchMethodException e) {
            throw new ContractBreachException(ErrorType.MISSING_CONDITION, fullMethodName(method), e);
        }
    }
}
