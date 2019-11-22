package com.mei.zhuang.controller.sys;


import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.service.sys.biz.UserBizCacheDemo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(value = "用户缓存控制层", description = "用户缓存", tags = {"用户缓存"})
@ApiIgnore
@RestController
@RequestMapping("userCacheDemo")
public class UserCacheDemoController {

    @Resource
    private UserBizCacheDemo userBizCacheDemo;

    @SysLog(MODULE = "用户缓存控制层", REMARK = "获取所有用户信息")
    @ApiOperation("获取所有用户信息")
    @GetMapping(value = "/users/all")
    public Map<String, Object> getAllUser(CrmSysUser user) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("all", userBizCacheDemo.getAll());
        return result;
    }

    @SysLog(MODULE = "用户缓存控制层", REMARK = "更新用户信息")
    @ApiOperation("更新用户信息")
    @PutMapping(value = "/users/{id}")
    public boolean updateUser(CrmSysUser user) {
        user.setUpdateUserId(123);
        return userBizCacheDemo.update(user);
    }

    @SysLog(MODULE = "用户缓存控制层", REMARK = "删除用户信息")
    @ApiOperation("删除用户信息")
    @DeleteMapping(value = "/users/{id}")
    public Integer deleteUser(@ApiParam("用户id") @PathVariable Integer id) {
        return userBizCacheDemo.delete(id);
    }

    @SysLog(MODULE = "用户缓存控制层", REMARK = "通过id查询用户信息")
    @ApiOperation("通过id查询用户信息")
    @GetMapping(value = "/users/{id}")
    @ResponseBody
    public CrmSysUser getUserById(@ApiParam("用户id") @PathVariable Integer id) {
        return userBizCacheDemo.getById(id);
    }
}
