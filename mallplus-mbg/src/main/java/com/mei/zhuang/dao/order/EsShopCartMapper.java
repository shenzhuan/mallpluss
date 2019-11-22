package com.mei.zhuang.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-23
 */
public interface EsShopCartMapper extends BaseMapper<EsShopCart> {

    Integer cartGoodsCount(@Param("memberId") Long memberId);

    List<Long> shopNumber(TradeAnalyzeParam param);
}
