package com.mei.zhuang.entity.sys;

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
 * @since 2019-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dyn_gate")
public class SysDynGate extends Model<SysDynGate> {

    private static final long serialVersionUID = 1L;

    /**
     * 路由器ID
     */
    private String id;
    /**
     * 路由器路径
     */
    private String path;
    /**
     * 要映射到此路由的服务ID（如果有）。可以指定物理URL或服务，但不能同时指定两者
     */
    @TableField("service_id")
    private String serviceId;
    /**
     * 映射到路由的完整物理URL。另一种方法是使用服务ID和服务发现来查找物理地址
     */
    private String url;
    /**
     * 指示此路由应可重试（如果支持）的标志。通常，重试需要服务ID和功能区。
     */
    private Boolean retryable;
    private Boolean enabled;
    /**
     * 一个标志，用于确定转发前是否应删除此路由的前缀（路径，减去模式修补程序）
     */
    @TableField("strip_prefix")
    private Boolean stripPrefix;
    @TableField("api_name")
    private String apiName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
