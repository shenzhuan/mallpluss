package com.mei.zhuang.vo.order;

import com.mei.zhuang.entity.goods.EsShopGoods;

import java.math.BigDecimal;
import java.util.List;

public class CouponFilterParam {

	private Long account;
	private List<EsShopGoods> goodsList;

	private BigDecimal orderAmount;

	public Long getAccount() {
		return account;
	}

	public void setAccount(Long account) {
		this.account = account;
	}

	public List<EsShopGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<EsShopGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

}
