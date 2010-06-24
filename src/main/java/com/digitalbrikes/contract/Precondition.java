package com.digitalbrikes.contract;

import java.lang.reflect.Method;

final class Precondition<T> extends Condition<T> {
    public Precondition(final Method m, final Object i) throws ContractBreachException {
        super(m, i);
    }

    public boolean verify(final T contractObject, final Object[] args) {
        return invoke(arguments(contractObject, args));
    }

    private Object[] arguments(final T contractObject, final Object[] invocationArguments) {
        final Object[] contractArgs = new Object[invocationArguments.length + 1];
        contractArgs[0] = contractObject;
        for (int i = 0; i < invocationArguments.length; i++) {
            contractArgs[i + 1] = invocationArguments[i];
        }
        return contractArgs;
    }

    @Override
    protected String methodName(final Method method) {
        return method.getAnnotation(PreConditioned.class).precondition();
    }

    protected Class<?>[] parameterTypes(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Class<?>[] result = new Class<?>[paramTypes.length + 1];
        result[0] = method.getDeclaringClass();
        for (int i = 0; i < paramTypes.length; i++) {
            result[i + 1] = paramTypes[i];
        }
        return result;
    }
}
