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
@TableName("es_member_group_map")
public class EsMemberGroupMap extends Model<EsMemberGroupMap> {

    private static final long serialVersionUID = 1L;

    @TableField("group_id")
    private Integer groupId;
    @TableField("member_id")
    private Integer memberId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
