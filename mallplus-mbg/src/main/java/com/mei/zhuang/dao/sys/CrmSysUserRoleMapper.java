package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysUserRole;

import java.util.List;

public interface CrmSysUserRoleMapper extends BaseMapper<CrmSysUserRole> {

    List<Integer> getRoleIdsbyUserId(Integer userId);

}
