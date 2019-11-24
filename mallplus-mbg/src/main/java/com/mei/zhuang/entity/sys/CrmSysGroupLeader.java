package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2017-10-16
 */
@Data
@Accessors(chain = true)
@TableName("crm_sys_group_leader")
public class CrmSysGroupLeader extends Model<CrmSysGroupLeader> {

    private static final long serialVersionUID = 1L;


     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    @TableField("group_id")
    private String groupId;
    @TableField("user_id")
    private String userId;
    private String description;
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Integer updateUserId;
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 乐观锁用
     */
    @Version
    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
