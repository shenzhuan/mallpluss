package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.arvato.admin.biz.MenuBiz;
import com.arvato.admin.vo.ZTreeNode;
import com.arvato.common.vo.returnformat.BaseResponse;
import com.arvato.file_manage.util.BizResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.constant.CommonConstant;
import com.arvato.utils.util.ToolUtil;
import com.mei.zhuang.entity.sys.CrmSysMenu;
import com.mei.zhuang.entity.sys.CrmSysUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "系统菜单", description = "", tags = {"系统菜单管理"})
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuBiz menuBiz;

    @SysLog(MODULE = "系统菜单", REMARK = "初始化数据")
    @ApiOperation("初始化数据")
    @PostMapping(value = "/init")
    public JSONObject init() {
        JSONObject json = new JSONObject();
        json.put("type", menuBiz.getMenuType());
        json.put("menuTree", menuBiz.menuTreeList());
        return json;
    }

    @SysLog(MODULE = "系统菜单", REMARK = "初始化菜单类型")
    @ApiOperation("初始化菜单类型")
    @PostMapping(value = "/init/type")
    public JSONObject initType() {
        JSONObject json = new JSONObject();
        json.put("type", menuBiz.getMenuType());
        return json;
    }

    @SysLog(MODULE = "系统菜单", REMARK = "保存菜单信息")
    @ApiOperation("保存菜单信息")
    @PostMapping(value = "/menus")
    @ApiParam(name = "菜单对象", value = "菜单对象", required = true)
    @ApiResponses({
            @ApiResponse(code = 0, message = "请求成功"),
            @ApiResponse(code = CommonConstant.CODE_BIZ_ERROR, message = "菜单名称重复")
    })
    public BaseResponse saveMenu(CrmSysMenu menu) {
        if (menuBiz.checkRepeatName(menu.getName(), null, menu.getPId())) {
            return new BaseResponse(CommonConstant.CODE_BIZ_ERROR, "菜单名称重复");
        }
        CrmSysUser currentUser = super.getCurrentUser();
        menu.setCreateUserId(currentUser.getId());
        menuBiz.insert(menu);
        return BaseResponse.successResponnse();
    }

    @SysLog(MODULE = "系统菜单", REMARK = "更新菜单信息")
    @ApiOperation("更新菜单信息")
    @PutMapping(value = "/updateMenu")
    public BaseResponse updateMenu(@ApiParam("菜单javaBean") CrmSysMenu menu) {
        if (menuBiz.checkRepeatName(menu.getName(), menu.getId(), menu.getPId())) {
            return new BaseResponse(CommonConstant.CODE_BIZ_ERROR, "菜单名称重复");
        }
        CrmSysUser currentUser = super.getCurrentUser();
        menu.setUpdateUserId(currentUser.getId());
        menuBiz.updateMenuById(menu);
        return BaseResponse.successResponnse();
    }

    @SysLog(MODULE = "系统菜单", REMARK = "删除菜单信息")
    @ApiOperation("删除菜单信息")
    @DeleteMapping(value = "/menus/{id}")
    public BizResult deleteMenu(@ApiParam("菜单id") @PathVariable Integer id) {
        return menuBiz.deleteById(id);
    }

    @SysLog(MODULE = "系统菜单", REMARK = "通过菜单id查询菜单信息")
    @ApiOperation("通过菜单id查询菜单信息")
    @PostMapping(value = "/getMenuById")
    public JSONObject getMenuById(@ApiParam("菜单id") Integer id) {
        return menuBiz.getMenuById(id);
    }

    @SysLog(MODULE = "系统菜单", REMARK = "通过用户角色查询菜单列表")
    @ApiOperation("通过用户角色查询菜单列表")
    @PostMapping(value = "/menus/tree")
    public List<ZTreeNode> menuTreeListByRoleId(@ApiParam("角色id") Integer roleId) {
        List<Integer> menuIds = menuBiz.getMenuIdsByRoleId(roleId);
        if (ToolUtil.isEmpty(menuIds)) {
            return menuBiz.menuTreeList();
        } else {
            return menuBiz.menuTreeListByMenuIds(menuIds);
        }
    }

    /*  @ApiOperation("获取左侧菜单树结构数据")
      @PostMapping(value = "/menus/left")
      public JSONObject leftMenu() {
          CrmSysUser currentUser = super.getCurrentUser();
          return menuBiz.leftMenu(currentUser);
      }*/
    @SysLog(MODULE = "系统菜单", REMARK = "获取左侧菜单树结构数据")
    @ApiOperation("获取左侧菜单树结构数据")
    @PostMapping(value = "/menus/left")
    public Object leftMenu() {
        CrmSysUser currentUser = super.getCurrentUser();
        return menuBiz.leftMenu(currentUser);
    }

    @SysLog(MODULE = "系统菜单", REMARK = "根据菜单id查询该菜单下的按钮列表")
    @ApiOperation("根据菜单id查询该菜单下的按钮列表")
    @PostMapping(value = "/system/permission/buttons")
    public JSONObject permissionButtons(@ApiParam("菜单id") Integer menuId) {
        CrmSysUser currentUser = this.getCurrentUser();
        JSONObject json = new JSONObject();
        json.put("code", CommonConstant.CODE_SUCCESS);
        json.put("msg", "获取按钮成功!");
        json.put("data", menuBiz.getPermissionButtons(currentUser.getId(), menuId));
        return json;
    }

    //动态路由
    @SysLog(MODULE = "系统菜单", REMARK = "根据角色id,获取可以分配权限列表")
    @ApiOperation("根据角色id,获取可以分配权限列表")
    @PostMapping(value = "/menus/auth/distribution")
    public JSONObject authDistributionMenu(@ApiParam("角色id") Integer roleId) {
        return menuBiz.getAuthDistributionMenu(roleId);
    }

    @SysLog(MODULE = "系统菜单", REMARK = "根据上级菜单id,获取上级菜单级别")
    @ApiOperation("根据上级菜单id,获取上级菜单级别")
    @PostMapping(value = "/menus/parent/level")
    public JSONObject parentLevel(@ApiParam("部门id") Integer pid) {
        return menuBiz.getParentLevel(pid);
    }

    @SysLog(MODULE = "系统菜单", REMARK = "获取有数据权限的部门ID列表")
    @ApiOperation(value = "获取有数据权限的部门ID列表", response = List.class)
    @PostMapping("/data/privilege")
    public List<Integer> roleDeptIds(@ApiParam("菜单id") @RequestParam Integer menuId,
                                     @ApiParam("用户id") @RequestParam Integer userId) {
        return getDeptIds(menuId, userId);
    }

}
