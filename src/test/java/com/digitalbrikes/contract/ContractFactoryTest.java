package com.digitalbrikes.contract;

import java.lang.reflect.Field;
import java.util.List;

import junit.framework.TestCase;

public final class ContractFactoryTest extends TestCase {
    private static final int NUMBER_OF_CLASSES = 3;

    public void testContractForReturnsNullWhenClassIsNotContracted() {
        assertNull(ContractFactory.instance().contractFor(ContractFactoryTest.class));
    }

    public void testContractForReturnsACompositeContractWhenClassIsSubjectToMultipleContracts() {
        assertTrue(ContractFactory.instance().contractFor(SubContractedClass.class) instanceof CompositeContract);
    }

    public void testContractForReturnsASimpleContractWhenClassIsSubjectToASingleContract() {
        assertTrue(ContractFactory.instance().contractFor(TopContractedClass.class) instanceof SimpleContract);
    }

    public void testContractForReturnsAIgnoresDuplicateContractedClass() throws ContractClassException, ContractBreachException, NoSuchFieldException, IllegalAccessException {
        assertEquals(NUMBER_OF_CLASSES, subcontracts(ContractFactory.instance().contractFor(OtherSubContractedClass.class)).size());
    }

    public void testContractImplementationForSingleContractIsTheDesignatedContract() throws NoSuchFieldException, IllegalAccessException {
        final Contract<TopContractedClass> contract = ContractFactory.instance().contractFor(TopContractedClass.class);

        assertTrue(implementation(contract) instanceof TopContract);
    }

    public void testFirstSubContractImplementationsForCompositeContractsIsTheLowestLevelContract() throws NoSuchFieldException, IllegalAccessException {
        final Contract<SubContractedClass> contract = ContractFactory.instance().contractFor(SubContractedClass.class);

        List<Contract<SubContractedClass>> subcontracts = subcontracts(contract);
        assertTrue(implementation(subcontracts.get(0)) instanceof SubContract);
    }

    public void testLastSubContractImplementationsForCompositeContractsIsTheHighestLevelContract() throws NoSuchFieldException, IllegalAccessException {
        final Contract<SubContractedClass> contract = ContractFactory.instance().contractFor(SubContractedClass.class);

        List<Contract<SubContractedClass>> subcontracts = subcontracts(contract);
        assertTrue(implementation(subcontracts.get(subcontracts.size() - 1)) instanceof TopContract);
    }

    private List<Contract<SubContractedClass>> subcontracts(final Contract<?> contract) throws NoSuchFieldException, IllegalAccessException {
        final Field subcontractsField = CompositeContract.class.getDeclaredField("subcontracts");
        subcontractsField.setAccessible(true);
        return (List<Contract<SubContractedClass>>) subcontractsField.get(contract);
    }

    private Object implementation(final Contract<?> contract) throws NoSuchFieldException, IllegalAccessException {
        final Field implementationField = SimpleContract.class.getDeclaredField("implementation");
        implementationField.setAccessible(true);
        return implementationField.get(contract);
    }

    @Contracted(contract = TopContract.class)
    public abstract static class TopContractedClass {

    }

    @Contracted(contract = TopContract.class)
    public abstract static class OtherTopContractedClass implements ContractedInterface {

    }

    @Contracted(contract = TopContract.class)
    public static interface ContractedInterface {

    }

    @Contracted(contract = SubContract.class)
    public static final class SubContractedClass extends TopContractedClass {

    }

    @Contracted(contract = SubContract.class)
    public static final class OtherSubContractedClass extends OtherTopContractedClass implements ContractedInterface {

    }

    public static final class TopContract {
    }

    public static final class SubContract {
    }
}
