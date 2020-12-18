package com.estimulo.logistics.production.to;

import com.estimulo.base.to.BaseTO;

public class SalesPlanInMpsAvailableTO extends BaseTO {
	 
	 private String salesPlanNo;
	 private String planClassification;
	 private String itemCode;
	 private String itemName;
	 private String unitOfSales;
	 private String salesPlanDate;
	 private String mpsPlanDate;
	 private String scheduledEndDate;
	 private String dueDateOfSales;
	 private String salesAmount;
	 private int unitPriceOfSales;
	 private int sumPriceOfSales;
	 private String mpsApplyStatus;
	 private String description;
	 
	public String getSalesPlanNo() {
		return salesPlanNo;
	}
	public void setSalesPlanNo(String salesPlanNo) {
		this.salesPlanNo = salesPlanNo;
	}
	public String getPlanClassification() {
		return planClassification;
	}
	public void setPlanClassification(String planClassification) {
		this.planClassification = planClassification;
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
	public String getUnitOfSales() {
		return unitOfSales;
	}
	public void setUnitOfSales(String unitOfSales) {
		this.unitOfSales = unitOfSales;
	}
	public String getSalesPlanDate() {
		return salesPlanDate;
	}
	public void setSalesPlanDate(String salesPlanDate) {
		this.salesPlanDate = salesPlanDate;
	}
	public String getMpsPlanDate() {
		return mpsPlanDate;
	}
	public void setMpsPlanDate(String mpsPlanDate) {
		this.mpsPlanDate = mpsPlanDate;
	}
	public String getScheduledEndDate() {
		return scheduledEndDate;
	}
	public void setScheduledEndDate(String scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}
	public String getDueDateOfSales() {
		return dueDateOfSales;
	}
	public void setDueDateOfSales(String dueDateOfSales) {
		this.dueDateOfSales = dueDateOfSales;
	}
	public String getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
	}
	public int getUnitPriceOfSales() {
		return unitPriceOfSales;
	}
	public void setUnitPriceOfSales(int unitPriceOfSales) {
		this.unitPriceOfSales = unitPriceOfSales;
	}
	public int getSumPriceOfSales() {
		return sumPriceOfSales;
	}
	public void setSumPriceOfSales(int sumPriceOfSales) {
		this.sumPriceOfSales = sumPriceOfSales;
	}
	public String getMpsApplyStatus() {
		return mpsApplyStatus;
	}
	public void setMpsApplyStatus(String mpsApplyStatus) {
		this.mpsApplyStatus = mpsApplyStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
}