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
@TableName("es_shop_order_verify")
public class EsShopOrderVerify extends Model<EsShopOrderVerify> {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    private Long id;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 存储，仓库id
     */
    @TableField("store_id")
    private Long storeId;
    /**
     * 审核类型
     */
    private Integer type;
    /**
     * 职员id
     */
    @TableField("clerk_id")
    private Long clerkId;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 验证码
     */
    @TableField("verify_code")
    private String verifyCode;
    /**
     * 会员id
     */
    @TableField("member_id")
    private Long memberId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
