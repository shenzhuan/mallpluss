package com.mei.zhuang.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.dao.marking.EsMemberActivatyRecordMapper;
import com.mei.zhuang.dao.marking.EsShopActivityMapper;
import com.mei.zhuang.dao.marking.EsShopActivityPrizeMapper;
import com.mei.zhuang.entity.marking.EsMemberActivatyRecord;
import com.mei.zhuang.entity.marking.EsShopActivity;
import com.mei.zhuang.entity.marking.EsShopActivityPrize;
import com.mei.zhuang.service.marking.EsShopActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsShopActivityServiceImpl extends ServiceImpl<EsShopActivityMapper, EsShopActivity> implements EsShopActivityService {

    @Resource
    private EsShopActivityMapper esShopActivityMapper;
    @Resource
    private EsShopActivityPrizeMapper esShopActivityPrizeMapper;
    @Resource
    private EsMemberActivatyRecordMapper esMemberActivatyRecordMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    @Override
    public Map<String, Object> selPageList(EsShopActivity entity) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageHelper.startPage(entity.getCurrent(), entity.getSize());
        List<EsShopActivity> list = esShopActivityMapper.selPageList(entity);
        EsMemberActivatyRecord esMemberActivatyRecord = new EsMemberActivatyRecord();

        if (list != null) {
            for (EsShopActivity activity : list) {
                try {

                    if (entity.getCurrentTime() >= activity.getActivityStartTime()) {
                        if (entity.getCurrentTime() > activity.getActivityEndTime()) {
                            activity.setStatus(3);
                        } else {
                            activity.setStatus(1);
                        }
                    } else {
                        activity.setStatus(2);
                    }

                    esMemberActivatyRecord.setActivatyId(activity.getId());
                    List<EsMemberActivatyRecord> listRecord = esMemberActivatyRecordMapper.distinctMember(activity.getId());
                    activity.setTotalNumber(listRecord.size());//参与总人数
                    List<EsMemberActivatyRecord> listSize = esMemberActivatyRecordMapper.selectList(new QueryWrapper<>(esMemberActivatyRecord));
                    activity.setTotalFrequency(listSize.size());//参与总次数
                    Integer num1 = 0;//中奖人数
                    Integer num2 = 0;//未中奖人数
                    for (EsMemberActivatyRecord record : listSize
                    ) {
                        if (record.getIsWin() == 0) {
                            num2 += 1;
                        } else {
                            num1 += 1;
                        }
                    }
                    activity.setWinPrizeNumber(num1);//中奖人数
                    activity.setNoWinPrizeNumber(num2);//未中奖人数
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        Integer count = esShopActivityMapper.count(entity);
        map.put("rows", list);
        map.put("total", count);
        map.put("current", entity.getCurrent());

        map.put("size", entity.getSize());
        return map;
    }

    @Override
    public boolean save(EsShopActivity entity) {


        Integer num = 100;
        if (entity.getList() != null && entity.getList().size() > 0) {
            for (EsShopActivityPrize es : entity.getList()) {
                if (es != null) {
                    es.setActivatyId(entity.getId());
                    num -= es.getWinning();
                    esShopActivityPrizeMapper.insert(es);
                }
            }
            if (num < 0) {
                num = 0;
            }
            entity.setWinRate(num);
            this.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean updates(EsShopActivity entity) {
        if (this.updateById(entity)) {
            EsShopActivityPrize prize = new EsShopActivityPrize();
            prize.setActivatyId(entity.getId());
            List<EsShopActivityPrize> list = esShopActivityPrizeMapper.selectList(new QueryWrapper<>(prize));
            for (EsShopActivityPrize es : list) {
                esShopActivityPrizeMapper.deleteById(es.getId());
            }
            if (entity.getList() != null && entity.getList().size() > 0) {
                for (EsShopActivityPrize es : entity.getList()) {
                    es.setActivatyId(entity.getId());
                    esShopActivityPrizeMapper.insert(es);
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean deletes(Long id) {
        if (this.removeById(id)) {
            EsShopActivityPrize prize = new EsShopActivityPrize();
            prize.setActivatyId(id);
            esShopActivityPrizeMapper.delete(new QueryWrapper<>(prize));
        }
        return false;
    }

    @Override
    public EsShopActivity detail(Long id) {
        EsShopActivity activity = esShopActivityMapper.selectById(id);
        if (activity != null) {
            EsShopActivityPrize prize = new EsShopActivityPrize();
            prize.setActivatyId(activity.getId());
            List<EsShopActivityPrize> list = esShopActivityPrizeMapper.selectList(new QueryWrapper<>(prize));
            activity.setList(list);
        }
        return activity;
    }


}
