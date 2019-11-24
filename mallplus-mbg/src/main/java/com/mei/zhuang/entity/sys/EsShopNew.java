package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.entity.goods.EsShopBrand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_new")
public class EsShopNew extends Model<EsShopNew> {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String typeEn;//类型汉译

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 店铺名称
     */

    private String name;
    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Integer brandId;
    /**
     * 店铺类型 1：小程序 2:其他
     */
    @TableField("type_id")
    private Integer typeId;
    /**
     * APPID
     */
    @TableField("app_id")
    private String appId;
    /**
     * APP秘钥
     */
    @TableField("app_secret")
    private String appSecret;
    /**
     * 原始id
     */
    @TableField("original_id")
    private String originalId;
    /**
     * 小程序图片
     */
    @TableField("applet_thumb")
    private String appletThumb;
    /**
     * 是否启用：1:启用 2：禁用
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;
    @TableField("create_date")
    private String createDate;
    /**
     * 创建人
     */
    @TableField("create_by_id")
    private Long createById;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private String updateTime;
    @TableField("update_date")
    private String updateDate;
    /**
     * 修改人
     */
    @TableField("update_by_id")
    private Long updateById;
    /**
     * 是否删除： 1：不删除 2：删除
     */
    @TableField("is_delete")
    private Integer isDelete;
    /**
     * 店铺描述
     */
    @TableField("description")
    private String description;
    /**
     * 品牌名称（方便页面查询）
     */
    @TableField("brand_name")
    private String brandName;
    /**
     * 购物门槛  0:不显示  1：限制
     */
    @TableField("shopping_threshold")
    private Integer shoppingThreshold;
    /**
     * 是否显示购物车  0：不显示  1：显示
     */
    @TableField("is_show_shop_cart")
    private Integer isShowshopCart;
    /**
     * 是否显示售罄商品  0：不显示  1：显示
     */
    @TableField("is_show_goods_sold_out")
    private Integer isShowGoodsSoldOut;
    /**
     * 是否显示评价  0：不显示  1：显示
     */
    @TableField("is_show_goods_evaluate")
    private Integer isShowGoodsEvaluate;

    @TableField(exist = false)
    private EsShopBrand shopBrand;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
