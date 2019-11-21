package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2017-10-25
 */
public interface CrmSysRoleMenuMapper extends BaseMapper<CrmSysRoleMenu> {

    Integer selectCountByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<CrmSysRoleMenu> crmSysRoleMenu(@Param("roleIds") List<Integer> roleIds);
}
