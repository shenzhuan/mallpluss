package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsCoreAttachmentMapper;
import com.arvato.service.goods.api.service.EsCoreAttachmentService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsCoreAttachment;
import com.mei.zhuang.entity.goods.EsShopGoods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsCoreAttachmentServiceImpl  extends ServiceImpl<EsCoreAttachmentMapper, EsCoreAttachment> implements EsCoreAttachmentService {

    @Resource
    private EsCoreAttachmentMapper esCoreAttachmentMapper;


    @Override
    public Map<String, Object> selPageList(EsCoreAttachment entity) {
        Map<String,Object> map = new HashMap<String,Object>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        List<EsCoreAttachment> list = esCoreAttachmentMapper.selPageList(page,entity);
        Integer count=esCoreAttachmentMapper.count(entity);
        map.put("rows", list);
        map.put("total", count);
        map.put("current", entity.getCurrent());
        map.put("size", entity.getSize());
        return map;
    }
}
