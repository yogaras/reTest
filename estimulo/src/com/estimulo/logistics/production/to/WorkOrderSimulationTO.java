package com.estimulo.logistics.production.to;

public class WorkOrderSimulationTO {

	private String mrpNo;
	private String mpsNo;
	private String mrpGatheringNo;
	private String itemClassification;
	private String itemCode;
	private String itemName;
	private String unitOfMrp;
	private String inputAmount;
	private String requiredAmount;
	private String stockAfterWork;
	private String orderDate;
	private String requiredDate;
	
	public String getMrpNo() {
		return mrpNo;
	}
	public void setMrpNo(String mrpNo) {
		this.mrpNo = mrpNo;
	}
	public String getMpsNo() {
		return mpsNo;
	}
	public void setMpsNo(String mpsNo) {
		this.mpsNo = mpsNo;
	}
	public String getMrpGatheringNo() {
		return mrpGatheringNo;
	}
	public void setMrpGatheringNo(String mrpGatheringNo) {
		this.mrpGatheringNo = mrpGatheringNo;
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
	public String getUnitOfMrp() {
		return unitOfMrp;
	}
	public void setUnitOfMrp(String unitOfMrp) {
		this.unitOfMrp = unitOfMrp;
	}
	public String getInputAmount() {
		return inputAmount;
	}
	public void setInputAmount(String inputAmount) {
		this.inputAmount = inputAmount;
	}
	public String getRequiredAmount() {
		return requiredAmount;
	}
	public void setRequiredAmount(String requiredAmount) {
		this.requiredAmount = requiredAmount;
	}
	public String getStockAfterWork() {
		return stockAfterWork;
	}
	public void setStockAfterWork(String stockAfterWork) {
		this.stockAfterWork = stockAfterWork;
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
	
}
