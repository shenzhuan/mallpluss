package com.mei.zhuang.dao.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.SysPlatformUser;
import com.mei.zhuang.vo.sys.SysPlatformUserPagingData;
import com.mei.zhuang.vo.sys.SysPlatformUserPagingParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-01-16
 */
public interface SysPlatformUserMapper extends BaseMapper<SysPlatformUser> {

    void recordLastLoginTimeAndResetLoginErrorCount(@Param("userId") Integer userId, @Param("lastTime") String lastTime);

    /**
     * 锁定用户
     *
     * @param userId
     */
    void lockAccount(@Param("userId") Integer userId);

    /**
     * 查询用户信息
     *
     * @param username
     * @return
     */
    SysPlatformUser selectByUsername(@Param("username") String username);

    int getPagingTotal(@Param("param") SysPlatformUserPagingParam param);

    List<SysPlatformUserPagingData> getPagingList(@Param("param") SysPlatformUserPagingParam param);

    Integer updatestatus(@Param("status") String status, @Param("username") String username);
}
