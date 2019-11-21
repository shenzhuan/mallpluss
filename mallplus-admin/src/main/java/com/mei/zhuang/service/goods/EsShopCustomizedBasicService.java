package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;

/**
 * 刻字服务：基础服务
 */
public interface EsShopCustomizedBasicService extends IService<EsShopCustomizedBasic> {

    EsShopCustomizedBasic detail(Long id);
}
