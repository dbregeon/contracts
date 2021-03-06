package com.digitalbrikes.contract;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * This aspect is weaved for all contracted classes and will enforce the contract(s).
 * 
 * The pre and post conditions will be verified before and after the actual invocation
 * respectively.
 */
public aspect ContractAspect pertypewithin(!com.digitalbrikes.contract.*){
    pointcut contracted(Object o): this(o) && if (isContracted(o));
    pointcut precondition(): contracted(Object) && execution(public * *.*(..)) && if (isPreconditioned(thisJoinPoint));
    pointcut postcondition(): contracted(Object) && execution(public * *.*(..)) && if (isPostconditioned(thisJoinPoint));
    
    private static Contract<Object> contract;
    
    private static Method method(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }
    
    @SuppressWarnings("unused")
    private static boolean isContracted(final Object o) {
        if (null == contract) {
            contract = ContractFactory.instance().contractFor(o.getClass());
        }
        return null == contract;
    }
    
    private static Contract<Object> contract() {
        return contract;
    }
    
    @SuppressWarnings("unused")
    private static boolean isPreconditioned(final JoinPoint joinPoint) {
        boolean result = false;
        if (null != contract) {
            result = contract().isPreconditioned(method(joinPoint));
        }
        return result;
    }
    
    @SuppressWarnings("unused")
    private static boolean isPostconditioned(final JoinPoint joinPoint) {
        boolean result = false;
        if (null != contract) {
            result = contract.isPostconditioned(method(joinPoint));
        }
        return result;
    }

    before() : precondition() {
        contract().verifyPrecondition(thisJoinPoint.getThis(), ((MethodSignature) thisJoinPoint.getSignature()).getMethod(), thisJoinPoint.getArgs());
    }

    after() returning(final Object result) : postcondition() {
        contract().verifyPostcondition(thisJoinPoint.getThis(), ((MethodSignature) thisJoinPoint.getSignature()).getMethod(), thisJoinPoint.getArgs(), result);
    }
}
