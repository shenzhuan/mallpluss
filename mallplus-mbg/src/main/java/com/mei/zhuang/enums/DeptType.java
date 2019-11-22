package com.mei.zhuang.enums;

/**
 * 用来存放订单积分计算类别
 * @author Zhan597
 *
 */
public enum DeptType {

	HOME(-1),
	AREA(0),
	REGION(1),
	AGENT(2),
	STORE(3);

	private Integer value;

	DeptType(Integer value){
		this.value=value;
	}

	public Integer getValue(){
		return this.value;
	};
}
