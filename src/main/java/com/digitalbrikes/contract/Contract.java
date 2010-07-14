package com.digitalbrikes.contract;

import java.lang.reflect.Method;

/**
 * Implementations of this interface enable the enforcement of contracts defined on a
 * type T.
 */
interface Contract<T> {
    /**
     * @param method the method for which to look for a precondition.
     * @return true when the method is subject to a precondition in this contract,
     * false otherwise.
     */
    boolean isPreconditioned(Method method);
    /**
     * @param method the method for which to look for a postcondition.
     * @return true when the method is subject to a postcondition in this contract,
     * false otherwise.
     */
    boolean isPostconditioned(Method method);

    /**
     * Verifies that the arguments of the method invocation on the contractObject
     * respect the preconditions imposed on the method.
     *
     * @param contractObject the subject of the contract.
     * @param method the method which execution we verify.
     * @param args the arguments passed to the method when it was called.
     * @throws ContractBreachException when the preconditions on the method for the contractObject
     * are not respected.
     */
    void verifyPrecondition(T contractObject, Method method, Object[] args) throws ContractBreachException;
    /**
     * Verifies that the arguments of the method invocation on the contractObject
     * respect the postconditions imposed on the method.
     *
     * @param contractObject the subject of the contract.
     * @param method the method which execution we verify.
     * @param args the arguments passed to the method when it was called.
     * @param result the value returned from the method invocation.
     * @throws ContractBreachException when the postconditions on the method for the contractObject
     * are not respected.
     */
    void verifyPostcondition(T contractObject, Method method, Object[] args, Object result) throws ContractBreachException;
}
