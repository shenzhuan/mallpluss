package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("es_shop_diypage_template_category")
public class EsShopDiypageTemplateCategory extends Model<EsShopDiypageTemplateCategory> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 公众号:对应的就是shopId
     */
    private Long uniacid;
    /**
     * 分类名
     */
    private String name;
    /**
     * 多商户id
     */
    private Long merch;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
