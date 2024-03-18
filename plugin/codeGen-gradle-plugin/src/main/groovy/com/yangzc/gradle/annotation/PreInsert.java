package com.yangzc.gradle.annotation;

import java.lang.annotation.*;

/**
 * insert 前执行方法
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PreInsert {

}
