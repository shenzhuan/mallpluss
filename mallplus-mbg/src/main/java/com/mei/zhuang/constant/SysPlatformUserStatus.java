package com.mei.zhuang.constant;

import com.mei.zhuang.vo.DictData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 平台账户状态
 */
public enum SysPlatformUserStatus {
    NORMAL("0","正常"),
    LOCKED("1","锁定");

    private String status;
    private String statusText;

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    SysPlatformUserStatus(String status, String statusText){
        this.setStatus(status);
        this.setStatusText(statusText);
    }

    public static List<DictData> toDictList(){
        List<DictData> list  = new ArrayList<>();
        Arrays.stream(SysPlatformUserStatus.values()).forEach(status -> {
            list.add(
                    new DictData()
                            .setValue(status.getStatus())
                            .setText(status.getStatusText())
            );
        });
        return list;
    }
}
