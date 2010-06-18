package com.digitalbrikes.contract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.digitalbrikes.contract.ContractBreachException.ErrorType;

public abstract class Condition {
    private final ConditionType type;
    private final Method conditionMethod;
    private final Object implementation;

    public Condition(final ConditionType t, final Method m, final Object i) {
        type = t;
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

    private Method conditionMethod(final Method method) {
        try {
            return implementation.getClass().getDeclaredMethod(type.conditionMethodName(method), parameterTypes(method));
        } catch (SecurityException e) {
            throw new ContractBreachException(ErrorType.INACCESSIBLE_CONDITION, method, e);
        } catch (NoSuchMethodException e) {
            throw new ContractBreachException(ErrorType.MISSING_CONDITION, method, e);
        }
    }
}
