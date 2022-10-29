package org.simpleframework.mvc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.render.ResultRender;
import org.simpleframework.mvc.render.impl.DefaultResultRender;
import org.simpleframework.mvc.render.impl.InternalErrorResultRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
@Data
@Slf4j
public class RequestProcessorChain {
    private Iterator<RequestProcessor> requestProcessorIterator;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String requestMethod;
    private String requestPath;
    private int requestCode;
    private ResultRender resultRender;

    public RequestProcessorChain(Iterator<RequestProcessor> iterator,
                                 HttpServletRequest req,
                                 HttpServletResponse resp) {
        this.requestProcessorIterator = iterator;
        this.request = req;
        this.response = resp;
        this.requestMethod = req.getMethod();
        this.requestPath = req.getPathInfo();
        this.requestCode = HttpServletResponse.SC_OK;
    }

    public void doRequestProcessorChain() {
        try {
            while (requestProcessorIterator.hasNext()) {
                if (!requestProcessorIterator.next().process(this)) {
                    break;
                }
            }
        } catch (Exception e) {
            this.resultRender = new InternalErrorResultRender(e.getMessage());
            log.error("doRequestProcessorChain error:", e);
        }
    }

    public void doRender() {
        if (this.resultRender == null) {
            this.resultRender = new DefaultResultRender();
        }
        try {
            this.resultRender.render(this);
        } catch (Exception e) {
            log.error("doRender error:", e);
            throw new RuntimeException(e);
        }
    }
}
