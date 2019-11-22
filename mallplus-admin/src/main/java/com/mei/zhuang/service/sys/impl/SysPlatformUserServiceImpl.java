package com.mei.zhuang.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.constant.UserConstant;
import com.mei.zhuang.dao.sys.SysDbResourceMapper;
import com.mei.zhuang.dao.sys.SysPlatformUserMapper;
import com.mei.zhuang.dao.sys.SysTenantMapper;
import com.mei.zhuang.entity.sys.SysDbResource;
import com.mei.zhuang.entity.sys.SysPlatformUser;
import com.mei.zhuang.entity.sys.SysTenant;
import com.mei.zhuang.service.sys.ICrmSysUserService;
import com.mei.zhuang.service.sys.ISysPlatformUserService;
import com.mei.zhuang.vo.sys.DataSourceDto;
import com.mei.zhuang.vo.sys.SysPlatformUserPagingData;
import com.mei.zhuang.vo.sys.SysPlatformUserPagingParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author meizhuang team
 * @since 2019-01-16
 */
@Service
public class SysPlatformUserServiceImpl extends ServiceImpl<SysPlatformUserMapper, SysPlatformUser> implements ISysPlatformUserService {

    @Resource
    private SysTenantMapper sysTenantMapper;
    @Resource
    private SysDbResourceMapper sysDbResourceMapper;
    @Resource
    private ICrmSysUserService crmSysUserService;
    @Resource
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
        return new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).matches(password, encryptedPassword);
    }

    @Override
    public void platformUserInit(SysPlatformUser platformUser) {
        String[] tenantIds = platformUser.getManageTenantIds().split(",");
        Arrays.stream(tenantIds).forEach(tenantId -> {
            SysTenant sysTenant = sysTenantMapper.selectById(tenantId);
            SysDbResource sysDbResource = sysDbResourceMapper.selectById(sysTenant.getDbResourceId());
            crmSysUserService.createTenantUserInfo(platformUser.getUsername(), new DataSourceDto(sysDbResource.getDbName(), sysTenant.getDbSchema()));
        });
    }

    @Override
    public Integer updatestatus(String status, String username) {
        return this.mapper.updatestatus(status, username);
    }


}
