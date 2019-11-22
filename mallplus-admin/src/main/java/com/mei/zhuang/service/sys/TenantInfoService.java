package com.mei.zhuang.service.sys;

import com.mei.zhuang.entity.sys.SysTenant;
import com.mei.zhuang.vo.sys.DataSourceDto;

import java.util.List;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/17
 */
public interface TenantInfoService {
    /**
     * 通过id 检测租户是否存在
     * @param tenantId : 租户id
     * @return
     */
    DataSourceDto getDataSourceByTenantId(Integer tenantId);

    /**
     * 根据id获取有效的租户列表
     * @param tenantIds
     * @return
     */
    List<SysTenant> getValidTenantByIds(String[] tenantIds);

    /**
     * 根据id查询租户
     * @param tenantId
     * @return
     */
    SysTenant getTenantById(Integer tenantId);
}
