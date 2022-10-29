package org.simpleframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.core.annotation.Component;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.core.annotation.Repository;
import org.simpleframework.core.annotation.Service;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/18
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {
    /**
     * 存放所有被标识的目标对象的Map
     */
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();
    /**
     * 加载bean的注解列表
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(Component.class, Controller.class, Service.class, Repository.class, Aspect.class);
    /**
     * 获取Bean容器实例
     * @return
     */
    public static BeanContainer getInstance() {
        return ContainerHolder.Holder.instance;
    }
    private enum ContainerHolder {
        Holder;
        private BeanContainer instance;
        ContainerHolder() {
            instance = new BeanContainer();
        }
    }

    /**
     * 容器是否被加载过
     */
    private boolean loaded = false;

    /**
     * 是否加载过容器
     * @return
     */
    public boolean isLoaded() {
        return loaded;
    }
    /**
     * bean实例数量
     * @return
     */
    public int size() {
        return beanMap.size();
    }
    /**
     * 扫描加载所有Bean
     * @param packageName
     */
    public synchronized void loadBeans(String packageName) {
        //判断容器是否加载过
        if (isLoaded()) {
            log.warn("BeanContainer has been loaded.");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        if (ValidationUtil.isEmpty(classSet)) {
            log.warn("extract nothing from packageName:" + packageName);
            return;
        }
        for (Class<?> clazz : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                // 如果类上面标记了定义的注解
                if (clazz.isAnnotationPresent(annotation)) {
                    // 将目标类本身作为键，目标类的实例作为值，放入到beanMap中
                    beanMap.put(clazz, ClassUtil.newInstance(clazz, true));
                }
            }
        }
        loaded = true;
    }

    /**
     * 添加一个Class对象及其Bean实例
     * @param clazz
     * @param bean
     * @return
     */
    public Object addBean(Class<?> clazz, Object bean) {
        return beanMap.put(clazz, bean);
    }
    /**
     * 移除IOC容器管理的对象
     * @param clazz
     * @return
     */
    public Object removeBean(Class<?> clazz) {
        return beanMap.remove(clazz);
    }

    /**
     * 根据class对象获取Bean实例
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    /**
     * 获取容器管理的所有Class对象集合
     * @return
     */
    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }
    /**
     * 获取所有Bean集合
     * @return
     */
    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    /**
     * 根据注解筛选出Bean的Class集合
     * @param annotation
     * @return
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            if (clazz.isAnnotationPresent(annotation)) {
                classSet.add(clazz);
            }
        }
        //为了返回值统一
        return classSet.size() > 0 ? classSet : null;
    }

    /**
     *通过接口或者父类获取类或者子类的Class集合，不包括本身
     * @param interfaceOrClass
     * @return
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass) {
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)) {
            log.warn("nothing in beanMap");
            return null;
        }
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : keySet) {
            if (interfaceOrClass.isAssignableFrom(clazz) && ! clazz.equals(interfaceOrClass)) {
                classSet.add(clazz);
            }
        }
        //为了返回值统一
        return classSet.size() > 0 ? classSet : null;
    }
}
