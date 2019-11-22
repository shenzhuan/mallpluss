package com.mei.zhuang.vo.sys;

import com.arvato.common.msg.PagingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("门店字典查询参数")
@Data
public class StoreDictParam extends PagingParam {

    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("菜单id")
    private Integer menuId;

}
