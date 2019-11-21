package com.mei.zhuang.service.goods.impl;

import com.alibaba.fastjson.JSON;
import com.arvato.service.goods.api.orm.dao.EsStartAdvertisingImgMapper;
import com.arvato.service.goods.api.orm.dao.EsStartAdvertisingMapper;
import com.arvato.service.goods.api.service.EsStartAdvertisingService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsStartAdvertising;
import com.mei.zhuang.entity.goods.EsStartAdvertisingImg;
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
    public Map<String,Object> select(EsStartAdvertising entity) {
        Map<String,Object> map = new HashMap<String,Object>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        try{
            List<EsStartAdvertising> list=esStartAdvertisingMapper.select(page,entity);
            Integer count=esStartAdvertisingMapper.count(entity);
            map.put("rows", list);
            map.put("total", count);
            map.put("current", entity.getCurrent());
            map.put("size", entity.getSize());
            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean save(EsStartAdvertising entity) {
        try{

            String time=sdf.format(new Date());
            entity.setCreateTime(sdf.parse(time));
           boolean bool= this.insert(entity);

            EsStartAdvertising es=this.selectOne(new QueryWrapper<>(entity));
            if(es!=null){
                if(entity.getListAdvertImg()!=null&&entity.getListAdvertImg().size()>0){
                    for (EsStartAdvertisingImg es1:entity.getListAdvertImg()) {
                        es1.setAdvertisingId(es.getId());
                        esStartAdvertisingImgMapper.insert(es1);
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
