package demo.pattern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
public class CglibUtil {
    public static <T> T createProxy(T targetObject, MethodInterceptor methodInterceptor) {
        return (T) Enhancer.create(targetObject.getClass(), methodInterceptor);
    }
}
