package com.zscat.mallplus.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户标签表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Setter
@Getter
@TableName("ums_member_tag")
public class UmsMemberTag extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String pic;
    /**
     * 1会员标签 2 商品标签 3 文章标签
     */
    private Integer type;

    private Integer status;
    /**
     * 1自动标签 2 手动标签
     */
    @TableField("gen_type")
    private Integer genType;

    @TableField("create_time")
    private Date createTime;


}
