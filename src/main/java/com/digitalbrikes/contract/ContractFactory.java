package com.digitalbrikes.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Factory that enables to build Contracts for specific types. ContractFactories are
 *  provided through a ThreadLocal instance and are therefore thread safe.
 */
final class ContractFactory {
    private static final ThreadLocal<ContractFactory> FACTORIES = new ThreadLocal<ContractFactory>() {
        @Override
        protected ContractFactory initialValue() {
            return new ContractFactory();
        }
    };

    /**
     * @return the instance of the ContractFactory for this thread.
     */
    public static ContractFactory instance() {
        return FACTORIES.get();
    }

    private ContractFactory() {
        // Hide the constructor.
        super();
    }

    /**
     * Provides access to this factory's instance of the contract for a particular type.
     * @param <T> the type that will be the subject of this contract.
     * @param clazz the class that represents the type that is the subject of this contract.
     * @return the contract implementation for the given type.
     * @throws ContractClassException when the contract cannot be instantiated.
     * @throws ContractBreachException when there is an inconsistency between the contract specification
     * in type <T> and the implementation of that contract (e.g. missing condition, incompatible
     * parameter types).
     */
    public <T> Contract<T> contractFor(final Class<?> clazz) throws ContractClassException, ContractBreachException {
        final List<Class<T>> contractedClasses = contractedClass(clazz);
        Contract<T> result = null;
        if (1 == contractedClasses.size()) {
            result = simpleContractFor(contractedClasses.get(0));
        } else if (!contractedClasses.isEmpty()) {
            result = compositeContractFor(contractedClasses);
        }
        return result;
    }

    private <T> Contract<T> compositeContractFor(final List<Class<T>> contractedClasses) throws ContractClassException, ContractBreachException {
        return new CompositeContract<T>(simpleContractsFor(contractedClasses));
    }

    private <T> List<Contract<T>> simpleContractsFor(final List<Class<T>> contractedClasses) {
        final List<Contract<T>> subcontracts = new ArrayList<Contract<T>>();
        for (final Class<T> clazz : contractedClasses) {
            subcontracts.add(simpleContractFor(clazz));
        }
        return subcontracts;
    }

    private <T> SimpleContract<T> simpleContractFor(final Class<T> clazz) throws ContractClassException, ContractBreachException {
        return new SimpleContract<T>(clazz);
    }

    private <T> List<Class<T>> contractedClass(final Class<?> clazz) {
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        final List<Class<T>> result = new ArrayList<Class<T>>();
        classes.add(clazz);
        while (!classes.isEmpty()) {
            final Class<?> current = classes.remove(0);
            addNewContractedClassToResult(result, current);
            classes.addAll(ancestors(current));
        }
        return result;
    }

    private <T> void addNewContractedClassToResult(final List<Class<T>> result,
            final Class<?> current) {
        if (current.isAnnotationPresent(Contracted.class) && !result.contains(current)) {
            result.add((Class<T>) current);
        }
    }

    private Set<Class<?>> ancestors(final Class<?> clazz) {
        final Set<Class<?>> result = new HashSet<Class<?>>();
        result.addAll(Arrays.asList(clazz.getInterfaces()));
        if (null != clazz.getSuperclass()) {
            result.add(clazz.getSuperclass());
        }
        return result;
    }
}
