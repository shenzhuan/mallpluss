package com.mei.zhuang.entity.goods;

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
 * @since 2019-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_diypage_template")
public class EsShopDiypageTemplate extends Model<EsShopDiypageTemplate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 公众号:对应的就是shopId
     */
    private Long uniacid;
    /**
     * 模板类型 1 自定页面 2 商城首页
     */
    private Integer type;
    /**
     * 模板名字
     */
    private String name;
    /**
     * 模板数据
     */
    private String data;
    /**
     * 模板预览图片地址
     */
    private String preview;
    /**
     * 模板ID
     */
    private Long tplid;
    /**
     * 模板分类
     */
    private Long cate;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 多商户id
     */
    private Long merch;
    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
