package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsStartAdvertising;

import java.util.Map;

public interface EsStartAdvertisingService extends IService<EsStartAdvertising> {

    Map<String,Object> select(EsStartAdvertising entity);

    boolean save(EsStartAdvertising entity);

    boolean updateAdvert(EsStartAdvertising entity);

    Integer countStatus(Long id);
}
