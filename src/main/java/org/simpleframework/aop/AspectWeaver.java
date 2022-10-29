package org.simpleframework.aop;

import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.annotation.Order;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.aop.aspect.DefaultAspect;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
public class AspectWeaver {
    private BeanContainer beanContainer;
    public AspectWeaver() {
        this.beanContainer = BeanContainer.getInstance();
    }

    /**
     * 织入逻辑
     */
    public void doAop() {
        //获取所有的切面类
        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
        if (ValidationUtil.isEmpty(aspectSet)) {
            return;
        }
        //2.0
        //拼装AspectInfoList
        List<AspectInfo> aspectInfoList = packAspectInfoList(aspectSet);
        //遍历容器里的类
        Set<Class<?>> classSet = beanContainer.getClasses();
        for (Class<?> targetClass : classSet) {
            if (targetClass.isAnnotationPresent(Aspect.class)) {
                continue;
            }
            //粗筛出符合条件的Aspect
            List<AspectInfo> roughMatchedAspectList = collectRoughMatchedAspectListForSpecificClass(aspectInfoList, targetClass);
            //尝试Aspect织入
            wrapIfNecessary(roughMatchedAspectList, targetClass);
        }
        //1.0
        //根据属性值进行归类，例如Controller，Service
        // Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap = new HashMap<>();
        // if (ValidationUtil.isEmpty(aspectSet)) {
        //     return;
        // }
        // for (Class<?> aspectClass : aspectSet) {
        //     if (verifyAspect(aspectClass)) {
        //         categorizeAspect(categorizedMap, aspectClass);
        //     } else {
        //         throw new RuntimeException("@Aspect和@Order没有被加到Aspect类上，或者Aspect类没有继承DefaultAspect，或者Aspect的值等于@Aspect");
        //     }
        // }
        // if (ValidationUtil.isEmpty(categorizedMap)) {
        //     return;
        // }
        // 根据不同织入目标分别按需织入Aspect逻辑
        // for (Class<? extends Annotation> category : categorizedMap.keySet()) {
        //     weaveByCategory(category, categorizedMap.get(category));
        // }
    }

    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        if (ValidationUtil.isEmpty(roughMatchedAspectList)) {
            return;
        }
        AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, roughMatchedAspectList);
        Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
        beanContainer.addBean(targetClass, proxyBean);
    }

    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList = new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfoList) {
            if (aspectInfo.getPointcutLocator().roughMatches(targetClass)) {
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> aspectInfoList = new ArrayList<>();
        for (Class<?> aspectClass : aspectSet) {
            if (verifyAspect(aspectClass)) {
                Order orderTag = aspectClass.getAnnotation(Order.class);
                Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
                DefaultAspect defaultAspect = (DefaultAspect) beanContainer.getBean(aspectClass);
                PointcutLocator pointcutLocator = new PointcutLocator(aspectTag.pointcut());
                AspectInfo aspectInfo = new AspectInfo(orderTag.value(), defaultAspect, pointcutLocator);
                aspectInfoList.add(aspectInfo);
            } else {
                throw new RuntimeException("@Aspect and @Order must be added to the Aspect class, and Aspect class must extend from DefaultAspect");
            }
        }
        return aspectInfoList;
    }

    /**
     * 根据类别织入，每个类别下有多个类，如Controller下有多个类，依次织入Aspect
     * @param category
     * @param aspectInfoList
     */
    // private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfoList) {
    //     //获取被代理类的集合
    //     Set<Class<?>> classSet = beanContainer.getClassesByAnnotation(category);
    //     if (ValidationUtil.isEmpty(classSet)) {
    //         return;
    //     }
    //     //遍历被代理类，分别为每个被代理类生成动态代理实例
    //     for (Class<?> targetClass :classSet) {
    //         AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, aspectInfoList);
    //         Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
    //         //将动态代理对象实例添加到容器里，取代未被代理前的类实例
    //         beanContainer.addBean(targetClass, proxyBean);
    //     }
    // }
    /**
     * 根据目标类分类加入map中，例如Controller为一类
     * @param categorizedMap
     * @param aspectClass
     */
    // private void categorizeAspect(Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap, Class<?> aspectClass) {
    //     Order orderTag = aspectClass.getAnnotation(Order.class);
    //     Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
    //     DefaultAspect aspect = (DefaultAspect) beanContainer.getBean(aspectClass);
    //     AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect);
    //     if (categorizedMap.containsKey(aspectTag.value())) {
    //         List<AspectInfo> aspectInfoList = new ArrayList<>();
    //         aspectInfoList.add(aspectInfo);
    //         categorizedMap.put(aspectTag.value(), aspectInfoList);
    //     } else {
    //         List<AspectInfo> aspectInfoList = categorizedMap.get(aspectTag.value());
    //         aspectInfoList.add(aspectInfo);
    //     }
    // }
    /**
     * 1、类上有@Aspect标签
     * 2、类上有@Order标签
     * 3、类必须继承自DefaultAspect
     * 4、Aspect属性值不能是Aspect，可以是Controller等
     * @param aspectClass
     * @return
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class)
                && aspectClass.isAnnotationPresent(Order.class)
                && DefaultAspect.class.isAssignableFrom(aspectClass);
    }
}
