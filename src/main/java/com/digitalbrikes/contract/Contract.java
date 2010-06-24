package com.digitalbrikes.contract;

import java.lang.reflect.Method;

public interface Contract<T> {
    boolean isPreconditioned(Method method);
    boolean isPostconditioned(Method method);

    void verifyPrecondition(T contractObject, Method method, Object[] args) throws ContractBreachException;
    void verifyPostcondition(T contractObject, Method method, Object[] args, Object result) throws ContractBreachException;
}
