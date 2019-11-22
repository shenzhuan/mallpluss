package com.mei.zhuang.service.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.constant.AdminCommonConstant;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.dao.sys.CrmSysDictMapper;
import com.mei.zhuang.dao.sys.CrmSysMenuMapper;
import com.mei.zhuang.dao.sys.CrmSysRoleMapper;
import com.mei.zhuang.dao.sys.CrmSysRoleMenuMapper;
import com.mei.zhuang.entity.sys.CrmSysMenu;
import com.mei.zhuang.entity.sys.CrmSysRoleMenu;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.vo.BizResult;
import com.mei.zhuang.vo.ZTreeNode;
import com.mei.zhuang.vo.sys.AuthMenuVo;
import com.mei.zhuang.vo.sys.ButtonNode;
import com.mei.zhuang.vo.sys.DataAuthNode;
import com.mei.zhuang.vo.sys.RoleAuthMenu;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MenuBiz {

    @Autowired
    private CrmSysMenuMapper crmSysMenuMapper;

    @Autowired
    private CrmSysDictMapper sysDictMapper;

    @Autowired
    private CrmSysRoleMapper roleMapper;

    @Autowired
    private CrmSysRoleMenuMapper roleMenuMapper;

    @Value("${system.ssrsMenuCode}")
    private String ssrsMenuCode;

    /**
     * 新增菜单
     *
     * @param menu 菜单实体对象
     * @return 新增结果
     */
    public void insert(CrmSysMenu menu) {
        dealMenuInfo(menu);
        crmSysMenuMapper.insert(menu);
    }

    /**
     * 根据ID获取菜单信息
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    public JSONObject getMenuById(int id) {
        JSONObject json = new JSONObject();
        List<ZTreeNode> menuTree = crmSysMenuMapper.menuTreeListByMenuId(id);
        json.put("menuTree", menuTree);
        json.put("crmSysMenu", crmSysMenuMapper.selectById(id));
        return json;
    }

    /**
     * 更新菜单信息
     *
     * @param menu 菜单实体对象
     * @return 更新菜单操作结果
     */
    public void updateMenuById(CrmSysMenu menu) {
        dealMenuInfo(menu);
        crmSysMenuMapper.updateById(menu);
    }

    /**
     * 新增、修改时，处理菜单信息
     * 1.处理排序字段
     * orderNum计算逻辑，如果新增菜单的orderNum不为同级别pid下的最大orderNum+1，则新增菜的orderNum设置为最大orderNum+1
     *
     * @param menu 菜单信息
     */
    private void dealMenuInfo(CrmSysMenu menu) {
        // orderNum处理
        Integer orderNum = menu.getOrderNum();
        Integer pid = menu.getPId() == null ? 0 : menu.getPId();
        Integer id = menu.getId();
        Integer orderNumCount = crmSysMenuMapper.getCountByOrderNumAndPid(id, pid, orderNum);
        if (orderNumCount > 0) {
            Integer maxOrderNum = crmSysMenuMapper.getMaxOrderNumByPid(pid);
            menu.setOrderNum(maxOrderNum + 1);
        }

        //菜单级别处理
        if (pid == 0) {
            menu.setLevel(1);
        } else {
            Integer parentLevel = crmSysMenuMapper.getLevelByPid(pid);
            menu.setLevel(parentLevel + 1);
        }

        Assert.isTrue(menu.getLevel() <= 4, "菜单最多只能有4个层级");
    }

    /**
     * 根据ID删除菜单
     * 删除下级菜单，删除菜单角色关联关系
     *
     * @param id 菜单id
     * @return 删除菜单操作结果
     */
    @Transactional
    public BizResult deleteById(int id) {
        BizResult bizResult = new BizResult();
        try {
            //根据ID删除菜单
            crmSysMenuMapper.deleteById(id);

            //删除菜单下级菜单
            QueryWrapper<CrmSysMenu> menuWrapper = new QueryWrapper<>();
            CrmSysMenu crmSysMenu = new CrmSysMenu();
            crmSysMenu.setPId(id);
            menuWrapper.setEntity(crmSysMenu);
            crmSysMenuMapper.delete(menuWrapper);

            //删除菜单角色关联关系
            QueryWrapper<CrmSysRoleMenu> roleMenuWrapper = new QueryWrapper<>();
            CrmSysRoleMenu crmSysRoleMenu = new CrmSysRoleMenu();
            crmSysRoleMenu.setMenuId(id);
            roleMenuWrapper.setEntity(crmSysRoleMenu);
            roleMenuMapper.delete(roleMenuWrapper);

            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("删除菜单成功");
        } catch (Exception e) {
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("删除菜单失败");
        }
        return bizResult;
    }

    /**
     * 获取角色已分配的菜单ID
     *
     * @param roleId 角色ID
     * @return 角色拥有菜单ID集合
     */
    public List<Integer> getMenuIdsByRoleId(Integer roleId) {
        return crmSysMenuMapper.getMenuIdsByRoleId(roleId);
    }

    /**
     * 获取选中菜单树
     *
     * @param menuIds 菜单ID集合
     * @return 菜单树集合
     */
    public List<ZTreeNode> menuTreeListByMenuIds(List<Integer> menuIds) {
        return crmSysMenuMapper.menuTreeListByMenuIds(menuIds);
    }

    /**
     * 获取菜单树
     *
     * @return 菜单树集合
     */
    public List<ZTreeNode> menuTreeList() {
        return crmSysMenuMapper.menuTreeList();
    }

    /**
     * 获取菜单类型
     *
     * @return 菜单类型集合
     */
    public List<Map<String, String>> getMenuType() {
        return sysDictMapper.getDictList(AdminCommonConstant.DICT_TABLE_CRM_SYS_MENU, AdminCommonConstant.DICT_FIELD_MENU_TYPE);
    }

    /**
     * 获取左侧菜单
     *
     * @return 获取结果
     */
    /*public JSONObject leftMenu(CrmSysUser currentUser){
        List<CrmSysMenu> rootMenus = null;
        JSONObject json = new JSONObject();
        try {
            String encodeName = Encryption.getEncryption(currentUser.getUsername());
            System.out.println(currentUser.getId());
            List<Integer> roleIdList = roleMapper.getRoleIdsByUserId(currentUser.getId());
            //获取菜单列表
            List<CrmSysMenu> menuList = crmSysMenuMapper.leftMenu(roleIdList);
            rootMenus = new ArrayList<>();
            Iterator<CrmSysMenu> ite = menuList.iterator();
            while (ite.hasNext()) {
                CrmSysMenu crmSysMenu = ite.next();
                if (ssrsMenuCode.contains(crmSysMenu.getCode())) {
                    crmSysMenu.setHref(crmSysMenu.getHref() + "?encodeName=" + encodeName);
                }
                if (crmSysMenu.getPId() == 0) {
                    rootMenus.add(crmSysMenu);
                    ite.remove();
                }
            }
            WrapperMenuOther(rootMenus, menuList);

            json.put("code", CommonConstant.CODE_SUCCESS);
            json.put("msg", "加载菜单成功");
            json.put("data", rootMenus);
        } catch (Exception e) {
            log.error("加载菜单失败", e);
            json.put("code", CommonConstant.CODE_BIZ_ERROR);
            json.put("msg", "加载菜单失败");
            json.put("data", rootMenus);
        }
        return json;
    }*/
    //静态模块显示
    public JSONObject leftMenu(CrmSysUser currentUser) {
        JSONObject json = new JSONObject();
        try {
            //String encodeName = Encryption.getEncryption(currentUser.getUsername());
            List<Integer> roleIdList = roleMapper.getRoleIdsByUserId(currentUser.getId());
            System.out.println(currentUser.getId());
        /*//获取菜单列表
        List<CrmSysMenu> menuList = crmSysMenuMapper.leftMenu(roleIdList);
        System.out.println(menuList+"222");
        Integer integer = roleMenuMapper.selectCountByRoleIds(roleIdList);
        System.out.println(integer);*/
            List<CrmSysRoleMenu> menuList2 = roleMenuMapper.crmSysRoleMenu(roleIdList);

            json.put("code", CommonConstant.CODE_SUCCESS);
            json.put("msg", "加载菜单成功");
            json.put("data", menuList2);
        } catch (Exception e) {
            log.error("加载菜单失败", e);
            json.put("code", CommonConstant.CODE_BIZ_ERROR);
            json.put("msg", "加载菜单失败");
        }
        return json;
    }

    /**
     * 封装左侧菜单
     *
     * @param parent 上级菜单集合
     * @param sub    下级菜单集合
     */
    private void WrapperMenuOther(List<CrmSysMenu> parent, List<CrmSysMenu> sub) {

        for (CrmSysMenu parentMenu : parent) {
            Iterator<CrmSysMenu> itSub = sub.iterator();
            List<CrmSysMenu> subList = new ArrayList<>();
            while (itSub.hasNext()) {
                CrmSysMenu subMenu = itSub.next();
                if (subMenu.getPId().intValue() == parentMenu.getId().intValue()) {
                    subList.add(subMenu);
                    itSub.remove();
                }
            }
            if (subList.size() > 0) {
                parentMenu.setChildren(subList);
                WrapperMenuOther(subList, sub);
            }
        }
    }

    /**
     * 获取菜单下没有权限的按钮
     *
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return 返回没有权限按钮集合
     */
    public List<CrmSysMenu> getPermissionButtons(Integer userId, Integer menuId) {
        List<Integer> roleIdList = roleMapper.getRoleIdsByUserId(userId);
        return crmSysMenuMapper.getPermissionButtons(roleIdList, menuId);
    }

    /**
     * 获取权限分配菜单
     *
     * @return 该角色拥有权限的菜单集合
     */
    public JSONObject getAuthDistributionMenu(Integer roleId) {
        List<RoleAuthMenu> resultList = new ArrayList<>();
        // 获取根节点----》一级菜单
        List<AuthMenuVo> rootMenus = crmSysMenuMapper.getMenuInfo(roleId, 0);
        if (rootMenus != null && !rootMenus.isEmpty()) {
            for (AuthMenuVo rootMenu : rootMenus) {
                //-----》二级菜单
                Integer menuId = rootMenu.getMenuId();
                List<AuthMenuVo> subMenus = crmSysMenuMapper.getMenuInfo(roleId, menuId);
                if (subMenus != null && !subMenus.isEmpty()) {
                    for (AuthMenuVo subMenu : subMenus) {
                        Integer menuType = subMenu.getMenuType();
                        if (menuType == 1) {
                            //有下级菜单，下级菜单（三级菜单）
                            Integer subMenuId = subMenu.getMenuId();
                            List<AuthMenuVo> funcMenus = crmSysMenuMapper.getMenuInfo(roleId, subMenuId);
                            if (funcMenus != null && !funcMenus.isEmpty()) {
                                //判断下级菜单是否还有下级菜单
                                for (AuthMenuVo last : funcMenus) {
                                    Integer lastMenuType = last.getMenuType();
                                    if (1 == lastMenuType) {
                                        Integer tempSubMenuId = last.getMenuId();
                                        List<AuthMenuVo> tempFuncMenus = crmSysMenuMapper.getMenuInfo(roleId, tempSubMenuId);
                                        wrapperDataAuthMenu(resultList, rootMenu, subMenu, funcMenus, tempFuncMenus, roleId);
                                    } else if (2 == lastMenuType) {
                                        //无下级菜单
                                        List<AuthMenuVo> tempFuncMenus = new ArrayList<>();
                                        tempFuncMenus.add(last);
                                        wrapperDataAuthMenu(resultList, rootMenu, subMenu, tempFuncMenus, null, roleId);
                                    }
                                }
                            }
                        } else if (menuType == 2) {
                            //无下级菜单
                            List<AuthMenuVo> funcMenus = new ArrayList<>();
                            funcMenus.add(subMenu);
                            wrapperDataAuthMenu(resultList, rootMenu, null, funcMenus, null, roleId);
                        }
                    }
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("rows", resultList);
        return json;
    }

    /**
     * 获取上级菜单级别
     *
     * @param pid 上级菜单id
     * @return 上级菜单级别
     */
    public JSONObject getParentLevel(Integer pid) {
        Integer level = crmSysMenuMapper.getLevelByPid(pid);
        JSONObject json = new JSONObject();
        json.put("parentLevel", level);
        return json;
    }

    private void wrapperDataAuthMenu(List<RoleAuthMenu> resultList, AuthMenuVo rootMenu, AuthMenuVo subMenu, List<AuthMenuVo> funcMenus, List<AuthMenuVo> subFuncMenus,
                                     Integer roleId) {
        if (funcMenus == null) {
            RoleAuthMenu roleAuthMenu = new RoleAuthMenu();
            roleAuthMenu.setModuleId(rootMenu.getMenuId());
            roleAuthMenu.setModuleName(rootMenu.getMenuName());
            roleAuthMenu.setModuleChecked(rootMenu.getIsChecked());
            roleAuthMenu.setSubModuleId(subMenu.getMenuId());
            roleAuthMenu.setSubModuleName(subMenu.getMenuName());
            roleAuthMenu.setSubModuleChecked(subMenu.getIsChecked());
            resultList.add(roleAuthMenu);
        } else {
            for (AuthMenuVo funcMenu : funcMenus) {
                Integer funcMenuId = funcMenu.getMenuId();
                RoleAuthMenu roleAuthMenu = new RoleAuthMenu();
                roleAuthMenu.setModuleId(rootMenu.getMenuId());
                roleAuthMenu.setModuleName(rootMenu.getMenuName());
                roleAuthMenu.setModuleChecked(rootMenu.getIsChecked());
                if (subMenu != null) {
                    roleAuthMenu.setSubModuleId(subMenu.getMenuId());
                    roleAuthMenu.setSubModuleName(subMenu.getMenuName());
                    roleAuthMenu.setSubModuleChecked(subMenu.getIsChecked());
                }
                roleAuthMenu.setMenuId(funcMenuId);
                roleAuthMenu.setMenuName(funcMenu.getMenuName());
                roleAuthMenu.setMenuChecked(funcMenu.getIsChecked());
                List<ButtonNode> buttonList = this.getButtons(roleId, funcMenuId);
                roleAuthMenu.setButtonList(buttonList);
                List<DataAuthNode> dataAuthList = this.getDataAuth(roleId, funcMenu.getMenuId());
                roleAuthMenu.setDataAuthList(dataAuthList);
                if (null != subFuncMenus && subFuncMenus.size() > 0) {
                    RoleAuthMenu roleAuthMenuNew = null;
                    for (AuthMenuVo temp : subFuncMenus) {
                        if (funcMenu.getMenuId().intValue() == temp.getMenuPid().intValue()) {
                            roleAuthMenuNew = new RoleAuthMenu();
                            try {
                                BeanUtils.copyProperties(roleAuthMenuNew, roleAuthMenu);
                            } catch (Exception e) {
                            }
                            roleAuthMenuNew.setDataAuthList(this.getDataAuth(roleId, temp.getMenuId()));
                            roleAuthMenuNew.setSubSubModuleId(temp.getMenuId());
                            roleAuthMenuNew.setSubSubModuleName(temp.getMenuName());
                            roleAuthMenuNew.setSubSubModuleChecked(temp.getIsChecked());
                            resultList.add(roleAuthMenuNew);
                        }

                    }
                } else {
                    resultList.add(roleAuthMenu);
                }
            }
        }
    }

    private List<ButtonNode> getButtons(Integer roleId, Integer menuId) {
        return crmSysMenuMapper.getAllButtons(roleId, menuId);
    }

    private List<DataAuthNode> getDataAuth(Integer roleId, Integer menuId) {
        List<DataAuthNode> dataAuthList = crmSysMenuMapper.getAllDataAuth(roleId, menuId);
        boolean flag = false;
        for (DataAuthNode dataAuthNode : dataAuthList) {
            if (dataAuthNode.isCheck()) {
                flag = true;
            }
        }
        if (!flag) {
            dataAuthList.get(0).setCheck(true);
        }
        return dataAuthList;
    }


    public Boolean checkRepeatName(String menuName, Integer id, Integer pid) {
        CrmSysMenu menu = new CrmSysMenu();
        menu.setName(menuName);
        menu.setPId(pid);
        if (id == null) {
            return crmSysMenuMapper.selectCount(new QueryWrapper<>(menu)) > 0 ? true : false;
        }
        return crmSysMenuMapper.selectCount(new QueryWrapper<>(menu).notIn("id", id)) > 0 ? true : false;
    }

}
