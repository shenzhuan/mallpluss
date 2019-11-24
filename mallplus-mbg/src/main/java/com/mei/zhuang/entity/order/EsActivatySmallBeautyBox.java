package com.mei.zhuang.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 小美盒定制——活动
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_activaty_small_beauty_box")
public class EsActivatySmallBeautyBox extends Model<EsActivatySmallBeautyBox> {

    /**
     * 编号
     */

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 活动名称
     */
    @TableField("activaty_name")
    private String activatyName;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 开始时间
     */
    @TableField("start_time")
    private String startTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private String endTime;
    /**
     * 步数
     */
    @TableField("step_number")
    private Integer stepNumber;

    @TableField(exist = false)
    private Integer current = 1;

    @TableField(exist = false)
    private Integer size = 10;
    @TableField(exist = false)
    private Long memberId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
