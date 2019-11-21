package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper extends BaseMapper<CrmSysUser> {
    public List<CrmSysUser> selectMemberByGroupId(@Param("groupId") int groupId);
    public List<CrmSysUser> selectLeaderByGroupId(@Param("groupId") int groupId);
}
