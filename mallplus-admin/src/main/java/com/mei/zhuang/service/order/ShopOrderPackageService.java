package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsShopOrderPackage;

import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-09 15:36
 * @Description:
 */
public interface ShopOrderPackageService extends IService<EsShopOrderPackage> {

    /**
     * 查询包裹明细
     *
     * @param orderId
     * @return
     */
    List<EsShopOrderPackage> selectPackItemByOrderId(Long orderId);


}
