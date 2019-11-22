package com.mei.zhuang.constant;

/**
 * ${DESCRIPTION}
 *
 * @author arvato team
 * @create 2017-06-17 14:41
 */
public class ComConstant {

	/** 数据状态无效 */
	public final static Integer STATE_INVALID = 0;

	/** 数据状态有效 */
	public final static Integer STATE_VALID = 1;

	/** 消息状态-初始化 */
	public final static Integer MESSAGE_INIT = 0;

	/** 消息状态-发送成功 */
	public final static Integer MESSAGE_SUCCESS = 1;

	/** 消息状态-被拦截 */
	public final static Integer MESSAGE_INTERCEPT = 2;

	/** 消息状态-发送失败 */
	public final static Integer MESSAGE_FAIL = 3;

	/** 消息类型-促销 */
	public final static Integer CAMPAIGN_SALES_PROMOTION = 1;

	/** 消息类型-消息群发 */
	public final static Integer CAMPAIGN_MASSMESSAGE = 3;

}
