package com.dsxx.base.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 过滤Controller返回的字段
 * 也可以做其他操作，比如字段加密
 * 使用方法 在Controller的方法上加上注解 @FieldsFilter(excludes={"addTime","updateTime","password"})
 *
 * @author slm
 * @date 2017/9/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsFilter {
    /**
     * 需要返回的字段
     *
     * @return
     */
    String[] includes() default {};

    /**
     * 需要去除的字段
     *
     * @return
     */
    String[] excludes() default {};
}
