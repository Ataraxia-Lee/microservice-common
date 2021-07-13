package com.ataraxia.microservices.annotation;

import java.lang.annotation.*;

/**
 * 接口限流规则
 *
 * @author lilong
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitLimit {

    //标识 指定sec时间段内的访问次数限制
    int limit() default 5;

    //标识 时间段
    long sec() default 5;

}
