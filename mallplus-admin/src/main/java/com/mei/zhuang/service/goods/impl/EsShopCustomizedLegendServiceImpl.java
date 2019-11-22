package com.mei.zhuang.service.goods.impl;

import com.mei.zhuang.dao.goods.EsShopCustomizedLegendMapper;
import com.mei.zhuang.service.goods.EsShopCustomizedLegendService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopCustomizedLegend;
import com.mei.zhuang.entity.goods.EsShopGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EsShopCustomizedLegendServiceImpl extends ServiceImpl<EsShopCustomizedLegendMapper, EsShopCustomizedLegend> implements EsShopCustomizedLegendService {

    @Resource
    private EsShopCustomizedLegendMapper esShopCustomizedLegendMapper;


}
