package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopCustomizedLegend;

import java.util.Map;

public interface EsShopCustomizedLegendService  extends IService<EsShopCustomizedLegend> {

    /**
     * 查询样图列表
     * @param entity
     * @return
     */
    Map<String,Object> selLegendPage(EsShopCustomizedLegend entity);
}
