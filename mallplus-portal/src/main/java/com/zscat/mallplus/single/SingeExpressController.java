package com.zscat.mallplus.single;


import cn.hutool.http.HttpUtil;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.build.BuildHomeResult;
import com.zscat.mallplus.build.service.IBuildingCommunityService;
import com.zscat.mallplus.cms.service.ICmsSubjectCategoryService;
import com.zscat.mallplus.cms.service.ICmsSubjectCommentService;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.pms.mapper.PmsProductCategoryMapper;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.pms.service.*;
import com.zscat.mallplus.pms.vo.GoodsDetailResult;
import com.zscat.mallplus.sms.mapper.SmsGroupMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupMemberMapper;
import com.zscat.mallplus.sms.service.ISmsFlashPromotionProductRelationService;
import com.zscat.mallplus.sms.service.ISmsGroupService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberLevelService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@Slf4j
@RestController
@Api(tags = "SingeBuildController", description = "物业管理")
@RequestMapping("/api/single/express")
public class SingeExpressController extends ApiBaseAction {

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

    @Resource
    private IBuildingCommunityService communityService;

    public static void main(String[] args) {
        String key = "LXFBZ-BEZC4-4W6UE-XOBM4-WHA5E-C6FHZ";
        String url = "https://apis.map.qq.com/ws/place/v1/search?key=" + key;
        String string = "https://apis.map.qq.com/uri/v1/search?keyword=酒店&region=北京&referer=" + key;
        StringBuffer sb = new StringBuffer("https://apis.map.qq.com/ws/geocoder/v1/?");
        sb.append("address=" + "北京市");
        sb.append("&key=" + key);

        System.out.println(HttpUtil.get(sb.toString()));
    }

    @SysLog(MODULE = "pms", REMARK = "所有社区和房间")
    @IgnoreAuth
    @GetMapping(value = "/allCommunity")
    @ApiOperation(value = "所有社区和房间")
    public Object allCommunity() {
        return new CommonResult().success(communityService.allCommunity());
    }

    @SysLog(MODULE = "pms", REMARK = "绑定小区和房间")
    @IgnoreAuth
    @PostMapping(value = "/bindCommunity")
    @ApiOperation(value = "绑定小区和房间")
    public Object bindCommunity(@RequestParam(value = "selectedMore2", required = false, defaultValue = "0") String selectedMore2) {

        try {
            return communityService.bindCommunity(selectedMore2);
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }
    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public Object home_mobile(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        String key = Rediskey.HOMEPAGEMOBILE;
        String json = redisService.get(key);
        BuildHomeResult contentResult = null;
        try {
            if (ValidatorUtils.empty(json)) {
                contentResult = communityService.singleContent(id);
                redisService.set(key, JsonUtils.objectToJson(contentResult));
                redisService.expire(key, 30);
            } else {
                contentResult = JsonUtils.jsonToPojo(redisService.get(key), BuildHomeResult.class);
            }
        } catch (Exception e) {
            contentResult = communityService.singleContent(id);
            redisService.set(key, JsonUtils.objectToJson(contentResult));
            redisService.expire(key, 30);
        }
        return new CommonResult().success(contentResult);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @GetMapping(value = "/goods/detail")
    @ApiOperation(value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL, id + "")), GoodsDetailResult.class);
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


        map.put("goods", goods);
        return new CommonResult().success(map);
    }


}
