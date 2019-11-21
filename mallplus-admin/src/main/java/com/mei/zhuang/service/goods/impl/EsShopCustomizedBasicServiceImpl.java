package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopCustomizedBasicMapper;
import com.arvato.service.goods.api.service.EsShopCustomizedBasicService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EsShopCustomizedBasicServiceImpl extends ServiceImpl<EsShopCustomizedBasicMapper, EsShopCustomizedBasic> implements EsShopCustomizedBasicService {

    @Resource
    private  EsShopCustomizedBasicMapper esShopCustomizedBasicMapper;

    @Override
    public EsShopCustomizedBasic detail(Long id) {
        return esShopCustomizedBasicMapper.detail(id);
    }
}
