package com.mei.zhuang.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ApiModel("字典")
@Data
public class DictData {
    @ApiModelProperty("字典值")
    private String value;
    @ApiModelProperty("字典文本")
    private String text;
    /**字典分类名称 //比如：性别 */
    private String fieldName;
    /**
     * 排序
     */
    private Integer sort;

    public DictData(){}

    public DictData(String value,String text){
        this.setValue(value);
        this.setText(text);
    }
}
