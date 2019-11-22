package com.mei.zhuang.service.sys.impl;


import com.mei.zhuang.dao.sys.SysTenantMapper;
import com.mei.zhuang.entity.sys.SysTenant;
import com.mei.zhuang.service.sys.TenantInfoService;
import com.mei.zhuang.vo.sys.DataSourceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/17
 */
@Service
@Slf4j
public class TenantInfoServiceImpl implements TenantInfoService {

    @Resource
    private SysTenantMapper sysTenantMapper;

    @Override
    public DataSourceDto getDataSourceByTenantId(Integer tenantId) {
        return sysTenantMapper.selectDataSourceById(tenantId);
    }

    @Override
    public List<SysTenant> getValidTenantByIds(String[] tenantIds) {
        if(tenantIds == null){
            return null;
        }
        List<Integer> ids = new LinkedList<>();
        for(int i =0;i<tenantIds.length;i++){
            ids.add(Integer.valueOf(tenantIds[i]));
        }
        return sysTenantMapper.getValidTenantByIds(ids);
    }

    @Override
    public SysTenant getTenantById(Integer tenantId) {
        return sysTenantMapper.selectById(tenantId);
    }
}
