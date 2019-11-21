package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_code_gift_goods_map")
public class EsShopCodeGiftGoodsMap extends Model<EsShopCodeGiftGoodsMap> {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码赠礼id
     */
    @TableField("code_gift_id")
    private Long codeGiftId;
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 商品类型 1商品  ，2 赠品
     */
    @TableField("goods_type")
    private Integer goodsType;
    /**
     * 规格
     */
    @TableField("spec_ids")
    private String specIds;
    //赠品名称
    @TableField("goods_name")
    private String goodsName;
    /**
     * 活动名称
     */
    @TableField("activity_name")
    private String activityName;

    private String pic;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
