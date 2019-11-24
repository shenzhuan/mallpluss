package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 卡片寄语切割图
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_card_message_cut")
public class EsShopCardMessageCut extends Model<EsShopCardMessageCut> {

    /**
     * 卡片寄语id
     */
    @TableField("card_message_id")
    private Long cardMessageId;

    /**
     * 切割图
     */
    @TableField("cut_img")
    private String cutImg;

    /**
     * 拼接图完整图
     */
    @TableField("complete_img")
    private String completeImg;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
