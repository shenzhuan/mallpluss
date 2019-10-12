package com.zscat.mallplus.sms.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zscat
 * @since 2019-10-12
 */

@Data
@TableName("sms_group_activity")
public class SmsGroupActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动价格
     */
    private BigDecimal price;

    /**
     * 运费
     */
    private BigDecimal fee;

    /**
     * 活动状态 1 开启 2 关闭
     */
    private String status;

    /**
     * 1 买家承担 2 卖家承担
     */
    private String feestatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;



    @TableField("goods_ids")
    private String goodsIds;


}
