package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_applet_set")
public class EsAppletSet extends Model<EsAppletSet> {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 小程序APPID
     */
    @TableField("appid")
    private String appId;
    /**
     * 小程序密钥
     */
    @TableField("app_secret")
    private String appSecret;
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
    private List<EsCoreMessageTemplate> listOrderSuccessNotification;//下单成功通知
    @TableField(exist = false)
    private List<EsCoreMessageTemplate> listOrderPaySuccess;//支付成功通知
    @TableField(exist = false)
    private List<EsCoreMessageTemplate> listOrderCancellationNotice;//订单取消通知
    @TableField(exist = false)
    private List<EsCoreMessageTemplate> listOrderSend;//订单发货通知
    @TableField(exist = false)
    private List<EsCoreMessageTemplate> listOrderStatusChangeNotification;//订单状态变更通知


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
