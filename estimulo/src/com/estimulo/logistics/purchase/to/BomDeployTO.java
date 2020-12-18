package com.estimulo.logistics.purchase.to;

import com.estimulo.base.to.BaseTO;

public class BomDeployTO extends BaseTO {
	
	private String bomNo;
	private int bomLevel;
	private String parentItemCode;
	private String itemCode;
	private String itemName;
	private String unitOfStock;
	private int netAmount;
	private String lossRate;
	private String necessaryAmount;	
	private String leadTime;
	private String isLeaf;
	private String description;
	
	public String getBomNo() {
		return bomNo;
	}
	public void setBomNo(String bomNo) {
		this.bomNo = bomNo;
	}
	public int getBomLevel() {
		return bomLevel;
	}
	public void setBomLevel(int bomLevel) {
		this.bomLevel = bomLevel;
	}
	public String getParentItemCode() {
		return parentItemCode;
	}
	public void setParentItemCode(String parentItemCode) {
		this.parentItemCode = parentItemCode;
	}
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
	public String getUnitOfStock() {
		return unitOfStock;
	}
	public void setUnitOfStock(String unitOfStock) {
		this.unitOfStock = unitOfStock;
	}
	public int getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(int netAmount) {
		this.netAmount = netAmount;
	}
	public String getLossRate() {
		return lossRate;
	}
	public void setLossRate(String lossRate) {
		this.lossRate = lossRate;
	}
	public String getNecessaryAmount() {
		return necessaryAmount;
	}
	public void setNecessaryAmount(String necessaryAmount) {
		this.necessaryAmount = necessaryAmount;
	}
	public String getLeadTime() {
		return leadTime;
	}
	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
