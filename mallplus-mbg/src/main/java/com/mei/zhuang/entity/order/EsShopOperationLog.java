package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("es_shop_operation_log")
@Data
public class EsShopOperationLog extends Model<EsShopOperationLog> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("user_name")
    private String userName;
    @TableField("service_name")
    private String serviceName;
    private String methods;
    @TableField("operation_desc")
    private String operationDesc;
    @TableField("create_time")
    private Date createTime;
    private String ip;
    private String params;
    @TableField("shop_id")
    private Long shopId=1l;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
