package com.estimulo.logistics.logisticsInfo.to;

import com.estimulo.base.to.BaseTO;

public class ItemInfoTO extends BaseTO {

	private String itemCode; 
	private String itemName;
	private String itemGroupCode;
	private String itemClassification;
	private String unitOfStock;
	private String lossRate;
	private String leadTime;
	private int standardUnitPrice;
	private String codeUseCheck;
	private String description;
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemGroupCode() {
		return itemGroupCode;
	}
	public void setItemGroupCode(String itemGroupCode) {
		this.itemGroupCode = itemGroupCode;
	}
	public String getItemClassification() {
		return itemClassification;
	}
	public void setItemClassification(String itemClassification) {
		this.itemClassification = itemClassification;
	}
	public String getUnitOfStock() {
		return unitOfStock;
	}
	public void setUnitOfStock(String unitOfStock) {
		this.unitOfStock = unitOfStock;
	}
	public String getLossRate() {
		return lossRate;
	}
	public void setLossRate(String lossRate) {
		this.lossRate = lossRate;
	}
	public String getLeadTime() {
		return leadTime;
	}
	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}
	public int getStandardUnitPrice() {
		return standardUnitPrice;
	}
	public void setStandardUnitPrice(int standardUnitPrice) {
		this.standardUnitPrice = standardUnitPrice;
	}
	public String getCodeUseCheck() {
		return codeUseCheck;
	}
	public void setCodeUseCheck(String codeUseCheck) {
		this.codeUseCheck = codeUseCheck;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
}
