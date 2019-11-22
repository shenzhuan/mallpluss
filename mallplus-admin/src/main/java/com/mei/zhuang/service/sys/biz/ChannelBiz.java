package com.mei.zhuang.service.sys.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.sys.CrmChannelMapper;
import com.mei.zhuang.entity.sys.CrmChannel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Meng.Liu1@arvato.com
 * @Date: 2018/12/5 15:01
 * @Description: ChannelBiz
 * @version: V1.0
 */
@Service
public class ChannelBiz {

    @Resource
    private CrmChannelMapper crmChannelMapper;

    /**
     * 获取入会渠道下拉列表
     *
     * @return List<Object>
     */
    public List<CrmChannel> getChannelList() {
        return crmChannelMapper.selectList(new QueryWrapper<>(new CrmChannel()));
    }
}
