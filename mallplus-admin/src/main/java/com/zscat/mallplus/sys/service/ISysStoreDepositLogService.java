package com.zscat.mallplus.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sys.entity.SysStoreCash;
import com.zscat.mallplus.sys.entity.SysStoreDepositLog;

/**
 * @author mallplus
 * @date 2020-04-10
 */

public interface ISysStoreDepositLogService extends IService<SysStoreDepositLog> {

    /**
     * 预存款提现
     *
     * @param entity
     * @return
     */
    Object drawStoreMoney(SysStoreCash entity);

    /**
     * 预存款充值
     *
     * @param entity
     * @return
     */
    Object addStoreMoney(SysStoreDepositLog entity);

    /**
     * 预存款提现审核
     *
     * @param entity
     * @return
     */
    Object auditStoreMoney(SysStoreCash entity);
}
