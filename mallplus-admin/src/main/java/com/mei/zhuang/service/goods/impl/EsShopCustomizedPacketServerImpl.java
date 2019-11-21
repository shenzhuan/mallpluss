package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopCustomizedPacketMapper;
import com.arvato.service.goods.api.service.EsShopCustomizedPacketServer;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsShopCustomizedPacketServerImpl extends ServiceImpl<EsShopCustomizedPacketMapper, EsShopCustomizedPacket> implements EsShopCustomizedPacketServer {

    @Resource
    private EsShopCustomizedPacketMapper esShopCustomizedPacketMapper;

    @Override
    public Map<String, Object> selPageList(EsShopCustomizedPacket entity) {
        Map<String,Object> map=new HashMap<String,Object>();
        Page<EsShopCustomizedPacket> page = this.selectPage(new Page<EsShopCustomizedPacket>(entity.getCurrent(), entity.getSize()), new QueryWrapper<>(entity));
        List<EsShopCustomizedPacket> list=esShopCustomizedPacketMapper.selPageList(page,entity.getType());
        Integer count=esShopCustomizedPacketMapper.count(entity.getType());
        map.put("rows", list);
        map.put("total", count);
        map.put("current", entity.getCurrent());
        map.put("size", entity.getSize());
        return map;
    }
}
