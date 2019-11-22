package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.dao.order.EsCoreMessageTemplateMapper;
import com.mei.zhuang.entity.order.EsCoreMessageTemplate;
import com.mei.zhuang.service.order.EsCoreMessageTemplateService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "模版消息管理", description = "", tags = {"模版消息管理"})
@Service
public class EsCoreMessageTemplateServiceImpl extends ServiceImpl<EsCoreMessageTemplateMapper, EsCoreMessageTemplate> implements EsCoreMessageTemplateService {

    @Resource
    private EsCoreMessageTemplateMapper esCoreMessageTemplateMapper;
    @Override
    public Map<String, Object> select(EsCoreMessageTemplate entity) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageHelper.startPage(entity.getCurrent(), entity.getSize());
            List<EsCoreMessageTemplate> list = esCoreMessageTemplateMapper.selectList(new QueryWrapper<>(entity));
            int count = esCoreMessageTemplateMapper.count(entity);
            result.put("rows", list);
            result.put("total", count);
            result.put("current", entity.getCurrent());
            result.put("size", entity.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
