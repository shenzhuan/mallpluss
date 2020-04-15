package com.zscat.mallplus.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.enums.StatusEnum;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.sys.entity.SysStoreCash;
import com.zscat.mallplus.sys.entity.SysStoreDepositLog;
import com.zscat.mallplus.sys.mapper.SysStoreCashMapper;
import com.zscat.mallplus.sys.mapper.SysStoreDepositLogMapper;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.sys.service.ISysStoreDepositLogService;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mallplus
 * @date 2020-04-10
 */
@Service
public class SysStoreDepositLogServiceImpl extends ServiceImpl<SysStoreDepositLogMapper, SysStoreDepositLog> implements ISysStoreDepositLogService {

    @Resource
    private SysStoreDepositLogMapper sysStoreDepositLogMapper;
    @Resource
    private SysStoreCashMapper sysStoreCashMapper;
    @Resource
    private SysStoreMapper storeMapper;

    @Override
    public Object drawStoreMoney(SysStoreCash entity) {
        if (ValidatorUtils.empty(entity.getAccount())) {
            return new CommonResult().paramFailed("账户为空");
        }
        if (ValidatorUtils.empty(entity.getAmount())) {
            return new CommonResult().paramFailed("金额为空");
        }
        if (ValidatorUtils.empty(entity.getBank())) {
            return new CommonResult().paramFailed("银行为空");
        }
        if (entity.getAmount().compareTo(entity.getBalance()) > 0) {
            return new CommonResult().paramFailed("余额不足");
        }
        entity.setStatus(StatusEnum.AuditType.INIT.code());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        sysStoreCashMapper.insert(entity);

        SysStore store = storeMapper.selectById(UserUtils.getCurrentMember().getStoreId());
        store.setAmount(store.getAmount().subtract(entity.getAmount()));
        store.setFreezAmount(store.getFreezAmount().add(entity.getAmount()));
        storeMapper.updateById(store);
        return new CommonResult().success();
    }

    @Transactional
    @Override
    public Object addStoreMoney(SysStoreDepositLog entity) {
        if (entity.getMemo().equals("1")) {
            entity.setMemo("微信预存款充值");
        } else {
            entity.setMemo("支付宝预存款充值");
        }
        entity.setBalance(entity.getBalance().add(entity.getCredit()));
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setType(1);
        sysStoreDepositLogMapper.insert(entity);

        SysStore store = storeMapper.selectById(UserUtils.getCurrentMember().getStoreId());
        store.setAmount(store.getAmount().add(entity.getCredit()));
        storeMapper.updateById(store);
        return new CommonResult().success();
    }

    @Transactional
    @Override
    public Object auditStoreMoney(SysStoreCash entity) {
        SysStore store = storeMapper.selectById(UserUtils.getCurrentMember().getStoreId());
        if (entity.getStatus() == StatusEnum.AuditType.FAIL.code()) {
            store.setAmount(store.getAmount().add(entity.getAmount()));
            store.setFreezAmount(store.getFreezAmount().subtract(entity.getAmount()));
        } else if (entity.getStatus() == StatusEnum.AuditType.SUCESS.code()) {
            store.setFreezAmount(store.getFreezAmount().subtract(entity.getAmount()));
            if (store.getFreezAmount().compareTo(BigDecimal.ZERO) < 0) {
                return new CommonResult().paramFailed("冻结金额错误");
            }
            SysStoreDepositLog depositLog = new SysStoreDepositLog();
            depositLog.setDebit(entity.getAmount());
            depositLog.setBalance(store.getAmount());
            depositLog.setCreateTime(new Date());
            depositLog.setUpdateTime(new Date());
            depositLog.setType(7);
            sysStoreDepositLogMapper.insert(depositLog);
        }
        storeMapper.updateById(store);
        entity.setUpdateTime(new Date());
        sysStoreCashMapper.updateById(entity);

        return new CommonResult().success();
    }
}
