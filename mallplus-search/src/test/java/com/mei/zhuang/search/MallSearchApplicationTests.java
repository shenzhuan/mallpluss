package com.mei.zhuang.search;

import com.mei.zhuang.search.dao.EsProductDao;
import com.mei.zhuang.search.domain.EsProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSearchApplicationTests {
    @Resource
    private EsProductDao productDao;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Test
    public void contextLoads() {
    }
    @Test
    public void testGetAllEsProductList(){
        List<EsProduct> esProductList = productDao.getAllEsProductList(null);
        System.out.print(esProductList);
    }
    @Test
    public void testEsProductMapping(){
        elasticsearchTemplate.putMapping(EsProduct.class);
        Map mapping = elasticsearchTemplate.getMapping(EsProduct.class);
        System.out.println(mapping);
    }

}
