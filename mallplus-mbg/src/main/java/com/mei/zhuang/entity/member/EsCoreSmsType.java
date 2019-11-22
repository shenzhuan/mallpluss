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
@TableName("es_core_sms_type")
public class EsCoreSmsType extends Model<EsCoreSmsType> {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<EcSmsFunction> smsTypeFunctionList;

    private Long id;
    /**
     * 类型名称
     */
    @TableField("type_name")
    private String typeName;
    /**
     * 父id
     */
    @TableField("parent_id")
    private Long parentId;
    /**
     * 分类级别
     */
    private Integer level;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_by_id")
    private Long createById;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 修改人id
     */
    @TableField("update_by_id")
    private Long updateById;
    /**
     * 是否删除 ：0:否 1：是
     */
    @TableField("is_delete")
    private Integer isDelete;
    /**
     * 描述
     */
    private String description;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
