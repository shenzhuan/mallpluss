package com.mei.zhuang.vo.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(value = {"handler"})
public class AuthMenuNode {

    private String moduleName;
    private String menuName;
    private Integer menuId;
    private Integer roleId;
    private Integer moduleId;
    private boolean menuChecked;
    private boolean moduleChecked;
    private List<ButtonNode> buttonList;
    private List<DataAuthNode> dataAuthList;
}
