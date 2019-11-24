package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField; import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/8/26 15:47
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_share_member")
public class EsShopShareMember {

    @TableId(value = "id", type = IdType.AUTO)     private Long id;
    @TableField("share_id")
    private Long shareId;//活动id
    @TableField("launch_member_id")
    private Long launchMemberId;//发起人
    @TableField("create_time")
    private Date createTime;//创建时间
    @TableField("share_num")
    private Integer shareNum;//分享次数
    @TableField("is_close")
    private Integer isClose;//1任务完成 0没完成
    @TableField("is_confirm")
    private Integer isConfirm;//发起者是否确认奖品0确认1没确认
}
