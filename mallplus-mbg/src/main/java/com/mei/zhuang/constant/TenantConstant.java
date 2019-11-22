package com.mei.zhuang.constant;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/17
 */
public class TenantConstant {
    /**
     * 租户状态
     */
    public enum TenantStatus{
        Valid("0"),
        InValid("1");
        private String value;
        TenantStatus(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}
