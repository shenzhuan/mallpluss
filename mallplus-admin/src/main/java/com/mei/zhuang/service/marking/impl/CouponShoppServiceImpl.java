package com.mei.zhuang.service.marking.impl;

import com.arvato.service.marking.api.orm.dao.EsShopCouponGoodsMapMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCouponNewRuleMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCouponShoppingMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCouponsTopupGoodsMapper;
import com.arvato.service.marking.api.service.CouponShoppingService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopCouponGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.entity.marking.EsShopCouponShopping;
import com.mei.zhuang.entity.marking.EsShopCouponsTopupGoods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CouponShoppServiceImpl extends ServiceImpl<EsShopCouponShoppingMapper,EsShopCouponShopping> implements CouponShoppingService {
  @Resource
  private EsShopCouponShoppingMapper couponShoppingMapper;
  @Resource
  private EsShopCouponNewRuleMapper couponsNewRuleMapper;
  @Resource
  private EsShopCouponsTopupGoodsMapper couponsTopupGoodsMapper;
    @Resource
  private EsShopCouponGoodsMapMapper goodsMapMapper;

  public void date(EsShopCouponShopping ent) throws Exception {
      //活动关闭
      if(ent.getActivitiesOpen()==2) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          if (ent.getTime()!= null&&!ent.getTime().equals("")) {
              String[] times = ent.getTime().split(",");
              ent.setStartingTime(sdf.parse(times[0]));
              ent.setEndTime(sdf.parse(times[1]));
          }
      }
      if(ent.getActivitiesOpen()==1){
          if (ent.getTime()!= null&&!ent.getTime().equals("")) {
              String[] times = ent.getTime().split(",");
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              ent.setStartingTime(sdf.parse(times[0]));
              ent.setEndTime(sdf.parse(times[1]));
          }
          }

  }

  //购物发券添加
  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean save(EsShopCouponShopping entity) throws Exception {
      date(entity);
      String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
      entity.setCouponShopid(uuid);
      entity.setGoodshopId(uuid);
      entity.setCreateTime(new Date());
      couponShoppingMapper.insert(entity);
      addsave(entity);
      return true;
  }
  @Transactional(rollbackFor=Exception.class)
  @Override
  public boolean update(EsShopCouponShopping entity) throws Exception {
    date(entity);
    couponShoppingMapper.updateById(entity);
    //指定商品
    EsShopCouponShopping esShopShopping = couponShoppingMapper.selectById(entity.getId());
    couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid",esShopShopping.getGoodshopId()));
      //实物发券查询和赠品
    List<EsShopCouponNewRule> physicalId = couponsNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", esShopShopping.getCouponShopid()));
      //实物商品和赠品
    couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("physical_id",physicalId.get(0).getId()));
    couponsNewRuleMapper.delete(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid",esShopShopping.getCouponShopid()));
    entity.setGoodshopId(esShopShopping.getGoodshopId());
    entity.setCouponShopid(esShopShopping.getCouponShopid());
    addsave(entity);
    return true;
  }
  @Transactional(rollbackFor=Exception.class)
  @Override
  public Integer deletecouponid(long id) {
      EsShopCouponShopping esShopping = couponShoppingMapper.selectById(id);
      couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid",esShopping.getGoodshopId()));
      couponsNewRuleMapper.delete(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid",esShopping.getCouponShopid()));
      couponShoppingMapper.delete(new QueryWrapper<EsShopCouponShopping>().eq("id",id));
    return 1;
  }

  //购物发券联查
  @Override
  public List<Map<String, Object>> selectshopping() {

    return couponShoppingMapper.selectshopping();
  }

  @Override
  public EsShopCouponShopping selectshopid(long id) {
          EsShopCouponShopping esShopping = couponShoppingMapper.selectById(id);
          List<EsShopCouponNewRule> couponid = couponsNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", esShopping.getCouponShopid()));
          esShopping.setCouponShopList(couponid);
          //指定商品
          List<EsShopCouponsTopupGoods> topupGoods = couponsTopupGoodsMapper.selectList(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid", esShopping.getGoodshopId()));
          esShopping.setShopGoodsList(topupGoods);
          return esShopping;
  }


    @Override
    public List<EsShopCouponsTopupGoods> goodsList(long id) {
        EsShopCouponShopping esShopping = couponShoppingMapper.selectById(id);
        List<EsShopCouponsTopupGoods> topupGoods = couponsTopupGoodsMapper.selectList(new QueryWrapper<EsShopCouponsTopupGoods>().eq("public_goodsid", esShopping.getGoodshopId()));
        return topupGoods;
    }

    @Override
    public Integer updatestatusid(Integer activitiesOpen, Long id) {
        return couponShoppingMapper.updatestatusid(activitiesOpen,id);
    }


    @Transactional(rollbackFor = Exception.class)
  public void addsave(EsShopCouponShopping entity){
    if(entity.getScopeId()==2&&entity.getShopGoodsList().size()>0) {
        for (EsShopCouponsTopupGoods tg : entity.getShopGoodsList()) {
            EsShopCouponsTopupGoods ctg = new EsShopCouponsTopupGoods();
            ctg.setGoodId(tg.getGoodId());
            ctg.setPublicGoodsid(entity.getGoodshopId());
            ctg.setSpecificationsId(tg.getSpecificationsId());
            couponsTopupGoodsMapper.insert(ctg);
        }
    }
    //优惠券
    if(entity.getCouponShopList()!=null &&entity.getCouponShopList().size()>0){
      for(EsShopCouponNewRule cnr:entity.getCouponShopList()){
        EsShopCouponNewRule cn=new EsShopCouponNewRule();
        cn.setPublicCouponid(entity.getCouponShopid());
        cn.setCouponid(cnr.getCouponid());
        cn.setCouponName(cnr.getCouponName());
        cn.setInventory(cnr.getInventory());
        cn.setCouponTypes(cnr.getCouponTypes());
        couponsNewRuleMapper.insert(cn);
          EsShopCouponNewRule NewRule = couponsNewRuleMapper.selectById(cn.getId());
          //赠品券
          if(NewRule.getCouponTypes()==4){
              List<EsShopCouponGoodsMap> GoodsMaps = goodsMapMapper.selcetcoupongoods(NewRule.getCouponid());
              for(EsShopCouponGoodsMap goods:GoodsMaps) {
                  EsShopCouponsTopupGoods trg = new EsShopCouponsTopupGoods();
                  trg.setGoodId(goods.getGoodsId());
                  trg.setSpecificationsId(goods.getSpecIds());
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




}
