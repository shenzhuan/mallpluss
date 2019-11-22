package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@TableName("crm_sys_menu")
@ApiModel(value = "菜单javaBean")
public class CrmSysMenu extends Model<CrmSysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 菜单id
     */
    private Integer id;
    /**
     * 路径编码
     */
    @ApiModelProperty(value = "路径编码", name = "code", required = true)
    private String code;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", name = "name", required = true)
    private String name;

    /**
     * 菜单类型
     */
    @ApiModelProperty(value = "菜单类型", name = "type", required = true)
    private Integer type;
    /**
     * 父级节点
     */
    @TableField("p_id")
    @JsonProperty("pId")
    @ApiModelProperty(value = "父级节点", name = "pId")
    private Integer pId;
    /**
     * 资源路径
     */
    @ApiModelProperty(value = "资源路径", name = "href")
    private String href;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标", name = "icon", required = true)
    private String icon;
    /**
     * 排序
     */
    @TableField("order_num")
    @ApiModelProperty(value = "排序", name = "orderNum")
    private Integer orderNum;

    /**
     * 菜单级别
     */
    @ApiModelProperty(value = "菜单级别", name = "level", required = true)
    private Integer level;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "description")
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

    @TableField(exist = false)
    private List<CrmSysMenu> children;

    /**
     * 菜单全名
     */
    @TableField(exist = false)
    private String fullName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
