package com.digitalbrikes.contract;

import junit.framework.TestCase;

public final class PostconditionTest extends TestCase {
    public void testPostconditionForVoidMethodDoesNotHaveAReturnParameter() throws NoSuchMethodException {
        final Postcondition testedCondition = new Postcondition(ContractedClass.class.getMethod("contractedMethod", new Class[] {Double.class}), new LocalContract());

        assertTrue(testedCondition.verify(new ContractedClass(), new Object[] {new Double(0)}, null));
    }

    public void testPostconditionIsBreachedWhenParametersAreOfTheWrongType() throws NoSuchMethodException {
        final Postcondition testedCondition = new Postcondition(ContractedClass.class.getMethod("contractedMethod", new Class[] {Double.class}), new LocalContract());

        try {
            testedCondition.verify(new ContractedClass(), new Object[] {new String()}, null);
            fail("Expected a " + ContractBreachException.class);
        } catch (ContractBreachException e) {
            assertTrue(e.getMessage().contains(LocalContract.class.getName() + ".postcondition"));
        }
    }

    public void testPostconditionIsBreachedWhenContractIsNotAccessible() throws NoSuchMethodException {
        final Postcondition testedCondition = new Postcondition(ContractedClass.class.getMethod("wrongMethod", new Class[] {Object.class}), new LocalContract());

        try {
            testedCondition.verify(new ContractedClass(), new Object[] {new String()}, null);
            fail("Expected a " + ContractBreachException.class);
        } catch (ContractBreachException e) {
            assertTrue(e.getMessage().contains(LocalContract.class.getName() + ".privatecondition"));
        }
    }

    public void testPostconditionIsBreachedWhenContractThrowsException() throws NoSuchMethodException {
        final Postcondition testedCondition = new Postcondition(ContractedClass.class.getMethod("throwingMethod", new Class[] {Double.class}), new LocalContract());

        try {
            testedCondition.verify(new ContractedClass(), new Object[] {new Double(0.)}, null);
            fail("Expected a " + ContractBreachException.class);
        } catch (ContractBreachException e) {
            assertTrue(e.getMessage().contains(LocalContract.class.getName() + ".throwingcondition"));
        }
    }

    @Contracted(contract = LocalContract.class)
    public static final class ContractedClass {
        private Object test = null;
        @PostConditioned(postcondition = "postcondition")
        public void contractedMethod(final Double value) {
            test = value;
        }
        @PostConditioned(postcondition = "throwingcondition")
        public void throwingMethod(final Double value) {
            test = value;
        }
        @PostConditioned(postcondition = "privatecondition")
        public void wrongMethod(final Object value) {
            test = value;
        }

        public boolean testIsNull() {
            return (null == test);
        }
    }

    public static final class LocalContract {
        public boolean postcondition(final ContractedClass contracted, final Double value) {
            return contracted.testIsNull();
        }

        public boolean throwingcondition(final ContractedClass contracted, final Double value) {
            throw new NullPointerException();
        }

        private boolean privatecondition(final ContractedClass contracted, final Object value) {
            return contracted.testIsNull();
        }
    }
}
