package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("es_core_sms_signature")
public class EsCoreSmsSignature extends Model<EsCoreSmsSignature> {

    private static final long serialVersionUID = 1L;

    /**
     * 是否默认 0:否 1：是
     */
    @TableField("is_default")
    private Integer isDefault;
    /**
     * 签名名称
     */
    private String signature;
    /**
     * id
     */
    private Long id;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
