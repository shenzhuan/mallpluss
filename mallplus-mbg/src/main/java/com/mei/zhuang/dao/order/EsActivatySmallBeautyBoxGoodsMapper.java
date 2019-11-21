package com.mei.zhuang.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGoods;

import java.util.List;

public interface EsActivatySmallBeautyBoxGoodsMapper extends BaseMapper<EsActivatySmallBeautyBoxGoods> {

    List<EsActivatySmallBeautyBoxGoods> selList(Pagination page, EsActivatySmallBeautyBoxGoods entity);

    int count(EsActivatySmallBeautyBoxGoods entity);

    List<EsActivatySmallBeautyBoxGoods> selectSmall(EsActivatySmallBeautyBoxGoods entity);
}
