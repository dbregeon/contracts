package com.digitalbrikes.contract;

import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public final class PostconditionTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void postconditionForVoidMethodShouldNotHaveAReturnParameter() throws NoSuchMethodException {
        final Postcondition<ContractedClass> testedCondition = new Postcondition<ContractedClass>(ContractedClass.class.getMethod("contractedMethod", new Class[] {Double.class}), new LocalContract());

        assertTrue(testedCondition.verify(new ContractedClass(), new Object[] {new Double(0)}, null));
    }

    @Test
    public void postconditionShouldBeBreachedWhenParametersAreOfTheWrongType() throws NoSuchMethodException {
        expects(ContractBreachException.class).andItsMessage(containsString(LocalContract.class.getName() + ".postcondition"));

        final Postcondition<ContractedClass> testedCondition = new Postcondition<ContractedClass>(ContractedClass.class.getMethod("contractedMethod", new Class[] {Double.class}), new LocalContract());

        testedCondition.verify(new ContractedClass(), new Object[] {new String()}, null);
    }

    @Test
    public void postconditionShouldBeBreachedWhenContractIsNotAccessible() throws NoSuchMethodException {
        expects(ContractBreachException.class).andItsMessage(containsString(LocalContract.class.getName() + ".privatecondition"));

        final Postcondition<ContractedClass> testedCondition = new Postcondition<ContractedClass>(ContractedClass.class.getMethod("wrongMethod", new Class[] {Object.class}), new LocalContract());

        testedCondition.verify(new ContractedClass(), new Object[] {new String()}, null);
    }

    @Test
    public void postconditionShouldBeBreachedWhenContractThrowsException() throws NoSuchMethodException {
        expects(ContractBreachException.class).andItsMessage(containsString(LocalContract.class.getName() + ".throwingcondition"));

        final Postcondition<ContractedClass> testedCondition = new Postcondition<ContractedClass>(ContractedClass.class.getMethod("throwingMethod", new Class[] {Double.class}), new LocalContract());

        testedCondition.verify(new ContractedClass(), new Object[] {new Double(0.)}, null);
    }

    private void andItsMessage(final Matcher<String> containsString) {
        thrown.expectMessage(containsString);
    }

    private PostconditionTest expects(final Class<? extends Throwable> exceptionClass) {
        thrown.expect(exceptionClass);
        return this;
    }
}
