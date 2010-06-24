package com.digitalbrikes.contract;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

final class Postcondition extends Condition {
    private boolean isVoid;

    public Postcondition(final Method method, final Object implementation) {
        super(method, implementation);
    }

    public boolean verify(final Object contractObject, final Object[] args, final Object result) {
        return invoke(arguments(contractObject, args, result));
    }

    protected Class<?>[] parameterTypes(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        final List<Class<?>> result = new ArrayList<Class<?>>();
        result.add(method.getDeclaringClass());
        for (int i = 0; i < paramTypes.length; i++) {
            result.add(paramTypes[i]);
        }
        if (!isVoid(method)) {
            result.add(method.getReturnType());
        }
        return result.toArray(new Class[0]);
    }

    private boolean isVoid(final Method method) {
        isVoid  = (Void.TYPE == method.getReturnType());
        return isVoid;
    }

    @Override
    protected String methodName(final Method method) {
        return method.getAnnotation(PostConditioned.class).postcondition();
    }

    private Object[] arguments(final Object contractObject, final Object[] invocationArguments, final Object result) {
        final List<Object> contractArgs = new ArrayList<Object>();
        contractArgs.add(contractObject);
        for (int i = 0; i < invocationArguments.length; i++) {
            contractArgs.add(invocationArguments[i]);
        }
        if (!isVoid) {
            contractArgs.add(result);
        }
        return contractArgs.toArray();
    }
}
