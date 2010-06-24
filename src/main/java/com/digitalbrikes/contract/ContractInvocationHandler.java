package com.digitalbrikes.contract;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * This InvocationHandler can be used to proxy an instance of a class that has a specified contract.
 * It will ensure that the contract will be enforced for calls to this instance.
 *
 * @param <T> the type under contract.
 */
public final class ContractInvocationHandler<T> implements InvocationHandler {
    private Contract<T> contract;
    private final T contractedObject;

    public ContractInvocationHandler(final T object) throws ContractClassException, ContractBreachException {
        contractedObject = object;
        contract = ContractFactory.instance().contractFor(contractedObject.getClass());
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

    private void verifyPrecondition(final Method method, final Object[] args) throws ContractBreachException {
        if (hasContract()) {
            contract.verifyPrecondition(contractedObject, method, args);
        }
    }

    private void verifyPostcondition(final Method method, final Object[] args, final Object result) throws ContractBreachException {
        if (hasContract()) {
            contract.verifyPostcondition(contractedObject, method, args, result);
        }
    }

    private boolean hasContract() {
        return (null != contract);
    }
}
