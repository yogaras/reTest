package com.estimulo.logistics.sales.to;

import com.estimulo.base.to.BaseTO;


public class EstimateDetailTO extends BaseTO {
	private String unitOfEstimate;
	private String estimateNo;
	private int unitPriceOfEstimate;
	private String estimateDetailNo;
	private int sumPriceOfEstimate;
	private String description;
	private String itemCode;
	private int estimateAmount;
	private String dueDateOfEstimate;
	private String itemName;
	public String getUnitOfEstimate() {
		return unitOfEstimate;
	}

	public void setUnitOfEstimate(String unitOfEstimate) {
		this.unitOfEstimate = unitOfEstimate;
	}

	public String getEstimateNo() {
		return estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

	public int getUnitPriceOfEstimate() {
		return unitPriceOfEstimate;
	}

	public void setUnitPriceOfEstimate(int unitPriceOfEstimate) {
		this.unitPriceOfEstimate = unitPriceOfEstimate;
	}

	public String getEstimateDetailNo() {
		return estimateDetailNo;
	}

	public void setEstimateDetailNo(String estimateDetailNo) {
		this.estimateDetailNo = estimateDetailNo;
	}

	public int getSumPriceOfEstimate() {
		return sumPriceOfEstimate;
	}

	public void setSumPriceOfEstimate(int sumPriceOfEstimate) {
		this.sumPriceOfEstimate = sumPriceOfEstimate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public int getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(int estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public String getDueDateOfEstimate() {
		return dueDateOfEstimate;
	}

	public void setDueDateOfEstimate(String dueDateOfEstimate) {
		this.dueDateOfEstimate = dueDateOfEstimate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}