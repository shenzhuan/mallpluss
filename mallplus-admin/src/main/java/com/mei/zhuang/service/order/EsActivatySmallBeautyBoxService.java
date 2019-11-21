package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBox;

import java.util.List;
import java.util.Map;

public interface EsActivatySmallBeautyBoxService extends IService<EsActivatySmallBeautyBox> {

    //查询小美盒活动列表
    Map<String,Object> selPageList(EsActivatySmallBeautyBox entity);

    //删除小美盒活动列表
    boolean deleteById(Long id);

    List<EsActivatySmallBeautyBox> select(EsActivatySmallBeautyBox entity);
}
