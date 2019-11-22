package com.mei.zhuang.service.order.impl;

import com.mei.zhuang.dao.order.EsAppletTemplatesMapper;
import com.mei.zhuang.service.order.EsAppletTemplateService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.order.EsAppletTemplates;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsAppletTemplateServiceImpl extends ServiceImpl<EsAppletTemplatesMapper, EsAppletTemplates> implements EsAppletTemplateService {

    @Resource
    private EsAppletTemplatesMapper esAppletTemplatesMapper;
    @Override
    public Map<String, Object> select(EsAppletTemplates entity) {
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        Map<String,Object> map=new HashMap<String,Object>();
        List<EsAppletTemplates> list=esAppletTemplatesMapper.select(page,entity);
        Integer count=esAppletTemplatesMapper.count();
        map.put("rows", list);
        map.put("total", count);
        map.put("current", entity.getCurrent());
        map.put("size", entity.getSize());
        return map;
    }
}
