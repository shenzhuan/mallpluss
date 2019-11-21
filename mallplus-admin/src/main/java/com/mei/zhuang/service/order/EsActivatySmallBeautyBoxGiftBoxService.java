package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGiftBox;

import java.util.List;

public interface EsActivatySmallBeautyBoxGiftBoxService extends IService<EsActivatySmallBeautyBoxGiftBox> {

    List<EsActivatySmallBeautyBoxGiftBox> selectSmall(List<String> list, Long id);

    List<EsActivatySmallBeautyBoxGiftBox> select(EsActivatySmallBeautyBoxGiftBox entity);
}
