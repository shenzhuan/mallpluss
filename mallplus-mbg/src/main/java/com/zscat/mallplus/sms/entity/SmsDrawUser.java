package com.zscat.mallplus.sms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 抽奖与用户关联表
 * </p>
 *
 * @author zscat
 * @since 2019-10-17
 */
@TableName("sms_draw_user")
public class SmsDrawUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 拼团ID
     */
    @TableField("draw_id")
    private Long drawId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户参团时间
     */
    private LocalDateTime time;

    /**
     * 用户角色（默认 0：团长  userid:该用户分享进来的用户）
     */
    private String role;

    /**
     * 抽奖状态（0.参团中 1.待抽奖 2.参团失败 3.抽奖失败 4.抽奖成功）
     */
    @TableField("lottery_status")
    private Integer lotteryStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDrawId() {
        return drawId;
    }

    public void setDrawId(Long drawId) {
        this.drawId = drawId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getLotteryStatus() {
        return lotteryStatus;
    }

    public void setLotteryStatus(Integer lotteryStatus) {
        this.lotteryStatus = lotteryStatus;
    }

    @Override
    public String toString() {
        return "SmsDrawUser{" +
        ", id=" + id +
        ", drawId=" + drawId +
        ", userId=" + userId +
        ", time=" + time +
        ", role=" + role +
        ", lotteryStatus=" + lotteryStatus +
        "}";
    }
}
