package com.mei.zhuang.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsShopOrderPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-15
 */
public interface EsShopOrderPackageMapper extends BaseMapper<EsShopOrderPackage> {


    /**
     * 查询包裹明细
     *
     * @param orderId
     * @return
     */
    List<EsShopOrderPackage> selectPackItemByOrderId(@Param("orderId") Long orderId);

}
