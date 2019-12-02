package com.zscat.mallplus;

import org.springframework.stereotype.Component;

@Component
public class ApiContext {


    private static ThreadLocal<Integer> requestIdThreadLocal = new ThreadLocal<>();

    private static final String KEY_CURRENT_PROVIDER_ID = "KEY_CURRENT_PROVIDER_ID";
   // private static final Map<String, Object> mContext = Maps.newConcurrentMap();

    public Integer getCurrentProviderId() {
        return (Integer) requestIdThreadLocal.get();
    }

    public void setCurrentProviderId(Integer providerId) {
        requestIdThreadLocal.set(providerId);
    }
}
