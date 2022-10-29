package com.imooc.entity.dto;

import com.imooc.entity.bo.HeadLine;
import com.imooc.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/17
 */
@Data
public class MainPageInfoDTO {
    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;
}
