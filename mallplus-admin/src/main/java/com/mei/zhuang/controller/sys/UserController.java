package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.service.sys.biz.UserBiz;
import com.mei.zhuang.vo.BaseResponse;
import com.mei.zhuang.vo.BizResult;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.sys.DataSourceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "系统用户模块-用户", description = "", tags = {"系统用户"})
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserBiz userBiz;


    @SysLog(MODULE = "系统用户模块", REMARK = "初始化数据")
    @ApiOperation("初始化数据")
    @PostMapping(value = "/init")
    public JSONObject init() {
        JSONObject result = new JSONObject();
        result.put("status", userBiz.getUserStatus());
        result.put("gender", userBiz.getUserGender());
        return result;
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "分页获取用户信息")
    @ApiOperation("分页获取用户信息")
    @PostMapping(value = "/getUserByPage")
    public Map<String, Object> getUserByPage(CrmSysUser user,
                                             @RequestParam(value = "current", defaultValue = "1") Integer current,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> result = new HashMap<String, Object>();
        PageHelper.startPage(current, size);
        List<CrmSysUser> crmSysUsers = userBiz.selectUserList(user);
        //user.setTotal((int) PageHelper.freeTotal());
        result.put("total", user.getTotal());
        result.put("rows", crmSysUsers);
        result.put("size", size);
        result.put("current", current);
        return result;
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "获取所有的用户信息")
    @ApiOperation("获取所有的用户信息")
    @PostMapping(value = "/users/all")
    public Map<String, Object> getAllUser() {
        Map<String, Object> result = new HashMap<>();
        result.put("all", userBiz.selectAllUser());
        return result;
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "用户记录信息")
    @ApiOperation("用户记录信息")
    @PostMapping(value = "/users/data/auth")
    public Map<String, Object> getDataAuthUser() {
        Map<String, Object> result = new HashMap<>();
        CrmSysUser crmSysUser = getCurrentUser();
        Integer menuId = userBiz.getUserManagerMenuId();
        List<Integer> deptIds = getDeptIds(menuId, crmSysUser.getId());
        result.put("data", userBiz.selectAllDataAuthUser(deptIds));
        return result;
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "保存用户信息")
    @ApiOperation("保存用户信息")
    @PostMapping(value = "/saveUser")
    public BizResult saveUser(CrmSysUser user) {
        CrmSysUser currentUser = super.getCurrentUser();
        user.setCreateUserId(currentUser.getId());
        DataSourceDto dataSourceDto = getDataSourceDto();
        return userBiz.insertSelective(user, currentUser, dataSourceDto, getCurrentPlatUserId());
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "更新用户信息,用户名是不可以修改的")
    @ApiOperation("更新用户信息,用户名是不可以修改的")
    @PostMapping(value = "/updateUser")
    public BizResult updateUser(CrmSysUser user) {
        CrmSysUser currentUser = super.getCurrentUser();
        user.setUpdateUserId(currentUser.getId());
        return userBiz.updateSelectiveById(user);
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "通过用户id删除用户信息")
    @ApiOperation("通过用户id删除用户信息")
    @PostMapping(value = "/users")
    public BizResult deleteUser(Integer id) {
        return userBiz.deleteById(id);
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "通过用户id获取用户信息")
    @ApiOperation("通过用户id获取用户信息")
    @PostMapping(value = "/getUserById")
    public JSONObject getUserById(Integer id) {
        return userBiz.getUserById(id);
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "通过用户名获取用户信息")
    @ApiOperation("通过用户名获取用户信息")
    @PostMapping(value = "/users/username")
    public JSONObject getUserByUserName(@RequestParam String username) {
        JSONObject json = new JSONObject();
        json.put("code", CommonConstant.CODE_SUCCESS);
        json.put("msg", "根据用户名获取用户信息成功");
        json.put("data", userBiz.getUserByUsername(username));
        return json;
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "获取未锁用户信息")
    @ApiOperation("获取未锁用户信息")
    @PostMapping(value = "/users/unlock")
    public JSONObject unlockUser(@RequestParam Integer userId) {
        CrmSysUser currentUser = super.getCurrentUser();
        int currentUserId = currentUser.getId();
        return userBiz.unlockUser(userId, currentUserId);
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "重置用户密码")
    @ApiOperation("重置用户密码")
    @PostMapping(value = "/users/password")
    public BaseResponse resetPassword(@RequestParam int id, @RequestParam String password) {
        userBiz.resetPassword(id, password);
        return BaseResponse.successResponnse();
    }

    @SysLog(MODULE = "系统用户模块", REMARK = "修改用户密码")
    @ApiOperation("修改用户密码")
    @PostMapping(value = "/users/password/modify")
    public JSONObject modifyPassword(CrmSysUser crmSysUser) {
        CrmSysUser currentUser = super.getCurrentUser();
        crmSysUser.setUpdateUserId(currentUser.getId());
        crmSysUser.setId(currentUser.getId());
        return userBiz.modifyPassword(crmSysUser);
    }

    //用户状态修改
    @PostMapping(value = "/updatestatus")
    public Object userstatus(@RequestParam String status, @RequestParam String username) {
        if (status.equals("0")) {
            if (userBiz.updatestatususer(status, username) > 0) {
                return new CommonResult().success("用户已解锁");
            } else {
                return new CommonResult().failed("修改状态失败");
            }
        }
        if (status.equals("1")) {
            if (userBiz.updatestatususer(status, username) > 0) {
                return new CommonResult().success("用户已被锁定");
            } else {
                return new CommonResult().failed("修改状态失败");
            }
        }
        return new CommonResult().success();
    }
}
