package com.estimulo.logistics.logisticsInfo.to;

import com.estimulo.base.to.BaseTO;

public class ItemTO extends BaseTO {
	 private String itemGroupCode;
	 private String leadTime;
	 private String unitOfStock;
	 private int standardUnitPrice;
	 private String description;
	 private String itemCode;
	 private String itemClassification;
	 private String lossRate;
	 private String itemName;

	 public String getItemGroupCode() { 
		 return itemGroupCode;
	 }
	 public void setItemGroupCode(String itemGroupCode) { 
		 this.itemGroupCode = itemGroupCode;
	 }
	 public String getLeadTime() { 
		 return leadTime;
	 }
	 public void setLeadTime(String leadTime) { 
		 this.leadTime = leadTime;
	 }
	 public String getUnitOfStock() { 
		 return unitOfStock;
	 }
	 public void setUnitOfStock(String unitOfStock) { 
		 this.unitOfStock = unitOfStock;
	 }
	 public int getStandardUnitPrice() { 
		 return standardUnitPrice;
	 }
	 public void setStandardUnitPrice(int standardUnitPrice) { 
		 this.standardUnitPrice = standardUnitPrice;
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
	 public String getItemClassification() { 
		 return itemClassification;
	 }
	 public void setItemClassification(String itemClassification) { 
		 this.itemClassification = itemClassification;
	 }
	 public String getLossRate() { 
		 return lossRate;
	 }
	 public void setLossRate(String lossRate) { 
		 this.lossRate = lossRate;
	 }
	 public String getItemName() { 
		 return itemName;
	 }
	 public void setItemName(String itemName) { 
		 this.itemName = itemName;
	 }
}