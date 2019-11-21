package com.mei.zhuang.service.order.impl;

import com.arvato.service.order.api.orm.dao.EsShopOrderPackageMapper;
import com.arvato.service.order.api.service.ShopOrderPackageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.order.EsShopOrderPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-09 15:40
 * @Description:
 */
@Service
public class ShopOrderPackageServiceImpl extends ServiceImpl<EsShopOrderPackageMapper, EsShopOrderPackage> implements ShopOrderPackageService {

    @Resource
    private EsShopOrderPackageMapper shopOrderPackageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<EsShopOrderPackage> selectPackItemByOrderId(Long orderId) {
        return shopOrderPackageMapper.selectPackItemByOrderId(orderId);
    }
}
