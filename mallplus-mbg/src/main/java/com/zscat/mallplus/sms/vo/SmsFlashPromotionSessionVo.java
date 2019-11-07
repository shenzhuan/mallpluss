package com.zscat.mallplus.sms.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.sms.entity.SmsFlashPromotionSession;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 限时购场次表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Data
public class SmsFlashPromotionSessionVo  implements Serializable {

   private String lovely;
   private Integer seckillTimeIndex;
    private List<SmsFlashPromotionSession> seckillTime;

}
