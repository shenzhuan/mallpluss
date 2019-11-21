package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopDiypageMapper;
import com.arvato.service.goods.api.service.EsShopDiypageService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopDiypage;
import com.mei.zhuang.entity.goods.EsShopGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:14
 * @Description:
 */
@Slf4j
@Service
public class EsShopDiypageServiceImpl extends ServiceImpl<EsShopDiypageMapper, EsShopDiypage> implements EsShopDiypageService {

    @Resource
    private EsShopDiypageMapper esShopDiypageMapper;

    @Override
    public Map<String, Object> selDiyPage(EsShopDiypage entity) {
        Map<String, Object> map=new HashMap<String,Object>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        List<EsShopDiypage> listDivPage=null;
        Integer count=0;
        try{
            listDivPage=esShopDiypageMapper.selDivpage(page,entity.getCurrent(),entity.getSize(),entity.getName(),entity.getType());
            count=esShopDiypageMapper.selDivPageCount(entity.getName(),entity.getType());//entity
            map.put("rows", listDivPage);
            map.put("total", count);
            map.put("current", entity.getCurrent());
            map.put("size", entity.getSize());
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public Map<String, Object> selDiyPageDetail(EsShopDiypage entity) {
        Map<String, Object> map=new HashMap<String,Object>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        List<EsShopDiypage> listDivPage=null;
        Integer count=0;
        try{
            listDivPage=esShopDiypageMapper.selDivpageDetail(page,entity.getCurrent(),entity.getSize(),entity.getName(),4);
            count=esShopDiypageMapper.selDivPageDetailCount(entity.getName(),4);//entity
            map.put("rows", listDivPage);
            map.put("total", count);
            map.put("current", entity.getCurrent());
            map.put("size", entity.getSize());
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public Object updStatus(Long id, Integer status, Integer typeId) {
        EsShopDiypage diypage = new EsShopDiypage();
        diypage.setId(id);
        diypage.setStatus(status);
        Object obj=esShopDiypageMapper.updateById(diypage);
        return obj;
    }

    @Override
    public Integer selDiyPageTypeId(Integer typeId,Long id) {
        return esShopDiypageMapper.selDiyPageTypeId(typeId,id);
    }

    @Override
    public EsShopDiypage selDiyDetail(Integer id) {
        return esShopDiypageMapper.selDiyDetail(id);
    }

    @Override
    public Integer selectCounts(Long id,String name) {
        return esShopDiypageMapper.selectCounts(id,name);
    }
}
