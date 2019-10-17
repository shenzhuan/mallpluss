package com.zscat.mallplus.sms.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 一分钱抽奖
 * </p>
 *
 * @author zscat
 * @since 2019-10-17
 */
@TableName("sms_draw")
public class SmsDraw implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 该抽奖所参与商品ID
     */
    @TableField("good_id")
    private Long goodId;

    /**
     * 创建时间
     */
    @TableField("found_time")
    private LocalDateTime foundTime;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 每个团所需人数
     */
    private Integer num;

    /**
     * 可抽中奖次数（默认为1）
     */
    @TableField("spelling_number")
    private Integer spellingNumber;

    /**
     * 最少开奖团数（默认为1）
     */
    @TableField("collage_number")
    private Integer collageNumber;

    /**
     * 该团的状态（默认为0：未开始 ，1：进行中 ， 2：已结束）
     */
    private Integer state;

    /**
     * 抽奖金额
     */
    private BigDecimal price;

    /**
     * 次数 
     */
    private Integer cishu;

    /**
     * 备注
     */
    private String type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public LocalDateTime getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(LocalDateTime foundTime) {
        this.foundTime = foundTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSpellingNumber() {
        return spellingNumber;
    }

    public void setSpellingNumber(Integer spellingNumber) {
        this.spellingNumber = spellingNumber;
    }

    public Integer getCollageNumber() {
        return collageNumber;
    }

    public void setCollageNumber(Integer collageNumber) {
        this.collageNumber = collageNumber;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCishu() {
        return cishu;
    }

    public void setCishu(Integer cishu) {
        this.cishu = cishu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SmsDraw{" +
        ", id=" + id +
        ", name=" + name +
        ", goodId=" + goodId +
        ", foundTime=" + foundTime +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", num=" + num +
        ", spellingNumber=" + spellingNumber +
        ", collageNumber=" + collageNumber +
        ", state=" + state +
        ", price=" + price +
        ", cishu=" + cishu +
        ", type=" + type +
        "}";
    }
}
