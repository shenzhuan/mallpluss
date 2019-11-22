package com.mei.zhuang.service.sys.impl;


import com.mei.zhuang.dao.sys.SysLoginLogMapper;
import com.mei.zhuang.entity.sys.SysLoginLog;
import com.mei.zhuang.service.sys.SysLoginLogService;
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
public class SysLoginLogServiceImpl implements SysLoginLogService {

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    public void updateUnlockFlagOfSpecifyMinutes( String username, String dateBefore){
        sysLoginLogMapper.updateUnlockFlagOfSpecifyMinutes(username,dateBefore);
    }

    @Override
    public int getSpecifyMinutesPasswordErrorCount(String username, String specifyMinutesDate) {
        return sysLoginLogMapper.getSpecifyMinutesPasswordErrorCount(username,specifyMinutesDate);
    }

    @Override
    public void saveCysLoginLog(int code, String value, String username) {
        log.info("插入登陆日志：code:{},value:{},usernaem:{}",code,value,username);
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setLoginName(username);
        loginLog.setLoginResult(code);
        loginLog.setLoginDesc(value);
        loginLog.setLoginTime(DateUtil.getYYYY_MM_DD_HH_MM_SSFormat());
      //  sysLoginLogMapper.insert(loginLog);
    }
}
