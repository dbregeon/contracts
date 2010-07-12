package com.digitalbrikes.contract;

public final class LocalContract {
    public boolean postcondition(final ContractedClass contracted, final Double value) {
        return contracted.testIsNull();
    }

    public boolean throwingcondition(final ContractedClass contracted, final Double value) {
        throw new NullPointerException();
    }
}
