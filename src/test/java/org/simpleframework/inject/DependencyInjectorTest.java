package org.simpleframework.inject;

import com.imooc.controller.frontend.MainPageController;
import com.imooc.service.combine.HeadLineShopCategoryCombineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContainer;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/18
 */
public class DependencyInjectorTest {
    @DisplayName("依赖注入doIoc")
    @Test
    public void doIocTest() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.imooc");
        Assertions.assertEquals(true, beanContainer.isLoaded());
        MainPageController mainPageController = (MainPageController) beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true, mainPageController instanceof MainPageController);
        Assertions.assertEquals(null, mainPageController.getHeadLineShopCategoryCombineService());
        new DependencyInjector().doIoc();
        Assertions.assertNotEquals(null, mainPageController.getHeadLineShopCategoryCombineService());
    }
}
