package com.mei.zhuang.entity.goods;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 省市地址表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_address")
public class EsCoreAddress extends Model<EsCoreAddress> {

    private Long id;
    @TableField("code_id")
    private Long codeId;
    @TableField("parent_id")
    private Long parentId;
    private String name;
    private Integer level;
    private String location;

    @TableField(exist = false)
    private List<EsCoreAddress> listEsCoreAddress;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
