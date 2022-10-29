package org.simpleframework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.simpleframework.aop.PointcutLocator;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/19
 */
@AllArgsConstructor
@Getter
public class AspectInfo {
    private int orderIndex;
    private DefaultAspect aspectObject;
    private PointcutLocator pointcutLocator;
}
