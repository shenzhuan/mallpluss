package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 卡片寄语设置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_card_message")
public class EsShopCardMessage extends Model<EsShopCardMessage> {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 轮播图
     */
    private String banner;
    /**
     * 卡片模版
     */
    @TableField("card_templ")
    private String cardTempl;
    /**
     * 分享标题
     */
    @TableField("share_title")
    private String shareTitle;
    /**
     * 分享图片
     */
    @TableField("share_img")
    private String shareImg;

    @TableField(exist = false)
    private String cutList;

    @TableField(exist = false)
    private List<EsShopCardMessageCut> listCut;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
