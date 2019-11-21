package com.mei.zhuang.entity.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author arvato team
 * @since 2019-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("es_shop_goods")
public class EsShopGoods extends Model<EsShopGoods> {

    private static final long serialVersionUID = 1L;

    private Long id;//编号
    @TableField("shop_id")
    private Long shopId;//商铺ID
    @TableField("brand_id")
    private Long branId;//品牌ID
    @TableField("create_time")
    private Date createTime;//创建时间
    private String title;//商品标题(商品名称)
    @TableField("sub_title")
    private String subTitle;//副标题
    @TableField("english_sub_title")
    private String englishSubTitle;//英文副标题
    @TableField("category_id")
    private String categoryId;//分类iD
    @TableField("recom_goods_id")
    private String recomGoodsId;//推荐商品ID
    @TableField("is_show_sales_count")
    private Integer isShowSalesCount;//是否显示销量   1 显示  0 不显示
    @TableField("is_show_stock")
    private Integer isShowStock;//是否显示库存   1 显示  0 不显示

    @TableField("category_name")
    private String categoryName;//分类名称
    private BigDecimal price;//商品现价
    private Integer type;//商品类型 1实物商品 2 虚拟商品 3 赠品  4规则商品 5套餐商品
    private Integer stock;//商品库存
    @TableField("vitural_stock")
    private Integer vituralStock;//商品虚拟库存
    @TableField("vitural_sales")
    private Integer vituralSales;//商品销量（不使用用：salesCount）
    @TableField("goods_code")
    private String goodsCode;//商品编码
    private BigDecimal weight;//商品重量
    private String unit;//商品单位
    private String keyword;//商品关键字
    //@TableField("is_refund")
    //private Integer isRefund;//是否支持退换货
    @TableField("display_order")
    private Integer displayOrder;//显示顺序
    @TableField("sales_count")
    private Integer salesCount;//商品已出售数量(也是销量)
    private String thumb;//商品图
    private String thumbs;//商品图集合
    private String video;//首图视频
    @TableField("product_sn")
    private String productSn;//
    @TableField("promotion_time")
    private String promotionTime ;

    private String channelName;//渠道名称
    @TableField(exist = false)
    private List<EsShopGoodsSpec> specTitleList;

    /**
     * 1 启用商品规格 0 不启用
     */
    @TableField("enable_spec")
    private Integer enableSpec;
    /**
     * 1 前端展示多规格 2 不展示
     */
    @TableField("show_spec")
    private Integer showSpec;

    /**
     * 模板相关
     *
     * @return
     */
    @TableField("template_id")
    private Long templateId;//模板ID
    @TableField("dispatch_type")
    private Integer dispatchType;//模板类型   1.统一模板  0.运费模板
    @TableField("dispatch_status")
    private String dispatchStatus;//模板类型   1.统一模板  0.运费模板
    @TableField("dispatch_price")
    private BigDecimal dispatchPrice;//运费模板价格
    @TableField("dispatch_id")
    private Long dispatchId;//运费模板iD
    private Integer status;//商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
    @TableField("is_refund_support")
    private Integer isRefundSupport;//是否支持退款  0支持  1不支持
    @TableField("stock_cnf")
    private Integer stockCnf;//减库存方式 0 拍下减库存 1 付款减库存 2 永不减库存
    @TableField("is_invoice_support")
    private Integer isInvoiceSupport;//是否支持发票  0支持   1不支持
    private Integer ednum;//单品满件包邮（件数）
    private BigDecimal edmoney;//单品满额包邮（元）
    private String edareas;//不参加包邮的地区
    @TableField("search_show")
    private String searchShow;//主商城是否显示搜索结果   0显示  1不显示

    @TableField("letter_status")
    private Integer letterStatus;//刻字服务状态（1-有，2-没有）
    @TableField("letter_free")
    private Integer letterFree;//刻字服务限时免费（1-是，2-否）
    @TableField("letter_time")
    private String letterTime;//刻字服务限时免费时间
    @TableField("letter_ids")
    private String letterIds;//刻字服务ids
    @TableField("letter_type")
    private Integer letterType;//刻字服务类型
    @TableField("letter_content")
    private Integer letterContent;//样图id

    @TableField("card_status")
    private Integer cardStatus;//卡片状态值（1-有，2-没有）
    @TableField("card_type")
    private Integer cardType;//卡片类型（1-全部卡片，2-制定卡片）
    @TableField("card_content")
    private String cardContent;//卡片ID

    @TableField("packet_status")
    private Integer packetStatus;//封套状态值（1-有，2-没有）
    @TableField("packet_content")
    private String packetContent;//封套ID
    @TableField("packet_type")
    private Integer packetType;//封套类型（1-全部封套，2-制定封套）

    @TableField("pack_box_status")
    private Integer packBoxStatus;//包装盒状态值（1-有，2-没有）
    @TableField("packet_box_content")
    private String packBoxContent;//包装盒ID
    @TableField("packet_box_type")
    private Integer packBoxType;//包装盒类型（1-全部包装盒，2-制定包装盒）

    @TableField("original_cost")
    private BigDecimal originalCost;//商品原价
    @TableField("cost_price")
    private BigDecimal costPrice;//商品成本价
    @TableField("count_postage")
    private String countPostage;//统一邮费
    @TableField("uniform_postage_price")
    private BigDecimal uniformPostagePrice;//统一邮费价格
    @TableField("uniform_postage_status")
    private Integer uniformPostageStatus;//统一邮费状态

    @TableField("is_invoice")
    private Integer isInvoice;//是否需要发票   0否  1是
    @TableField("is_putaway")
    private Integer isPutaway;//是否上架
    @TableField("putaway_type")
    private String putawayType;//上架方式（例如微信小程序）
    @TableField("is_putaway_time")
    private Integer isPutawayTime;//是否定时上架
    @TableField("putaway_time")
    private String putawayTime;//上架时间
    @TableField("is_sales")
    private String isSales;//是否支持退换货
    @TableField("cargo_time")
    private String cargoTime;//收货时间
    @TableField("bar_code")
    private String barCode;//商品条码
    @TableField("rule_content")
    private String ruleContent;//规则信息
    @TableField("is_color_show")
    private String isColorShow;//显示试色(0不显示,1显示)
    @TableField("icon_img")
    private String iconImg;//icon 图片
    @TableField("template_details")
    private String templateDetails;//模板详情
    @TableField("num_max")
    private Integer numMax;//单次最多购买件数
    @TableField("num_min")
    private Integer numMin;//单次最低购买件数
    @TableField("most_purchases")
    private Integer mostPurchases;//最多购买件数
    @TableField("jurisdiction_member_browse")
    private String jurisdictionMemberBrowse;//会员等级浏览权限
    @TableField("jurisdiction_member_purchase")
    private String jurisdictionMemberPurchase;//会员等级购买权限
    @TableField("jurisdiction_labs_browse")
    private String jurisdictionLabsBrowse;//标签组浏览权限
    @TableField("jurisdiction_labs_purchase")
    private String jurisdictionLabsPurchase;//会员标签组购买权限
    @TableField("promotion_title")
    private String promotionTitle;//促销标题
    @TableField("promotion_money")
    private BigDecimal promotionMoney;//会员促销价格
    @TableField("present_integral")
    private String presentIntegral;//赠送积分
    @TableField("return_money")
    private String returnMoney;//余额返现额度
    private String discount;//重复购买折扣(折数)
    @TableField("is_sustained_use")
    private Integer isSustainedUse;//是否持续使用(状态0否,1是)
    @TableField("service_conditions")
    private Integer serviceConditions;//使用条件(1订单付款后 ,0订单完成后)
    @TableField("is_share")
    private String isShare;//与其他优惠共享(0否,1是)
    @TableField("member_discount")
    private String memberDiscount;//会员折扣(折数)
    @TableField("member_discount_money")
    private BigDecimal memberDiscountMoney;//会员折扣元数
    @TableField("card_exclusive")
    private String cardExclusive;//专属卡片
    @TableField("delete_time")
    private Date deleteTime;//删除时间
    @TableField("sellout_time")
    private Date selloutTime;//售罄时间
    @TableField("default_spec")
    private String defaultSpec;//默认规格
    @TableField("pay_by_post")
    private Integer payByPost;//是否付邮使用
    @TableField(exist = false)
    private Integer current = 1;
    @TableField(exist = false)
    private Integer size = 10;
    @TableField(exist = false)
    private String idAndName;//商品关键字
    @TableField(exist = false)
    private String ids;//商品关键字
    @TableField(exist = false)
    private List<String> categoryNames;

    /**
     * 商品推荐
     *
     * @return
     */
    private String recomType;//商品推荐类型（1.按商品分类   0.按单品）
    @TableField("recom_category_name")
    private String recomCategoryName;//商品分类名称
    @TableField("recom_category_num")
    private Integer recomCategoryNum;//显示格式

    @TableField("small_beauty_box_id")
    private Long smallBeautyBoxId;//小美盒定制礼盒编号

    private Integer fabulous;//点赞



    @TableField(exist = false)
    private List<EsShopGoodsRecom> recomList;
    @TableField(exist = false)
    private List<EsShopGoodsCateMap> goodsCateMapList;
    @TableField(exist = false)
    private List<EsShopGoodsCategory> goodsCategoryList;
    @TableField(exist = false)
    private List<EsShopGoodsSpec> specs;
    @TableField(exist = false)
    private List<EsShopGoodsSpecItem> specItems;
    @TableField(exist = false)
    private List<EsShopGoodsOption> options;
    @TableField(exist = false)
    private List<EsShopGoods> recomGoodsList;
    @TableField(exist = false)
    private List<EsShopDiypage> diypageList;
    @TableField(exist = false)
    private EsShopGoodsDiyPageMap diypageALl;
    @TableField(exist = false)
    private Long diyPageId;//模板编号
    @TableField(exist = false)
    private String data;
    @TableField(exist = false)
    private Integer total;
    @TableField(exist = false)
    private EsShopGoodsOption option;
    @TableField(exist = false)
    private String groupId;

    @Override
    protected Serializable pkVal() {
        return id;
    }


}
