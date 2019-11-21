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
 * @author arvato team
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_batch_send")
public class EsShopOrderBatchSend extends Model<EsShopOrderBatchSend> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 用户uid
     */
    private Long uid;
    /**
     * 计数
     */
    private Long count;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    private Long id;
    /**
     * 真实的计数
     */
    @TableField("real_count")
    private Long realCount;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
