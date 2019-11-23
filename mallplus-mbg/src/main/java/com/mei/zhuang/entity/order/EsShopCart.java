package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_cart")
public class EsShopCart extends Model<EsShopCart> {

    private static final long serialVersionUID = 1L;
    /*  //商品定制服务
      @TableField(exist = false)
      private EsShopCustomizedBasic  esShopCustomizedBasic;*/
    @TableField(exist = false)
    //商品定制服务
            EsShopCustomizedApplet esShopCustApplet;
    @TableField("create_time")
    private Date createTime;
    @TableField("goods_id")
    private Long goodsId;
    @TableField("category_id")
    private String categoryId;
    @TableField("is_lose_efficacy")
    private Integer isLoseEfficacy;
    private Integer total;
    private Long id;
    @TableField("is_selected")
    private Integer isSelected;
    @TableField("member_id")
    private Long memberId;
    @TableField("option_id")
    private Long optionId;
    private BigDecimal price;
    @TableField("shop_id")
    private Long shopId=1l;
    @TableField("goods_name")
    private String goodsName;
    @TableField("cust_id")
    private Long custId;//定制服务编号
    @TableField("activaty_type")
    private Integer activatyType;//活动类型
    private String pic;
    @TableField("option_name")
    private String optionName;
    private Integer type;//规格选项的类型（1-文字、2-颜色、3-图片）
    private String typeword;//规格选项的内容（和type一一对应）

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
