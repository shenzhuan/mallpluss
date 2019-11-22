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
 * @since 2019-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_payment")
public class EsShopPayment extends Model<EsShopPayment> {

    private static final long serialVersionUID = 1L;

    /**
     * API秘钥
     */

    private String key;
    /**
     * cert证书秘钥
     */

    @TableField("key_cert")
    private String keyCert;
    /**
     * 私钥
     */
    @TableField("private_key")
    private String privateKey;
    /**
     * 支付状态 0:启用 1：禁用
     */

    private Integer status;
    /**
     * 支付类型 0：微信支付  1：微信支付子商户
     */

    private Integer type;
    /**
     * 服务商支付商户号
     */

    @TableField("mch_id")
    private String mchId;
    /**
     * 公钥
     */
    @TableField("public_key")
    private String publicKey;
    /**
     * 子服务商支付商户号
     */
    @TableField("sub_mch_id")
    private Long subMchId;
    /**
     * 小程序id
     */

    @TableField("appid")
    private String appid;
    /**
     * cert证书文件
     */

    @TableField("cert")
    private String cert;
    /**
     * 支付名称
     */
    @TableField("title")
    private String title;
    /**
     * 使用次数
     */
    @TableField("use_count")
    private Integer useCount;
    /**
     * 支持渠道
     */
    @TableField("channel_support")
    private Integer channelSupport;
    private Long id;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 子支付商户号
     */
    @TableField("sub_appid")
    private String subAppid;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人id（memberId）
     */
    @TableField("create_by_id")
    private Long createById;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 修改人id（memberId）
     */
    @TableField("update_by_id")
    private Long updateById;
    /**
     * 余额支付状态： 0:启用 1:禁用
     */
    @TableField("remain_staus")
    private Integer remainStaus;

    /**
     * 是否删除 0:不删除 1：删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 下单回调地址
     */
    @TableField("notify_url")
    private String notifyUrl;
    /**
     * 支付方式
     */

    @TableField("pay_type")
    private Integer payType;

    /**
     * cert文件
     *//*
    @TableField(exist = false)
    private File certFile;
    */

    /**
     * cert文件秘钥
     *//*
    @TableField(exist = false)
    private File keyCertFile;*/
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
