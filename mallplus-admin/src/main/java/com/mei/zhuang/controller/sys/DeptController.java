package com.mei.zhuang.controller.sys;

import com.arvato.admin.dto.DeptDictData;
import com.arvato.admin.enums.DeptType;
import com.arvato.admin.service.*;
import com.arvato.admin.vo.ZTreeNode;
import com.arvato.common.vo.returnformat.BaseResponse;
import com.arvato.file_manage.util.BizResult;
import com.arvato.file_manage.util.Result;
import com.arvato.utils.annotation.SysLog;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.mei.zhuang.entity.sys.CrmDeptCreditList;
import com.mei.zhuang.entity.sys.CrmMember;
import com.mei.zhuang.entity.sys.CrmSysDept;
import com.mei.zhuang.entity.sys.CrmSysUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "部门管理", description = "", tags = {"部门管理"})
@RestController
@RequestMapping("depts")
public class DeptController extends BaseController {

    @Autowired
    private ICrmSysDeptService deptService;
    @Autowired
    private ICrmStoreService storeService;
    @Autowired
    private ICrmDeptCreditListService deptCreditListService;
    @Autowired
    private ICrmMemberService memberService;
    @Autowired
    private ICrmSysUserService sysUserService;

    @SysLog(MODULE = "部门管理", REMARK = "获取部门关系信息")
    @ApiOperation("获取部门关系信息")
    @GetMapping(value = "/init/{orgType}")
    public BizResult deptTreeOfOrgType(@ApiParam("orgType") @PathVariable int orgType, HttpServletRequest request) {
        return Result.success(new HashMap<String, Object>() {{
            this.put(
                    "list",
                    deptService.getDeptTreeByOrgType(
                            orgType,
                            getDeptIds(getCurrentMenuId(request), getCurrentUser().getId())
                    )
            );
        }});
    }

    @SysLog(MODULE = "部门管理", REMARK = "根据父级节点获取所有叶子节点")
    @ApiOperation("根据父级节点获取所有叶子节点")
    @GetMapping(value = "/getLeafNodes/{Pid}")
    public BizResult deptTreeOfPid(@ApiParam("orgType") @PathVariable int orgType, HttpServletRequest request) {
        return Result.success(new HashMap<String, Object>() {{
            this.put(
                    "list",
                    deptService.getDeptTreeByOrgType(
                            orgType,
                            getDeptIds(getCurrentMenuId(request), getCurrentUser().getId())
                    )
            );
        }});
    }

    @SysLog(MODULE = "部门管理", REMARK = "保存部门信息")
    @ApiOperation("保存部门信息")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping
    public BizResult save(@RequestBody CrmSysDept dept) {
        Assert.isTrue(
                this.deptService.selectCount(new QueryWrapper<>(new CrmSysDept() {{
                    this.setDeptNo(dept.getDeptNo());
                }})) == 0,
                "组织编码已存在"
        );

        Assert.isTrue(deptService.insert(dept), "操作失败");
        //白名单设置
        if (dept.getCrmDeptCreditList() != null && dept.getCrmDeptCreditList().getType().equals("1")) {
            dept.getCrmDeptCreditList().setDeptId(dept.getId());
            Assert.isTrue(this.deptCreditListService.insert(dept.getCrmDeptCreditList()), "操作失败");
        }
        //门店详情设置
        if (dept.getOrgType() == 3) {
            dept.getStore().setId(dept.getId());
            dept.getStore().setDeptId(dept.getPId());
            dept.getStore().setName(dept.getName());
            dept.getStore().setStoreNo(dept.getDeptNo());
            Assert.isTrue(this.storeService.insert(dept.getStore()), "操作失败");
        }
        return Result.success();
    }

    @SysLog(MODULE = "部门管理", REMARK = "更新")
    @ApiOperation("更新")
    @Transactional(rollbackFor = Exception.class)
    @PutMapping
    public BizResult update(@RequestBody CrmSysDept dept) {
        Assert.isTrue(!dept.getId().equals(dept.getPId()), "所属组织不能选择自身");
        Assert.isTrue(deptService.updateById(dept), "操作失败");
        //设置白名单
        if (dept.getCrmDeptCreditList() != null && dept.getCrmDeptCreditList().getType().equals("1")) {
            if (
                    this.deptCreditListService.selectCount(new QueryWrapper<>(new CrmDeptCreditList() {{
                        this.setDeptId(dept.getId());
                    }})) == 0
                    ) {
                dept.getCrmDeptCreditList().setDeptId(dept.getId());
                Assert.isTrue(
                        this.deptCreditListService.insert(dept.getCrmDeptCreditList()),
                        "操作失败"
                );
            }
        }
        //取消白名单
        if (dept.getCrmDeptCreditList() != null && !dept.getCrmDeptCreditList().getType().equals("1")) {
            if (
                    this.deptCreditListService.selectCount(new QueryWrapper<>(new CrmDeptCreditList() {{
                        this.setDeptId(dept.getId());
                    }})) > 0
                    ) {
                Assert.isTrue(
                        this.deptCreditListService.delete(new QueryWrapper<>(new CrmDeptCreditList() {{
                            this.setDeptId(dept.getId());
                        }})),
                        "操作失败"
                );
            }
        }
        //门店详情设置
        if (dept.getOrgType() == 3) {
            dept.getStore().setId(dept.getId());
            dept.getStore().setDeptId(dept.getPId());
            dept.getStore().setName(dept.getName());
            Assert.isTrue(this.storeService.updateById(dept.getStore()), "操作失败");
        }
        return Result.success();
    }

    @SysLog(MODULE = "部门管理", REMARK = "删除部门")
    @ApiOperation("删除部门")
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(value = "/{id}")
    public BizResult deleteDept(@ApiParam("部门id") @PathVariable Integer id) {
        CrmSysDept dept = this.deptService.selectById(id);

        Assert.isTrue(
                sysUserService.selectCount(new QueryWrapper<>(new CrmSysUser() {{
                    this.setDeptId(dept.getId());
                }})) == 0,
                "该组织下已有系统用户，不能删除"
        );

        if (dept.getOrgType() == 3) {
            Assert.isTrue(
                    memberService.selectCount(new QueryWrapper<>(new CrmMember() {{
                        this.setStoreId(dept.getId());
                    }})) == 0,
                    "该门店下已有会员，不能删除"
            );
        }

        //删除组织
        Assert.isTrue(this.deptService.deleteById(dept.getId()), "操作失败");
        //删除白名单的经销商
        if (dept.getOrgType() == 2) {
            if (
                    this.deptCreditListService.selectCount(new QueryWrapper<>(new CrmDeptCreditList() {{
                        this.setDeptId(dept.getId());
                    }})) > 0
                    ) {

                Assert.isTrue(
                        this.deptCreditListService.delete(new QueryWrapper<>(new CrmDeptCreditList() {{
                            this.setDeptId(dept.getId());
                        }})),
                        "操作失败"
                );
            }
        }
        //删除门店详情
        if (dept.getOrgType() == 3) {
            if (
                    this.storeService.selectById(dept.getId()) != null
                    ) {
                Assert.isTrue(
                        this.storeService.deleteById(dept.getId()),
                        "操作失败"
                );
            }
        }

        return Result.success();
    }

    @SysLog(MODULE = "部门管理", REMARK = "通过id获取部门信息")
    @ApiOperation("通过id获取部门信息")
    @GetMapping(value = "/{id}")
    public BizResult getDeptById(@ApiParam("部门id") @PathVariable Integer id) {
        CrmSysDept dept = deptService.selectById(id);
        //白名单
        dept.setCrmDeptCreditList(
                this.deptCreditListService.selectOne(new QueryWrapper<>(new CrmDeptCreditList() {{
                    this.setDeptId(dept.getId());
                }}))
        );
        //门店详情
        if (dept.getOrgType() == 3) {
            dept.setStore(
                    this.storeService.selectById(dept.getId())
            );
        }
        return Result.success(dept);
    }

    /**
     * 根据当前的登陆用户获取归属组织
     *
     * @return
     */
    @SysLog(MODULE = "部门管理", REMARK = "根据当前的登陆用户获取归属组织")
    @ApiOperation("根据当前的登陆用户获取归属组织")
    @GetMapping(value = "/getDept")
    public BizResult getDept() {

        CrmSysUser currentUser = super.getCurrentUser();
        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
        List<String> storeType = new ArrayList<String>();
        List<String> group1 = new ArrayList<String>();
        List<String> group3 = new ArrayList<String>();
        List<String> group4 = new ArrayList<String>();
        List<String> store = new ArrayList<String>();

        CrmSysDept dept = deptService.selectById(currentUser.getDeptId());
        if (DeptType.AREA.getValue().equals(dept.getOrgType())) {
            group1.add(dept.getName());
        } else if (DeptType.REGION.getValue().equals(dept.getOrgType())) {
            group3.add(dept.getName());
        } else if (DeptType.AGENT.getValue().equals(dept.getOrgType())) {
            group4.add(dept.getName());
        } else if (DeptType.STORE.getValue().equals(dept.getOrgType())) {
            store.add(dept.getName());
        }
        //判断门店是否是直属直营
        while (dept.getOrgType().intValue() > 0) {
            dept = deptService.selectById(dept.getPId());
        }
        if ("直属直营".equals(dept.getName())) {
            storeType.add("直属直营");
        }
        if (storeType.size() == 0)
            storeType.add("all");
        if (group1.size() == 0)
            group1.add("all");
        if (group3.size() == 0)
            group3.add("all");
        if (group4.size() == 0)
            group4.add("all");
        if (store.size() == 0)
            store.add("all");
        resultMap.put("storeType", storeType);
        resultMap.put("group1", group1);
        resultMap.put("group3", group3);
        resultMap.put("group4", group4);
        resultMap.put("store", store);
        return Result.success(resultMap);
    }

    @SysLog(MODULE = "部门管理", REMARK = "初始化用户部门数据")
    @ApiOperation("初始化用户部门数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "部门父id", paramType = "Integer", required = true),
            @ApiImplicitParam(name = "menuId", value = "菜单Id", paramType = "Integer", required = true)}
    )
    @GetMapping(value = "/initDeptData/menuId/{menuId}/pid/{pid}")
    public BaseResponse<List<ZTreeNode>> initDeptDatasByCurrentUser(@PathVariable Integer pid, @PathVariable Integer menuId) {
        List<ZTreeNode> nodes = deptService.initDeptDatasByCurrentUser(pid, menuId, getCurrentUser());
        return BaseResponse.successResponnse(nodes);
    }

    @SysLog(MODULE = "部门管理", REMARK = "组织架构数据字典")
    @ApiOperation("组织架构数据字典")
    @GetMapping("dict")
    public BaseResponse<List<DeptDictData>> dict() {
        return BaseResponse.successResponnse(this.deptService.dict());
    }
}
