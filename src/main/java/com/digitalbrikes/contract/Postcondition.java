package com.digitalbrikes.contract;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class Postcondition<T> extends Condition<T> {
    private boolean isVoid;

    Postcondition(final Method method, final Object implementation) throws ContractBreachException {
        super(method, implementation);
    }

    boolean verify(final T contractObject, final Object[] args, final Object result) {
        return invoke(arguments(contractObject, args, result));
    }

    @Override
    protected Class<?>[] parameterTypes(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        final List<Class<?>> result = new ArrayList<Class<?>>();
        result.add(method.getDeclaringClass());
        result.addAll(Arrays.asList(paramTypes));
        if (!isVoid(method)) {
            result.add(method.getReturnType());
        }
        return result.toArray(new Class[0]);
    }

    private boolean isVoid(final Method method) {
        isVoid  = Void.TYPE == method.getReturnType();
        return isVoid;
    }

    @Override
    protected String methodName(final Method method) {
        return method.getAnnotation(PostConditioned.class).postcondition();
    }

    private Object[] arguments(final T contractObject, final Object[] invocationArguments, final Object result) {
        final List<Object> contractArgs = new ArrayList<Object>();
        contractArgs.add(contractObject);
        contractArgs.addAll(Arrays.asList(invocationArguments));
        if (!isVoid) {
            contractArgs.add(result);
        }
        return contractArgs.toArray();
    }
}
