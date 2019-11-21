package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysStatusColor;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2018-01-15
 */
public interface CrmSysStatusColorMapper extends BaseMapper<CrmSysStatusColor> {

    List<Map<String, Object>> getStatusColor();
}
