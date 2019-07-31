package com.cold.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Auther: ohj
 * @Date: 2019/7/25 08:23
 * @Description:Token 注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {

    /**
     * 添加token的开关[true：添加；false:不添加，default：false]
     *
     * @return
     */
    boolean add() default false;

    /**
     * 移除token的开关[true：删除；false:不删除，default：false]
     *
     * @return
     */
    boolean remove() default false;

}