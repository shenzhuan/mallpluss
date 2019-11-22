package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmHarassmentRights;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2018-08-28
 */
public interface CrmHarassmentRightsMapper extends BaseMapper<CrmHarassmentRights> {

    List<Map<String, Object>> getallstore();

}
