package com.estimulo.logistics.purchase.to;

public class OrderInfoTO {
	
	private String orderNo;
	private String orderDate;
	private String orderInfoStatus;
	private String orderSort;
	private String itemCode;
	private String itemName;
	private String orderAmount;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderInfoStatus() {
		return orderInfoStatus;
	}
	public void setOrderInfoStatus(String orderInfoStatus) {
		this.orderInfoStatus = orderInfoStatus;
	}
	public String getOrderSort() {
		return orderSort;
	}
	public void setOrderSort(String orderSort) {
		this.orderSort = orderSort;
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
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	
}
