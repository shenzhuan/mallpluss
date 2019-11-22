package com.mei.zhuang.service.sys;

import com.mei.zhuang.entity.sys.SysPlatformUser;

/**
 * @Description:系统用户
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/17
 */
public interface SysUserService {

    /**
     * 根据用户名获取平台用户信息
     * @param username
     * @return
     */
    SysPlatformUser getPlatUserInfoByUserName(String username);

    /**
     * 更新用户登陆信息
     * @param userId
     * @return
     */
    void recordLastLoginTimeAndResetLoginErrorCount(Integer userId);

    /**
     * 锁定用户
     * @param userId
     */
    void lockAccount(Integer userId);
}
