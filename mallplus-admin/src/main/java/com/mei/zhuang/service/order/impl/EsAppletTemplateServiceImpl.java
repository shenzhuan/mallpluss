package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.dao.order.EsAppletTemplatesMapper;
import com.mei.zhuang.entity.order.EsAppletTemplates;
import com.mei.zhuang.service.order.EsAppletTemplateService;
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
        Map<String, Object> map = new HashMap<String, Object>();
        PageHelper.startPage(entity.getCurrent(), entity.getSize());
        List<EsAppletTemplates> list = esAppletTemplatesMapper.selectList(new QueryWrapper<>(entity));
        Integer count = esAppletTemplatesMapper.count();
        map.put("rows", list);
        map.put("total", count);
        map.put("current", entity.getCurrent());
        map.put("size", entity.getSize());
        return map;
    }
}
