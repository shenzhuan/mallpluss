package com.zscat.mallplus;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

@Component
public class ApiContext {

    private static ThreadLocal<ConcurrentMap> requestIdThreadLocal = new ThreadLocal<>();

    private static final String KEY_CURRENT_PROVIDER_ID = "KEY_CURRENT_PROVIDER_ID";
    // private static final Map<String, Object> mContext = Maps.newConcurrentMap();

    public Integer getCurrentProviderId() {
        ConcurrentMap map=requestIdThreadLocal.get();
        if (map!=null){
            return 0;
        }
        return (Integer) map.get(KEY_CURRENT_PROVIDER_ID);
    }

    public void setCurrentProviderId(Integer providerId) {
        ConcurrentMap map=requestIdThreadLocal.get();
        if (map==null){
            map= Maps.newConcurrentMap();
        }
        map.put(KEY_CURRENT_PROVIDER_ID,providerId);
        requestIdThreadLocal.set(map);
    }

}
