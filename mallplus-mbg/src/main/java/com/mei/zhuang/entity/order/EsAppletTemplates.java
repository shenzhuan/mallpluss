package com.mei.zhuang.entity.order;

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
 *
 * </p>
 *
 * @author arvato team
 * @since 2019-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_applet_templates")
public class EsAppletTemplates extends Model<EsAppletTemplates> {

    private static final long serialVersionUID = 1L;

    private Long id;
    @TableField("template_id")
    private String templateId;
    @TableField("shop_id")
    private Long shopId;
    private String title;
    private String content;
    private String example;
    /**
     * 1 q启动 2 禁用
     */
    private Integer status;
    @TableField("create_time")
    private Date createTime;
    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
