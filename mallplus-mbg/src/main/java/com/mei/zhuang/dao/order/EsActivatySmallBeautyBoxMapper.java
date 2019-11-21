package com.mei.zhuang.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mei.zhuang.entity.order.EsActivatySmallBeautyBox;

import java.util.List;

/**
 * 小美定制活动列表
 */
public interface EsActivatySmallBeautyBoxMapper extends BaseMapper<EsActivatySmallBeautyBox> {

    List<EsActivatySmallBeautyBox> selList(Pagination page, EsActivatySmallBeautyBox entity);

    int count(EsActivatySmallBeautyBox entity);

    List<EsActivatySmallBeautyBox> select(EsActivatySmallBeautyBox entity);

}
