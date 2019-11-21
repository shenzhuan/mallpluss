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
@TableName("es_decorate_system_template_page")
public class EsDecorateSystemTemplatePage extends Model<EsDecorateSystemTemplatePage> {

    private static final long serialVersionUID = 1L;

    private Long id;
    @TableField("template_id")
    private Long templateId;
    private Long type;
    private String name;
    private String thumb;
    private String content;
    @TableField("is_home")
    private Long isHome;
    private Long key;
    @TableField("display_order")
    private Long displayOrder;
    @TableField("system_id")
    private Long systemId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
