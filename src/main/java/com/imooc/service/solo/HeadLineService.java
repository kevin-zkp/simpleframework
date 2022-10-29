package com.imooc.service.solo;

import com.imooc.entity.bo.HeadLine;
import com.imooc.entity.dto.Result;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/17
 */
public interface HeadLineService {
    Result<Boolean> addHeadLine(HeadLine headLine);
    Result<Boolean> removeHeadLine(int headLineId);
    Result<Boolean> modifyHeadLine(HeadLine headLine);
    Result<HeadLine> queryHeadLineById(int headLineId);
    Result<List<HeadLine>> queryHeadLine(HeadLine headLineCondition, int pageIndex, int pageSize);
}
