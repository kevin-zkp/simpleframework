package org.simpleframework.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.aop.mock.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
public class AspectListExecutorTest {

    @DisplayName("Aspect排序：sortedAspectList")
    @Test
    public void sortTest() {
        // List<AspectInfo> aspectInfoList = new ArrayList<>();
        // aspectInfoList.add(new AspectInfo(1, new Mock1()));
        // aspectInfoList.add(new AspectInfo(2, new Mock2()));
        // aspectInfoList.add(new AspectInfo(3, new Mock3()));
        // aspectInfoList.add(new AspectInfo(4, new Mock4()));
        // aspectInfoList.add(new AspectInfo(5, new Mock5()));
        // AspectListExecutor aspectListExecutor = new AspectListExecutor(AspectListExecutorTest.class, aspectInfoList);
        // List<AspectInfo> sortedAspectInfoList = aspectListExecutor.getSortedAspectInfoList();
        // for (AspectInfo aspectInfo : sortedAspectInfoList) {
        //     System.out.println(aspectInfo.getAspectObject().getClass().getName());
        // }
    }
}
