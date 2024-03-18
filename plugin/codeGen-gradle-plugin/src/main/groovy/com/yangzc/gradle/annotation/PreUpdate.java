package com.yangzc.gradle.annotation;

import java.lang.annotation.*;

/**
 * update 前执行方法
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PreUpdate {

}
