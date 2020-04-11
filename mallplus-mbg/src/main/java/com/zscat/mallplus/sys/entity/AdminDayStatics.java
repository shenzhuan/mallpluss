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
商户管理
*/
@Data
@TableName("admin_day_statics")
public class AdminDayStatics extends BaseEntity implements Serializable {


            @TableId(value = "id", type = IdType.AUTO)
        private Integer id;


            /**
            添加商品
            **/
            @TableField( "goods_count")
        private Integer goodsCount;


            /**
            支付订单数
            **/
            @TableField( "pay_order_count")
        private Integer payOrderCount;


            /**
            文章数
            **/
            @TableField( "article_count")
        private Integer articleCount;


            /**
            支付金额
            **/
            @TableField( "pay_amount")
        private BigDecimal payAmount;


            /**
            添加会员数
            **/
            @TableField( "member_count")
        private Integer memberCount;


            /**
            创建时间
            **/
            @TableField( "create_time")
        private Date createTime;


            /**
            商户
            **/
            @TableField( "store_id")
        private Integer storeId;


            /**
            没有支付订单数
            **/
            @TableField( "not_pay_order_count")
        private Integer notPayOrderCount;


            /**
            没有支付订单金额
            **/
            @TableField( "not_pay_amount")
        private BigDecimal notPayAmount;


            /**
            商户
            **/
            @TableField( "store_name")
        private String storeName;


}
