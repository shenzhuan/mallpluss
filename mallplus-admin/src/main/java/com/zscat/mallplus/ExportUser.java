package com.zscat.mallplus;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.aliyun.oss.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportUser {
    @Excel(name = "用户名", width = 10)
    private String username;

    @Excel(name = "创建时间", format = "yyyy-MM-dd", width = 15)
    private Date createTime;



}

