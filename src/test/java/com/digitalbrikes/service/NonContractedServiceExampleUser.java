package com.digitalbrikes.service;

public final class NonContractedServiceExampleUser {
    private final NonContractedServiceExample service;

    public NonContractedServiceExampleUser(final NonContractedServiceExample s) {
        service = s;
    }

    public Object methodUnderTest(final Object o) {
        return service.doSomething(o);
    }
}
