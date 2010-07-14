package com.digitalbrikes.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods marked with this annotation are subject to a postcondition.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostConditioned {
    /**
     * @return the name of the postcondition method to apply.
     */
    String postcondition();
}
