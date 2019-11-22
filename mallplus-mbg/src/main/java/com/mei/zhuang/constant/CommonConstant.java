package com.mei.zhuang.constant;


/**
 * ${DESCRIPTION}
 * 通用Constant
 *
 * @author meizhuang team
 * @create 2017-06-17 14:41
 */
public class CommonConstant {

    /**************接口返回状态start****************/
    public final static int CODE_SUCCESS = 0;
    public final static int CODE_BIZ_ERROR = 1;
    public final static int CODE_SYS_ERROR = 2;
    public final static int CODE_RPC_ERROR = 3;
    /**************接口返回状态end****************/

    /********************用户token异常************************/
    public static final Integer EX_TOKEN_ERROR_CODE = 40101;
    public static final Integer EX_USER_INVALID_CODE = 40102;

    /********************客户端token异常**********************/
    public static final Integer EX_CLIENT_INVALID_CODE = 40131;
    public static final Integer EX_CLIENT_FORBIDDEN_CODE = 40331;
    public static final Integer EX_OTHER_CODE = 500;

    public static final int PAGENUM = 1200;
    public final static int ROOT = -1;
    public final static int DEFAULT_GROUP_TYPE = 0;

    public static final String CONTEXT_KEY_USER_ID = "currentUserId";
    public static final String CONTEXT_KEY_USERNAME = "currentUserName";
    public static final String CONTEXT_KEY_USER_NAME = "currentUser";
    public static final String CONTEXT_KEY_USER_TOKEN = "currentUserToken";
    public static final String CONTEXT_KEY_THIRD_TOKEN = "currentThirdUserToken";
    public static final String JWT_KEY_USER_ID = "userId";
    public static final String JWT_KEY_NAME = "name";

    /****************CsvWriter 工具***************/
    public static final char EXPROT_DATA_DELIMITER = ',';

    public final static String MSG_SUCCESS = "操作成功";
    public final static String MSG_RPC_ERROR = "远程服务调用失败";
    public final static String MSG_SYS_ERROR = "当前操作不可用，请退出重试，如问题仍然存在，请联系系统管理员！";
    public final static String MSG_DATA_BEEN_UPDATED = "数据已被他人更新，请刷新重试！";
    public final static String MSG_DATA_BEEN_DELETED = "数据已被他人删除，请刷新重试！";

    public final static String REQUEST_MAPPING_INIT = "/init";
    public final static String REQUEST_MAPPING_QUERY = "/query";
    public final static String REQUEST_MAPPING_DETAIL = "/detail";
    public final static String REQUEST_MAPPING_SAVE = "/save";
    public final static String REQUEST_MAPPING_UPDATE = "/update";
    public final static String REQUEST_MAPPING_DELETE = "/delete";

    /******************request header**************/
    public final static String REQUEST_HEADER_MENU_ID = "menuId";

    /**
     * 客服细分动态表前缀
     */
    public static final String CRM_CAMPAIGN_SEGMENT_RESULT_ = "crm_campaign_segment_result_";

    /***
     * 活动人群表前缀
     */
    public static final String CRM_CAMPAIGN_TARGET = "crm_campaign_target_";
    /**
     * 查询时活动细分人群表别名，统一使用
     */
    public static final String CAMPAIN_SEGMENT_RESULT_ALIAS = "cResultAlias";

    /**
     * 营销类型，活动
     */
    public static final int MARKET_TYPE_CAMPAIGN = 1;
    /**
     * 租户信息,当前租户id
     */
    public final static String CURR_TENANT_ID = "tenantId";
    /**
     * 租户数据库信息
     */
    public final static String TENANT_DB_NAME = "dbName";
    /**
     * 租户schemal信息
     */
    public final static String TENANT_SCHEMA = "dbschema";
    public static String MSG_SAVE_SUCCESS = "保存成功";
    public static String MSG_SAVE_ERROR = "保存失败";
    public static String MSG_SAVE_ERROR_NAME_REPEAT = "保存失败,模板名称重复";
    public static String MSG_DELETE_SUCCESS = "删除成功";
    public static String MSG_DELETE_ERROR = "删除失败";
    /**
     * 消息模板替换-》请求来源,bpm,event,message 都模块使用到
     */
    public enum MessageTemplateReplaceRequestOrigin {
        /**
         * 活动
         */
        CAMPAIGN("campaign"),
        /**
         * 事件
         */
        EVENT("event");
        private String value;

        MessageTemplateReplaceRequestOrigin(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    /**
     * 用户媒体类型
     */
    public enum MemberMediaAccountType {
        /**
         * 支付宝生活号
         */
        ALIPAY_NO("1"),
        /**
         * 手淘
         */
        TAOBAO("2"),
        /**
         * 微信
         */
        WECHAT("3"),
        /**
         * QQ
         */
        QQ("4"),
        /**
         * 微博
         */
        MICROBLOG("5");
        private String value;

        MemberMediaAccountType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 消息分类
     */
    public enum MessageType {
        /**
         * 短信
         */
        SMS("sms"),
        /**
         * 邮件
         */
        EMAIL("email"),
        /**
         * 微信
         */
        WECHAT("wechat");
        private String value;

        MessageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    /**
     * 短信渠道
     */
    public enum SmsSendChannel {
        /**
         * 短信通
         */
        ARVATO_TEST(1),
        /**
         * 欧莱雅测试渠道
         */
        LOREAL_TEST(2);
        private Integer value;

        SmsSendChannel(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 邮件渠道
     */
    public enum EmailSendChannel {
        /**
         * 通用渠道
         */
        Arvato_CHANNEL(1),
        Splio_Channel(2);
        private Integer value;

        EmailSendChannel(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
    /**
     * 活动时间波次执行类型
     */
    public enum CampaignScheduleExecuteType {
        /**
         * 单次执行
         */
        ONCE("once"),
        /**
         * 波次执行
         */
        WAVE("wave"),
        /**
         * 周期执行
         */
        CYCLE("cycle");
        private String value;

        CampaignScheduleExecuteType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    /**
     * 活动时间波次周期执行频次
     */
    public enum CampaignScheduleFrequency {
        /**
         * 按月
         */
        MONTHLY("Monthly"),
        /**
         * 按周
         */
        WEEKLY("Weekly"),
        /**
         * 按天
         */
        DAILY("Daily");
        private String value;

        CampaignScheduleFrequency(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
