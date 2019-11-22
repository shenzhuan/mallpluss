package com.mei.zhuang.entity.member;

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
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_import_map")
public class EsMemberImportMap extends Model<EsMemberImportMap> {

    private static final long serialVersionUID = 1L;

    @TableField("create_time")
    private Date createTime;
    private Integer id;
    @TableField("import_id")
    private Integer importId;
    @TableField("member_id")
    private String memberId;
    @TableField("shop_id")
    private Integer shopId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
