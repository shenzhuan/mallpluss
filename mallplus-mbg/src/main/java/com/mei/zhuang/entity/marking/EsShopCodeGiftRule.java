package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_code_gift_rule")
public class EsShopCodeGiftRule extends Model<EsShopCodeGiftRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 活动类型1 唯一验证码 2 批量验证码
     */
    @TableField("activity_type")
    private Integer activityType;
    /**
     * 验证码赠礼id
     */
    @TableField("code_gift_id")
    private Long codeGiftId;
    private Long id;
    @TableField("shop_id")
    private Long shopId;
    /**
     * 验证码Url
     */
    private String code;
    /**
     * 状态 1 未使用 2 已使用
     */
    private Integer status;

    //验证码信息
    @TableField("code_openid")
    private String codeOpenid;
    //数量
    @TableField(exist =  false)
    private Integer count;
    @Override
    protected Serializable pkVal() {
        return null;
    }

}
