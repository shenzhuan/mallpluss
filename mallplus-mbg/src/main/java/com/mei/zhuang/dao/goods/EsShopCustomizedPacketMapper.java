package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsShopCustomizedPacketMapper extends BaseMapper<EsShopCustomizedPacket> {

    //  List<EsShopCustomizedPacket> selPageList(Page page, @Param("type") Integer type);

    Integer count(@Param("type") Integer type);

    List<EsShopCustomizedPacket> list(Integer type);
}
