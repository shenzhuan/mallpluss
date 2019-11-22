package com.mei.zhuang.vo.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(value = {"handler"})
public class ButtonNode {

    private Integer id;
    private String code;
    private String name;
    private Integer pId;
    private boolean check;
}
