package com.digitalbrikes.contract;


@Contracted(contract = LocalContract.class)
public final class ContractedClass {
    private Object test = null;
    @PostConditioned(postcondition = "postcondition")
    public void contractedMethod(final Double value) {
        test = value;
    }
    @PostConditioned(postcondition = "throwingcondition")
    public void throwingMethod(final Double value) {
        test = value;
    }
    @PostConditioned(postcondition = "privatecondition")
    public void wrongMethod(final Object value) {
        test = value;
    }

    public boolean testIsNull() {
        return null == test;
    }
}
