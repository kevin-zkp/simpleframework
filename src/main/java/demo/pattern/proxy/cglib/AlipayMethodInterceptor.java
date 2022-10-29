package demo.pattern.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
public class AlipayMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        beforePay();
        Object result = methodProxy.invokeSuper(o, args);
        afterPay();
        return result;
    }
    private void beforePay() {
        System.out.println("before");
    }
    private void afterPay() {
        System.out.println("after");
    }
}
