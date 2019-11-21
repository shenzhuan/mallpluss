package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopCustomizedLegendMapper;
import com.arvato.service.goods.api.service.EsShopCustomizedLegendService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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

    @Override
    public Map<String, Object> selLegendPage(EsShopCustomizedLegend entity) {
        Map<String,Object> result=new HashMap<String,Object>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        List<EsShopCustomizedLegend> list=esShopCustomizedLegendMapper.selLegendPage(page,entity);
        Integer count=esShopCustomizedLegendMapper.selectCount(new QueryWrapper<>(entity));
        result.put("rows", list);
        result.put("total", count);
        result.put("current", entity.getCurrent());
        result.put("size", entity.getSize());
        return result;
    }
}
