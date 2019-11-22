package com.mei.zhuang.service.marking;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopCodeGift;
import com.mei.zhuang.entity.marking.EsShopCodeGiftRule;
import com.mei.zhuang.vo.marking.CodeResult;
import com.mei.zhuang.vo.order.CartMarkingVo;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface CodeGiftService extends IService<EsShopCodeGift> {
    boolean save(EsShopCodeGift entity) ;

    int updateShowStatus(Long ids, Integer status);

    boolean update(EsShopCodeGift entity) throws Exception;
    //验证码删除
    Integer deleteCode(long id);

    //验证码明细查询
    EsShopCodeGift CodeList(long id);
    //唯一验证
    EsShopCodeGiftRule codegif(long codeGiftId);
    EsShopCodeGiftRule codegif2(String code);



    public CodeResult getCodeGoods(CartMarkingVo vo);
    public  void updateCodeStatus(String code, Integer status);
}
