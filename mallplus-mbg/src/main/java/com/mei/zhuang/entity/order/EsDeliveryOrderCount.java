package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_delivery_order_count")
public class EsDeliveryOrderCount extends Model<EsDeliveryOrderCount> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 打印次数
     */
    @TableField("print_count")
    private Integer printCount;
    /**
     * 类型
     */
    private Long type;
    /**
     * 修改时间
     */
    @TableField("updated_time")
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
