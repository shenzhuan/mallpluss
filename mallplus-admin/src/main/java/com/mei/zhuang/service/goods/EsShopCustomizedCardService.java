package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopCustomizedCard;

import java.util.Map;

/**
 * 刻字服务：定制卡片
 */
public interface EsShopCustomizedCardService extends IService<EsShopCustomizedCard> {

    Map<String,Object> selCardPage(EsShopCustomizedCard entity);
}
