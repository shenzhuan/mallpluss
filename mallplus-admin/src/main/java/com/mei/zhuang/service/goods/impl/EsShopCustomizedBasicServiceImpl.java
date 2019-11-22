package com.mei.zhuang.service.goods.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsShopCustomizedBasicMapper;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import com.mei.zhuang.service.goods.EsShopCustomizedBasicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EsShopCustomizedBasicServiceImpl extends ServiceImpl<EsShopCustomizedBasicMapper, EsShopCustomizedBasic> implements EsShopCustomizedBasicService {

    @Resource
    private EsShopCustomizedBasicMapper esShopCustomizedBasicMapper;

    @Override
    public EsShopCustomizedBasic detail(Long id) {
        return esShopCustomizedBasicMapper.detail(id);
    }
}
