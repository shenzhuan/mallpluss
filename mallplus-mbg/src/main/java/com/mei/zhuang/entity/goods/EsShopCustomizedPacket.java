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
 * 封套/包装列表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_customized_packet")
public class EsShopCustomizedPacket extends Model<EsShopCustomizedPacket> {
    /**
     * 编号
     */
    private Long id;

    private Integer uniacid;
    /**
     * 图片
     */
    private String img;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 封套1包装盒2
     */
    private Integer type;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
