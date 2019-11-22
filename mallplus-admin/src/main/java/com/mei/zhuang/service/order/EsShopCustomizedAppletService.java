package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsShopCustomizedApplet;
import com.mei.zhuang.vo.order.EsShopCustAppletParam;

public interface EsShopCustomizedAppletService extends IService<EsShopCustomizedApplet> {

    Object saveCust(EsShopCustAppletParam entity);

    Object detailCustService(EsShopCustomizedApplet entity);
}
