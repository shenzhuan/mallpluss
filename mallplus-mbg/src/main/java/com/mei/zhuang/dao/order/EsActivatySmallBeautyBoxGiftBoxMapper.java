package com.mei.zhuang.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGiftBox;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsActivatySmallBeautyBoxGiftBoxMapper extends BaseMapper<EsActivatySmallBeautyBoxGiftBox> {

    List<EsActivatySmallBeautyBoxGiftBox> selectGiftBox(List<String> list, @Param("id") Long id);

    List<EsActivatySmallBeautyBoxGiftBox> select(EsActivatySmallBeautyBoxGiftBox entity);

}
