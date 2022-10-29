package org.simpleframework.aop.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    /**
     * 需要被织入横切逻辑的注解标签
     * @return
     */
    // Class<? extends Annotation> value();

    String pointcut();
}
