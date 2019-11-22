package com.mei.zhuang.constant;

import com.mei.zhuang.vo.DictData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 租户状态
 */
public enum SysTenantStatus {
    ENABLE("0","启用"),
    DISABLE("1","禁用");

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

    SysTenantStatus(String status,String statusText){
        this.setStatus(status);
        this.setStatusText(statusText);
    }

    public static List<DictData> toDictList(){
        List<DictData> list  = new ArrayList<>();
        Arrays.stream(SysTenantStatus.values()).forEach(status -> {
            list.add(
                    new DictData()
                            .setValue(status.getStatus())
                            .setText(status.getStatusText())
            );
        });
        return list;
    }
}
