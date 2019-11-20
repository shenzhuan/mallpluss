package com.zscat.mallplus;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApiContext {


    private static final String KEY_CURRENT_PROVIDER_ID = "KEY_CURRENT_PROVIDER_ID";
    private static final Map<String, Object> mContext = Maps.newConcurrentMap();

    public Integer getCurrentProviderId() {
        return (Integer) mContext.get(KEY_CURRENT_PROVIDER_ID);
    }

    public void setCurrentProviderId(Integer providerId) {
        mContext.put(KEY_CURRENT_PROVIDER_ID, providerId);
    }
}
