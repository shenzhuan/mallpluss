package com.mei.zhuang.service.goods;

import com.arvato.ec.common.vo.data.trade.TradeAnalyzeParam;
import com.arvato.ec.common.vo.goods.EsShopGoodsParam;
import com.arvato.ec.common.vo.goods.GoodsDetail;
import com.arvato.ec.common.vo.goods.GoodsQuery;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopDiypage;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsSpec;
import com.mei.zhuang.entity.goods.EsShopGoodsSpecItem;

import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */

public interface EsShopGoodsService extends IService<EsShopGoods> {


    public Object getGoodsByPage(EsShopGoods entity);


    public Object saveGoods(EsShopGoodsParam entity);


    public Object updateGoods(EsShopGoodsParam entity);


    public Object deleteGoods(Long id);

    List<EsShopGoods> selShopGoodsList(TradeAnalyzeParam param);

    public Object getGoodsById(Long id);


    public Object updEsShopGoodsStatus(String id, Integer status);


    public Object UpdShopDelID(String id);


    public Object delRecycleShop(String id);


    public Object updRecycleShopStatus(String id, Integer status);

    Object selDisplayShopGoodsCategoryOne() ;
    public Object selShopGoodsCategoryOne();

    public Object selShopGoodsCategoryTwo(Long parentId, Integer level);

    public Object selShopGoodsGroup();

    /**
     * 查询根据名称赠品商品
     */
    List<EsShopGoods> selectgiftsgoods(String title);

     Map<String, Object> selGoodsPageList(GoodsQuery esShopGoods);

    GoodsDetail goodsDetail(Long id);

    EsShopGoods selShopGoodsDetail(Long id);

    Integer selCountShopTitle(String title);

    Integer updGoodsDisplayOrder(Long id, Integer num);

    Object getGoodsSku(Long id, String specsParam);


    List<EsShopGoods> selectgoodsListByCateIds(List<String> ids);

     Map<String, Object> goodsListByCatePageList(GoodsQuery esShopGoods);

     List<EsShopGoods> selGoodsPutAwayTime();

    List<EsShopDiypage> selectAll();

    Map<String,Object> selGoodsPutaway(GoodsQuery goodsQuery);

    Map<String,Object> selCustService();

    EsShopGoods detail(Long id);

    Map<String,Object> lists(GoodsQuery entity);

    Map<String, Object> searchGoods(String keywords);
    //删除规格
    boolean delSpec(EsShopGoodsSpec spec);
    //删除规格值
    boolean delSpecItem(EsShopGoodsSpecItem item);
}
