package com.michelle.annotations;

import java.lang.annotation.*;


/**
 * @author michelle.min
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditObjectAnn {
    Class<?> type() default Object.class;

    String name() default "";

    String columns() default "";
}
