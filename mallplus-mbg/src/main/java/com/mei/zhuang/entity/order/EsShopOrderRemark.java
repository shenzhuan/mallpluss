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
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_remark")
public class EsShopOrderRemark extends Model<EsShopOrderRemark> {

    private static final long serialVersionUID = 1L;

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
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 用户名
     */
    private Long uid;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
