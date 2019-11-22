package com.mei.zhuang.service.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.constant.AdminCommonConstant;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.constant.UserConstant;
import com.mei.zhuang.dao.sys.*;
import com.mei.zhuang.entity.sys.*;
import com.mei.zhuang.exception.UserNameRepeatException;
import com.mei.zhuang.service.sys.ISysPlatformUserService;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.vo.BizResult;
import com.mei.zhuang.vo.ZTreeNode;
import com.mei.zhuang.vo.sys.DataSourceDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserBiz {
    @Autowired
    SysPlatformUserMapper mapper;
    private CrmSysUserMapper crmSysUserMapper;
    private CrmSysUserRoleMapper crmSysUserRoleMapper;
    private CrmSysDictMapper sysDictMapper;
    private CrmSysRoleMenuMapper crmSysRoleMenuMapper;
    private CrmSysDeptMapper crmSysDeptMapper;
    private CrmSysLoginLogMapper crmSysLoginLogMapper;
    private CrmSysMenuMapper crmSysMenuMapper;
    @Autowired
    private SysPlatformUserMapper sysPlatformUserMapper;
    @Autowired
    private SysPlatUserBiz sysPlatUserBiz;
    @Autowired
    private ISysPlatformUserService sysPlatformUserService;
    @Value("${system.userManagerMenuCode}")
    private String userManagerMenuCode;
    @Value("${masterdbsource.dbname}")
    private String masterDbName;
    @Value("${masterdbsource.schema}")
    private String masterSchema;

    @Autowired
    public UserBiz(CrmSysUserMapper crmSysUserMapper, CrmSysUserRoleMapper crmSysUserRoleMapper,
                   CrmSysDictMapper sysDictMapper, CrmSysRoleMenuMapper crmSysRoleMenuMapper,
                   CrmSysDeptMapper crmSysDeptMapper,
                   CrmSysLoginLogMapper crmSysLoginLogMapper, CrmSysMenuMapper crmSysMenuMapper) {
        this.crmSysUserMapper = crmSysUserMapper;
        this.crmSysUserRoleMapper = crmSysUserRoleMapper;
        this.sysDictMapper = sysDictMapper;
        this.crmSysRoleMenuMapper = crmSysRoleMenuMapper;
        this.crmSysDeptMapper = crmSysDeptMapper;
        this.crmSysLoginLogMapper = crmSysLoginLogMapper;
        this.crmSysMenuMapper = crmSysMenuMapper;
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode("123321"));
        //  System.out.println(new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode("123321"));
    }

    /**
     * 新增用户
     *
     * @param entity      新增用户实体
     * @param currentUser 当前登录用户对象
     * @return 操作成功失败信息
     */
    @Transactional
    public BizResult insertSelective(CrmSysUser entity, CrmSysUser currentUser, DataSourceDto dataSourceDto, Integer managerId) {
        BizResult bizResult = new BizResult();
        try {
            if (crmSysUserMapper.selectByUsername(entity.getUsername()) != null) {
                throw new UserNameRepeatException("用户名重复,不能添加");
            }
          /*  if(!sysPlatUserBiz.checkUserNameValid(entity.getUsername(),new DataSourceDto(masterDbName,masterSchema))){
                throw new UserNameRepeatException("用户名在本品牌和其他品牌都不能重复,请重新输入用户名");
            }*/
            //号码效验
            String regex = "^((13[0-9])|(14[5,6,7,8,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern pattern1 = Pattern.compile(regex);
            Boolean matcher1 = pattern1.matcher(entity.getMobile()).matches();
            //密码验证
            String pass = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{15,30})$";
            Pattern pattern = Pattern.compile(pass);
            Boolean matcher = pattern.matcher(entity.getPassword()).matches();
            if (!matcher1) {
                throw new UserNameRepeatException("手机号码输入格式错误");
            }
            if (!matcher) {
                throw new UserNameRepeatException("密码格式不正确");
            }
            String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
            entity.setPassword(password);
            crmSysUserMapper.insert(entity);
            sysPlatUserBiz.addPlatFormUser(entity.getUsername(), password, managerId, new Integer[]{dataSourceDto.getTenantId()}, new DataSourceDto(masterDbName, masterSchema));
            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("添加用户成功");
        } catch (UserNameRepeatException e) {
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加用户失败", e);
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("添加用户失败");
        }
        return bizResult;
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public JSONObject getUserById(int id) {
        JSONObject json = new JSONObject();
        CrmSysUser crmSysUser = crmSysUserMapper.selectById(id);
        json.put("userInfo", crmSysUser);
        Integer deptId = crmSysUser.getDeptId();
        String manageDeptId = crmSysUser.getManageDeptId();
        if (deptId > -1) {
            List<ZTreeNode> deptTree = crmSysUserMapper.deptTreeListByDeptId(deptId);
            json.put("deptTree", deptTree);
        }
        if (StringUtils.isNotEmpty(manageDeptId)) {
            List<Integer> manageDeptList = new ArrayList<>();
            String[] manageDeptIdArr = manageDeptId.split(",");
            for (String aManageDeptIdArr : manageDeptIdArr) {
                manageDeptList.add(Integer.valueOf(aManageDeptIdArr));
            }
            List<ZTreeNode> manageDeptTree = crmSysUserMapper.manageDeptTree(manageDeptList);
            json.put("manageDeptTree", manageDeptTree);
        } else {
            List<ZTreeNode> manageDeptTree = crmSysDeptMapper.getDeptTree();
            json.put("manageDeptTree", manageDeptTree);
        }
        return json;
    }

    /**
     * 根据ID更新用户信息
     *
     * @param entity 用户实体
     */
    public BizResult updateSelectiveById(CrmSysUser entity) {
        BizResult bizResult = new BizResult();
        try {
            //号码效验
            String regex = "^((13[0-9])|(14[5,6,7,8,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern pattern1 = Pattern.compile(regex);
            Boolean matcher1 = pattern1.matcher(entity.getMobile()).matches();
            if (!matcher1) {
                bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
                bizResult.setMsg("手机号码输入格式错误");
            } else {
          /*  if(entity.getMobile().length()==11){
                throw new UserNameRepeatException("手机号码输入格式错误");
            }*/
                crmSysUserMapper.updateById(entity);
                bizResult.setCode(CommonConstant.CODE_SUCCESS);
                bizResult.setMsg("更新用户信息成功");
            }
        } catch (Exception e) {
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("更新用户信息失败");
        }
        return bizResult;
    }

    /**
     * 根据ID删除用户
     * 删除用户关联的角色数据
     *
     * @param id 用户ID
     */
    @Transactional
    public BizResult deleteById(int id) {
        BizResult bizResult = new BizResult();
        try {
            CrmSysUser crmSysUser = crmSysUserMapper.selectById(id);
            QueryWrapper<SysPlatformUser> wrapperuser = new QueryWrapper<>();
            SysPlatformUser foruser = new SysPlatformUser();
            foruser.setUsername(crmSysUser.getUsername());
            wrapperuser.setEntity(foruser);
            sysPlatformUserMapper.delete(wrapperuser);
            //删除用户
            crmSysUserMapper.deleteById(id);

            //删除用户角色关系
            QueryWrapper<CrmSysUserRole> wrapper = new QueryWrapper<>();
            CrmSysUserRole entity = new CrmSysUserRole();
            entity.setUserId(id);
            wrapper.setEntity(entity);
            crmSysUserRoleMapper.delete(wrapper);


            // 删除多租户表中的用户
            QueryWrapper<CrmPlatformUser> wrapperPlat = new QueryWrapper<>();
            CrmPlatformUser crmPlatformUser = new CrmPlatformUser();
            crmPlatformUser.setUsername(crmSysUser.getUsername());
            wrapperPlat.setEntity(crmPlatformUser);

            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("删除用户成功");
        } catch (Exception e) {
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("删除用户失败");
        }
        return bizResult;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户实体
     */
    public CrmSysUser getUserByUsername(String username) {
        CrmSysUser user = new CrmSysUser();
        user.setUsername(username);
        return crmSysUserMapper.selectOne(new QueryWrapper<>(user));
    }

    /**
     * 获取登陆的用户平台信息
     *
     * @param username 用户名
     * @return 多租户用户实体
     */
    public CrmPlatformUser getPlatFormUsername(String username) {
        CrmPlatformUser user = new CrmPlatformUser();
        user.setUsername(username);
        return user.selectOne(new QueryWrapper<>(user));
    }

    /**
     * 获取用户状态
     *
     * @return 用户状态集合
     */
    public List<Map<String, String>> getUserStatus() {
        return sysDictMapper.getDictList(AdminCommonConstant.DICT_TABLE_CRM_SYS_USER, AdminCommonConstant.DICT_FIELD_USER_STATUS);
    }

    /**
     * 获取用户性别
     *
     * @return 性别集合
     */
    public List<Map<String, String>> getUserGender() {
        return sysDictMapper.getDictList(AdminCommonConstant.DICT_TABLE_GLOBAL, AdminCommonConstant.DICT_FIELD_GENDER);
    }

    /**
     * 根据条件获取用户数量
     *
     * @param user 查询条件对象
     * @return 用户数量
     */
    public int selectUserCount(CrmSysUser user) {
        return crmSysUserMapper.selectUserCount(user);
    }

    /**
     * 根据条件获取用户列表
     *
     * @param user 查询条件对象
     * @return 用户集合
     */
    public List<CrmSysUser> selectUserList(CrmSysUser user) {
        List<CrmSysUser> list = crmSysUserMapper.selectUserList(user);
       /* for (CrmSysUser crmSysUser : list) {
            crmSysUser.setCreateDate(DateUtil.format(crmSysUser.getCreateDate(),DateUtil.YYYYMMDD,DateUtil.YYYY_MM_DD));
        }*/
        return list;
    }

    /**
     * 获取全部用户列表
     *
     * @return 用户集合
     */
    public List<CrmSysUser> selectAllUser() {
        List<CrmSysUser> list = crmSysUserMapper.selectAllUser();
        /*for (CrmSysUser crmSysUser : list) {
            crmSysUser.setCreateDate(DateUtil.format(crmSysUser.getCreateDate(),DateUtil.YYYYMMDD,DateUtil.YYYY_MM_DD));
        }*/
        return list;
    }

    /**
     * 获取当前登录用户能查看的用户
     *
     * @param deptIds 部门id
     * @return 用户集合
     */
    public List<CrmSysUser> selectAllDataAuthUser(List<Integer> deptIds) {
        List<CrmSysUser> list = crmSysUserMapper.selectAllDataAuthUser(deptIds);
        /*for (CrmSysUser crmSysUser : list) {
            crmSysUser.setCreateDate(DateUtil.format(crmSysUser.getCreateDate(),DateUtil.YYYYMMDD,DateUtil.YYYY_MM_DD));
        }*/
        return list;
    }

    /**
     * 获取用户所选角色ID
     *
     * @param id 用户ID
     * @return 用户所属角色集合
     */
    public List<CrmSysUserRole> selectUserRoles(Integer id) {
        QueryWrapper<CrmSysUserRole> wrapper = new QueryWrapper<>();
        CrmSysUserRole entity = new CrmSysUserRole();
        entity.setUserId(id);
        wrapper.setEntity(entity);
        return crmSysUserRoleMapper.selectList(wrapper);
    }

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     * @return 操作信息
     */
    @Transactional
    public JSONObject unlockUser(Integer userId, Integer currentUserId) {
        JSONObject json = new JSONObject();
        try {
            // 更新用户锁定状态
            CrmSysUser crmSysUser = new CrmSysUser();
            crmSysUser.setId(userId);
            crmSysUser.setStatus("0");
            crmSysUser.setLoginErrorCount(0);
            crmSysUser.setUpdateUserId(currentUserId);
            crmSysUserMapper.updateById(crmSysUser);

            // 将最近10分钟登录错误日志打上标记，下次登录不统计带标记的错误次数
            CrmSysUser user = crmSysUserMapper.selectById(userId);
            String userName = user.getUsername();
            String specifyMinutesDate = DateUtil.format(DateUtil.getDateBeforerMin(10), DateUtil.YYYY_MM_DD_HH_MM);
            crmSysLoginLogMapper.updateUnlockFlagOfSpecifyMinutes(userName, specifyMinutesDate);

            json.put("code", CommonConstant.CODE_SUCCESS);
            json.put("msg", "解锁用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", CommonConstant.CODE_BIZ_ERROR);
            json.put("msg", "解锁用户失败，请稍后重试");
        }
        return json;
    }

    /**
     * 重置密码
     *
     * @param id
     * @param newPassword
     */
    public void resetPassword(int id, String newPassword) {
        CrmSysUser crmSysUser = this.crmSysUserMapper.selectById(id);
        SysPlatformUser sysPlatformUser = this.sysPlatUserBiz.getByUsername(crmSysUser.getUsername(), new DataSourceDto(masterDbName, masterSchema));
        String oldPassword = sysPlatformUser.getPassword();
        Assert.isTrue(
                !this.sysPlatformUserService.matchPassword(newPassword, oldPassword),
                "重置密码失败,和原密码相同"
        );
        this.sysPlatUserBiz.updateByUsername(
                crmSysUser.getUsername(),
                new SysPlatformUser().setPassword(this.sysPlatformUserService.encryptPassword(newPassword)),
                new DataSourceDto(masterDbName, masterSchema)
        );
    }

    /**
     * 用户更新密码
     *
     * @param crmSysUser 用户对象
     * @return 操作信息
     */
    public JSONObject modifyPassword(CrmSysUser crmSysUser) {
        JSONObject json = new JSONObject();
        CrmSysUser userInfo = crmSysUserMapper.selectById(crmSysUser.getId());
        String password = userInfo.getPassword();
        String newPasswrod = crmSysUser.getNewPassword();
        String newPasswordAgain = crmSysUser.getNewPasswordAgain();
        boolean bo = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).matches(crmSysUser.getOldPassword(), password);
        if (!bo) {
            json.put("code", CommonConstant.CODE_BIZ_ERROR);
            json.put("msg", "旧密码输入错误");
        } else if (!StringUtils.equals(newPasswrod, newPasswordAgain)) {
            json.put("code", CommonConstant.CODE_BIZ_ERROR);
            json.put("msg", "新密码与确认密码不一致");
        } else {
            CrmSysUser modifyInfo = new CrmSysUser();
            modifyInfo.setId(crmSysUser.getId());
            modifyInfo.setPassword(new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(crmSysUser.getNewPassword()));
            crmSysUserMapper.updateById(modifyInfo);
            json.put("code", CommonConstant.CODE_SUCCESS);
            json.put("msg", "更新用户密码成功");
        }
        return json;
    }

    /**
     * 获取系统管理/用户管理菜单ID
     *
     * @return 用户管理菜单ID
     */
    public Integer getUserManagerMenuId() {
        CrmSysMenu crmSysMenu = new CrmSysMenu();
        crmSysMenu.setCode(userManagerMenuCode);
        CrmSysMenu result = crmSysMenuMapper.selectOne(new QueryWrapper<>(crmSysMenu));
        return result.getId();
    }

    public Integer updatestatususer(String status, String username) {
        mapper.updatestatus(status, username);
        return crmSysUserMapper.updatestatususer(status, username);
    }

}
