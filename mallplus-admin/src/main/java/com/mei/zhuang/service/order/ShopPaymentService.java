package com.mei.zhuang.service.order;

import com.arvato.ec.common.vo.order.PayParam;
import com.arvato.ec.common.vo.order.PaySettingParam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.plugins.Page;
import com.mei.zhuang.entity.order.EsShopPayment;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 10:42
 * @Description:
 */
public interface ShopPaymentService extends IService<EsShopPayment> {

    boolean deleteById(Long id);

    boolean savePayment(EsShopPayment entity);

    boolean updateStatus(PayParam param);

    Page<EsShopPayment> selectPageList(PaySettingParam paySettingParam);
}
