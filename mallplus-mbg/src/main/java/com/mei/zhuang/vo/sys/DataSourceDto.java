package com.mei.zhuang.vo.sys;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DataSourceDto implements Serializable {

    /**
     * 租户id
     */
    private Integer tenantId;

    private String dataSource;

    private String schema;

    public DataSourceDto() {
    }

    public DataSourceDto(String dataSource, String schema) {
        this.dataSource = dataSource;
        this.schema = schema;
    }
}
