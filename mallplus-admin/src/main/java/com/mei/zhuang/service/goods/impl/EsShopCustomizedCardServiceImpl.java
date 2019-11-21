package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopCustomizedCardMapper;
import com.arvato.service.goods.api.service.EsShopCustomizedCardService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopCustomizedCard;
import com.mei.zhuang.entity.goods.EsShopGoods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsShopCustomizedCardServiceImpl extends ServiceImpl<EsShopCustomizedCardMapper, EsShopCustomizedCard> implements EsShopCustomizedCardService {

   @Resource
   private EsShopCustomizedCardMapper esShopCustomizedCardMapper;

    @Override
    public Map<String, Object> selCardPage(EsShopCustomizedCard entity) {
        Map<String,Object> result=new HashMap<String,Object>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        List<EsShopCustomizedCard> list=esShopCustomizedCardMapper.selCardPage(page,entity);
        Integer num=esShopCustomizedCardMapper.count();
        result.put("rows", list);
        result.put("total", num);
        result.put("current", entity.getCurrent());
        result.put("size", entity.getSize());
        return result;
    }
}
