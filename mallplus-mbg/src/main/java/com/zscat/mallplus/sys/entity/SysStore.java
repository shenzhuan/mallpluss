package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author zscat
 * @since 2019-05-18
 */
@Data
@TableName("sys_store")
public class SysStore implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 店铺名
     */
    private String name;
    // 1 申请 2 拒绝 3 成功
    private Integer status;

    /**
     *店铺logo
     */
    private String logo;

    private Long uid;

    /**
     * 分类
     */
    private Integer type;

    @TableField("contact_qq")
    private String contactQq;

    /**
     * 负责人
     */
    @TableField("contact_name")
    private String contactName;
    /**
     * 应用执照pic
     */
    @TableField("support_name")
    private String supportName;
    /**
     * 联系电话
     */
    @TableField("support_phone")
    private String supportPhone;

    /**
     * 二维码
     */
    @TableField("contact_qrcode")
    private String contactQrcode;


    /**
     *联系电话
     */
    @TableField("contact_mobile")
    private String contactMobile;
    /**
     * 身份证照
     */
    @TableField("service_phone")
    private String servicePhone;

    @TableField("address_province")
    private String addressProvince;

    @TableField("address_lat")
    private String addressLat;

    @TableField("address_lng")
    private String addressLng;

    
    @TableField("address_detail")
    private String addressDetail;

    @TableField("address_city")
    private String addressCity;


    @TableField("address_area")
    private String addressArea;

    @TableField("buy_plan_times")
    private Long buyPlanTimes;

    @TableField("create_time")
    private Date createTime;

    @TableField("sms_quantity")
    private Long smsQuantity;

    @TableField("register_type")
    private Integer registerType;


    @TableField("expire_time")
    private Date expireTime;

    @TableField("try_time")
    private Date tryTime;
    @TableField("is_checked")
    private Integer isChecked;

    @TableField("is_deleted")
    private Integer isDeleted;





    @TableField("delete_time")
    private Date deleteTime;

    @TableField("diy_profile")
    private String diyProfile;

    @TableField("industry_two")
    private Long industryTwo;

    @TableField("is_star")
    private Integer isStar;

    @TableField("is_try")
    private Integer isTry;


    @TableField("plan_id")
    private Long planId;
    @TableField("last_login_time")
    private Date lastLoginTime;


    private String description;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("industry_one")
    private Long industryOne;

    @TableField(exist = false)
    private List<PmsProductAttributeCategory> list;

    /**
     *浏览
     */
    private Integer hit;

    /**
     * 余额
     */
    private BigDecimal amount;
    /**
     *冻结金额
     */
    @TableField("freez_amount")
    private BigDecimal freezAmount;
    /**
     *收藏
     */
    private Integer collect;
    /**
     *是否首页展示
     */
    @TableField("is_boutique")
    private Integer isBoutique;
    @TableField("goods_count")
    private Integer goodsCount;


    /**
     * 支付订单数
     */
    @TableField("order_count")
    private Integer orderCount;

    /**
     * 文章数
     */
    @TableField("article_count")
    private Integer articleCount;

    /**
     * 支付金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;

    /**
     * 添加会员数
     */
    @TableField("member_count")
    private Integer memberCount;

    @TableField(exist = false)
    private double  distance;
    @TableField(exist = false)
    private Integer sort;
    @TableField(exist = false)
    private boolean favorite;
}
