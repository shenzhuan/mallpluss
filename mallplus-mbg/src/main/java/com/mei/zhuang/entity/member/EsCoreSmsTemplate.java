package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_sms_template")
public class EsCoreSmsTemplate extends Model<EsCoreSmsTemplate> {

    private static final long serialVersionUID = 1L;


    @TableField(exist = false)
    private List<SmsVariable> templateVariMapList;
    private Long id;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 短信模板名称
     */
    @TableField("sms_name")
    private String smsName;
    /**
     * 发送条件 （冗余）
     */
    @TableField("send_condition")
    private Long sendCondition;
    /**
     * 发送对象 1买家 2用户（冗余）
     */
    @TableField("send_object")
    private Long sendObject;
    /**
     * 短信类型id
     */
    private Long type;

    /**
     * 服务商模板签名
     */
    @TableField("sms_sign")
    private String smsSign;
    /**
     * 模板数据(json格式数据)
     */
    private String data;
    /**
     * 模板状态 0禁用 1启用
     */
    private Integer status;
    /**
     * 模板内容
     */
    private String content;
    /**
     * 是否默认 0否 1是
     */
    @TableField("is_default")
    private Long isDefault;
    /**
     * 模板创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 是否删除：0:不删除 1：删除
     */
    @TableField("is_delete")
    private Integer isDelete;


    /**
     * 服务商表id
     */
    @TableField("server_id")
    private Long serverId;

    @TableField("sms_template_id")
    private String smsTemplateId;

    /**
     * 模板类别（模板类型）
     */
    @TableField("template_type")
    private Long templateType;

    @TableField(exist = false)
    private EsCoreSms serverInfo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /*public void setTemplateVariMap(String jsonData){
        //....
    }

    public Map<String,Integer> getTemplateVariMap(String jsonData){
        //....
        return null;
    }
*/
}
