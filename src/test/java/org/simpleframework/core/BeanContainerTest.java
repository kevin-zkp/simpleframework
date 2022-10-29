package org.simpleframework.core;

import org.simpleframework.mvc.DispatcherServlet;
import com.imooc.controller.frontend.MainPageController;
import org.junit.jupiter.api.*;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/18
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {

    private static BeanContainer beanContainer;
    @BeforeAll
    static void init() {
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName("加载目标类及其实例到BeanContainer：loadBeanTest")
    @Order(1)
    @Test
    public void loadBeansTest() {
        Assertions.assertEquals(false, beanContainer.isLoaded());
        beanContainer.loadBeans("com.imooc");
        Assertions.assertEquals(6, beanContainer.size());
        Assertions.assertEquals(true, beanContainer.isLoaded());
    }

    @DisplayName("根据类获取其实例：getBeanTest")
    @Order(2)
    @Test
    public void getBeanTest() {
        System.out.println(beanContainer.getBeans());
        MainPageController controller = (MainPageController) beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true, controller instanceof MainPageController);
        // System.out.println(controller);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) beanContainer.getBean(DispatcherServlet.class);
        Assertions.assertEquals(null, dispatcherServlet);
    }
}
