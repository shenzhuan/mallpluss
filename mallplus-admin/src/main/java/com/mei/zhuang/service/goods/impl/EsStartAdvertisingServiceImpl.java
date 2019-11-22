package com.mei.zhuang.service.goods.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.dao.goods.EsStartAdvertisingImgMapper;
import com.mei.zhuang.dao.goods.EsStartAdvertisingMapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsStartAdvertising;
import com.mei.zhuang.entity.goods.EsStartAdvertisingImg;
import com.mei.zhuang.service.goods.EsStartAdvertisingService;
import com.mei.zhuang.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EsStartAdvertisingServiceImpl extends ServiceImpl<EsStartAdvertisingMapper, EsStartAdvertising> implements EsStartAdvertisingService {

    @Resource
    private EsStartAdvertisingMapper esStartAdvertisingMapper;
    @Resource
    private EsStartAdvertisingImgMapper esStartAdvertisingImgMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object select(EsStartAdvertising entity) {
        PageHelper.startPage(entity.getCurrent(), entity.getSize());
        return new CommonResult().success(PageInfo.of(esStartAdvertisingMapper.selectList(new QueryWrapper<EsStartAdvertising>())));

    }

    @Override
    public boolean save(EsStartAdvertising entity) {
        try{

            String time=sdf.format(new Date());
            entity.setCreateTime(sdf.parse(time));
           Integer bool= esStartAdvertisingMapper.insert(entity);

            EsStartAdvertising es=esStartAdvertisingMapper.selectOne(new QueryWrapper<>(entity));
            if(es!=null){
                if(entity.getListAdvertImg()!=null&&entity.getListAdvertImg().size()>0){
                    for (EsStartAdvertisingImg es1:entity.getListAdvertImg()) {
                        es1.setAdvertisingId(es.getId());
                        esStartAdvertisingImgMapper.insert(es1);
                    }
                }
            }
            return bool>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAdvert(EsStartAdvertising entity) {
        try{
            String time=sdf.format(new Date());
            entity.setLastTime(sdf.parse(time));
            boolean bool=this.updateById(entity);
            if(bool){
                if(entity.getAdvertImg()!=null&&!entity.getAdvertImg().equals("")){
                    esStartAdvertisingImgMapper.delete(new QueryWrapper<EsStartAdvertisingImg>().eq("advertising_id", entity.getId()));
                }
                List<EsStartAdvertisingImg> list= JSON.parseArray(entity.getAdvertImg(),EsStartAdvertisingImg.class);
                if(list!=null&&list.size()>0){
                    for (EsStartAdvertisingImg img:list) {
                        img.setAdvertisingId(entity.getId());
                        esStartAdvertisingImgMapper.insert(img);
                    }
                }

            }
            return bool;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Integer countStatus(Long id) {
        return esStartAdvertisingMapper.countStatus(id);
    }

}
