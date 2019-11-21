package com.mei.zhuang.entity.goods;

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
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_operation_log")
public class EsShopOperationLog extends Model<EsShopOperationLog> {

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
    @TableField("service_name")
    private String serviceName;

    /**
     * 记录类名+方法名
     */
    private String methods;

    /**
     * 描述
     */
    @TableField("operation_desc")
    private String operationDesc;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * IP
     */
    private String ip;

    /**
     * 参数
     */
    private String params;

    @TableField("shop_id")
    private Long shopId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
