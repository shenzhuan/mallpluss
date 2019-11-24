package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_refund")
public class EsShopOrderRefund extends Model<EsShopOrderRefund> {

    private static final long serialVersionUID = 1L;

    /**
     * 适用价格
     */
    @TableField("apply_price")
    private BigDecimal applyPrice;
    /**
     * 关闭评论
     */
    @TableField("close_remark")
    private String closeRemark;
    /**
     * 退款内容
     */
    private String content;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 退款图片
     */
    private String images;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 退款地址id
     */
    @TableField("refund_address_id")
    private Long refundAddressId;
    /**
     * 退款地址类型
     */
    @TableField("refund_type")
    private Integer refundType;
    /**
     * 卖家消息
     */
    @TableField("saler_message")
    private String salerMessage;
    /**
     * 步骤
     */
    private Long step;
    /**
     * 拒绝理由
     */
    @TableField("deny_reason")
    private String denyReason;
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
     * 退款地址名称
     */
    @TableField("refund_address_name")
    private String refundAddressName;
    /**
     * 状态
     */
    private Integer status;
    private Long uid;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 卖家快递编号
     */
    @TableField("saler_express_sn")
    private String salerExpressSn;
    /**
     * 卖家备注
     */
    @TableField("saler_remark")
    private String salerRemark;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 步骤2时间
     */
    @TableField("step2_time")
    private Date step2Time;
    /**
     * 卖家步骤3时间
     */
    @TableField("step3_saler_time")
    private Date step3SalerTime;
    /**
     * 退款类型
     */
    private Long type;
    /**
     * 实际价格
     */
    @TableField("real_price")
    private BigDecimal realPrice;
    /**
     * 退货原因
     */
    private String reason;
    /**
     * 退款状态
     */
    @TableField("refund_status")
    private Long refundStatus;
    /**
     * 原物价格
     */
    @TableField("original_price")
    private BigDecimal originalPrice;
    /**
     * 步骤1时间
     */
    @TableField("step1_time")
    private Date step1Time;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 退款地址信息
     */
    @TableField("refund_address_info")
    private String refundAddressInfo;
    /**
     * 卖家快递编码
     */
    @TableField("saler_express_code")
    private String salerExpressCode;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 快递，快车名称
     */
    @TableField("express_name")
    private String expressName;
    /**
     * 退款地址电话
     */
    @TableField("refund_address_mobile")
    private String refundAddressMobile;
    /**
     * 退款接受账户
     */
    @TableField("refund_recv_accout")
    private String refundRecvAccout;
    /**
     * 快递，快车编码
     */
    @TableField("express_code")
    private String expressCode;
    /**
     * 快递编号
     */
    @TableField("express_sn")
    private String expressSn;
    /**
     * 完成时间
     */
    @TableField("finish_time")
    private Date finishTime;
    /**
     * 退款编号
     */
    @TableField("refund_no")
    private String refundNo;
    /**
     * 退款通知数据
     */
    @TableField("refund_notify_data")
    private String refundNotifyData;
    /**
     * 退款成功时间
     */
    @TableField("refund_success_time")
    private Date refundSuccessTime;
    /**
     * 卖家快递名称
     */
    @TableField("saler_express_name")
    private String salerExpressName;
    /**
     * 步骤3时间
     */
    @TableField("step3_time")
    private Date step3Time;
    /**
     * 步骤4时间
     */
    @TableField("step4_time")
    private Date step4Time;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
