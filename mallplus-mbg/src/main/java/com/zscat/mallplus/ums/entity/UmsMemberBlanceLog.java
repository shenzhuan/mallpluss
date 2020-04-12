package com.zscat.mallplus.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Data
@TableName("ums_member_blance_log")
public class UmsMemberBlanceLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    BigDecimal money;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("member_id")
    private Long memberId;
    private BigDecimal price;
    //0, 1, 2, 3, 4, 5, 7
    // '全部', '消费', '退款', '充值', '提现', '佣金', '平台调整'
    private Integer type;
    private String note;
    @TableField("create_time")
    private Date createTime;
    // 1 申请 2 通过 3 拒绝
    private Integer status;
    // 提现类型 1 银行卡 2 微信 3支付宝
    @TableField("with_draw_type")
    private Integer withDrawType;
    //关联提现类型的编号
    @TableField("obj_id")
    private Long objId;
    private String objname;
}
