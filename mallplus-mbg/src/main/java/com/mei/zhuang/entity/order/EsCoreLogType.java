package com.mei.zhuang.entity.order;

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
 * @since 2019-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_core_log_type")
public class EsCoreLogType extends Model<EsCoreLogType> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 操作类型
     */
    @TableField("operation_type")
    private String operationType;
    /**
     * 描述
     */
    private String desc;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
