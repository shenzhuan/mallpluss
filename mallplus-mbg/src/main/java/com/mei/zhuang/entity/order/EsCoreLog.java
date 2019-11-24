package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2019-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_log")
public class EsCoreLog extends Model<EsCoreLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 操作内容
     */
    private String content;
    /**
     * 操作时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 操作ip
     */
    private String ip;
    /**
     * 操作类型名称
     */
    private String type;
    /**
     * 操作店员id
     */
    private Long uid;
    /**
     * 店员名称
     */
    @TableField("employee_name")
    private String employeeName;

    /**
     * 操作类型id
     */
    @TableField("operation_type_id")
    private Long operationTypeId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
