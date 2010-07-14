package com.digitalbrikes.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes marked with this annotation are signaled as subject to a contract.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Contracted {
    /**
     * @return the class that implements the contract for the annotated class.
     */
    Class<?> contract();
}
