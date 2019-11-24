package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_sms")
public class EsCoreSms extends Model<EsCoreSms> {

    private static final long serialVersionUID = 1L;

    /**
     * 冗余：短信接口标识
     */
    private String type;
    /**
     * 短信服务商名称
     */
    private String name;
    /**
     * 冗余：短信服务商官方网址
     */
    private String url;
    /**
     * 组建类名(对应后端调用不同接口)
     */
    @TableField("class_name")
    private String className;
    /**
     * 冗余：默认配置
     */
    private String settings;
    /**
     * 冗余：接口配置
     */
    @TableField("template_settings")
    private String templateSettings;
    /**
     * 是否启用 0:禁用 1：启用
     */
    @TableField("status")
    private Integer status;
    /**
     * 账号
     */
    @TableField("account")
    private String account;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 短信签名
     */
    @TableField("signature")
    private String signature;

    /**
     * 模板id
     */
    @TableField("template_id")
    private String templateId;
    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private long memberId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
