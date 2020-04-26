package com.zscat.mallplus.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/19 02:16
 * @Description:
 */
@Setter
@Getter
public class BaseEntity implements Serializable {
    //部署admin的时候 删除
    //部署portal的时候 解除
    @TableField("store_id")
    private Integer storeId;


}
