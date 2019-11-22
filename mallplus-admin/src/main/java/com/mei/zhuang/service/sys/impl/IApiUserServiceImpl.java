package com.mei.zhuang.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.sys.CrmApiInterfaceMapper;
import com.mei.zhuang.dao.sys.CrmApiUserMapper;
import com.mei.zhuang.dao.sys.CrmApiUserPermissionMapper;
import com.mei.zhuang.entity.sys.CrmApiUser;
import com.mei.zhuang.service.sys.IApiUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IApiUserServiceImpl implements IApiUserService {

    @Resource
    private CrmApiUserMapper apiUserMapper;
    @Resource
    private CrmApiInterfaceMapper crmApiInterfaceMapper;
    @Resource
    private CrmApiUserPermissionMapper crmApiUserPermissionMapper;


    @Override
    public CrmApiUser getApiUserByName(String name) {
        CrmApiUser apiUser = new CrmApiUser();
        apiUser.setAccount(name);
        // 判断用户是否存在
        try {
            apiUser = apiUserMapper.selectOne(new QueryWrapper<>(apiUser));
            return apiUser;
        } catch (Exception e) {
            log.error("数据问题，有重复用户名！",e);
            return null;
        }
    }

    @Override
    public boolean hasApiAuth(String apiUserId, String requestUri) {
        log.info("验证用户接口权限");
        Integer apiId = crmApiInterfaceMapper.selectInterfaceByUri(requestUri);
        if(apiId!= null && crmApiUserPermissionMapper.hasAuthByApiId(Integer.parseInt(apiUserId),apiId) > 0){
            log.info("用户id:{}拥有接口：{} 权限",apiUserId,apiId);
            return true;
        }
        return false;
    }
}
