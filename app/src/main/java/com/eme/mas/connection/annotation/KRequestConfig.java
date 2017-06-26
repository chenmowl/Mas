package com.eme.mas.connection.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hongjian.Liu 2:57:31 PM Mar 6, 2014
 *         <p/>
 *         Annotation to config action
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KRequestConfig {
    int index();

    int method();

    String url();

    Class clazz();

}
