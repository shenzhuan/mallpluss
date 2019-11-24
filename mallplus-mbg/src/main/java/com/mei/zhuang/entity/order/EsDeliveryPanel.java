package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_delivery_panel")
public class EsDeliveryPanel extends Model<EsDeliveryPanel> {

    private static final long serialVersionUID = 1L;

    /**
     * 校验码
     */
    @TableField("app_secret")
    private String appSecret;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 面板密码
     */
    @TableField("panel_pwd")
    private String panelPwd;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 面板账户
     */
    @TableField("panel_account")
    private String panelAccount;
    /**
     * 邮费支付方式
     */
    @TableField("postage_payment_method")
    private String postagePaymentMethod;
    /**
     * 改变状态
     */
    @TableField("change_status")
    private Long changeStatus;
    /**
     * 店铺收藏编号
     */
    @TableField("collection_shop_no")
    private String collectionShopNo;
    /**
     * 公司
     */
    private String company;
    /**
     * 是否默认：0：不默认 1：默认
     */
    @TableField("is_default")
    private Integer isDefault;
    /**
     * 密钥
     */
    @TableField("app_key")
    private String appKey;
    /**
     * 月结编号
     */
    @TableField("month_knot_no")
    private String monthKnotNo;
    /**
     * 名称
     */
    private String name;
    /**
     * 样式
     */
    private String style;
    /**
     * 是佛通知： 0 ：不通知 1：通知
     */
    @TableField("is_notice")
    private Integer isNotice;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
