package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("es_member_behavior_count")
public class EsMemberBehaviorCount extends Model<EsMemberBehaviorCount> {

    private static final long serialVersionUID = 1L;

    @TableField("goods_id")
    private Integer goodsId;
    @TableField("option_id")
    private Integer optionId;
    @TableField("plugin_name")
    private String pluginName;
    @TableField("shop_id")
    private Integer shopId;
    private String total;
    @TableField("activity_id")
    private Integer activityId;
     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    private String key;
    @TableField("member_id")
    private Integer memberId;
    private String value;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
