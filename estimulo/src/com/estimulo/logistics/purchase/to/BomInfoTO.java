package com.estimulo.logistics.purchase.to;

import com.estimulo.base.to.BaseTO;

public class BomInfoTO extends BaseTO {
	
	private String itemCode;
	private String parentItemCode;
	private int no;
	private String itemName;
	private String itemClassification;
	private String itemClassificationName;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemClassification() {
		return itemClassification;
	}
	public void setItemClassification(String itemClassification) {
		this.itemClassification = itemClassification;
	}
	public String getItemClassificationName() {
		return itemClassificationName;
	}
	public void setItemClassificationName(String itemClassificationName) {
		this.itemClassificationName = itemClassificationName;
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
