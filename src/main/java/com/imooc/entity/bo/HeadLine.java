package com.imooc.entity.bo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @description:
 * @author: ZKP
 * @time: 2022/10/16
 */
@Data
public class HeadLine {
    private Long lineId;
    private String lineName;
    private String lineLink;
    private String lineImg;
    private Integer priority;
    private Integer enableStatus;
    private Date createTime;
    private Date lastEditTime;
}
