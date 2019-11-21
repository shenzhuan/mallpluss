package com.mei.zhuang.entity.goods;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Upload {
    private String visitPath;
    private String savePath;
    private String midPath;
    private String pathFileName;
    private String filename;
}
