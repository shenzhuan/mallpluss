package com.mei.zhuang.service.goods.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsShopCustomizedLegendMapper;
import com.mei.zhuang.entity.goods.EsShopCustomizedLegend;
import com.mei.zhuang.service.goods.EsShopCustomizedLegendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class EsShopCustomizedLegendServiceImpl extends ServiceImpl<EsShopCustomizedLegendMapper, EsShopCustomizedLegend> implements EsShopCustomizedLegendService {

    @Resource
    private EsShopCustomizedLegendMapper esShopCustomizedLegendMapper;


}
