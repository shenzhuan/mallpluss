package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopGoodsImgGroupMapper;
import com.arvato.service.goods.api.orm.dao.EsShopGoodsImgMapper;
import com.arvato.service.goods.api.service.EsShopGoodsImgService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsImg;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "图片管理", description = "", tags = {"图片管理"})
@Service
public class EsShopGoodsImgServiceImpl extends ServiceImpl<EsShopGoodsImgMapper, EsShopGoodsImg> implements EsShopGoodsImgService {

    @Resource
    private EsShopGoodsImgMapper esShopGoodsImgMapper;
    @Resource
    private EsShopGoodsImgGroupMapper esShopGoodsImgGroupMapper;

    @Override
    public Map<String, Object> selImg(EsShopGoodsImg entity) {
        //分页
        Map<String,Object> map=new HashMap<String,Object>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
        List<EsShopGoodsImg> list=esShopGoodsImgMapper.selImg(page,entity);
        Integer count=esShopGoodsImgMapper.selCountImg(entity);
        map.put("rows", list);
        map.put("total", count);
        map.put("current", entity.getCurrent());
        map.put("size", entity.getSize());
        return map;
    }

    @Override
    public Integer delImg(Long id) {
        return esShopGoodsImgMapper.delImg(id);
    }

    @Override
    public Integer insImg(Long groupId, String img,String filename) {
        EsShopGoodsImg entity = new EsShopGoodsImg();
        entity.setImgAddress(img);
        entity.setImgGroupId(groupId);
        entity.setImgName(filename);
        return esShopGoodsImgMapper.insert(entity);
    }

    @Override
    public List<EsShopGoodsImg> selImgs(EsShopGoodsImg entity) {
        return esShopGoodsImgMapper.selImgs(entity.getImgGroupId());
    }
}
