package com.mei.zhuang.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.sys.SysTenant;
import com.mei.zhuang.vo.DictData;
import com.mei.zhuang.vo.sys.SysTenantPagingData;
import com.mei.zhuang.vo.sys.SysTenantPagingParam;

import java.util.List;

/**
 * <p>
 * 服务类
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
     *
     * @return
     */
    boolean schemaNameCheck(String schema);

    List<DictData> getDictList();

    /**
     * 租户初始化
     */
    void initTenant(SysTenant sysTenant);
}
