package com.mei.zhuang.entity.goods;

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
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_decorate_template_channel")
public class EsDecorateTemplateChannel extends Model<EsDecorateTemplateChannel> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String channel;
    @TableField("shop_id")
    private Long shopId;
    @TableField("template_id")
    private Long templateId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
