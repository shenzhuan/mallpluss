package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.arvato.admin.biz.ApiInterfaceBiz;
import com.arvato.admin.biz.ApiUserBiz;
import com.arvato.admin.constant.AdminCommonConstant;
import com.arvato.admin.orm.dao.CrmApiUserMapper;
import com.arvato.admin.orm.dao.CrmApiUserPermissionMapper;
import com.arvato.admin.orm.dao.CrmSysDictMapper;
import com.arvato.admin.vo.ApiInterfaceVo;
import com.arvato.common.dto.SysDictRequestdDto;
import com.arvato.common.vo.ZTreeNode;
import com.arvato.common.vo.returnformat.Result;
import com.arvato.utils.annotation.SysLog;
import com.mei.zhuang.entity.sys.CrmApiUser;
import com.mei.zhuang.entity.sys.CrmApiUserPermission;
import com.mei.zhuang.entity.sys.CrmSysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:Api接口用户管理
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/3
 */
@Api(value = "接口调用用户管理", description = "api用户管理", tags = {"接口调用用户管理"})
@RestController
@RequestMapping("/apiUser")
@Slf4j
public class ApiUserController extends BaseController {

    @Autowired
    private CrmSysDictMapper sysDictMapper;
    @Autowired
    private CrmApiUserMapper apiUserMapper;
    @Autowired
    private CrmApiUserPermissionMapper apiUserPermissionMapper;
    @Autowired
    private ApiUserBiz apiUserBiz;
    @Autowired
    private ApiInterfaceBiz apiInterfaceBiz;

    @SysLog(MODULE = "接口调用用户管理", REMARK = "获取用户状态、用户类型列表")
    @ApiOperation(value = "获取用户状态、用户类型列表", notes = "data.status:用户状态列表")
    @GetMapping("/init")
    public Result init() {
        JSONObject data = new JSONObject();
        //获取下拉框数据
        Map<String, SysDictRequestdDto> requestDicts = new HashMap<>();
        data.put("status", sysDictMapper.getDictList("global", "valid"));//状态
        return Result.success(data);
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "获取接口用户列表数据")
    @ApiOperation(value = "获取接口用户列表数据", response = CrmApiUser.class)
    @GetMapping("/list")
    public Map<String, Object> list(@ApiParam("用户名") String account,
                                    @ApiParam("企业名称") String firmName,
                                    @ApiParam("用户状态") String status,
                                    @ApiParam("查询数量") @RequestParam Integer limit,
                                    @ApiParam("查询起始位置") @RequestParam Integer offset,
                                    @ApiParam("菜单id") Integer menuId) {
        //获取菜单数据权限级别
        //获取菜单数据权限级别
        CrmSysUser currentUser = getCurrentUser();
        List<Integer> deptIds = getDeptIds(menuId, currentUser.getId());
        return this.apiUserBiz.getApiUserList(account, firmName, status, limit, offset, deptIds);
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "根据id获取api用户信息")
    @ApiOperation(value = "根据id获取api用户信息", response = CrmApiUser.class, notes = "status:用户状态列表，type:用户类型列表")
    @GetMapping("/info/{id}")
    public Result getUserById(@ApiParam("用户id") @PathVariable int id) {
        JSONObject data = new JSONObject();
        data.put("status", sysDictMapper.getDictList("global", "valid"));
        if (id > 0) {
            CrmApiUser apiUser = apiUserMapper.selectById(id);
            data.put("apiUser", apiUser);
        }
        return Result.success(data);
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "添加接口用户信息")
    @ApiOperation("添加接口用户信息")
    @PostMapping("/addUser")
    public Result add(@ApiParam("接口用户信息") CrmApiUser apiUser) {
        if (checkAccountRepeat(apiUser.getAccount())) {
            return Result.failed("用户名已经存在，请重新设置");
        }
        CrmSysUser currentUser = getCurrentUser();
        if (currentUser != null) {
            apiUser.setCreateUserId(currentUser.getId());
        }
        String password = new BCryptPasswordEncoder(AdminCommonConstant.USER_PW_ENCORDER_SALT).encode(apiUser.getPassword());
        apiUser.setPassword(password);
        int ret = apiUserMapper.insert(apiUser);
        return ret > 0 ? Result.success() : Result.failed();
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "更新接口用户信息")
    @ApiOperation("更新接口用户信息")
    @RequestMapping(value = "/updateUser/{id}", method = RequestMethod.POST)
    public Result update(@ApiParam("用户id") @PathVariable Integer id,
                         @ApiParam("接口用户信息") CrmApiUser apiUser) {
        CrmApiUser oldUser = apiUserMapper.selectById(id);
        if (oldUser != null && !StringUtils.equals(oldUser.getAccount(), apiUser.getAccount())) {
            if (checkAccountRepeat(apiUser.getAccount())) {
                return Result.failed("用户名已经存在，请重新设置");
            }
        }
        apiUser.setId(id);
        CrmSysUser currentUser = getCurrentUser();
        if (currentUser != null) {
            apiUser.setUpdateUserId(currentUser.getId());
        }
        int ret = apiUserMapper.updateById(apiUser);
        return ret > 0 ? Result.success() : Result.failed();
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "删除接口用户")
    @ApiOperation("删除接口用户")
    @RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.POST)
    public Result delete(@ApiParam("用户id") @PathVariable Integer id) {
        int ret = apiUserBiz.delete(id);
        return ret > 0 ? Result.success() : Result.failed();
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "更新用户密码")
    @ApiOperation("更新用户密码")
    @RequestMapping(value = "/updatepwd/{id}", method = RequestMethod.POST)
    public Result updatePassword(@ApiParam("用户id") @PathVariable Integer id,
                                 @ApiParam("接口用户信息") @RequestParam String newPwd) {
        CrmApiUser apiUser = new CrmApiUser();
        apiUser.setId(id);
        CrmSysUser currentUser = getCurrentUser();
        if (currentUser != null) {
            apiUser.setUpdateUserId(currentUser.getId());
        }
        String password = new BCryptPasswordEncoder(AdminCommonConstant.USER_PW_ENCORDER_SALT).encode(newPwd);
        apiUser.setPassword(password);
        int ret = apiUserMapper.updateById(apiUser);
        return ret > 0 ? Result.success() : Result.failed();
    }

    /**
     * 获取项目名称、接口类型
     *
     * @return
     */
    @SysLog(MODULE = "接口调用用户管理", REMARK = "外部接口类型列表树")
    @ApiOperation(value = "外部接口类型列表树")
    @GetMapping("/interface/type")
    public Result interfaceType() {
        List<ZTreeNode> apiTyps = apiInterfaceBiz.getAllTypes();
        return Result.success(apiTyps);
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "获取外部接口列表")
    @ApiOperation(value = "获取外部接口列表", response = ApiInterfaceVo.class)
    @GetMapping("/interface/list")
    public Map<String, Object> list(@ApiParam("接口名称") String name,
                                    @ApiParam("接口类型id") Integer typeId,
                                    @ApiParam("用户id") @RequestParam Integer apiUserId,
                                    @ApiParam("查询数量") @RequestParam Integer limit,
                                    @ApiParam("查询起始位置") @RequestParam Integer offset,
                                    @ApiParam("菜单id") Integer menuId) {
        //获取菜单数据权限级别
        CrmSysUser currentUser = getCurrentUser();
        List<Integer> deptIds = null;
        if (currentUser != null) {
            deptIds = getDeptIds(menuId, currentUser.getId());
        }
        return this.apiInterfaceBiz.getInterfaceList(name, typeId, apiUserId, limit, offset, deptIds);
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "添加接口用户的接口权限")
    @ApiOperation(value = "添加接口用户的接口权限", response = CrmApiUserPermission.class)
    @PostMapping("/interface/addPermission")
    public Result addPermission(@ApiParam("权限信息") CrmApiUserPermission permission) {
        CrmSysUser currentUser = getCurrentUser();
        permission.setCreateUserId(currentUser.getId());
        int ret = apiUserPermissionMapper.insert(permission);
        return ret > 0 ? Result.success() : Result.failed();
    }

    @SysLog(MODULE = "接口调用用户管理", REMARK = "删除接口用户的接口权限")
    @ApiOperation("删除接口用户的接口权限")
    @RequestMapping(value = "/interface/delPermission/{apiUserId}/{interfaceId}", method = RequestMethod.POST)
    public Result deletePermission(@ApiParam("权限id") @PathVariable Integer interfaceId,
                                   @ApiParam("用户id") @PathVariable Integer apiUserId) {
        Map param = new HashMap();
        param.put("interface_id", interfaceId);
        param.put("api_user_id", apiUserId);
        int ret = apiUserPermissionMapper.deleteByMap(param);
        return ret > 0 ? Result.success() : Result.failed();
    }

    /**
     * 检测用户名是否重复
     *
     * @param account
     * @return
     */
    private boolean checkAccountRepeat(String account) {
        try {
            CrmApiUser apiUser = apiUserMapper.selectOne(new CrmApiUser().setAccount(account));
            if (apiUser != null) {
                log.info("账号重复，不能添加");
                return true;
            }
        } catch (Exception e) {
            log.info("数据库数据重复，错误！");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String password = new BCryptPasswordEncoder(AdminCommonConstant.USER_PW_ENCORDER_SALT).encode("111111");
        System.out.println(password);
    }
}
