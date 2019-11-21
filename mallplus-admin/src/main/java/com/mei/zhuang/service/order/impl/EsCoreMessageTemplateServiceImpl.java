package com.mei.zhuang.service.order.impl;

import com.arvato.service.order.api.orm.dao.EsCoreMessageTemplateMapper;
import com.arvato.service.order.api.service.EsCoreMessageTemplateService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.order.EsCoreMessageTemplate;
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
            Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
            List<EsCoreMessageTemplate> list = esCoreMessageTemplateMapper.select(page,entity);
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
