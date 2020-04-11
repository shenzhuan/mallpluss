package com.zscat.mallplus.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.bo.Tree;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysPermissionNode;
import com.zscat.mallplus.sys.entity.SysRolePermission;
import com.zscat.mallplus.sys.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 后台用户权限表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<Tree<SysPermission>> getPermissionsByUserId(Long id);

    List<SysPermissionNode> treeList();

    List<Tree<SysPermission>> getAllPermission();

    List<SysRolePermission> leftMenu(Long userId);

    Set<String> getMenuPermission(SysUser id);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    Set<String> getRolePermission(SysUser umsAdmin);
}
