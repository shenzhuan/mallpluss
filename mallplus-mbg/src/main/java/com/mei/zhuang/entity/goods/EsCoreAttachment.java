package com.mei.zhuang.entity.goods;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_attachment")
public class EsCoreAttachment extends Model<EsCoreAttachment> {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * md5校验
     */
    private Char md5;
    /**
     * 当前登录用户ID
     */
    private Long uid;
    /**
     * 上传时间
     */
    @TableField("upload_time")
    private Date uploadTime;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 商铺ID
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 文件类型(image，video，audio)
     */
    private String type;
    /**
     * 文件地址
     */
    @TableField("img_address")
    private String imgAddress;

    /**
     * 分组id
     */
    @TableField("category_id")
    private Long categoryId;

    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
