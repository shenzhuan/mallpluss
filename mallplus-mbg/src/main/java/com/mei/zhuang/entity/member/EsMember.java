package com.mei.zhuang.entity.member;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("es_member")
public class EsMember extends Model<EsMember> {

    private static final long serialVersionUID = 1L;
    //编号
    @TableField("tail_number")
    private String tailNumber;
    //成交次数
    @TableField("buy_count")
    private Integer buyCount;
    //成交金额
    @TableField("buy_money")
    private BigDecimal buyMoney;
    private Long followed;
    //手机号
    private String mobile;
    //省
    private String province;
    //信用
    private Integer credit;
    //邮箱
    private String email;
    //特权
    private String privilege;
    //真实姓名
    private String realname;
    //密码
    private String password;
    //生日
    private Date birthday;
    //来自来源渠道
    @TableField("come_from")
    private Integer comeFrom;
    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    //邀请
    private Integer inviter;
    @TableField("openid_app")
    private String openidApp;
    @TableField("create_time")
    private Date createTime;
    //昵称
    private String nickname;
    //风趣
    private String salt;
    //微信
    private String wechat;
    //星座
    private Long constellation;
    //等级
    @TableField("level_id")
    private Integer levelId;
    //备注
    private String remark;
    //性别
    private String sex;
    private Integer uid;
    //年龄
    private Integer age;
    //头像
    private String avatar;
    //城市
    private String city;
    private String unionid;
    //区域
    private String area;
    //余额
    private BigDecimal balance;
    // 1 正常 2 黑名单
    @TableField("is_black")
    private Integer isBlack;
    @TableField("openid_wxapp")
    private String openidWxapp;
    //移动电话验证 是否绑定
    @TableField("mobile_verified")
    private Integer mobileVerified;
    private String openid;
    @TableField("shop_id")
    private Long shopId=1l;
    //开卡号
    @TableField("open_card")
    private String openCard;
    //是否绑定
    private Integer binding;
    //用户积分
    private Long integral;
    @TableField(exist = false)
    private Integer size;
    @TableField(exist = false)
    private Integer current;
    @TableField(exist = false)
    private String mnames;
    @TableField(exist = false)
    private String formid;
    @TableField(exist = false)
    private Long ids;
    @TableField(exist = false)
    private String date;
    @TableField(exist = false)
    private String confimpassword;

    @TableField(exist = false)
    private String phonecode;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
