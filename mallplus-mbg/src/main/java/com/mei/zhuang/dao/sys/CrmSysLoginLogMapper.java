package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysLoginLog;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2018-01-09
 */
public interface CrmSysLoginLogMapper extends BaseMapper<CrmSysLoginLog> {

    int getSpecifyMinutesPasswordErrorCount(@Param("loginName") String loginName, @Param("specifyTime") String specifyTime, @Param("schema") String schema);

    void updateUnlockFlagOfSpecifyMinutes(@Param("loginName") String loginName, @Param("specifyTime") String specifyTime);

    void updateUnlockFlagOfSpecifyMinutesWithSchema(@Param("loginName") String loginName, @Param("specifyTime") String specifyTime, @Param("schema") String schema);

    void insertLoginLog(@Param("loginLog") CrmSysLoginLog loginLog, @Param("schema") String schema);
}
