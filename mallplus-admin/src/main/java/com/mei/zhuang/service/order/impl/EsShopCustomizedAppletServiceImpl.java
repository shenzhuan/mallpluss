package com.mei.zhuang.service.order.impl;

import com.arvato.ec.common.exception.BusinessException;
import com.arvato.ec.common.vo.order.EsShopCustAppletParam;
import com.arvato.service.order.api.feigin.GoodsFegin;
import com.arvato.service.order.api.orm.dao.EsShopCartMapper;
import com.arvato.service.order.api.orm.dao.EsShopCustomizedAppletMapper;
import com.arvato.service.order.api.service.EsShopCustomizedAppletService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopCustomizedApplet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EsShopCustomizedAppletServiceImpl extends ServiceImpl<EsShopCustomizedAppletMapper, EsShopCustomizedApplet> implements EsShopCustomizedAppletService {

    @Resource
    private EsShopCartMapper cartMapper;
    @Resource
    private EsShopCustomizedAppletMapper esShopCustomizedAppletMapper;
    @Resource
    private GoodsFegin goodsFegin;


//    @Resource
//    private EsShopCustomizedPacket



    @Override
    public Object saveCust(EsShopCustAppletParam entity) {
        try{
            if (ValidatorUtils.empty(entity.getTotal())) {
                entity.setTotal(1);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            EsShopCart cart = new EsShopCart();
            EsShopCart exit = new EsShopCart();
            Date time=null;
            EsShopGoods goods = goodsFegin.getGoodsById(entity.getGoodsId());
            if (ValidatorUtils.empty(entity.getOptionId())) {
                if (ValidatorUtils.empty(entity.getGoodsId())) {
                    return new CommonResult().paramFailed();
                }
                checkGoods(goods, true, entity.getTotal());
                cart.setGoodsId(entity.getGoodsId());
                cart.setMemberId(entity.getMemberId());
                cart.setShopId(goods.getShopId());
                cart.setType(entity.getTypeOption());
                cart.setTypeword(entity.getTypeword());
                cart.setPic(goods.getThumb());
                cart.setGoodsName(goods.getTitle());
                cart.setPrice(goods.getPrice());
                cart.setTotal(entity.getTotal());
                cart.setCreateTime(new Date());
                this.insert(entity);
                cart.setCustId(entity.getId());
                cartMapper.insert(cart);
            } else {
                checkGoods(goods, false, entity.getTotal());
                EsShopGoodsOption sku = getSkuByGoods(goods, entity.getOptionId());
                checkSkuGoods(sku, entity.getTotal());
                cart.setGoodsId(entity.getGoodsId());
                cart.setMemberId(entity.getMemberId());
                cart.setShopId(sku.getShopId());
                cart.setOptionId(entity.getOptionId());
                cart.setType(entity.getTypeOption());
                cart.setTypeword(entity.getTypeword());
                cart.setPic(sku.getThumb());
                cart.setGoodsName(sku.getGoodsName());
                cart.setOptionName(sku.getTitle());
                cart.setPrice(sku.getPrice());
                cart.setTotal(entity.getTotal());
                cart.setCreateTime(new Date());
                this.insert(entity);
                cart.setCustId(entity.getId());
                cartMapper.insert(cart);
            }

            return cart;
            }catch (Exception e){
                e.printStackTrace();

                return  null;
            }
    }

    @Override
    public Object detailCustService(EsShopCustomizedApplet entity) {
        try{
            EsShopCustomizedApplet obj= this.selectOne(new QueryWrapper<>(entity));
            if(obj != null ){
                //查询刻字服务和样图
                if(obj.getBasicId() != null){
                    EsShopCustomizedBasic basic = new EsShopCustomizedBasic();
                    basic.setId(obj.getBasicId());
                    obj.setEsShopCustomizedBasic(goodsFegin.detailBasics(basic));
                    //样图
                    obj.setEsShopCustomizedLegend(goodsFegin.detailLegend(obj.getLegendId()));
                }
                //查询卡片
                if(obj.getCardId() != null){
                    obj.setEsShopCustomizedCard(goodsFegin.detail(obj.getCardId()));
                }
                //查询套装
                if(obj.getPacketId() != null){
                    obj.setEsShopCustomizedPacket(goodsFegin.details(obj.getPacketId()));
                }
                obj.setMap( goodsFegin.selectCust(obj.getGoodsId()));
            }
            return obj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private void checkGoods(EsShopGoods goods, boolean falg, int count) {
        if (goods == null || goods.getId() == null) {
            throw new BusinessException("商品已删除");
        }
        if (goods.getStatus() != 1) {
            throw new BusinessException("商品已售罄");//        //商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
        }
        if (falg && (goods.getVituralStock() <= 0 || goods.getVituralStock() < count)) {
            throw new BusinessException("库存不足!");
        }
    }

    private void checkSkuGoods(EsShopGoodsOption goods, int count) {
        if (goods == null || goods.getId() == null) {
            throw new BusinessException("商品已删除");
        }
        if (goods.getVirtualStock() <= 0 || goods.getVirtualStock() < count) {
            throw new BusinessException("库存不足!");
        }
    }
    private EsShopGoodsOption getSkuByGoods(EsShopGoods goods, Long optionId) {
        List<EsShopGoodsOption> options = goods.getOptions();
        if (options != null && options.size() > 0) {
            for (EsShopGoodsOption o : options) {
                if (o.getId().equals(optionId)) {
                    return o;
                }
            }
        }
        return null;
    }

}
