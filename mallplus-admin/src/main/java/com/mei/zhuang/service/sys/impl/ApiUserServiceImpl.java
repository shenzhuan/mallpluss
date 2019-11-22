package com.mei.zhuang.service.sys.impl;


import com.mei.zhuang.dao.sys.CrmApiInterfaceMapper;
import com.mei.zhuang.dao.sys.CrmApiUserMapper;
import com.mei.zhuang.dao.sys.CrmApiUserPermissionMapper;
import com.mei.zhuang.service.sys.ApiUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/3
 */
@Service
@Slf4j
public class ApiUserServiceImpl  implements ApiUserService {

    @Resource
    private CrmApiUserMapper apiUserMapper;
    @Resource
    private CrmApiInterfaceMapper crmApiInterfaceMapper;
    @Resource
    private CrmApiUserPermissionMapper crmApiUserPermissionMapper;

    @Override
    public boolean hasApiAuth(String apiUserId, String requestUri) {
        log.info("接口是否有权限");
        Integer apiId = crmApiInterfaceMapper.selectInterfaceByUri(requestUri);
        if(apiId!= null && crmApiUserPermissionMapper.hasAuthByApiId(Integer.parseInt(apiUserId),apiId) > 0){
            log.info("用户id:{}拥有接口：{} 权限",apiUserId,apiId);
            return true;
        }
        return false;
    }
}
