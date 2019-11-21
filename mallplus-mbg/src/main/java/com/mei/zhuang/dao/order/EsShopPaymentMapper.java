package com.mei.zhuang.dao.order;

import com.arvato.ec.common.vo.order.PaySettingParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsShopPayment;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-22
 */
public interface EsShopPaymentMapper extends BaseMapper<EsShopPayment> {


    List<EsShopPayment> selectPageList(PaySettingParam paySettingParam);
}
