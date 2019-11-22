package com.mei.zhuang.dao.sys;

import com.arvato.common.orm.model.SysLoginLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    void updateUnlockFlagOfSpecifyMinutes(@Param("loginName") String loginName, @Param("specifyTime") String specifyTime);

    /**
     * 获取指定时间内密码错误次数
     * @param loginName
     * @param specifyTime
     * @return
     */
    int getSpecifyMinutesPasswordErrorCount(@Param("loginName") String loginName, @Param("specifyTime") String specifyTime);

}
