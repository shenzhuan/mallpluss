package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmApiUserPermission;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2017-12-05
 */
public interface CrmApiUserPermissionMapper extends BaseMapper<CrmApiUserPermission> {

    int hasAuthByApiId(@Param("apiUserId") Integer apiUserId, @Param("apiId") Integer apiId);
}
