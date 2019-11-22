package com.mei.zhuang.service.order;


import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(value = "/api/sku/detail", params = "optionId")
    EsShopGoodsOption getSkuById(@RequestParam("optionId") Long optionId);

    @PostMapping(value = "/api/sku/goods/detail", params = "goodsId")
    EsShopGoods getGoodsById(@RequestParam("goodsId") Long goodsId);

    @PostMapping(value = "/api/sku/updateSkuById")
    void updateSkuById(@RequestBody EsShopGoodsOption option);

    @PostMapping(value = "/api/sku/updateGoodsById")
    void updateGoodsById(@RequestBody EsShopGoods goods);

    @PostMapping(value = "/api/sku/decrSkuStock")
    int decrSkuStock(@RequestParam("optionId") Long optionId, @RequestParam("total") Integer total, @RequestParam("goodsId") Long goodsId, @RequestParam("type") Integer type);

    @PostMapping(value = "/api/sku/decrGoodsStock")
    int decrGoodsStock(@RequestParam("goodsId") Long goodsId, @RequestParam("total") Integer total);

    @PostMapping(value = "/api/sku/addSkuStock")
    void addSkuStock(@RequestParam("optionId") Long optionId, @RequestParam("total") Integer total, @RequestParam("goodsId") Long goodsId);

    @PostMapping(value = "/api/sku/addGoodsStock")
    void addGoodsStock(@RequestParam("goodsId") Long goodsId, @RequestParam("total") Integer total);

    //样图详情
    @PostMapping(value = "/shop/basic/detailLegend")
    Object detailLegend(@RequestParam("id") Long id);

    //刻字服务基础信息详情
    @PostMapping(value = "/shop/basic/detailBasic")
    Object detailBasics(EsShopCustomizedBasic entity);

    //定制卡片详情
    @PostMapping(value = "/shop/card/detail")
    Object detail(@RequestParam("id") Long id);

    //套装详情
    @PostMapping(value = "/shop/package/detail")
    Object details(@RequestParam("id") Long id);

    /*//根据购物车id 获得定制服务信息
    @PostMapping(value = "/api/goods/custMarking/selMakingDetail")
    Map<String, OrderGoodsCustMakingInfo> getCustMakedInfo(@RequestParam("cartIds") List<Long> cartIds);
*/

    //根据商品id查询商品定制服务
    @PostMapping(value = "/applet/selectCust")
    Object selectCust(@RequestParam("id") Long id);

    //根据套装ids 获得封套/包装盒信息List
    @PostMapping(value = "/api/goods/custMarking/getPackInfo")
    List<EsShopCustomizedPacket> getPackInfo(@RequestParam("套装ids") List<Long> packIds);

    @PostMapping(value = "/api/goods/custMarking/getPackInfoByOne")
    EsShopCustomizedPacket getPackInfoByOne(@RequestParam("套装id") Long packId);

    @PostMapping(value = "/shop/basic/detailBasics")
    EsShopCustomizedBasic detailBasics(@RequestParam("id") Long id);

    @PostMapping("/api/goods/custMarking/getCustBasicById")
    EsShopCustomizedBasic getCustBasicById(@RequestParam("定字基础id") Long basicId);

    @PostMapping("/api/goods/custMarking/getCustBasicByIds")
    List<EsShopCustomizedBasic> getCustBasicByIds(@RequestParam("定字基础ids") List<Long> basicIds);

    //新增小美盒定制礼盒
    @PostMapping("/api/goods/insSmallBeautyBoxCust")
    public boolean insSmallBeautyBoxCust(@RequestParam("boxName") String boxName,
                                         @RequestParam("boxCode") String boxCode,
                                         @RequestParam("boxImg") String boxImg,
                                         @RequestParam("vituralStock") Integer vituralStock,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("id") Long id,
                                         @RequestParam("productSn") String productSn);

    //编辑小美盒定制礼盒
    @PostMapping("/api/goods/updSmallBeautyBoxCust")
    boolean updSmallBeautyBoxCust(@RequestParam("boxName") String boxName,
                                  @RequestParam("boxCode") String boxCode,
                                  @RequestParam("boxImg") String boxImg,
                                  @RequestParam("vituralStock") Integer vituralStock,
                                  @RequestParam("price") BigDecimal price,
                                  @RequestParam("id") Long id,
                                  @RequestParam("productSn") String productSn);

    //删除小美盒定制礼盒
    @PostMapping("/api/goods/delSmallBeautyBoxCust")
    boolean delSmallBeautyBoxCust(@RequestParam("id") Long id);

    //查询商品中的定制商品
    @PostMapping("/api/goods/goodsDetail")
    EsShopGoods goodsDetail(@RequestParam("id") Long id);

    @PostMapping("/api/goods/goodsOption")
    EsShopGoodsOption goodsOption(@RequestParam("goodsId") Long goodsId);

    @PostMapping("/api/goods/selShopGoodsList")
    List<EsShopGoods> selShopGoodsList(@RequestBody TradeAnalyzeParam param);
}
