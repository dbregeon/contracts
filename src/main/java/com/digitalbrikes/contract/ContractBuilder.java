package com.digitalbrikes.contract;


final class ContractBuilder {
    private static final ContractBuilder INSTANCE = new ContractBuilder();

    static ContractBuilder instance() {
        return INSTANCE;
    }

    private ContractBuilder() {
        // Hide the constructor
    }

    <T> Object buildContractFor(final Class<T> contractedClass) {
        return buildInstanceOf(contractClass(contractedClass));
    }

    private <T> Class<?> contractClass(final Class<T> clazz) {
        return clazz.getAnnotation(Contracted.class).contract();
    }

    private Object buildInstanceOf(final Class<?> contractClass) {
        try {
            return contractClass.newInstance();
        } catch (final InstantiationException e) {
            throw new ContractClassException(ContractClassException.ErrorType.CONTRACT_INSTANTIATION, contractClass, e);
        } catch (final IllegalAccessException e) {
            throw new ContractClassException(ContractClassException.ErrorType.CONTRACT_ACCESS, contractClass, e);
        }
    }
}
