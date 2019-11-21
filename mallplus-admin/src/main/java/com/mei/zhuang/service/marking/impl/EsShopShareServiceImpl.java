package com.mei.zhuang.service.marking.impl;

import com.arvato.service.marking.api.orm.dao.*;
import com.arvato.service.marking.api.service.EsShopShareService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopShare;
import com.mei.zhuang.entity.marking.EsShopShareMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EsShopShareServiceImpl extends ServiceImpl<EsShopShareMapper, EsShopShare> implements EsShopShareService {
    @Resource
    private EsShopShareMapper shopShareMapper;
    @Resource
    private EsShopShareMemberMapper esShopShareAssistanceMapper;

    @Resource
    private EsShopCouponGoodsMapMapper goodsMapMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");


    @Resource
    private EsShopShareMapMapper shopShareMapMapper;

    public void datetime(EsShopShare en) throws Exception {
        String[] times = en.getActivityTime().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        en.setActivitystartTime(sdf.parse(times[0]));
        en.setActivityendTime(sdf.parse(times[1]));

    }

    @Override
    public List<EsShopShare> ShareList() {
        return shopShareMapper.ShareList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveShare(EsShopShare share) throws Exception {
        datetime(share);
        share.setStatus(2);
        share.setCreateTime(new Date());
        shopShareMapper.insert(share);
        addShare(share);
        return 1;
    }

    public void addShare(EsShopShare share){
        //发起者
        if(share.getSharemapList()!=null&&share.getSharemapList().size()>0){
            for(EsShopShareMap shareMap:share.getSharemapList()){
                EsShopShareMap map=new EsShopShareMap();
                map.setShareId(share.getId());
                map.setType(share.getGiftPrize());
                map.setWinningId(shareMap.getWinningId());
             /*   EsShopCouponGoodsMap pic = goodsMapMapper.selectById(map.getWinningId());
                if(pic!=null){
                    map.setPic(pic.getPic());
                    map.setGoodsId(pic.getGoodsId());
                }*/
                map.setWinningType(1);
                map.setWinningName(share.getActivityName());
                shopShareMapMapper.insert(map);
            }
        }
        if(share.getSharemapList2()!=null&&share.getSharemapList2().size()>0){
            for(EsShopShareMap shareMap:share.getSharemapList2()){
                EsShopShareMap map=new EsShopShareMap();
                map.setShareId(share.getId());
                map.setType(share.getHelpPrize());
                map.setWinningId(shareMap.getWinningId());
               /* EsShopCouponGoodsMap pic = goodsMapMapper.selectById(map.getWinningId());
                if(pic!=null){
                    map.setPic(pic.getPic());
                    map.setGoodsId(pic.getGoodsId());
                }*/
              //  获赠类型 1 发起者 2.助力
                map.setWinningType(2);
                map.setWinningName(share.getActivityName());
                shopShareMapMapper.insert(map);
            }
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer delete(long id) {
        EsShopShare share=new EsShopShare();
        share.setId(id);
        share.setIsDel(2);
        return shopShareMapper.updateById(share);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer update(EsShopShare share) throws Exception {
        datetime(share);
        share.setUpdateTime(new Date());
        shopShareMapper.updateById(share);
        shopShareMapMapper.delete(new QueryWrapper<EsShopShareMap>().eq("share_id",share.getId()));
        addShare(share);
        return 1;
    }

    @Override
    public Integer updatestatus(long id, Integer status) {
        return shopShareMapper.updateStatus(id,status);
    }

    @Override
    public EsShopShare sharedetail(long id) {
        //获赠类型 1 发起者 2.助力
        EsShopShare esShopShare = shopShareMapper.selectById(id);
        List<EsShopShareMap> initiate = shopShareMapMapper.selectList(new QueryWrapper<EsShopShareMap>().eq("share_id", id).eq("winning_type", 1));
        esShopShare.setSharemapList(initiate);
        List<EsShopShareMap> help = shopShareMapMapper.selectList(new QueryWrapper<EsShopShareMap>().eq("share_id", id).eq("winning_type", 2));
        esShopShare.setSharemapList2(help);
        return esShopShare;
    }

    @Override
    public Integer status(Integer status) {
        return shopShareMapper.status(status);
    }



}
