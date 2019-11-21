package com.mei.zhuang.entity.marking;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

//手工券上传用户
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_coupon_manual_user")
public class EsShopCouponManualUser {
    private long id;
    //手工发券id
    @TableField("manual_id")
    private long manualId;
    //上传用户id
    @TableField("user_id")
    private String userId;
    //发放状态 1.未发，2已发
    private String status;

}
