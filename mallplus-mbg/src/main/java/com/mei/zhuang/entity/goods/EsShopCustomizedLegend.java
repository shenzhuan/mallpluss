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
 * 定制服务：刻字服务的样图
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_customized_legend")
public class EsShopCustomizedLegend extends Model<EsShopCustomizedLegend> {

    /**
     * 编号
     */
    private Long id;

    private Integer uniacid;
    /**
     * 预览图片
     */
    private String img;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 刻字标题
     */
    private String title;
    /**
     * 刻字内容
     */
    private String content;
    /**
     * 文字颜色
     */
    @TableField("write_color")
    private String writeColor;
    /**
     * 文字大小
     */
    @TableField("write_size")
    private Integer writeSize;
    /**
     * 文字显示排版(0不显示 1显示)
     */
    @TableField("write_show")
    private Integer writeShow;
    /**
     * 定位
     */
    private String position;

    /**
     * 当前页码
     */
    @TableField(exist = false)
    private Integer current=1;
    /**
     * 每页显示条数
     */
    @TableField(exist = false)
    private Integer size=10;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
