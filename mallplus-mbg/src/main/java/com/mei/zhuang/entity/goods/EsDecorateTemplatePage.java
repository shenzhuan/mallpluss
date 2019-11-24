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
@TableName("es_decorate_template_page")
public class EsDecorateTemplatePage extends Model<EsDecorateTemplatePage> {

    private static final long serialVersionUID = 1L;

    @TableField("template_id")
    private Long templateId;
    private String thumb;
    private Long type;
    @TableField("update_time")
    private Date updateTime;
    @TableField("create_time")
    private Date createTime;
    @TableField("is_home")
    private Long isHome;
    private Long key;
    @TableField("system_id")
    private Long systemId;
    @TableField("shop_id")
    private Long shopId=1l;
    private String content;
    @TableField("display_order")
    private Long displayOrder;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    private String name;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
