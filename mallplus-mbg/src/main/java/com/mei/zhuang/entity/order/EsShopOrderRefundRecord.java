package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_refund_record")
public class EsShopOrderRefundRecord extends Model<EsShopOrderRefundRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 退款id
     */
    @TableField("refund_id")
    private Long refundId;
    /**
     * 角色，任务
     */
    private Long role;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 详细
     */
    private String detail;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 会员id
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 订单商品id
     */
    @TableField("order_goods_id")
    private Long orderGoodsId;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    private Long id;
    /**
     * 图片
     */
    private String images;
    /**
     * 步骤名称
     */
    @TableField("step_name")
    private String stepName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
