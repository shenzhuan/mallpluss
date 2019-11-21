package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author arvato team
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_share_map")
public class EsShopShareMap extends Model<EsShopShareMap> {

    private static final long serialVersionUID = 1L;

    /**
     * 获赠奖品id
     */
    @TableField("share_id")
    private Long shareId;
    /**
     * 获赠类型 1 发起者 2.助力
     */
    @TableField("winning_type")
    private Integer winningType;
    /**
     * 获赠id
     */
    @TableField("winning_id")
    private Long winningId;
    /**
     * 活动名称
     */
    @TableField("winning_name")
    private String winningName;
    /**
     * 图片
     */
    private String pic;
    //商品id或赠品
    @TableField("goods_id")
    private long goodsId;
    private Integer id;
   // 1.优惠 2 赠品
    private Integer type;
    @TableField(exist = false)
    private Long memberId;
    @TableField(exist = false)
    private Long shareAssId;
    @TableField(exist = false)
    private Integer number;
    @TableField(exist = false)
    private Long shareMemberId;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
