package com.mei.zhuang.entity.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/8/27 17:57
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_share_member_assistance")
public class EsShopShareMemberAssistance {

    private Long id;
    @TableField("share_member_id")
    private Long shareMemberId;//发起者
    @TableField("assis_member_id")
    private Long assisMemberId;//助力者
}
