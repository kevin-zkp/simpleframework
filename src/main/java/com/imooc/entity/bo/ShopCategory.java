package com.imooc.entity.bo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/16
 */
@Data
public class ShopCategory {
    private Long shopCategoryId;
    private String shopCategoryName;
    private String shopCategoryDesc;
    private String shopCategoryImg;
    private Integer priority;
    private Date createTime;
    private Date lastEditTime;
    private ShopCategory parent;
}
