package com.digitalbrikes.contract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.digitalbrikes.contract.ContractBreachException.ErrorType;

abstract class Condition<T> {
    private final Method conditionMethod;
    private final Object implementation;

    Condition(final Method m, final Object i) throws ContractBreachException {
        implementation = i;
        conditionMethod = conditionMethodFor(m);
    }

    protected final Boolean invoke(final Object[] args) throws ContractBreachException {
        try {
            return (Boolean) conditionMethod.invoke(implementation, args);
        } catch (final IllegalArgumentException e) {
            throw new ContractBreachException(ErrorType.INVALID_CONDITION, conditionMethod, e);
        } catch (final IllegalAccessException e) {
            throw new ContractBreachException(ErrorType.INACCESSIBLE_CONDITION, conditionMethod, e);
        } catch (final InvocationTargetException e) {
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

    private Method conditionMethodFor(final Method method) throws ContractBreachException {
        try {
            return implementation.getClass().getDeclaredMethod(methodName(method), parameterTypes(method));
        } catch (final SecurityException e) {
            throw new ContractBreachException(ErrorType.INACCESSIBLE_CONDITION, fullMethodName(method), e);
        } catch (final NoSuchMethodException e) {
            throw new ContractBreachException(ErrorType.MISSING_CONDITION, fullMethodName(method), e);
        }
    }
}
