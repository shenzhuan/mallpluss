package com.mei.zhuang.dao.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysDict;
import com.mei.zhuang.vo.DictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2017-09-27
 */
public interface CrmSysDictMapper extends BaseMapper<CrmSysDict> {
    List<Map<String, String>> getDictList(@Param("tableName") String tableName, @Param("field") String field);

    String getDictText(@Param("tableName") String tableName, @Param("field") String field, @Param("value") String value);

    int selectDictCount(CrmSysDict crmSysDict);

    List<CrmSysDict> selectDictList(CrmSysDict crmSysDict);

    List<DictData> selectDictObjectList(@Param("tableName") String tableName, @Param("field") String field);
}
