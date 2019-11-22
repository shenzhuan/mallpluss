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
 * @author meizhuang team
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_decorate_system_template")
public class EsDecorateSystemTemplate extends Model<EsDecorateSystemTemplate> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long type;
    private String name;
    private String thumb;
    private String description;
    private String common;
    @TableField("page_count")
    private Long pageCount;
    @TableField("display_order")
    private Long displayOrder;
    @TableField("system_id")
    private Long systemId;
    private Long author;
    @TableField("author_name")
    private String authorName;
    @TableField("is_charge")
    private Long isCharge;
    @TableField("is_forever")
    private Long isForever;
    private Long status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
