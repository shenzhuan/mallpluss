package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 抽奖有礼:奖项
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_activity_prize")
public class EsShopActivityPrize extends Model<EsShopActivityPrize> {
    /**
     * 编号
     */
    private Long id;
    /**
     * 抽奖活动iD
     */
    @TableField("activaty_id")
    private Long activatyId;
    /**
     * 奖品等级 （1、一等奖 2、二等奖 ~~~~）
     */
    private Integer level;
    /**
     * 奖品名称
     */
    private String name;
    /**
     * 奖品类型（1、赠品 2、优惠卷 3、送积分）
     */
    private Integer type;
    /**
     * 赠品信息
     */
    @TableField("goods_content")
    private String goodsContent;
    /**
     * 总数
     */
    private Integer total;
    /**
     * 单日最低发放
     */
    private Integer minimum;
    /**
     * 单日最多发放
     */
    private Integer most;
    /**
     * 单人中奖个数上限
     */
    @TableField("upper_limit")
    private Integer upperLimit;
    /**
     * 奖品图片
     */
    private String img;
    /**
     * 奖品说明
     */
    private String content;
    /**
     * 中奖率
     */
    private Integer winning;

    private Integer location;//奖品位置
    @TableField(exist = false)
    private List<Integer> list;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
