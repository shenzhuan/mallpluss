package com.mei.zhuang.service.goods.impl;

import com.mei.zhuang.dao.goods.EsShopCustomizedPacketMapper;
import com.mei.zhuang.service.goods.EsShopCustomizedPacketServer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
