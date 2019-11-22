package com.mei.zhuang.util;


import com.mei.zhuang.service.sys.SysLoginLogService;

public enum LoginResultEnum {
    FAIL_NOT_EXIST(0,"用户不存在") {
        @Override
        public void recordLoginLog(String username, SysLoginLogService sysLoginLogService) {
            insertLoginLog(username, sysLoginLogService, this);
        }
    },

    FAIL_PASSWORD_ERROR(1,"密码错误"){
        @Override
        public void recordLoginLog(String username, SysLoginLogService sysLoginLogService) {
            insertLoginLog(username, sysLoginLogService, this);
        }
    },

    FAIL_ACCOUNT_LOCKED(2,"账号已被锁定"){
        @Override
        public void recordLoginLog(String username, SysLoginLogService sysLoginLogService) {
            insertLoginLog(username, sysLoginLogService, this);
        }
    },

    FAIL_NO_AUTH(3,"未分配权限"){
        @Override
        public void recordLoginLog(String username, SysLoginLogService sysLoginLogService) {
            insertLoginLog(username, sysLoginLogService, this);
        }
    },

    SUCCESS(4,"登录成功"){
        @Override
        public void recordLoginLog(String username, SysLoginLogService sysLoginLogService) {
            insertLoginLog(username, sysLoginLogService, this);
        }
    };

    LoginResultEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    int code;
    String value;

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public abstract void recordLoginLog(String username, SysLoginLogService sysLoginLogService);

    private static void insertLoginLog(String username, SysLoginLogService sysLoginLogService, LoginResultEnum loginResultEnum) {
        sysLoginLogService.saveCysLoginLog(loginResultEnum.getCode(),loginResultEnum.getValue(),username);
    }

}
