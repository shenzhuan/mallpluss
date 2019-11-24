package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author meizhuang team
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_decorate_template_order")
public class EsDecorateTemplateOrder extends Model<EsDecorateTemplateOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    @TableField("is_free")
    private Long isFree;
    @TableField("shop_id")
    private Long shopId=1l;
    @TableField("template_id")
    private Long templateId;
    @TableField("start_time")
    private Date startTime;
    @TableField("buy_expire_time")
    private Date buyExpireTime;
    @TableField("expire_time")
    private Date expireTime;
    @TableField("is_buy")
    private Long isBuy;
    @TableField("is_forever")
    private Long isForever;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
