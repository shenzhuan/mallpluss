package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2018-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_dept_credit_list")
public class CrmDeptCreditList extends Model<CrmDeptCreditList> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @TableField("dept_id")
    private Integer deptId;
    /**
     * 0白名单
     */
    private String type;
    /**
     * 创建人
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    /**
     * 创建日期
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Integer updateUserId;
    /**
     * 更新日期
     */
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    /**
     * 更新时间
     */
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
