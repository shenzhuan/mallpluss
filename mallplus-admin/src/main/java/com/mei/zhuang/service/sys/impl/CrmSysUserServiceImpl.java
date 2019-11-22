package com.mei.zhuang.service.sys.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.constant.AdminCommonConstant;
import com.mei.zhuang.dao.sys.CrmSysUserMapper;
import com.mei.zhuang.dao.sys.CrmSysUserRoleMapper;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.entity.sys.CrmSysUserRole;
import com.mei.zhuang.service.sys.ICrmSysUserService;
import com.mei.zhuang.vo.sys.DataSourceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author arvato team
 * @since 2018-08-22
 */
@Service
public class CrmSysUserServiceImpl extends ServiceImpl<CrmSysUserMapper, CrmSysUser> implements ICrmSysUserService {


    @Resource
    private CrmSysUserMapper crmSysUserMapper;
    @Resource
    private CrmSysUserRoleMapper crmSysUserRoleMapper;

    @Transactional
    @Override
    public void createTenantUserInfo(String username, DataSourceDto dataSourceDto){
        CrmSysUser sysUser = new CrmSysUser()
                                    .setUsername(username)
                                    .setDeptId(AdminCommonConstant.SYS_DEPT_IT_ID);
        this.crmSysUserMapper.insert(sysUser);
        this.crmSysUserRoleMapper.insert(
                new CrmSysUserRole()
                        .setUserId(sysUser.getId())
                        .setRoleId(AdminCommonConstant.SYS_ROLE_ADMIN_ID)
        );
    }


    public Integer updatestatus(String status,String username){
        return crmSysUserMapper.updatestatususer(status,username);
    }
}
