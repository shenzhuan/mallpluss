package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_decorate_template")
public class EsDecorateTemplate extends Model<EsDecorateTemplate> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    @TableField("system_id")
    private Long systemId;
    @TableField("create_time")
    private Date createTime;
    @TableField("page_count")
    private String pageCount;
    @TableField("release_time")
    private Date releaseTime;
    @TableField("shop_id")
    private Long shopId;
    /**
     * 1 保存 2 发布 3 禁用
     */
    private Long status;
    private String thumb;
    @TableField("update_time")
    private Date updateTime;
    private String common;
    /**
     * 1 首页 2商品详情 3会员中心 4 自定义
     */
    private Integer type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
