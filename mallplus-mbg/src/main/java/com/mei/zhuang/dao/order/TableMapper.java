package com.mei.zhuang.dao.order;


import com.mei.zhuang.entity.Table.TableColumnInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-06-21 15:07
 * @Description:
 */
public interface TableMapper {

    //error
    List<TableColumnInfo> getColumnInfoByTableName(@Param("tableName") String tableName);


}
