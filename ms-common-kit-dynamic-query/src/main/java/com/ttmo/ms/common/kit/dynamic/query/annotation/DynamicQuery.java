package com.ttmo.ms.common.kit.dynamic.query.annotation;

import java.lang.annotation.*;

/**
 * @author Jover Zhang
 * @see com.ttmo.ms.common.kit.dynamic.query.aspect.DynamicQueryAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicQuery {

    String value();

}
