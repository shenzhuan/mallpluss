package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.order.EsShopOrderSettingsMapper;
import com.mei.zhuang.entity.order.EsShopOrderSettings;
import com.mei.zhuang.service.order.ShopOrderSettingsService;
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
