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
 * @author arvato team
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_member_import_record")
public class EsMemberImportRecord extends Model<EsMemberImportRecord> {

    private static final long serialVersionUID = 1L;

    private Long status;
    @TableField("success_number")
    private Integer successNumber;
    @TableField("tag_ids")
    private String tagIds;
    private Integer amount;
    @TableField("create_time")
    private Date createTime;
    @TableField("file_path")
    private String filePath;
    private String reason;
    @TableField("shop_id")
    private Integer shopId;
    @TableField("verify_mobile")
    private Long verifyMobile;
    @TableField("fail_file_path")
    private String failFilePath;
    @TableField("fail_number")
    private Integer failNumber;
    private Integer id;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
