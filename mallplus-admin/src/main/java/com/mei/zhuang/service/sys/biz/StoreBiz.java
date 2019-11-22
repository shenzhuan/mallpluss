package com.mei.zhuang.service.sys.biz;

import com.mei.zhuang.dao.sys.CrmStoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Meng.Liu1@arvato.com
 * @Date: 2018/11/28 11:10
 * @Description:
 * @version: V1.0
 */
@Service
@Slf4j
public class StoreBiz {

    @Autowired
    private CrmStoreMapper crmStoreMapper;

    /**
     * 查询所有的交易发生渠道/门店
     *
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> getStoreList() {
        return crmStoreMapper.getStoreList();
    }

}
