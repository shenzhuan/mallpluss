package com.mei.zhuang.entity.sys;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ApiModel("接口类")
@Setter
@Getter
@Accessors(chain = true)
@TableName("crm_sys_menu_interface")
public class CrmSysMenuInterface extends Model<CrmSysMenuInterface> {
    private long id;
    @TableField("menu_id")
    private Integer menuId;
    //接口路径
    @TableField("menu_interface")
    private String menuInterface;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
