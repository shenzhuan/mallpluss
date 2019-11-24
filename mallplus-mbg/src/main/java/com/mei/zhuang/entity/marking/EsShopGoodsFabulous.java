package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/8/29 14:22
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_fabulous")
public class EsShopGoodsFabulous {

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    @TableField("member_id")
    private Long memberId;//用户id
    @TableField("goods_id")
    private Long goodsId;//商品id
    @TableField("create_time")
    private Date createTime;//创建时间
    @TableField(exist = false)
    private Integer number;//1:点赞次数+1 -1：点赞次数-1

}
