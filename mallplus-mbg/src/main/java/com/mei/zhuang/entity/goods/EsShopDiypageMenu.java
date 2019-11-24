package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("es_shop_diypage_menu")
public class EsShopDiypageMenu extends Model<EsShopDiypageMenu> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    /**
     * 公众号:对应的就是shopId
     */
    private Long uniacid;
    /**
     * 名字
     */
    private String name;
    /**
     * 数据
     */
    private String data;
    /**
     * 建立时间
     */
    private Date createtime;
    /**
     * 最后编辑时间
     */
    private Date lasteditime;
    /**
     * 多商户id
     */
    private Long merch;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
