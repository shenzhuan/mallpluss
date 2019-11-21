package com.mei.zhuang.service.sys;

import com.arvato.common.dto.SysPlatformUserPagingData;
import com.arvato.common.dto.SysPlatformUserPagingParam;
import com.arvato.common.orm.model.SysPlatformUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
public interface ISysPlatformUserService extends IService<SysPlatformUser> {

    int getPagingTotal(SysPlatformUserPagingParam param);
    List<SysPlatformUserPagingData> getPagingList(SysPlatformUserPagingParam param);

    /**
     * 密码加密
     * @param password
     * @return
     */
    String encryptPassword(String password);

    SysPlatformUser selectByUsername(@Param("username") String username);

    /**
     * 密码匹配
     * @param password
     * @param encryptedPassword
     * @return
     */
    boolean matchPassword(String password, String encryptedPassword);

    /**
     * 平台账户初始化
     * @param platformUser
     */
    void platformUserInit(SysPlatformUser platformUser);
    //修改状态
    Integer updatestatus(String status, String username);
}
