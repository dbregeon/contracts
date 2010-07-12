package com.digitalbrikes.contract;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public final class SimpleContractTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorShouldThrowContractBreachExceptionWhenMissingAPrecondititon() {
        thrown.expect(ContractBreachException.class);
        thrown.expectMessage(MissingConditionsContract.class.getName() + ".missingPrecondition");

        new SimpleContract<MissingPreconditionService>(MissingPreconditionService.class);
    }

    @Test
    public void constructorShouldThrowContractBreachExceptionWhenMissingAPostcondititon() {
        thrown.expect(ContractBreachException.class);
        thrown.expectMessage(MissingConditionsContract.class.getName() + ".missingPostcondition");

        new SimpleContract<MissingPostconditionService>(MissingPostconditionService.class);
    }

    @Test
    public void constructorShouldThrowContractClassExceptionWhenContractClassCannotBeInstantiated() {
        thrown.expect(ContractClassException.class);
        thrown.expectMessage(AbstractContract.class.getName());

        new SimpleContract<AbstractContractService>(AbstractContractService.class);
    }

    @Test
    public void isPreconditionedShouldReturnFalseWhenNoPreconditionIsSetOnAMethod() throws NoSuchMethodException {
        final SimpleContract<ContractService> testedContract = new SimpleContract<ContractService>(ContractService.class);

        assertFalse(testedContract.isPreconditioned(ContractService.class.getMethod("postConditioned", new Class[0])));
    }

    @Test
    public void isPostconditionedShouldReturnFalseWhenNoPostconditionIsSetOnAMethod() throws NoSuchMethodException {
        final SimpleContract<ContractService> testedContract = new SimpleContract<ContractService>(ContractService.class);

        assertFalse(testedContract.isPostconditioned(ContractService.class.getMethod("preConditioned", new Class[0])));
    }

    @Test
    public void isPreconditionedShouldReturnTrueWhenAPreconditionIsSetOnAMethod() throws NoSuchMethodException {
        final SimpleContract<ContractService> testedContract = new SimpleContract<ContractService>(ContractService.class);

        assertTrue(testedContract.isPreconditioned(ContractService.class.getMethod("preConditioned", new Class[0])));
    }

    @Test
    public void isPostconditionedShouldReturnTrueWhenAPostconditionIsSetOnAMethod() throws NoSuchMethodException {
        final SimpleContract<ContractService> testedContract = new SimpleContract<ContractService>(ContractService.class);

        assertTrue(testedContract.isPostconditioned(ContractService.class.getMethod("postConditioned", new Class[0])));
    }

    @Test
    public void constructorShouldThrowContractClassExceptionWhenContractClassIsNotAccessible() {
        thrown.expect(ContractClassException.class);
        thrown.expectMessage(InaccessibleContract.class.getName());

        new SimpleContract<InaccessibleContractService>(InaccessibleContractService.class);

    }

    @Contracted(contract = MissingConditionsContract.class)
    public static interface MissingPreconditionService {
        @PreConditioned(precondition = "missingPrecondition")
        Object doSomething(Object o);
    }

    @Contracted(contract = MissingConditionsContract.class)
    public static interface MissingPostconditionService {
        @PostConditioned(postcondition = "missingPostcondition")
        Object doSomething(Object o);
    }

    @Contracted(contract = AbstractContract.class)
    public static interface AbstractContractService {
    }

    @Contracted(contract = InaccessibleContract.class)
    public static interface InaccessibleContractService {
    }

    @Contracted(contract = GoodContract.class)
    public static interface ContractService {
        @PreConditioned(precondition = "precondition")
        void preConditioned();

        @PostConditioned(postcondition = "postcondition")
        void postConditioned();
    }

    public static final class MissingConditionsContract {

    }

    public abstract static class AbstractContract {

    }

    private static final class InaccessibleContract {

    }
}
