package com.estimulo.logistics.production.to;

import com.estimulo.base.to.BaseTO;

public class OpenMrpTO extends BaseTO {
	
	private String mpsNo;
	private String bomNo;
	private String itemClassification;
	private String itemCode;
	private String itemName;
	private String orderDate;
	private String requiredDate;
	private String planAmount;
	private String totalLossRate;	 
	private String caculatedAmount;
	private int requiredAmount;
	private String unitOfMrp;
	
	public String getMpsNo() {
		return mpsNo;
	}
	public void setMpsNo(String mpsNo) {
		this.mpsNo = mpsNo;
	}
	public String getBomNo() {
		return bomNo;
	}
	public void setBomNo(String bomNo) {
		this.bomNo = bomNo;
	}
	public String getItemClassification() {
		return itemClassification;
	}
	public void setItemClassification(String itemClassification) {
		this.itemClassification = itemClassification;
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
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(String requiredDate) {
		this.requiredDate = requiredDate;
	}
	public String getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(String planAmount) {
		this.planAmount = planAmount;
	}
	public String getTotalLossRate() {
		return totalLossRate;
	}
	public void setTotalLossRate(String totalLossRate) {
		this.totalLossRate = totalLossRate;
	}
	public String getCaculatedAmount() {
		return caculatedAmount;
	}
	public void setCaculatedAmount(String caculatedAmount) {
		this.caculatedAmount = caculatedAmount;
	}
	public int getRequiredAmount() {
		return requiredAmount;
	}
	public void setRequiredAmount(int requiredAmount) {
		this.requiredAmount = requiredAmount;
	}
	public String getUnitOfMrp() {
		return unitOfMrp;
	}
	public void setUnitOfMrp(String unitOfMrp) {
		this.unitOfMrp = unitOfMrp;
	}
	 
}
