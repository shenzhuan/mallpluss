package com.zscat.mallplus.b2c;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.entity.CmsFavorite;
import com.zscat.mallplus.cms.service.ICmsFavoriteService;
import com.zscat.mallplus.cms.service.ICmsSubjectCategoryService;
import com.zscat.mallplus.cms.service.ICmsSubjectCommentService;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.pms.entity.*;
import com.zscat.mallplus.pms.mapper.PmsProductCategoryMapper;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.pms.service.*;
import com.zscat.mallplus.pms.vo.ConsultTypeCount;
import com.zscat.mallplus.pms.vo.GoodsDetailResult;
import com.zscat.mallplus.pms.vo.ProductTypeVo;
import com.zscat.mallplus.single.ApiBaseAction;
import com.zscat.mallplus.sms.entity.SmsGroup;
import com.zscat.mallplus.sms.entity.SmsGroupActivity;
import com.zscat.mallplus.sms.entity.SmsGroupMember;
import com.zscat.mallplus.sms.mapper.SmsGroupMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupMemberMapper;
import com.zscat.mallplus.sms.service.ISmsGroupActivityService;
import com.zscat.mallplus.sms.service.ISmsGroupService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberLevel;
import com.zscat.mallplus.ums.service.IUmsMemberLevelService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.ApiContext;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@Slf4j
@RestController
@Api(tags = "SingePmsController", description = "商品关系管理")
public class BPmsController extends ApiBaseAction {

    @Resource
    private IUmsMemberService memberService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ISmsGroupService groupService;
    @Resource
    private SmsGroupMapper groupMapper;
    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private IPmsProductService pmsProductService;
    @Resource
    private IPmsProductAttributeCategoryService productAttributeCategoryService;
    @Resource
    private IPmsProductCategoryService productCategoryService;
    @Resource
    private IPmsBrandService IPmsBrandService;
    @Resource
    private ISmsGroupActivityService smsGroupActivityService;
    @Resource
    private ICmsSubjectCategoryService subjectCategoryService;
    @Resource
    private ICmsSubjectService subjectService;
    @Resource
    private ICmsSubjectCommentService commentService;
    @Autowired
    private ISmsHomeAdvertiseService advertiseService;
    @Resource
    private PmsProductMapper productMapper;
    @Resource
    private RedisService redisService;
    @Autowired
    private IPmsProductConsultService pmsProductConsultService;
    @Autowired
    private IPmsFavoriteService favoriteService;
    @Resource
    private SmsGroupMemberMapper groupMemberMapper;
    @Resource
    private  PmsProductCategoryMapper categoryMapper;
    @Resource
    private IPmsGiftsService giftsService;
    @Resource
    private IPmsGiftsCategoryService giftsCategoryService;
    @Resource
    private SysStoreMapper storeMapper;
    @Resource
    private OmsOrderMapper omsOrderMapper;

    @Autowired
    private ApiContext apiContext;

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @PostMapping(value = "/goods.getdetial")
    @ApiOperation(value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get(apiContext.getCurrentProviderId()+":"+String.format(Rediskey.GOODSDETAIL, id+"")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods) || ValidatorUtils.empty(goods.getGoods())){
                log.info("redis缓存失效："+id);
                goods = pmsProductService.getGoodsRedisById(id);
            }
        } catch (Exception e) {
            log.info("redis缓存失效："+id);
            goods = pmsProductService.getGoodsRedisById(id);
        }
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            PmsProduct p = goods.getGoods();
            PmsFavorite query = new PmsFavorite();
            query.setObjId(p.getId());
            query.setMemberId(umsMember.getId());
            query.setType(1);
            PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
            if(findCollection!=null){
                map.put("favorite", true);
            }else{
                map.put("favorite", false);
            }
        }
        //记录浏览量到redis,然后定时更新到数据库
        recordGoodsFoot(id);

        map.put("goods", goods);
        return new CommonResult().success(map);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品列表")
    @PostMapping(value = "/goods.getlist")
    public Object goodsList(PmsProduct product,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        product.setPublishStatus(1);
        product.setVerifyStatus(1);
        product.setMemberId(null);
        IPage<PmsProduct> list;
        if (ValidatorUtils.notEmpty(product.getKeyword())){
            list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).like("name",product.getKeyword()));
        }else{
            list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product));
        }
        return new CommonResult().success(list);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品分类列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品分类列表")
    @PostMapping(value = "/productCategoryList")
    public Object productCategoryList(PmsProductCategory productCategory,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(productCategoryService.page(new Page<PmsProductCategory>(pageNum, pageSize), new QueryWrapper<>(productCategory)));
    }
    @SysLog(MODULE = "pms", REMARK = "查询商品分类列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品分类列表和子分类")
    @PostMapping(value = "/pidCategoryList")
    public Object pidCategoryList() throws Exception {
        String json = redisService.get(Rediskey.categoryAndChilds+apiContext.getCurrentProviderId());
        List<PmsProductCategory> list = new ArrayList<>();
        try {
            if (ValidatorUtils.empty(json)){
                list = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id",0).eq("show_status",1));
                for (PmsProductCategory category : list){
                    List<PmsProductCategory> childlist = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id",category.getId()).eq("show_status",1));
                    category.setChildList(childlist);
                }
                redisService.set(Rediskey.categoryAndChilds+apiContext.getCurrentProviderId(),JsonUtils.objectToJson(list));
                redisService.expire(Rediskey.categoryAndChilds+apiContext.getCurrentProviderId(),3600*5);
            }else{
                list = JsonUtils.json2list(json, PmsProductCategory.class);
            }
        }catch (Exception e){
            list = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id",0).eq("show_status",1));
            for (PmsProductCategory category : list){
                List<PmsProductCategory> childlist = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id",category.getId()).eq("show_status",1));
                category.setChildList(childlist);
            }
            redisService.set(Rediskey.categoryAndChilds+apiContext.getCurrentProviderId(),JsonUtils.objectToJson(list));
            redisService.expire(Rediskey.categoryAndChilds+apiContext.getCurrentProviderId(),3600*5);
        }

        return new CommonResult().success(list);
    }

    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有品牌表列表")
    @ApiOperation("根据条件查询所有品牌表列表")
    @PostMapping(value = "/brand/list")
    public Object getPmsBrandByPage(PmsBrand entity,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IPmsBrandService.page(new Page<PmsBrand>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有品牌表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @IgnoreAuth
    @ApiOperation("获取某个商品的评价")
    @RequestMapping(value = "/goods.getgoodscomment", method = RequestMethod.POST)
    @ResponseBody
    public Object list(@RequestParam(value = "goodsId", required = false, defaultValue = "0") Long goodsId,
                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        Map<String, Object> objectMap = new HashMap<>();
        String json = redisService.get(Rediskey.goodsConsult+apiContext.getCurrentProviderId()+"goods"+goodsId);
        if (ValidatorUtils.notEmpty(json)){
            objectMap = JsonUtils.readJsonToMap1(json);
            return   new CommonResult().success(objectMap);
        }

        PmsProductConsult productConsult = new PmsProductConsult();
        productConsult.setGoodsId(goodsId);
        productConsult.setType(AllEnum.ConsultType.GOODS.code());
        List<PmsProductConsult> list =  pmsProductConsultService.list(new QueryWrapper<>(productConsult));

        int goods = 0;
        int general = 0;
        int bad = 0;
        ConsultTypeCount count = new ConsultTypeCount();
        for (PmsProductConsult consult : list) {
            if (consult.getStars() != null) {
                if (consult.getStars()==1){
                    bad++;
                }
                if (consult.getStars()==2){
                    general++;
                }
                if (consult.getStars()==3){
                    goods++;
                }
            }
        }
        count.setAll(list.size());
        count.setBad(bad);
        count.setGeneral(general);
        count.setGoods(goods);
        if (count.getAll()>0){
            count.setPersent(new BigDecimal(goods).divide(new BigDecimal(count.getAll())).multiply(new BigDecimal(100)));
        }else {
            count.setPersent(new BigDecimal(200));
        }

        objectMap.put("list", list);
        objectMap.put("count", count);
        redisService.set(Rediskey.goodsConsult+apiContext.getCurrentProviderId()+"goods"+goodsId,JsonUtils.objectToJson(objectMap));
        redisService.expire(Rediskey.goodsConsult+apiContext.getCurrentProviderId()+"goods"+goodsId,3600*5);
        return new CommonResult().success(objectMap);
    }

    @SysLog(MODULE = "pms", REMARK = "查询团购商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询拼团列表")
    @PostMapping(value = "/groupHotGoods/list")
    public Object groupHotGoods(PmsProduct product,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        List<SmsGroup> groupList =  groupService.list(new QueryWrapper<>());
        List<SmsGroup> result = new ArrayList<>();
        for (SmsGroup group :groupList){
            if (ValidatorUtils.empty(group.getHours())){
                continue;
            }
            Long nowT = System.currentTimeMillis();
            Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(group.getEndTime(), group.getHours()), "yyyy-MM-dd HH:mm:ss");
            if (nowT > group.getStartTime().getTime() && nowT < endTime.getTime()) {
                PmsProduct g =pmsProductService.getById(group.getGoodsId());
                if(g!=null){
                    group.setGoods(g);
                    result.add(group);
                }

            }
        }
        return new CommonResult().success(result);
    }

    @SysLog(MODULE = "pms", REMARK = "查询团购商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询拼团商品列表")
    @PostMapping(value = "/pintuan.list")
    public Object pintuanList(PmsProduct product,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
       List<SmsGroup> groupList =  groupService.list(new QueryWrapper<>());
       if (groupList!=null && groupList.size()>0){
           List<Long> ids = groupList.stream()
                   .map(SmsGroup::getGoodsId)
                   .collect(Collectors.toList());
           product.setPublishStatus(1);
           product.setVerifyStatus(1);
           product.setMemberId(null);
           IPage<PmsProduct> list  = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).in("id",ids));
           return new CommonResult().success(list);
       }
        return null;
    }

    @SysLog(MODULE = "pms", REMARK = "拼团商品详情")
    @IgnoreAuth
    @PostMapping(value = "/pintuan.goodsinfo")
    @ApiOperation(value = "拼团商品详情")
    public Object groupGoodsDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
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
                viewCount=1;
                redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY,key,1+"");
            }
        }else{
            viewCount=1;
            redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY,key,1+"");
        }
        GoodsDetailResult goods = null;
        try {
              goods =JsonUtils.jsonToPojo(redisService.get(apiContext.getCurrentProviderId()+":"+String.format(Rediskey.GOODSDETAIL, id+"")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods)){
                log.info("redis缓存失效："+id);
                goods = pmsProductService.getGoodsRedisById(id);
            }
        } catch (Exception e) {
            log.info("redis缓存失效："+id);
            goods = pmsProductService.getGoodsRedisById(id);
            e.printStackTrace();
        }
        SmsGroup group = groupMapper.getByGoodsId(id);
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            PmsProduct p = goods.getGoods();
            p.setHit(viewCount);
            PmsFavorite query = new PmsFavorite();
            query.setObjId(p.getId());
            query.setMemberId(umsMember.getId());
            query.setType(1);
            PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
            if(findCollection!=null){
                map.put("favorite", true);
            }else{
                map.put("favorite", false);
            }
        }
        if (group!=null){
            group.setPintuan_start_status(1);
            group.setTimeSecound(ValidatorUtils.getTimeSecound(group.getEndTime()));
            Long nowT = System.currentTimeMillis();
            Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(group.getEndTime(), group.getHours()), "yyyy-MM-dd HH:mm:ss");
            if (nowT < group.getStartTime().getTime() ) {
                group.setPintuan_start_status(2);
            }
            if ( nowT > endTime.getTime()) {
                group.setPintuan_start_status(3);
            }
            List<SmsGroupMember> list = new ArrayList<>();
            List<SmsGroupMember> groupMembers = groupMemberMapper.selectList(new QueryWrapper<SmsGroupMember>().eq("group_id",group.getId()));
            for (SmsGroupMember groupMember : groupMembers){
                if (ValidatorUtils.notEmpty(groupMember.getMemberId()) ){
                    List<String> ids = Arrays.asList(groupMember.getMemberId().split(","));
                    if (group.getMaxPeople()>ids.size()){
                        groupMember.setList((List<UmsMember>) memberService.listByIds(ids));
                    }else {
                        continue;
                    }
                    list.add(groupMember);

                }
            }
            map.put("memberGroupList",list);
            map.put("group", group);
        }


        map.put("goods", goods);
        return new CommonResult().success(map);
    }


    @SysLog(MODULE = "pms", REMARK = "查询团购商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询礼物商品列表")
    @PostMapping(value = "/gift/list")
    public Object giftList(PmsGifts product,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

            IPage<PmsGifts> list  = giftsService.page(new Page<PmsGifts>(pageNum, pageSize), new QueryWrapper<>(product));
            return new CommonResult().success(list);

    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @PostMapping(value = "/gift/detail")
    @ApiOperation(value = "查询礼物商品详情信息")
    public Object giftDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        PmsGifts  goods = giftsService.getById(id);
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            PmsFavorite query = new PmsFavorite();
            query.setObjId(goods.getId());
            query.setMemberId(umsMember.getId());
            query.setType(4);
            PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
            if(findCollection!=null){
                map.put("favorite", true);
            }else{
                map.put("favorite", false);
            }
        }
        map.put("goods", goods);
        return new CommonResult().success(map);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品类型下的商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询积分商品类型")
    @PostMapping(value = "/typeGiftList")
    public Object typeGiftList(PmsGiftsCategory productCategory) {
        List<PmsGiftsCategory> categories = giftsCategoryService.list(new QueryWrapper<>(productCategory));
        return new CommonResult().success(categories);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品分类列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品分类和分类的商品列表")
    @PostMapping(value = "/categories.getallcat")
    public Object categoryAndGoodsList(PmsProductAttributeCategory productCategory) throws Exception {
        List<PmsProductAttributeCategory> productAttributeCategoryList = new ArrayList<>();
        String json = redisService.get(Rediskey.categoryAndGoodsList+apiContext.getCurrentProviderId());
        if (ValidatorUtils.notEmpty(json)){
            productAttributeCategoryList = JsonUtils.json2list(json,PmsProductAttributeCategory.class);
            return   new CommonResult().success(productAttributeCategoryList);
        }
        productAttributeCategoryList = productAttributeCategoryService.list(new QueryWrapper<>());
        for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
            PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(gt.getId());
            productQueryParam.setPublishStatus(1);
            productQueryParam.setVerifyStatus(1);
            gt.setGoodsList(pmsProductService.list(new QueryWrapper<>(productQueryParam).select(ConstansValue.sampleGoodsList)));
        }
        redisService.set(Rediskey.categoryAndGoodsList+apiContext.getCurrentProviderId(),JsonUtils.objectToJson(productAttributeCategoryList));
        redisService.expire(Rediskey.categoryAndGoodsList+apiContext.getCurrentProviderId(),3600*5);
        return new CommonResult().success(productAttributeCategoryList);
    }




    @SysLog(MODULE = "pms", REMARK = "查询首页热销商品")
    @IgnoreAuth
    @ApiOperation(value = "查询首页热销商品")
    @PostMapping(value = "/hotProductList/list")
    public Object getHotProductList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(pmsProductService.getHotProductList(pageNum,pageSize));
    }


    @SysLog(MODULE = "pms", REMARK = "查询商品类型下的商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品类型下的商品列表")
    @PostMapping(value = "/typeGoodsList")
    public Object typeGoodsList() throws Exception {
        List<ProductTypeVo> relList = new ArrayList<>();
        String json = redisService.get(Rediskey.specialcategoryAndGoodsList+apiContext.getCurrentProviderId());
        if (ValidatorUtils.notEmpty(json)){
            relList = JsonUtils.json2list(json,ProductTypeVo.class);
            return   new CommonResult().success(relList);
        }

        PmsProduct productQueryParam = new PmsProduct();
        productQueryParam.setPublishStatus(1);
        productQueryParam.setVerifyStatus(1);
        List<PmsProduct>  list = pmsProductService.page(new Page<PmsProduct>(1, 8), new QueryWrapper<>(productQueryParam)).getRecords();
        for (PmsProduct l : list){
            ProductTypeVo vo = new ProductTypeVo();
            vo.setGoodsId(l.getId());
            vo.setId(l.getId());
            vo.setPic(l.getPic());
            vo.setName(l.getName());
            vo.setPrice(l.getPrice());
            vo.setPid(l.getProductCategoryId());
            relList.add(vo);
        }
        List<PmsProductCategory> categories = categoryMapper.selectList(new QueryWrapper<>());
        for (PmsProductCategory v : categories){
            if (v.getParentId()==0){
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                relList.add(vo);
            }else{
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                vo.setPid(v.getParentId());
                relList.add(vo);
            }
        }
        redisService.set(Rediskey.specialcategoryAndGoodsList+apiContext.getCurrentProviderId(),JsonUtils.objectToJson(relList));
        redisService.expire(Rediskey.specialcategoryAndGoodsList+apiContext.getCurrentProviderId(),3600*5);
        return new CommonResult().success(relList);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品类型下的商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品类型下的商品列表")
    @PostMapping(value = "/typeList")
    public Object typeList(PmsProductCategory productCategory) {
        List<ProductTypeVo> relList = new ArrayList<>();
        List<PmsProductCategory> categories = categoryMapper.selectList(new QueryWrapper<>());
        for (PmsProductCategory v : categories){
            if (v.getParentId()==0){
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                relList.add(vo);
            }else{
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                vo.setPid(v.getParentId());
                relList.add(vo);
            }
        }

        return new CommonResult().success(relList);
    }
    @SysLog(MODULE = "cms", REMARK = "添加商品评论")
    @ApiOperation(value = "添加商品评论")
    @PostMapping(value = "/addGoodsConsult")
    public Object addGoodsConsult(PmsProductConsult subject, BindingResult result) {
        CommonResult commonResult;
        UmsMember member = memberService.getNewCurrentMember();
        if (member!=null){
            subject.setPic(member.getIcon());
            subject.setMemberName(member.getNickname());
            subject.setMemberId(member.getId());
        }else {
            return new CommonResult().failed("请先登录");
        }
        subject.setConsultAddtime(new Date());
        subject.setType(AllEnum.ConsultType.GOODS.code());
        boolean count = pmsProductConsultService.save(subject);
        if (count) {
            commonResult = new CommonResult().success(count);
        } else {
            commonResult = new CommonResult().failed();
        }
        return commonResult;
    }
    @IgnoreAuth
    @ApiOperation("添加商品浏览记录")
    @SysLog(MODULE = "pms", REMARK = "添加商品浏览记录")
    @PostMapping(value = "/user.addgoodsbrowsing")
    public Object addView(@RequestParam  Long goodsId) {

        String key = String.format(Rediskey.GOODSHISTORY, memberService.getNewCurrentMember().getId());

        //为了保证浏览商品的 唯一性,每次添加前,将list 中该 商品ID去掉,在加入,以保证其浏览的最新的商品在最前面

        redisUtil.lRemove(key, 1, goodsId.toString());
        //将value push 到该key下的list中
        redisUtil.lLeftPush(key,goodsId.toString());
        //使用ltrim将60个数据之后的数据剪切掉
        redisUtil.lTrim(key,0,59);
        //设置缓存时间为一个月
        redisUtil.expire(key,60*60*24*30, TimeUnit.SECONDS);
        return new CommonResult().success();
    }

    @SysLog(MODULE = "pms", REMARK = "查询用户浏览记录列表")
    @IgnoreAuth
    @ApiOperation(value = "查询用户浏览记录列表")
    @PostMapping(value = "/user.goodsbrowsing")
    public Object viewList(
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        String key = String.format(Rediskey.GOODSHISTORY, memberService.getNewCurrentMember().getId());

        //获取用户的浏览的商品的总页数;
        long pageCount = redisUtil.lLen(key);
        //拼装返回
        Map<String,Object> map = new HashMap<>();
        //根据用户的ID分頁获取该用户最近浏览的50个商品信息
        List<String> result = redisUtil.lRange(key,(pageNum-1)*pageSize,pageNum*pageSize-1);
        if (result!=null && result.size()>0){
            List<PmsProduct> list = (List<PmsProduct>) pmsProductService.listByIds(result);

            map.put("result",list);
            map.put("total",pageCount);
            map.put("pageCount",(pageCount%pageSize == 0 ? pageCount/pageSize : pageCount/pageSize+1));
        }

        return new CommonResult().success(map);
    }

    @Autowired
    private IPmsFavoriteService memberCollectionService;
    @Autowired
    private ICmsFavoriteService cmsFavoriteService;

    @ApiOperation("添加和取消收藏 type 1 商品 2 文章")
    @PostMapping("user.goodscollection")
    public Object favoriteSave(PmsFavorite productCollection) {
        if (memberService.getNewCurrentMember().getId()==null){
            return new CommonResult().fail(100);
        }
        int count = memberCollectionService.addProduct(productCollection);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }
    @ApiOperation("删除收藏中的某个商品")
    @PostMapping(value = "/batchcollect.delete")
    public Object batchcollectDel(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new CommonResult().failed("参数为空");
        }
        List<Long> resultList = new ArrayList<>(ids.split(",").length);
        for (String s : ids.split(",")) {
            resultList.add(Long.valueOf(s));
        }
        if (memberCollectionService.removeByIds(resultList)) {
            return new CommonResult().success();
        }
        return new CommonResult().failed();
    }
    @ApiOperation("删除收藏中的某个商品")
    @PostMapping(value = "/collect.delete")
    public Object delete(Long id) {
        if (StringUtils.isEmpty(id)) {
            return new CommonResult().failed("参数为空");
        }
        if (memberCollectionService.removeById(id)) {
            return new CommonResult().success();
        }
        return new CommonResult().failed();
    }

    @ApiOperation("显示收藏列表")
    @PostMapping(value = "/user.goodscollectionlist")
    public Object listCollectByType( PmsFavorite productCollection,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        productCollection.setMemberId(memberService.getNewCurrentMember().getId());
        return new CommonResult().success(memberCollectionService.page(new Page<PmsFavorite>(pageNum, pageSize), new QueryWrapper<>(productCollection).orderByDesc("add_time")));
    }



    @ApiOperation("添加和取消点赞 type 1 商品 2 文章")
    @PostMapping("likeSave")
    public Object likeSave(CmsFavorite productCollection) {
        int count = cmsFavoriteService.addProduct(productCollection);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("删除点赞中的某个商品")
    @PostMapping(value = "/deleteLike")
    public Object deleteLike(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new CommonResult().failed("参数为空");
        }
        List<Long> resultList = new ArrayList<>(ids.split(",").length);
        for (String s : ids.split(",")) {
            resultList.add(Long.valueOf(s));
        }

        if (cmsFavoriteService.removeByIds(resultList)) {
            return new CommonResult().success();
        }
        return new CommonResult().failed();
    }

    @ApiOperation("显示点赞列表")
    @PostMapping(value = "/listLikeByType")
    public Object listLikeByType( CmsFavorite productCollection) {
        List<CmsFavorite> memberProductCollectionList = cmsFavoriteService.listProduct(memberService.getNewCurrentMember().getId(),productCollection.getType());
        return new CommonResult().success(memberProductCollectionList);
    }
    @ApiOperation("显示点赞列表")
    @PostMapping(value = "/listLike")
    public Object listLike( CmsFavorite productCollection) {
        List<CmsFavorite> memberProductCollectionList = cmsFavoriteService.listCollect(memberService.getNewCurrentMember().getId());
        return new CommonResult().success(memberProductCollectionList);
    }


    @ApiOperation("生成海报")
    @PostMapping(value = "/user.getposter")
    public Object getposter( @RequestParam Long id) {
        PmsProduct product = pmsProductService.getById(id);
        return new CommonResult().success(product.getPic());
    }

    @IgnoreAuth
    @ApiOperation("显示默认收货地址")
    @RequestMapping(value = "/store.getdefaultstore", method = RequestMethod.POST)
    @ResponseBody
    public Object getItemDefautl() {
        SysStore address = storeMapper.selectById(apiContext.getCurrentProviderId());
        return new CommonResult().success(address);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐商品")
    @PostMapping(value = "/initGoodsRedis")
    public Object initGoodsRedis() {

        return pmsProductService.initGoodsRedis();

    }

    @IgnoreAuth
    @SysLog(MODULE = "oms", REMARK = "查询团购列表")
    @ApiOperation(value = "查询订单列表")
    @ResponseBody
    @RequestMapping(value = "/groupActivityList", method = RequestMethod.POST)
    public Object orderList(SmsGroupActivity groupActivity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        IPage<SmsGroupActivity> page = null;
        groupActivity.setStatus(1);
        page = smsGroupActivityService.page(new Page<SmsGroupActivity>(pageNum, pageSize), new QueryWrapper<>(groupActivity).orderByDesc("create_time")) ;

        for (SmsGroupActivity smsGroupActivity : page.getRecords()) {
            if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
                List<PmsProduct> productList = (List<PmsProduct>) pmsProductService.list(new QueryWrapper<PmsProduct>().eq("id",Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList())).select(ConstansValue.sampleGoodsList));
                if (productList != null && productList.size() > 0) {
                    smsGroupActivity.setProductList(productList);
                }
            }

        }
        return new CommonResult().success(page);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品团购详情信息")
    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/group.activity.getdetial", method = RequestMethod.POST)
    @ApiOperation(value = "查询商品团购详情信息")
    public Object queryGroupProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SmsGroupActivity groupActivity = smsGroupActivityService.getById(id);
        Map<String, Object> map = new HashMap<>();
        if (groupActivity != null) {
            if (ValidatorUtils.notEmpty(groupActivity.getGoodsIds())) {
                List<Long> goodIds = Arrays.asList(groupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                GoodsDetailResult goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL, goodIds.get(0) + "")), GoodsDetailResult.class);
                if (goods == null || goods.getGoods() == null) {
                    goods = pmsProductService.getGoodsRedisById(goodIds.get(0));
                }
                if (goods != null && goods.getGoods() != null) {
                    UmsMember umsMember = memberService.getNewCurrentMember();
                    if (umsMember != null && umsMember.getId() != null) {
                        isCollectGoods(map, goods, umsMember);
                    }
                    recordGoodsFoot(id);
                    List<Long> newGoodIds = goodIds.subList(1, goodIds.size());
                    if (newGoodIds != null && newGoodIds.size() > 0) {
                        List<PmsProduct> productList = (List<PmsProduct>) pmsProductService.list(new QueryWrapper<PmsProduct>().eq("id",newGoodIds).select(ConstansValue.sampleGoodsList));
                        if (productList != null && productList.size() > 0) {
                            groupActivity.setProductList(productList);
                        }
                    }
                    map.put("groupActivity", groupActivity);
                    map.put("goods", goods);
                    return new CommonResult().success(map);
                }
            }

        }
        return new CommonResult().failed();
    }

    @ApiOperation("创建商品")
    @SysLog(MODULE = "pms", REMARK = "创建商品")
    @PostMapping(value = "/createGoods")
    public Object createGoods(PmsProduct productParam) {
        CommonResult commonResult;
        UmsMember member = memberService.getNewCurrentMember();
        if (member.getMemberLevelId() > 0) {
            UmsMemberLevel memberLevel = memberLevelService.getById(member.getMemberLevelId());
            Integer countGoodsByToday  = pmsProductService.countGoodsByToday(member.getId());
            if (ValidatorUtils.empty(countGoodsByToday)){
                countGoodsByToday=0;
            }
            if (countGoodsByToday > memberLevel.getGoodscount()) {
                commonResult = new CommonResult().failed("你今天已经有发" + countGoodsByToday + "个商品");
                return commonResult;
            }
        }else {
            return new CommonResult().success("没有设置会员等级");
        }
        if (productParam.getQsType()==1){
            productParam.setSchoolName(member.getSchoolName());
            productParam.setSchoolId(member.getSchoolId());
        }else {
            productParam.setAreaName(member.getAreaName());
            productParam.setAreaId(member.getAreaId());
        }
        productParam.setMemberId(member.getId());
        productParam.setCreateTime(new Date());
        boolean count = pmsProductService.save(productParam);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    private Integer recordGoodsFoot(Long id) {
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
            viewCount=1;
            redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY,key,1+"");
        }
        return viewCount;
    }

    /**
     * 判断是否收藏商品
     * @param map
     * @param goods
     * @param umsMember
     */
    private void isCollectGoods(Map<String, Object> map, GoodsDetailResult goods, UmsMember umsMember) {
        PmsProduct p = goods.getGoods();
        PmsFavorite query = new PmsFavorite();
        query.setObjId(p.getId());
        query.setMemberId(umsMember.getId());
        query.setType(1);
        PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
        if (findCollection != null) {
            map.put("favorite", true);
        } else {
            map.put("favorite", false);
        }
    }


}
