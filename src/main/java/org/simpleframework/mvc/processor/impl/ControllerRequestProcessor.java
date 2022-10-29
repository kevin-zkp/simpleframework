package org.simpleframework.mvc.processor.impl;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.BeanContainer;
import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.annotation.RequestMapping;
import org.simpleframework.mvc.annotation.RequestParam;
import org.simpleframework.mvc.annotation.ResponseBody;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.render.ResultRender;
import org.simpleframework.mvc.render.impl.JsonResultRender;
import org.simpleframework.mvc.render.impl.ResourceNotFoundResultRender;
import org.simpleframework.mvc.render.impl.ViewResultRender;
import org.simpleframework.mvc.type.ControllerMethod;
import org.simpleframework.mvc.type.RequestPathInfo;
import org.simpleframework.util.ConvertUtil;
import org.simpleframework.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
@Slf4j
public class ControllerRequestProcessor implements RequestProcessor {
    private BeanContainer beanContainer;
    private Map<RequestPathInfo, ControllerMethod> pathControllerMethodMap = new ConcurrentHashMap<>();

    public ControllerRequestProcessor() {
        this.beanContainer = BeanContainer.getInstance();
        Set<Class<?>> requestMappingSet = beanContainer.getClassesByAnnotation(RequestMapping.class);
        initPathControllerMethodMap(requestMappingSet);
    }

    private void initPathControllerMethodMap(Set<Class<?>> requestMappingSet) {
        if (ValidationUtil.isEmpty(requestMappingSet)) {
            return;
        }
        for (Class<?> requestMappingClass : requestMappingSet) {
            RequestMapping requestMapping = requestMappingClass.getAnnotation(RequestMapping.class);
            String basePath = requestMapping.value();
            if (!basePath.startsWith("/")) {
                basePath = "/" + basePath;
            }
            Method[] methods = requestMappingClass.getDeclaredMethods();
            if (ValidationUtil.isEmpty(methods)) {
                continue;
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodRequest = method.getAnnotation(RequestMapping.class);
                    String methodPath = methodRequest.value();
                    if (!methodPath.startsWith("/")) {
                        methodPath = "/" + methodPath;
                    }
                    String url = basePath + methodPath;
                    Map<String, Class<?>> methodParams = new HashMap<>();
                    Parameter[] parameters = method.getParameters();
                    if (!ValidationUtil.isEmpty(parameters)) {
                        for (Parameter parameter : parameters) {
                            RequestParam param = parameter.getAnnotation(RequestParam.class);
                            if (param == null) {
                                throw new RuntimeException("The parameter must have @RequestParam");
                            }
                            methodParams.put(param.value(), parameter.getType());
                        }
                    }
                    String httpMethod = String.valueOf(methodRequest.method());
                    RequestPathInfo requestPathInfo = new RequestPathInfo(httpMethod, url);
                    if (this.pathControllerMethodMap.containsKey(requestPathInfo)) {
                        log.warn("duplicate uil:{}registration, current class {} method {} will override the former one",
                                requestPathInfo.getHttpPath(), requestMappingClass.getName(), method.getName());
                    }
                    ControllerMethod controllerMethod = new ControllerMethod(requestMappingClass, method, methodParams);
                    this.pathControllerMethodMap.put(requestPathInfo, controllerMethod);
                }
            }
        }
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        String method = requestProcessorChain.getRequestMethod();
        String path = requestProcessorChain.getRequestPath();
        ControllerMethod controllerMethod = this.pathControllerMethodMap.get(new RequestPathInfo(method, path));
        if (controllerMethod == null) {
            requestProcessorChain.setResultRender(new ResourceNotFoundResultRender(method, path));
            return false;
        }
        Object result = invokeControllerMethod(controllerMethod, requestProcessorChain.getRequest());
        setResultRender(result, controllerMethod, requestProcessorChain);
        return true;
    }

    private void setResultRender(Object result, ControllerMethod controllerMethod, RequestProcessorChain requestProcessorChain) {
        if (result == null) {
            return;
        }
        ResultRender resultRender;
        boolean isJson = controllerMethod.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            resultRender = new JsonResultRender(result);
        } else {
            resultRender = new ViewResultRender(result);
        }
        requestProcessorChain.setResultRender(resultRender);
    }

    private Object invokeControllerMethod(ControllerMethod controllerMethod, HttpServletRequest request) {
        Map<String, String> requestParamMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> parameter : parameterMap.entrySet()) {
            if (!ValidationUtil.isEmpty(parameter.getValue())) {
                requestParamMap.put(parameter.getKey(), parameter.getValue()[0]);
            }
        }
        List<Object> methodParams = new ArrayList<>();
        Map<String, Class<?>> methodParamMap = controllerMethod.getMethodParameters();
        for (String paramName : methodParamMap.keySet()) {
            Class<?> type = methodParamMap.get(paramName);
            String requestValue = requestParamMap.get(paramName);
            Object value;
            if (requestValue == null) {
                value = ConvertUtil.primitiveNull(type);
            } else {
                System.out.println(requestValue);
                value = ConvertUtil.convert(type, requestValue);
            }
            methodParams.add(value);
        }
        Object controller = beanContainer.getBean(controllerMethod.getControllerClass());
        Method invokeMethod = controllerMethod.getInvokeMethod();
        invokeMethod.setAccessible(true);
        Object result;
        try {
            if (methodParams.size() == 0) {
                result = invokeMethod.invoke(controller);
            } else {
                result = invokeMethod.invoke(controller, methodParams.toArray());
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


}
