package com.mei.zhuang.service.order.impl;

import com.arvato.service.order.api.orm.dao.EsShopOrderSettingsMapper;
import com.arvato.service.order.api.service.ShopOrderSettingsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.order.EsShopOrderSettings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 10:43
 * @Description:
 */
@Service
public class ShopOrderSettingsServiceImpl extends ServiceImpl<EsShopOrderSettingsMapper, EsShopOrderSettings> implements ShopOrderSettingsService {

    @Resource
    private EsShopOrderSettingsMapper shopOrderSettingsMapper;




}
