package com.zscat.mallplus.ums.service.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beust.jcommander.internal.Maps;
import com.zscat.mallplus.config.AppConfiguration;
import com.zscat.mallplus.sys.entity.SysArea;
import com.zscat.mallplus.sys.mapper.SysAreaMapper;
import com.zscat.mallplus.ums.entity.OmsShip;
import com.zscat.mallplus.ums.mapper.OmsShipMapper;
import com.zscat.mallplus.ums.service.IOmsShipService;
import com.zscat.mallplus.ums.service.IPositionService;
import com.zscat.mallplus.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.nutz.json.Json;
import org.nutz.mapl.Mapl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配送方式表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-09-16
 */
@Slf4j
@Service
public class PositionServiceImpl  implements IPositionService {

    @Autowired
    private AppConfiguration appConfiguration;
    @Autowired
    private SysAreaMapper areaMapper;
    /**
     * 根据经纬度坐标获取位置信息
     *
     * @param geohash
     * @return
     */
    public Map pois(String geohash) {
        log.info("获取地址信息:{}",geohash);
        Map<String, Object> map = Maps.newHashMap();
        map.put("location", geohash);
        map.put("key", appConfiguration.getTencentKey());
        Map result = Maps.newHashMap();
        try {
            String str = HttpUtil.get(appConfiguration.getQqApiUrl() + "geocoder/v1", map);
            Map response = (Map) JsonUtils.readJsonToMap(str);
            log.error("获取地理位置异常", response);
            System.out.println(response);
            if ("0".equals(response.get("status").toString())) {
                result.put("address", Mapl.cell(response,"result.address"));
                result.put("city", Mapl.cell(response, "result.address_component.city"));
                result.put("name", Mapl.cell(response, "result.formatted_addresses.recommend"));
                result.put("latitude", Mapl.cell(response, "result.location.lat"));
                result.put("longitude", Mapl.cell(response, "result.location.lng"));
            }else{
                log.error("获取地理位置信息失败:{}",str);
            }

        } catch (Exception e) {
            log.error("获取地理位置异常", e);
        }
        return result;
    }

    public List searchPlace(String cityName, String keyword) {
        log.info("获取地址信息:{}，{}",cityName,keyword);
        Map<String, Object> params = Maps.newHashMap();
        params.put("key", appConfiguration.getTencentKey());
        params.put("keyword", URLEncoder.encode(keyword));
        params.put("boundary", "region(" + URLEncoder.encode(cityName) + ",0)");
        params.put("page_size", "10");
        try {
            String str = HttpUtil.get(appConfiguration.getQqApiUrl() + "place/v1/search", params);
            Map result = (Map) Json.fromJson(str);
            if (Integer.valueOf(result.get("status").toString()).intValue() == 0) {
                return (List) result.get("data");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
    public SysArea getPostion(String ip) {
        log.info("根据ip:{}获取城市信息",ip);
        Map<String, Object> map = Maps.newHashMap();
        map.put("ip", ip);
        map.put("key", appConfiguration.getTencentKey());
        Map result = null;
        try {
            String str = HttpUtil.get(appConfiguration.getQqApiUrl() + "location/v1/ip", map);
            result = (Map) Json.fromJson(str);
        } catch (Exception e) {
            log.error("获取地理位置异常", e);
        }
        if (result == null || Integer.valueOf(result.get("status").toString()) != 0) {
            try {
                map.put("key", appConfiguration.getTencentKey2());
                String str = HttpUtil.get(appConfiguration.getQqApiUrl() + "location/v1/ip", map);
                result = (Map) Json.fromJson(str);
            } catch (Exception e) {
                log.error("获取地理位置异常", e);
            }
        }
        if (result == null || Integer.valueOf(result.get("status").toString()) != 0) {
            try {
                map.put("key", appConfiguration.getTencentKey3());
                String str = HttpUtil.get(appConfiguration.getQqApiUrl() + "location/v1/ip", map);
                result = (Map) Json.fromJson(str);
            } catch (Exception e) {
                log.error("获取地理位置异常", e);
            }

        }
        if (Integer.valueOf(result.get("status").toString()) == 0) {
            Map resultData = (Map) result.get("result");

            String lat = String.valueOf(Mapl.cell(resultData, "location.lat"));
            String lng = String.valueOf(Mapl.cell(resultData, "location.lng"));
            String city = (String) Mapl.cell(resultData, "ad_info.city");
            city = city.replace("市", "");
            SysArea cityInfo = new SysArea();
            cityInfo.setName(city);
            cityInfo.setLat(lat);
            cityInfo.setLng(lng);
            return cityInfo;

        }
        return null;
    }

    public SysArea guessCity(String ip){
        SysArea cityInfo = getPostion(ip);
        if(cityInfo!=null) {
            SysArea city = areaMapper.selectOne(new QueryWrapper<SysArea>().eq("name",cityInfo.getName()).eq("deep",1));
            return city;
        }
        return null;
    }

    @Override
    public SysArea findById(Long cityId) {
        return areaMapper.selectById(cityId);
    }

    public Map<String,String> getDistance(String from,String to){
        Map<String,Object> params = new HashMap<>();
        params.put("ak",appConfiguration.getBaiduKey());
        params.put("output","json");
        params.put("origins",from);
        params.put("destinations",to);

        try {
            //使用百度地图api获取距离值：
            //routematrix/v2/riding 骑行
            //routematrix/v2/driving 开车
            //routematrix/v2/walking 步行
            String str = HttpUtil.get(appConfiguration.getBaiduApiUrl() + "routematrix/v2/riding", params);
            Map response = (Map) Json.fromJson(str);
            if("0".equals(response.get("status").toString())){
                Map result =  Maps.newHashMap(
                        "distance",Mapl.cell(response,"result[0].distance.text"),
                        "duration",Mapl.cell(response,"result[0].duration.text")
                );
                return result;
            }

        }catch (Exception e){
            log.error("通过百度获取配送距离失败",e);
        }
        return null;
    }
}
