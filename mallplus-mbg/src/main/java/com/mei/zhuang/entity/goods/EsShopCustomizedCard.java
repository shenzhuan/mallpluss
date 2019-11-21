package com.mei.zhuang.entity.goods;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 刻字服务:基本信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_customized_card")
public class EsShopCustomizedCard extends Model<EsShopCustomizedCard> {
    /**
     * 编号
     */
    private Long id;

    private String name;

    private Integer uniacid;
    /**
     * 图片
     */
    private String img;
    /**
     * 卡片类型 1：手写卡片  2：用户输入卡片
     */
    private Integer type;
    /**
     * 卡片提示语
     */
    private String notice;
    /**
     * 描述
     */
    private String code;

    @TableField("create_time")
    private Date createTime;
    @TableField(exist = false)
    private Integer current=1;
    @TableField(exist = false)
    private Integer size=10;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
