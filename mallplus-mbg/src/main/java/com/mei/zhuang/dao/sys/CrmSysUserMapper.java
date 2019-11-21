package com.mei.zhuang.dao.sys;

import com.arvato.admin.vo.ZTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CrmSysUserMapper extends BaseMapper<CrmSysUser> {

    int selectUserCount(CrmSysUser user);

    List<CrmSysUser> selectUserList(CrmSysUser user);

    List<CrmSysUser> selectAllUser();

    List<ZTreeNode> deptTreeListByDeptId(Integer dpetId);

    List<ZTreeNode> manageDeptTree(@Param("manageDeptList") List<Integer> manageDeptList);

    List<CrmSysUser> selectAllDataAuthUser(@Param("deptIds") List<Integer> deptIds);

    //状态修改
    Integer updatestatususer(@Param("status") String status, @Param("username") String username);

    /**
     * 根据用户名查找
     * @param username
     * @return
     */
    CrmSysUser selectByUsername(@Param("username") String username);

    Integer selectid(@Param("username") String username);
}
