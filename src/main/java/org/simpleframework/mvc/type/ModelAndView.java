package org.simpleframework.mvc.type;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储结果数据，以及显示该数据的视图
 * @description:
 * @author: ZKP
 * @time: 2022/10/26
 */
@Getter
public class ModelAndView {
    private String view;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView setView(String view) {
        this.view = view;
        return this;
    }
    public ModelAndView addViewData(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }
}
