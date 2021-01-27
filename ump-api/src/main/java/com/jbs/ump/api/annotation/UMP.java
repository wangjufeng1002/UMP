package com.jbs.ump.api.annotation;

import java.lang.annotation.*;

/**
 * @created by wjf
 * @date 2019/12/25 17:24
 * @description:
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UMP {
    boolean enable() default true;
}
