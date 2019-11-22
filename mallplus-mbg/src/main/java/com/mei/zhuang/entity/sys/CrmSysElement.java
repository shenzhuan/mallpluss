package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author arvato team
 * @since 2017-10-16
 */
@Data
@Accessors(chain = true)
@TableName("crm_sys_element")
public class CrmSysElement extends Model<CrmSysElement> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 资源编码
     */
    private String code;
    /**
     * 资源类型
     */
    private String type;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源路径
     */
    private String uri;
    /**
     * 资源关联菜单
     */
    @TableField("menu_id")
    private String menuId;
    @TableField("parent_id")
    private String parentId;
    /**
     * 资源树状检索路径
     */
    private String path;
    /**
     * 资源请求类型
     */
    private String method;
    /**
     * 描述
     */
    private String description;
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Integer updateUserId;
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 乐观锁用
     */
    @Version
    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
