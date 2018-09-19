package com.dsxx.base.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标记导出Excel的VO对象字段所对应的列
 *
 * @author slm
 * @date 2018/7/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {

    /**
     * 列名
     */
    String name() default "";

    /**
     * 列的顺序 从0开始
     */
    int order() default 0;
}
