package com.mei.zhuang.entity.goods;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 刻字服务:基本信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_customized_basic")
public class EsShopCustomizedBasic extends Model<EsShopCustomizedBasic> {

    /**
     * 编号
     */
    private Long id;
    /**
     * 刻字标题
     */
    private String title;
    /**
     * 刻字价格
     */
    private BigDecimal price;
    /**
     * 是否开启满额免费
     */
    @TableField("is_full")
    private Integer isFull;
    /**
     * 满额免费价格
     */
    @TableField("full_price")
    private BigDecimal fullPrice;
    /**
     * 减免开始时间
     */
    @TableField("start_time")
    private String startTime;
    /**
     * 减免结束时间
     */
    @TableField("end_time")
    private String endTime;
    /**
     * 刻字内容提示
     */
    @TableField("basic_content_tip")
    private String basicContentTip;
    /**
     * 刻字要求描述
     */
    @TableField("basic_rqe_desc")
    private String basicRqeDesc;
    /**
     * 样例图
     */
    @TableField("legend_img")
    private String legendImg;
    /**
     * 特殊符号标题
     */
    @TableField("spec_sym_title")
    private String specSymTitle;
    /**
     * 特殊符号描述
     */
    @TableField("spec_sym_desc")
    private String specSymDesc;
    /**
     * 特殊符号图片
     */
    @TableField("spec_sym_img")
    private String specSymImg;
    /**
     * 留言框提示
     */
    @TableField("box_message")
    private String boxMessage;
    /**
     * 刻字须知内容
     */
    @TableField("basic_notice")
    private String basicNotice;

    @TableField(exist = false)
    private EsShopCustomizedBasic data;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
