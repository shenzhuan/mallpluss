package com.mei.zhuang.service.sys;

import com.arvato.common.dto.SysTenantPagingData;
import com.arvato.common.dto.SysTenantPagingParam;
import com.arvato.common.msg.DictData;
import com.arvato.common.orm.model.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
public interface ISysTenantService extends IService<SysTenant> {

    int getPagingTotal(SysTenantPagingParam param);
    List<SysTenantPagingData> getPagingList(SysTenantPagingParam param);

    /**
     * schema 名称规范检查
     * @return
     */
    boolean schemaNameCheck(String schema);

    List<DictData> getDictList();

    /**
     * 租户初始化
     */
    void initTenant(SysTenant sysTenant);
}
