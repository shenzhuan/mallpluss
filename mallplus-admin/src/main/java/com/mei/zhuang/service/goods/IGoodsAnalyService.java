package com.mei.zhuang.service.goods;


import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.vo.data.goods.*;

import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-08-01 13:52
 * @Description:商品分析
 */
public interface IGoodsAnalyService {

    /**
     * 商品分析头部分析统计
     *
     * @param param
     * @return
     */
    Object goodsStatic(GoodsAnalyzeParam param) throws Exception;

    /**
     * 商品趋势图
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<GoodsTrendMapInfoVo> goodsTrendMapStatic(GoodsTrendMapParam param) throws Exception;

    /**
     * 商品销量排行榜
     *
     * @param param
     * @return
     */
    List<GoodsRankTopSaleVo> goodsRankTopBySaleCount(GoodsRankTopParam param) throws Exception;

    /**
     * 商品支付金额排行榜
     *
     * @param param
     * @return
     */
    List<GoodsRankTopPayPriceVo> goodsRankTopByPrice(GoodsRankTopParam param);

    List<GoodsRankTopDetailVo> goodsRankTopDetail(GoodsRankTopParam param);

    List<EsShopOrderGoods> goodsRankTopRanking(GoodsAnalyzeParam param);
}
