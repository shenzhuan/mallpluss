package com.mei.zhuang.service.sys.impl;

import com.arvato.admin.service.ICrmSysUserService;
import com.arvato.admin.service.ISysPlatformUserService;
import com.arvato.common.dto.DataSourceDto;
import com.arvato.common.dto.SysPlatformUserPagingData;
import com.arvato.common.dto.SysPlatformUserPagingParam;
import com.arvato.common.orm.dao.SysDbResourceMapper;
import com.arvato.common.orm.dao.SysPlatformUserMapper;
import com.arvato.common.orm.dao.SysTenantMapper;
import com.arvato.common.orm.model.SysDbResource;
import com.arvato.common.orm.model.SysPlatformUser;
import com.arvato.common.orm.model.SysTenant;
import com.arvato.utils.constant.UserConstant;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
@Service
public class SysPlatformUserServiceImpl extends ServiceImpl<SysPlatformUserMapper, SysPlatformUser> implements ISysPlatformUserService {

    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysDbResourceMapper sysDbResourceMapper;
    @Autowired
    private ICrmSysUserService crmSysUserService;
    @Autowired
    private SysPlatformUserMapper mapper;

    @Override
    public int getPagingTotal(SysPlatformUserPagingParam param) {
        return this.baseMapper.getPagingTotal(param);
    }

    @Override
    public List<SysPlatformUserPagingData> getPagingList(SysPlatformUserPagingParam param) {
        return this.baseMapper.getPagingList(param);
    }

    @Override
    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(password);
    }

    @Override
    public SysPlatformUser selectByUsername(String username) {
        return this.baseMapper.selectByUsername(username);
    }

    @Override
    public boolean matchPassword(String password, String encryptedPassword) {
        return new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).matches(password,encryptedPassword);
    }

    @Override
    public void platformUserInit(SysPlatformUser platformUser) {
        Integer[] tenantIds = platformUser.getManageTenantIds();
        Arrays.stream(tenantIds).forEach(tenantId -> {
            SysTenant sysTenant = sysTenantMapper.selectById(tenantId);
            SysDbResource sysDbResource = sysDbResourceMapper.selectById(sysTenant.getDbResourceId());
            crmSysUserService.createTenantUserInfo(platformUser.getUsername(),new DataSourceDto(sysDbResource.getDbName(),sysTenant.getDbSchema()));
        });
    }

    @Override
    public Integer updatestatus(String status,String username){
        return this.mapper.updatestatus(status,username);
    }



}
