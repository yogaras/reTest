package com.estimulo.logistics.purchase.to;

import com.estimulo.base.to.BaseTO;

public class BomTO extends BaseTO {

	private String itemCode;
	private String parentItemCode;
	private int no;
	private int netAmount;
	private String description;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getParentItemCode() {
		return parentItemCode;
	}

	public void setParentItemCode(String parentItemCode) {
		this.parentItemCode = parentItemCode;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(int netAmount) {
		this.netAmount = netAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}