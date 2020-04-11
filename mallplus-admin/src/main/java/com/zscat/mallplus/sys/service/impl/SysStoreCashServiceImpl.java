package com.zscat.mallplus.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.sys.entity.SysStoreCash;
import com.zscat.mallplus.sys.mapper.SysStoreCashMapper;
import com.zscat.mallplus.sys.service.ISysStoreCashService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mallplus
 * @date 2020-04-10
 */
@Service
public class SysStoreCashServiceImpl extends ServiceImpl<SysStoreCashMapper, SysStoreCash> implements ISysStoreCashService {

    @Resource
    private SysStoreCashMapper sysStoreCashMapper;

    @Override
    public int audit(List<Long> ids, Integer showStatus) {
        SysStoreCash pmsBrand = new SysStoreCash();
        pmsBrand.setStatus(showStatus);
        return sysStoreCashMapper.update(pmsBrand, new QueryWrapper<SysStoreCash>().in("id", ids));
    }

}
