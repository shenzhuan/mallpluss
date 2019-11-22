package com.mei.zhuang.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.marking.EsShopPackageGoodsMapper;
import com.mei.zhuang.dao.marking.EsShopPackageGoodsSpecMapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.marking.EsShopPackageGoods;
import com.mei.zhuang.entity.marking.EsShopPackageGoodsSpec;
import com.mei.zhuang.service.goods.EsShopGoodsService;
import com.mei.zhuang.service.marking.PackageGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PackageGoodsServiceImpl extends ServiceImpl<EsShopPackageGoodsMapper, EsShopPackageGoods> implements PackageGoodsService {

    @Resource
    private EsShopPackageGoodsMapper PackageGoodsMapper;
    @Resource
    private EsShopPackageGoodsSpecMapper packageGoodsSpecMapper;

    @Resource
    private EsShopGoodsService goodsServiceFegin;

    public void datetime(EsShopPackageGoods en) {
        try {
            if (en.getTime() != null) {
                String[] times = en.getTime().split(",");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                en.setStartingTime(sdf.parse(times[0]));
                en.setEndTime(sdf.parse(times[1]));
                System.out.println(en.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(EsShopPackageGoods entity) {
        EsShopGoods goods = new EsShopGoods();
        goods.setTitle(entity.getPackageName());
        goods.setType(5);
        goods.setPrice(entity.getPrice());
        goods.setThumb(entity.getPicture());
        //轮播
        goods.setThumbs(entity.getByPicture());
        //goods.setCategoryId(coreConfig.getValue());
        goods.setCreateTime(new Date());
        goods.setStatus(-2);
        goods.setIsPutaway(1);
        goodsServiceFegin.save(goods);
        entity.setGoodId(goods.getId());
        try {
            datetime(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PackageGoodsMapper.insert(entity);
        addspec(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(EsShopPackageGoods entity) {
        EsShopPackageGoods PackageGoods = PackageGoodsMapper.selectById(entity.getId());
        datetime(entity);
        entity.setGoodId(PackageGoods.getGoodId());
        PackageGoodsMapper.updateById(entity);
        EsShopGoods goods = new EsShopGoods();
        goods.setTitle(entity.getPackageName());
        goods.setId(entity.getGoodId());
        goodsServiceFegin.updateById(goods);

        packageGoodsSpecMapper.delete(new QueryWrapper<EsShopPackageGoodsSpec>().eq("package_id", entity.getId()));
        addspec(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deletePackage(String id) {
        String sid[] = id.split(",");
        for (String pid : sid) {
            EsShopPackageGoods PackageGoods = PackageGoodsMapper.selectById(Long.parseLong(pid));
            goodsServiceFegin.removeById(PackageGoods.getGoodId());
            PackageGoods.setAccording(2);
            PackageGoodsMapper.updateById(PackageGoods);
            packageGoodsSpecMapper.delete(new QueryWrapper<EsShopPackageGoodsSpec>().eq("package_id", Integer.parseInt(pid)));
        }
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updatestatus(Integer status, String pid) {
        String sid[] = pid.split(",");
        for (String id : sid) {
            PackageGoodsMapper.updatestatus(status, Long.parseLong(id));
        }
        return 1;
    }

    @Override
    public EsShopPackageGoods packageList(long id) {
        EsShopPackageGoods esPackageGoods = PackageGoodsMapper.selectById(id);
        List<EsShopPackageGoodsSpec> packageid = packageGoodsSpecMapper.selectList(new QueryWrapper<EsShopPackageGoodsSpec>().eq("package_id", esPackageGoods.getId()));
        esPackageGoods.setPackageGoodsSpecList(packageid);
        return esPackageGoods;
    }

    @Override
    public List<EsShopPackageGoods> packageList(EsShopPackageGoods goods) {
        return PackageGoodsMapper.packageList(goods);
    }

    @Override
    public Integer delete(long goodId, int according) {
        EsShopPackageGoods PackageGoods = PackageGoodsMapper.selectById(goodId);
        PackageGoods.setAccording(according);
        return PackageGoodsMapper.updateById(PackageGoods);
    }

    @Override
    public Integer updatePackage(String packageName, long goodId) {
        return 1;
    }

    public void addspec(EsShopPackageGoods entity) {
        if (entity.getPackageGoodsSpecList() != null && entity.getPackageGoodsSpecList().size() > 0) {
            for (EsShopPackageGoodsSpec goods : entity.getPackageGoodsSpecList()) {
                EsShopPackageGoodsSpec goodsspec = new EsShopPackageGoodsSpec();
                goodsspec.setPackageId(entity.getId());
                goodsspec.setGoodsId(goods.getGoodsId());
                if (entity.getPackageType() == 1) {
                    if (!goods.getSpecsId().equals("")) {
                        String[] spec = goods.getSpecsId().split(",");
                        String[] speprice = goods.getSpecprice().split(",");
                        for (int i = 0; i < spec.length; i++) {
                            goodsspec.setSpecsId(spec[i]);
                            BigDecimal big = new BigDecimal(speprice[i]);
                            goodsspec.setPackagePrice(big);
                            packageGoodsSpecMapper.insert(goodsspec);
                        }
                    } else {
                        packageGoodsSpecMapper.insert(goodsspec);
                    }
                } else {
                    goodsspec.setSpecsId(goods.getSpecsId());
                    goodsspec.setPackagePrice(goods.getPackagePrice());
                    packageGoodsSpecMapper.insert(goodsspec);
                }
            }
        }
    }
}
