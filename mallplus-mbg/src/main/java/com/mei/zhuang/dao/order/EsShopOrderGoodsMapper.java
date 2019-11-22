package com.mei.zhuang.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.vo.data.goods.GoodsAnalyzeParam;
import com.mei.zhuang.vo.order.OrderParam;
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
public interface EsShopOrderGoodsMapper extends BaseMapper<EsShopOrderGoods> {

    Integer
    selectGoodsByIds(List<Long> ids);

    List<Long> selOgByCondition(OrderParam param);

    List<EsShopOrderGoods> OrderGoods(GoodsAnalyzeParam param);

    //复购人数
    List<EsShopOrderGoods> purchaseGoods(GoodsAnalyzeParam param);

    Integer selGoodsTotalSaleCount(@Param("ids") List<Long> ids);

    EsShopOrderGoods orderid(long orderId);
}
