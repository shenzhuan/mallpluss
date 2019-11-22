package com.mei.zhuang.constant;

/**
 * ${DESCRIPTION}
 *
 * @author arvato team
 * @create 2017-06-17 14:41
 */
public class ApiUserConstant {

	/**
	 * 用户加密长度
	 */
	public static int USER_PW_ENCORDER_SALT = 12;

	/**
	 * api用户状态
	 */
	public enum ApiUserStatus{
		Valid("1"),
		InValid("2");
		private String value;
		ApiUserStatus(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
}
