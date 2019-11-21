package com.mei.zhuang.service.marking.impl;

import com.arvato.ec.common.utils.Weekutils;
import com.arvato.ec.common.vo.marking.CodeResult;
import com.mei.zhuang.vo.marking.GoodsSepcVo;
import com.arvato.ec.common.vo.order.CartMarkingVo;
import com.arvato.service.marking.api.orm.dao.EsShopCodeGiftGoodsMapMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCodeGiftMapper;
import com.arvato.service.marking.api.orm.dao.EsShopCodeGiftRuleMapper;
import com.arvato.service.marking.api.service.CodeGiftService;
import com.arvato.utils.date.DateUtil;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopCodeGift;
import com.mei.zhuang.entity.marking.EsShopCodeGiftGoodsMap;
import com.mei.zhuang.entity.marking.EsShopCodeGiftRule;
import com.mei.zhuang.entity.order.EsShopCart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Service
public class CodeGifterviceImpl extends ServiceImpl<EsShopCodeGiftMapper, EsShopCodeGift> implements CodeGiftService {
    @Resource
    private EsShopCodeGiftMapper fullGiftMapper;
    @Resource
    private EsShopCodeGiftGoodsMapMapper fullGiftGoodsMapMapper;
    @Resource
    private EsShopCodeGiftRuleMapper fullGiftRuleMapper;

    @Override
    public CodeResult getCodeGoods(CartMarkingVo vo) {
        CodeResult codeResult = new CodeResult();
        EsShopCodeGiftRule query = new EsShopCodeGiftRule();
        query.setCode(vo.getCode());
        EsShopCodeGiftRule rule = fullGiftRuleMapper.selectOne(query);
        if (rule!=null) {
            if (rule.getStatus() == 2 && rule.getActivityType()==2) {
                codeResult.setStatus(4);
                return codeResult;
            }
            EsShopCodeGift codeGift = fullGiftMapper.selectById(rule.getCodeGiftId());
            try {
                if (codeGift != null && codeGift.getStatus() == 2) {
                    codeResult.setStatus(5);
                    return codeResult;
                }
                if (checkEsShopCodeGift(codeGift)) {
                    boolean falg = false;
                    /**
                     *  * 商品模式，风格 1 全部商品 2 指定商品 3 不指定商品
                     */
                    if (codeGift.getGoodsMode() == 1) {
                        return getEsShopCodeGiftGoodsMaps(rule, codeGift);
                    } else if (codeGift.getGoodsMode() == 2) {
                        List<EsShopCodeGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopCodeGiftGoodsMap>().
                                eq("code_gift_id", codeGift.getId()));
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            for (EsShopCodeGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : vo.getCartList()) {
                                    if (manjianGoodsMap.getGoodsType() == 1 && manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        falg = true;
                                        break;
                                    }
                                }
                                if (falg) {
                                    break;
                                }
                            }
                            if (falg) {
                                return getEsShopCodeGiftGoodsMaps(rule, codeGift);
                            } else {
                                codeResult.setStatus(2);
                                return codeResult;
                            }

                        }
                    } else {
                        List<EsShopCodeGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopCodeGiftGoodsMap>().
                                eq("code_gift_id", codeGift.getId()));
                        int count = 0;
                        boolean falgn = false;
                        if (manjianGoodsMaps != null && manjianGoodsMaps.size() > 0) {
                            for (EsShopCodeGiftGoodsMap manjianGoodsMap : manjianGoodsMaps) {
                                for (EsShopCart cart : vo.getCartList()) {
                                    if (manjianGoodsMap.getGoodsType() == 1 && manjianGoodsMap.getGoodsId().equals(cart.getGoodsId()) && (ValidatorUtils.empty(manjianGoodsMap.getSpecIds())
                                            || manjianGoodsMap.getSpecIds().contains(cart.getOptionId() + ""))) {
                                        count++;
                                    } else {
                                        falgn = true;
                                    }
                                }
                            }
                            if (falgn && vo.getCartList().size() > count) {
                                return getEsShopCodeGiftGoodsMaps(rule, codeGift);
                            } else {
                                codeResult.setStatus(2);
                                return codeResult;
                            }

                        }
                    }
                } else {
                    codeResult.setStatus(1);
                    return codeResult;
                }

            } catch (ParseException e) {
                codeResult.setStatus(9);
                return codeResult;
            }
        }else{
            codeResult.setStatus(1);
            return codeResult;
        }
        return null;
    }

    private CodeResult getEsShopCodeGiftGoodsMaps(EsShopCodeGiftRule rule, EsShopCodeGift codeGift) {
        CodeResult codeResult = new CodeResult();
        codeResult.setStatus(3);
        if (rule.getActivityType() == 1) {
            List<EsShopCodeGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopCodeGiftGoodsMap>().
                    eq("code_gift_id", codeGift.getId()).eq("goods_type", 2));
            codeResult.setGiftGoodsMaps(manjianGoodsMaps);
            return codeResult;
        } else {
            if (rule.getStatus() == 2) {
                return null;
            } else {
                List<EsShopCodeGiftGoodsMap> manjianGoodsMaps = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopCodeGiftGoodsMap>().
                        eq("code_gift_id", codeGift.getId()).eq("goods_type", 2));
                codeResult.setGiftGoodsMaps(manjianGoodsMaps);
                return codeResult;
            }
        }
    }

    /*
          判断是否在高级设置活动范围内
    */
    private boolean checkEsShopCodeGift(EsShopCodeGift manjian) throws ParseException {
        if (manjian != null && manjian.getStatus() == 1) {
            Date da = new Date();
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            String format = sdf2.format(da);
            Date f = sdf2.parse(format);
            if (ValidatorUtils.notEmpty(manjian.getStartTime())) {
                String statime = DateUtil.format(manjian.getStartTime(), "HH:mm:ss");
                Date starttime = DateUtil.parse(statime, "HH:mm:ss");
                if (f.getTime() < starttime.getTime()) {
                    return false;
                }
            }
            if (ValidatorUtils.notEmpty(manjian.getEndTime())) {
                String entime = DateUtil.format(manjian.getEndTime(), "HH:mm:ss");
                Date endtime = DateUtil.parse(entime, "HH:mm:ss");
                if (f.getTime() > endtime.getTime()) {
                    return false;
                }
            }
            int week = Weekutils.getWeekofDay(da);
            if (ValidatorUtils.empty(manjian.getWeekdays()) || manjian.getWeekdays().contains(week + "")) {
                if (manjian.getExpiryBeginTime().getTime() <= da.getTime() && manjian.getExpiryEndTime().getTime() >= da.getTime()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateCodeStatus(String code,Integer status) {
        fullGiftRuleMapper.updateCodeStatus(code,status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopCodeGift entity) throws Exception {
        datetime(entity);
        entity.setUpdateTime(new Date());
        fullGiftMapper.updateById(entity);
        fullGiftGoodsMapMapper.delete(new QueryWrapper<EsShopCodeGiftGoodsMap>().eq("code_gift_id", entity.getId()));
        fullGiftRuleMapper.delete(new QueryWrapper<EsShopCodeGiftRule>().eq("code_gift_id", entity.getId()));
        addExtrInfo(entity);
        return true;
    }

    @Override
    public Integer deleteCode(long id) {
        fullGiftRuleMapper.delete(new QueryWrapper<EsShopCodeGiftRule>().eq("code_gift_id",id));
        fullGiftGoodsMapMapper.delete(new QueryWrapper<EsShopCodeGiftGoodsMap>().eq("code_gift_id",id));
        fullGiftMapper.deleteById(id);
        return 1;
    }

    @Override
    public EsShopCodeGift CodeList(long id) {
        EsShopCodeGift CodeGift = fullGiftMapper.selectById(id);
        List<EsShopCodeGiftGoodsMap> codegoods = fullGiftGoodsMapMapper.selectList(new QueryWrapper<EsShopCodeGiftGoodsMap>().eq("code_gift_id", CodeGift.getId()));
        CodeGift.setGoodsCouponList(codegoods);
        List<EsShopCodeGiftRule> coderule = fullGiftRuleMapper.codelist(CodeGift.getId());
        CodeGift.setCoderuleList(coderule);
        return CodeGift;
    }

    @Override
    public EsShopCodeGiftRule codegif(long codeGiftId) {
        return fullGiftRuleMapper.codegif(codeGiftId);
    }

    @Override
    public EsShopCodeGiftRule codegif2(String code) {
        return fullGiftRuleMapper.codegif2(code);
    }


    @Override
    public Map<String, Object> selPageList(EsShopCodeGift entity) {
        Page<EsShopCodeGift> page = new Page<EsShopCodeGift>(entity.getCurrent(), entity.getSize());
        Map<String, Object> map = new HashMap<String, Object>();
        List<EsShopCodeGift> list = fullGiftMapper.selPageList(page, entity);
        Integer count = fullGiftMapper.count(entity);
        map.put("rows", list);
        map.put("total", count);
        map.put("current", entity.getCurrent());
        map.put("size", entity.getSize());
        return map;
    }

    public void datetime(EsShopCodeGift en) throws Exception {

        String[] times = en.getTime().split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        en.setExpiryBeginTime(sdf.parse(times[0]));
        en.setExpiryEndTime(sdf.parse(times[1]));
        Date tim = sdf.parse(sdf.format(new Date()));
        if (tim.before(en.getExpiryBeginTime())) {
            en.setStatus(2);
        } else if (tim.after(en.getExpiryBeginTime()) && tim.before(en.getExpiryEndTime())) {
            en.setStatus(1);
        } else if (tim.after(en.getExpiryEndTime())) {
            en.setStatus(2);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopCodeGift entity) throws Exception {
        entity.setShopId((long) 1);
        entity.setCreateTime(new Date());
        datetime(entity);
        fullGiftMapper.insert(entity);
        addExtrInfo(entity);
        return true;
    }


    private void addExtrInfo(EsShopCodeGift entity) throws IOException {
        //商品模式，风格 1 全部商品 2 指定商品 3 不指定商品
        if (entity.getGoodsMode() == 2 || entity.getGoodsMode() == 3) {
            if (entity.getGoodsSepcVoList() != null && entity.getGoodsSepcVoList().size() > 0) {
                for (GoodsSepcVo vo : entity.getGoodsSepcVoList()) {
                    EsShopCodeGiftGoodsMap group = new EsShopCodeGiftGoodsMap();
                    group.setCodeGiftId(entity.getId());
                    group.setGoodsId(vo.getGoodsId());
                    group.setSpecIds(vo.getSpecIds());
                    group.setActivityName(entity.getTitles());
                    group.setGoodsType(1);
                    fullGiftGoodsMapMapper.insert(group);
                }
            }
        }
        //赠品
        if (entity.getGoodsCouponList()!=null&&entity.getGoodsCouponList().size()>0) {

            for (EsShopCodeGiftGoodsMap gid : entity.getGoodsCouponList()) {
                EsShopCodeGiftGoodsMap group = new EsShopCodeGiftGoodsMap();
                group.setCodeGiftId(entity.getId());
                group.setGoodsId(gid.getGoodsId());
                group.setGoodsType(2);
                group.setPic(gid.getPic());
                group.setActivityName(entity.getTitles());
                group.setGoodsName(gid.getGoodsName());
                fullGiftGoodsMapMapper.insert(group);
            }
        }
        if (entity.getCoderuleList() != null && entity.getCoderuleList().size() > 0) {
            for (EsShopCodeGiftRule rule : entity.getCoderuleList()) {
                String str[] =rule.getCode().split(",");
                for(String st:str){
                    rule.setActivityType(entity.getType());
                    rule.setCodeGiftId(entity.getId());
                    rule.setShopId(entity.getShopId());
                    rule.setCode(st);
                    //未使用
                    rule.setStatus(1);
                    fullGiftRuleMapper.insert(rule);
                }
            }
        }
    }

    @Override
    public int updateShowStatus(Long id, Integer showStatus) {
        if (showStatus == 1) {
            EsShopCodeGift record = new EsShopCodeGift();
            record.setId(id);
            record.setStatus(showStatus);
            record.setUpdateTime(new Date());
            return fullGiftMapper.updateById(record);
        } else {
            EsShopCodeGift record = new EsShopCodeGift();
            record.setUpdateTime(new Date());
            record.setId(id);
            record.setStatus(2);
            return fullGiftMapper.updateById(record);
        }

    }
}
