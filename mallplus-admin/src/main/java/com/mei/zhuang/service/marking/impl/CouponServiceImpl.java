package com.mei.zhuang.service.marking.impl;

import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.arvato.service.marking.api.orm.dao.EsMemberCouponMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCouponGoodsMapMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCouponMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCouponRuleMapper;
import com.arvato.service.marking.api.service.CouponService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopCoupon;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCouponRule;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**

 */
/*
     优惠券
 */
@Service
public class CouponServiceImpl extends ServiceImpl<EsShopCouponMapper, EsShopCoupon> implements CouponService {

    @Resource
    private EsShopCouponMapper couponMapper;
    @Resource
    private EsShopCouponRuleMapper ruleMapper;
    @Resource
    private EsShopCouponGoodsMapMapper goodsMapMapper;
    @Resource
    private EsMemberCouponMapper membercouponMapper;

    /*public void dxtime(EsShopCoupon entity)throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tim = sdf.parse(sdf.format(new Date()));
            if (tim.before(entity.getExpiryBeginTime())) {
                entity.setStatus(2);
            } else if (tim.after(entity.getExpiryBeginTime()) && tim.before(entity.getExpiryEndTime())) {
                entity.setStatus(1);
            } else if (tim.after(entity.getExpiryEndTime())) {
                entity.setStatus(3);
            }

    }*/

    public void datetime(EsShopCoupon entity) throws Exception {
        try {
        if(entity.getTime()!=null&&!entity.getTime().equals("")) {
            String[] times = entity.getTime().split(",");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tim = sdf.parse(sdf.format(new Date()));
            Date te = sdf.parse(times[0]);
            Date tes = sdf.parse(times[1]);
            //当前时间开小于开始时间
            if (entity.getAmount()==1) {
                //开始时间
                entity.setExpiryBeginTime(te);
                //结束时间
                entity.setExpiryEndTime(tes);
                if (tim.before(entity.getExpiryBeginTime())) {
                    entity.setStatus(2);
                } else if (tim.after(entity.getExpiryBeginTime()) && tim.before(entity.getExpiryEndTime())) {
                    entity.setStatus(1);
                } else if (tim.after(entity.getExpiryEndTime())) {
                    entity.setStatus(3);
                }
            }
        }
        if(entity.getAmount()==3||entity.getAmount()==2){
            entity.setStatus(1);
        }
            /* if(entity.getAmount()==2){
                //当前日期
                Calendar c = Calendar.getInstance();
                entity.setExpiryBeginTime(c.getTime());
                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(entity.getTimeInterval()));
                entity.setExpiryEndTime(c.getTime());
                entity.setStatus(1);
            }else if(entity.getAmount()==3){
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY,0);
                c.set(Calendar.MINUTE,0);
                c.set(Calendar.SECOND,0);
                entity.setExpiryBeginTime(c.getTime());
                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(entity.getTimeInterval()));
                entity.setExpiryEndTime(c.getTime());
                 dxtime(entity);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer save(EsShopCoupon entity,EsShopCouponRule rule) throws Exception {
        datetime(entity);
        entity.setCreateTime(new Date());
        entity.setShopId((long) 1);
        couponMapper.insert(entity);
        addcoupon(entity,rule);
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addcoupon(EsShopCoupon entity,EsShopCouponRule rule) {
         // 处理实物券商品添加
        if (entity.getType()==3) {
            if (entity.getGoodsSepcVoLists() != null && entity.getGoodsSepcVoLists().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoLists()) {
                    EsShopCouponGoodsMap goodsMap = new EsShopCouponGoodsMap();
                            goodsMap.setCouponId(entity.getId());
                            goodsMap.setGoodsId(vo.getGoodsId());
                            goodsMap.setGoodslimit(1);
                            goodsMap.setGoodsName(vo.getGoodsName());
                            goodsMap.setPic(vo.getPic());
                            goodsMap.setSpecIds(vo.getSpecIds());
                            goodsMapMapper.insert(goodsMap);
                }
            }
        }
        //赠品券添加
        if (entity.getType()==4) {
            if (entity.getGoodsSepcVoLists() != null && entity.getGoodsSepcVoLists().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoLists()) {
                    EsShopCouponGoodsMap goodsMap = new EsShopCouponGoodsMap();
                    goodsMap.setCouponId(entity.getId());
                    goodsMap.setGoodsId(vo.getGoodsId());
                    goodsMap.setGoodsName(vo.getGoodsName());
                    goodsMap.setPic(vo.getPic());
                    goodsMap.setGoodslimit(3);
                    goodsMapMapper.insert(goodsMap);
                }
            }
        }
        if (entity.getLimit()==2) {
                        //限制条件
                        rule.setActivityType(entity.getLimit());
                        rule.setCouponId(entity.getId());
                        //指定商品添加
            if (rule.getGoodsLimitedId()!=1 && rule.getGoodsSepcVoList() != null && rule.getGoodsSepcVoList().size()> 0) {
                for (GoodsSepcVo voi : rule.getGoodsSepcVoList()) {
                    EsShopCouponGoodsMap goodsMap = new EsShopCouponGoodsMap();
                    if(voi.getSpecIds()!=null&&!voi.getSpecIds().equals("")) {
                            goodsMap.setCouponId(entity.getId());
                            goodsMap.setGoodsId(voi.getGoodsId());
                            goodsMap.setSpecIds(voi.getSpecIds());
                            goodsMap.setGoodslimit(2);
                            goodsMapMapper.insert(goodsMap);

                    }else{
                        goodsMap.setCouponId(entity.getId());
                        goodsMap.setGoodsId(voi.getGoodsId());
                        goodsMap.setGoodslimit(2);
                        goodsMapMapper.insert(goodsMap);
                    }
                }
            }
               ruleMapper.insert(rule);

        }else{
            if (rule.getGoodsLimitedId()!=1 && rule.getGoodsSepcVoList() != null && rule.getGoodsSepcVoList().size() > 0) {
                for (GoodsSepcVo voi : rule.getGoodsSepcVoList()) {
                    EsShopCouponGoodsMap goodsMap = new EsShopCouponGoodsMap();
                   if(voi.getSpecIds()!=null&&!voi.getSpecIds().equals("")) {
                            goodsMap.setCouponId(entity.getId());
                            goodsMap.setGoodsId(voi.getGoodsId());
                            goodsMap.setSpecIds(voi.getSpecIds());
                            goodsMap.setGoodslimit(2);
                            goodsMapMapper.insert(goodsMap);
                    }else{
                        goodsMap.setCouponId(entity.getId());
                        goodsMap.setGoodsId(voi.getGoodsId());
                        goodsMap.setGoodslimit(2);
                        goodsMapMapper.insert(goodsMap);
                    }
                }
            }
            rule.setActivityType(1);
            rule.setCouponId(entity.getId());
            //无限制条件
            rule.setConditions(3);
            ruleMapper.insert(rule);
        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteid(Long id) {

        return goodsMapMapper.delete(new QueryWrapper<EsShopCouponGoodsMap>().eq("goods_id", id));
    }

    /**
     * 删除所有的优惠券
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer detetecouponid(Long id) {
        couponMapper.deleteById(id);
        goodsMapMapper.delete(new QueryWrapper<EsShopCouponGoodsMap>().eq("coupon_id", id));
        ruleMapper.delete(new QueryWrapper<EsShopCouponRule>().eq("coupon_id", id));
        return 1;
    }

    @Override
    public Integer updatestatus(Long id,String statusOpen) {
        return couponMapper.updatestatus(id,statusOpen);
    }


    //优惠券选择查询
    @Transactional(readOnly = true)
    @Override
    public List<EsShopCoupon> selectcoupon(EsShopCoupon esShopCoupon) {

        return couponMapper.selectcoupon(esShopCoupon);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopCoupon entity,EsShopCouponRule rule) throws Exception {
        datetime(entity);
        couponMapper.updateById(entity);
        EsShopCoupon Coupon = couponMapper.selectById(entity.getId());
        if(Coupon.getAmount()==1) {
            List<EsMemberCoupon> title = membercouponMapper.selectstatus(Coupon.getCouponsName());
            for (EsMemberCoupon ti : title) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date tim = sdf.parse(sdf.format(new Date()));
                EsMemberCoupon memberCoupon = new EsMemberCoupon();
                memberCoupon.setStartTime(Coupon.getExpiryBeginTime());
                memberCoupon.setEndTime(Coupon.getExpiryEndTime());
                memberCoupon.setTitle(Coupon.getCouponsName());
                //有效状态1有效  2，未生效，3失效
                if (tim.before(entity.getExpiryBeginTime())) {
                    memberCoupon.setEffective(2);
                } else if (tim.after(entity.getExpiryBeginTime()) && tim.before(entity.getExpiryEndTime())) {
                    memberCoupon.setEffective(1);
                } else if (tim.after(entity.getExpiryEndTime())) {
                    memberCoupon.setEffective(3);
                }
                membercouponMapper.updatecoupon(memberCoupon);
            }
        }
        goodsMapMapper.delete(new QueryWrapper<EsShopCouponGoodsMap>().eq("coupon_id", entity.getId()));
        ruleMapper.delete(new QueryWrapper<EsShopCouponRule>().eq("coupon_id", entity.getId()));
        addcoupon(entity,rule);
        return true;
    }
    //优惠券管理列表显示
    @Override
    public Map<String, Object> selectmapcoupon(EsShopCoupon esShopCoupon) throws Exception {
        if(esShopCoupon.getTime()!=null&&!esShopCoupon.getTime().equals("")) {
            String[] times = esShopCoupon.getTime().split(",");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //开始时间
            esShopCoupon.setExBeginTime(sdf.parse(times[0]));
            //结束时间
            esShopCoupon.setExEndTime(sdf.parse(times[1]));
        }
        Map<String,Object> map=new HashedMap();
        List<Map<String, Object>> selectmapcoupon = couponMapper.selectmapcoupon(esShopCoupon);
        map.put("rows",selectmapcoupon);
        return map;
    }


    @Override
    public List<Map<String, Object>> couponlimit(Long id) {
        return couponMapper.couponlimit(id);
    }

    //优惠券限制商品明细
    @Override
    public List<EsShopCouponGoodsMap> selectgoods(Long couponId) {

        return goodsMapMapper.selectgoods(couponId);
    }
    //实物的商品
    @Override
    public List<EsShopCouponGoodsMap> selectgoods2(Long couponId) {
        return goodsMapMapper.selectgoods2(couponId);
    }

    @Override
    public List<EsShopCouponGoodsMap> selcetcoupongoods(Long couponId) {
        return goodsMapMapper.selcetcoupongoods(couponId);
    }

    @Override
    public List<EsShopCoupon> couponbatch(String id) {
        String str []=id.split(",");
        List<EsShopCoupon> listcoupon=new ArrayList<>();
        for(String st:str){
            EsShopCoupon esShopCoupon = couponMapper.selectById(Integer.parseInt(st));
            listcoupon.add(esShopCoupon);
        }
        return listcoupon;
    }

}
