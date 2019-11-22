package com.mei.zhuang.service.order;


import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:商品服务接口
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2018/11/26
 */

public interface GoodsFegin {


    EsShopGoodsOption getSkuById(@RequestParam("optionId") Long optionId);


    EsShopGoods getGoodsById(@RequestParam("goodsId") Long goodsId);


    void updateSkuById(@RequestBody EsShopGoodsOption option);


    void updateGoodsById(@RequestBody EsShopGoods goods);


    int decrSkuStock(@RequestParam("optionId") Long optionId, @RequestParam("total") Integer total, @RequestParam("goodsId") Long goodsId, @RequestParam("type") Integer type);


    int decrGoodsStock(@RequestParam("goodsId") Long goodsId, @RequestParam("total") Integer total);


    void addSkuStock(@RequestParam("optionId") Long optionId, @RequestParam("total") Integer total, @RequestParam("goodsId") Long goodsId);


    void addGoodsStock(@RequestParam("goodsId") Long goodsId, @RequestParam("total") Integer total);

    //样图详情

    Object detailLegend(@RequestParam("id") Long id);

    //刻字服务基础信息详情

    Object detailBasics(EsShopCustomizedBasic entity);

    //定制卡片详情

    Object detail(@RequestParam("id") Long id);

    //套装详情

    Object details(@RequestParam("id") Long id);

    /*//根据购物车id 获得定制服务信息
    @PostMapping(value = "/api/goods/custMarking/selMakingDetail")
    Map<String, OrderGoodsCustMakingInfo> getCustMakedInfo(@RequestParam("cartIds") List<Long> cartIds);
*/

    //根据商品id查询商品定制服务

    Object selectCust(@RequestParam("id") Long id);

    //根据套装ids 获得封套/包装盒信息List

    List<EsShopCustomizedPacket> getPackInfo(@RequestParam("套装ids") List<Long> packIds);


    EsShopCustomizedPacket getPackInfoByOne(@RequestParam("套装id") Long packId);


    EsShopCustomizedBasic detailBasics(@RequestParam("id") Long id);


    EsShopCustomizedBasic getCustBasicById(@RequestParam("定字基础id") Long basicId);


    List<EsShopCustomizedBasic> getCustBasicByIds(@RequestParam("定字基础ids") List<Long> basicIds);

    //新增小美盒定制礼盒

    public boolean insSmallBeautyBoxCust(@RequestParam("boxName") String boxName,
                                         @RequestParam("boxCode") String boxCode,
                                         @RequestParam("boxImg") String boxImg,
                                         @RequestParam("vituralStock") Integer vituralStock,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("id") Long id,
                                         @RequestParam("productSn") String productSn);

    //编辑小美盒定制礼盒

    boolean updSmallBeautyBoxCust(@RequestParam("boxName") String boxName,
                                  @RequestParam("boxCode") String boxCode,
                                  @RequestParam("boxImg") String boxImg,
                                  @RequestParam("vituralStock") Integer vituralStock,
                                  @RequestParam("price") BigDecimal price,
                                  @RequestParam("id") Long id,
                                  @RequestParam("productSn") String productSn);

    //删除小美盒定制礼盒

    boolean delSmallBeautyBoxCust(@RequestParam("id") Long id);

    //查询商品中的定制商品

    EsShopGoods goodsDetail(@RequestParam("id") Long id);


    EsShopGoodsOption goodsOption(@RequestParam("goodsId") Long goodsId);


    List<EsShopGoods> selShopGoodsList(@RequestBody TradeAnalyzeParam param);
}
