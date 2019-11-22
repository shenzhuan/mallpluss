package com.mei.zhuang.vo.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mei.zhuang.entity.goods.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-16
 */
@Data
public class EsShopGoodsParam extends EsShopGoods {

    //规格sku
    private List<EsShopGoodsOption> goodsOptionList;
    //商品分类(编辑的时候需要)
    private String oldCateIds;
    //商品分类
    private String cateIds;

    //商品推荐商品
    private List<EsShopGoodsRecom> goodsRecomsList;
    // 商品规格
    private List<EsShopGoodsSpec> goodsSpecList;
    @TableField(exist = false)
    private List<EsShopGoodsSpecItem> itemList;


    private String specList;
    private String specItem;
    private String optionList;
    private String specItemId;//规格值iD


    private Date putawayTimes;


}
