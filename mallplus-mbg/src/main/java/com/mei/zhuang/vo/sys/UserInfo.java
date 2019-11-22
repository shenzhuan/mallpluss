package com.mei.zhuang.vo.sys;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 4969701793450048362L;

    public String id;
    public String username;
    public String password;
    public String name;
    public String description;
    public Date updTime;
    public Integer loginErrorCount;
    public String status;
    private String birthday;
    private String address;
    private String mobile;
    private String email;
    private String sex;
    private String schema;

}
