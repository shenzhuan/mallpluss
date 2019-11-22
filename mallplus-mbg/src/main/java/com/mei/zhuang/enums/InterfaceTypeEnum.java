package com.mei.zhuang.enums;

/**
 * ${DESCRIPTION}
 *
 * @author arvato team
 * @create 2017-06-14 8:36
 */
public enum InterfaceTypeEnum {

	//天猫会员信息同步接口
	TMALL_MEMBER_SYNC("TMALL_MEMBER_SYNC")
	;


	private  InterfaceTypeEnum(String ret) {
		this.ret=ret;
	}

	private String ret;

	public String  getValue(){
		return ret;
	}

}
