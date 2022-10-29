package org.simpleframework.mvc.render.impl;

import org.simpleframework.mvc.RequestProcessorChain;
import org.simpleframework.mvc.render.ResultRender;
import org.simpleframework.mvc.type.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/20
 */
public class ViewResultRender implements ResultRender {
    private ModelAndView modelAndView;
    public ViewResultRender(Object mv) {
        if (mv instanceof ModelAndView) {
            this.modelAndView = (ModelAndView) mv;
        } else if (mv instanceof String) {
            this.modelAndView = new ModelAndView().setView((String) mv);
        } else {
            throw new RuntimeException("illegal request result type");
        }
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest request = requestProcessorChain.getRequest();
        HttpServletResponse response = requestProcessorChain.getResponse();
        String path = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        request.getRequestDispatcher("/templates/" + path).forward(request, response);
    }
}
