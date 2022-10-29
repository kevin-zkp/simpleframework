package org.simpleframework.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * 解析Aspect表达式并且定位被织入目标
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
public class PointcutLocator {
    /**
     * Point解析器
     */
    private PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            PointcutParser.getAllSupportedPointcutPrimitives()
    );
    /**
     * 表达式解析器
     */
    private PointcutExpression pointcutExpression;
    /**
     * 无参构造方法
     * @param expression
     */
    public PointcutLocator(String expression) {
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }
    /**
     * 判断传入的Class对象是否是Aspect的目标代理类，即匹配Pointcut表达式
     * @param targetClass
     * @return
     */
    public boolean roughMatches(Class<?> targetClass) {
        //只能校验within，不能校验execution，get，set，call，这些直接返回true
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }
    /**
     * 判断传入的Method对象是否是Aspect的目标代理方法，即匹配Pointcut表达式
     * @param method
     * @return
     */
    public boolean accurateMatches(Method method) {
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
        return shadowMatch.alwaysMatches();
    }
}
