package org.simpleframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 所有切面要继承这个类
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
public abstract class DefaultAspect {
    /**
     * 事前拦截
     * @param targetClass 被代理的目标类
     * @param method 被代理的目标方法
     * @param args 被代理的目标方法的参数列表
     * @throws Throwable
     */
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {

    }
    /**
     * 事后拦截
     * @param targetClass
     * @param method
     * @param args
     * @param returnValue 被代理的目标方法执行后的返回值
     * @throws Throwable
     */
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        return returnValue;
    }
    /**
     *
     * @param targetClass
     * @param method
     * @param args
     * @param e 被代理的目标方法抛出的异常
     * @throws Throwable
     */
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable e) throws Throwable {

    }
}
