package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mei.zhuang.entity.order.EsAppletSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_message")
public class EsCoreMessage extends Model<EsAppletSet> {

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;
    /**
     * 权限等级
     */
    @TableField("role")
    private Long role;
    /**
     * 消息类型
     */
    @TableField("message_type")
    private String messageType;
    /**
     * 模板标题
     */
    @TableField("title")
    private String title;
    /**
     * 模板签名
     */
    @TableField("message_sign")
    private String messageSign;
    /**
     * 描述
     */
    @TableField("description")
    private String description;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
