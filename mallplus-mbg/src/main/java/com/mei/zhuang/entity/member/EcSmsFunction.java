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
 *
 * </p>
 *
 * @author arvato team
 * @since 2019-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ec_sms_function")
public class EcSmsFunction extends Model<EcSmsFunction> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 功能名字
     */
    @TableField("function_name")
    private String functionName;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 修改时间
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 是否删除 0:否 1：是
     */
    @TableField("is_delete")
    private String isDelete;
    /**
     * 功能描述
     */
    private String description;
    /**
     * 短信类型id
     */
    @TableField("sms_type_id")
    private Long smsTypeId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
