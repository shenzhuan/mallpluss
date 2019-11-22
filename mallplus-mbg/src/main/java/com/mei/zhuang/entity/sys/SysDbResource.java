package com.mei.zhuang.entity.sys;

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
 * @author arvato team
 * @since 2019-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_db_resource")
public class SysDbResource extends Model<SysDbResource> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 数据源名称
     */
    @TableField("source_name")
    private String sourceName;
    private String url;
    private String username;
    private String password;
    /**
     * 数据库
     */
    @TableField("db_name")
    private String dbName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
