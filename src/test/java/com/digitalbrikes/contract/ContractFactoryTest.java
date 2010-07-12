package com.digitalbrikes.contract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;


public final class ContractFactoryTest {
    private static final int NUMBER_OF_CLASSES = 3;

    @Test
    public void contractForShouldReturnNullWhenClassIsNotContracted() {
        assertNull(ContractFactory.instance().contractFor(ContractFactoryTest.class));
    }

    @Test
    public void contractForShouldReturnACompositeContractWhenClassIsSubjectToMultipleContracts() {
        assertTrue(ContractFactory.instance().contractFor(SubContractedClass.class) instanceof CompositeContract);
    }

    @Test
    public void contractForShouldReturnASimpleContractWhenClassIsSubjectToASingleContract() {
        assertTrue(ContractFactory.instance().contractFor(TopContractedClass.class) instanceof SimpleContract);
    }

    @Test
    public void contractForShouldIgnoreDuplicateContractedClass() throws ContractClassException, ContractBreachException, NoSuchFieldException, IllegalAccessException {
        assertEquals(NUMBER_OF_CLASSES, subcontracts(ContractFactory.instance().contractFor(OtherSubContractedClass.class)).size());
    }

    @Test
    public void contractImplementationForSingleContractShouldBeTheDesignatedContract() throws NoSuchFieldException, IllegalAccessException {
        final Contract<TopContractedClass> contract = ContractFactory.instance().contractFor(TopContractedClass.class);

        assertTrue(implementation(contract) instanceof TopContract);
    }

    @Test
    public void firstSubContractImplementationsForCompositeContractsShouldBeTheLowestLevelContract() throws NoSuchFieldException, IllegalAccessException {
        final Contract<SubContractedClass> contract = ContractFactory.instance().contractFor(SubContractedClass.class);

        final List<Contract<SubContractedClass>> subcontracts = subcontracts(contract);
        assertTrue(implementation(subcontracts.get(0)) instanceof SubContract);
    }

    @Test
    public void lastSubContractImplementationsForCompositeContractsShouldBeTheHighestLevelContract() throws NoSuchFieldException, IllegalAccessException {
        final Contract<SubContractedClass> contract = ContractFactory.instance().contractFor(SubContractedClass.class);

        final List<Contract<SubContractedClass>> subcontracts = subcontracts(contract);
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
