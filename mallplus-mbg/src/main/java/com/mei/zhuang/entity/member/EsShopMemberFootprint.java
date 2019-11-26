package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_member_footprint")
public class EsShopMemberFootprint extends Model<EsShopMemberFootprint> {

    private static final long serialVersionUID = 1L;

    @TableField("goods_id")
    private Long goodsId;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    @TableField("member_id")
    private Long memberId;
    @TableField("shop_id")
    private Long shopId=1l;
    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}