package com.mei.zhuang.service.sys.impl;

import com.mei.zhuang.dao.sys.CrmSysDictMapper;
import com.mei.zhuang.dao.sys.CrmSysMenuMapper;
import com.mei.zhuang.dao.sys.CrmSysRoleMapper;
import com.mei.zhuang.dao.sys.CrmSysUserMapper;
import com.mei.zhuang.service.sys.ICrmSysDeptService;
import com.mei.zhuang.service.sys.ISysPlatformUserService;
import com.mei.zhuang.service.sys.IUserService;
import com.mei.zhuang.vo.sys.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private CrmSysUserMapper sysUserMapper;

    @Resource
    private ICrmSysDeptService crmSysDeptService;
    @Resource
    private ISysPlatformUserService service;

    @Resource
    private CrmSysMenuMapper crmSysMenuMapper;

    @Resource
    private CrmSysDictMapper sysDictMapper;

    @Resource
    private CrmSysRoleMapper roleMapper;
    @Resource
    private PermissionService permissionService;

    @Override
    public UserInfo getUserByUsername(String username) {
        return permissionService.getUserByUsernameWithSchema(username);
    }

    @Override
    public Integer getCurrentname(String userName) {
        return this.sysUserMapper.selectid(userName);
    }

    @Override
    public Integer updatestatus(String status, String username) {
        return sysUserMapper.updatestatususer(status,username);
    }

    @Override
    public boolean hasApiAuth(Integer userId, String requestUri) {
        List<Integer> roleIdList=  roleMapper.getRoleIdsByUserId(userId);
        //  int i = crmSysMenuMapper.hasAuthByRoleIds2(roleIdList, requestUri);
        if (roleIdList!=null && roleIdList.size()>0){
            if(crmSysMenuMapper.hasAuthByRoleIds2(roleIdList,requestUri)>0){
                log.info("用户id:{}拥有接口：{} 的权限",userId,requestUri);
                return true;
            }
        }
        log.info("用户id:{}没有接口：{} 的权限",userId,requestUri);
        return false;
    }
}
