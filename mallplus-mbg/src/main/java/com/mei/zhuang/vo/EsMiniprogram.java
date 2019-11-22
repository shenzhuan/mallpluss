package com.mei.zhuang.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.entity.order.EsCoreMessageTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_miniprogram")
public class EsMiniprogram extends Model<EsMiniprogram> {

    private static final long serialVersionUID = 1L;

    @TableField("shop_type")
    private Integer shopType;
    private String signature;
    @TableField("refresh_token")
    private String refreshToken;
    @TableField("app_secret")
    private String appSecret;
    @TableField("func_info")
    private String funcInfo;
    @TableField("nick_name")
    private String nickName;
    @TableField("user_name")
    private String userName;
    private String appid;
    private Long id;
    @TableField("shop_id")
    private Long shopId;
    @TableField("principal_name")
    private String principalName;
    @TableField("qrcode_url")
    private String qrcodeUrl;
    @TableField("authorizer_access_token")
    private String authorizerAccessToken;
    @TableField("head_img")
    private String headImg;
    @TableField("is_authorized")
    private Integer isAuthorized;

    /**
     * 是否维护状态
     */
    @TableField("is_vindicate")
    private Integer isVindicate;
    /**
     * 是否开启用户绑定
     */
    @TableField("is_user_binding")
    private Integer isUserBinding;
    /**
     * 模板消息选择(1.小程序模板消息  2.公众号模板消息)
     */
    @TableField("type")
    private Integer type;
    /**
     * 是否显示在线客服悬窗
     */
    @TableField("is_show_customer_service")
    private Integer isShowCustomerService;
    /**
     * 是否显示电话客服悬窗
     */
    @TableField("is_show_phone_service")
    private Integer isShowPhoneService;
    /**
     * 下单成功通知
     */
    @TableField("place_order_inform")
    private Integer placeOrderInform;
    /**
     * 支付成功通知
     */
    @TableField("pay_succed_inform")
    private Integer paySuccedInform;
    /**
     * 订单取消通知
     */
    @TableField("order_cancel_inform")
    private Integer orderCancelInform;
    /**
     * 订单发货通知
     */
    @TableField("order_shipments_inform")
    private Integer orderShipmentsInform;
    /**
     * 订单状态更新通知
     */
    @TableField("order_status_inform")
    private Integer orderStatusInform;
    /**
     * 绑定节点（1.支付订单2.确认订单 3.进入店铺 4.加入购物车）
     */
    @TableField("binding_nodes")
    private Integer bindingNodes;
    @TableField(exist = false)
    private List<EsCoreMessageTemplate> listOrderTemplate;//查询所有模版列表


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
