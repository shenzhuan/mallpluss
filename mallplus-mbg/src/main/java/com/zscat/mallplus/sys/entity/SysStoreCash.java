package com.zscat.mallplus.sys.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
    import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author mallplus
* @date 2020-04-10
门店管理
*/
@Data
@TableName("sys_store_cash")
public class SysStoreCash extends BaseEntity implements Serializable {


            @TableId(value = "id", type = IdType.AUTO)
        private Long id;


            /**
            
            **/
            @TableField( "create_time")
        private Date createTime;


            /**
            
            **/
            @TableField( "update_time")
        private Date updateTime;


            /**
             收款账号
            **/
            @TableField( "account")
        private String account;


            /**
             获取金额
            **/
            @TableField( "amount")
        private BigDecimal amount;


            /**
             收款银行
            **/
            @TableField( "bank")
        private String bank;


            /**
            
            **/
            @TableField( "status")
        private Integer status;



         private BigDecimal balance;

}
