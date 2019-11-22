package com.mei.zhuang.service.sys.biz;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.constant.AdminCommonConstant;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.dao.sys.*;
import com.mei.zhuang.entity.sys.CrmSysDataAuth;
import com.mei.zhuang.entity.sys.CrmSysRole;
import com.mei.zhuang.entity.sys.CrmSysRoleMenu;
import com.mei.zhuang.entity.sys.CrmSysUserRole;
import com.mei.zhuang.vo.BizResult;
import com.mei.zhuang.vo.ZTreeNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleBiz {

    @Resource
    private CrmSysRoleMapper crmSysRoleMapper;

    @Resource
    private CrmSysDictMapper sysDictMapper;

    @Resource
    private CrmSysUserRoleMapper crmSysUserRoleMapper;

    @Resource
    private CrmSysRoleMenuMapper crmSysRoleMenuMapper;

    @Resource
    private CrmSysDataAuthMapper dataAuthMapper;

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    public BizResult insert(CrmSysRole role) {
        BizResult bizResult = new BizResult();

      /*  Assert.isTrue(
                this.crmSysRoleMapper.selectCount(new EntityWrapper<>(new CrmSysRole(){{
                    this.setName(role.getName());
                }}))==0,
                "角色名称不能重复"
        );*/

        try {
            Integer roleupdate = crmSysRoleMapper.Roleupdate2(role.getName());
            if (roleupdate > 0) {
                bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
                bizResult.setMsg("角色名称不能重复");
            } else {
                crmSysRoleMapper.insert(role);
                bizResult.setCode(CommonConstant.CODE_SUCCESS);
                bizResult.setMsg("添加角色成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("添加角色失败");
        }
        return bizResult;
    }


    /**
     * 根据ID获取角色信息
     *
     * @param id
     * @return
     */
    public CrmSysRole getRoleById(int id) {
        return crmSysRoleMapper.selectById(id);
    }

    /**
     * 根据ID更新角色信息
     *
     * @param role
     * @return
     */
    public BizResult updateRoleById(CrmSysRole role) {
        BizResult bizResult = new BizResult();

      /*  Assert.isTrue(
                this.crmSysRoleMapper.selectCount(
                        new EntityWrapper<CrmSysRole>(new CrmSysRole(){{
                            this.setName(role.getName());
                        }}).notIn("id",role.getId())
                )==0,
                "角色名称不能重复"
        );*/

        try {
            Integer roleupdate = crmSysRoleMapper.Roleupdate(role.getId(), role.getName());
            if (roleupdate == 0) {
                Integer roleupdate2 = crmSysRoleMapper.Roleupdate2(role.getName());
                if (roleupdate2 > 0) {
                    bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
                    bizResult.setMsg("角色名称不能重复");
                } else {
                    crmSysRoleMapper.updateById(role);
                    bizResult.setCode(CommonConstant.CODE_SUCCESS);
                    bizResult.setMsg("更新角色信息成功");
                }
            } else {
                crmSysRoleMapper.updateById(role);
                bizResult.setCode(CommonConstant.CODE_SUCCESS);
                bizResult.setMsg("更新角色信息成功");
            }


        } catch (Exception e) {
            e.printStackTrace();
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("更新角色信息失败");
        }
        return bizResult;
    }

    /**
     * 根据ID删除角色
     * 删除与角色相关联的信息，如：角色关联用户
     *
     * @param id
     * @return
     */
    @Transactional
    public BizResult deleteById(int id) {
        BizResult bizResult = new BizResult();
        try {
            //删除角色
            crmSysRoleMapper.deleteById(id);

            //删除用户角色关系
            QueryWrapper<CrmSysUserRole> userRoleWrapper = new QueryWrapper<>();
            CrmSysUserRole userRole = new CrmSysUserRole();
            userRole.setRoleId(id);
            userRoleWrapper.setEntity(userRole);
            crmSysUserRoleMapper.delete(userRoleWrapper);

            //删除角色菜单关联关系
            QueryWrapper<CrmSysRoleMenu> roleMenuWrapper = new QueryWrapper<>();
            CrmSysRoleMenu roleMenu = new CrmSysRoleMenu();
            roleMenu.setRoleId(id);
            roleMenuWrapper.setEntity(roleMenu);
            crmSysRoleMenuMapper.delete(roleMenuWrapper);

            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("删除角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("删除角色失败");
        }
        return bizResult;
    }


    /**
     * 获取角色状态
     *
     * @return
     */
    public List<Map<String, String>> getRoleStatus() {
        return sysDictMapper.getDictList(AdminCommonConstant.DICT_TABLE_GLOBAL, AdminCommonConstant.DICT_FIELD_STATUS);
    }

    /**
     * 根据条件获取角色数量
     *
     * @return
     */
    public int selectRoleCount(CrmSysRole role) {
        return crmSysRoleMapper.selectRoleCount(role);
    }

    /**
     * 根据条件获取角色列表
     *
     * @return
     */
    public List<CrmSysRole> selectRoleList(CrmSysRole role) {
        List<Map<String, String>> dict = sysDictMapper.getDictList(AdminCommonConstant.DICT_TABLE_GLOBAL, AdminCommonConstant.DICT_FIELD_STATUS);
        List<CrmSysRole> roleList = crmSysRoleMapper.selectRoleList(role);
        for (CrmSysRole crmSysRole : roleList) {
            for (Map<String, String> map : dict) {
                if (map.get("value").equals(crmSysRole.getStatus())) {
                    crmSysRole.setStatus(map.get("text"));
                    break;
                }
            }
        }
        return roleList;
    }

    /**
     * 角色分配
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Transactional
    public BizResult roleAssign(Integer userId, String roleIds) {
        BizResult bizResult = new BizResult();
        try {
            QueryWrapper<CrmSysUserRole> wrapper = new QueryWrapper<CrmSysUserRole>();
            wrapper.eq("user_id", userId);
            crmSysUserRoleMapper.delete(wrapper);
            if (StringUtils.isNotEmpty(roleIds)) {
                String[] roleIdsArr = roleIds.split(",");

                for (String roleId : roleIdsArr) {
                    if (StringUtils.isNotEmpty(roleId)) {
                        CrmSysUserRole crmSysUserRole = new CrmSysUserRole();
                        crmSysUserRole.setUserId(userId);
                        crmSysUserRole.setRoleId(Integer.valueOf(roleId));
                        crmSysUserRoleMapper.insert(crmSysUserRole);
                    }
                }
            }
            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("角色分配成功");
        } catch (Exception e) {
            e.printStackTrace();
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("角色分配失败");
        }
        return bizResult;
    }

    /**
     * 角色权限分配
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID字符串
     * @return 操作结果
     */
    @Transactional
    public BizResult roleAuthAssign(Integer roleId, String menuIds, List<CrmSysDataAuth> dataAuthList) {
        BizResult bizResult = new BizResult();
        try {

            CrmSysRoleMenu roleMenuEntity = new CrmSysRoleMenu();
            roleMenuEntity.setRoleId(roleId);
            QueryWrapper<CrmSysRoleMenu> roleMenuWrapper = new QueryWrapper<>();
            roleMenuWrapper.setEntity(roleMenuEntity);
            crmSysRoleMenuMapper.delete(roleMenuWrapper);

            if (StringUtils.isNotEmpty(menuIds)) {
                String[] menuIdsArr = menuIds.split(",");
                Set<String> set = new HashSet<>(Arrays.asList(menuIdsArr));
                String[] arrayResult = set.toArray(new String[set.size()]);

                for (String menuId : arrayResult) {
                    if (StringUtils.isNotEmpty(menuId)) {
                        CrmSysRoleMenu crmSysRoleMenu = new CrmSysRoleMenu();
                        crmSysRoleMenu.setRoleId(roleId);
                        crmSysRoleMenu.setMenuId(Integer.valueOf(menuId));
                        crmSysRoleMenuMapper.insert(crmSysRoleMenu);
                    }
                }
            }
            CrmSysDataAuth crmSysDatAuth = new CrmSysDataAuth();
            crmSysDatAuth.setRoleId(roleId);
            QueryWrapper<CrmSysDataAuth> dataAuthWrapper = new QueryWrapper<>();
            dataAuthWrapper.setEntity(crmSysDatAuth);
            roleMenuWrapper.eq("role_id", roleId);
            dataAuthMapper.delete(dataAuthWrapper);

            if (dataAuthList != null && dataAuthList.size() > 0) {
                for (CrmSysDataAuth crmSysDataAuth : dataAuthList) {
                    dataAuthMapper.insert(crmSysDataAuth);
                }
            }
            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("角色权限分配成功");
        } catch (Exception e) {
            e.printStackTrace();
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("角色权限分配失败");
        }
        return bizResult;
    }

    /**
     * 根据角色ID获取该角色已分配的菜单
     *
     * @param roleId
     * @return
     */
    public List<CrmSysRoleMenu> selectMenus(Integer roleId) {
        QueryWrapper<CrmSysRoleMenu> wrapper = new QueryWrapper<CrmSysRoleMenu>();
        wrapper.eq("role_id", roleId);
        return crmSysRoleMenuMapper.selectList(wrapper);
    }

    /**
     * 根据用户ID获取关联的角色ID
     *
     * @param userId
     * @return
     */
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        return crmSysRoleMapper.getRoleIdsByUserId(userId);
    }

    /**
     * 获取角色树
     *
     * @return
     */
    public List<ZTreeNode> getRoleTreeList() {
        return crmSysRoleMapper.selectRoleTree();
    }

    /**
     * 根据角色ID获取角色树
     *
     * @param roleIds
     * @return
     */
    public List<ZTreeNode> getRoleTreeListByRoleIds(List<Integer> roleIds) {
        return crmSysRoleMapper.getRoleTreeListByRoleIds(roleIds);
    }

    /**
     * 检验角色名是否唯一
     *
     * @param name 角色名
     * @return 返回数量
     */
    public Integer checkRoleName(Integer roleId, String name) {
        return crmSysRoleMapper.selectByRoleIdAndName(roleId, name);
    }


    public Boolean isHasRoleByUserName(String username) {
        List<Integer> roles = crmSysRoleMapper.getRoleIdsByUserName(username);
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }
        return true;
    }

}
