package com.digitalbrikes.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ContractFactory {
    private static final ThreadLocal<ContractFactory> FACTORIES = new ThreadLocal<ContractFactory>() {
        @Override
        protected ContractFactory initialValue() {
            return new ContractFactory();
        }
    };

    public static ContractFactory instance() {
        return FACTORIES.get();
    }

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
        for (Class<T> clazz : contractedClasses) {
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
            if (current.isAnnotationPresent(Contracted.class) && !result.contains(current)) {
                result.add((Class<T>) current);
            }
            classes.addAll(ancestors(current));
        }
        return result;
    }

    private Set<Class<?>> ancestors(final Class<?> clazz) {
        Set<Class<?>> result = new HashSet<Class<?>>();
        result.addAll(Arrays.asList(clazz.getInterfaces()));
        if (null != clazz.getSuperclass()) {
            result.add(clazz.getSuperclass());
        }
        return result;
    }
}
