package com.mei.zhuang.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author meizhuang team
 * @since 2017-12-22
 */
@ApiModel("接口用户信息")
@Data
@Accessors(chain = true)
@TableName("crm_api_user")
public class CrmApiUser extends Model<CrmApiUser> {

    private static final long serialVersionUID = 1L;

     @TableId(value = "id", type = IdType.AUTO)     private Integer id;
    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String account;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    @TableField("firm_name")
    private String firmName;

    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态")
    private String status;

    /**
     * 第三方唯一标识号
     */
    @ApiModelProperty("第三方唯一标识号")
    @TableField("api_key")
    private String apiKey;

//	/**
//	 * 根据第三方唯一标识号生成的私钥
//	 */
//	@ApiModelProperty("根据第三方唯一标识号生成的私钥")
//	@TableField("private_key")
//	private String privateKey;

    /**
     * 创建人
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建日期
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private String createDate;
    /**
     * 更新人
     */
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    private Integer updateUserId;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 更新日期
     */
    @TableField(value = "update_date", fill = FieldFill.UPDATE)
    private String updateDate;
    /**
     * 乐观锁用
     */
    @Version
    private Integer version;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
