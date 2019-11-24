package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
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
 * @author meizhuang team
 * @since 2018-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_store")
public class CrmStore extends Model<CrmStore> {

    private static final long serialVersionUID = 1L;


     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    private String name;
    @TableField("dept_id")
    private Integer deptId;
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 门店图片
     */
    @TableField("store_img")
    private String storeImg;
    /**
     * 门店编号
     */
    @TableField("store_no")
    private String storeNo;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区县
     */
    private String region;
    /**
     * 地址
     */
    private String address;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 0 开店 1 闭店
     */
    private String status;
    /**
     * 创建人
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    /**
     * 创建日期
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Integer updateUserId;
    /**
     * 更新日期
     */
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 乐观锁用
     */
    @Version
    private Integer version;

    /**
     * 门店联系方式
     */
    private String telphone;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
