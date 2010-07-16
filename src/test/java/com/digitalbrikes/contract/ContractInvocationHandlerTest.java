package com.digitalbrikes.contract;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.junit.Test;

public final class ContractInvocationHandlerTest {
    private ContractedService service;
    private Object value;
    private ContractFreeService contractFreeService;

    @Test(expected = ContractBreachException.class)
    public void methodUnderTestShouldThrowContractBreachWhenPreconditionIsBroken() {
        givenAContractedService();

        final ContractInvocationHandler<ContractedService> testedHandler = new ContractInvocationHandler<ContractedService>(contractedService());
        final ContractedService proxiedService = (ContractedService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {ContractedService.class}, testedHandler);

        proxiedService.doSomething(null);
    }

    @Test
    public void methodUnderTestShouldRespectContract() {
        givenAContractedService();
        givenAValue();

        when(contractedService().doSomething(value())).thenReturn(value());

        final ContractInvocationHandler<ContractedService> testedHandler = new ContractInvocationHandler<ContractedService>(contractedService());
        final ContractedService proxiedService = (ContractedService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {ContractedService.class}, testedHandler);

        assertSame(value(), proxiedService.doSomething(value()));
    }

    @Test(expected = ContractBreachException.class)
    public void testShouldThrowContractBreachWhenStubbingBreachesThePostcondition() {
        givenAContractedService();
        givenAValue();

        final ContractInvocationHandler<ContractedService> testedHandler = new ContractInvocationHandler<ContractedService>(contractedService());
        final ContractedService proxiedService = (ContractedService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {ContractedService.class}, testedHandler);

        proxiedService.doSomething(value());
    }

    @Test
    public void otherMethodUnderTestShouldHaveNoContract() {
        givenAContractedService();

        final ContractInvocationHandler<ContractedService> testedHandler = new ContractInvocationHandler<ContractedService>(contractedService());
        final ContractedService proxiedService = (ContractedService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {ContractedService.class}, testedHandler);

        assertNull(proxiedService.nonContractedAction(null));
    }

    @Test
    public void noContractVerifcationShouldHappen() {
        givenAContractFreeService();

        final ContractInvocationHandler<ContractFreeService> testedHandler = new ContractInvocationHandler<ContractFreeService>(contractFreeService());
        final ContractFreeService proxiedService = (ContractFreeService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {ContractFreeService.class}, testedHandler);

        assertNull(proxiedService.nonContractedAction(null));
    }

    @Test
    public void contractedObjectShouldReturnTheConstructorArgument() {
        givenAContractFreeService();

        final ContractInvocationHandler<ContractFreeService> testedHandler = new ContractInvocationHandler<ContractFreeService>(contractFreeService());

        assertSame(contractFreeService(), testedHandler.contractedObject());
    }

    private Object value() {
        return value;
    }

    private void givenAValue() {
        value = new Object();
    }

    private ContractFreeService contractFreeService() {
        return contractFreeService;
    }

    private void givenAContractFreeService() {
        contractFreeService = mock(ContractFreeService.class);
    }

    private ContractedService contractedService() {
        return service;
    }

    private void givenAContractedService() {
        service = mock(ContractedService.class);
    }

    @Contracted(contract = ContractedServiceContract.class)
    public static interface ContractedService {
        @PreConditioned(precondition = "preDoSomething")
        @PostConditioned(postcondition = "postDoSomething")
        Object doSomething(Object o);

        Object nonContractedAction(Object o);
    }

    public static interface ContractFreeService {
        Object nonContractedAction(Object o);
    }

    public static final class ContractedServiceContract {
        public boolean preDoSomething(final ContractedService s, final Object o) {
            return null != o;
        }

        public boolean postDoSomething(final ContractedService s, final Object o, final Object result) {
            return o == result;
        }
    }
}
