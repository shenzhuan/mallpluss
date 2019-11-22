package com.mei.zhuang.service.sys;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/17
 */
public interface SysLoginLogService {

    /**
     * 更新登陆解锁状态
     * @param username
     * @param dateBefore
     */
    void updateUnlockFlagOfSpecifyMinutes(String username, String dateBefore);

    /**
     * 获取10分前密码错误次数
     * @param username
     * @param specifyMinutesDate
     * @return
     */
    int getSpecifyMinutesPasswordErrorCount(String username, String specifyMinutesDate);

    /**
     * 插入登陆日志
     * @param code
     * @param value
     * @param username
     */
    void saveCysLoginLog(int code, String value, String username);
}
