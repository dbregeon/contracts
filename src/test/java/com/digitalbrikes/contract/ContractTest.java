package com.digitalbrikes.contract;

import junit.framework.TestCase;

public final class ContractTest extends TestCase {
    public void testConstructorThrowsContractBreachExceptionWhenMissingAPrecondititon() {
        try {
            new Contract<MissingPreconditionService>(MissingPreconditionService.class);
            fail("Expected a " + ContractBreachException.class);
        } catch (ContractBreachException e) {
            assertTrue(e.getMessage().contains(MissingConditionsContract.class.getName() + ".missingPrecondition"));
        }
    }

    public void testConstructorThrowsContractBreachExceptionWhenMissingAPostcondititon() {
        try {
            new Contract<MissingPostconditionService>(MissingPostconditionService.class);
            fail("Expected a " + ContractBreachException.class);
        } catch (ContractBreachException e) {
            assertTrue(e.getMessage().contains(MissingConditionsContract.class.getName() + ".missingPostcondition"));
        }
    }

    public void testConstructorThrowsContractClassExceptionWhenContractClassCannotBeInstantiated() {
        try {
            new Contract<AbstractContractService>(AbstractContractService.class);
            fail("Expected a " + ContractClassException.class);
        } catch (ContractClassException e) {
            assertTrue(e.getMessage().contains(AbstractContract.class.getName()));
        }
    }

    public void testIsPreconditionedReturnsFalseWhenNoPreconditionIsSetOnAMethod() throws NoSuchMethodException {
        final Contract<ContractService> testedContract = new Contract<ContractService>(ContractService.class);

        assertFalse(testedContract.isPreconditioned(ContractService.class.getMethod("postConditioned", new Class[0])));
    }

    public void testIsPostconditionedReturnsFalseWhenNoPostconditionIsSetOnAMethod() throws NoSuchMethodException {
        final Contract<ContractService> testedContract = new Contract<ContractService>(ContractService.class);

        assertFalse(testedContract.isPostconditioned(ContractService.class.getMethod("preConditioned", new Class[0])));
    }

    public void testIsPreconditionedReturnsTrueWhenAPreconditionIsSetOnAMethod() throws NoSuchMethodException {
        final Contract<ContractService> testedContract = new Contract<ContractService>(ContractService.class);

        assertTrue(testedContract.isPreconditioned(ContractService.class.getMethod("preConditioned", new Class[0])));
    }

    public void testIsPostconditionedReturnsTrueWhenAPostconditionIsSetOnAMethod() throws NoSuchMethodException {
        final Contract<ContractService> testedContract = new Contract<ContractService>(ContractService.class);

        assertTrue(testedContract.isPostconditioned(ContractService.class.getMethod("postConditioned", new Class[0])));
    }

    public void testConstructorThrowsContractClassExceptionWhenContractClassIsNotAccessible() {
        try {
            new Contract<InaccessibleContractService>(InaccessibleContractService.class);
            fail("Expected a " + ContractClassException.class);
        } catch (ContractClassException e) {
            assertTrue(e.getMessage().contains(InaccessibleContract.class.getName()));
        }
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

    public static final class GoodContract {
        public boolean precondition(final ContractService service) {
            return true;
        }

        public boolean postcondition(final ContractService service) {
            return true;
        }
    }

    private static final class InaccessibleContract {

    }
}
