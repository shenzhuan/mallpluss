package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopCardMessage;

public interface EsShopCardMessageServer extends IService<EsShopCardMessage> {

    Object save(EsShopCardMessage entity);

    Object updates(EsShopCardMessage entity);

    EsShopCardMessage selPageList();
}
