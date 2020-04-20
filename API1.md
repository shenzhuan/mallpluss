# mallplus多商户 uniapp 接口文档
```

baseUrl: http://www.yjlive.cn:8083/

```

## 目录：

[1、获取首页轮播图列表](#1获取首页轮播图列表)<br/>
[2、获取商品列表信息（已审核和已上架）](#2、获取商品列表信息（已审核和已上架）)<br/>
[3、获取商户列表信息](#3获取商户列表信息)<br/>
[4、首页优惠券；未登录查询为过期的，登录后查询未领取和未过期的](#4首页优惠券；未登录查询为过期的，登录后查询未领取和未过期的)<br/>
[5、团购活动列表](#5团购活动列表)<br/>
[6、团购活动详情](#6团购活动详情)<br/>
[7、拼团列表](#7拼团列表)<br/>
[8、获取拼团列表](#8获取拼团列表)<br/>
[9、发起拼团](#9发起拼团)<br/>
[10、发起拼团下单](#10发起拼团下单)<br/>
[11、支付方式列表](#11支付方式列表)<br/>
[12、余额支付](#12余额支付)<br/>
[13、订单列表](#13订单列表)<br/>
[14、订单详情](#14订单详情)<br/>
[15、商品浏览记录](#15商品浏览记录)<br/>
[16、添加商品浏览记录](#添加商品浏览记录)<br/>
[17、获取商品评价信息](#17获取商品评价信息)<br/>
[18、收藏和取消收藏](#18收藏和取消收藏)<br/>
[19、获取我的收藏](#19获取我的收藏)<br/>
[20、删除收藏](#20删除收藏)<br/>
[21、用户信息](#21用户信息)<br/>
[22、获取收货地址列表](#22获取收货地址列表)<br/>
[23、添加收货地址](#23添加收货地址)<br/>
[24、删除收货地址](#24删除收货地址)<br/>
[25、我的优惠券列表](#25我的优惠券列表)<br/>
[26、我的售后列表](#26我的售后列表)<br/>
[27、申请售后](#27申请售后)<br/>
[28、确认收货](#28确认收货)<br/>
[29、订单评价](#29订单评价)<br/>
[30、图片上传](#30图片上传)<br/>
[31、我的购物车](#31我的购物车)<br/>
[32、删除购物车](#32删除购物车)<br/>
[33、商品详情](#33商品详情)<br/>
[34、加入购物车](#34加入购物车)<br/>
[35、商品详情立即购买](#35商品详情立即购买)<br/>
[36、商品详情下单](#36商品详情下单)<br/>
[37、清空购物车](#37清空购物车)<br/>
[38、购物车下单数据预览](#38购物车下单数据预览)<br/>
[39、购物车下单](#39购物车下单)<br/>
[40、商品分类](#40商品分类)<br/>
[41、用户密码登录](#41用户密码登录)<br/>
[42、用户密码注册](#42用户密码注册)<br/>
[43、获取手机验证码](#43获取手机验证码)<br/>
[44、手机和验证码登录](#44手机和验证码登录)<br/>




## 接口列表：

### 1、获取首页轮播图列表

#### 请求URL:  
```
http://www.yjlive.cn:8083/api/single/home/bannerList?storeId=0&type=2&authorization=Bearer%20eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzE0NjU4NzcyMiIsImNyZWF0ZWQiOjE1ODQyMzg2OTQ0NzAsImV4cCI6MTcwNDIzODY5NH0.5D9b_drjShLtOF1vIlQnVzxF0x1TA-imFJE_I_YMMLOglM_9p0KfTCxSLU_Dl8VTdqicfNwKxIubvPzTPia6SQ
```

#### 示例：
 [http://www.yjlive.cn:8083/api/single/home/bannerList?storeId=0&type=2](http://www.yjlive.cn:8083/api/single/home/bannerList?storeId=0&type=2)

#### 请求方式: 
```
GET
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|type      |Y       |int  |1->PC首页轮播；2->android首页轮播；3->ios首页轮播；4->h5首页轮播；5->pc首页轮播市 |
|storeId   |N       |int  |1  2 3

#### 返回示例：

```javascript
{
 {"code":200,"data":[{"clickCount":0,"endTime":1542988800000,"id":9,"name":"电影推荐广告","note":"电影推荐广告","orderCount":0,"pic":"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20181113/movie_ad.jpg","sort":100,"startTime":1541001600000,"status":1,"storeId":0,"type":2,"url":"www.baidu.com"},{"clickCount":0,"endTime":1543507200000,"id":11,"name":"汽车推荐广告","note":"","orderCount":0,"pic":"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20181113/car_ad2.jpg","sort":98,"startTime":1542038400000,"status":1,"storeId":0,"type":2,"url":"xxx"},{"clickCount":0,"endTime":1542988800000,"id":13,"name":"汽车促销广告","note":"","orderCount":0,"pic":"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20181113/car_ad.jpg","sort":99,"startTime":1542038400000,"status":1,"storeId":0,"type":2,"url":"xxx"},{"clickCount":0,"endTime":1579276800000,"id":26,"name":"轮播阿里云测试","note":"434","orderCount":0,"pic":"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20181113/car_ad.jpg","sort":54,"startTime":1578931200000,"status":1,"storeId":0,"type":2,"url":"12323"}]}
}
```

### 2、获取商品列表信息（已审核和已上架）

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/goods/list?pageNum=1
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/pms/goods/list?pageNum=1](http://www.yjlive.cn:8083/api/single/pms/goods/list?pageNum=1)

#### 请求方式：
```
GET
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
| isVip                      |N       |int   |1 vip商品 |
| isFenxiao                  |N       |int   |1 分销商品 |
| storeId                    |N       |int   |店铺编号 |
| areaId                     |N       |int   |城市id |
| schoolId                   |N       |int   |学校id |
| productAttributeCategoryId |N       |int   |类别id |
| productCategoryId          |N       |int   |分类id |
| recommandStatus            |N       |int   |1推荐商品 |
| brandId                    |N       |int   |品牌id |
| sort                       |N       |int   |排序 1销量 2价格 默认添加时间 |
| orderBy                    |N       |int   |1 倒序 2 顺序 默认倒序 |
| keyword                    |N       |int   |名称模糊搜索 |
| pageSize                   |N       |int   |单页数量 |
| pageNum                    |N       |int   |页码|

#### 返回示例：
```javascript
{"code":200,"data":{"current":1,"pages":129,"records":[{"albumPics":"","areaId":4103,"areaName":"","brandId":543,"brandName":"罗莱制造商","createTime":1584511861000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":10,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":25073,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"脑白金","newStatus":0,"note":"","originalPrice":30.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 2@3x(2).png","previewStatus":0,"price":50.00,"productAttributeCategoryId":401,"productCategoryId":3314,"productCategoryName":"","productSn":"sp001231","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":13,"schoolId":0,"schoolName":"","serviceIds":"1,2,3","sort":0,"stock":48,"storeId":1,"storeName":"北京豪车专卖","subTitle":"脑白金美味健康","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"包","usePointLimit":0,"verifyStatus":1,"weight":30.00},{"albumPics":"","areaId":12,"areaName":"","brandId":543,"brandName":"罗莱制造商","createTime":1584448280000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":16,"fenxiaoPrice":0.33,"giftGrowth":0,"giftPoint":0,"hit":0,"id":25071,"isFenxiao":1,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"111112222222","newStatus":1,"note":"","originalPrice":1.00,"pic":"","previewStatus":1,"price":11.00,"productAttributeCategoryId":385,"productCategoryId":3290,"productCategoryName":"女衣服","productSn":"11111111111","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":0,"schoolId":0,"schoolName":"","serviceIds":"1,2,3","sort":0,"stock":88,"storeId":1,"storeName":"北京豪车专卖","subTitle":"11111","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"箱","usePointLimit":0,"verifyStatus":1,"weight":3.00},{"albumPics":"","areaId":4401,"areaName":"","brandId":780,"brandName":" 七匹狼","createTime":1584238793000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":25066,"isFenxiao":1,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"上衣","newStatus":1,"note":"","originalPrice":199.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","previewStatus":0,"price":99.00,"productAttributeCategoryId":387,"productCategoryId":3309,"productCategoryName":"服装","productSn":"111","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":42,"schoolId":0,"schoolName":"","serviceIds":"","sort":0,"stock":77,"storeId":1189,"storeName":"佳佳百货","subTitle":"上衣","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0.00},{"albumPics":"","areaId":1101,"areaName":"","brandId":543,"brandName":"罗莱制造商","createTime":1582311611000,"deleteStatus":1,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":10,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":25039,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"test","newStatus":1,"note":"","originalPrice":0.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200222/3b2f80a50c6a1ac5fc557491a8736b77.jpg","previewStatus":0,"price":0.00,"productAttributeCategoryId":385,"productCategoryId":3300,"productCategoryName":"手机","productSn":"90","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":1,"schoolId":0,"schoolName":"","serviceIds":"1,2,3","sort":0,"stock":17316,"storeId":1,"storeName":"北京豪车专卖","subTitle":"123","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"斤","usePointLimit":0,"verifyStatus":1,"weight":0.00},{"albumPics":"","areaId":13,"areaName":"","brandId":542,"brandName":"Ralph Lauren制造商","createTime":1582256956000,"deleteStatus":1,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":25038,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"sad","newStatus":0,"note":"","originalPrice":0.00,"pic":"","previewStatus":0,"price":0.00,"productAttributeCategoryId":386,"productCategoryId":3275,"productCategoryName":"二级分类测试1","productSn":"asddas","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":0,"schoolId":0,"schoolName":"","serviceIds":"","sort":0,"stock":0,"storeId":1,"storeName":"北京豪车专卖","subTitle":"asdasd","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0.00},{"albumPics":"","areaId":0,"areaName":"","brandId":1001020,"brandName":"","createTime":1578262903000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":5394,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"美利奴羊毛盖毯设计师款","newStatus":1,"note":"","originalPrice":349.00,"pic":"http://yanxuan.nosdn.127.net/2bfecfe58ea3ee0d554f2ed58e9ba30a.png","previewStatus":0,"price":369.00,"productAttributeCategoryId":1008009,"productCategoryId":1008009,"productCategoryName":"","productSn":"1046044","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1008104,"schoolId":0,"schoolName":"","serviceIds":"","sort":34,"stock":1045956,"storeId":3,"storeName":"北京名衣专卖","subTitle":"欧洲知名品牌设计师联合打造","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"","areaId":0,"areaName":"","brandId":1001020,"brandName":"","createTime":1578262903000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":5411,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"全棉色织绗缝夏凉件套","newStatus":1,"note":"","originalPrice":599.00,"pic":"http://yanxuan.nosdn.127.net/69145abddddd31ae8878ea7ca7297b4b.png","previewStatus":0,"price":619.00,"productAttributeCategoryId":1036000,"productCategoryId":1036000,"productCategoryName":"","productSn":"1068012","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1036066,"schoolId":0,"schoolName":"","serviceIds":"","sort":8,"stock":1067955,"storeId":3,"storeName":"北京名衣专卖","subTitle":"夏季凉被，冬季暖套，四季可用","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"","areaId":0,"areaName":"","brandId":61,"brandName":"欧思路","createTime":1578262903000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":92,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","newStatus":1,"note":"","originalPrice":139.00,"pic":"https://img14.360buyimg.com/n7/jfs/t29722/14/1386865551/93819/2f07f87d/5cdd3184N4b991c77.jpg","previewStatus":0,"price":139.00,"productAttributeCategoryId":387,"productCategoryId":44,"productCategoryName":"连衣裙","productSn":"X1564467705309","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":101,"schoolId":2,"schoolName":"","serviceIds":"","sort":0,"stock":0,"storeId":2,"storeName":"北京豪车专卖","subTitle":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"","areaId":0,"areaName":"","brandId":1001000,"brandName":"","createTime":1578262903000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":660,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"日式圆形宠物盆猫砂盆","newStatus":1,"note":"","originalPrice":89.00,"pic":"http://yanxuan.nosdn.127.net/f0abf2bf11c8d303212e4a0c1106bb73.png","previewStatus":0,"price":109.00,"productAttributeCategoryId":1017000,"productCategoryId":603,"productCategoryName":"","productSn":"1071004","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1017017,"schoolId":0,"schoolName":"","serviceIds":"","sort":20,"stock":1070987,"storeId":2,"storeName":"北京豪车专卖","subTitle":"日式配色，圆滑细腻","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"","areaId":0,"areaName":"","brandId":1001000,"brandName":"","createTime":1578262903000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":661,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"便携多功能宠物拾便器","newStatus":1,"note":"","originalPrice":39.00,"pic":"http://yanxuan.nosdn.127.net/07a47d73e2eb53b1a7939219a4e63618.png","previewStatus":0,"price":59.00,"productAttributeCategoryId":1017000,"productCategoryId":604,"productCategoryName":"","productSn":"1071005","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1017012,"schoolId":0,"schoolName":"","serviceIds":"","sort":17,"stock":1071001,"storeId":2,"storeName":"北京豪车专卖","subTitle":"方便携带，环保卫生","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0}],"size":10,"total":1284}}
```

### 3、获取商户列表信息

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/store/store/list
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/store/store/list](http://www.yjlive.cn:8083/api/single/store/store/list)

#### 请求方式：
```
GET
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
| pageSize|N       |int   |单页数量 |
| pageNum|N       |int   |页码|
| isBoutique|N       |int   |1推荐|
| isChecked|N       |int   |1 hit量倒序 2 收藏倒序|


#### 返回示例：

```javascript
{"code":200,"data":{"current":1,"pages":2,"records":[{"addressArea":"荔湾区","addressCity":"广州市","addressDetail":"","addressLat":"0","addressLng":"0","addressProvince":"广东省","buyPlanTimes":0,"collect":0,"contactMobile":"","contactName":"","contactQq":"284924933","contactQrcode":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20200315-b9ec74e565714987a2b7d4d18ed0725f.png","createTime":1584238127000,"deleteTime":null,"description":"卖水果了","diyProfile":"","expireTime":null,"goodsCount":0,"hit":0,"id":1189,"industryOne":0,"industryTwo":0,"isBoutique":1,"isChecked":0,"isDeleted":0,"isStar":0,"isTry":0,"lastLoginTime":null,"list":[],"logo":"","name":"佳佳百货","planId":0,"registerType":0,"servicePhone":"","smsQuantity":0,"status":3,"supportName":"","supportPhone":"13556193079","tryTime":1584238127000,"type":1,"uid":0},{"addressArea":"西城区","addressCity":"市辖区","addressDetail":"广东省深圳市宝安区西乡大道341号国汇通商务大厦","addressLat":"22.57223","addressLng":"113.86357","addressProvince":"北京市","buyPlanTimes":0,"collect":0,"contactMobile":"","contactName":"","contactQq":"153456","contactQrcode":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20200304-a4f81fee222f4dedb199c5721bf4e179.png","createTime":1583288690000,"deleteTime":null,"description":"哈哈哈哈哈","diyProfile":"","expireTime":null,"goodsCount":0,"hit":0,"id":1103,"industryOne":0,"industryTwo":0,"isBoutique":1,"isChecked":0,"isDeleted":0,"isStar":0,"isTry":0,"lastLoginTime":null,"list":[],"logo":"","name":"小木","planId":0,"registerType":0,"servicePhone":"","smsQuantity":0,"status":3,"supportName":"","supportPhone":"13512345678","tryTime":1583288690000,"type":1,"uid":0},{"addressArea":"吉州区","addressCity":"吉安市","addressDetail":"","addressLat":"0","addressLng":"0","addressProvince":"江西省","buyPlanTimes":0,"collect":0,"contactMobile":"","contactName":"","contactQq":"4564231","contactQrcode":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20200304-f1b9135c7b054fff90569f45095958bd.png","createTime":1583268356000,"deleteTime":null,"description":"","diyProfile":"","expireTime":null,"goodsCount":0,"hit":0,"id":1096,"industryOne":0,"industryTwo":0,"isBoutique":1,"isChecked":0,"isDeleted":0,"isStar":0,"isTry":0,"lastLoginTime":null,"list":[],"logo":"","name":"测试商户","planId":0,"registerType":0,"servicePhone":"","smsQuantity":0,"status":3,"supportName":"","supportPhone":"13850361234","tryTime":1583268356000,"type":1,"uid":0},{"addressArea":"西城区","addressCity":"市辖区","addressDetail":"北京市朝阳区日坛北街33号朝阳区政府","addressLat":"39.9219","addressLng":"116.44355","addressProvince":"北京市","buyPlanTimes":0,"collect":0,"contactMobile":"","contactName":"","contactQq":"12345678","contactQrcode":"","createTime":1577658441000,"deleteTime":null,"description":"一家花店","diyProfile":"","expireTime":null,"goodsCount":0,"hit":0,"id":526,"industryOne":0,"industryTwo":0,"isBoutique":1,"isChecked":0,"isDeleted":0,"isStar":0,"isTry":0,"lastLoginTime":null,"list":[],"logo":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20191230-06324c64b4e342e2a238a29639574379.jpg","name":"一家花店","planId":0,"registerType":0,"servicePhone":"","smsQuantity":0,"status":3,"supportName":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20191230-a5b848ed2c774136950edf1e7e6127bc.jpeg","supportPhone":"13146581133","tryTime":1577658441000,"type":1,"uid":0},{"addressArea":"1","addressCity":"1","addressDetail":"1","addressLat":"1","addressLng":"1","addressProvince":"1","buyPlanTimes":1,"collect":1,"contactMobile":"1","contactName":"北京皮包专卖","contactQq":"1","contactQrcode":"1","createTime":1558191645000,"deleteTime":1558191672000,"description":"1","diyProfile":"1","expireTime":1558191635000,"goodsCount":0,"hit":5,"id":1,"industryOne":1,"industryTwo":1,"isBoutique":1,"isChecked":1,"isDeleted":1,"isStar":1,"isTry":1,"lastLoginTime":1558191742000,"list":[],"logo":"https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3190817320,3163827622&fm=26&gp=0.jpg","name":"北京皮包专卖","planId":1,"registerType":1,"servicePhone":"1","smsQuantity":1,"status":3,"supportName":"1","supportPhone":"1","tryTime":1558191638000,"type":1,"uid":1},{"addressArea":"0","addressCity":"0","addressDetail":"","addressLat":"北京","addressLng":"0","addressProvince":"0","buyPlanTimes":0,"collect":3,"contactMobile":"","contactName":"","contactQq":"951449465","contactQrcode":"","createTime":null,"deleteTime":null,"description":"","diyProfile":"","expireTime":null,"goodsCount":0,"hit":4,"id":2,"industryOne":0,"industryTwo":0,"isBoutique":1,"isChecked":0,"isDeleted":0,"isStar":0,"isTry":0,"lastLoginTime":null,"list":[],"logo":"https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2090841040,2025243380&fm=26&gp=0.jpg","name":"北京豪车专卖","planId":0,"registerType":0,"servicePhone":"","smsQuantity":0,"status":3,"supportName":"","supportPhone":"13146587788","tryTime":null,"type":1,"uid":0},{"addressArea":"0","addressCity":"0","addressDetail":"","addressLat":"11","addressLng":"0","addressProvince":"0","buyPlanTimes":0,"collect":2,"contactMobile":"","contactName":"","contactQq":"11","contactQrcode":"","createTime":null,"deleteTime":null,"description":"","diyProfile":"","expireTime":null,"goodsCount":0,"hit":3,"id":3,"industryOne":0,"industryTwo":0,"isBoutique":1,"isChecked":0,"isDeleted":0,"isStar":0,"isTry":0,"lastLoginTime":null,"list":[],"logo":"https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=47694830,3660792876&fm=26&gp=0.jpg","name":"北京名衣专卖","planId":0,"registerType":0,"servicePhone":"","smsQuantity":0,"status":3,"supportName":"111111","supportPhone":"11","tryTime":null,"type":1,"uid":0},{"addressArea":"0","addressCity":"0","addressDetail":"","addressLat":"123","addressLng":"0","addressProvince":"0","buyPlanTimes":0,"collect":4,"contactMobile":"","contactName":"","contactQq":"12345","contactQrcode":"","createTime":null,"deleteTime":null,"description":"","diyProfile":"","expireTime":null,"goodsCount":0,"hit":2,"id":4,"industryOne":0,"industryTwo":0,"isBoutique":1,"isChecked":0,"isDeleted":0,"isStar":0,"isTry":0,"lastLoginTime":null,"list":[],"logo":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20190807/QQ%E5%9B%BE%E7%89%8720190807191952.jpg","name":"北京房屋专卖","planId":0,"registerType":0,"servicePhone":"","smsQuantity":0,"status":3,"supportName":"111111","supportPhone":"123","tryTime":null,"type":1,"uid":0}],"size":8,"total":11}}
```

### 4、首页优惠券（未登录 查询为过期的，登录后查询未领取和未过期的）

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/home/selectNotRecive?pageSize=3
```

#### 示例：
http://www.yjlive.cn:8083/api/single/home/selectNotRecive?pageSize=3](http://www.yjlive.cn:8083/api/single/home/selectNotRecive?pageSize=3)

#### 请求方式：
```
GET
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|Integer pageSize|N       |int   |单页数量 |

#### 返回示例：

```javascript
{"code":200,"data":[{"amount":1.00,"code":"","count":0,"enableTime":null,"endTime":1636099200000,"id":50,"memberLevel":0,"minPoint":200.00,"name":"111","note":"","perLimit":1,"platform":0,"publishCount":1,"receiveCount":1,"startTime":1573632000000,"storeId":1,"type":0,"useCount":0,"useType":0},{"amount":1.00,"code":"","count":0,"enableTime":null,"endTime":1636099200000,"id":52,"memberLevel":0,"minPoint":1.00,"name":"123","note":"","perLimit":1,"platform":0,"publishCount":1,"receiveCount":1,"startTime":1572768000000,"storeId":1,"type":0,"useCount":0,"useType":0},{"amount":1.00,"code":"","count":21,"enableTime":null,"endTime":1585296000000,"id":62,"memberLevel":0,"minPoint":20.00,"name":"asasasa","note":"asasd","perLimit":1,"platform":0,"publishCount":22,"receiveCount":1,"startTime":1583654400000,"storeId":1,"type":0,"useCount":0,"useType":2}]}
```

### 5、团购活动列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/sms/groupActivityList?pageNum=1
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/sms/groupActivityList?pageNum=1](http://www.yjlive.cn:8083/api/single/sms/groupActivityList?pageNum=1)

#### 请求方式：
```
GET
```

#### 参数类型：

|参数|是否必选|类型|说明|
|Integer pageSize|N       |int   |单页数量 |
|Integer pageNum|N       |int   |页码|

#### 返回示例：

```javascript
{"code":200,"data":{"current":1,"pages":1,"records":[{"createTime":"2019-10-12T18:11:50","feestatus":2,"goodsIds":"22818","id":2,"name":"包套装","originprice":79.00,"pic":"https://img10.360buyimg.com/n7/jfs/t1/83640/18/5361/160844/5d395179E2b16a8b8/f1d257e1b930b8da.jpg","price":50.00,"productList":[{"albumPics":"","areaId":0,"areaName":"","brandId":1001000,"brandName":"","createTime":1577831453000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":22818,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"男式玩色内裤","newStatus":1,"note":"","originalPrice":59.00,"pic":"http://yanxuan.nosdn.127.net/922fdbe007033f7a88f7ebc57c3d1e75.png","previewStatus":0,"price":79.00,"productAttributeCategoryId":1010002,"productCategoryId":1010002,"productCategoryName":"","productSn":"1056002","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":0,"qsType":0,"recommandStatus":0,"sale":1010023,"schoolId":0,"schoolName":"","serviceIds":"","sort":7,"stock":1055984,"storeId":1,"storeName":"北京皮包专卖","subTitle":"德国工艺，多色随搭","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0}],"status":1,"transfee":0},{"createTime":null,"feestatus":0,"goodsIds":"24595,24596","id":3,"name":"123","originprice":3037.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20191014/balancepay.png","price":123.00,"productList":[],"status":1,"transfee":11},{"createTime":null,"feestatus":0,"goodsIds":"25026","id":9,"name":"00000","originprice":4000.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20191207/barcode.png","price":10.00,"productList":[],"status":1,"transfee":100},{"createTime":null,"feestatus":0,"goodsIds":"25045","id":11,"name":"商品","originprice":11.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200301/club_cashier_icon.png","price":1.00,"productList":[],"status":1,"transfee":1},{"createTime":null,"feestatus":0,"goodsIds":"25044","id":12,"name":"对对对","originprice":9.90,"pic":"","price":15151.00,"productList":[],"status":1,"transfee":11},{"createTime":null,"feestatus":0,"goodsIds":"25034","id":14,"name":"消毒套餐","originprice":80.00,"pic":"","price":60.00,"productList":[{"albumPics":"","areaId":0,"areaName":"","brandId":541,"brandName":"MUJI制造商","createTime":1581986148000,"deleteStatus":1,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":25034,"isFenxiao":0,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"高原牛肉","newStatus":1,"note":"","originalPrice":80.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200304/184.jpeg","previewStatus":1,"price":80.00,"productAttributeCategoryId":385,"productCategoryId":3280,"productCategoryName":"九九","productSn":"smte0001","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":0,"qsType":0,"recommandStatus":1,"sale":2,"schoolId":0,"schoolName":"","serviceIds":"3","sort":0,"stock":2108,"storeId":1,"storeName":"北京豪车专卖","subTitle":"苏门塔尔牛肉","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"箱","usePointLimit":0,"verifyStatus":1,"weight":230.00}],"status":1,"transfee":5},{"createTime":null,"feestatus":0,"goodsIds":"25066","id":15,"name":"mm","originprice":99.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","price":9.00,"productList":[{"albumPics":"","areaId":4401,"areaName":"","brandId":780,"brandName":" 七匹狼","createTime":1584238793000,"deleteStatus":0,"description":"","detailDesc":"","detailHtml":"","detailMobileHtml":"","detailTitle":"","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":0,"id":25066,"isFenxiao":1,"isPaiMai":0,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"上衣","newStatus":1,"note":"","originalPrice":199.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","previewStatus":0,"price":99.00,"productAttributeCategoryId":387,"productCategoryId":3309,"productCategoryName":"服装","productSn":"111","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":42,"schoolId":0,"schoolName":"","serviceIds":"","sort":0,"stock":77,"storeId":1189,"storeName":"佳佳百货","subTitle":"上衣","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0.00}],"status":1,"transfee":0}],"size":100,"total":7}}
```

### 6、团购活动详情

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/sms/group.activity.getdetial?id=3
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/sms/group.activity.getdetial?id=3](http://www.yjlive.cn:8083/api/single/sms/group.activity.getdetial?id=3)

#### 请求方式：
```
GET
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|id      |Y      |int |id|


#### 返回示例：

```javascript

```


### 7、拼团列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/groupHotGoods/list?pageNum=1
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/pms/groupHotGoods/list?pageNum=1](http://www.yjlive.cn:8083/api/single/pms/groupHotGoods/list?pageNum=1)

#### 请求方式：
```
GET
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|Integer pageSize|N       |int   |单页数量 |
|Integer pageNum|N       |int   |页码|

#### 返回示例：

```javascript
{"code":200,"data":[{"createTime":1570083658000,"endTime":1698652800000,"goods":{"albumPics":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20191007/balancepay.png,http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20191007/coupon-element.png","areaId":0,"areaName":"","brandId":61,"brandName":"欧思路","createTime":1578262903000,"deleteStatus":0,"description":"品牌： 唯唯（WEWE）\n                                \n                            \n                        \n                                                \n                                商品名称：WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)\n    商品编号：47871067624\n                      店铺： WEWE旗舰店\n                     商品毛重：500.00g\n                货号：F93741\n                                版型：修身型\n                  领型：圆领\n                  流行元素：不规则\n                  图案文化：无图案\n                  材质：棉\n                  袖型：常规袖\n                  适用年龄：25-29周岁\n                  风格：休闲风，简约，百搭\n                  衣长：常规款\n                  组合形式：单件\n                  袖长：短袖\n                  上市时间：2019年夏季\n                                          \n                                                \n                            更多参数>>","detailDesc":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","detailHtml":"<p><img class=\"\" style=\"width: auto; height: auto; max-width: 100%;\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t11401/113/3026363602/145812/42de0009/5cdd3141N447438be.jpg\" /><img class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t10303/198/2978835002/18844/86c46ff5/5cdd3141N7ab352ec.jpg\" /></p>","detailMobileHtml":"","detailTitle":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":8,"giftPoint":0,"hit":1075,"id":92,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","newStatus":1,"note":"","originalPrice":139.00,"pic":"https://img14.360buyimg.com/n7/jfs/t29722/14/1386865551/93819/2f07f87d/5cdd3184N4b991c77.jpg","previewStatus":0,"price":139.00,"productAttributeCategoryId":387,"productCategoryId":44,"productCategoryName":"连衣裙","productSn":"X1564467705309","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":101,"schoolId":2,"schoolName":"","serviceIds":"","sort":0,"stock":0,"storeId":2,"storeName":"北京豪车专卖","subTitle":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","supplyId":0,"timeSecound":null,"transfee":10,"type":1,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0},"goodsId":92,"goodsName":"20寸 铝镁合金登机箱","groupPrice":9,"hours":9,"id":13,"limitGoods":10,"maxPeople":90,"originPrice":879,"peoples":0,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20190830/uniapp.jpeg","pintuan_start_status":0,"startTime":1570089600000,"status":0,"storeId":2,"timeSecound":null},{"createTime":1576106219000,"endTime":1698652800000,"goods":{"albumPics":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","areaId":0,"areaName":"","brandId":61,"brandName":"欧思路","createTime":1564438905000,"deleteStatus":0,"description":"品牌： FILA\n                                \n                            \n                        \n                                                \n                                商品名称：FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL\n    商品编号：47740462787\n                      店铺： FILA斐乐服饰旗舰店\n                     商品毛重：0.8kg\n                货号：F11W927122F\n                                版型：其它\n                  领型：POLO领\n                  流行元素：印花\n                  衣长：常规款\n                  组合形式：其它\n                  材质：聚酰胺纤维(锦纶)\n                  袖型：其它\n                  适用年龄：18-24周岁\n                  图案文化：其它\n                  图案：字母，品牌LOGO\n                  风格：简约，运动风，百搭\n                  袖长：短袖\n                  上市时间：2019年夏季\n                                          \n                                                \n                            更多参数>>","detailDesc":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","detailHtml":"<img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t11017/216/2925553326/397804/3d38f939/5cdccd98Na29407ef.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29986/337/1318891484/157211/56dfcace/5cdccd97N97f85c06.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30256/79/1323110900/91513/f7c0f34f/5cdccd97Nb455f92d.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t10678/212/2914717723/180951/9511024d/5cdccd98Na8290e1d.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30508/334/1302925282/97704/7c6911be/5cdccd98Nac4cf4ed.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/53558/7/509/110852/5cdccd98E143e2af5/e50b2ffb70c1c527.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/38595/36/6981/145235/5cdccda6E47de4e8b/5e94b63d7e6e7355.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t10006/244/2899222461/139756/f0165ccb/5cdccd99N456bd5f6.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30124/323/1325247737/101813/7a445a57/5cdccd99N83150f28.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29317/264/1313145537/113858/b69a0f48/5cdccd99Nd435008a.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t28198/249/1314487457/144606/3c71ff72/5cdccd9aN6a4e0dc0.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29806/292/1338532051/57463/a14b9dcd/5cdccd9aN9f7da091.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29794/293/1282500912/33889/f41b4236/5cdccd9aNdc528b6a.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29743/313/1272516579/266496/475ec2fe/5cdccd9bN6fddb618.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t11395/145/2924258149/432972/e119f9c7/5cdccd9cNc4612362.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t28006/215/1344690766/135392/7bf6e810/5cdccd9bNbac05c82.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30295/331/1297389421/311878/c85e66d8/5cdccd9cNccb68d6e.jpg\"><br>","detailMobileHtml":"","detailTitle":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":3,"giftPoint":0,"hit":156,"id":93,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","newStatus":1,"note":"","originalPrice":680.00,"pic":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","previewStatus":0,"price":509.00,"productAttributeCategoryId":387,"productCategoryId":58,"productCategoryName":"连衣裙","productSn":"X1564467705416","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":76,"schoolId":2,"schoolName":"","serviceIds":"","sort":0,"stock":39,"storeId":1,"storeName":"北京皮包专卖","subTitle":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","supplyId":0,"timeSecound":null,"transfee":10,"type":1,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0},"goodsId":93,"goodsName":"testc","groupPrice":1,"hours":1,"id":19,"limitGoods":10,"maxPeople":2,"originPrice":169,"peoples":0,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20190830/uniapp.jpeg","pintuan_start_status":0,"startTime":1576134998000,"status":0,"storeId":2,"timeSecound":null},{"createTime":1583192821000,"endTime":1585123200000,"goods":{"albumPics":"http://yanxuan.nosdn.127.net/d8c18953bcb05f0b07d6b48e2d159ace.png","areaId":0,"areaName":"","brandId":1001000,"brandName":"","createTime":1577754293000,"deleteStatus":0,"description":"可抓可睡，一物两用","detailDesc":"可抓可睡，一物两用","detailHtml":"<p><img src=\"http://yanxuan.nosdn.127.net/7f5a8783df659ea49722ea12304a5f63.jpg\" _src=\"http://yanxuan.nosdn.127.net/7f5a8783df659ea49722ea12304a5f63.jpg\"/></p>","detailMobileHtml":"<p><img src=\"http://yanxuan.nosdn.127.net/7f5a8783df659ea49722ea12304a5f63.jpg\" _src=\"http://yanxuan.nosdn.127.net/7f5a8783df659ea49722ea12304a5f63.jpg\"/></p>","detailTitle":"可抓可睡，一物两用","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":1017001,"giftPoint":1017000,"hit":19,"id":22729,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"圆钵碗状高密材质猫抓板","newStatus":1,"note":"","originalPrice":59.00,"pic":"http://yanxuan.nosdn.127.net/d8c18953bcb05f0b07d6b48e2d159ace.png","previewStatus":0,"price":79.00,"productAttributeCategoryId":1017000,"productCategoryId":1017000,"productCategoryName":"","productSn":"1092039","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1017004,"schoolId":0,"schoolName":"","serviceIds":"","sort":18,"stock":1092035,"storeId":1,"storeName":"北京皮包专卖","subTitle":"可抓可睡，一物两用","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0},"goodsId":22729,"goodsName":"","groupPrice":121,"hours":3,"id":27,"limitGoods":11,"maxPeople":2,"originPrice":79,"peoples":0,"pic":"","pintuan_start_status":0,"startTime":1583740800000,"status":0,"storeId":1,"timeSecound":null},{"createTime":1584244153000,"endTime":1585555200000,"goods":{"albumPics":"","areaId":4401,"areaName":"","brandId":780,"brandName":" 七匹狼","createTime":1584238793000,"deleteStatus":0,"description":"上衣","detailDesc":"","detailHtml":"","detailMobileHtml":"<p><img class=\"wscnph\" src=\"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg\" /></p>","detailTitle":"","expireTime":1584604800000,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":1,"giftPoint":0,"hit":88,"id":25066,"isFenxiao":1,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"上衣","newStatus":1,"note":"","originalPrice":199.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","previewStatus":0,"price":99.00,"productAttributeCategoryId":387,"productCategoryId":3309,"productCategoryName":"服装","productSn":"111","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":42,"schoolId":0,"schoolName":"","serviceIds":"","sort":0,"stock":77,"storeId":1189,"storeName":"佳佳百货","subTitle":"上衣","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0.00},"goodsId":25066,"goodsName":"上衣","groupPrice":33,"hours":111,"id":29,"limitGoods":2,"maxPeople":2,"originPrice":99,"peoples":0,"pic":"","pintuan_start_status":0,"startTime":1584259200000,"status":0,"storeId":1189,"timeSecound":null}]}
```


### 8、获取所有商铺分类列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/goodsGroup/detail?id=93&groupId=1
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/pms/goodsGroup/detail?id=93&groupId=](http://www.yjlive.cn:8083/api/single/pms/goodsGroup/detail?id=93&groupId=y)

#### 请求方式：
```
GET
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|id      |Y      |int |id|

#### 返回示例：

```javascript

```


### 9、 发起拼团

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/addGroup
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/oms/addGroup](http://www.yjlive.cn:8083/api/single/oms/addGroup)

#### 请求方式：
```
POST
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|groupId      |N       |long   |拼团编号 |
|goodsId      |N       |long   |商品编号 |

#### 返回示例：

```javascript
{"code":200,"data":{"address":{"city":"市辖区","defaultStatus":1,"detailAddress":"分身乏术的发生地","id":166,"latitude":0.0,"longitude":0.0,"memberId":297,"name":"时尚大方","phoneNumber":"13811090913","postCode":"","province":"北京市","region":"西城区","storeId":1},"basicGiftsList":[],"blance":0,"calcAmount":{"freightAmount":10,"payAmount":11,"promotionAmount":0,"totalAmount":1},"cartPromotionItemList":[{"checked":1,"createDate":1584774708733,"deleteStatus":0,"id":0,"isFenxiao":0,"memberId":297,"memberNickname":"","modifyDate":null,"price":1,"product":null,"productAttr":"","productBrand":"欧思路","productCategoryId":58,"productId":93,"productName":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","productPic":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","productSkuCode":"","productSkuId":0,"productSn":"","productSubTitle":"","quantity":1,"skuStock":null,"sp1":"","sp2":"","sp3":"","storeId":0,"storeName":""}],"couponHistoryDetailList":[],"goods":null,"groupActivity":null,"groupAndOrderVo":null,"integrationAmount":0,"integrationConsumeSetting":{"couponStatus":0,"deductionPerAmount":1000,"id":1,"login":20,"maxPercentPerOrder":50,"orders":100,"register":2000,"sign":200,"storeId":1,"useUnit":100},"memberIntegration":28460,"memberReceiveAddressList":[{"city":"市辖区","defaultStatus":1,"detailAddress":"分身乏术的发生地","id":166,"latitude":0.0,"longitude":0.0,"memberId":297,"name":"时尚大方","phoneNumber":"13811090913","postCode":"","province":"北京市","region":"西城区","storeId":1}],"storeName":""}}
```

### 10、发起拼团下单

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/acceptGroup
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/oms/acceptGroup](http://www.yjlive.cn:8083/api/single/oms/acceptGroups)

#### 请求方式：
```
POST
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|content      |N       |string   |下单备注 |
|mgId      |Y       |int   |参与拼团 团购记录id |
|groupType      |Y       |int   |1 发起拼团 2 参与拼团 |
|type      |Y       |int   | 1 商品详情 2 勾选购物车 3全部购物车的商品 |
|orderType      |Y       |int   |1 普通订单 2 秒杀订单 3 团购订单 4 拼团订单 5 积分订单 |
|goodsId      |Y       |int   |商品编号 |
|addressId      |Y       |int   |地址编号 |
|couponId      |Y       |int   |优惠券编号 |
|memberCouponId      |Y       |string   |使用领取的优惠券编号 |


#### 返回示例：

```javascript
{"code":200,"data":{"orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5691,"integrationAmount":0,"orderId":3922,"orderSn":"15847748406380101297","productAttr":"","productBrand":"欧思路","productCategoryId":58,"productId":93,"productName":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","productPic":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","productPrice":509.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":0,"storeName":"","type":1}],"order":{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584774840638,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":0,"goodsName":"","groupId":148,"growth":0,"historyList":[],"id":3922,"integration":0,"integrationAmount":0,"isComment":1,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[],"orderSn":"15847748406380101297","orderType":2,"payAmount":11,"payCode":"","payType":1,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"市辖区","receiverDetailAddress":"分身乏术的发生地","receiverId":166,"receiverName":"时尚大方","receiverPhone":"13811090913","receiverPostCode":"","receiverProvince":"北京市","receiverRegion":"西城区","schoolId":0,"sourceType":1,"status":12,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":1,"totalAmount":509.00,"useIntegration":0}}}
```


### 11、支付方式列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/pay/paymentlist
```

#### 示例：
[http://www.yjlive.cn:8083/api/pay/paymentlist](http://www.yjlive.cn:8083/api/pay/paymentlist)

#### 请求方式：
```
GET
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|



#### 返回示例：

```javascript
{"code":200,"data":[{"code":"wechatpay","id":1,"isOnline":1,"memo":"点击微信支付","name":"微信支付","params":"点击微信支付","sort":111,"status":1},{"code":"alipay","id":2,"isOnline":1,"memo":"点击去支付宝支付","name":"支付宝支付","params":"","sort":100,"status":1},{"code":"balancepay","id":4,"isOnline":1,"memo":"账户余额支付","name":"余额支付","params":"","sort":100,"status":1}]}
```


### 12、余额支付

#### 请求URL:  
```
http://www.yjlive.cn:8083/api/pay/balancePay
```

#### 示例：
[http://www.yjlive.cn:8083/api/pay/balancePay](http://www.yjlive.cn:8083/api/pay/balancePay)
#### 请求方式: 
```
POST
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|orderId      |Y       |int  |订单编号 |

#### 返回示例：

```javascript
{"code":200,"data":{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0.00,"couponId":0,"createTime":1584774841000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0.00,"freightAmount":0,"goodsId":0,"goodsName":"","groupId":148,"growth":0,"historyList":[],"id":3922,"integration":0,"integrationAmount":0.00,"isComment":1,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[],"orderSn":"15847748406380101297","orderType":2,"payAmount":11.00,"payCode":"","payType":3,"paymentTime":1584775364333,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"市辖区","receiverDetailAddress":"分身乏术的发生地","receiverId":166,"receiverName":"时尚大方","receiverPhone":"13811090913","receiverPostCode":"","receiverProvince":"北京市","receiverRegion":"西城区","schoolId":0,"sourceType":1,"status":2,"storeId":1,"storeName":"","supplyId":0,"taxCode":"","taxContent":"商品详情","taxTitle":"","taxType":1,"totalAmount":509.00,"useIntegration":0}}
```


### 13、订单列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/order/list?pageNum=1&status=0
```

#### 示例：


#### 请求方式：
```
get
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|status      |Y       |int   | 订单状态 0全部 12->待付款；2->待发货；3->待收货；4->待评价；5->已完成|
|pageSize    |N       |int     |单页数量 |
|pageNum     |N       |int     |页码|

#### 返回示例：

```javascript
{"code":200,"data":{"current":1,"pages":7,"records":[{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584774841000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":0,"goodsName":"","groupId":0,"growth":0,"historyList":[],"id":3922,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5691,"integrationAmount":0,"orderId":3922,"orderSn":"15847748406380101297","productAttr":"","productBrand":"欧思路","productCategoryId":58,"productId":93,"productName":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","productPic":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","productPrice":509.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"","type":1}],"orderSn":"15847748406380101297","orderType":2,"payAmount":11.00,"payCode":"","payType":3,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":509.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584773459000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":5394,"goodsName":"美利奴羊毛盖毯设计师款","groupId":0,"growth":0,"historyList":[],"id":3921,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5690,"integrationAmount":0,"orderId":3921,"orderSn":"15847734585890101297","productAttr":"","productBrand":"","productCategoryId":1008009,"productId":5394,"productName":"美利奴羊毛盖毯设计师款","productPic":"http://yanxuan.nosdn.127.net/2bfecfe58ea3ee0d554f2ed58e9ba30a.png","productPrice":369.00,"productQuantity":2,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"北京名衣专卖","type":1}],"orderSn":"15847734585890101297","orderType":1,"payAmount":748.00,"payCode":"","payType":1,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":15,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":738.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584772568000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":661,"goodsName":"便携多功能宠物拾便器","groupId":0,"growth":0,"historyList":[],"id":3920,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5688,"integrationAmount":0,"orderId":3920,"orderSn":"15847725676040101297","productAttr":"","productBrand":"","productCategoryId":604,"productId":661,"productName":"便携多功能宠物拾便器","productPic":"http://yanxuan.nosdn.127.net/07a47d73e2eb53b1a7939219a4e63618.png","productPrice":59.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"北京豪车专卖","type":1}],"orderSn":"15847725676040101297","orderType":1,"payAmount":69.00,"payCode":"","payType":3,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":59.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584764702000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":661,"goodsName":"便携多功能宠物拾便器","groupId":0,"growth":0,"historyList":[],"id":3919,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5686,"integrationAmount":0,"orderId":3919,"orderSn":"15847647016790101297","productAttr":"","productBrand":"","productCategoryId":604,"productId":661,"productName":"便携多功能宠物拾便器","productPic":"http://yanxuan.nosdn.127.net/07a47d73e2eb53b1a7939219a4e63618.png","productPrice":59.00,"productQuantity":2,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"北京豪车专卖","type":1}],"orderSn":"15847647016790101297","orderType":1,"payAmount":98.00,"payCode":"","payType":1,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":15,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":118.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584715782000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":25066,"goodsName":"上衣","groupId":0,"growth":0,"historyList":[],"id":3918,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5684,"integrationAmount":0,"orderId":3918,"orderSn":"15847157816770101297","productAttr":"","productBrand":"","productCategoryId":1008002,"productId":22782,"productName":"帆布丝羽绒多用坐垫","productPic":"http://yanxuan.nosdn.127.net/19ecd7c6f6f31219cf75117238d95139.png","productPrice":59.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"北京皮包专卖","type":1},{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5685,"integrationAmount":0,"orderId":3918,"orderSn":"15847157816770101297","productAttr":"","productBrand":" 七匹狼","productCategoryId":3309,"productId":25066,"productName":"上衣","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","productPrice":99.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"佳佳百货","type":1}],"orderSn":"15847157816770101297","orderType":1,"payAmount":158.00,"payCode":"","payType":3,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":158.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584714247000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":25073,"goodsName":"脑白金","groupId":0,"growth":0,"historyList":[],"id":3917,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5682,"integrationAmount":0,"orderId":3917,"orderSn":"15847142472650101297","productAttr":"","productBrand":"罗莱制造商","productCategoryId":3314,"productId":25073,"productName":"脑白金","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 2@3x(2).png","productPrice":50.00,"productQuantity":3,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"北京豪车专卖","type":1}],"orderSn":"15847142472650101297","orderType":1,"payAmount":162.00,"payCode":"","payType":3,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":150.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584699502000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":5394,"goodsName":"美利奴羊毛盖毯设计师款","groupId":0,"growth":0,"historyList":[],"id":3916,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5680,"integrationAmount":0,"orderId":3916,"orderSn":"15846995021250101297","productAttr":"","productBrand":"","productCategoryId":1008009,"productId":5394,"productName":"美利奴羊毛盖毯设计师款","productPic":"http://yanxuan.nosdn.127.net/2bfecfe58ea3ee0d554f2ed58e9ba30a.png","productPrice":369.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"北京名衣专卖","type":1}],"orderSn":"15846995021250101297","orderType":1,"payAmount":379.00,"payCode":"","payType":3,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":369.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584675269000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":13,"goodsName":"消毒湿纸巾","groupId":0,"growth":0,"historyList":[],"id":3915,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5679,"integrationAmount":0,"orderId":3915,"orderSn":"","productAttr":"","productBrand":"","productCategoryId":24,"productId":13,"productName":"消毒湿纸巾","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200304/商品图片1.jpg","productPrice":19.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":0,"storeId":1,"storeName":"","type":1}],"orderSn":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200304/商品图片1.jpg","orderType":5,"payAmount":0.00,"payCode":"","payType":5,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":19.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584675268000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":13,"goodsName":"消毒湿纸巾","groupId":0,"growth":0,"historyList":[],"id":3914,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5678,"integrationAmount":0,"orderId":3914,"orderSn":"","productAttr":"","productBrand":"","productCategoryId":24,"productId":13,"productName":"消毒湿纸巾","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200304/商品图片1.jpg","productPrice":19.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":0,"storeId":1,"storeName":"","type":1}],"orderSn":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200304/商品图片1.jpg","orderType":5,"payAmount":0.00,"payCode":"","payType":5,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":19.00,"useIntegration":0},{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584675263000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":9,"goodsName":"12","groupId":0,"growth":0,"historyList":[],"id":3913,"integration":0,"integrationAmount":0,"isComment":0,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5677,"integrationAmount":0,"orderId":3913,"orderSn":"","productAttr":"","productBrand":"","productCategoryId":25,"productId":9,"productName":"12","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200115/dan.jpg","productPrice":11.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":0,"storeId":1,"storeName":"","type":1}],"orderSn":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200115/dan.jpg","orderType":5,"payAmount":0.00,"payCode":"","payType":5,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"","receiverDetailAddress":"","receiverId":0,"receiverName":"","receiverPhone":"","receiverPostCode":"","receiverProvince":"","receiverRegion":"","schoolId":0,"sourceType":1,"status":2,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":0,"totalAmount":11.00,"useIntegration":0}],"size":10,"total":68}}
```

### 14、订单详情

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/detail?id=3922
```

#### 示例：


#### 请求方式：
```
get
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|id:       |Y       |int   | 订单编号 |


#### 返回示例：

```javascript
{"code":200,"data":{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":640,"commentTime":null,"confirmStatus":0,"couponAmount":0.00,"couponId":0,"createTime":1584774841000,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0.00,"freightAmount":0,"goodsId":0,"goodsName":"","groupId":148,"growth":0,"historyList":[],"id":3922,"integration":0,"integrationAmount":0.00,"isComment":1,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5691,"integrationAmount":0,"orderId":3922,"orderSn":"15847748406380101297","productAttr":"","productBrand":"欧思路","productCategoryId":58,"productId":93,"productName":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","productPic":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","productPrice":509.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"","type":1}],"orderSn":"15847748406380101297","orderType":2,"payAmount":11.00,"payCode":"","payType":3,"paymentTime":1584775364000,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"市辖区","receiverDetailAddress":"分身乏术的发生地","receiverId":166,"receiverName":"时尚大方","receiverPhone":"13811090913","receiverPostCode":"","receiverProvince":"北京市","receiverRegion":"西城区","schoolId":0,"sourceType":1,"status":2,"storeId":1,"storeName":"","supplyId":0,"taxCode":"","taxContent":"商品详情","taxTitle":"","taxType":1,"totalAmount":509.00,"useIntegration":0}}
```

### 15、商品浏览记录

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/viewList
```

#### 示例：


#### 请求方式：
```
get
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|


#### 返回示例：

```javascript
{"code":200,"data":{"result":[{"albumPics":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20191007/balancepay.png,http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20191007/coupon-element.png","areaId":0,"areaName":"","brandId":61,"brandName":"欧思路","createTime":1578262903000,"deleteStatus":0,"description":"品牌： 唯唯（WEWE）\n                                \n                            \n                        \n                                                \n                                商品名称：WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)\n    商品编号：47871067624\n                      店铺： WEWE旗舰店\n                     商品毛重：500.00g\n                货号：F93741\n                                版型：修身型\n                  领型：圆领\n                  流行元素：不规则\n                  图案文化：无图案\n                  材质：棉\n                  袖型：常规袖\n                  适用年龄：25-29周岁\n                  风格：休闲风，简约，百搭\n                  衣长：常规款\n                  组合形式：单件\n                  袖长：短袖\n                  上市时间：2019年夏季\n                                          \n                                                \n                            更多参数>>","detailDesc":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","detailHtml":"<p><img class=\"\" style=\"width: auto; height: auto; max-width: 100%;\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t11401/113/3026363602/145812/42de0009/5cdd3141N447438be.jpg\" /><img class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t10303/198/2978835002/18844/86c46ff5/5cdd3141N7ab352ec.jpg\" /></p>","detailMobileHtml":"","detailTitle":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":8,"giftPoint":0,"hit":1075,"id":92,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","newStatus":1,"note":"","originalPrice":139.00,"pic":"https://img14.360buyimg.com/n7/jfs/t29722/14/1386865551/93819/2f07f87d/5cdd3184N4b991c77.jpg","previewStatus":0,"price":139.00,"productAttributeCategoryId":387,"productCategoryId":44,"productCategoryName":"连衣裙","productSn":"X1564467705309","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":101,"schoolId":2,"schoolName":"","serviceIds":"","sort":0,"stock":0,"storeId":2,"storeName":"北京豪车专卖","subTitle":"WEWE唯唯2019夏季新款短袖T恤女圆领修身商场同款纯棉上衣 绿色 M(165)","supplyId":0,"timeSecound":null,"transfee":10,"type":1,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","areaId":0,"areaName":"","brandId":61,"brandName":"欧思路","createTime":1564438905000,"deleteStatus":0,"description":"品牌： FILA\n                                \n                            \n                        \n                                                \n                                商品名称：FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL\n    商品编号：47740462787\n                      店铺： FILA斐乐服饰旗舰店\n                     商品毛重：0.8kg\n                货号：F11W927122F\n                                版型：其它\n                  领型：POLO领\n                  流行元素：印花\n                  衣长：常规款\n                  组合形式：其它\n                  材质：聚酰胺纤维(锦纶)\n                  袖型：其它\n                  适用年龄：18-24周岁\n                  图案文化：其它\n                  图案：字母，品牌LOGO\n                  风格：简约，运动风，百搭\n                  袖长：短袖\n                  上市时间：2019年夏季\n                                          \n                                                \n                            更多参数>>","detailDesc":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","detailHtml":"<img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t11017/216/2925553326/397804/3d38f939/5cdccd98Na29407ef.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29986/337/1318891484/157211/56dfcace/5cdccd97N97f85c06.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30256/79/1323110900/91513/f7c0f34f/5cdccd97Nb455f92d.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t10678/212/2914717723/180951/9511024d/5cdccd98Na8290e1d.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30508/334/1302925282/97704/7c6911be/5cdccd98Nac4cf4ed.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/53558/7/509/110852/5cdccd98E143e2af5/e50b2ffb70c1c527.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/38595/36/6981/145235/5cdccda6E47de4e8b/5e94b63d7e6e7355.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t10006/244/2899222461/139756/f0165ccb/5cdccd99N456bd5f6.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30124/323/1325247737/101813/7a445a57/5cdccd99N83150f28.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29317/264/1313145537/113858/b69a0f48/5cdccd99Nd435008a.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t28198/249/1314487457/144606/3c71ff72/5cdccd9aN6a4e0dc0.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29806/292/1338532051/57463/a14b9dcd/5cdccd9aN9f7da091.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29794/293/1282500912/33889/f41b4236/5cdccd9aNdc528b6a.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t29743/313/1272516579/266496/475ec2fe/5cdccd9bN6fddb618.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t11395/145/2924258149/432972/e119f9c7/5cdccd9cNc4612362.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t28006/215/1344690766/135392/7bf6e810/5cdccd9bNbac05c82.jpg\"><img style=\"width:auto;height:auto;max-width:100%;\" class=\"\" src=\"//img30.360buyimg.com/popWaterMark/jfs/t30295/331/1297389421/311878/c85e66d8/5cdccd9cNccb68d6e.jpg\"><br>","detailMobileHtml":"","detailTitle":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":3,"giftPoint":0,"hit":156,"id":93,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","newStatus":1,"note":"","originalPrice":680.00,"pic":"https://img12.360buyimg.com/n7/jfs/t1/78991/14/5561/138157/5d3af988Efb04236e/9b122e74a95193d0.jpg","previewStatus":0,"price":509.00,"productAttributeCategoryId":387,"productCategoryId":58,"productCategoryName":"连衣裙","productSn":"X1564467705416","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":77,"schoolId":2,"schoolName":"","serviceIds":"","sort":0,"stock":38,"storeId":1,"storeName":"北京皮包专卖","subTitle":"FILA 斐乐女装Red Line系列官方 女子短袖POLO 2019夏季新品优雅弹力短袖衫商场同款 RD宝蓝-NV 180/96A/XXL","supplyId":0,"timeSecound":null,"transfee":10,"type":1,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"http://yanxuan.nosdn.127.net/07a47d73e2eb53b1a7939219a4e63618.png","areaId":0,"areaName":"","brandId":1001000,"brandName":"","createTime":1578262903000,"deleteStatus":0,"description":"方便携带，环保卫生","detailDesc":"方便携带，环保卫生","detailHtml":"<p><img src=\"http://yanxuan.nosdn.127.net/0439a458150f17b3c54e1898b4ddf71f.jpg\" _src=\"http://yanxuan.nosdn.127.net/0439a458150f17b3c54e1898b4ddf71f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/759a3d2dc0fea138932feb0b03def7ab.jpg\" _src=\"http://yanxuan.nosdn.127.net/759a3d2dc0fea138932feb0b03def7ab.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/4fbc1e3aaaec589986fe8c460006b408.jpg\" _src=\"http://yanxuan.nosdn.127.net/4fbc1e3aaaec589986fe8c460006b408.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/29ff62dcfcc7df4e69bdbfb6830d3729.jpg\" _src=\"http://yanxuan.nosdn.127.net/29ff62dcfcc7df4e69bdbfb6830d3729.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8413e338d3d16087645d817aac894bc3.jpg\" _src=\"http://yanxuan.nosdn.127.net/8413e338d3d16087645d817aac894bc3.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8746c36ec37fe13c0124c13162a903cd.jpg\" _src=\"http://yanxuan.nosdn.127.net/8746c36ec37fe13c0124c13162a903cd.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/5ba42ac14b311acbcc1b1ab3f9c0c239.jpg\" _src=\"http://yanxuan.nosdn.127.net/5ba42ac14b311acbcc1b1ab3f9c0c239.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/381576780a6acb18d4b9720caff81d51.jpg\" _src=\"http://yanxuan.nosdn.127.net/381576780a6acb18d4b9720caff81d51.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/ec97befffd24b79f095b698d5da500bc.jpg\" _src=\"http://yanxuan.nosdn.127.net/ec97befffd24b79f095b698d5da500bc.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1a95dcc67a1ddd5c653aa38d085ca289.jpg\" _src=\"http://yanxuan.nosdn.127.net/1a95dcc67a1ddd5c653aa38d085ca289.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/148b5c0861ce138fed9ce344be4f3735.jpg\" _src=\"http://yanxuan.nosdn.127.net/148b5c0861ce138fed9ce344be4f3735.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/853b3e416ac990886f252b492a18d765.jpg\" _src=\"http://yanxuan.nosdn.127.net/853b3e416ac990886f252b492a18d765.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/bc62219c8eded9aa632bf130fd816844.jpg\" _src=\"http://yanxuan.nosdn.127.net/bc62219c8eded9aa632bf130fd816844.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/ad28c490b2c43b7ca1d9da3fc3631465.jpg\" _src=\"http://yanxuan.nosdn.127.net/ad28c490b2c43b7ca1d9da3fc3631465.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/d371fd88d9312493cfd3be36a322d92b.jpg\" _src=\"http://yanxuan.nosdn.127.net/d371fd88d9312493cfd3be36a322d92b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f40e9c97a58c8c93335e5374b6e6d70c.jpg\" _src=\"http://yanxuan.nosdn.127.net/f40e9c97a58c8c93335e5374b6e6d70c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/74092ef98970e14ad640949e923a40e3.jpg\" _src=\"http://yanxuan.nosdn.127.net/74092ef98970e14ad640949e923a40e3.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/ea98fada7b079deb466e5a787d241dae.jpg\" _src=\"http://yanxuan.nosdn.127.net/ea98fada7b079deb466e5a787d241dae.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f69dd49e4d45cc27ccc9e5a6e765fbfd.jpg\" _src=\"http://yanxuan.nosdn.127.net/f69dd49e4d45cc27ccc9e5a6e765fbfd.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/77137b44be1a680cae7b1c25e37e386d.jpg\" _src=\"http://yanxuan.nosdn.127.net/77137b44be1a680cae7b1c25e37e386d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/ce607d0b53a481683a78520c8ab6ccae.jpg\" _src=\"http://yanxuan.nosdn.127.net/ce607d0b53a481683a78520c8ab6ccae.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/011c0ff03dc6d9c4abbfb717d1757bf4.jpg\" _src=\"http://yanxuan.nosdn.127.net/011c0ff03dc6d9c4abbfb717d1757bf4.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/2290de54b4dfca00cb57fc879b5bbbab.jpg\" _src=\"http://yanxuan.nosdn.127.net/2290de54b4dfca00cb57fc879b5bbbab.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c08cc448c81b6256db91211579dfb738.jpg\" _src=\"http://yanxuan.nosdn.127.net/c08cc448c81b6256db91211579dfb738.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/9ede1bd346840f490cadbb4e2091e236.jpg\" _src=\"http://yanxuan.nosdn.127.net/9ede1bd346840f490cadbb4e2091e236.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/0243642fd350cb2b7873817f052c37e6.jpg\" _src=\"http://yanxuan.nosdn.127.net/0243642fd350cb2b7873817f052c37e6.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/7a4acab695c5596de04e3642ac2bd711.jpg\" _src=\"http://yanxuan.nosdn.127.net/7a4acab695c5596de04e3642ac2bd711.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/fd5e8c7e865dcf50b662c6f2224deb05.jpg\" _src=\"http://yanxuan.nosdn.127.net/fd5e8c7e865dcf50b662c6f2224deb05.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b132996c99b3a02443692ececf52f5fd.jpg\" _src=\"http://yanxuan.nosdn.127.net/b132996c99b3a02443692ececf52f5fd.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/175b24fab11d1f543e10ccc2f2ac4eb6.jpg\" _src=\"http://yanxuan.nosdn.127.net/175b24fab11d1f543e10ccc2f2ac4eb6.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/e0a51eb838b01dac5bec6057e1e59428.jpg\" _src=\"http://yanxuan.nosdn.127.net/e0a51eb838b01dac5bec6057e1e59428.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/cda60b57d1f0e78cd3921bc4e3d8ccf2.jpg\" _src=\"http://yanxuan.nosdn.127.net/cda60b57d1f0e78cd3921bc4e3d8ccf2.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1437057917d6deb2e453e711bcbf40ee.jpg\" _src=\"http://yanxuan.nosdn.127.net/1437057917d6deb2e453e711bcbf40ee.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/121b6dc31268b1a82c632fcb634f9a64.jpg\" _src=\"http://yanxuan.nosdn.127.net/121b6dc31268b1a82c632fcb634f9a64.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/0923c11aec66cec291f8a142b8391e01.jpg\" _src=\"http://yanxuan.nosdn.127.net/0923c11aec66cec291f8a142b8391e01.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/736a20d6584ffaacc39bd62c700b68ea.jpg\" _src=\"http://yanxuan.nosdn.127.net/736a20d6584ffaacc39bd62c700b68ea.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/7643379507709dba5947571eb18fc7b1.jpg\" _src=\"http://yanxuan.nosdn.127.net/7643379507709dba5947571eb18fc7b1.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/0217adfd4f3a62963f18f841fa945e5f.jpg\" _src=\"http://yanxuan.nosdn.127.net/0217adfd4f3a62963f18f841fa945e5f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c4055e755e250b2c84a0548e031de7f4.jpg\" _src=\"http://yanxuan.nosdn.127.net/c4055e755e250b2c84a0548e031de7f4.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/9f3c65c42ffac403bf55ff33454849f7.jpg\" _src=\"http://yanxuan.nosdn.127.net/9f3c65c42ffac403bf55ff33454849f7.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/349b23e34055e38d2311b82cc42b9402.jpg\" _src=\"http://yanxuan.nosdn.127.net/349b23e34055e38d2311b82cc42b9402.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/2f9a830bb03074182f19931ec6aaf1c0.jpg\" _src=\"http://yanxuan.nosdn.127.net/2f9a830bb03074182f19931ec6aaf1c0.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/32bc1a4cb7aa944ac67856fcb6e5c73f.jpg\" _src=\"http://yanxuan.nosdn.127.net/32bc1a4cb7aa944ac67856fcb6e5c73f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/381b80fabf330062ab9ffcde4ea025be.jpg\" _src=\"http://yanxuan.nosdn.127.net/381b80fabf330062ab9ffcde4ea025be.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1f987d33585b22286115fbb684edbbf7.jpg\" _src=\"http://yanxuan.nosdn.127.net/1f987d33585b22286115fbb684edbbf7.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/89e8c521f234dfb1c0217e553826b4f9.jpg\" _src=\"http://yanxuan.nosdn.127.net/89e8c521f234dfb1c0217e553826b4f9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/de5822513f7a21e52a23e65709311072.jpg\" _src=\"http://yanxuan.nosdn.127.net/de5822513f7a21e52a23e65709311072.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/4437746b0494178f77250f55937d7080.jpg\" _src=\"http://yanxuan.nosdn.127.net/4437746b0494178f77250f55937d7080.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b46621bf43e028b86ae69fb68019318b.jpg\" _src=\"http://yanxuan.nosdn.127.net/b46621bf43e028b86ae69fb68019318b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/208bf739edd3b19c8fba3f2cb47792f5.jpg\" _src=\"http://yanxuan.nosdn.127.net/208bf739edd3b19c8fba3f2cb47792f5.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/e63ee61d221db9cbe9978da2394ee2b2.jpg\" _src=\"http://yanxuan.nosdn.127.net/e63ee61d221db9cbe9978da2394ee2b2.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3719e6b0d3e8cd37b9c9e3f7c84fbc2c.jpg\" _src=\"http://yanxuan.nosdn.127.net/3719e6b0d3e8cd37b9c9e3f7c84fbc2c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1e5613aef6325482b310cb2bbe4a23e8.jpg\" _src=\"http://yanxuan.nosdn.127.net/1e5613aef6325482b310cb2bbe4a23e8.jpg\" style=\"\"/></p><p><br/></p>","detailMobileHtml":"","detailTitle":"方便携带，环保卫生","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":1017001,"giftPoint":1017000,"hit":131,"id":661,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"便携多功能宠物拾便器","newStatus":1,"note":"","originalPrice":39.00,"pic":"http://yanxuan.nosdn.127.net/07a47d73e2eb53b1a7939219a4e63618.png","previewStatus":0,"price":59.00,"productAttributeCategoryId":1017000,"productCategoryId":604,"productCategoryName":"","productSn":"1071005","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1017012,"schoolId":0,"schoolName":"","serviceIds":"","sort":17,"stock":1071001,"storeId":2,"storeName":"北京豪车专卖","subTitle":"方便携带，环保卫生","supplyId":0,"timeSecound":null,"transfee":10,"type":1,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"http://yanxuan.nosdn.127.net/0e9d5954d7dc2477d9c46b730e05ab42.png","areaId":0,"areaName":"","brandId":1001020,"brandName":"","createTime":1578262903000,"deleteStatus":0,"description":"清素色织，搭配水洗棉旧色的温柔","detailDesc":"清素色织，搭配水洗棉旧色的温柔","detailHtml":"<p><img src=\"http://yanxuan.nosdn.127.net/f8a74bd8e87987849bf2505c08ac69e2.jpg\" _src=\"http://yanxuan.nosdn.127.net/f8a74bd8e87987849bf2505c08ac69e2.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/be7ae107fc4b5085241507ed711254f8.jpg\" _src=\"http://yanxuan.nosdn.127.net/be7ae107fc4b5085241507ed711254f8.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/143c402e82c2d99c3bc7cc51d064a62b.jpg\" _src=\"http://yanxuan.nosdn.127.net/143c402e82c2d99c3bc7cc51d064a62b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/52f5b211f5da083da2bc9b5237c40b3b.jpg\" _src=\"http://yanxuan.nosdn.127.net/52f5b211f5da083da2bc9b5237c40b3b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/5af00c6f7dc0d798dffe2cdc2da0f870.jpg\" _src=\"http://yanxuan.nosdn.127.net/5af00c6f7dc0d798dffe2cdc2da0f870.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/750bbc1c2207159b58a6de350060f5c9.jpg\" _src=\"http://yanxuan.nosdn.127.net/750bbc1c2207159b58a6de350060f5c9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/d55dd54de50ff03b199261f3e021f388.jpg\" _src=\"http://yanxuan.nosdn.127.net/d55dd54de50ff03b199261f3e021f388.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b659703ddf8e35bb89ee5ff36e84b4f8.jpg\" _src=\"http://yanxuan.nosdn.127.net/b659703ddf8e35bb89ee5ff36e84b4f8.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/573f30d45c3eea65f29a6497ac2b9a98.jpg\" _src=\"http://yanxuan.nosdn.127.net/573f30d45c3eea65f29a6497ac2b9a98.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f7d58eb147f2d8bedfc88ba08b544d7c.jpg\" _src=\"http://yanxuan.nosdn.127.net/f7d58eb147f2d8bedfc88ba08b544d7c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/ec89200168dd4a703f7b89ec2be0b8a4.jpg\" _src=\"http://yanxuan.nosdn.127.net/ec89200168dd4a703f7b89ec2be0b8a4.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3d123c7d7b79ef8cd2703976e8bf1dbc.jpg\" _src=\"http://yanxuan.nosdn.127.net/3d123c7d7b79ef8cd2703976e8bf1dbc.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/2797ba305c10b0e5fd6eafb384101402.jpg\" _src=\"http://yanxuan.nosdn.127.net/2797ba305c10b0e5fd6eafb384101402.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/abcfbdafabe855e77598860b426fa5e9.jpg\" _src=\"http://yanxuan.nosdn.127.net/abcfbdafabe855e77598860b426fa5e9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8d6ff672dae187d85d199ee1b1b62f9b.jpg\" _src=\"http://yanxuan.nosdn.127.net/8d6ff672dae187d85d199ee1b1b62f9b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3e4424539e51dca9d000c6b67bea1f8b.jpg\" _src=\"http://yanxuan.nosdn.127.net/3e4424539e51dca9d000c6b67bea1f8b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/942da4a7c05d2a25be7a26decc40f53a.jpg\" _src=\"http://yanxuan.nosdn.127.net/942da4a7c05d2a25be7a26decc40f53a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/2c872c106b7da7d7b1167a9554c92aa1.jpg\" _src=\"http://yanxuan.nosdn.127.net/2c872c106b7da7d7b1167a9554c92aa1.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/82607c78aedacd534a2e0f1b38e2e3e7.jpg\" _src=\"http://yanxuan.nosdn.127.net/82607c78aedacd534a2e0f1b38e2e3e7.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/07e4e5ac1501706c8666eff4a703b5fb.jpg\" _src=\"http://yanxuan.nosdn.127.net/07e4e5ac1501706c8666eff4a703b5fb.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/a56805f63dfe98583e6b58eaa9dd595a.jpg\" _src=\"http://yanxuan.nosdn.127.net/a56805f63dfe98583e6b58eaa9dd595a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/ecdd28de374db2de0c0c2f0f50d0cd26.jpg\" _src=\"http://yanxuan.nosdn.127.net/ecdd28de374db2de0c0c2f0f50d0cd26.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/69fcb2566dea15f16a182b7e4e05893f.jpg\" _src=\"http://yanxuan.nosdn.127.net/69fcb2566dea15f16a182b7e4e05893f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/bc3c5b3a6a14c8307e57842bf737601b.jpg\" _src=\"http://yanxuan.nosdn.127.net/bc3c5b3a6a14c8307e57842bf737601b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/53d9c980cc924790bd7983bd80b0221a.jpg\" _src=\"http://yanxuan.nosdn.127.net/53d9c980cc924790bd7983bd80b0221a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/787bdee826a5112c3e2bd4b9c15717e8.jpg\" _src=\"http://yanxuan.nosdn.127.net/787bdee826a5112c3e2bd4b9c15717e8.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/44dc985f99b3a95f255624a4d97857ca.jpg\" _src=\"http://yanxuan.nosdn.127.net/44dc985f99b3a95f255624a4d97857ca.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/bf117d9aef1ef7ab10d2ef040d9444c6.jpg\" _src=\"http://yanxuan.nosdn.127.net/bf117d9aef1ef7ab10d2ef040d9444c6.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/07044f1a6c11d8d15e410a01482fb39a.jpg\" _src=\"http://yanxuan.nosdn.127.net/07044f1a6c11d8d15e410a01482fb39a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/5ca8ea48405cb43c901181a684c623dc.jpg\" _src=\"http://yanxuan.nosdn.127.net/5ca8ea48405cb43c901181a684c623dc.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1b4111628661d4216c8d0fc057e6d81e.jpg\" _src=\"http://yanxuan.nosdn.127.net/1b4111628661d4216c8d0fc057e6d81e.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/4adb10168dbfcee6f49460b2478de249.jpg\" _src=\"http://yanxuan.nosdn.127.net/4adb10168dbfcee6f49460b2478de249.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/e798dd4abe3a833a4d0066453955ec1c.jpg\" _src=\"http://yanxuan.nosdn.127.net/e798dd4abe3a833a4d0066453955ec1c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/528abeae19cf1b5af2f49be4e142a2da.jpg\" _src=\"http://yanxuan.nosdn.127.net/528abeae19cf1b5af2f49be4e142a2da.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/2d3d5ae118bcacdfda08eb182a03c15a.jpg\" _src=\"http://yanxuan.nosdn.127.net/2d3d5ae118bcacdfda08eb182a03c15a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3323a393743f839e12586418a0711123.jpg\" _src=\"http://yanxuan.nosdn.127.net/3323a393743f839e12586418a0711123.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8ac29c5a1569d9fb37420f2eae835b91.jpg\" _src=\"http://yanxuan.nosdn.127.net/8ac29c5a1569d9fb37420f2eae835b91.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/89ef4f7ec1c86f13d328c23c0429892a.jpg\" _src=\"http://yanxuan.nosdn.127.net/89ef4f7ec1c86f13d328c23c0429892a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8b005b97dbffa333e49ff484abbcff04.jpg\" _src=\"http://yanxuan.nosdn.127.net/8b005b97dbffa333e49ff484abbcff04.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/afa56b9370cdc27f27f8f951108cea4c.jpg\" _src=\"http://yanxuan.nosdn.127.net/afa56b9370cdc27f27f8f951108cea4c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/5daae71eb699e7417e695472e1142678.jpg\" _src=\"http://yanxuan.nosdn.127.net/5daae71eb699e7417e695472e1142678.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3dbb71ba9fff82849a959deb8f5b7946.jpg\" _src=\"http://yanxuan.nosdn.127.net/3dbb71ba9fff82849a959deb8f5b7946.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/0e024ba974bad906db9e2a28d42293a7.jpg\" _src=\"http://yanxuan.nosdn.127.net/0e024ba974bad906db9e2a28d42293a7.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/87e2d46daab9e1dd3e112c15692c66ba.jpg\" _src=\"http://yanxuan.nosdn.127.net/87e2d46daab9e1dd3e112c15692c66ba.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/342bc9bb1cbf81d1bc7b33f77fa915ff.jpg\" _src=\"http://yanxuan.nosdn.127.net/342bc9bb1cbf81d1bc7b33f77fa915ff.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f54d685cb3c111beff7ef58bb16252d6.jpg\" _src=\"http://yanxuan.nosdn.127.net/f54d685cb3c111beff7ef58bb16252d6.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b7c4c896daa0ac2ff5f89d5b738fc007.jpg\" _src=\"http://yanxuan.nosdn.127.net/b7c4c896daa0ac2ff5f89d5b738fc007.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/839da4fb5874271d54f94c60ad4d7387.jpg\" _src=\"http://yanxuan.nosdn.127.net/839da4fb5874271d54f94c60ad4d7387.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/7147d5acd91c330479762e51cf4257f6.jpg\" _src=\"http://yanxuan.nosdn.127.net/7147d5acd91c330479762e51cf4257f6.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/2a9ca13fd9189e60df2f4a21783a3795.jpg\" _src=\"http://yanxuan.nosdn.127.net/2a9ca13fd9189e60df2f4a21783a3795.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/09fa8e6e95cc716138549e6b244a7924.jpg\" _src=\"http://yanxuan.nosdn.127.net/09fa8e6e95cc716138549e6b244a7924.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/772e18352ad2c156ee190f068726cd8c.jpg\" _src=\"http://yanxuan.nosdn.127.net/772e18352ad2c156ee190f068726cd8c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/92c0a1e57d429d19fb7f4e678fa7fae1.jpg\" _src=\"http://yanxuan.nosdn.127.net/92c0a1e57d429d19fb7f4e678fa7fae1.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/88ddae38e8b363acdf79bfbe0547cc50.jpg\" _src=\"http://yanxuan.nosdn.127.net/88ddae38e8b363acdf79bfbe0547cc50.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3c4c683112a57543d4e028754be53e0d.jpg\" _src=\"http://yanxuan.nosdn.127.net/3c4c683112a57543d4e028754be53e0d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f476dbaf8a8cae1e8a311edb77d4ea78.jpg\" _src=\"http://yanxuan.nosdn.127.net/f476dbaf8a8cae1e8a311edb77d4ea78.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b7493e6d2980dc116654bdb3b24c1a3d.jpg\" _src=\"http://yanxuan.nosdn.127.net/b7493e6d2980dc116654bdb3b24c1a3d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/57e84b0e4b979782b0c3a4bce908d797.jpg\" _src=\"http://yanxuan.nosdn.127.net/57e84b0e4b979782b0c3a4bce908d797.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/a8eb47f38c324fd320c74bf0aa3ebdf1.jpg\" _src=\"http://yanxuan.nosdn.127.net/a8eb47f38c324fd320c74bf0aa3ebdf1.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/622949bf7e667ea54eaf0861ac31c842.jpg\" _src=\"http://yanxuan.nosdn.127.net/622949bf7e667ea54eaf0861ac31c842.jpg\" style=\"\"/></p><p><br/></p>","detailMobileHtml":"","detailTitle":"清素色织，搭配水洗棉旧色的温柔","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":1008002,"giftPoint":1008002,"hit":80,"id":664,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"色织水洗棉绣花抱枕套","newStatus":1,"note":"","originalPrice":49.00,"pic":"http://yanxuan.nosdn.127.net/0e9d5954d7dc2477d9c46b730e05ab42.png","previewStatus":0,"price":69.00,"productAttributeCategoryId":252,"productCategoryId":607,"productCategoryName":"","productSn":"1072001","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1008010,"schoolId":0,"schoolName":"","serviceIds":"","sort":7,"stock":1071993,"storeId":3,"storeName":"北京名衣专卖","subTitle":"清素色织，搭配水洗棉旧色的温柔","supplyId":0,"timeSecound":null,"transfee":10,"type":1,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"http://yanxuan.nosdn.127.net/be64df0a04ade4cfd75bf7d4e8509ecc.png","areaId":0,"areaName":"","brandId":1001000,"brandName":"","createTime":1577754304000,"deleteStatus":0,"description":"精梳长绒棉,亲肤舒适","detailDesc":"精梳长绒棉,亲肤舒适","detailHtml":"<p><img src=\"http://yanxuan.nosdn.127.net/43a852a45f4a56d3392f1ddea87eb8eb.jpg\" _src=\"http://yanxuan.nosdn.127.net/43a852a45f4a56d3392f1ddea87eb8eb.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3c9c1934bce0b69ec220ff981ec738fe.jpg\" _src=\"http://yanxuan.nosdn.127.net/3c9c1934bce0b69ec220ff981ec738fe.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/56aa71cb1c36013a6d4e7f884af010fa.jpg\" _src=\"http://yanxuan.nosdn.127.net/56aa71cb1c36013a6d4e7f884af010fa.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f8aab18bfc5b668651064d29cfcd4fcf.jpg\" _src=\"http://yanxuan.nosdn.127.net/f8aab18bfc5b668651064d29cfcd4fcf.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/caf48b811fb86d6a4ee47a849afbc3f5.jpg\" _src=\"http://yanxuan.nosdn.127.net/caf48b811fb86d6a4ee47a849afbc3f5.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1a23bed5db163248ef7d5f696bde6923.jpg\" _src=\"http://yanxuan.nosdn.127.net/1a23bed5db163248ef7d5f696bde6923.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/6d2c20eff38f2b10631fdaf45363642f.jpg\" _src=\"http://yanxuan.nosdn.127.net/6d2c20eff38f2b10631fdaf45363642f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/902eb2eefa3cfb7104755cf8fc7b7d33.jpg\" _src=\"http://yanxuan.nosdn.127.net/902eb2eefa3cfb7104755cf8fc7b7d33.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/4e1a836f6969eb1694d62ce99ea70e5f.jpg\" _src=\"http://yanxuan.nosdn.127.net/4e1a836f6969eb1694d62ce99ea70e5f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1413c155daac6279deb6400671798da6.jpg\" _src=\"http://yanxuan.nosdn.127.net/1413c155daac6279deb6400671798da6.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/9ecdabedeeae302bfeed80572ca51490.jpg\" _src=\"http://yanxuan.nosdn.127.net/9ecdabedeeae302bfeed80572ca51490.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c6ff45ee262e8d486f17134a9dbc0a3a.jpg\" _src=\"http://yanxuan.nosdn.127.net/c6ff45ee262e8d486f17134a9dbc0a3a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/be082ed4addbd787bb44fae512fb55fe.jpg\" _src=\"http://yanxuan.nosdn.127.net/be082ed4addbd787bb44fae512fb55fe.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/bd32aac49141d87b7a983fe4646a7a75.jpg\" _src=\"http://yanxuan.nosdn.127.net/bd32aac49141d87b7a983fe4646a7a75.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/62fd7c2f44ceccc4ee33bdde2ee998f4.jpg\" _src=\"http://yanxuan.nosdn.127.net/62fd7c2f44ceccc4ee33bdde2ee998f4.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/91e4e6bc20051706e74a6f65684e97cc.jpg\" _src=\"http://yanxuan.nosdn.127.net/91e4e6bc20051706e74a6f65684e97cc.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f47723ba3ffdf2ee107b497c560aef88.jpg\" _src=\"http://yanxuan.nosdn.127.net/f47723ba3ffdf2ee107b497c560aef88.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/95836389e457d1a2c6810e104c907f93.jpg\" _src=\"http://yanxuan.nosdn.127.net/95836389e457d1a2c6810e104c907f93.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/871322a2bd8b44226d597af3f0c52249.jpg\" _src=\"http://yanxuan.nosdn.127.net/871322a2bd8b44226d597af3f0c52249.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/891195e7bb58a6782f0f1da15ff34176.jpg\" _src=\"http://yanxuan.nosdn.127.net/891195e7bb58a6782f0f1da15ff34176.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c232a44890a7e2c1312c214ee4aaead5.jpg\" _src=\"http://yanxuan.nosdn.127.net/c232a44890a7e2c1312c214ee4aaead5.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c7ee91d512fc23f83a9cde92bcc9663d.jpg\" _src=\"http://yanxuan.nosdn.127.net/c7ee91d512fc23f83a9cde92bcc9663d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1649cd095c5299283d9d307c337f36ec.jpg\" _src=\"http://yanxuan.nosdn.127.net/1649cd095c5299283d9d307c337f36ec.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8f0e3f5422a1e605d2cb0c66d03389e4.jpg\" _src=\"http://yanxuan.nosdn.127.net/8f0e3f5422a1e605d2cb0c66d03389e4.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/e1e90d6bc0b48db9859944113ef36a49.jpg\" _src=\"http://yanxuan.nosdn.127.net/e1e90d6bc0b48db9859944113ef36a49.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/e12296776ba8d5b6c0c18fac593772a2.jpg\" _src=\"http://yanxuan.nosdn.127.net/e12296776ba8d5b6c0c18fac593772a2.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/eab9452b80d2c4e4cc68e309c7de50a8.jpg\" _src=\"http://yanxuan.nosdn.127.net/eab9452b80d2c4e4cc68e309c7de50a8.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3ac44a0026be72d166cd4c00824d600b.jpg\" _src=\"http://yanxuan.nosdn.127.net/3ac44a0026be72d166cd4c00824d600b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f98ab8b23724f26fd3a2db9e539e2e4d.jpg\" _src=\"http://yanxuan.nosdn.127.net/f98ab8b23724f26fd3a2db9e539e2e4d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/579e35893568aa03920e05eb16d4009d.jpg\" _src=\"http://yanxuan.nosdn.127.net/579e35893568aa03920e05eb16d4009d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b8921d7ddfe11cb12db4f05a6f120632.jpg\" _src=\"http://yanxuan.nosdn.127.net/b8921d7ddfe11cb12db4f05a6f120632.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8398cca3432ace05b02234ff47cbb25f.jpg\" _src=\"http://yanxuan.nosdn.127.net/8398cca3432ace05b02234ff47cbb25f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1296cacff7a32d6fea465ff1859e52e2.jpg\" _src=\"http://yanxuan.nosdn.127.net/1296cacff7a32d6fea465ff1859e52e2.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/0ab0a51959ae1ac64818d568e64dc6c9.jpg\" _src=\"http://yanxuan.nosdn.127.net/0ab0a51959ae1ac64818d568e64dc6c9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1f0d167aa533ecf32abaf3521b744402.jpg\" _src=\"http://yanxuan.nosdn.127.net/1f0d167aa533ecf32abaf3521b744402.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/7bdb1e96d56ca8542a130a4ddbba7ac9.jpg\" _src=\"http://yanxuan.nosdn.127.net/7bdb1e96d56ca8542a130a4ddbba7ac9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/66c8b538d430b7e4a9772aa9375d284f.jpg\" _src=\"http://yanxuan.nosdn.127.net/66c8b538d430b7e4a9772aa9375d284f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3410feb14590c53cd31c24c69a875732.jpg\" _src=\"http://yanxuan.nosdn.127.net/3410feb14590c53cd31c24c69a875732.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/fc3fa3cbe0657feaf771a9703635fbc7.jpg\" _src=\"http://yanxuan.nosdn.127.net/fc3fa3cbe0657feaf771a9703635fbc7.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1b44910a98624d648d266ef44922fb7d.jpg\" _src=\"http://yanxuan.nosdn.127.net/1b44910a98624d648d266ef44922fb7d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8eddee7e749deb33c8ca8c28f56f2568.jpg\" _src=\"http://yanxuan.nosdn.127.net/8eddee7e749deb33c8ca8c28f56f2568.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/adfe104a5b1132e734647dc0c950e8af.jpg\" _src=\"http://yanxuan.nosdn.127.net/adfe104a5b1132e734647dc0c950e8af.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/be8ef89cf818a4aeba8d0ddf413cbcf9.jpg\" _src=\"http://yanxuan.nosdn.127.net/be8ef89cf818a4aeba8d0ddf413cbcf9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3829f4ed5b477680f169d390c8220c15.jpg\" _src=\"http://yanxuan.nosdn.127.net/3829f4ed5b477680f169d390c8220c15.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/126768f1680c147b7e11f853074d5289.jpg\" _src=\"http://yanxuan.nosdn.127.net/126768f1680c147b7e11f853074d5289.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b1321f2e495b39212ff2b50c2d0a84b1.jpg\" _src=\"http://yanxuan.nosdn.127.net/b1321f2e495b39212ff2b50c2d0a84b1.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/125bfac27b6596ebe50535ceb87ced60.jpg\" _src=\"http://yanxuan.nosdn.127.net/125bfac27b6596ebe50535ceb87ced60.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8a93ed3b3caf85e8fd26ad79d771fb69.jpg\" _src=\"http://yanxuan.nosdn.127.net/8a93ed3b3caf85e8fd26ad79d771fb69.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/efbe7ce1c9de4da97ca143a3566d4c48.jpg\" _src=\"http://yanxuan.nosdn.127.net/efbe7ce1c9de4da97ca143a3566d4c48.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/943384ff66cec540eb1c14c6b6a3ce2c.jpg\" _src=\"http://yanxuan.nosdn.127.net/943384ff66cec540eb1c14c6b6a3ce2c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/06ade0cd8bfe76fc980199281bf0073d.jpg\" _src=\"http://yanxuan.nosdn.127.net/06ade0cd8bfe76fc980199281bf0073d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/075c4bd0119df63479696ee151aa0bfc.jpg\" _src=\"http://yanxuan.nosdn.127.net/075c4bd0119df63479696ee151aa0bfc.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/65f98f9eb6cc47f603a27200335ed0bd.jpg\" _src=\"http://yanxuan.nosdn.127.net/65f98f9eb6cc47f603a27200335ed0bd.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1b904f6f9f30b6eea98ee14116291573.jpg\" _src=\"http://yanxuan.nosdn.127.net/1b904f6f9f30b6eea98ee14116291573.jpg\" style=\"\"/></p><p><br/></p>","detailMobileHtml":"<p><img src=\"http://yanxuan.nosdn.127.net/43a852a45f4a56d3392f1ddea87eb8eb.jpg\" _src=\"http://yanxuan.nosdn.127.net/43a852a45f4a56d3392f1ddea87eb8eb.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3c9c1934bce0b69ec220ff981ec738fe.jpg\" _src=\"http://yanxuan.nosdn.127.net/3c9c1934bce0b69ec220ff981ec738fe.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/56aa71cb1c36013a6d4e7f884af010fa.jpg\" _src=\"http://yanxuan.nosdn.127.net/56aa71cb1c36013a6d4e7f884af010fa.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f8aab18bfc5b668651064d29cfcd4fcf.jpg\" _src=\"http://yanxuan.nosdn.127.net/f8aab18bfc5b668651064d29cfcd4fcf.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/caf48b811fb86d6a4ee47a849afbc3f5.jpg\" _src=\"http://yanxuan.nosdn.127.net/caf48b811fb86d6a4ee47a849afbc3f5.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1a23bed5db163248ef7d5f696bde6923.jpg\" _src=\"http://yanxuan.nosdn.127.net/1a23bed5db163248ef7d5f696bde6923.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/6d2c20eff38f2b10631fdaf45363642f.jpg\" _src=\"http://yanxuan.nosdn.127.net/6d2c20eff38f2b10631fdaf45363642f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/902eb2eefa3cfb7104755cf8fc7b7d33.jpg\" _src=\"http://yanxuan.nosdn.127.net/902eb2eefa3cfb7104755cf8fc7b7d33.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/4e1a836f6969eb1694d62ce99ea70e5f.jpg\" _src=\"http://yanxuan.nosdn.127.net/4e1a836f6969eb1694d62ce99ea70e5f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1413c155daac6279deb6400671798da6.jpg\" _src=\"http://yanxuan.nosdn.127.net/1413c155daac6279deb6400671798da6.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/9ecdabedeeae302bfeed80572ca51490.jpg\" _src=\"http://yanxuan.nosdn.127.net/9ecdabedeeae302bfeed80572ca51490.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c6ff45ee262e8d486f17134a9dbc0a3a.jpg\" _src=\"http://yanxuan.nosdn.127.net/c6ff45ee262e8d486f17134a9dbc0a3a.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/be082ed4addbd787bb44fae512fb55fe.jpg\" _src=\"http://yanxuan.nosdn.127.net/be082ed4addbd787bb44fae512fb55fe.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/bd32aac49141d87b7a983fe4646a7a75.jpg\" _src=\"http://yanxuan.nosdn.127.net/bd32aac49141d87b7a983fe4646a7a75.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/62fd7c2f44ceccc4ee33bdde2ee998f4.jpg\" _src=\"http://yanxuan.nosdn.127.net/62fd7c2f44ceccc4ee33bdde2ee998f4.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/91e4e6bc20051706e74a6f65684e97cc.jpg\" _src=\"http://yanxuan.nosdn.127.net/91e4e6bc20051706e74a6f65684e97cc.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f47723ba3ffdf2ee107b497c560aef88.jpg\" _src=\"http://yanxuan.nosdn.127.net/f47723ba3ffdf2ee107b497c560aef88.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/95836389e457d1a2c6810e104c907f93.jpg\" _src=\"http://yanxuan.nosdn.127.net/95836389e457d1a2c6810e104c907f93.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/871322a2bd8b44226d597af3f0c52249.jpg\" _src=\"http://yanxuan.nosdn.127.net/871322a2bd8b44226d597af3f0c52249.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/891195e7bb58a6782f0f1da15ff34176.jpg\" _src=\"http://yanxuan.nosdn.127.net/891195e7bb58a6782f0f1da15ff34176.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c232a44890a7e2c1312c214ee4aaead5.jpg\" _src=\"http://yanxuan.nosdn.127.net/c232a44890a7e2c1312c214ee4aaead5.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/c7ee91d512fc23f83a9cde92bcc9663d.jpg\" _src=\"http://yanxuan.nosdn.127.net/c7ee91d512fc23f83a9cde92bcc9663d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1649cd095c5299283d9d307c337f36ec.jpg\" _src=\"http://yanxuan.nosdn.127.net/1649cd095c5299283d9d307c337f36ec.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8f0e3f5422a1e605d2cb0c66d03389e4.jpg\" _src=\"http://yanxuan.nosdn.127.net/8f0e3f5422a1e605d2cb0c66d03389e4.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/e1e90d6bc0b48db9859944113ef36a49.jpg\" _src=\"http://yanxuan.nosdn.127.net/e1e90d6bc0b48db9859944113ef36a49.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/e12296776ba8d5b6c0c18fac593772a2.jpg\" _src=\"http://yanxuan.nosdn.127.net/e12296776ba8d5b6c0c18fac593772a2.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/eab9452b80d2c4e4cc68e309c7de50a8.jpg\" _src=\"http://yanxuan.nosdn.127.net/eab9452b80d2c4e4cc68e309c7de50a8.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3ac44a0026be72d166cd4c00824d600b.jpg\" _src=\"http://yanxuan.nosdn.127.net/3ac44a0026be72d166cd4c00824d600b.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/f98ab8b23724f26fd3a2db9e539e2e4d.jpg\" _src=\"http://yanxuan.nosdn.127.net/f98ab8b23724f26fd3a2db9e539e2e4d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/579e35893568aa03920e05eb16d4009d.jpg\" _src=\"http://yanxuan.nosdn.127.net/579e35893568aa03920e05eb16d4009d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b8921d7ddfe11cb12db4f05a6f120632.jpg\" _src=\"http://yanxuan.nosdn.127.net/b8921d7ddfe11cb12db4f05a6f120632.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8398cca3432ace05b02234ff47cbb25f.jpg\" _src=\"http://yanxuan.nosdn.127.net/8398cca3432ace05b02234ff47cbb25f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1296cacff7a32d6fea465ff1859e52e2.jpg\" _src=\"http://yanxuan.nosdn.127.net/1296cacff7a32d6fea465ff1859e52e2.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/0ab0a51959ae1ac64818d568e64dc6c9.jpg\" _src=\"http://yanxuan.nosdn.127.net/0ab0a51959ae1ac64818d568e64dc6c9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1f0d167aa533ecf32abaf3521b744402.jpg\" _src=\"http://yanxuan.nosdn.127.net/1f0d167aa533ecf32abaf3521b744402.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/7bdb1e96d56ca8542a130a4ddbba7ac9.jpg\" _src=\"http://yanxuan.nosdn.127.net/7bdb1e96d56ca8542a130a4ddbba7ac9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/66c8b538d430b7e4a9772aa9375d284f.jpg\" _src=\"http://yanxuan.nosdn.127.net/66c8b538d430b7e4a9772aa9375d284f.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3410feb14590c53cd31c24c69a875732.jpg\" _src=\"http://yanxuan.nosdn.127.net/3410feb14590c53cd31c24c69a875732.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/fc3fa3cbe0657feaf771a9703635fbc7.jpg\" _src=\"http://yanxuan.nosdn.127.net/fc3fa3cbe0657feaf771a9703635fbc7.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1b44910a98624d648d266ef44922fb7d.jpg\" _src=\"http://yanxuan.nosdn.127.net/1b44910a98624d648d266ef44922fb7d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8eddee7e749deb33c8ca8c28f56f2568.jpg\" _src=\"http://yanxuan.nosdn.127.net/8eddee7e749deb33c8ca8c28f56f2568.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/adfe104a5b1132e734647dc0c950e8af.jpg\" _src=\"http://yanxuan.nosdn.127.net/adfe104a5b1132e734647dc0c950e8af.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/be8ef89cf818a4aeba8d0ddf413cbcf9.jpg\" _src=\"http://yanxuan.nosdn.127.net/be8ef89cf818a4aeba8d0ddf413cbcf9.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/3829f4ed5b477680f169d390c8220c15.jpg\" _src=\"http://yanxuan.nosdn.127.net/3829f4ed5b477680f169d390c8220c15.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/126768f1680c147b7e11f853074d5289.jpg\" _src=\"http://yanxuan.nosdn.127.net/126768f1680c147b7e11f853074d5289.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/b1321f2e495b39212ff2b50c2d0a84b1.jpg\" _src=\"http://yanxuan.nosdn.127.net/b1321f2e495b39212ff2b50c2d0a84b1.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/125bfac27b6596ebe50535ceb87ced60.jpg\" _src=\"http://yanxuan.nosdn.127.net/125bfac27b6596ebe50535ceb87ced60.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/8a93ed3b3caf85e8fd26ad79d771fb69.jpg\" _src=\"http://yanxuan.nosdn.127.net/8a93ed3b3caf85e8fd26ad79d771fb69.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/efbe7ce1c9de4da97ca143a3566d4c48.jpg\" _src=\"http://yanxuan.nosdn.127.net/efbe7ce1c9de4da97ca143a3566d4c48.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/943384ff66cec540eb1c14c6b6a3ce2c.jpg\" _src=\"http://yanxuan.nosdn.127.net/943384ff66cec540eb1c14c6b6a3ce2c.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/06ade0cd8bfe76fc980199281bf0073d.jpg\" _src=\"http://yanxuan.nosdn.127.net/06ade0cd8bfe76fc980199281bf0073d.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/075c4bd0119df63479696ee151aa0bfc.jpg\" _src=\"http://yanxuan.nosdn.127.net/075c4bd0119df63479696ee151aa0bfc.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/65f98f9eb6cc47f603a27200335ed0bd.jpg\" _src=\"http://yanxuan.nosdn.127.net/65f98f9eb6cc47f603a27200335ed0bd.jpg\" style=\"\"/></p><p><img src=\"http://yanxuan.nosdn.127.net/1b904f6f9f30b6eea98ee14116291573.jpg\" _src=\"http://yanxuan.nosdn.127.net/1b904f6f9f30b6eea98ee14116291573.jpg\" style=\"\"/></p><p><br/></p>","detailTitle":"精梳长绒棉,亲肤舒适","expireTime":null,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":1008009,"giftPoint":1008009,"hit":4,"id":22779,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"清新趣粉全棉四件套 青粉拼接","newStatus":1,"note":"","originalPrice":399.00,"pic":"http://yanxuan.nosdn.127.net/be64df0a04ade4cfd75bf7d4e8509ecc.png","previewStatus":0,"price":419.00,"productAttributeCategoryId":1008009,"productCategoryId":1008009,"productCategoryName":"","productSn":"1127039","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":1008009,"schoolId":0,"schoolName":"","serviceIds":"","sort":11,"stock":1127039,"storeId":1,"storeName":"北京皮包专卖","subTitle":"精梳长绒棉,亲肤舒适","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"","usePointLimit":0,"verifyStatus":1,"weight":0},{"albumPics":"","areaId":1101,"areaName":"天津市","brandId":543,"brandName":"罗莱制造商","createTime":1582311611000,"deleteStatus":1,"description":"","detailDesc":"哈哈哈","detailHtml":"","detailMobileHtml":"","detailTitle":"哈哈哈","expireTime":1582704000000,"feightTemplateId":10,"fenxiaoPrice":0,"giftGrowth":1,"giftPoint":0,"hit":67,"id":25039,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"哈哈哈","lowStock":0,"memberId":0,"name":"test","newStatus":1,"note":"哈哈哈","originalPrice":0.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200222/3b2f80a50c6a1ac5fc557491a8736b77.jpg","previewStatus":0,"price":0.00,"productAttributeCategoryId":385,"productCategoryId":3300,"productCategoryName":"手机","productSn":"90","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":1,"schoolId":0,"schoolName":"","serviceIds":"1,2,3","sort":0,"stock":17316,"storeId":1,"storeName":"北京豪车专卖","subTitle":"123","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"斤","usePointLimit":0,"verifyStatus":1,"weight":0.00},{"albumPics":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 2@3x(2).png","areaId":4103,"areaName":"","brandId":543,"brandName":"罗莱制造商","createTime":1584511861000,"deleteStatus":0,"description":"","detailDesc":"美味每一天,永恒健康","detailHtml":"","detailMobileHtml":"<p><img class=\"wscnph\" src=\"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 3@3x.png\" /><img class=\"wscnph\" src=\"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 4@3x.png\" /><img class=\"wscnph\" src=\"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 23@3x.png\" /><img class=\"wscnph\" src=\"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 2@3x(3).png\" /></p>","detailTitle":"非常非常喜欢","expireTime":1584892800000,"feightTemplateId":10,"fenxiaoPrice":0,"giftGrowth":0,"giftPoint":0,"hit":47,"id":25073,"isFenxiao":0,"isPaiMai":1,"keyword":"","keywords":"老人爱喝","lowStock":0,"memberId":0,"name":"脑白金","newStatus":0,"note":"","originalPrice":30.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 2@3x(2).png","previewStatus":0,"price":50.00,"productAttributeCategoryId":401,"productCategoryId":3314,"productCategoryName":"","productSn":"sp001231","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":0,"sale":13,"schoolId":0,"schoolName":"","serviceIds":"1,2,3","sort":0,"stock":48,"storeId":1,"storeName":"北京豪车专卖","subTitle":"脑白金美味健康","supplyId":0,"timeSecound":null,"transfee":12,"type":0,"unit":"包","usePointLimit":0,"verifyStatus":1,"weight":30.00}],"pageCount":5}}
```

### 16、添加商品浏览记录

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/addView
```

#### 示例：

[http://www.yjlive.cn:8083/api/single/pms/addView](http://www.yjlive.cn:8083/api/single/pms/addView)


#### 请求方式：
```
POST
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|goodsId      |Y       |int   |商品ID |


#### 返回示例：

```javascript

{"code":200,"data":"操作成功"}
```


### 17、获取商品评价信息

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/consult/list?goodsId=92
```

#### 示例：

[http://www.yjlive.cn:8083/api/single/pms/consult/list?goodsId=92](http://www.yjlive.cn:8083/api/single/pms/consult/list?goodsId=92)


#### 请求方式：
```
GET
```

#### 参数类型：query, param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|goodsId      |Y       |int   | 商品ID |



#### 返回示例：

```javascript

{"code":200,"data":{"count":{"all":0,"bad":0,"general":0,"goods":0,"other":0,"persent":200,"storeId":0},"list":[{"consultAddtime":1580284163000,"consultContent":"噢噢噢哦哦","consultReply":"","consultReplyTime":null,"email":"","goodsId":92,"goodsName":"","id":15,"isanonymous":true,"memberId":142,"memberName":"","orderId":1,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20190830/uniapp.jpeg","stars":0,"storeId":0,"type":1}]}}
```

### 18、收藏和取消收藏

#### 请求URL：
```
http://www.yjlive.cn:8083/api/collection/favoriteSave
```

#### 示例：

[http://www.yjlive.cn:8083/api/collection/favoriteSave](http://www.yjlive.cn:8083/api/collection/favoriteSave)


#### 请求方式：
```
GET
```

#### 参数类型： param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|objId      |Y       |int   | 收藏对象ID |
|type       |Y       |int   | 收藏类别 1商品 2 文章 3 店铺 |
|name       |Y       |int   | 名称 |
|meno1      |Y       |int   | 价格 |
|meno2      |Y       |int   | 销量 |

#### 返回示例：

```javascript

{"code":200,"data":1}

```

### 19、获取我的收藏

#### 请求URL：
```
http://www.yjlive.cn:8083/api/collection/listCollect
```

#### 示例：

[http://www.yjlive.cn:8083/api/collection/listCollect](http://www.yjlive.cn:8083/api/collection/listCollects)


#### 请求方式：
```
GET
```

#### 参数类型： param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|



#### 返回示例：

```javascript

{"code":200,"data":{"current":1,"pages":1,"records":[{"addTime":1584671434000,"id":565,"memberId":297,"meno1":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200222/3b2f80a50c6a1ac5fc557491a8736b77.jpg","meno2":"0","meno3":"1","name":"test","objId":25039,"storeId":0,"type":1},{"addTime":1584601655000,"id":564,"memberId":297,"meno1":"http://yanxuan.nosdn.127.net/addc278cf9c301dd535791df2e03b2ea.png","meno2":"379","meno3":"1008011","name":"简约知性全棉四件套 星空蓝","objId":22778,"storeId":0,"type":1},{"addTime":1584512842000,"id":562,"memberId":297,"meno1":"http://yanxuan.nosdn.127.net/922fdbe007033f7a88f7ebc57c3d1e75.png","meno2":"79","meno3":"1010022","name":"男式玩色内裤","objId":22818,"storeId":0,"type":1},{"addTime":1584502829000,"id":560,"memberId":297,"meno1":"https://img14.360buyimg.com/n7/jfs/t1/68975/8/5480/196083/5d3a590bE542ecaed/f3fe18422af01f4f.jpg","meno2":"798","meno3":"85","name":"LALABOBO 2019夏季新品可爱少女连衣裙女短袖T恤大头像裙子|L19B-WLDQ21商场同款 白色 M","objId":91,"storeId":0,"type":1},{"addTime":1584502792000,"id":559,"memberId":297,"meno1":"https://img10.360buyimg.com/n7/jfs/t1/36352/31/14713/230259/5d3af98aEdd0ab6d9/aa323333953423a9.jpg","meno2":"389","meno3":"127","name":"垃圾代码一斤多少钱啊，哈哈哈，4千，值4千，骗一个是一个场同款 玫瑰紫-PC 175/96A/L","objId":75,"storeId":0,"type":1},{"addTime":1584422141000,"id":557,"memberId":297,"meno1":"undefined","meno2":"19","meno3":"undefined","name":"undefined","objId":13,"storeId":1,"type":4}],"size":10,"total":6}}

```

### 20、删除收藏

#### 请求URL：
```
http://www.yjlive.cn:8083/api/collection/delete
```

#### 示例：

#### 请求方式：
```
POST
```

#### 参数类型： param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|ids      |Y       |string   | 逗号分割 |


#### 返回示例：

```javascript

{"code":200,"data":"操作成功"}

```


### 21、用户信息

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/home/userInfo
```

#### 示例：

[http://www.yjlive.cn:8083/api/single/home/userInfo](http://www.yjlive.cn:8083/api/single/home/userInfo)


#### 请求方式：
```
GET
```

#### 参数类型： param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|



#### 返回示例：

```javascript

{"code":200,"data":{"histories":[{"amount":23,"couponCode":"1370311099310297","couponId":38,"createTime":1584384903000,"endTime":1636099200000,"getType":1,"id":151,"memberId":297,"memberNickname":"13146587722","minPoint":23,"note":"产品测试:满23.00减23.00","orderId":0,"orderSn":"","startTime":1569916800000,"storeId":1,"useStatus":0,"useTime":null},{"amount":10,"couponCode":"1967184206040297","couponId":51,"createTime":1584419672000,"endTime":1636099200000,"getType":1,"id":152,"memberId":297,"memberNickname":"13146587722","minPoint":1,"note":"测试1:满1.00减10.00","orderId":3851,"orderSn":"15844200999550101297","startTime":1573804800000,"storeId":1,"useStatus":1,"useTime":1584420100000},{"amount":10,"couponCode":"2006707011880297","couponId":51,"createTime":1584420067000,"endTime":1636099200000,"getType":1,"id":153,"memberId":297,"memberNickname":"13146587722","minPoint":1,"note":"测试1:满1.00减10.00","orderId":3864,"orderSn":"15844297237720101297","startTime":1573804800000,"storeId":1,"useStatus":1,"useTime":1584429724000},{"amount":10,"couponCode":"3071959478670297","couponId":39,"createTime":1584401920000,"endTime":1636099200000,"getType":1,"id":154,"memberId":297,"memberNickname":"13146587722","minPoint":100,"note":"全场优惠券:满100.00减10.00","orderId":3874,"orderSn":"15844499648340101297","startTime":1569916800000,"storeId":1,"useStatus":1,"useTime":1584449965000},{"amount":5,"couponCode":"3879422994960297","couponId":40,"createTime":1584438794000,"endTime":1636099200000,"getType":1,"id":155,"memberId":297,"memberNickname":"13146587722","minPoint":50,"note":"测试优惠券:满50.00减5.00","orderId":3876,"orderSn":"15844556834340101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584455683000},{"amount":15,"couponCode":"4477683929520297","couponId":41,"createTime":1584444777000,"endTime":1636099200000,"getType":1,"id":156,"memberId":297,"memberNickname":"13146587722","minPoint":150,"note":"1234:满150.00减15.00","orderId":3879,"orderSn":"15845026818520101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584502682000},{"amount":5,"couponCode":"0110541889240297","couponId":44,"createTime":1584501105000,"endTime":1636099200000,"getType":1,"id":157,"memberId":297,"memberNickname":"13146587722","minPoint":50,"note":"测试优惠券:满50.00减5.00","orderId":3881,"orderSn":"15845129771640101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584512977000},{"amount":10,"couponCode":"0119191541460297","couponId":43,"createTime":1584501192000,"endTime":1636099200000,"getType":1,"id":158,"memberId":297,"memberNickname":"13146587722","minPoint":100,"note":"全场优惠券:满100.00减10.00","orderId":3889,"orderSn":"15845218966070101297","startTime":1569916800000,"storeId":1,"useStatus":1,"useTime":1584521897000},{"amount":10,"couponCode":"1486596770480297","couponId":45,"createTime":1584514866000,"endTime":1636099200000,"getType":1,"id":159,"memberId":297,"memberNickname":"11","minPoint":100,"note":"全场优惠券:满100.00减10.00","orderId":0,"orderSn":"","startTime":1569916800000,"storeId":1,"useStatus":0,"useTime":null},{"amount":1,"couponCode":"1959586724450297","couponId":49,"createTime":1584519596000,"endTime":1636099200000,"getType":1,"id":160,"memberId":297,"memberNickname":"11","minPoint":200,"note":"222:满200.00减1.00","orderId":3887,"orderSn":"15845215551670101297","startTime":1573632000000,"storeId":1,"useStatus":1,"useTime":1584521555000},{"amount":5,"couponCode":"2468629282790297","couponId":46,"createTime":1584524686000,"endTime":1636099200000,"getType":1,"id":161,"memberId":297,"memberNickname":"11","minPoint":50,"note":"测试优惠券:满50.00减5.00","orderId":3893,"orderSn":"15845374431090101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584537443000},{"amount":30,"couponCode":"8009532432480297","couponId":53,"createTime":1584580095000,"endTime":1636099200000,"getType":1,"id":162,"memberId":297,"memberNickname":"11","minPoint":30,"note":"双12优惠券:满30.00减30.00","orderId":3919,"orderSn":"15847647016790101297","startTime":1574668800000,"storeId":1,"useStatus":1,"useTime":1584764702000},{"amount":20,"couponCode":"6963778870100297","couponId":54,"createTime":1584669638000,"endTime":1636099200000,"getType":1,"id":163,"memberId":297,"memberNickname":"测试","minPoint":20,"note":"双11优惠券:满20.00减20.00","orderId":0,"orderSn":"","startTime":1574582400000,"storeId":1,"useStatus":0,"useTime":null},{"amount":10,"couponCode":"8573483158900297","couponId":55,"createTime":1584685735000,"endTime":1636099200000,"getType":1,"id":164,"memberId":297,"memberNickname":"测试","minPoint":100,"note":"朋克:满100.00减10.00","orderId":0,"orderSn":"","startTime":1576051200000,"storeId":1,"useStatus":0,"useTime":null},{"amount":10,"couponCode":"0988584015910297","couponId":56,"createTime":1584709886000,"endTime":1636099200000,"getType":1,"id":165,"memberId":297,"memberNickname":"测试","minPoint":100,"note":"双12促销:满100.00减10.00","orderId":0,"orderSn":"","startTime":1575273600000,"storeId":1,"useStatus":0,"useTime":null},{"amount":100,"couponCode":"5908653044000297","couponId":58,"createTime":1584759087000,"endTime":1585123200000,"getType":1,"id":166,"memberId":297,"memberNickname":"测试","minPoint":500,"note":"yxtest:满500.00减100.00","orderId":0,"orderSn":"","startTime":1583827200000,"storeId":1,"useStatus":0,"useTime":null}],"member":{"areaId":0,"areaName":"","avatar":"","birthday":null,"blance":640,"buyCount":27,"buyMoney":10917,"city":"","confimpassword":"","createTime":1584412356000,"gender":1,"growth":0,"historyIntegration":0,"icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20200320-8b68497a31c1487097b9affe5e0e9c04.png","id":297,"integration":28466,"invitecode":"","job":"","luckeyCount":0,"memberLevelId":1,"memberLevelName":"砖石会员","nickname":"测试","password":"$2a$10$UHBlIz4wjXhWGo9aWUfPIObAM9CtdYGYOz4W7RcsE.a7m83d0pTfS","personalizedSignature":"","phone":"13146587722","phonecode":"","roomDesc":"","roomNums":"","schoolId":0,"schoolName":"","sourceType":0,"status":1,"storeId":1,"username":"13146587722","weixinOpenid":""}}}

```


### 22、获取收货地址列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/address/list
```

#### 示例：

[http://www.yjlive.cn:8083/api/address/list](http://www.yjlive.cn:8083/api/address/list)


#### 请求方式：
```
GET
```

#### 参数类型： param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|



#### 返回示例：

```javascript

{"code":200,"data":[{"city":"市辖区","defaultStatus":1,"detailAddress":"分身乏术的发生地","id":166,"latitude":0.0,"longitude":0.0,"memberId":297,"name":"时尚大方","phoneNumber":"13811090913","postCode":"","province":"北京市","region":"西城区","storeId":1}]}
```

### 23、 添加收货地址

#### 请求URL：
```
http://www.yjlive.cn:8083/api/address/save
```

#### 示例：


#### 请求方式：
```
POST
```

#### 参数类型：

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|defaultStatus: 1|
|region: 西城区|
|detailAddress: 猪脚街2号|
|phoneNumber: 13146587733|
|province: 北京市|
|city: 市辖区|
|longitude: 113.45046|
|latitude: 29.47664|
|name: 123|

#### 返回示例：

```javascript
{"code":200,"data":true}
```


### 24、删除收货地址

#### 请求URL：
```
http://www.yjlive.cn:8083/api/address/delete
```

#### 示例：


#### 请求方式：
```
GET
```

#### 参数类型：

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|id: 166|

#### 返回示例：

```javascript

{"code":200,"data":true}
```

### 25、我的优惠券列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/sms/listMemberCoupon?
```

#### 示例：


#### 请求方式：
```
get
```

#### 参数类型：

|参数|是否必选|类型|说明|




#### 返回示例：

```javascript

{"code":200,"data":[{"amount":23,"couponCode":"1370311099310297","couponId":38,"createTime":1584384903000,"endTime":1636099200000,"getType":1,"id":151,"memberId":297,"memberNickname":"13146587722","minPoint":23,"note":"产品测试:满23.00减23.00","orderId":0,"orderSn":"","startTime":1569916800000,"storeId":1,"useStatus":0,"useTime":null},{"amount":10,"couponCode":"1967184206040297","couponId":51,"createTime":1584419672000,"endTime":1636099200000,"getType":1,"id":152,"memberId":297,"memberNickname":"13146587722","minPoint":1,"note":"测试1:满1.00减10.00","orderId":3851,"orderSn":"15844200999550101297","startTime":1573804800000,"storeId":1,"useStatus":1,"useTime":1584420100000},{"amount":10,"couponCode":"2006707011880297","couponId":51,"createTime":1584420067000,"endTime":1636099200000,"getType":1,"id":153,"memberId":297,"memberNickname":"13146587722","minPoint":1,"note":"测试1:满1.00减10.00","orderId":3864,"orderSn":"15844297237720101297","startTime":1573804800000,"storeId":1,"useStatus":1,"useTime":1584429724000},{"amount":10,"couponCode":"3071959478670297","couponId":39,"createTime":1584401920000,"endTime":1636099200000,"getType":1,"id":154,"memberId":297,"memberNickname":"13146587722","minPoint":100,"note":"全场优惠券:满100.00减10.00","orderId":3874,"orderSn":"15844499648340101297","startTime":1569916800000,"storeId":1,"useStatus":1,"useTime":1584449965000},{"amount":5,"couponCode":"3879422994960297","couponId":40,"createTime":1584438794000,"endTime":1636099200000,"getType":1,"id":155,"memberId":297,"memberNickname":"13146587722","minPoint":50,"note":"测试优惠券:满50.00减5.00","orderId":3876,"orderSn":"15844556834340101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584455683000},{"amount":15,"couponCode":"4477683929520297","couponId":41,"createTime":1584444777000,"endTime":1636099200000,"getType":1,"id":156,"memberId":297,"memberNickname":"13146587722","minPoint":150,"note":"1234:满150.00减15.00","orderId":3879,"orderSn":"15845026818520101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584502682000},{"amount":5,"couponCode":"0110541889240297","couponId":44,"createTime":1584501105000,"endTime":1636099200000,"getType":1,"id":157,"memberId":297,"memberNickname":"13146587722","minPoint":50,"note":"测试优惠券:满50.00减5.00","orderId":3881,"orderSn":"15845129771640101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584512977000},{"amount":10,"couponCode":"0119191541460297","couponId":43,"createTime":1584501192000,"endTime":1636099200000,"getType":1,"id":158,"memberId":297,"memberNickname":"13146587722","minPoint":100,"note":"全场优惠券:满100.00减10.00","orderId":3889,"orderSn":"15845218966070101297","startTime":1569916800000,"storeId":1,"useStatus":1,"useTime":1584521897000},{"amount":10,"couponCode":"1486596770480297","couponId":45,"createTime":1584514866000,"endTime":1636099200000,"getType":1,"id":159,"memberId":297,"memberNickname":"11","minPoint":100,"note":"全场优惠券:满100.00减10.00","orderId":0,"orderSn":"","startTime":1569916800000,"storeId":1,"useStatus":0,"useTime":null},{"amount":1,"couponCode":"1959586724450297","couponId":49,"createTime":1584519596000,"endTime":1636099200000,"getType":1,"id":160,"memberId":297,"memberNickname":"11","minPoint":200,"note":"222:满200.00减1.00","orderId":3887,"orderSn":"15845215551670101297","startTime":1573632000000,"storeId":1,"useStatus":1,"useTime":1584521555000},{"amount":5,"couponCode":"2468629282790297","couponId":46,"createTime":1584524686000,"endTime":1636099200000,"getType":1,"id":161,"memberId":297,"memberNickname":"11","minPoint":50,"note":"测试优惠券:满50.00减5.00","orderId":3893,"orderSn":"15845374431090101297","startTime":1570003200000,"storeId":1,"useStatus":1,"useTime":1584537443000},{"amount":30,"couponCode":"8009532432480297","couponId":53,"createTime":1584580095000,"endTime":1636099200000,"getType":1,"id":162,"memberId":297,"memberNickname":"11","minPoint":30,"note":"双12优惠券:满30.00减30.00","orderId":3919,"orderSn":"15847647016790101297","startTime":1574668800000,"storeId":1,"useStatus":1,"useTime":1584764702000},{"amount":20,"couponCode":"6963778870100297","couponId":54,"createTime":1584669638000,"endTime":1636099200000,"getType":1,"id":163,"memberId":297,"memberNickname":"测试","minPoint":20,"note":"双11优惠券:满20.00减20.00","orderId":0,"orderSn":"","startTime":1574582400000,"storeId":1,"useStatus":0,"useTime":null},{"amount":10,"couponCode":"8573483158900297","couponId":55,"createTime":1584685735000,"endTime":1636099200000,"getType":1,"id":164,"memberId":297,"memberNickname":"测试","minPoint":100,"note":"朋克:满100.00减10.00","orderId":0,"orderSn":"","startTime":1576051200000,"storeId":1,"useStatus":0,"useTime":null},{"amount":10,"couponCode":"0988584015910297","couponId":56,"createTime":1584709886000,"endTime":1636099200000,"getType":1,"id":165,"memberId":297,"memberNickname":"测试","minPoint":100,"note":"双12促销:满100.00减10.00","orderId":0,"orderSn":"","startTime":1575273600000,"storeId":1,"useStatus":0,"useTime":null},{"amount":100,"couponCode":"5908653044000297","couponId":58,"createTime":1584759087000,"endTime":1585123200000,"getType":1,"id":166,"memberId":297,"memberNickname":"测试","minPoint":500,"note":"yxtest:满500.00减100.00","orderId":0,"orderSn":"","startTime":1583827200000,"storeId":1,"useStatus":0,"useTime":null}]}
```

### 26、我的售后列表

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/order/aftersaleslist?pageNum=1&pageSize=5
```

#### 示例：


#### 请求方式：
```
GET
```

#### 参数类型：

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|


#### 返回示例：

```javascript

{"code":200,"data":{"current":1,"pages":1,"records":[{"companyAddressId":0,"createTime":1584435430000,"description":"","handleMan":"","handleNote":"","handleTime":null,"id":41,"memberUsername":"13146587722","orderId":3868,"orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5603,"integrationAmount":0,"orderId":3868,"orderSn":"15844304681470101297","productAttr":"","productBrand":"","productCategoryId":1036000,"productId":5411,"productName":"全棉色织绗缝夏凉件套","productPic":"http://yanxuan.nosdn.127.net/69145abddddd31ae8878ea7ca7297b4b.png","productPrice":619.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":1,"storeName":"","type":1}],"orderSn":"","productAttr":"","productBrand":"","productCount":1,"productId":8,"productName":"测试赠品","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200108/赠品.png","productPrice":2.00,"productRealPrice":0,"proofPics":"","reason":"","receiveMan":"","receiveNote":"","receiveTime":null,"returnAmount":0.00,"returnName":"13146587722","returnPhone":"13146587722","status":0,"storeId":1,"type":1},{"companyAddressId":0,"createTime":1584248916000,"description":"","handleMan":"","handleNote":"","handleTime":null,"id":39,"memberUsername":"13146587722","orderId":3813,"orderItemList":[],"orderSn":"","productAttr":"","productBrand":"","productCount":1,"productId":81,"productName":"FILA斐乐女装White Line系列官方马思纯同款2019秋季新款梭织连帽外套女商场同款 骑士蓝-RB 165/84A/M","productPic":"https://img14.360buyimg.com/n7/jfs/t1/69650/15/5473/162729/5d3af988E0408f4df/af31d78ae6be9c87.jpg","productPrice":889.00,"productRealPrice":0,"proofPics":"","reason":"","receiveMan":"","receiveNote":"","receiveTime":null,"returnAmount":0.00,"returnName":"电话的","returnPhone":"13146587722","status":0,"storeId":1,"type":1},{"companyAddressId":0,"createTime":1584248916000,"description":"","handleMan":"","handleNote":"","handleTime":null,"id":40,"memberUsername":"13146587722","orderId":3813,"orderItemList":[],"orderSn":"","productAttr":"","productBrand":"","productCount":1,"productId":25066,"productName":"上衣","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","productPrice":99.00,"productRealPrice":0,"proofPics":"","reason":"","receiveMan":"","receiveNote":"","receiveTime":null,"returnAmount":0.00,"returnName":"电话的","returnPhone":"13146587722","status":0,"storeId":1,"type":1},{"companyAddressId":0,"createTime":1584227512000,"description":"","handleMan":"","handleNote":"","handleTime":null,"id":31,"memberUsername":"13146587722","orderId":3805,"orderItemList":[],"orderSn":"","productAttr":"","productBrand":"","productCount":2,"productId":88,"productName":"普普风商场同款2019夏装新款纽扣装饰牛仔半裙复古百搭高腰半身裙女13929 牛仔兰 L","productPic":"https://img11.360buyimg.com/n7/jfs/t1/57913/16/6004/248427/5d3a652fE62621ff9/0f85a3275effaca7.jpg","productPrice":279.00,"productRealPrice":0,"proofPics":"","reason":"","receiveMan":"","receiveNote":"","receiveTime":null,"returnAmount":0.00,"returnName":"电话的","returnPhone":"13146587722","status":0,"storeId":1,"type":1}],"size":5,"total":4}}
```


### 27、申请售后

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/saveOmsOrderReturnApply
```

#### 示例：


#### 请求方式：
```
POST
```

#### 参数类型：

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|orderId      |Y       |int      | 订单编号 |
|type         |Y       |int      | 0换货 1退钱 2退货3 退钱退货 |
|items        |Y       |string   | 订单商品编号 逗号分割 |
|images       |Y       |string   | 图片 |
|returnAmount |Y       |string   | 退款金额 |
|desc         |Y       |string   | 退款理由 |

#### 返回示例：

```javascript

{"code":200,"data":"操作成功"}
```

### 28、确认收货

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/confimDelivery
```

#### 示例：


#### 请求方式：
```
POST
```

#### 参数类型：param, query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|id      |Y       |int   | 订单id |



#### 返回示例：

```javascript

{"code":200,"data":"操作成功"}
```


### 29、订单评价

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/orderevaluate
```

#### 示例：


#### 请求方式：
```
DELETE
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|orderId      |Y       |int   | 用户id |
|items        |Y       |string   | [{"goodsId":5660,"score":5,"textarea":"11"}] |

#### 返回示例：

```javascript

{"code":200,"data":1}
```


### 30、图片上传

#### 请求URL：
```
http://www.yjlive.cn:8083/api/upload
```

#### 示例：

#### 请求方式：
```
POST
```

#### 参数类型： param, query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|method: images.upload|
|upfile: blob:http://localhost:8083/b9a722fc-dd16-4ad1-85bc-6d1c2dbbde94|
|file: (binary)|

#### 返回示例：

```javascript

{"code":200,"data":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20200321-dc2b2029ad494c3385397f19cf03c8f7.jpg"}

```



### 31、我的购物车

#### 请求URL：
```
http://www.yjlive.cn:8083/api/cart/list
```

#### 示例：

[http://www.yjlive.cn:8083/api/cart/list](http://www.yjlive.cn:8083/api/cart/list)


#### 请求方式：
```
GET
```

#### 参数类型： param, query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|


#### 返回示例：

```javascript

{"code":200,"data":{"cartTotal":{"goodsCount":2,"checkedGoodsCount":2,"goodsAmount":100.00,"checkedGoodsAmount":100.00},"cartItemList":[{"checked":1,"createDate":1584775016000,"deleteStatus":0,"id":2504,"isFenxiao":0,"memberId":297,"memberNickname":"","modifyDate":1584775020000,"price":50.00,"product":null,"productAttr":"","productBrand":"罗莱制造商","productCategoryId":3314,"productId":25073,"productName":"脑白金","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/编组 2@3x(2).png","productSkuCode":"","productSkuId":0,"productSn":"","productSubTitle":"","quantity":2,"skuStock":null,"sp1":"","sp2":"","sp3":"","storeId":1,"storeName":"北京豪车专卖"}],"promoteAmount":0}}
```


### 32、删除购物车

#### 请求URL：
```
http://www.yjlive.cn:8083/api/cart/delete
```

#### 示例：

[http://www.yjlive.cn:8083/api/cart/delete](http://www.yjlive.cn:8083/api/cart/delete)


#### 请求方式：
```
GET
```

#### 参数类型： param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|cart_id_list      |Y       |int   | 逗号分割 |


#### 返回示例：

```javascript

{"code":200,"data":{"code":200,"data":{"cartTotal":{"goodsCount":0,"checkedGoodsCount":0,"goodsAmount":0,"checkedGoodsAmount":0},"cartItemList":[],"promoteAmount":0}}}
```


### 33、商品详情

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/goods/detail?id=25066
```

#### 示例：

[http://www.yjlive.cn:8083/api/single/pms/goods/detail?id=25066](http://www.yjlive.cn:8083/api/single/pms/goods/detail?id=25066)


#### 请求方式：
```
GET
```

#### 参数类型： param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|id     |Y       |int   | 商品编号 |

#### 返回示例：

```javascript

{"code":200,"data":{"goods":{"goods":{"albumPics":"","areaId":4401,"areaName":"","brandId":780,"brandName":" 七匹狼","createTime":1584238793000,"deleteStatus":0,"description":"上衣","detailDesc":"","detailHtml":"","detailMobileHtml":"<p><img class=\"wscnph\" src=\"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg\" /></p>","detailTitle":"","expireTime":1584604800000,"feightTemplateId":0,"fenxiaoPrice":0,"giftGrowth":1,"giftPoint":0,"hit":84,"id":25066,"isFenxiao":1,"isPaiMai":1,"keyword":"","keywords":"","lowStock":0,"memberId":0,"name":"上衣","newStatus":1,"note":"","originalPrice":199.00,"pic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","previewStatus":0,"price":99.00,"productAttributeCategoryId":387,"productCategoryId":3309,"productCategoryName":"服装","productSn":"111","promotionEndTime":null,"promotionPerLimit":0,"promotionPrice":0.00,"promotionStartTime":null,"promotionType":0,"publishStatus":1,"qsType":0,"recommandStatus":1,"sale":42,"schoolId":0,"schoolName":"","serviceIds":"","sort":0,"stock":77,"storeId":1189,"storeName":"佳佳百货","subTitle":"上衣","supplyId":0,"timeSecound":null,"transfee":0,"type":0,"unit":"件","usePointLimit":0,"verifyStatus":1,"weight":0.00},"memberPriceList":[],"prefrenceAreaProductRelationList":[],"productAttributeNameValueList":null,"productAttributeValueList":[],"productCanShuValueList":[],"productFullReductionList":[],"productLadderList":[],"skuStockList":[],"storeInfo":null,"subjectProductRelationList":[],"typeGoodsList":[]},"favorite":false}}
```


### 34、加入购物车

#### 请求URL：
```
http://www.yjlive.cn:8083/api/cart/addCart
```

#### 示例：

[http://www.yjlive.cn:8083/api/cart/addCart](http://www.yjlive.cn:8083/api/cart/addCart)


#### 请求方式：
```
GET
```

#### 参数类型： param, query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|goodsId      |Y       |int   | 商品id |



#### 返回示例：

```javascript

{"code":200,"data":{"checked":1,"createDate":1584779293913,"deleteStatus":0,"id":2505,"isFenxiao":0,"memberId":297,"memberNickname":"","modifyDate":null,"price":99.00,"product":null,"productAttr":"","productBrand":" 七匹狼","productCategoryId":3309,"productId":25066,"productName":"上衣","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","productSkuCode":"","productSkuId":0,"productSn":"","productSubTitle":"","quantity":1,"skuStock":null,"sp1":"","sp2":"","sp3":"","storeId":1189,"storeName":"佳佳百货"}}
```


### 35、商品详情立即购买

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/submitPreview?cartId=2505&type=1
```

#### 示例：

[http://www.yjlive.cn:8083/api/single/oms/submitPreview?cartId=2505&type=1](http://www.yjlive.cn:8083/api/single/oms/submitPreview?cartId=2505&type=1)


#### 请求方式：
```
GET
```

#### 参数类型： param, query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|cartId      |Y       |int   | 购物车id |
|type        |N       |int   | 1商品详情 2购物车 |



#### 返回示例：

```javascript

{"code":200,"data":{"address":{"city":"市辖区","defaultStatus":1,"detailAddress":"猪脚街2号","id":168,"latitude":29.0,"longitude":113.0,"memberId":297,"name":"123","phoneNumber":"13146587733","postCode":"","province":"北京市","region":"西城区","storeId":1},"basicGiftsList":[{"activiGoods":0,"activiUser":0,"actrule":[],"actrule1":[],"bigType":0,"createTime":null,"endTime":null,"giftIds":"","giftsList":[{"id":8,"name":"锤子按摩小礼品","name1":"","pic":"https://img01.hua.com/uploadpic/newpic/9010011.jpg_220x240.jpg","price":""}],"goodsIds":"","id":7,"memberLevelList":[],"name":"双11小礼品","note":"","productCategoryRelationList":[],"productRelationList":[],"rule":null,"rules":"","smallType":0,"startTime":null,"status":0,"storeId":0,"userLevel":""}],"blance":640,"calcAmount":{"freightAmount":0,"payAmount":198.00,"promotionAmount":0,"totalAmount":198.00},"cartPromotionItemList":[{"checked":1,"createDate":1584779294000,"deleteStatus":0,"id":2505,"isFenxiao":0,"memberId":297,"memberNickname":"","modifyDate":1584779360000,"price":99.00,"product":null,"productAttr":"","productBrand":" 七匹狼","productCategoryId":3309,"productId":25066,"productName":"上衣","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","productSkuCode":"","productSkuId":0,"productSn":"","productSubTitle":"","quantity":2,"skuStock":null,"sp1":"","sp2":"","sp3":"","storeId":1189,"storeName":"佳佳百货"}],"couponHistoryDetailList":[{"amount":0,"categoryRelationList":[],"coupon":{"amount":10.00,"code":"","count":0,"enableTime":null,"endTime":1636099200000,"id":45,"memberLevel":0,"minPoint":100.00,"name":"全场优惠券","note":"","perLimit":0,"platform":0,"publishCount":0,"receiveCount":0,"startTime":1569916800000,"storeId":0,"type":0,"useCount":0,"useType":0},"couponCode":"1486596770480297","couponId":45,"createTime":1584514866000,"endTime":1636099200000,"getType":1,"id":159,"memberId":297,"memberNickname":"11","minPoint":0,"note":"全场优惠券:满100.00减10.00","orderId":0,"orderSn":"","productRelationList":[],"startTime":1569916800000,"storeId":0,"useStatus":0,"useTime":null},{"amount":0,"categoryRelationList":[],"coupon":{"amount":20.00,"code":"","count":0,"enableTime":null,"endTime":1636099200000,"id":54,"memberLevel":0,"minPoint":20.00,"name":"双11优惠券","note":"","perLimit":0,"platform":0,"publishCount":0,"receiveCount":0,"startTime":1574582400000,"storeId":0,"type":0,"useCount":0,"useType":0},"couponCode":"6963778870100297","couponId":54,"createTime":1584669638000,"endTime":1636099200000,"getType":1,"id":163,"memberId":297,"memberNickname":"测试","minPoint":0,"note":"双11优惠券:满20.00减20.00","orderId":0,"orderSn":"","productRelationList":[],"startTime":1574582400000,"storeId":0,"useStatus":0,"useTime":null},{"amount":0,"categoryRelationList":[],"coupon":{"amount":10.00,"code":"","count":0,"enableTime":null,"endTime":1636099200000,"id":55,"memberLevel":0,"minPoint":100.00,"name":"朋克","note":"","perLimit":0,"platform":0,"publishCount":0,"receiveCount":0,"startTime":1576051200000,"storeId":0,"type":0,"useCount":0,"useType":0},"couponCode":"8573483158900297","couponId":55,"createTime":1584685735000,"endTime":1636099200000,"getType":1,"id":164,"memberId":297,"memberNickname":"测试","minPoint":0,"note":"朋克:满100.00减10.00","orderId":0,"orderSn":"","productRelationList":[],"startTime":1576051200000,"storeId":0,"useStatus":0,"useTime":null}],"goods":null,"groupActivity":null,"groupAndOrderVo":null,"integrationAmount":14,"integrationConsumeSetting":null,"memberIntegration":14248,"memberReceiveAddressList":[{"city":"市辖区","defaultStatus":1,"detailAddress":"猪脚街2号","id":168,"latitude":29.0,"longitude":113.0,"memberId":297,"name":"123","phoneNumber":"13146587733","postCode":"","province":"北京市","region":"西城区","storeId":1}],"storeName":""}}
```



### 36、商品详情下单

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/generateOrder
```

#### 示例：


#### 请求方式：
```
POST
```

#### 参数类型： param, query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|content       |N       |string   |下单备注 |
|type          |Y       |int   | 1 商品详情 2 勾选购物车 3全部购物车的商品 |
|orderType     |Y       |int   |1 普通订单 2 秒杀订单 3 团购订单 4 拼团订单 5 积分订单 |
|addressId     |Y       |int   |地址编号 |
|couponId      |N       |int   |优惠券编号 |
|memberCouponId|N       |string   |使用领取的优惠券编号 |


#### 返回示例：

```javascript
{"code":200,"data":{"orderItemList":[{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5692,"integrationAmount":0,"orderId":3923,"orderSn":"15847794686140101297","productAttr":"","productBrand":" 七匹狼","productCategoryId":3309,"productId":25066,"productName":"上衣","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/350xq_7.jpg","productPrice":99.00,"productQuantity":2,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":2,"storeId":0,"storeName":"佳佳百货","type":1},{"couponAmount":0,"giftGrowth":0,"giftIntegration":0,"id":5693,"integrationAmount":0,"orderId":3923,"orderSn":"15847794686140101297","productAttr":"","productBrand":"","productCategoryId":0,"productId":8,"productName":"测试赠品","productPic":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200108/赠品.png","productPrice":2.00,"productQuantity":1,"productSkuCode":"","productSkuId":0,"productSn":"","promotionAmount":0,"promotionName":"","realAmount":0,"sp1":"","sp2":"","sp3":"","status":0,"storeId":0,"storeName":"","type":2}],"order":{"autoConfirmDay":0,"billContent":"","billHeader":"","billReceiverEmail":"","billReceiverPhone":"","billType":0,"blance":0,"commentTime":null,"confirmStatus":0,"couponAmount":0,"couponId":0,"createTime":1584779468614,"deleteStatus":0,"deliveryCompany":"","deliverySn":"","deliveryTime":null,"discountAmount":0,"freightAmount":0,"goodsId":25066,"goodsName":"上衣","groupId":0,"growth":0,"historyList":[],"id":3923,"integration":0,"integrationAmount":0,"isComment":1,"memberId":297,"memberUsername":"13146587722","modifyTime":null,"note":"","orderItemList":[],"orderSn":"15847794686140101297","orderType":1,"payAmount":198.00,"payCode":"","payType":1,"paymentTime":null,"pid":0,"prepayId":"","promotionAmount":0,"promotionInfo":"","receiveTime":null,"receiverCity":"市辖区","receiverDetailAddress":"猪脚街2号","receiverId":168,"receiverName":"123","receiverPhone":"13146587733","receiverPostCode":"","receiverProvince":"北京市","receiverRegion":"西城区","schoolId":0,"sourceType":1,"status":12,"storeId":0,"storeName":"","supplyId":0,"taxCode":"","taxContent":"","taxTitle":"","taxType":1,"totalAmount":198.00,"useIntegration":0}}}
```


### 37、清空购物车

#### 请求URL：
```
http://www.yjlive.cn:8083/api/cart/clear
```

#### 示例：


#### 请求方式：
```
POST
```

#### 参数类型：query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|



#### 返回示例：

```javascript

{"code":200,"data":1}
```

### 38、购物车下单数据预览

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/submitStorePreview
```

#### 示例：


#### 请求方式：
```
GET
```

#### 参数类型：

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|cartIds      |Y       |int   | 购物车id 2508,2507|
|type        |N       |int   | 1商品详情 2购物车 |



#### 返回示例：

```javascript

{
  status: 1,
  success: '退出成功'
}
```



### 39、购物车下单

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/oms/generateOrder
```

#### 示例：


#### 请求方式：
```
POST
```

#### 参数类型： param, query

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|content       |N       |string   |下单备注 |
|type          |Y       |int   | 1 商品详情 2 勾选购物车 3全部购物车的商品 |
|orderType     |Y       |int   |1 普通订单 2 秒杀订单 3 团购订单 4 拼团订单 5 积分订单 |
|addressId     |Y       |int   |地址编号 |
|couponId      |N       |int   |优惠券编号 |
|memberCouponId|N       |string   |使用领取的优惠券编号 |
|cartIds       |Y       |string   |2508,2507 |


#### 返回示例：

```javascript

{
  status: 1,
  data: {
    user_name: "cangdu",
    id: 1,
    create_time: "2017-05-29 12:02",
    status: 1,
    city: "上海",
    avatar: "default.jpg",
    admin: "管理员"
  }
}
```

### 40、商品分类

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/pms/getGoodsTypes
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/pms/getGoodsTypes](http://www.yjlive.cn:8083/api/single/pms/getGoodsTypes)

#### 请求方式：
```
GET
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|




#### 返回示例：

```javascript

{"code":200,"data":[{"childList":[{"childList":[],"description":"","icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200306/IMG_2563.JPG","id":3302,"indexStatus":1,"keywords":"","level":1,"name":"男装","navStatus":1,"parentId":3306,"productAttributeIdList":[],"productCount":0,"productUnit":"","showStatus":1,"sort":0,"storeId":1}],"description":"","icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200313/印章.png","id":3306,"indexStatus":1,"keywords":"空调","level":0,"name":"空调","navStatus":1,"parentId":0,"productAttributeIdList":[],"productCount":0,"productUnit":"","showStatus":1,"sort":0,"storeId":1},{"childList":[],"description":"","icon":"","id":3307,"indexStatus":1,"keywords":"","level":0,"name":"手套","navStatus":1,"parentId":0,"productAttributeIdList":[],"productCount":0,"productUnit":"双","showStatus":1,"sort":0,"storeId":1},{"childList":[{"childList":[],"description":"2","icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/8-8.png","id":3310,"indexStatus":1,"keywords":"2","level":1,"name":"男上衣","navStatus":1,"parentId":3309,"productAttributeIdList":[],"productCount":0,"productUnit":"件","showStatus":1,"sort":0,"storeId":1189},{"childList":[],"description":"袜子","icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/2-2.png","id":3311,"indexStatus":1,"keywords":"袜子","level":1,"name":"袜子","navStatus":1,"parentId":3309,"productAttributeIdList":[],"productCount":0,"productUnit":"条","showStatus":1,"sort":0,"storeId":1189}],"description":"sss","icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200315/6-6.png","id":3309,"indexStatus":1,"keywords":"ss","level":0,"name":"服装","navStatus":1,"parentId":0,"productAttributeIdList":[],"productCount":0,"productUnit":"件","showStatus":1,"sort":0,"storeId":1189},{"childList":[{"childList":[],"description":"","icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20200318/母婴产品@3x.png","id":3314,"indexStatus":1,"keywords":"","level":1,"name":"保健品","navStatus":1,"parentId":3313,"productAttributeIdList":[],"productCount":0,"productUnit":"1","showStatus":1,"sort":0,"storeId":1}],"description":"","icon":"","id":3313,"indexStatus":1,"keywords":"","level":0,"name":"母婴保健","navStatus":1,"parentId":0,"productAttributeIdList":[],"productCount":0,"productUnit":"1","showStatus":1,"sort":0,"storeId":1},{"childList":[],"description":"","icon":"","id":3315,"indexStatus":1,"keywords":"好物","level":0,"name":"生活好物","navStatus":1,"parentId":0,"productAttributeIdList":[],"productCount":0,"productUnit":"","showStatus":1,"sort":0,"storeId":1}]}
```

### 41、用户密码登录

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/home/login
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/home/login](http://www.yjlive.cn:8083/api/single/home/login)

#### 请求方式：
```
POST
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|phone          |Y       |string   | |
|password       |Y       |string   | |



#### 返回示例：

```javascript

{"code":200,"data":{"tokenHead":"Bearer ","userInfo":{"areaId":0,"areaName":"","avatar":"","birthday":null,"blance":640,"buyCount":27,"buyMoney":10917,"city":"","confimpassword":"","createTime":1584412356000,"gender":1,"growth":0,"historyIntegration":0,"icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20200320-8b68497a31c1487097b9affe5e0e9c04.png","id":297,"integration":28699,"invitecode":"","job":"","luckeyCount":0,"memberLevelId":1,"memberLevelName":"砖石会员","nickname":"测试","password":"$2a$10$UHBlIz4wjXhWGo9aWUfPIObAM9CtdYGYOz4W7RcsE.a7m83d0pTfS","personalizedSignature":"","phone":"13146587722","phonecode":"","roomDesc":"","roomNums":"","schoolId":0,"schoolName":"","sourceType":0,"status":1,"storeId":1,"username":"13146587722","weixinOpenid":""},"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzE0NjU4NzcyMiIsImNyZWF0ZWQiOjE1ODQ3ODAzMDUxNDQsImV4cCI6MTcwNDc4MDMwNX0.5WfKUxxdU4ocO7UDX9lJYsgeXkVyzFWUS0FYLZyusQXe3y9LG2TKrekNVenLB1Dy5Ax3gbMY_c8pft7_7ucENA"}}
```
### 42、用户密码注册

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/home/simpleReg
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/home/simpleReg](http://www.yjlive.cn:8083/api/single/home/simpleReg)

#### 请求方式：
```
GET
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|phone               |Y       |string   | |
|password            |Y       |string   | |
|confimpassword      |Y       |string   | |
|invitecode          |N       |string   | 邀请没|


#### 返回示例：

```javascript

{"code":200,"data":null}
```
### 43、获取手机验证码

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/home/sms/codes
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/home/sms/codes](http://www.yjlive.cn:8083/api/single/home/sms/codes)

#### 请求方式：
```
GET
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|phone               |Y       |string   | |



#### 返回示例：

```javascript

{"code":200,"data":{"key":"bff9cf51-afbf-46d0-a4e7-85213fd32823"}}
```
### 44、手机和验证码登录

#### 请求URL：
```
http://www.yjlive.cn:8083/api/single/home/loginByCode
```

#### 示例：
[http://www.yjlive.cn:8083/api/single/home/loginByCode](http://www.yjlive.cn:8083/api/single/home/loginByCode)

#### 请求方式：
```
GET
```

#### 参数类型：param

|参数|是否必选|类型|说明|
|:-----|:-------:|:-----|:-----|
|phone               |Y       |string   | |
|authCode            |Y       |手机验证码   | |


#### 返回示例：

```javascript

{"code":200,"data":{"tokenHead":"Bearer ","userInfo":{"areaId":0,"areaName":"","avatar":"","birthday":null,"blance":640,"buyCount":27,"buyMoney":10917,"city":"","confimpassword":"","createTime":1584412356000,"gender":1,"growth":0,"historyIntegration":0,"icon":"http://yjlive160322.oss-cn-beijing.aliyuncs.com/web-20200320-8b68497a31c1487097b9affe5e0e9c04.png","id":297,"integration":28709,"invitecode":"","job":"","luckeyCount":0,"memberLevelId":1,"memberLevelName":"砖石会员","nickname":"测试","password":"$2a$10$UHBlIz4wjXhWGo9aWUfPIObAM9CtdYGYOz4W7RcsE.a7m83d0pTfS","personalizedSignature":"","phone":"13146587722","phonecode":"","roomDesc":"","roomNums":"","schoolId":0,"schoolName":"","sourceType":0,"status":1,"storeId":1,"username":"13146587722","weixinOpenid":""},"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzE0NjU4NzcyMiIsImNyZWF0ZWQiOjE1ODQ3ODA2NTMxODAsImV4cCI6MTcwNDc4MDY1M30.xl5ViXDle-vWffAiDKFj8fSGl5VZA6_APfu48V5OeE3ZjERVrWuUtZCrCQpKZ8iux7mSn-n5pCQuAmPaOidfAQ"}}
```
