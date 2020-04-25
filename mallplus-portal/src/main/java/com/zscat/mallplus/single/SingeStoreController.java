package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.StatusEnum;
import com.zscat.mallplus.oms.vo.StoreContentResult;
import com.zscat.mallplus.pms.entity.PmsFavorite;
import com.zscat.mallplus.pms.entity.PmsGifts;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsFavoriteService;
import com.zscat.mallplus.sys.entity.*;
import com.zscat.mallplus.sys.mapper.SysShopMapper;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.*;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@RestController
@Api(tags = "SingeStoreController", description = "会员关系管理")
@RequestMapping("/api/single/store")
public class SingeStoreController extends ApiBaseAction {
    @Resource
    IUmsMemberService memberService;
    @Resource
    private IStoreService storeService;
    @Autowired
    private RedisService redisService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IPositionService positionService;
    @Resource
    private SysStoreMapper storeMapper;

    @Resource
    private SysShopMapper shopMapper;

    @Resource
    private ISysStoreClassService classService;
    @Resource
    private ISysStoreCommentService commentService;

    @Autowired
    private IPmsFavoriteService favoriteService;
    @SysLog(MODULE = "sys", REMARK = "保存")
    @ApiOperation("保存")
    @PostMapping(value = "/applyStore")
    public Object applyStore(SysStore entity) {
        try {
            return storeService.applyStore(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed(e.getMessage());
        }
    }


    @IgnoreAuth
    @ApiOperation(value = "查询商铺列表")
    @GetMapping(value = "/store/list")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object storeList(SysStore entity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        entity.setStatus(StatusEnum.AuditType.SUCESS.code());
        String orderColum = "create_time";
        if (ValidatorUtils.notEmpty(entity.getSort())) {
            if (entity.getSort() == 1) {
                orderColum = "hit";
            } else if (entity.getSort() == 2) {
                orderColum = "collect";
            }
        }
        return new CommonResult().success(storeService.page(new Page<SysStore>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc(orderColum)));
    }

    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        UmsMember member = memberService.getNewCurrentMember();
        if (ValidatorUtils.empty(id)) {
            if (member == null) {
                return new CommonResult().fail(100);
            }
            id = memberService.getById(member.getId()).getStoreId();
        }
        SysStore store = storeService.getById(id);
        //记录浏览量到redis,然后定时更新到数据库
        String key = Rediskey.STORE_VIEWCOUNT_CODE + id;
        //找到redis中该篇文章的点赞数，如果不存在则向redis中添加一条
        Map<Object, Object> viewCountItem = redisUtil.hGetAll(Rediskey.STORE_VIEWCOUNT_KEY);
        Integer viewCount = 0;
        if (!viewCountItem.isEmpty()) {
            if (viewCountItem.containsKey(key)) {
                viewCount = Integer.parseInt(viewCountItem.get(key).toString()) + 1;
                redisUtil.hPut(Rediskey.STORE_VIEWCOUNT_KEY, key, viewCount + "");
            } else {
                viewCount = 1;
                redisUtil.hPut(Rediskey.STORE_VIEWCOUNT_KEY, key, 1 + "");
            }
        } else {
            redisUtil.hPut(Rediskey.STORE_VIEWCOUNT_KEY, key, 1 + "");
        }

        PmsFavorite query = new PmsFavorite();
        query.setObjId(Long.valueOf(store.getId()));
        query.setMemberId(member.getId());
        query.setType(3);
        PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
        if (findCollection != null) {
           store.setFavorite(true);
        } else {
            store.setFavorite(false);
        }

        return new CommonResult().success(store);
    }

    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/detail1", method = RequestMethod.GET)
    @ResponseBody
    public Object detail1(@RequestParam(value = "id", required = false) Integer id) {
        Map map = new HashMap();
        UmsMember member = memberService.getNewCurrentMember();
        if (ValidatorUtils.empty(id)) {
            if (member == null) {
                return new CommonResult().fail(100);
            }
            UmsMember newMember = memberService.getById(member.getId());
            id = newMember.getStoreId();
            map.put("member", newMember);
        }
        SysStore store = storeService.getById(id);
        PmsFavorite query = new PmsFavorite();
        query.setObjId(Long.valueOf(store.getId()));
        query.setMemberId(member.getId());
        query.setType(3);
        PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
        if (findCollection != null) {
            store.setFavorite(true);
        } else {
            store.setFavorite(false);
        }
        map.put("store", store);
        return new CommonResult().success(map);
    }

    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @ResponseBody
    public Object home(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        StoreContentResult contentResult = null;
        String key = "";
        try {
            if (ValidatorUtils.empty(id)) {
                UmsMember member = memberService.getNewCurrentMember();
                if (member == null) {
                    return new CommonResult().fail(100);
                }
                UmsMember newMember = memberService.getById(member.getId());
                id = newMember.getStoreId();
            }
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("商户申请中");
            }
            key = String.format(Rediskey.STOREHOMEPAGEMOBILE, id);
            String json = redisService.get(key);
            if (ValidatorUtils.empty(json)) {
                contentResult = storeService.singeleContent(id);
                redisService.set(key, JsonUtils.objectToJson(contentResult));
                redisService.expire(key, 60);
            } else {
                contentResult = JsonUtils.jsonToPojo(redisService.get(key), StoreContentResult.class);
            }
        } catch (Exception e) {
            contentResult = storeService.singeleContent(id);
            redisService.set(key, JsonUtils.objectToJson(contentResult));
            redisService.expire(key, 60);
        }
        return new CommonResult().success(contentResult);
    }
    /**
     * 根据经纬度坐标获取位置信息
     * @param geoHash
     * @return
     */
    @RequestMapping(value = "/v1/position/pois",method = RequestMethod.GET)
    public Object getPoiByGeoHash(@RequestParam("geohash")String geoHash){
        System.out.println("geohash:"+geoHash);
        return  new CommonResult().success(positionService.pois(geoHash));
    }

    @RequestMapping(value = "/v1/pois",method = RequestMethod.GET)
    public Object getPoiByCityAndKeyword(@RequestParam(value = "type",defaultValue = "search")String type,
                                         @RequestParam(value = "city_id",required = false)Long cityId,
                                         @RequestParam(value = "keyword")String keyword){
        String cityName = null;
        if(cityId==null){
            SysArea city = positionService.guessCity(getClientIp());
            cityName = city.getName();
        }else {
            SysArea map = positionService.findById(cityId);
            cityName = map.getName();
        }
        return new CommonResult().success(positionService.searchPlace(cityName, keyword));

    }

    /**
     * 根据经纬度获取商铺列表
     * @param geoHash 40.0844020000,116.3483150000
     * @param distance
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/dis/storeList", method = RequestMethod.GET)
    public Object restaurants(@RequestParam("geohash") String geoHash
            , @RequestParam(value = "distance",defaultValue = "10") Integer distance,
                              @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        String[] geoHashArr = geoHash.split(",");
        String longitude = geoHashArr[1];
        String latitude = geoHashArr[0];
        List<SysStore> storeList =  storeMapper.selectDisStore(distance,Double.valueOf(latitude),Double.valueOf(longitude),pageSize);
       /* GeoResults<Map> geoResults = mongoRepository.near(Double.valueOf(longitude), Double.valueOf(latitude), "shops", params);
        List<GeoResult<Map>> geoResultList = geoResults.getContent();
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < geoResultList.size(); i++) {
            Map map = geoResultList.get(i).getContent();
            Distance distance = new Distance(Double.valueOf(longitude), Double.valueOf(latitude),
                    Double.valueOf(map.get("longitude").toString()), Double.valueOf(map.get("latitude").toString()));
            map.put("distance", distance.getDistance());
            list.add(map);
        }*/
        return new CommonResult().success(storeList);
    }

    /**
     * 根据经纬度获取商铺列表
     * @param geoHash 40.0844020000,116.3483150000
     * @param distance
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/dis/shopList", method = RequestMethod.GET)
    public Object shopList(@RequestParam("geohash") String geoHash
            , @RequestParam(value = "distance",defaultValue = "10") Integer distance,
                           @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        String[] geoHashArr = geoHash.split(",");
        String longitude = geoHashArr[1];
        String latitude = geoHashArr[0];
        List<SysShop> storeList =  shopMapper.selectDisShop(distance,Double.valueOf(latitude),Double.valueOf(longitude),pageSize);
       /* GeoResults<Map> geoResults = mongoRepository.near(Double.valueOf(longitude), Double.valueOf(latitude), "shops", params);
        List<GeoResult<Map>> geoResultList = geoResults.getContent();
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < geoResultList.size(); i++) {
            Map map = geoResultList.get(i).getContent();
            Distance distance = new Distance(Double.valueOf(longitude), Double.valueOf(latitude),
                    Double.valueOf(map.get("longitude").toString()), Double.valueOf(map.get("latitude").toString()));
            map.put("distance", distance.getDistance());
            list.add(map);
        }*/
        return new CommonResult().success(storeList);
    }
    @IgnoreAuth
    @ApiOperation(value = "查询门店管理列表")
    @GetMapping(value = "/shoplist")
    @SysLog(MODULE = "ums", REMARK = "查询门店管理列表")
    public Object shoplist(@RequestParam(value = "storeId", required = true) Long storeId) {
        return new CommonResult().success(shopMapper.selectList(new QueryWrapper<SysShop>().eq("store_id", storeId)));
    }

    @ApiOperation("门店详情")
    @GetMapping(value = "/shopDetail")
    public Object shopDetail(Long id) {
        return new CommonResult().success(shopMapper.selectById(id));
    }

    @SysLog(MODULE = "pms", REMARK = "查询商户内部分类")
    @IgnoreAuth
    @ApiOperation(value = "查询商户内部分类")
    @GetMapping(value = "/storeClass/list")
    public Object SysStoreClassList(SysStoreClass product,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        IPage<SysStoreClass> list = classService.page(new Page<SysStoreClass>(pageNum, pageSize), new QueryWrapper<>(product));
        return new CommonResult().success(list);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商户内部评论")
    @IgnoreAuth
    @ApiOperation(value = "查询商户评论")
    @GetMapping(value = "/storeComment/list")
    public Object SysStoreClassList(SysStoreComment product,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        IPage<SysStoreComment> list = commentService.page(new Page<SysStoreComment>(pageNum, pageSize), new QueryWrapper<>(product));
        return new CommonResult().success(list);
    }

    @SysLog(MODULE = "sys", REMARK = "保存")
    @ApiOperation("保存")
    @PostMapping(value = "/addStoreComment")
    public Object addStoreComment(SysStoreComment entity) {
        try {
            UmsMember member = memberService.getNewCurrentMember();
            if (member==null){
                return new CommonResult().fail(100);
            }
            entity.setCreateTime(new Date());
            entity.setMemberId(member.getId());
            entity.setName(member.getUsername());
            return new CommonResult().success(commentService.save(entity));
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }
    }
}
