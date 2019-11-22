package com.mei.zhuang.vo.sys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(value = {"handler"})
public class DataAuthNode {

    private String value;
    private String text;
    private boolean check;
}
