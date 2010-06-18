package com.digitalbrikes.service;

public final class ContractedServiceExampleUser {
    private final ContractedServiceExample service;

    public ContractedServiceExampleUser(final ContractedServiceExample s) {
        service = s;
    }

    public Object methodUnderTest(final Object o) {
        return service.doSomething(o);
    }

    public Object otherMethodUnderTest(final Object o) {
        return service.nonContractedAction(o);
    }
}
