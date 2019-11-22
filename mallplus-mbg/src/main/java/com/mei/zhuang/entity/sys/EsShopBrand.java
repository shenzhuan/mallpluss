package com.mei.zhuang.entity.sys;

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
 * @author arvato team
 * @since 2019-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_brand")
public class EsShopBrand extends Model<EsShopBrand> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 品牌编号
     */
    @TableField("brand_no")
    private Long brandNo;
    /**
     * 品牌名称
     */
    @TableField("brand_name")
    private String brandName;
    /**
     * 联系电话
     */
    @TableField("contact_mobile")
    private String contactMobile;
    /**
     * 联系人
     */
    @TableField("contact_name")
    private String contactName;
    /**
     * 图标
     */
    private String logo;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_by_id")
    private Long createById;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 修改人
     */
    @TableField("update_by_id")
    private Long updateById;
    /**
     * 是否删除： 0:不删除 1：删除
     */
    @TableField("is_delete")
    private Integer isDelete;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
