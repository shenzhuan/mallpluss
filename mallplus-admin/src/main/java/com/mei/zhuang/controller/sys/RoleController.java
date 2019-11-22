package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.sys.CrmSysDataAuth;
import com.mei.zhuang.entity.sys.CrmSysRole;
import com.mei.zhuang.entity.sys.CrmSysRoleMenu;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.service.sys.biz.RoleBiz;
import com.mei.zhuang.util.ToolUtil;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.vo.BizResult;
import com.mei.zhuang.vo.ZTreeNode;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "系统用户模块-用户角色", description = "", tags = {"用户角色"})
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleBiz roleBiz;

    @SysLog(MODULE = "用户角色", REMARK = "初始化数据信息")
    @ApiOperation("初始化数据信息")
    @PostMapping(value = "/init")
    public JSONObject init() {
        JSONObject result = new JSONObject();
        result.put("status", roleBiz.getRoleStatus());
        return result;
    }

    @SysLog(MODULE = "用户角色", REMARK = "分页获取角色信息")
    @ApiOperation(value = "分页获取角色信息")
    @PostMapping(value = "/rolesByPage")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色名称", name = "name", paramType = "String"),
            @ApiImplicitParam(value = "每页显示数", name = "size", paramType = "Integer", defaultValue = "10", required = true),
            @ApiImplicitParam(value = "开始行数", name = "current", paramType = "Integer", defaultValue = "1", required = true)
    })
    public Map<String, Object> getRoleByPage(String name, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> result = new HashMap<String, Object>();
        CrmSysRole role = new CrmSysRole();
        role.setName(name);
        PageHelper.startPage(current, size);
        List<CrmSysRole> crmSysRoles = roleBiz.selectRoleList(role);
        //   role.setTotal((int) PageHelper.freeTotal());
        result.put("current", current);
        result.put("size", size);
        result.put("total", role.getTotal());
        result.put("rows", crmSysRoles);
        return result;
    }

    @SysLog(MODULE = "用户角色", REMARK = "保存角色信息")
    @ApiOperation("保存角色信息")
    @PostMapping(value = "/roles")
    public BizResult saveUser(@ApiParam(value = "角色对象", name = "role", required = true) CrmSysRole role) {
        CrmSysUser currentUser = super.getCurrentUser();
        role.setCreateUserId(currentUser.getId());
        role.setCreateDate(DateUtil.format(role.getCreateDate(), DateUtil.YYYYMMDD, DateUtil.YYYY_MM_DD));
        return roleBiz.insert(role);
    }

    @SysLog(MODULE = "用户角色", REMARK = "更新角色信息")
    @ApiOperation("更新角色信息")
    @PostMapping(value = "/rolesupdate")
    public BizResult updateUser(@ApiParam(value = "角色对象", name = "role", required = true) CrmSysRole role) {
        System.out.println(role);
        CrmSysUser currentUser = super.getCurrentUser();
        role.setUpdateUserId(currentUser.getId());
        return roleBiz.updateRoleById(role);
    }

    @SysLog(MODULE = "用户角色", REMARK = "删除角色信息")
    @ApiOperation("删除角色信息")
    @PostMapping(value = "/delRoles")
    @ApiImplicitParam(value = "角色id", required = true, name = "id")
    public BizResult deleteUser(Integer id) {
        return roleBiz.deleteById(id);
    }

    @SysLog(MODULE = "用户角色", REMARK = "通过角色id查询角色信息")
    @ApiOperation("通过角色id查询角色信息")
    @PostMapping(value = "/queryRoleById")
    @ApiImplicitParam(value = "角色id", required = true, name = "id")
    public CrmSysRole getRoleById(Integer id) {
        return roleBiz.getRoleById(id);
    }

    @SysLog(MODULE = "用户角色", REMARK = "分配角色信息")
    @ApiOperation("分配角色信息")
    @PostMapping(value = "/roles/assign")
    public BizResult roleAssign(@ApiParam("用户id") @RequestParam("userId") Integer userId,
                                @ApiParam("角色ids") @RequestParam("roleIds") String roleIds) {
        return roleBiz.roleAssign(userId, roleIds);
    }

    @SysLog(MODULE = "用户角色", REMARK = "分配角色")
    @ApiOperation("分配角色")
    @PostMapping(value = "/roles/auth/assign")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色id", name = "roleId", required = true),
            @ApiImplicitParam(value = "菜单id,以逗号分隔的字符串", name = "menuIds", required = true),
            @ApiImplicitParam(value = "dataAuthStr", name = "dataAuthStr", required = true)
    })
    public BizResult roleAuthAssign(@RequestParam("roleId") Integer roleId,
                                    @RequestParam("menuIds") String menuIds, @RequestParam String dataAuthStr) {
        BizResult bizResult = new BizResult();
        if (dataAuthStr.equals("") || menuIds.equals("")) {
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("角色权限分配失败");
            return bizResult;
        }
        JSONArray jsonArray = JSONArray.parseArray(dataAuthStr);
        List<CrmSysDataAuth> dataAuthList = jsonArray.toJavaList(CrmSysDataAuth.class);
        return roleBiz.roleAuthAssign(roleId, menuIds, dataAuthList);
    }

    @SysLog(MODULE = "用户角色", REMARK = "通过角色id获取菜单信息")
    @ApiOperation("通过角色id获取菜单信息")
    @PostMapping(value = "/menus")
    @ApiImplicitParam(value = "角色id", required = true, name = "roleId")
    public List<CrmSysRoleMenu> getMenus(Integer roleId) {
        return roleBiz.selectMenus(roleId);
    }

    @SysLog(MODULE = "用户角色", REMARK = "通过角色id获取权限树信息")
    @ApiOperation("通过角色id获取权限树信息")
    @PostMapping(value = "/roles/tree")
    @ApiImplicitParam(value = "用户id", required = true, name = "userId")
    public List<ZTreeNode> roleTreeListByRoleId(Integer userId) {
        List<Integer> roleIds = roleBiz.getRoleIdsByUserId(userId);
        if (ToolUtil.isEmpty(roleIds)) {
            return roleBiz.getRoleTreeList();
        } else {
            return roleBiz.getRoleTreeListByRoleIds(roleIds);
        }
    }

    @SysLog(MODULE = "用户角色", REMARK = "检查角色名称")
    @ApiOperation("检查角色名称")
    @PostMapping(value = "/roles/checkByRoleName")
    @ApiImplicitParam(value = "角色名称", required = true, name = "name")
    public JSONObject checkRoleName(String name) {
        JSONObject json = new JSONObject();
        json.put("code", CommonConstant.CODE_SUCCESS);
        json.put("msg", "根据角色名获取角色信息成功");
        json.put("data", roleBiz.checkRoleName(null, name));
        return json;
    }

    @SysLog(MODULE = "用户角色", REMARK = "检查角色名称")
    @ApiOperation("检查角色名称")
    @PostMapping(value = "/roles/check")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "角色id", name = "roleId", required = true),
            @ApiImplicitParam(value = "角色名称", name = "name", required = true)
    })
    public JSONObject checkRoleName(Integer roleId, String name) {
        JSONObject json = new JSONObject();
        json.put("code", CommonConstant.CODE_SUCCESS);
        json.put("msg", "根据角色名获取角色信息成功");
        json.put("data", roleBiz.checkRoleName(roleId, name));
        return json;
    }

    @SysLog(MODULE = "用户角色", REMARK = "根据username校验用户是否分配角色")
    @ApiOperation("根据username校验用户是否分配角色")
    @PostMapping("/hasRole")
    @ApiImplicitParam(value = "用户名称", required = true, name = "username")
    public JSONObject hasRole(String username) {
        Boolean bool = roleBiz.isHasRoleByUserName(username);
        JSONObject json = new JSONObject();
        if (!bool) {
            json.put("code", CommonConstant.CODE_BIZ_ERROR);
            json.put("msg", "用户没有分配角色,请联系管理员");
            return json;
        }
        json.put("code", CommonConstant.CODE_SUCCESS);
        json.put("msg", CommonConstant.MSG_SUCCESS);
        return json;
    }
}
