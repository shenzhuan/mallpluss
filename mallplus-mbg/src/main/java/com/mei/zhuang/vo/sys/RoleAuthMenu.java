package com.mei.zhuang.vo.sys;

import lombok.Data;

import java.util.List;

@Data
public class RoleAuthMenu {
    private Integer moduleId;
    private String moduleName;
    private Boolean moduleChecked;
    private Integer subModuleId;
    private String subModuleName;
    private Boolean subModuleChecked;

    private Integer subSubModuleId;
    private String subSubModuleName;
    private Boolean subSubModuleChecked;

    private Integer menuId;
    private String menuName;
    private Boolean menuChecked;
    private List<ButtonNode> buttonList;
    private List<DataAuthNode> dataAuthList;

}
