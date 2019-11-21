package com.mei.zhuang.service.sys.impl;

import com.arvato.admin.constant.AdminCommonConstant;
import com.arvato.admin.orm.dao.CrmSysUserMapper;
import com.arvato.admin.orm.dao.CrmSysUserRoleMapper;
import com.arvato.admin.service.ICrmSysUserService;
import com.arvato.common.annotation.DynamicData;
import com.arvato.common.dto.DataSourceDto;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.entity.sys.CrmSysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Autowired
    private CrmSysUserMapper crmSysUserMapper;
    @Autowired
    private CrmSysUserRoleMapper crmSysUserRoleMapper;

    @Transactional
    @DynamicData
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
