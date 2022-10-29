package org.simpleframework.mvc.processor;

import org.simpleframework.mvc.RequestProcessorChain;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
public interface RequestProcessor {
    boolean process(RequestProcessorChain requestProcessorChain) throws Exception;
}
