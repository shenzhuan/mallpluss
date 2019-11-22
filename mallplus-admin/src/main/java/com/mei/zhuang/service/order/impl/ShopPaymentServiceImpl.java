package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.order.EsShopOperationLogMapper;
import com.mei.zhuang.dao.order.EsShopPayLogMapper;
import com.mei.zhuang.dao.order.EsShopPaymentMapper;
import com.mei.zhuang.entity.order.EsShopOperationLog;
import com.mei.zhuang.entity.order.EsShopPayment;
import com.mei.zhuang.service.order.ShopPaymentService;
import com.mei.zhuang.vo.order.PayParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 10:43
 * @Description:
 */
@Service
public class ShopPaymentServiceImpl extends ServiceImpl<EsShopPaymentMapper, EsShopPayment> implements ShopPaymentService {

    @Resource
    private EsShopPaymentMapper shopPaymentMapper;
    @Resource
    private EsShopPayLogMapper shopPayLogMapper;
    @Resource
    private EsShopOperationLogMapper shopOperationLogMapper;


    @Override
    public boolean deleteById(Long id) {
        EsShopPayment entity = new EsShopPayment();
        entity.setId(id);
        entity.setIsDelete(1);
        shopPaymentMapper.updateById(entity);

        StringBuffer sb = new StringBuffer();
        sb.append("支付id为：" + id + "已被删除");
        EsShopOperationLog log = new EsShopOperationLog();
        log.setOperationDesc(sb.toString());
        log.setCreateTime(new Date());
        return shopOperationLogMapper.insert(log) > 0;
    }

    @Override
    public boolean savePayment(EsShopPayment entity) {
        //判断添加的是否是 子商户 还是 父商户
        if (entity.getType() == 0) {//微信支付（父商户）
            entity.setSubAppid("");
            entity.setSubMchId(0l);
        }
        return shopPaymentMapper.insert(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(PayParam param) {
        EsShopPayment entity = new EsShopPayment();
        entity.setId(param.getPayId());
        entity.setStatus(param.getStatus());
        entity.setRemainStaus(param.getRemainStatus());
        shopPaymentMapper.updateById(entity);

        StringBuffer sb = new StringBuffer();
        sb.append("支付id为：" + param.getPayId() + "支付状态更改为：" + param.getStatus());
        sb.append("余额支付状态更改为：" + param.getRemainStatus());
        EsShopOperationLog log = new EsShopOperationLog();
        log.setOperationDesc(sb.toString());
        log.setCreateTime(new Date());

        return shopOperationLogMapper.insert(log) > 0;
    }


}
