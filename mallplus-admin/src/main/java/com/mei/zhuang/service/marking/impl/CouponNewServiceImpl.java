package com.mei.zhuang.service.marking.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.EsShopCouponGoodsMapMapper;
import com.mei.zhuang.dao.marking.EsShopCouponNewMapper;
import com.mei.zhuang.dao.marking.EsShopCouponNewRuleMapper;
import com.mei.zhuang.dao.marking.EsShopCouponsTopupGoodsMapper;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCouponNew;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.entity.marking.EsShopCouponsTopupGoods;
import com.mei.zhuang.service.marking.CouponNewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
/*
     优惠券
 */
@Service
public class CouponNewServiceImpl extends ServiceImpl<EsShopCouponNewMapper, EsShopCouponNew> implements CouponNewService {

    @Resource
    private EsShopCouponNewMapper couponNewMapper;
    @Resource
    private EsShopCouponNewRuleMapper couponNewRuleMapper;
    @Resource
    private EsShopCouponGoodsMapMapper goodsMapMapper;
    @Resource
    private EsShopCouponsTopupGoodsMapper couponsTopupGoodsMapper;

    public void date(EsShopCouponNew ent) throws Exception {
        if(!ent.getTime().equals("")) {
            String[] times = ent.getTime().split(",");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ent.setStartingTime(sdf.parse(times[0]));
            ent.setEndTime(sdf.parse(times[1]));
        }else{
            ent.setStartingTime(null);
            ent.setEndTime(null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addsave(EsShopCouponNew entity) throws Exception {
        if(entity.getRulesList()!=null&&entity.getRulesList().size()>0){
            for(EsShopCouponNewRule newrule:entity.getRulesList()){
                EsShopCouponNewRule nr=new EsShopCouponNewRule();
                nr.setCouponid(newrule.getCouponid());
                nr.setInventory(newrule.getInventory());
                nr.setPublicCouponid(entity.getCouponNewid());
                nr.setCouponTypes(newrule.getCouponTypes());
                nr.setCouponName(newrule.getCouponName());
                couponNewRuleMapper.insert(nr);
                EsShopCouponNewRule NewRule = couponNewRuleMapper.selectById(nr.getId());
                //赠品券
                if(NewRule.getCouponTypes()==4){
                    List<EsShopCouponGoodsMap> GoodsMaps = goodsMapMapper.selcetcoupongoods(NewRule.getCouponid());
                    for(EsShopCouponGoodsMap goods:GoodsMaps) {
                        EsShopCouponsTopupGoods trg = new EsShopCouponsTopupGoods();
                        trg.setGoodId(goods.getGoodsId());
                        trg.setPhysicalId(NewRule.getId());
                        couponsTopupGoodsMapper.insert(trg);
                    }
                }
                //商品券
                if(NewRule.getCouponTypes()==3){
                    List<EsShopCouponGoodsMap> GoodsMaps = goodsMapMapper.selectgoods2(NewRule.getCouponid());
                    for(EsShopCouponGoodsMap goods:GoodsMaps) {
                        EsShopCouponsTopupGoods trg = new EsShopCouponsTopupGoods();
                        trg.setGoodId(goods.getGoodsId());
                        trg.setSpecificationsId(goods.getSpecIds());
                        trg.setPhysicalId(NewRule.getId());
                        couponsTopupGoodsMapper.insert(trg);
                    }
                }
            }
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopCouponNew entity)  {
        try {
            if(entity.getStatus()==1){
                if(entity.getActivityId()==1) {
                    date(entity);
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
                entity.setCouponNewid(uuid);
                couponNewMapper.insert(entity);
                addsave(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopCouponNew couponNew) throws Exception {
      //  EsShopCouponNew couponnewid=selectOne(new QueryWrapper<EsShopCouponNew>().eq("id",couponNew.getId()));
        EsShopCouponNew couponnewid = couponNewMapper.selectById(couponNew.getId());
        List<EsShopCouponNewRule> shopCouponNew = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid",couponnewid.getCouponNewid()));
        //实物商品和赠品
        if(shopCouponNew.size()>0&&shopCouponNew!=null) {
            couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("physical_id", shopCouponNew.get(0).getCouponid()));
            couponNewRuleMapper.delete(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", couponnewid.getCouponNewid()));
        }
        date(couponNew);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
        couponNew.setCouponNewid(uuid);
        if(couponNew.getSingleCoupon()==null){
            couponNew.setSingleCoupon(0);
        }
        couponNewMapper.updateById(couponNew);
        addsave(couponNew);
        return true;
    }

    @Override
    public EsShopCouponNew listcouponnew() {
        Map<String,Object> map=new HashMap<String,Object>();
        EsShopCouponNew listcoupon= couponNewMapper.listcouponnew();
        if(listcoupon!=null) {
            List<EsShopCouponNewRule> coupontype = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", listcoupon.getCouponNewid()));
            listcoupon.setRulesList(coupontype);
            return listcoupon;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deletetypeid(Long couponid,String typeid) {

        return couponNewRuleMapper.deletetypeid(couponid,typeid);
    }

}
