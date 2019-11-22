package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsShopPayment;
import com.mei.zhuang.vo.order.PayParam;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 10:42
 * @Description:
 */
public interface ShopPaymentService extends IService<EsShopPayment> {

    boolean deleteById(Long id);

    boolean savePayment(EsShopPayment entity);

    boolean updateStatus(PayParam param);

}
