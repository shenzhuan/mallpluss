package com.mei.zhuang.service.order;

import com.arvato.ec.common.vo.order.EsShopCustAppletParam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsShopCustomizedApplet;

public interface EsShopCustomizedAppletService extends IService<EsShopCustomizedApplet> {

    Object saveCust(EsShopCustAppletParam entity);

    Object detailCustService(EsShopCustomizedApplet entity);
}
