package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author arvato team
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_diypage")
public class EsShopDiypage extends Model<EsShopDiypage> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 公众号:对应的就是shopId
     */
    private Long uniacid;
    /**
     * 页面类型 1全部页面 2商城首页 3会员中心页 4商城详情页 5自定义页面
     */
    private Integer type;
    /**
     * 页面名称
     */
    private String name;
    /**
     * 页面标题
     */
    private String title;
    /**
     * DIY数据
     */
    private String data;
    /**
     * 建立时间
     */
    @TableField("establish_time")
    private Date establishTime;
    /**
     * 最后编辑时间
     */
    @TableField("lastedit_time")
    private Date lasteditTime;
    /**
     * 是否显示
     */
    private Integer status;
    /**
     * 关键字设置
     */
    private String keyword;
    /**
     * 底部菜单 -1 不显示 0 系统默认
     */
    private Integer diymenu;
    /**
     * 多商户ID
     */
    private Long merch;
    @TableField("create_time")
    private Date createTime;

    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
