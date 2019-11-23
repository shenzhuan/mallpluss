package com.mei.zhuang.entity.order;

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
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_settings")
public class EsShopOrderSettings extends Model<EsShopOrderSettings> {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Long id;
    /**
     * 自动关闭时间（以分钟单位）
     */
    @TableField("auto_close_time")
    private Integer autoCloseTime;
    /**
     * 自动完成确认天数 （以天为单位）
     */
    @TableField("auto_finsh_day")
    private Integer autoFinshDay;
    /**
     * 是否在下单的时候获取微信收货地址  0:获取 1：不获取
     */
    @TableField("is_get_address")
    private Integer isGetAddress;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人id
     */
    @TableField("create_by_id")
    private Long createById;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 修改人id
     */
    @TableField("update_by_id")
    private Long updateById;
    /**
     * 是否删除 0：不删除 1：删除
     */
    @TableField("is_delete")
    private Integer isDelete;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
