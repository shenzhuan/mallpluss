package com.mei.zhuang.dao.sys;

import com.arvato.admin.vo.AuthMenuVo;
import com.arvato.admin.vo.ButtonNode;
import com.arvato.admin.vo.DataAuthNode;
import com.arvato.admin.vo.ZTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CrmSysMenuMapper extends BaseMapper<CrmSysMenu> {

    List<Integer> getMenuIdsByRoleId(Integer roleId);

    List<ZTreeNode> menuTreeListByMenuIds(List<Integer> menuIds);

    List<ZTreeNode> menuTreeList();

    List<ZTreeNode> menuTreeListByMenuId(Integer menuId);

    List<CrmSysMenu> leftMenu(@Param("roleIds") List<Integer> roleIds);

    List<CrmSysMenu> getAllPermission(@Param("roleIds") List<Integer> roleIds);

    int hasAuthByRoleIds(@Param("roleIds") List<Integer> roleIds, @Param("name") String name);

    int hasAuthByRoleIds2(@Param("roleIds") List<Integer> roleIds, @Param("name") String name);

    List<CrmSysMenu> getPermissionButtons(@Param("roleIds") List<Integer> roleIds, @Param("menuId") Integer menuId);

    List<AuthMenuVo> getMenuInfo(@Param("roleId") Integer roleId, @Param("pid") Integer pid);

    List<ButtonNode> getAllButtons(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    List<DataAuthNode> getAllDataAuth(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    int getCountByOrderNumAndPid(@Param("id") Integer id, @Param("pid") Integer pid, @Param("orderNum") Integer orderNum);

    int getMaxOrderNumByPid(@Param("pid") Integer pid);

    int getLevelByPid(@Param("pid") Integer pid);
}
