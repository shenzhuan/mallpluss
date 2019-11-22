package com.mei.zhuang.entity.order;

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
 * @author meizhuang team
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_delivery_setting")
public class EsDeliverySetting extends Model<EsDeliverySetting> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 类型
     */
    private String type;
    /**
     * 值
     */
    private String value;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
