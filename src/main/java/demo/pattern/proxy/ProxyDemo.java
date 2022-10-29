package demo.pattern.proxy;

import demo.pattern.proxy.cglib.AlipayMethodInterceptor;

import demo.pattern.proxy.cglib.CglibUtil;
import demo.pattern.proxy.impl.CommonPayment;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
public class ProxyDemo {
    public static void main(String[] args) {
        CommonPayment commonPayment = new CommonPayment();
        AlipayMethodInterceptor alipayMethodInterceptor = new AlipayMethodInterceptor();
        CommonPayment payment = CglibUtil.createProxy(commonPayment, alipayMethodInterceptor);
        payment.pay();
    }
}
