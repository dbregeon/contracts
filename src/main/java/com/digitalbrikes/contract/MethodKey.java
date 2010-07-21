package com.digitalbrikes.contract;

import java.lang.reflect.Method;
import java.util.Arrays;

final class MethodKey {
    private final String name;
    private final Class<?>[] parameterTypes;

    @Override
    public int hashCode() {
        return name.hashCode() ^ Arrays.hashCode(parameterTypes);
    }

    @Override
    public boolean equals(final Object obj) {
        final MethodKey other = (MethodKey) obj;
        return name.equals(other.name) && Arrays.equals(parameterTypes, other.parameterTypes);
    }

    MethodKey(final Method method) {
        name = method.getName();
        parameterTypes = method.getParameterTypes();
    }

}
