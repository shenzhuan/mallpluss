package com.mei.zhuang.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("分页数据模型")
@Data
public class TableData<T> {
    @ApiModelProperty("总条数")
    private long total;
    @ApiModelProperty("单页数据")
    private List<T> rows;

    public TableData(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public TableData() {
    }

    public TableData total(int total){
        this.setTotal(total);
        return this;
    }
    public TableData rows(List<T> rows){
        this.setRows(rows);
        return this;
    }
}
