package com.digitalbrikes.contract;

import static java.lang.System.arraycopy;

import java.lang.reflect.Method;

final class Precondition<T> extends Condition<T> {
    Precondition(final Method m, final Object i) throws ContractBreachException {
        super(m, i);
    }

    boolean verify(final T contractObject, final Object[] args) {
        return invoke(arguments(contractObject, args));
    }

    private Object[] arguments(final T contractObject, final Object[] invocationArguments) {
        final Object[] contractArgs = new Object[invocationArguments.length + 1];
        contractArgs[0] = contractObject;
        arraycopy(invocationArguments, 0, contractArgs, 1, invocationArguments.length);
        return contractArgs;
    }

    @Override
    protected String methodName(final Method method) {
        return method.getAnnotation(PreConditioned.class).precondition();
    }

    @Override
    protected Class<?>[] parameterTypes(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Class<?>[] result = new Class<?>[paramTypes.length + 1];
        result[0] = method.getDeclaringClass();
        arraycopy(paramTypes, 0, result, 1, paramTypes.length);
        return result;
    }
}
