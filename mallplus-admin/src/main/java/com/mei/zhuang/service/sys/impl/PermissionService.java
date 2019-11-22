package com.mei.zhuang.service.sys.impl;

import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.service.sys.biz.UserBiz;
import com.mei.zhuang.vo.sys.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by john on 2017/9/12.
 */
@Service
public class PermissionService {
    @Resource
    private UserBiz userBiz;

    public UserInfo getUserByUsernameWithSchema(String username) {
        UserInfo info = null;
        CrmSysUser user = userBiz.getUserByUsername(username);
        if (user != null){
            info = new UserInfo();
            BeanUtils.copyProperties(user, info);
            info.setId(user.getId().toString());
        }
        return info;
    }

}
