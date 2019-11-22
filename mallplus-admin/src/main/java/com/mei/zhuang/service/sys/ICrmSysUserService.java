package com.mei.zhuang.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.vo.sys.DataSourceDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author arvato team
 * @since 2018-08-22
 */
public interface ICrmSysUserService extends IService<CrmSysUser> {

    void createTenantUserInfo(String username, DataSourceDto dataSourceDto);
    //状态修改
    Integer updatestatus(String status, String username);
}
