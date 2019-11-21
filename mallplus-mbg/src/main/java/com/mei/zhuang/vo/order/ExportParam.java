package com.mei.zhuang.vo.order;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-05-08 14:21
 * @Description:
 */
@Data
public class ExportParam {

    private String fileName;//文件名

    private String sheetName;//工作部名

    private String path;//路径

    private String headers;//要导出的名字

    private String columns;//要导出的字段

}
