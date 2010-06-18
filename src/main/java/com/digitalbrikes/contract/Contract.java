package com.digitalbrikes.contract;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class Contract<T> {
    private final Class<T> contractedClass;
    private final Object implementation;
    private final Map<Method, Precondition> preconditions = new HashMap<Method, Precondition>();
    private final Map<Method, Postcondition> postconditions = new HashMap<Method, Postcondition>();

    public Contract(final Class<T> clazz) {
        contractedClass = clazz;
        implementation = buildInstanceOf(contractClass(contractedClass));
    }

    public void verifyPrecondition(final T contractObject, final Method method, final Object[] args) {
        if (method.isAnnotationPresent(PreConditioned.class) && !precondition(method).verify(contractObject, args)) {
            throw new ContractBreachException(ContractBreachException.ErrorType.BREACHED_CONTRACT,  method);
        }
    }

    public void verifyPostcondition(final T contractObject, final Method method, final Object[] args, final Object result) {
        if (method.isAnnotationPresent(PostConditioned.class) && !postcondition(method).verify(contractObject, args, result)) {
            throw new ContractBreachException(ContractBreachException.ErrorType.BREACHED_CONTRACT,  method);
        }
    }

    private Precondition precondition(final Method method) {
        Precondition result = preconditions.get(method);
        if (null == result) {
            result = new Precondition(method, implementation);
            preconditions.put(method, result);
        }
        return result;
    }

    private Postcondition postcondition(final Method method) {
        Postcondition result = postconditions.get(method);
        if (null == result) {
            result = new Postcondition(method, implementation);
            postconditions.put(method, result);
        }
        return result;
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
        Class<?> result = null;
        try {
            result = getClass().getClassLoader().loadClass(clazz.getPackage().getName() + ".contract." + clazz.getSimpleName() + "Contract");
        } catch (ClassNotFoundException e) {
            throw new ContractClassException(ContractClassException.ErrorType.MISSING_CONTRACT, clazz, e);
        }
        return result;
    }
}
