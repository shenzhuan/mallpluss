package com.mei.zhuang.vo.sys;

import lombok.Data;

@Data
public class AuthMenuVo {
    private Integer menuId;
    private String menuName;
    private Integer menuPid;
    private Integer menuType;
    private Boolean isChecked;
}
