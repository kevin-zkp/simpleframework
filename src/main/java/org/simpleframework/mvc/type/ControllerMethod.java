package org.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerMethod {
    private Class<?> controllerClass;
    private Method invokeMethod;
    private Map<String, Class<?>> methodParameters;
}
