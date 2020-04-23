package com.zscat.mallplus.sys.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
* @author mallplus
* @date 2020-04-22
商户分类
*/
@Data
@TableName("sys_store_class")
public class SysStoreClass extends BaseEntity implements Serializable {


            @TableId(value = "id", type = IdType.AUTO)
        private Long id;


            /**
            名称
            **/
            @TableField( "name")
        private String name;


            /**
            创建时间
            **/
            @TableField( "create_time")
        private Date createTime;


            /**
            图片
            **/
            @TableField( "pic")
        private String pic;


            /**
            排序
            **/
            @TableField( "sort")
        private Integer sort;


            /**
            备注
            **/
            @TableField( "memo")
        private String memo;




}
