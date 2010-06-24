package com.digitalbrikes.contract;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Contract<T> {
    private final Class<T> contractedClass;
    private final Object implementation;
    private final Map<MethodKey, Precondition> preconditions = new HashMap<MethodKey, Precondition>();
    private final Map<MethodKey, Postcondition> postconditions = new HashMap<MethodKey, Postcondition>();

    public Contract(final Class<T> clazz) {
        contractedClass = clazz;
        implementation = buildInstanceOf(contractClass(contractedClass));
        initializeConditions();
    }

    public void verifyPrecondition(final T contractObject, final Method method, final Object[] args) {
        final Precondition precondition = precondition(method);
        if (null != precondition && !precondition.verify(contractObject, args)) {
            throw new ContractBreachException(ContractBreachException.ErrorType.BREACHED_CONTRACT,  method);
        }
    }

    public void verifyPostcondition(final T contractObject, final Method method, final Object[] args, final Object result) {
        final Postcondition postcondition = postcondition(method);
        if (null != postcondition &&  !postcondition.verify(contractObject, args, result)) {
            throw new ContractBreachException(ContractBreachException.ErrorType.BREACHED_CONTRACT,  method);
        }
    }

    public boolean isPreconditioned(final Method m) {
        return (null != precondition(m));
    }

    public boolean isPostconditioned(final Method m) {
        return (null != postcondition(m));
    }

    private Precondition precondition(final Method method) {
        return preconditions.get(new MethodKey(method));
    }

    private Postcondition postcondition(final Method method) {
        return postconditions.get(new MethodKey(method));
    }

    private Object buildInstanceOf(final Class<?> contractClass) {
        try {
            return contractClass.newInstance();
        } catch (InstantiationException e) {
            throw new ContractClassException(ContractClassException.ErrorType.CONTRACT_INSTANTIATION, contractClass, e);
        } catch (IllegalAccessException e) {
            throw new ContractClassException(ContractClassException.ErrorType.CONTRACT_ACCESS, contractClass, e);
        }
    }

    private Class<?> contractClass(final Class<T> clazz) {
        return clazz.getAnnotation(Contracted.class).contract();
    }

    private void initializeConditions() {
        for (Method method : contractedClass.getDeclaredMethods()) {
            addPreconditionFor(method);
            addPostConditionFor(method);
        }
    }

    private void addPostConditionFor(final Method method) {
        if (method.isAnnotationPresent(PostConditioned.class)) {
            postconditions.put(new MethodKey(method), new Postcondition(method, implementation));
        }
    }

    private void addPreconditionFor(final Method method) {
        if (method.isAnnotationPresent(PreConditioned.class)) {
            preconditions.put(new MethodKey(method), new Precondition(method, implementation));
        }
    }

    private static class MethodKey {
        private final String name;
        private final Class<?>[] parameterTypes;

        @Override
        public int hashCode() {
            return name.hashCode() ^ Arrays.hashCode(parameterTypes);
        }

        @Override
        public boolean equals(final Object obj) {
            final MethodKey other = (MethodKey) obj;
            return name.equals(other.name) && Arrays.equals(parameterTypes, other.parameterTypes);
        }

        public MethodKey(final Method method) {
            name = method.getName();
            parameterTypes = method.getParameterTypes();
        }

    }
}
