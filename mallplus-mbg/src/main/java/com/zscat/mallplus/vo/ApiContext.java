package com.zscat.mallplus.vo;

import org.springframework.stereotype.Component;

@Component
public class ApiContext {


    private static ThreadLocal<Long> requestIdThreadLocal = new ThreadLocal<>();

    private static final String KEY_CURRENT_PROVIDER_ID = "KEY_CURRENT_PROVIDER_ID";
   // private static final Map<String, Object> mContext = Maps.newConcurrentMap();

    public Long getCurrentProviderId() {
        return (Long) requestIdThreadLocal.get();
    }

    public void setCurrentProviderId(Long providerId) {
        requestIdThreadLocal.set(providerId);
    }
}
