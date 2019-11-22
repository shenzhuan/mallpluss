package com.mei.zhuang.vo.sys;

import lombok.Data;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author johnbin
 * @create 2017-06-14 22:40
 */
@Data
public class TableResultResponse<T> extends BaseResponse {

    private TableData<T> data;

    public TableResultResponse(long total, List<T> rows) {
        this.data = new TableData<T>(total, rows);
    }

    public TableResultResponse() {
        this.data = new TableData<T>();
    }

    public TableResultResponse<T> total(int total) {
        this.data.setTotal(total);
        return this;
    }

    public TableResultResponse<T> rows(List<T> rows) {
        this.data.setRows(rows);
        return this;
    }


}
