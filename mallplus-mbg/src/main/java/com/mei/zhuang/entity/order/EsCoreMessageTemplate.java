package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_message_template")
public class EsCoreMessageTemplate extends Model<EsCoreMessageTemplate> {
    /**
     *编号
     */
    private Long id;
    /**
     *信息签名
     */
    @TableField("message_sign")
    private String messageSign;
    @TableField("template_id")
    private Long templateId;
    /**
     *模板签名
     */
    @TableField("template_sign")
    private String templateSign;
    /**
     *是否默认
     */
    @TableField("is_default")
    private Long isDefault;
    /**
     *关键字编号集合
     */
    @TableField("keyword_id_list")
    private String keywordIdlist;
    /**
     *消息类型
     */
    @TableField("message_type")
    private String messageType;
    /**
     *原始模板ID
     */
    @TableField("original_template_id")
    private String originalTemplateId;
    /**
     *发送类型
     */
    @TableField("send_type")
    private String sendType;
    /**
     *模板
     */
    @TableField("template")
    private String template;
    /**
     *
     */
    @TableField("thumb")
    private String thumb;
    /**
     *app名称
     */
    @TableField("app_name")
    private String appName;
    /**
     *核心消息id
     */
    @TableField("core_message_id")
    private Long coreMessageId;
    /**
     *标题
     */
    @TableField("title")
    private String title;
    @TableField("create_time")
    private Date createTime;
    /**
     * 状态
     */
    @TableField("status")
    private Integer status;
    @TableField(exist = false)
    private Integer size=10;

    private String address;
    @TableField(exist = false)
    private Integer current=1;
    @TableField(exist = false)
    private List<EsAppletTemplates> appletTemplatesList;





    @Override
    protected Serializable pkVal() {
        return null;
    }
}
