package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGoods;

import java.util.List;
import java.util.Map;

public interface EsActivatySmallBeautyBoxGoodsService extends IService<EsActivatySmallBeautyBoxGoods> {

    Map<String,Object> selPageList(EsActivatySmallBeautyBoxGoods entity);

    Boolean inserts(List<EsActivatySmallBeautyBoxGoods> entity);

    //List<EsActivatySmallBeautyBoxGiftBox> selectSmall(EsActivatySmallBeautyBoxGoods entity);


}
