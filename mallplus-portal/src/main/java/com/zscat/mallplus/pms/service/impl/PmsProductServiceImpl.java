package com.zscat.mallplus.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.cms.service.ICmsPrefrenceAreaProductRelationService;
import com.zscat.mallplus.cms.service.ICmsSubjectProductRelationService;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.pms.entity.*;
import com.zscat.mallplus.pms.mapper.*;
import com.zscat.mallplus.pms.service.*;
import com.zscat.mallplus.pms.vo.GoodsDetailResult;
import com.zscat.mallplus.pms.vo.PmsProductAndGroup;
import com.zscat.mallplus.pms.vo.PmsProductParam;
import com.zscat.mallplus.sms.entity.*;
import com.zscat.mallplus.sms.mapper.SmsGroupMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupMemberMapper;
import com.zscat.mallplus.sms.mapper.SmsPaimaiLogMapper;
import com.zscat.mallplus.sms.service.ISmsHomeBrandService;
import com.zscat.mallplus.sms.service.ISmsHomeNewProductService;
import com.zscat.mallplus.sms.service.ISmsHomeRecommendProductService;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.ApiContext;
import com.zscat.mallplus.vo.Rediskey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements IPmsProductService {

    @Resource
    private IPmsBrandService brandService;
    @Resource
    private ISmsHomeBrandService homeBrandService;
    @Resource
    private ISmsHomeNewProductService homeNewProductService;
    @Resource
    private ISmsHomeRecommendProductService homeRecommendProductService;
    @Resource
    private PmsProductMapper productMapper;
    @Resource
    private IPmsMemberPriceService memberPriceDao;
    @Resource
    private PmsMemberPriceMapper memberPriceMapper;
    @Resource
    private IPmsProductLadderService productLadderDao;
    @Resource
    private PmsProductLadderMapper productLadderMapper;
    @Resource
    private IPmsProductFullReductionService productFullReductionDao;
    @Resource
    private PmsProductFullReductionMapper productFullReductionMapper;
    @Resource
    private IPmsSkuStockService skuStockDao;
    @Resource
    private PmsSkuStockMapper skuStockMapper;
    @Resource
    private IPmsProductAttributeValueService productAttributeValueDao;
    @Resource
    private PmsProductAttributeValueMapper productAttributeValueMapper;
    @Resource
    private ICmsSubjectProductRelationService subjectProductRelationDao;
    @Resource
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;
    @Resource
    private ICmsPrefrenceAreaProductRelationService prefrenceAreaProductRelationDao;
    @Resource
    private CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;

    @Resource
    private PmsProductVertifyRecordMapper productVertifyRecordDao;

    @Resource
    private PmsProductVertifyRecordMapper productVertifyRecordMapper;

    @Resource
    private SmsGroupMapper groupMapper;
    @Resource
    private SmsGroupMemberMapper groupMemberMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private SysStoreMapper storeMapper;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private IPmsFavoriteService favoriteService;
    @Autowired
    private ApiContext apiContext;
    @Resource
    private SmsPaimaiLogMapper paimaiLogMapper;
    @Autowired
    private IUmsMemberService memberService;
    @Override
    public PmsProductAndGroup getProductAndGroup(Long id) {
        PmsProduct goods = productMapper.selectById(id);
        PmsProductAndGroup vo = new PmsProductAndGroup();
        try {
            BeanUtils.copyProperties(goods, vo);
            SmsGroup queryG = new SmsGroup();
            queryG.setGoodsId(id);
            SmsGroup group = groupMapper.selectOne(new QueryWrapper<>(queryG));
            SmsGroupMember newG = new SmsGroupMember();
            newG.setGoodsId(id);
            List<SmsGroupMember> list = groupMemberMapper.selectList(new QueryWrapper<>(newG));
            if (group != null) {
                Map<String, List<SmsGroupMember>> map = groupMemberByMainId(list, group);
                vo.setMap(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 按照异常批次号对已开单数据进行分组
     *
     * @param billingList
     * @return
     * @throws Exception
     */
    private Map<String, List<SmsGroupMember>> groupMemberByMainId(List<SmsGroupMember> billingList, SmsGroup group) throws Exception {
        Map<String, List<SmsGroupMember>> resultMap = new HashMap<String, List<SmsGroupMember>>();
        Map<String, List<SmsGroupMember>> map = new HashMap<String, List<SmsGroupMember>>();
        try {
            List<Long> ids = new ArrayList<>();
            for (SmsGroupMember tmExcpNew : billingList) {
                if (tmExcpNew.getMemberId().equals(tmExcpNew.getMainId())) {
                    Date cr = tmExcpNew.getCreateTime();
                    Long nowT = System.currentTimeMillis();
                    Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(cr, group.getHours()), "yyyy-MM-dd HH:mm:ss");
                    if (nowT <= endTime.getTime()) {
                        ids.add(tmExcpNew.getMainId());
                    }
                }
                if (resultMap.containsKey(tmExcpNew.getMainId() + "")) {//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中
                    resultMap.get(tmExcpNew.getMainId() + "").add(tmExcpNew);
                } else {//map中不存在，新建key，用来存放数据
                    List<SmsGroupMember> tmpList = new ArrayList<SmsGroupMember>();
                    tmpList.add(tmExcpNew);
                    resultMap.put(tmExcpNew.getMainId() + "", tmpList);
                }
            }
            for (Long id : ids) {
                if (resultMap.get(id + "") != null) {
                    map.put(id + "", resultMap.get(id + ""));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("按照异常批次号对已开单数据进行分组时出现异常", e);
        }

        return map;
    }

    @Override
    public PmsProduct getUpdateInfo(Long id) {
        return  productMapper.selectById(id);
    }

    @Override
    public Object initGoodsRedis() {
        List<PmsProduct> list = productMapper.selectList(new QueryWrapper<>());
        for (PmsProduct goods : list) {
            PmsProductParam param = new PmsProductParam();
            param.setGoods(goods);

            List<PmsProductLadder> productLadderList = productLadderMapper.selectList(new QueryWrapper<PmsProductLadder>().eq("product_id", goods.getId()));

            List<PmsProductFullReduction> productFullReductionList = productFullReductionMapper.selectList(new QueryWrapper<PmsProductFullReduction>().eq("product_id", goods.getId()));

            List<PmsMemberPrice> memberPriceList = memberPriceMapper.selectList(new QueryWrapper<PmsMemberPrice>().eq("product_id", goods.getId()));

            List<PmsSkuStock> skuStockList = skuStockMapper.selectList(new QueryWrapper<PmsSkuStock>().eq("product_id", goods.getId()));

            List<PmsProductAttributeValue> productAttributeValueList = productAttributeValueMapper.selectList(new QueryWrapper<PmsProductAttributeValue>().eq("product_id", goods.getId()));

            List<CmsSubjectProductRelation> subjectProductRelationList = subjectProductRelationMapper.selectList(new QueryWrapper<CmsSubjectProductRelation>().eq("product_id", goods.getId()));

            List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList = prefrenceAreaProductRelationMapper.selectList(new QueryWrapper<CmsPrefrenceAreaProductRelation>().eq("product_id", goods.getId()));

            param.setMemberPriceList(memberPriceList);
            param.setPrefrenceAreaProductRelationList(prefrenceAreaProductRelationList);
            param.setProductAttributeValueList(productAttributeValueList);
            param.setProductFullReductionList(productFullReductionList);
            param.setProductLadderList(productLadderList);
            param.setSkuStockList(skuStockList);
            param.setSubjectProductRelationList(subjectProductRelationList);
            redisService.set(apiContext.getCurrentProviderId()+":"+String.format(Rediskey.GOODSDETAIL, goods.getId()), JsonUtils.objectToJson(param));
        }
        return 1;
    }

    @Override
    public GoodsDetailResult getGoodsRedisById(Long id) {
        PmsProduct goods = productMapper.selectById(id);

        GoodsDetailResult param = new GoodsDetailResult();
        param.setGoods(goods);

      /*  List<PmsProductLadder> productLadderList = productLadderMapper.selectList(new QueryWrapper<PmsProductLadder>().eq("product_id", goods.getId()));

        List<PmsProductFullReduction> productFullReductionList = productFullReductionMapper.selectList(new QueryWrapper<PmsProductFullReduction>().eq("product_id", goods.getId()));

        List<PmsMemberPrice> memberPriceList = memberPriceMapper.selectList(new QueryWrapper<PmsMemberPrice>().eq("product_id", goods.getId()));
  param.setMemberPriceList(memberPriceList);
        param.setProductFullReductionList(productFullReductionList);
        param.setProductLadderList(productLadderList);
*/
        List<PmsSkuStock> skuStockList = skuStockMapper.selectList(new QueryWrapper<PmsSkuStock>().eq("product_id", goods.getId()));

        List<PmsProductAttributeValue> productAttributeValueList = productAttributeValueMapper.selectList(new QueryWrapper<PmsProductAttributeValue>().eq("product_id", goods.getId()).eq("type",1));

        List<CmsSubjectProductRelation> subjectProductRelationList = subjectProductRelationMapper.selectList(new QueryWrapper<CmsSubjectProductRelation>().eq("product_id", goods.getId()));

        List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList = prefrenceAreaProductRelationMapper.selectList(new QueryWrapper<CmsPrefrenceAreaProductRelation>().eq("product_id", goods.getId()));

        List<PmsProductAttributeValue> productCanShuValueList = productAttributeValueMapper.selectList(new QueryWrapper<PmsProductAttributeValue>().eq("product_id", goods.getId()).eq("type",2));
        param.setProductCanShuValueList(productCanShuValueList);

        param.setPrefrenceAreaProductRelationList(prefrenceAreaProductRelationList);
        param.setProductAttributeValueList(productAttributeValueList);

        param.setSkuStockList(skuStockList);
        param.setSubjectProductRelationList(subjectProductRelationList);
        param.setStoreInfo(storeMapper.selectById(apiContext.getCurrentProviderId()));
        List<PmsProduct> typeGoodsList = productMapper.selectList(new QueryWrapper<PmsProduct>().eq("product_attribute_category_id",goods.getProductAttributeCategoryId()).select(ConstansValue.sampleGoodsList));
        param.setTypeGoodsList(typeGoodsList.subList(0,typeGoodsList.size()>8?8:typeGoodsList.size()));
        redisService.set(apiContext.getCurrentProviderId()+":"+apiContext.getCurrentProviderId()+String.format(Rediskey.GOODSDETAIL, goods.getId()), JsonUtils.objectToJson(param));

        return param;
    }
    @Override
    public List<PmsBrand> getRecommendBrandList(int pageNum, int pageSize) {

        List<SmsHomeBrand> brands = homeBrandService.list(new QueryWrapper<>());
        if (brands!=null && brands.size()>0){
            List<Long> ids = brands.stream()
                    .map(SmsHomeBrand::getBrandId)
                    .collect(Collectors.toList());
            if (ids!=null && ids.size()>0) {
                return (List<PmsBrand>) brandService.listByIds(ids);
            }
        }
        return  new ArrayList<>();

    }
    @Override
    public List<PmsProduct> getNewProductList(int pageNum, int pageSize) {

        List<SmsHomeNewProduct> brands = homeNewProductService.list(new QueryWrapper<>());
        if (brands!=null && brands.size()>0){
            List<Long> ids = brands.stream()
                    .map(SmsHomeNewProduct::getProductId)
                    .collect(Collectors.toList());
            if (ids!=null && ids.size()>0) {
                return  productMapper.selectList(new QueryWrapper<PmsProduct>().in("id",ids).select(ConstansValue.sampleGoodsList));
            }
        }
        return  new ArrayList<>();
    }
    @Override
    public List<PmsProduct> getHotProductList(int pageNum, int pageSize) {
        List<SmsHomeRecommendProduct> brands = homeRecommendProductService.list(new QueryWrapper<>());
        if (brands!=null && brands.size()>0){
            List<Long> ids = brands.stream()
                    .map(SmsHomeRecommendProduct::getProductId)
                    .collect(Collectors.toList());
            if (ids!=null && ids.size()>0) {
                return  productMapper.selectList(new QueryWrapper<PmsProduct>().in("id",ids).select(ConstansValue.sampleGoodsList));
            }
        }
       return  new ArrayList<>();
    }

    @Override
    public  Integer countGoodsByToday(Long id){
        return productMapper.countGoodsByToday(id);
    }

    @Override
    public Map<String, Object> queryPaiMaigoodsDetail(Long id) {
        Map<String, Object> map = new HashMap<>();
        PmsProduct goods = productMapper.selectById(id);
        List<SmsPaimaiLog> paimaiLogList = paimaiLogMapper.selectList(new QueryWrapper<SmsPaimaiLog>().eq("goods_id",id).orderByDesc("create_time"));
        map.put("paimaiLogList", paimaiLogList);
        UmsMember umsMember = memberService.getNewCurrentMember();
        map.put("favorite", false);
        if (umsMember != null && umsMember.getId() != null) {
            PmsFavorite query = new PmsFavorite();
            query.setObjId(goods.getId());
            query.setMemberId(umsMember.getId());
            query.setType(1);
            PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
            if(findCollection!=null){
                map.put("favorite", true);
            }
        }
        //记录浏览量到redis,然后定时更新到数据库
        String key=Rediskey.GOODS_VIEWCOUNT_CODE+id;
        //找到redis中该篇文章的点赞数，如果不存在则向redis中添加一条
        Map<Object,Object> viewCountItem=redisUtil.hGetAll(Rediskey.GOODS_VIEWCOUNT_KEY);
        Integer viewCount=0;
        if(!viewCountItem.isEmpty()){
            if(viewCountItem.containsKey(key)){
                viewCount=Integer.parseInt(viewCountItem.get(key).toString())+1;
                redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY,key,viewCount+"");
            }else {
                redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY,key,1+"");
            }
        }else{
            redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY,key,1+"");
        }
        goods.setTimeSecound(ValidatorUtils.getTimeSecound(goods.getExpireTime()));
        map.put("goods", goods);
        return map;
    }

    @Transactional
    @Override
    public Object updatePaiMai(PmsProduct goods) {
        goods.setExpireTime(DateUtils.strToDate(DateUtils.addMins(goods.getExpireTime(),5)));
        productMapper.updateById(goods);
        SmsPaimaiLog log = new SmsPaimaiLog();
        log.setCreateTime(new Date());
        log.setGoodsId(goods.getId());
        log.setMemberId(memberService.getNewCurrentMember().getId());
        log.setPrice(goods.getOriginalPrice());
        log.setPic(memberService.getNewCurrentMember().getIcon());
        paimaiLogMapper.insert(log);
        return new CommonResult().success();
    }
}
