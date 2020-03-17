package com.zscat.mallplus.single;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.service.ICmsSubjectCategoryService;
import com.zscat.mallplus.cms.service.ICmsSubjectCommentService;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.fenxiao.entity.FenxiaoConfig;
import com.zscat.mallplus.fenxiao.mapper.FenxiaoConfigMapper;
import com.zscat.mallplus.pms.entity.*;
import com.zscat.mallplus.pms.mapper.PmsProductCategoryMapper;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.pms.service.*;
import com.zscat.mallplus.pms.vo.*;
import com.zscat.mallplus.sms.entity.SmsFlashPromotionProductRelation;
import com.zscat.mallplus.sms.entity.SmsGroup;
import com.zscat.mallplus.sms.entity.SmsGroupMember;
import com.zscat.mallplus.sms.entity.SmsGroupRecord;
import com.zscat.mallplus.sms.mapper.SmsGroupMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupMemberMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupRecordMapper;
import com.zscat.mallplus.sms.service.ISmsFlashPromotionProductRelationService;
import com.zscat.mallplus.sms.service.ISmsGroupService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
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
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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
@RequestMapping("/api/single/pms")
public class SingePmsController extends ApiBaseAction {

    @Resource
    FenxiaoConfigMapper fenxiaoConfigMapper;
    @Autowired
    private ISmsFlashPromotionProductRelationService smsFlashPromotionProductRelationService;
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
    private SmsGroupRecordMapper groupRecordMapper;

    @Resource
    private IPmsProductAttributeCategoryService productAttributeCategoryService;
    @Resource
    private IPmsProductCategoryService productCategoryService;
    @Resource
    private IPmsBrandService IPmsBrandService;

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
    private PmsProductCategoryMapper categoryMapper;
    @Resource
    private IPmsGiftsService giftsService;
    @Resource
    private IPmsGiftsCategoryService giftsCategoryService;

    @Autowired
    private IUmsMemberService memberService;

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/paimai/detail")
    @ApiOperation(value = "查询商品详情信息")
    public Object queryPaiMaiProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

        return new CommonResult().success(pmsProductService.queryPaiMaigoodsDetail(id));
    }


    @ApiOperation("创建商品")
    @SysLog(MODULE = "pms", REMARK = "创建商品")
    @PostMapping(value = "/updatePaiMai")
    public Object updatePaiMai(PaiMaiParam paiMaiParam) {
        PmsProduct goods = pmsProductService.getById(paiMaiParam.getId());
        if (paiMaiParam.getPrice().compareTo(goods.getOriginalPrice()) > 0) {
            goods.setOriginalPrice(paiMaiParam.getPrice());
            return pmsProductService.updatePaiMai(goods);
        } else {
            return new CommonResult().failed("出价低于上次价格");
        }
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/goods/detail")
    @ApiOperation(value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get( String.format(Rediskey.GOODSDETAIL, id + "")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods) || ValidatorUtils.empty(goods.getGoods())) {
                log.info("redis缓存失效：" + id);
                goods = pmsProductService.getGoodsRedisById(id);
            }
        } catch (Exception e) {
            log.info("redis缓存失效：" + id);
            goods = pmsProductService.getGoodsRedisById(id);
        }
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            isCollectGoods(map, goods, umsMember);
        }
        //记录浏览量到redis,然后定时更新到数据库
        recordGoodsFoot(id);

        map.put("goods", goods);
        return new CommonResult().success(map);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/goods/category")
    @ApiOperation(value = "查询商品详情信息")
    public Object category(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        PmsProductCategory cur = productCategoryService.getById(id);
        PmsProductCategory parent = null;
        List<PmsProductCategory> children = null;

        if (cur.getParentId() == 0) {
            parent = cur;
            children = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id", cur.getParentId()));
            cur = children.size() > 0 ? children.get(0) : cur;
        } else {
            parent = productCategoryService.getById(cur.getParentId());
            children = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id", cur.getParentId()));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("currentCategory", cur);
        data.put("parentCategory", parent);
        data.put("brotherCategory", children);
        return new CommonResult().success(data);

    }

    /**
     * 当前分类栏目
     *
     * @param id 分类类目ID
     * @return 当前分类栏目
     */
    @GetMapping("catalog/current")
    public Object current(@NotNull Integer id) {
        // 当前分类
        PmsProductCategory currentCategory = productCategoryService.getById(id);
        if (currentCategory == null) {
            return new CommonResult().paramFailed();
        }
        List<PmsProductCategory>
                currentSubCategory = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id", currentCategory.getParentId()));

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);
        return new CommonResult().success(data);
    }

    /**
     * 分类详情
     *
     * @param id 分类类目ID。
     *           如果分类类目ID是空，则选择第一个分类类目。
     *           需要注意，这里分类类目是一级类目
     * @return 分类详情
     */
    @GetMapping("catalog/index")
    public Object index(Integer id) {

        // 所有一级分类目录
        List<PmsProductCategory> l1CatList = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("level", 1));

        // 当前一级分类目录
        PmsProductCategory currentCategory = null;
        if (id != null) {
            currentCategory = productCategoryService.getById(id);
        } else {
            currentCategory = l1CatList.get(0);
        }

        // 当前一级分类目录对应的二级分类目录
        List<PmsProductCategory> currentSubCategory = null;
        if (null != currentCategory) {
            currentSubCategory = productCategoryService.list(new QueryWrapper<PmsProductCategory>().eq("parent_id", currentCategory.getParentId()));
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("categoryList", l1CatList);
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);
        return new CommonResult().success(data);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/goods/detail1")
    @ApiOperation(value = "查询商品详情信息")
    public Object queryProductDetail1(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL1, id + "")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods) || ValidatorUtils.empty(goods.getGoods())) {
                log.info("redis缓存失效：" + id);
                goods = pmsProductService.getGoodsRedisById1(id);
            }
        } catch (Exception e) {
            log.info("redis缓存失效：" + id);
            goods = pmsProductService.getGoodsRedisById1(id);
        }
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            isCollectGoods(map, goods, umsMember);
        }
        //记录浏览量到redis,然后定时更新到数据库
        recordGoodsFoot(id);

        map.put("goods", goods);
        return new CommonResult().success(map);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品列表")
    @GetMapping(value = "/goods/list")
    public Object goodsList(
            @RequestParam(value = "storeId", required = false) Integer storeId,
            @RequestParam(value = "areaId", required = false) Long areaId,
            @RequestParam(value = "schoolId", required = false) Long schoolId,
            @RequestParam(value = "productAttributeCategoryId", required = false) Long productAttributeCategoryId,
            @RequestParam(value = "productCategoryId", required = false) Long productCategoryId,
            @RequestParam(value = "recommandStatus", required = false) Integer recommandStatus,
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "sort", required = false) Integer sort,
            @RequestParam(value = "orderBy", required = false, defaultValue = "1") Integer orderBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        PmsProduct product = new PmsProduct();
        product.setPublishStatus(1);
        product.setVerifyStatus(1);
        product.setMemberId(null);
        product.setSort(sort);

        if (ValidatorUtils.notEmpty(productCategoryId) && productCategoryId > 0) {
            product.setProductCategoryId(productCategoryId);
        }
        if (ValidatorUtils.notEmpty(recommandStatus) && recommandStatus > 0) {
            product.setRecommandStatus(1);
        }
        if (ValidatorUtils.notEmpty(brandId) && brandId > 0) {
            product.setBrandId(brandId);
        }
        if (ValidatorUtils.notEmpty(productAttributeCategoryId) && productAttributeCategoryId > 0) {
            product.setProductAttributeCategoryId(productAttributeCategoryId);
        }
        if (ValidatorUtils.notEmpty(storeId) && storeId > 0) {
            product.setStoreId(storeId);
        }
        if (ValidatorUtils.notEmpty(areaId) && areaId > 0) {
            product.setAreaId(areaId);
        }
        if (ValidatorUtils.notEmpty(schoolId) && schoolId > 0) {
            product.setSchoolId(schoolId);
        }
        String orderColum = "create_time";
        if (ValidatorUtils.notEmpty(product.getSort())) {
            if (product.getSort() == 1) {
                orderColum = "sale";
            } else if (product.getSort() == 2) {
                orderColum = "price";
            } else if (product.getSort() == 3) {

            }
        }
        product.setSort(null);
        IPage<PmsProduct> list;
        if (ValidatorUtils.notEmpty(keyword)) {
            if (orderBy==1) {
                list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).like("name", keyword).select(ConstansValue.sampleGoodsList).orderByDesc(orderColum));
                buildFenPrice(list);
            } else {
                list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).like("name", keyword).select(ConstansValue.sampleGoodsList).orderByAsc(orderColum));
                buildFenPrice(list);
            }
        } else {
            if (orderBy==1) {
                list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).select(ConstansValue.sampleGoodsList).orderByDesc(orderColum));
                buildFenPrice(list);
            } else {
                list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).select(ConstansValue.sampleGoodsList).orderByAsc(orderColum));
                buildFenPrice(list);
            }
        }
        return new CommonResult().success(list);
    }

    private void buildFenPrice(IPage<PmsProduct> list) {
        if (list != null && list.getRecords() != null && list.getRecords().size() > 0) {
            for (PmsProduct pmsProduct : list.getRecords()) {
                if (pmsProduct.getIsFenxiao() != null && pmsProduct.getIsFenxiao() == 1) {
                    FenxiaoConfig fenxiaoConfig = fenxiaoConfigMapper.selectById(pmsProduct.getStoreId());
                    if (fenxiaoConfig != null && fenxiaoConfig.getStatus() == 1 && fenxiaoConfig.getOnePercent() > 0) {
                        pmsProduct.setFenxiaoPrice(pmsProduct.getPrice().multiply(new BigDecimal(fenxiaoConfig.getOnePercent())).divide(BigDecimal.valueOf(100)));
                    }
                }
            }
        }
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品分类列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品分类列表")
    @GetMapping(value = "/productCategory/list")
    public Object productCategoryList(PmsProductCategory productCategory,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(productCategoryService.page(new Page<PmsProductCategory>(pageNum, pageSize), new QueryWrapper<>(productCategory)));
    }
    @SysLog(MODULE = "pms", REMARK = "查询商品分类列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品分类列表")
    @GetMapping(value = "/productAttrCategory/list")
    public Object productCategoryList(PmsProductAttributeCategory productCategory,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(productAttributeCategoryService.page(new Page<PmsProductAttributeCategory>(pageNum, pageSize), new QueryWrapper<>(productCategory)));
    }

    @ApiOperation("创建商品")
    @SysLog(MODULE = "pms", REMARK = "创建商品")
    @PostMapping(value = "/createGoods")
    public Object createGoods(PmsProduct productParam) {
        CommonResult commonResult;
        UmsMember member = memberService.getNewCurrentMember();
        if (member.getMemberLevelId() > 0) {
            UmsMemberLevel memberLevel = memberLevelService.getById(member.getMemberLevelId());
            Integer countGoodsByToday = pmsProductService.countGoodsByToday(member.getId());
            if (ValidatorUtils.empty(countGoodsByToday)) {
                countGoodsByToday = 0;
            }
            if (countGoodsByToday > memberLevel.getGoodscount()) {
                commonResult = new CommonResult().failed("你今天已经有发" + countGoodsByToday + "个商品");
                return commonResult;
            }
        } else {
            return new CommonResult().success("没有设置会员等级");
        }
        if (productParam.getQsType() == 1) {
            productParam.setSchoolName(member.getSchoolName());
            productParam.setSchoolId(member.getSchoolId());
        } else {
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


    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有品牌表列表")
    @ApiOperation("根据条件查询所有品牌表列表")
    @GetMapping(value = "/brand/list")
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


    @SysLog(MODULE = "pms", REMARK = "查询品牌详情信息")
    @IgnoreAuth
    @GetMapping(value = "/brand/detail")
    @ApiOperation(value = "查询品牌详情信息")
    public Object queryBrandDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        return new CommonResult().success(IPmsBrandService.getById(id));

    }

    @SysLog(MODULE = "cms", REMARK = "添加商品评论")
    @ApiOperation(value = "添加商品评论")
    @PostMapping(value = "/addGoodsConsult")
    public Object addGoodsConsult(PmsProductConsult subject, BindingResult result) {
        CommonResult commonResult;
        UmsMember member = memberService.getNewCurrentMember();
        if (member != null) {
            subject.setPic(member.getIcon());
            subject.setMemberName(member.getNickname());
            subject.setMemberId(member.getId());
        } else {
            return new CommonResult().failed("请先登录");
        }
        subject.setConsultAddtime(new Date());
        boolean count = pmsProductConsultService.save(subject);
        if (count) {
            commonResult = new CommonResult().success(count);
        } else {
            commonResult = new CommonResult().failed();
        }
        return commonResult;
    }

    @IgnoreAuth
    @ApiOperation("获取某个商品的评价")
    @RequestMapping(value = "/consult/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "goodsId", required = false, defaultValue = "0") Long goodsId,
                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        PmsProductConsult productConsult = new PmsProductConsult();
        productConsult.setGoodsId(goodsId);
        List<PmsProductConsult> list = pmsProductConsultService.list(new QueryWrapper<>(productConsult));

        int goods = 0;
        int general = 0;
        int bad = 0;
        ConsultTypeCount count = new ConsultTypeCount();
        for (PmsProductConsult consult : list) {
            if (consult.getStars() != null) {
                if (consult.getStars() == 1) {
                    bad++;
                }
                if (consult.getStars() == 2) {
                    general++;
                }
                if (consult.getStars() == 3) {
                    goods++;
                }
            }
        }
        count.setAll(goods + general + bad);
        count.setBad(bad);
        count.setGeneral(general);
        count.setGoods(goods);
        if (count.getAll() > 0) {
            count.setPersent(new BigDecimal(goods).divide(new BigDecimal(count.getAll())).multiply(new BigDecimal(100)));
        } else {
            count.setPersent(new BigDecimal(200));
        }
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("list", list);
        objectMap.put("count", count);
        return new CommonResult().success(objectMap);
    }

    @SysLog(MODULE = "pms", REMARK = "查询团购商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询带团购商品列表")
    @GetMapping(value = "/groupHotGoods/list")
    public Object groupHotGoods(PmsProduct product,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        List<SmsGroup> groupList = groupService.list(new QueryWrapper<>());
        List<SmsGroup> result = new ArrayList<>();
        for (SmsGroup group : groupList) {
            if (ValidatorUtils.empty(group.getHours())) {
                continue;
            }
            Long nowT = System.currentTimeMillis();
            Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(group.getEndTime(), group.getHours()), "yyyy-MM-dd HH:mm:ss");
            if (nowT > group.getStartTime().getTime() && nowT < endTime.getTime()) {
                PmsProduct g = pmsProductService.getById(group.getGoodsId());
                if (g != null) {
                    group.setGoods(g);
                    result.add(group);
                }

            }
        }
        return new CommonResult().success(result);
    }

    @SysLog(MODULE = "pms", REMARK = "查询团购商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询团购商品列表")
    @GetMapping(value = "/groupGoods/list")
    public Object groupGoodsList(PmsProduct product,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        List<SmsGroup> groupList = groupService.list(new QueryWrapper<>());
        if (groupList != null && groupList.size() > 0) {
            List<Long> ids = groupList.stream()
                    .map(SmsGroup::getGoodsId)
                    .collect(Collectors.toList());
            product.setPublishStatus(1);
            product.setVerifyStatus(1);
            product.setMemberId(null);
            IPage<PmsProduct> list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).in("id", ids));
            return new CommonResult().success(list);
        }
        return null;
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/goodsGroup/detail")
    @ApiOperation(value = "查询商品详情信息")
    public Object groupGoodsDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        //记录浏览量到redis,然后定时更新到数据库

        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get( String.format(Rediskey.GOODSDETAIL, id + "")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods)) {
                log.info("redis缓存失效：" + id);
                goods = pmsProductService.getGoodsRedisById(id);
            }
        } catch (Exception e) {
            log.info("redis缓存失效：" + id);
            goods = pmsProductService.getGoodsRedisById(id);
            e.printStackTrace();
        }
        SmsGroup group = groupMapper.getByGoodsId(id);
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            PmsProduct p = goods.getGoods();
            p.setHit(recordGoodsFoot(id));
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
        if (group != null) {
            List<SmsGroupRecord> groupRecords = groupRecordMapper.selectList(new QueryWrapper<SmsGroupRecord>().eq("group_id", group.getId()));
            for (SmsGroupRecord groupRecord : groupRecords) {
                List<SmsGroupMember> groupMembers = groupMemberMapper.selectList(new QueryWrapper<SmsGroupMember>().eq("group_record_id", groupRecord.getId()).eq("status", 2));
                groupRecord.setList(groupMembers);
            }
            map.put("memberGroupList", groupRecords);
            map.put("group", group);
        }


        map.put("goods", goods);
        return new CommonResult().success(map);
    }


    @SysLog(MODULE = "pms", REMARK = "查询团购商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询礼物商品列表")
    @GetMapping(value = "/gift/list")
    public Object giftList(PmsGifts product,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        IPage<PmsGifts> list = giftsService.page(new Page<PmsGifts>(pageNum, pageSize), new QueryWrapper<>(product));
        return new CommonResult().success(list);

    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/secskill/detail")
    @ApiOperation(value = "查询商品详情信息")
    public Object secskillDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        //记录浏览量到redis,然后定时更新到数据库

        SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(id);

        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL, relation.getProductId() + "")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods)) {
                log.info("redis缓存失效：" + relation.getProductId());
                goods = pmsProductService.getGoodsRedisById(relation.getProductId());
            }
        } catch (Exception e) {
            log.info("redis缓存失效：" + relation.getProductId());
            goods = pmsProductService.getGoodsRedisById(relation.getProductId());
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            PmsProduct p = goods.getGoods();
            p.setHit(recordGoodsFoot(id));
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
        map.put("skillDetail", relation);
        map.put("goods", goods);
        return new CommonResult().success(map);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/gift/detail")
    @ApiOperation(value = "查询礼物商品详情信息")
    public Object giftDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        PmsGifts goods = giftsService.getById(id);
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            PmsFavorite query = new PmsFavorite();
            query.setObjId(goods.getId());
            query.setMemberId(umsMember.getId());
            query.setType(4);
            PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
            if (findCollection != null) {
                map.put("favorite", true);
            } else {
                map.put("favorite", false);
            }
        }
        map.put("goods", goods);
        return new CommonResult().success(map);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品类型下的商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询积分商品类型")
    @GetMapping(value = "/typeGiftList")
    public Object typeGiftList(PmsGiftsCategory productCategory) {
        List<PmsGiftsCategory> categories = giftsCategoryService.list(new QueryWrapper<>(productCategory));
        return new CommonResult().success(categories);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品分类列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品分类列表")
    @GetMapping(value = "/categoryAndGoodsList/list")
    public Object categoryAndGoodsList(PmsProductAttributeCategory productCategory) {
        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.list(new QueryWrapper<>());
        for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
            PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(gt.getId());
            productQueryParam.setPublishStatus(1);
            productQueryParam.setVerifyStatus(1);
            gt.setGoodsList(pmsProductService.list(new QueryWrapper<>(productQueryParam).select(ConstansValue.sampleGoodsList)));
        }
        return new CommonResult().success(productAttributeCategoryList);
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页推荐品牌")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐品牌")
    @GetMapping(value = "/recommendBrand/list")
    public Object getRecommendBrandList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(pmsProductService.getRecommendBrandList(1, 1));
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页新品")
    @IgnoreAuth
    @ApiOperation(value = "查询首页新品")
    @GetMapping(value = "/newProductList/list")
    public Object getNewProductList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(pmsProductService.getHotProductList(1, 1));
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页热销商品")
    @IgnoreAuth
    @ApiOperation(value = "查询首页热销商品")
    @GetMapping(value = "/hotProductList/list")
    public Object getHotProductList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(pmsProductService.getHotProductList(1, 1));
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品优惠")
    @GetMapping(value = "/getPromotionProductList")
    public List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids) {
        return productMapper.getPromotionProductList(ids);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品类型下的商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品类型下的商品列表")
    @GetMapping(value = "/typeGoodsList")
    public Object typeGoodsList(PmsProductCategory productCategory) throws Exception {
        List<ProductTypeVo> relList = new ArrayList<>();
        String json = redisService.get(Rediskey.specialcategoryAndGoodsList);
        if (ValidatorUtils.notEmpty(json)) {
            relList = JsonUtils.json2list(json, ProductTypeVo.class);
            return new CommonResult().success(relList);
        }
        PmsProduct productQueryParam = new PmsProduct();

        productQueryParam.setPublishStatus(1);
        productQueryParam.setVerifyStatus(1);
        List<PmsProduct> list = pmsProductService.page(new Page<PmsProduct>(1, 100), new QueryWrapper<>(productQueryParam)).getRecords();

        for (PmsProduct l : list) {
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
        for (PmsProductCategory v : categories) {
            if (v.getParentId() == 0) {
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                relList.add(vo);
            } else {
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                vo.setPid(v.getParentId());
                relList.add(vo);
            }
        }
        redisService.set(Rediskey.specialcategoryAndGoodsList , JsonUtils.objectToJson(relList));
        redisService.expire(Rediskey.specialcategoryAndGoodsList , 2);
        return new CommonResult().success(relList);
    }


    @SysLog(MODULE = "pms", REMARK = "查询商品分类")
    @IgnoreAuth
    @ApiOperation(value = "查询商品分类")
    @GetMapping(value = "/getGoodsTypes")
    public Object getGoodsTypes() throws Exception {
        List<PmsProductCategory> relList = new ArrayList<>();
        String json = redisService.get(Rediskey.goodsCategorys);
        if (ValidatorUtils.notEmpty(json)) {
            relList = JsonUtils.json2list(json, PmsProductCategory.class);
            return new CommonResult().success(relList);
        }
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        pmsProductCategory.setShowStatus(1);
        pmsProductCategory.setNavStatus(1);
        List<PmsProductCategory> categories = categoryMapper.selectList(new QueryWrapper<>(pmsProductCategory));
        for (PmsProductCategory v : categories) {
            if (ValidatorUtils.empty(v.getParentId()) || v.getParentId() == 0) {
                relList.add(v);
            }
        }
        List<PmsProductCategory> list = null;
        //组装二级分类
        for (int i = 0; i < relList.size(); i++) {
            list = new ArrayList<>();
            for (PmsProductCategory v : categories) {
                if ( ValidatorUtils.notEmpty(v.getParentId()) &&  v.getParentId().longValue() == relList.get(i).getId().longValue()) {
                    list.add(v);
                }
            }
            relList.get(i).setChildList(list);
        }
        redisService.set(Rediskey.goodsCategorys , JsonUtils.objectToJson(relList));
        redisService.expire(Rediskey.goodsCategorys , 2);
        return new CommonResult().success(relList);
    }


    @SysLog(MODULE = "pms", REMARK = "查询商品类型下的商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询商品类型下的商品列表")
    @GetMapping(value = "/typeList")
    public Object typeList(PmsProductCategory productCategory) {
        List<ProductTypeVo> relList = new ArrayList<>();
        List<PmsProductCategory> categories = categoryMapper.selectList(new QueryWrapper<>());
        for (PmsProductCategory v : categories) {
            if ( ValidatorUtils.empty(v.getParentId()) || v.getParentId() == 0) {
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                relList.add(vo);
            } else {
                ProductTypeVo vo = new ProductTypeVo();
                vo.setName(v.getName());
                vo.setId(v.getId());
                vo.setPid(v.getParentId());
                relList.add(vo);
            }
        }

        return new CommonResult().success(relList);
    }

    @IgnoreAuth
    @ApiOperation("添加商品浏览记录")
    @SysLog(MODULE = "pms", REMARK = "添加商品浏览记录")
    @PostMapping(value = "/addView")
    public Object addView(@RequestParam Long goodsId) {

        String key = String.format(Rediskey.GOODSHISTORY, memberService.getNewCurrentMember().getUsername());

        //为了保证浏览商品的 唯一性,每次添加前,将list 中该 商品ID去掉,在加入,以保证其浏览的最新的商品在最前面

        redisUtil.lRemove(key, 1, goodsId.toString());
        //将value push 到该key下的list中
        redisUtil.lLeftPush(key, goodsId.toString());
        //使用ltrim将60个数据之后的数据剪切掉
        redisUtil.lTrim(key, 0, 59);
        //设置缓存时间为一个月
        redisUtil.expire(key, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        return new CommonResult().success();
    }

    @SysLog(MODULE = "pms", REMARK = "查询用户浏览记录列表")
    @IgnoreAuth
    @ApiOperation(value = "查询用户浏览记录列表")
    @GetMapping(value = "/viewList")
    public Object viewList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        //拼装返回
        Map<String, Object> map = new HashMap<>();
        if (memberService.getNewCurrentMember() == null) {
            return new CommonResult().success(map);
        }
        String key = String.format(Rediskey.GOODSHISTORY, memberService.getNewCurrentMember().getId());

        //获取用户的浏览的商品的总页数;
        long pageCount = redisUtil.lLen(key);

        //根据用户的ID分頁获取该用户最近浏览的50个商品信息
        List<String> result = redisUtil.lRange(key, (pageNum - 1) * pageSize, pageNum * pageSize - 1);
        if (result != null && result.size() > 0) {
            List<PmsProduct> list = (List<PmsProduct>) pmsProductService.listByIds(result);

            map.put("result", list);
            map.put("pageCount", (pageCount % pageSize == 0 ? pageCount / pageSize : pageCount / pageSize + 1));
        }

        return new CommonResult().success(map);
    }

    @ApiOperation("生成商品海报")
    @GetMapping(value = "/getposter")
    public Object getposter(@RequestParam Long id) {
        PmsProduct product = pmsProductService.getById(id);
        return new CommonResult().success(product.getPic());
    }

    @ApiOperation("生成商品海报")
    @GetMapping(value = "/goodsCount")
    public Object goodsCount() {
        PmsProduct productQueryParam = new PmsProduct();

        productQueryParam.setPublishStatus(1);
        productQueryParam.setVerifyStatus(1);
        return new CommonResult().success(pmsProductService.count(new QueryWrapper<>(productQueryParam)));
    }

    private Integer recordGoodsFoot(Long id) {
        //记录浏览量到redis,然后定时更新到数据库
        String key = Rediskey.GOODS_VIEWCOUNT_CODE + id;
        //找到redis中该篇文章的点赞数，如果不存在则向redis中添加一条
        Map<Object, Object> viewCountItem = redisUtil.hGetAll(Rediskey.GOODS_VIEWCOUNT_KEY);
        Integer viewCount = 0;
        if (!viewCountItem.isEmpty()) {
            if (viewCountItem.containsKey(key)) {
                viewCount = Integer.parseInt(viewCountItem.get(key).toString()) + 1;
                redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY, key, viewCount + "");
            } else {
                redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY, key, 1 + "");
            }
        } else {
            viewCount = 1;
            redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY, key, 1 + "");
        }
        return viewCount;
    }

    /**
     * 判断是否收藏商品
     *
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
