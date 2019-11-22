package com.mei.zhuang.vo.goods;

import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.entity.goods.EsShopGoodsSpec;
import com.mei.zhuang.entity.goods.EsShopGoodsSpecItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/13 16:27
 * @Description:
 */
@Data
public class GoodsDetail implements Serializable {

    private EsShopGoods goods;
    //规格sku
    private List<EsShopGoodsOption> goodsOptionList;
    //商品分类(编辑的时候需要)
    private String oldCateIds;
    //商品分类
    private String cateIds;
    //商品推荐商品
    private List<EsShopGoods> goodsRecomsList;
    // 商品规格
    private List<EsShopGoodsSpec> goodsSpecList;
    // 商品规格值
    private List<EsShopGoodsSpecItem> goodsSpecItemList;
}
