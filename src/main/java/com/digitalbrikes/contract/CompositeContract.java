package com.digitalbrikes.contract;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class CompositeContract<T> implements Contract<T> {
    private final List<Contract<T>> subcontracts = new ArrayList<Contract<T>>();

    CompositeContract(final List<Contract<T>> contracts) throws ContractBreachException {
        subcontracts.addAll(contracts);
    }

    @Override
    public boolean isPostconditioned(final Method method) {
        final Iterator<Contract<T>> iter = subcontracts.iterator();
        boolean result = false;
        while (!result && iter.hasNext()) {
            result = iter.next().isPostconditioned(method);
        }
        return result;
    }

    @Override
    public boolean isPreconditioned(final Method method) {
        final Iterator<Contract<T>> iter = subcontracts.iterator();
        boolean result = false;
        while (!result && iter.hasNext()) {
            result = iter.next().isPreconditioned(method);
        }
        return result;
    }

    @Override
    public void verifyPostcondition(final T contractObject, final Method method, final Object[] args, final Object result) throws ContractBreachException {
        for (final Contract<T> subcontract : subcontracts) {
            subcontract.verifyPostcondition(contractObject, method, args, result);
        }
    }

    @Override
    public void verifyPrecondition(final T contractObject, final Method method, final Object[] args) throws ContractBreachException {
        for (final Contract<T> subcontract : subcontracts) {
            subcontract.verifyPrecondition(contractObject, method, args);
        }
    }

}
