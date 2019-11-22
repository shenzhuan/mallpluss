package com.mei.zhuang.service.order.impl;

import com.mei.zhuang.dao.order.EsDeliveryAddresserMapper;
import com.mei.zhuang.service.order.ShopDeliveryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.entity.order.EsDeliveryAddresser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: Tiger
 * @Date: 2019-05-06 11:37
 * @Description:
 */
@Service
public class ShopDeliveryServiceImpl extends ServiceImpl<EsDeliveryAddresserMapper, EsDeliveryAddresser> implements ShopDeliveryService {

    @Resource
    EsDeliveryAddresserMapper deliveryAddresserMapper;
}
