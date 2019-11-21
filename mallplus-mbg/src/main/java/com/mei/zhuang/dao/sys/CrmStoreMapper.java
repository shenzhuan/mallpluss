package com.mei.zhuang.dao.sys;

import com.arvato.admin.dto.StoreDictParam;
import com.arvato.common.msg.DictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2018-08-10
 */
public interface CrmStoreMapper extends BaseMapper<CrmStore> {

    List<Map<String, Object>> getStoreList();

    List<DictData> getStoreDict(@Param("param") StoreDictParam param, @Param("deptIds") List<Integer> deptIds);
}
