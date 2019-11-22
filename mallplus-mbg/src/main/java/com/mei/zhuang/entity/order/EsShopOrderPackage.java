package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@TableName("es_shop_order_package")
public class EsShopOrderPackage extends Model<EsShopOrderPackage> {

    private static final long serialVersionUID = 1L;

    /**
     * 是否快车
     */
    @TableField("no_express")
    private Integer noExpress;
    /**
     * 订单商品标识
     */
    @TableField("order_goods_ids")
    private String orderGoodsIds;
    /**
     * 发货时间
     */
    @TableField("send_time")
    private Date sendTime;
    /**
     * 城市分配类型
     */
    @TableField("city_distribution_type")
    private Integer cityDistributionType;
    /**
     * 快车id
     */
    @TableField("express_id")
    private Long expressId;
    /**
     * 完成时间
     */
    @TableField("finish_time")
    private Date finishTime;
    private Long id;
    /**
     * 城市分布
     */
    @TableField("is_city_distribution")
    private Integer isCityDistribution;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 快递，快车编号
     */
    @TableField("express_sn")
    private String expressSn;
    /**
     * 会员号
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 订单商品信息
     */
    @TableField(exist = false)
    private List<EsShopOrderGoods> orderGoodsList;
    /**
     * 包裹快递信息
     */
    @TableField(exist = false)
    private EsShopOrderBatchSendDetail batchSendDetail;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
