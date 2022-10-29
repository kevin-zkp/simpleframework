package com.imooc.service.combine;

import com.imooc.entity.dto.MainPageInfoDTO;
import com.imooc.entity.dto.Result;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/17
 */
public interface HeadLineShopCategoryCombineService {
    Result<MainPageInfoDTO> getMainPageInfo();
}
