package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 套餐商品
 * </p>
 *
 * @author arvato team
 * @since 2019-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_package_goods")
public class EsShopPackageGoods extends Model<EsShopPackageGoods> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 套餐类型1.自组套餐 2.固定套餐
     */
    @TableField("package_type")
    private Integer packageType;
    /**
     * 套餐名称
     */
    @TableField("package_name")
    private String packageName;
    /**
     * 推荐图片
     */
    private String picture;
    /**
     * 轮播图片
     */
    @TableField("by_picture")
    private String byPicture;
    /**
     * 订单图片
     */
    @TableField("order_picture")
    private String orderPicture;
    /**
     * 起始时间
     */
    @TableField("starting_time")
    private Date startingTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 1.开启，2禁用
     */
    private Integer status;
    /**
     * 套餐价格
     */
    @TableField("package_price")
    private BigDecimal packagePrice;

    @TableField(exist = false)
    private String specPrice;
    /**
     * 原价
     */
    private BigDecimal price;
    /**
     * 成本价
     */
    @TableField("cost_price")
    private BigDecimal costPrice;
    //1.显示  2,隐藏
    private int according;
    @TableField(exist = false)
    private List<EsShopPackageGoodsSpec> PackageGoodsSpecList;
    @TableField(exist = false)
    private String packagespecList;
    @TableField(exist = false)
    private String time;
    @TableField(exist = false)
    private Integer total;

    @TableField("good_id")
    private long goodId;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
