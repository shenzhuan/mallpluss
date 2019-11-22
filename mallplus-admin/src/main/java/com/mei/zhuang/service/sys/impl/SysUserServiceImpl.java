package com.mei.zhuang.service.sys.impl;


import com.mei.zhuang.dao.sys.SysPlatformUserMapper;
import com.mei.zhuang.entity.sys.SysPlatformUser;
import com.mei.zhuang.service.sys.SysUserService;
import com.mei.zhuang.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/17
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysPlatformUserMapper sysPlatformUserMapper;

    @Override
    public SysPlatformUser getPlatUserInfoByUserName(String username) {
//        return sysPlatformUserMapper.selectOne(new SysPlatformUser().setUsername(username));
        return sysPlatformUserMapper.selectByUsername(username);

    }

    @Override
    public void recordLastLoginTimeAndResetLoginErrorCount(Integer userId) {
        String lastTime = DateUtil.getYYYY_MM_DD_HH_MM_SSFormat();
        sysPlatformUserMapper.recordLastLoginTimeAndResetLoginErrorCount(userId,lastTime);
    }

    @Override
    public void lockAccount(Integer userId) {
        sysPlatformUserMapper.lockAccount(userId);
    }
}
