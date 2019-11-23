package com.mei.zhuang.entity;

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
 * @author meizhuang team
 * @since 2019-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("crm_operation_log")
public class CrmOperationLog extends Model<CrmOperationLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 模块名
     */
    private String module;
    /**
     * 记录类名+方法名
     */
    private String method;
    /**
     * 描述
     */
    @TableField("operation_desc")
    private String operationDesc;
    /**
     * 创建时间
     */
    @TableField("add_time")
    private Date addTime;
    /**
     * IP
     */
    private String ip;
    /**
     * 参数
     */
    private String params;
    /**
     * 所属店铺
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 运行时间
     */
    @TableField("time_min")
    private Long timeMin;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
