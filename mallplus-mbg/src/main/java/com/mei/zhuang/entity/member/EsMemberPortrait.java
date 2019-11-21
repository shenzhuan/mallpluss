package com.mei.zhuang.entity.member;

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
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_portrait")
public class EsMemberPortrait extends Model<EsMemberPortrait> {

    private static final long serialVersionUID = 1L;

    @TableField("shop_id")
    private Integer shopId;
    private Integer status;
    @TableField("update_time")
    private Date updateTime;
    private String conditions;
    private Integer id;
    private String name;
    private Integer number;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
