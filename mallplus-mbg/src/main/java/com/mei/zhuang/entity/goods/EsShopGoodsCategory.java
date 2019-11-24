package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-16
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods_category")
public class EsShopGoodsCategory extends Model<EsShopGoodsCategory> {

    private static final long serialVersionUID = 1L;

    @TableField("shop_id")
    private Long shopId=1l;        //商铺ID
    private String thumb;          //分类图
    @TableField("display_order")
    private Integer displayOrder;//显示顺序
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    private String name;        //分类名称
    @TableField("parent_id")
    private Long parentId;      //父级iD
    @TableField("create_time")
    private Date createTime;    //创建时间
    @TableField("last_time")
    private Date lastTime;//最后修改时间
    private Integer level;      //分类等级
    private Integer status;     //状态：0 禁用 1 启用

    @TableField(exist = false)
    private String goodsIds;    //商品ID
    @TableField("is_show")
    private Integer isShow;//是否前端显示 暂未用的
    @TableField(exist = false)
    private String advertImg;//广告图
    @TableField(exist = false)
    private String advertAddress;//图片地址

    @TableField(exist = false)
    private Integer current = 0;
    @TableField(exist = false)
    private Integer size = 10;

    @TableField(exist = false)
    private List<EsShopGoodsCategoryAdvertimg> categoryAdvert; //分类广告图//格式：图片|地址,图片|地址
    @TableField(exist = false)
    private List<EsShopGoods> goodsList;//推荐商品

    @TableField(exist = false)
    private List<EsShopGoodsCategory> listTwo;
    @TableField(exist = false)
    private List<EsShopGoodsCategory> listThree;
    @TableField(exist = false)
    private EsShopGoodsCategory categoryTwo;
    @TableField(exist = false)
    private EsShopGoodsCategory categoryOne;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
