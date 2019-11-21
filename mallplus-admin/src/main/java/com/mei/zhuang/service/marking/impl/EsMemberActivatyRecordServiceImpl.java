package com.mei.zhuang.service.marking.impl;

import com.arvato.service.marking.api.orm.dao.EsMemberActivatyRecordMapper;
import com.arvato.service.marking.api.orm.dao.EsShopActivityPrizeMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCouponMapper;
import com.arvato.service.marking.api.service.EsMemberActivatyRecordService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsMemberActivatyRecord;
import com.mei.zhuang.entity.marking.EsShopActivityPrize;
import com.mei.zhuang.entity.marking.EsShopCoupon;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsMemberActivatyRecordServiceImpl extends ServiceImpl<EsMemberActivatyRecordMapper, EsMemberActivatyRecord> implements EsMemberActivatyRecordService {

    @Resource
    private EsMemberActivatyRecordMapper esMemberActivatyRecordMapper;
    @Resource
    private EsShopActivityPrizeMapper esShopActivityPrizeMapper;
    @Resource
    private EsShopCouponMapper esShopCouponMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Map<String, Object> selPageList(EsMemberActivatyRecord entity) {

        /**
         * 时间戳转Date
         */
        if(entity.getBeginTimeS() != null && !entity.getBeginTimeS().equals("")){
            long lt = new Long(entity.getBeginTimeS());
            entity.setBeginTimeD( new Date(lt));
        }
        if(entity.getEndTimeS() != null && !entity.getEndTimeS().equals("")){
            long lt = new Long(entity.getEndTimeS());
            entity.setEndTimeD( new Date(lt));
        }
        Map<String,Object> map = new HashMap<String,Object>();
        Page<EsMemberActivatyRecord> page = this.selectPage(new Page<EsMemberActivatyRecord>(entity.getCurrent(), entity.getSize()), new QueryWrapper<>(entity));
        List<EsMemberActivatyRecord> selPageList = esMemberActivatyRecordMapper.selPageList(page,entity);
        if(selPageList != null && selPageList.size()>0){
            for (EsMemberActivatyRecord record:selPageList) {
                if(record.getIsWin() == 1){
                    //查询奖品
                    EsShopActivityPrize prize =  esShopActivityPrizeMapper.selectById(record.getPrizeId());
                    if(prize != null){
                        EsShopCoupon coupon = esShopCouponMapper.selectById(Long.parseLong(prize.getGoodsContent()));
                        record.setPrizeName(coupon.getCouponsName());
                    }

                }
            }
        }

        Integer count = esMemberActivatyRecordMapper.count(entity);
        map.put("rows", selPageList);
        map.put("total", count);
        map.put("current", entity.getCurrent());
        map.put("size", entity.getSize());
        return map;
    }
}
