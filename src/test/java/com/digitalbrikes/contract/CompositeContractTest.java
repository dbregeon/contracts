package com.digitalbrikes.contract;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

public final class CompositeContractTest {
    private ArrayList<Contract<CompositeContractTest>> contractList;
    private Contract<CompositeContractTest> contract;
    private Method method;
    private Object[] args;
    private Object result;

    @Test
    public void isPreconditionShouldDelegateToSubcontract() {
        givenAListOfContract();
        givenAContract();
        givenAMethod();

        contractList().add(contract());

        final CompositeContract<CompositeContractTest> testedContract = new CompositeContract<CompositeContractTest>(contractList());

        testedContract.isPreconditioned(method());

        verify(contract()).isPreconditioned(method());
    }

    @Test
    public void isPostconditionShouldDelegateToSubcontract() {
        givenAListOfContract();
        givenAContract();
        givenAMethod();

        contractList().add(contract());

        final CompositeContract<CompositeContractTest> testedContract = new CompositeContract<CompositeContractTest>(contractList());

        testedContract.isPostconditioned(method());

        verify(contract()).isPostconditioned(method());
    }

    @Test
    public void isPreconditionShouldReturnValueFromTheSubcontract() {
        givenAListOfContract();
        givenAContract();
        givenAMethod();

        contractList().add(contract());
        when(contract().isPreconditioned(method())).thenReturn(true);

        final CompositeContract<CompositeContractTest> testedContract = new CompositeContract<CompositeContractTest>(contractList());

        assertTrue(testedContract.isPreconditioned(method()));
    }

    @Test
    public void isPostconditionShouldReturnValueFromTheSubcontract() {
        givenAListOfContract();
        givenAContract();
        givenAMethod();

        contractList().add(contract());
        when(contract().isPostconditioned(method())).thenReturn(true);

        final CompositeContract<CompositeContractTest> testedContract = new CompositeContract<CompositeContractTest>(contractList());

        assertTrue(testedContract.isPostconditioned(method()));
    }

    @Test
    public void verifyPreconditionShouldDelegateToSubcontract() {
        givenAListOfContract();
        givenAContract();
        givenAMethod();
        givenArgs();

        contractList().add(contract());

        final CompositeContract<CompositeContractTest> testedContract = new CompositeContract<CompositeContractTest>(contractList());

        testedContract.verifyPrecondition(this, method(), args());

        verify(contract()).verifyPrecondition(this, method(), args());
    }

    @Test
    public void verifyPostconditionShouldDelegateToSubcontract() {
        givenAListOfContract();
        givenAContract();
        givenAMethod();
        givenArgs();
        givenAResult();

        contractList().add(contract());

        final CompositeContract<CompositeContractTest> testedContract = new CompositeContract<CompositeContractTest>(contractList());

        testedContract.verifyPostcondition(this, method(), args(), result());

        verify(contract()).verifyPostcondition(this, method(), args(), result());
    }

    private Object result() {
        return result;
    }

    private void givenAResult() {
        result = new Object();
    }

    private Object[] args() {
        return args;
    }

    private void givenArgs() {
        args = new Object[] {new Object()};
    }

    private Method method() {
        return method;
    }

    private Contract<CompositeContractTest> contract() {
        return contract;
    }

    private ArrayList<Contract<CompositeContractTest>> contractList() {
        return contractList;
    }

    private void givenAMethod() {
        method = getClass().getMethods()[0];
    }

    private void givenAContract() {
        contract = mock(Contract.class);
    }

    private void givenAListOfContract() {
        contractList = new ArrayList<Contract<CompositeContractTest>>();
    }
}
