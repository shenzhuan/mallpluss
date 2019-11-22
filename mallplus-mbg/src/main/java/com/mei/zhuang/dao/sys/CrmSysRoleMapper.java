package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysRole;
import com.mei.zhuang.vo.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2017-10-19
 */
public interface CrmSysRoleMapper extends BaseMapper<CrmSysRole> {

    int selectRoleCount(CrmSysRole role);

    List<CrmSysRole> selectRoleList(CrmSysRole role);

    List<ZTreeNode> selectRoleTree();

    List<Integer> getRoleIdsByUserId(Integer userId);

    //静态权限
    Integer getRoleIdsBy(Integer userId);

    List<ZTreeNode> getRoleTreeListByRoleIds(List<Integer> roleIds);

    Integer selectByRoleIdAndName(@Param("roleId") Integer roleId, @Param("name") String name);

    List<Integer> getRoleIdsByUserName(String userName);

    Integer Roleupdate(Integer id, String name);

    Integer Roleupdate2(String name);


}
