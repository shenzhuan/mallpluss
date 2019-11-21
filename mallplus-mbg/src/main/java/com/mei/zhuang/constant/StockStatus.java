package com.mei.zhuang.constant;

public enum StockStatus {

		STOCK_ENOUGH(1),//库存充足
		STOCK_LESS_THAN_BUYCOUNT(-1),//库存状态正常，库存量小于购买量
		STOCK_STATUS_FAULT(0),//库存状态为 “缺货”
	    NO_STOCK(-2),//无库存
		OVER_LIMIT(-3);//超过限购数量

		private int value;
		private StockStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

}
