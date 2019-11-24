package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_batch_send_detail")
public class EsShopOrderBatchSendDetail extends Model<EsShopOrderBatchSendDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 快车,快递名称
     */
    @TableField("express_name")
    private String expressName;
    /**
     * 快车，快递编号
     */
    @TableField("express_sn")
    private String expressSn;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 订单数字
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 批次id
     */
    @TableField("batch_id")
    private Long batchId;
    /**
     * 时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 发货类型 1：按订单发货 2：商品分包裹发货
     */
    @TableField("deliver_type")
    private Integer deliverType;
    /**
     * 发货的商品id 多个
     */
    @TableField("goods_ids")
    private String goodsIds;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
