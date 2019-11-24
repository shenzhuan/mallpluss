package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_start_advertising")
public class EsStartAdvertising extends Model<EsStartAdvertising> {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 广告名称
     */
    @TableField("advert_name")
    private String advertName;
    /**
     * 广告背景
     */
    @TableField("advert_backaground")
    private String advertBackaground;
    /**
     * 显示设置（1.仅显示一次 2.每次显示 3.间隔时间显示）
     */
    @TableField("display_setup")
    private Integer displaySetup;
    /**
     * 显示页面（1.仅首页显示 2.指定页面显示 3.全部页面）
     */
    @TableField("display_page")
    private Integer displayPage;
    /**
     * 指定页面
     */
    @TableField("specified_page")
    private String specifiedPage;
    /**
     *广告图片编号
     */
   /* @TableField("advert_img_id")
    private Long advertImgId;*/
    /**
     * 是否启用（0禁用 1立即启用 2定时启用）
     */
    @TableField("is_start")
    private Integer isStart;
    /**
     *
     */
    @TableField("interval_time")
    private String intervalTime;
    /**
     * 启动时间
     */
    @TableField("start_time")
    private String startTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最后修改时间
     */
    @TableField("last_time")
    private Date lastTime;

    @TableField(exist = false)
    private Integer size = 10;
    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private String advertImg;
    @TableField(exist = false)
    private List<EsStartAdvertisingImg> listAdvertImg;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
