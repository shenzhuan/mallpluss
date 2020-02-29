package com.zscat.mallplus.single;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.oms.entity.OmsCartItem;
import com.zscat.mallplus.oms.service.IOmsCartItemService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.CartProduct;
import com.zscat.mallplus.pms.service.IPmsSkuStockService;
import com.zscat.mallplus.sms.service.ISmsBasicMarkingService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.CartParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 购物车管理Controller
 * https://github.com/shenzhuan/mallplus on 2018/8/2.
 */
@RestController
@Api(tags = "OmsCartItemController", description = "购物车管理")
@RequestMapping("/api/cart")
public class OmsCartItemController {
    @Autowired
    private IOmsCartItemService cartItemService;
    @Autowired
    private IUmsMemberService memberService;

    @Autowired
    private IPmsSkuStockService pmsSkuStockService;

    @Resource
    private IOmsOrderService orderService;
    @Resource
    private ISmsBasicMarkingService smsBasicMarkingService;

    @ApiOperation("添加商品到购物车")
    @RequestMapping(value = "/addCart")
    @ResponseBody
    public Object addCart(CartParam cartParam) {
        try {
            return orderService.addCart(cartParam);
        } catch (ApiMallPlusException e) {
            return new CommonResult().failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    @ApiOperation("获取某个会员的购物车列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list() {
        UmsMember umsMember = memberService.getNewCurrentMember();
        Map<String, Object> map = new HashMap<>();
        if (umsMember != null && umsMember.getId() != null) {
            List<OmsCartItem> cartItemList = cartItemService.list(umsMember.getId(), null);
            map.put("cartItemList", cartItemList);
            /*CartMarkingVo vo = new CartMarkingVo();
            vo.setCartList(cartItemList);
            SmsBasicMarking marking = smsBasicMarkingService.matchOrderBasicMarking(vo);
            if (marking != null) {
                map.put("promoteAmount", marking.getMinAmount());
            } else {
                map.put("promoteAmount", 0);
            }*/
            map.put("promoteAmount", 0);
            Integer goodsCount = 0;
            BigDecimal goodsAmount = new BigDecimal(0.00);
            Integer checkedGoodsCount = 0;
            BigDecimal checkedGoodsAmount = new BigDecimal(0.00);
            for (OmsCartItem cart : cartItemList) {
                goodsCount += cart.getQuantity();
                goodsAmount = goodsAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getQuantity())));
                if (cart.getChecked() == 1) {
                    checkedGoodsCount += cart.getQuantity();
                    checkedGoodsAmount = checkedGoodsAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getQuantity())));
                }
            }
            Map<String, Object> cartTotal = new HashMap<>();
            cartTotal.put("goodsCount", goodsCount);
            cartTotal.put("goodsAmount", goodsAmount);
            cartTotal.put("checkedGoodsCount", checkedGoodsCount);
            cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);

            map.put("cartTotal", cartTotal);

            return new CommonResult().success(map);
        }
        return new CommonResult().success(map);
    }


    @ApiOperation("修改购物车中某个商品的数量")
    @RequestMapping(value = "/update/quantity", method = RequestMethod.GET)
    @ResponseBody
    public Object updateQuantity(@RequestParam Long id,
                                 @RequestParam Integer quantity) {
        int count = cartItemService.updateQuantity(id, memberService.getNewCurrentMember().getId(), quantity);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("获取购物车中某个商品的规格,用于重选规格")
    @RequestMapping(value = "/getProduct/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getCartProduct(@PathVariable Long productId) {
        CartProduct cartProduct = cartItemService.getCartProduct(productId);
        return new CommonResult().success(cartProduct);
    }

    @ApiOperation("修改购物车中商品的规格")
    @RequestMapping(value = "/update/attr", method = RequestMethod.POST)
    @ResponseBody
    public Object updateAttr(@RequestBody OmsCartItem cartItem) {
        int count = cartItemService.updateAttr(cartItem);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除购物车中的某个商品")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String cart_id_list) {
        if (StringUtils.isEmpty(cart_id_list)) {
            return new CommonResult().failed("参数为空");
        }
        List<Long> resultList = new ArrayList<>(cart_id_list.split(",").length);
        for (String s : cart_id_list.split(",")) {
            resultList.add(Long.valueOf(s));
        }
        int count = cartItemService.delete(memberService.getNewCurrentMember().getId(), resultList);
        if (count > 0) {
            return new CommonResult().success(list());
        }
        return new CommonResult().failed();
    }

    @ApiOperation("清空购物车")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public Object clear() {
        int count = cartItemService.clear(memberService.getNewCurrentMember().getId());
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    /**
     * 购物车商品货品勾选状态
     * <p>
     * 如果原来没有勾选，则设置勾选状态；如果商品已经勾选，则设置非勾选状态。
     *
     * @return 购物车信息
     */
    @PostMapping("checked")
    public Object checked(@RequestParam Integer checkValue,
                          @RequestParam List<Integer> productIds) {
        if (productIds == null) {
            return new CommonResult().paramFailed();
        }
        if (checkValue == null) {
            return new CommonResult().paramFailed();
        }
        OmsCartItem item = new OmsCartItem();
        item.setChecked(checkValue);
        item.setModifyDate(new Date());
        cartItemService.update(item, new QueryWrapper<OmsCartItem>().eq("member_id", memberService.getNewCurrentMember().getId()).in("product_id", productIds));
        return list();
    }

    @PostMapping("singleChecked")
    public Object singleChecked(@RequestParam Integer checkValue,
                                @RequestParam Integer productId) {
        if (productId == null) {
            return new CommonResult().paramFailed();
        }
        if (checkValue == null) {
            return new CommonResult().paramFailed();
        }
        OmsCartItem item = new OmsCartItem();
        item.setChecked(checkValue);
        item.setModifyDate(new Date());
        cartItemService.update(item, new QueryWrapper<OmsCartItem>().eq("member_id", memberService.getNewCurrentMember().getId()).eq("product_id", productId));
        return list();
    }

    @ApiOperation("修改购物车中某个商品的数量")
    @RequestMapping(value = "/getnumber", method = RequestMethod.GET)
    @ResponseBody
    public Object getnumber() {
        Integer count = cartItemService.countCart(memberService.getNewCurrentMember().getId());
        if (ValidatorUtils.notEmpty(count) && count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().success(0);
    }
}
