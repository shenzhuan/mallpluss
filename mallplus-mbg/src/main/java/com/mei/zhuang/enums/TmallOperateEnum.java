package com.mei.zhuang.enums;

/**
 * ${DESCRIPTION}
 *
 * @author arvato team
 * @create 2017-06-14 8:36
 */
public enum TmallOperateEnum {

	//绑定
	BIND_OPERATE_BINDING("1"),
	//解绑
	BIND_OPERATE_UNBINDING("2"),

	;


	private  TmallOperateEnum(String ret) {
		this.ret=ret;
	}

	private String ret;

	public String  getValue(){
		return ret;
	}
}
