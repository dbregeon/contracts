package com.digitalbrikes.contract;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.mockito.Mockito;

public final class ContractInvocationHandlerTest {
    private ContractedServiceExample service;
    private Object value;

    @Test(expected = ContractBreachException.class)
    public void methodUnderTestShouldThrowContractBreachWhenPreconditionIsBroken() {
        givenAContractedService();

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        testedUser.methodUnderTest(null);
    }

    @Test
    public void methodUnderTestShouldRespectContract() {
        givenAContractedService();
        givenAValue();

        when(mock().doSomething(value())).thenReturn(value());

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        assertSame(value(), testedUser.methodUnderTest(value()));
    }

    @Test(expected = ContractBreachException.class)
    public void testShouldThrowContractBreachWhenStubbingBreachesThePostcondition() {
        givenAContractedService();
        givenAValue();

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        testedUser.methodUnderTest(value());
    }

    @Test
    public void otherMethodUnderTestShouldHaveNoContract() {
        givenAContractedService();

        final ContractedServiceExampleUser testedUser = new ContractedServiceExampleUser(contractedService());

        assertNull(testedUser.otherMethodUnderTest(null));
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
        final ContractInvocationHandler<T> invocationHandler = new ContractInvocationHandler<T>(Mockito.mock(clazz));
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {clazz}, invocationHandler);
    }

    @Contracted(contract = ContractedServiceExampleContract.class)
    public static interface ContractedServiceExample {
        @PreConditioned(precondition = "preDoSomething")
        @PostConditioned(postcondition = "postDoSomething")
        Object doSomething(Object o);

        Object nonContractedAction(Object o);
    }

    public static final class ContractedServiceExampleContract {
        public boolean preDoSomething(final ContractedServiceExample s, final Object o) {
            return null != o;
        }

        public boolean postDoSomething(final ContractedServiceExample s, final Object o, final Object result) {
            return o == result;
        }
    }

    public static final class ContractedServiceExampleUser {
        private final ContractedServiceExample contractedService;

        public ContractedServiceExampleUser(
                final ContractedServiceExample s) {
            contractedService = s;
        }

        public Object methodUnderTest(final Object object) {
            return contractedService.doSomething(object);
        }

        public Object otherMethodUnderTest(final Object object) {
            return contractedService.nonContractedAction(object);
        }

    }
}
