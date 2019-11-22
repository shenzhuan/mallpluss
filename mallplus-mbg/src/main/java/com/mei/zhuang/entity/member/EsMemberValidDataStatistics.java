package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_valid_data_statistics")
public class EsMemberValidDataStatistics extends Model<EsMemberValidDataStatistics> {

    private static final long serialVersionUID = 1L;

    @TableField("member_id")
    private Integer memberId;
    @TableField("order_money")
    private String orderMoney;
    @TableField("order_num")
    private Integer orderNum;
    @TableField("shop_id")
    private Integer shopId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
