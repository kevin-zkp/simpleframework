package org.simpleframework.mvc.render;

import org.simpleframework.mvc.RequestProcessorChain;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
public interface ResultRender {
    public void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
