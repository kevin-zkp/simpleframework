package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
public class DefaultResultRender implements ResultRender {
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        int requestCode = requestProcessorChain.getRequestCode();
        requestProcessorChain.getResponse().setStatus(requestCode);
    }
}
