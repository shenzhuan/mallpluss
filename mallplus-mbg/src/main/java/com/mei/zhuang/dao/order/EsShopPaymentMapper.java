package com.mei.zhuang.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsShopPayment;
import com.mei.zhuang.vo.order.PaySettingParam;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-22
 */
public interface EsShopPaymentMapper extends BaseMapper<EsShopPayment> {


    List<EsShopPayment> selectPageList(PaySettingParam paySettingParam);
}
