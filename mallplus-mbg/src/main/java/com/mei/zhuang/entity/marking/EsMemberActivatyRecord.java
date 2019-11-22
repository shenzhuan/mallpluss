package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户抽奖记录表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_activaty_record")
public class EsMemberActivatyRecord extends Model<EsMemberActivatyRecord> {

    /**
     * 编号
     */
    private Long id;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 活动编号
     */
    @TableField("activaty_id")
    private Long activatyId;
    /**
     * 用户ID
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 是否中奖（0未中奖 1 中奖）
     */
    @TableField("is_win")
    private Integer isWin;
    /**
     * 奖品编号
     */
    @TableField("prize_id")
    private Long prizeId;
    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 用户标识符
     */
    @TableField("open_id")
    private String openId;
    /**
     * 奖项
     */
    @TableField("prize_level")
    private String prizeLevel;
    /**
     * 奖品名称
     */
    @TableField("prize_name")
    private String prizeName;
    /**
     * 活动名称
     */
    @TableField("activaty_name")
    private String activatyName;

    /**
     * 开始时间(前端传的是时间戳需要转换)
     */
    @TableField(exist = false)
    private String beginTimeS;
    @TableField(exist = false)
    private Date beginTimeD;
    /**
     * 结束时间(前端传的是时间戳需要转换)
     */
    @TableField(exist = false)
    private String endTimeS;
    @TableField(exist = false)
    private Date endTimeD;
    /**
     * 每页显示条数
     */
    @TableField(exist = false)
    private Integer size = 10;
    /**
     * 当前页码
     */
    @TableField(exist = false)
    private Integer current = 1;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
