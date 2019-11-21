package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 小程序商品定制服务表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_customized_applet")
public class EsShopCustomizedApplet extends Model<EsShopCustomizedApplet> {

    /**
     * 编号
     */
    private Long id;
    /**
     * 商品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 用户iD
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 刻字服务编号
     */
    @TableField("basic_id")
    private Long basicId;
    /**
     * 样图编号
     */
    @TableField("legend_id")
    private Long legendId;
    /**
     * 定制卡片编号
     */
    @TableField("card_id")
    private Long cardId;
    /**
     * 套装编号
     */
    @TableField("packet_id")
    private Long packetId;
    /**
     *      * 刻字内容
     */
    @TableField("basic_content")
    private String basicContent;
    /**
     * 特殊符号
     */
    @TableField("special_symbols")
    private String specialSymbols;
    /**
     * 留言
     */
    @TableField("leaving_message")
    private String leavingMessage;
    /**
     * 卡片寄语
     */
    @TableField("card_message")
    private String cardMessage;

    /**
     * 刻字图片
     */
    @TableField("packet_img")
    private String packetImg;

    /**
     * 卡片图片
     */
    @TableField("card_img")
    private String cardImg;
    /**
     * 购物车ID
     */
    @TableField("cart_id")
    private Long cartId;

    @TableField(exist = false)
    private Object esShopCustomizedBasic;

    @TableField(exist = false)
    private Object esShopCustomizedLegend;

    @TableField(exist = false)
    private Object esShopCustomizedCard;

    @TableField(exist = false)
    private Object esShopCustomizedPacket;

    @TableField(exist = false)
    private Object map;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
