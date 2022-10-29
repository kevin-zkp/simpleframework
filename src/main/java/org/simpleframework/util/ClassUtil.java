package org.simpleframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/18
 */
@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";

    public static Set<Class<?>> extractPackageClass(String packageName) {
        //获取到类加载器
        ClassLoader classLoader = getClassLoader();
        //获取加载的资源
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            log.warn("unable to retrieve anything from package:" + packageName);
            return null;
        }
        //根据不同资源类型，采用不同方式获取资源的集合
        Set<Class<?>> classSet = null;
        //过滤出文件类型的资源
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            classSet = new HashSet<Class<?>>();
            File packageDirectory = null;
            try {
                packageDirectory = new File(URLDecoder.decode(url.getPath(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            extractClassFile(classSet, packageDirectory, packageName);
        }
        // TODO 此处加入针对其他类型资源的处理
        return classSet;
    }

    /**
     * 递归获取目标package里面的所有class文件，包括子文件夹里的class文件
     * @param emptyClassSet
     * @param fileSource
     * @param packageName
     */
    private static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName) {
        if (! fileSource.isDirectory()) {
            return;
        }
        // 如果是文件夹，则调用listFiles获取文件夹下的文件或文件夹
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String absoluteFilePath = file.getAbsolutePath();
                    if (absoluteFilePath.endsWith(".class")) {
                        addToClassSet(absoluteFilePath);
                    }
                }
                return false;
            }

            private void addToClassSet(String absoluteFilePath) {
                // 提取类名
                absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
                String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                className = className.substring(0,className.lastIndexOf("."));
                // 通过反射获取Class对象并加入到classSet
                Class<?> targetClass = loadClass(className);
                emptyClassSet.add(targetClass);
            }
        });
        if (files != null) {
            for (File f : files) {
                extractClassFile(emptyClassSet, f, packageName);
            }
        }
    }

    /**
     *
     * @param className = package + 类名
     * @return
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 实例化class
     * @param clazz
     * @param accessible 是否支持创建出私有Class对象的实例
     * @param <T> class的类型
     * @return
     */
    public static <T> T newInstance(Class<?> clazz, boolean accessible) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            log.error("newInstance error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取ClassLoader
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static void main(String[] args) {
        extractPackageClass("com.imooc.entity");
    }
    public static void setField(Field field, Object target, Object value, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.error("setField error", e);
            throw new RuntimeException(e);
        }
    }
}
