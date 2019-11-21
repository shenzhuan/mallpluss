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
@TableName("es_start_advertising_img")
public class EsStartAdvertisingImg  extends Model<EsStartAdvertisingImg> {

    /**
     * 编号
     */
    private Long id;
    /**
     * 启动广告编号
     */
    @TableField("advertising_id")
    private Long advertisingId;
    /**
     * 图片地址
     */
    @TableField("img_address")
    private String imgAddress;
    /**
     * 链接地址
     */
    @TableField("jump_address")
    private String jumpAddress;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
