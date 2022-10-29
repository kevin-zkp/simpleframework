package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
public class InternalErrorResultRender implements ResultRender {
    private String errorMsg;
    public InternalErrorResultRender(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
    }
}
