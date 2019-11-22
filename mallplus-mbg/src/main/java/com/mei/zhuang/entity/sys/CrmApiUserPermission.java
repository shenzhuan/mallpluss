package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2017-12-05
 */
@Data
@Accessors(chain = true)
@TableName("crm_api_user_permission")
@ApiModel("接口权限信息")
public class CrmApiUserPermission extends Model<CrmApiUserPermission> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 用户ID
     */
    @ApiModelProperty("用户id")
    @TableField("api_user_id")
    private Integer apiUserId;

    /**
     * 接口id
     */
    @ApiModelProperty("接口id")
    @TableField("interface_id")
    private Integer interfaceId;

    /**
     * 创建人
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建日期
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    /**
     * 更新人
     */
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Integer updateUserId;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 更新日期
     */
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
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
