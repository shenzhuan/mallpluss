package com.mei.zhuang.service.goods.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsShopCustomizedPacketMapper;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import com.mei.zhuang.service.goods.EsShopCustomizedPacketServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EsShopCustomizedPacketServerImpl extends ServiceImpl<EsShopCustomizedPacketMapper, EsShopCustomizedPacket> implements EsShopCustomizedPacketServer {

    @Resource
    private EsShopCustomizedPacketMapper esShopCustomizedPacketMapper;

}
