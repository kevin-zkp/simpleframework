package org.simpleframework.aop;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
public class AspectListExecutor implements MethodInterceptor {
    // 被代理的类
    private Class<?> targetClass;
    @Getter
    private List<AspectInfo> sortedAspectInfoList;
    /**
     * 构造函数
     * @param targetClass
     * @param aspectInfoList
     */
    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList);
    }
    /**
     * 按照order的值进行升序排序，order值小的aspect先被织入
     * @param aspectInfoList
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex() - o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue = null;
        collectAccurateMatchedAspectList(method);
        if (ValidationUtil.isEmpty(sortedAspectInfoList)) {
            returnValue  = methodProxy.invokeSuper(proxy, args);
            return returnValue;
        }
        //按照order熟悉怒执行完所有Aspect的before方法
        invokeBeforeAdvices(method, args);
        try {
            //执行被代理类的原来的方法
            returnValue = methodProxy.invokeSuper(proxy, args);
            //如果正常返回，按照order顺序执行所有afterReturning方法
            returnValue = invokeAfterReturningAdvices(method, args, returnValue);
        } catch (Exception e) {
            //如果抛出异常，按照order顺序执行所有afterThrowing方法
            invokeAfterThrowingAdvices(method, args, e);
        }
        return returnValue;
    }

    private void collectAccurateMatchedAspectList(Method method) {
        if (ValidationUtil.isEmpty(sortedAspectInfoList)) {
            return;
        }
        Iterator<AspectInfo> it = sortedAspectInfoList.iterator();
        while (it.hasNext()) {
            AspectInfo aspectInfo = it.next();
            if (! aspectInfo.getPointcutLocator().accurateMatches(method)) {
                it.remove();
            }
        }
    }
    /**
     * 降序执行
     * @param method
     * @param args
     * @param e
     * @throws Throwable
     */
    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i --) {
            AspectInfo aspectInfo = sortedAspectInfoList.get(i);
            aspectInfo.getAspectObject().afterThrowing(targetClass, method, args, e);
        }
    }
    /**
     * 根据order降序执行方法
     * @param method
     * @param args
     * @param returnValue
     * @return
     * @throws Throwable
     */
    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
        Object result = null;
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i --) {
            AspectInfo aspectInfo = sortedAspectInfoList.get(i);
            result = aspectInfo.getAspectObject().afterReturning(targetClass, method, args, returnValue);
        }
        return result;
    }
    /**
     * 升序执行
     * @param method
     * @param args
     * @throws Throwable
     */
    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo : sortedAspectInfoList) {
            aspectInfo.getAspectObject().before(targetClass, method, args);
        }
    }
}
