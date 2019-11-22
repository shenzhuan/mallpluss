package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_attachment_category")
public class EsCoreAttachmentCategory extends Model<EsCoreAttachmentCategory> {
    /**
     * 编号
     */
    private Long id;
    /**
     * 分组名称
     */
    private String name;
    /**
     * 商铺编号
     */
    @TableField("shop_id")
    private Long shopId = Long.parseLong("1");
    /**
     * 分组类型 （imgage/file/video/audio）
     */
    private String type;
    /**
     * 用户编号
     */
    private Long uid;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
