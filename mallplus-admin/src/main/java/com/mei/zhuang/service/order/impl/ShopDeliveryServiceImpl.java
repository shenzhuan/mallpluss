package com.mei.zhuang.service.order.impl;

import com.arvato.service.order.api.orm.dao.EsDeliveryAddresserMapper;
import com.arvato.service.order.api.service.ShopDeliveryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
