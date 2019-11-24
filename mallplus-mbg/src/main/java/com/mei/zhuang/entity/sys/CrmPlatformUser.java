package com.mei.zhuang.entity.sys;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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
 * @since 2017-12-18
 */
@Data
@Accessors(chain = true)
@TableName("crm_platform_user")
public class CrmPlatformUser extends Model<CrmPlatformUser> {

    private static final long serialVersionUID = 1L;

     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    private String username;
    @TableField("user_schema")
    private String userSchema;
    @TableField("is_admin")
    private String isAdmin;
    @TableField("company_id")
    private Integer companyId;
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
    @Version
    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
