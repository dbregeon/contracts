package com.digitalbrikes.contract;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public final class ContractInvocationHandler<T> implements InvocationHandler {
    private Contract<T> contract;
    private final T contractedObject;
    private final Class<T> contractedClass;

    public ContractInvocationHandler(final Class<T> clazz, final T object) {
        contractedClass = clazz;
        contractedObject = object;
        if (contractedClass.isAnnotationPresent(Contracted.class)) {
            contract = new Contract<T>(contractedClass);
        }
    }

    public T contractedObject() {
        return contractedObject;
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {
        verifyPrecondition(method, args);
        final Object result = method.invoke(contractedObject, args);
        verifyPostcondition(method, args, result);
        return result;
    }

    private void verifyPrecondition(final Method method, final Object[] args) {
        if (hasContract()) {
            contract.verifyPrecondition(contractedObject, method, args);
        }
    }

    private void verifyPostcondition(final Method method, final Object[] args, final Object result) {
        if (hasContract()) {
            contract.verifyPostcondition(contractedObject, method, args, result);
        }
    }

    private boolean hasContract() {
        return (null != contract);
    }
}
