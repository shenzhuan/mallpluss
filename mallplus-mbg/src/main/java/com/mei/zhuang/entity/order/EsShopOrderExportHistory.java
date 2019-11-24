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
 * <p>
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_order_export_history")
public class EsShopOrderExportHistory extends Model<EsShopOrderExportHistory> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId=1l;
    /**
     * 用户uid
     */
    private Long uid;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 下载次数
     */
    @TableField("download_count")
    private Long downloadCount;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件路径
     */
    private String filepath;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 条件
     */
    private String condition;
    /**
     * 计数
     */
    private Long count;
    /**
     * 终止时间
     */
    @TableField("finish_time")
    private Date finishTime;
    /**
     * 真实的计数
     */
    @TableField("real_count")
    private Integer realCount;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
