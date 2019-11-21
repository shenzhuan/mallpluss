package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @author arvato team
 * @since 2019-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("crm_sys_log_type")
public class CrmSysLogType extends Model<CrmSysLogType> {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 操作类型名称
     */
    @TableField("operation_type")
    private String operationType;
    /**
     * 描述
     */
    private String desc;

    /**
     * 所属模块
     */
    @TableField("belong_module")
    private String belongModule;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
