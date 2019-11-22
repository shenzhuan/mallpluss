package com.mei.zhuang.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsCoreAddressMapper;
import com.mei.zhuang.entity.goods.EsCoreAddress;
import com.mei.zhuang.service.goods.EsCoreAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EsCoreAddressServiceImpl extends ServiceImpl<EsCoreAddressMapper, EsCoreAddress> implements EsCoreAddressService {

    @Resource
    private EsCoreAddressMapper esCoreAddressMapper;

    @Override
    public List<EsCoreAddress> selEsCoreAddress(EsCoreAddress entity) {
        List<EsCoreAddress> list = esCoreAddressMapper.selEsCoreAddress(entity.getLevel(), entity.getParentId());
        if (list != null) {
            //查询子分类
            for (EsCoreAddress coreAddress : list) {
                EsCoreAddress esCoreAddress = new EsCoreAddress();
                esCoreAddress.setParentId(coreAddress.getCodeId());
                coreAddress.setListEsCoreAddress(esCoreAddressMapper.selectList(new QueryWrapper<>(esCoreAddress)));
            }
        }
        return list;
    }
}
