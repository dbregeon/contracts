package com.digitalbrikes.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public <T> Contract<T> contractFor(final Class<?> clazz) {
        final Class<T> contractedClass = contractedClass(clazz);
        Contract<T> result = null;
        if (null != contractedClass) {
            result = new Contract<T>(contractedClass);
        }
        return result;
    }

    private <T> Class<T> contractedClass(final Class<?> clazz) {
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        Class<T> result = null;
        classes.add(clazz);
        while (null == result && !classes.isEmpty()) {
            final Class<?> current = classes.remove(0);
            if (current.isAnnotationPresent(Contracted.class)) {
                result = (Class<T>) current;
            } else {
                classes.addAll(Arrays.asList(current.getInterfaces()));
                if (null != current.getSuperclass()) {
                    classes.add(current.getSuperclass());
                }
            }
        }
        return result;
    }
}
