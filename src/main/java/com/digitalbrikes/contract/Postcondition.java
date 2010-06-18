package com.digitalbrikes.contract;

import java.lang.reflect.Method;

public final class Postcondition extends Condition {
    public Postcondition(final Method method, final Object implementation) {
        super(ConditionType.POST, method, implementation);
    }

    public boolean verify(final Object contractObject, final Object[] args, final Object result) {
        return invoke(arguments(contractObject, args, result));
    }

    protected Class<?>[] parameterTypes(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Class<?>[] result = new Class<?>[paramTypes.length + 2];
        result[0] = method.getDeclaringClass();
        for (int i = 0; i < paramTypes.length; i++) {
            result[i + 1] = paramTypes[i];
        }
        result[paramTypes.length + 1] = method.getReturnType();
        return result;
    }

    private Object[] arguments(final Object contractObject, final Object[] invocationArguments, final Object result) {
        final Object[] contractArgs = new Object[invocationArguments.length + 2];
        contractArgs[0] = contractObject;
        for (int i = 0; i < invocationArguments.length; i++) {
            contractArgs[i + 1] = invocationArguments[i];
        }
        contractArgs[invocationArguments.length + 1] = result;
        return contractArgs;
    }

}
