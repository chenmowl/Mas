package com.eme.mas.connection.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by luokaiwen on 15/4/29.
 * <p/>
 * 用于注解每个页面或者碎片
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WLayout {
    // 配置LayoutId
    int layoutId() default -1;
}
