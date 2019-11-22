package com.mei.zhuang.service.marking.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.EsShopCouponGoodsMapMapper;
import com.mei.zhuang.dao.marking.EsShopCouponNewRuleMapper;
import com.mei.zhuang.dao.marking.EsShopCouponsTopupGoodsMapper;
import com.mei.zhuang.dao.marking.EsShopCouponsTopupMapper;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.entity.marking.EsShopCouponsTopup;
import com.mei.zhuang.entity.marking.EsShopCouponsTopupGoods;
import com.mei.zhuang.service.marking.CouponsTopupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author meizhuang team
 * @since 2019-05-13
 */
@Service
public class CouponsTopupServiceImpl extends ServiceImpl<EsShopCouponsTopupMapper, EsShopCouponsTopup> implements CouponsTopupService {
    @Resource
    private EsShopCouponsTopupMapper couponsTopupMapper;
    @Resource
    private EsShopCouponsTopupGoodsMapper couponsTopupGoodsMapper;
    @Resource
    private EsShopCouponNewRuleMapper couponsNewRuleMapper;
    @Resource
    private EsShopCouponGoodsMapMapper goodsMapMapper;

    public void datenew(EsShopCouponsTopup ent) {
        try {
            if (ent.getActivityStatus() == 2) {
                if (ent.getTime() != null && !ent.getTime().equals("")) {
                    String[] times = ent.getTime().split(",");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ent.setStartingTime(sdf.parse(times[0]));
                    ent.setEndTime(sdf.parse(times[1]));
                }
            }
            //活动开启
            if (ent.getActivityStatus() == 1) {
                if (ent.getTime() != null && !ent.getTime().equals("")) {
                    String[] times = ent.getTime().split(",");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ent.setStartingTime(sdf.parse(times[0]));
                    ent.setEndTime(sdf.parse(times[1]));
                    // Date tim = sdf.parse(sdf.format(new Date()));
              /*  if (tim.before(ent.getEndTime()) && tim.after(ent.getStartingTime())) {
                    ent.setActivityStatus(1);
                } else {
                    ent.setActivityStatus(2);
                }*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //满额发券添加
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopCouponsTopup entity) {
        datenew(entity);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        entity.setCouponTopupid(uuid);
        entity.setGoodsId(uuid);
        couponsTopupMapper.insert(entity);
        addsave(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopCouponsTopup entity) throws Exception {
        datenew(entity);
        couponsTopupMapper.updateById(entity);
        EsShopCouponsTopup esTopup = couponsTopupMapper.selectById(entity.getId());
        //z指定商品
        couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid", esTopup.getGoodsId()));
        //实物发券查询和赠品
        List<EsShopCouponNewRule> physicalId = couponsNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", esTopup.getCouponTopupid()));
        //实物商品和赠品
        couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("physical_id", physicalId.get(0).getId()));
        couponsNewRuleMapper.delete(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", esTopup.getCouponTopupid()));
        entity.setCouponTopupid(esTopup.getCouponTopupid());
        entity.setGoodsId(esTopup.getGoodsId());
        addsave(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deletegoodsid(Long goodsid, String typeid) {

        return couponsTopupGoodsMapper.deletegoodsid(goodsid, typeid);
    }

    //满额删除(根据优惠券id)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deletecouponid(long id) {
        EsShopCouponsTopup esShopTopup = couponsTopupMapper.selectById(id);
        couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid", esShopTopup.getGoodsId()));
        couponsNewRuleMapper.delete(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", esShopTopup.getCouponTopupid()));
        couponsTopupMapper.delete(new QueryWrapper<EsShopCouponsTopup>().eq("id", id));
        return 1;
    }

    //查询满额发券明细
    @Override
    public EsShopCouponsTopup selectTopupid(long id) {
        EsShopCouponsTopup esShopTopup = couponsTopupMapper.selectById(id);
        if (esShopTopup != null) {
            List<EsShopCouponNewRule> couponidlist = couponsNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", esShopTopup.getCouponTopupid()));
            esShopTopup.setCouponsList(couponidlist);
            List<EsShopCouponsTopupGoods> goodsidlist = couponsTopupGoodsMapper.selectList(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid", esShopTopup.getGoodsId()));
            esShopTopup.setCouponsTopupGoodsList(goodsidlist);
            return esShopTopup;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> selectTopup(EsShopCouponsTopup Topup) {
        List<Map<String, Object>> listmaps = couponsTopupMapper.selectTopup(Topup);
        return listmaps;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updatestatusid(Integer activitiesOpen, Long id) {

        return couponsTopupMapper.updatestatusid(activitiesOpen, id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addsave(EsShopCouponsTopup entity) {

        if (entity.getCouponsList() != null && entity.getCouponsList().size() > 0) {
            for (EsShopCouponNewRule ctr : entity.getCouponsList()) {
                EsShopCouponNewRule couptr = new EsShopCouponNewRule();
                couptr.setCouponTypes(ctr.getCouponTypes());
                couptr.setCouponid(ctr.getCouponid());
                couptr.setInventory(ctr.getInventory());
                couptr.setCouponName(ctr.getCouponName());
                //优惠券id
                couptr.setPublicCouponid(entity.getCouponTopupid());
                couponsNewRuleMapper.insert(couptr);
                EsShopCouponNewRule NewRule = couponsNewRuleMapper.selectById(couptr.getId());
                //赠品券
                if (NewRule.getCouponTypes() == 4) {
                    List<EsShopCouponGoodsMap> GoodsMaps = goodsMapMapper.selcetcoupongoods(NewRule.getCouponid());
                    for (EsShopCouponGoodsMap goods : GoodsMaps) {
                        EsShopCouponsTopupGoods trg = new EsShopCouponsTopupGoods();
                        trg.setGoodId(goods.getGoodsId());
                        trg.setPhysicalId(NewRule.getId());
                        couponsTopupGoodsMapper.insert(trg);
                    }
                }
                //商品券
                if (NewRule.getCouponTypes() == 3) {
                    List<EsShopCouponGoodsMap> GoodsMaps = goodsMapMapper.selectgoods2(NewRule.getCouponid());
                    for (EsShopCouponGoodsMap goods : GoodsMaps) {
                        EsShopCouponsTopupGoods trg = new EsShopCouponsTopupGoods();
                        trg.setGoodId(goods.getGoodsId());
                        trg.setSpecificationsId(goods.getSpecIds());
                        trg.setPhysicalId(NewRule.getId());
                        couponsTopupGoodsMapper.insert(trg);
                    }
                }
            }
        }
        if (entity.getCouponGoods() == 2) {
            if (entity.getCouponsTopupGoodsList() != null && entity.getCouponsTopupGoodsList().size() > 0) {
                for (EsShopCouponsTopupGoods ctrg : entity.getCouponsTopupGoodsList()) {
                    EsShopCouponsTopupGoods ectrg = new EsShopCouponsTopupGoods();
                    //指定商品id
                    ectrg.setGoodId(ctrg.getGoodId());
                    //关联商品id
                    ectrg.setPublicGoodsid(entity.getGoodsId());
                    ectrg.setSpecificationsId(ctrg.getSpecificationsId());
                    couponsTopupGoodsMapper.insert(ectrg);
                }
            }
        }
    }

}
