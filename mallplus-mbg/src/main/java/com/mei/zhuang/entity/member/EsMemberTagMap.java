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
 * @author arvato team
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_tag_map")
public class EsMemberTagMap extends Model<EsMemberTagMap> {

    private static final long serialVersionUID = 1L;

    @TableField("member_id")
    private Integer memberId;
    @TableField("shop_id")
    private Integer shopId;
    @TableField("tag_id")
    private Integer tagId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
