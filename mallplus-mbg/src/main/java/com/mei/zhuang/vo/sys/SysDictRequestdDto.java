package com.mei.zhuang.vo.sys;

import lombok.Data;

/**
 * @Description:数据字典请求服务传输对象
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2018/12/20
 */
@Data
public class SysDictRequestdDto {

    /**大类*/
    private String tableName;
    /**小类*/
    private String field;
    /**值*/
    private String value;

    public SysDictRequestdDto(String tableName,String field){
        this.tableName  = tableName;
        this.field = field;
    }

    public SysDictRequestdDto(String tableName,String field,String value){
        this.tableName  = tableName;
        this.field = field;
        this.value = value;
    }

}
