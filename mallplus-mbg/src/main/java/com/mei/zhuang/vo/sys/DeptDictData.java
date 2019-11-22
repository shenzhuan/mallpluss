package com.mei.zhuang.vo.sys;


import com.mei.zhuang.vo.DictData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptDictData extends DictData {

    @ApiModelProperty("父级id")
    private Integer pId;

}
