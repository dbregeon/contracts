package com.digitalbrikes.contract;

import java.lang.reflect.Method;

public enum ConditionType {
    PRE, POST;

    public String conditionMethodName(final Method method) {
        final StringBuilder result = new StringBuilder(prefix());
        result.append(capitalizedName(method));
        return result.toString();
    }

    private String capitalizedName(final Method method) {
        final String name = method.getName();
        final String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
        return capitalizedName;
    }

    private String prefix() {
        return name().toLowerCase();
    }
}
