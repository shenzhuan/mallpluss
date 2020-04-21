package com.zscat.mallplus.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 首页推荐专题表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Setter
@Getter
@TableName("sms_home_recommend_subject")
public class SmsHomeRecommendSubject extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("subject_id")
    private Long subjectId;

    @TableField("subject_name")
    private String subjectName;

    @TableField("recommend_status")
    private Integer recommendStatus;

    private Integer sort;

    private String pic;

    /**
     * 点击量
     */
    @TableField("read_count")
    private Integer readCount;

    /**
     * 描述
     */
    private String description;
}
