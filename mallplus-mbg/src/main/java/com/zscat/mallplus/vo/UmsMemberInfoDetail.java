package com.zscat.mallplus.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.sms.entity.SmsCouponHistory;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Data
public class UmsMemberInfoDetail  implements Serializable {

   private UmsMember member;
    private List<SmsCouponHistory> histories ;
}
