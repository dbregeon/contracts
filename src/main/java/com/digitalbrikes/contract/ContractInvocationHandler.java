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
    private final Contract<T> contract;
    private final T contractedObject;

    /**
     * Builds an invocation handler that will wrap an object and verify its declared
     * contract when methods are invoked.
     *
     * @param object the object to be wrapped.
     * @throws ContractClassException when the contract for the object can not be built.
     * @throws ContractBreachException when specification of the contract for the object
     * does not match its implementation.
     */
    public ContractInvocationHandler(final T object) throws ContractClassException, ContractBreachException {
        contractedObject = object;
        contract = ContractFactory.instance().contractFor(contractedObject.getClass());
    }

    /**
     * @return the object wrapped by this invocation handler.
     */
    public T contractedObject() {
        return contractedObject;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
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
        return null != contract;
    }
}
