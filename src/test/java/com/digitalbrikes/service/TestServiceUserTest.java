package com.digitalbrikes.service;


import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import junit.framework.TestCase;

import org.mockito.Mockito;

import com.digitalbrikes.contract.ContractBreachException;
import com.digitalbrikes.contract.ContractInvocationHandler;

public final class TestServiceUserTest extends TestCase {
    private ContractedServiceExample service;
    private Object value;
    private NonContractedServiceExample nonContractedService;

    public void testMethodUnderTestThrowsContractBreach() {
        givenAContractedService();

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        try {
            testedUser.methodUnderTest(null);
            fail("Expected a " + ContractBreachException.class);
        } catch (ContractBreachException e) {
            e.printStackTrace();
        }
    }

    public void testMethodUnderTestRespectsContract() {
        givenAContractedService();
        givenAValue();

        when(mock().doSomething(value())).thenReturn(value());

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        assertSame(value(), testedUser.methodUnderTest(value()));
    }

    public void testTestThrowsContractBreachWhenStubbingBreachesTheContract() {
        givenAContractedService();
        givenAValue();

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        try {
            testedUser.methodUnderTest(value());
            fail("Expected a " + ContractBreachException.class);
        } catch (ContractBreachException e) {
            e.printStackTrace();
        }
    }

    public void testOtherMethodUnderTestHasNoContract() {
        givenAContractedService();

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        assertNull(testedUser.otherMethodUnderTest(null));
    }

    public void testNonContractedServiceHasNoContract() {
        givenANonContractedService();

        final NonContractedServiceExampleUser testedUser = new NonContractedServiceExampleUser(nonContractedService());

        assertNull(testedUser.methodUnderTest(null));
    }

    private NonContractedServiceExample nonContractedService() {
        return nonContractedService;
    }

    private void givenANonContractedService() {
        nonContractedService = contractedMock(NonContractedServiceExample.class);
    }

    private ContractedServiceExample mock() {
        return ((ContractInvocationHandler<ContractedServiceExample>) Proxy.getInvocationHandler(contractedService())).contractedObject();
    }

    private Object value() {
        return value;
    }

    private void givenAValue() {
        value = new Object();
    }

    private ContractedServiceExample contractedService() {
        return service;
    }

    private void givenAContractedService() {
        service = contractedMock(ContractedServiceExample.class);
    }

    private <T> T contractedMock(final Class<T> clazz) {
        final ContractInvocationHandler<T> invocationHandler = new ContractInvocationHandler<T>(clazz, Mockito.mock(clazz));
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {clazz}, invocationHandler);
    }
}
