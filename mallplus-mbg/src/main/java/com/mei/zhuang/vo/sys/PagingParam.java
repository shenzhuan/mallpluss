package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("分页参数")
@Data
public class PagingParam {
    @ApiModelProperty("从哪条开始，起始为0")
    private Integer offset;
    @ApiModelProperty("拿几条数据")
    private Integer limit;
}
