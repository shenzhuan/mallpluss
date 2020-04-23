package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysArea;
import com.zscat.mallplus.sys.entity.SysShop;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.sys.mapper.SysShopMapper;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.sys.vo.Distance;
import com.zscat.mallplus.ums.service.IPositionService;
import com.zscat.mallplus.util.WeixinCheckoutUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.nutz.json.Json;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WechatController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/5
 **/
@Slf4j
@RestController
@RequestMapping("/api")
public class WechatController extends ApiBaseAction{

    @Resource
    private IPositionService positionService;
    @Resource
    private SysStoreMapper storeMapper;

    @Resource
    private SysShopMapper shopMapper;
    /**
     * 微信公众号签名认证接口
     *
     * @throws
     * @Title: test
     * @Description: TODO
     * @param: @param signature
     * @param: @param timestamp
     * @param: @param nonce
     * @param: @param echostr
     * @param: @return
     * @return: String
     */
    @RequestMapping("/wx")
    public String test(String signature, String timestamp, String nonce, String echostr) {

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (signature != null && WeixinCheckoutUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }

        return null;
    }


}
