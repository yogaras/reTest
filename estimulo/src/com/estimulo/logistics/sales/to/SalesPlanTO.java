package com.estimulo.logistics.sales.to;

import com.estimulo.base.to.BaseTO;

public class SalesPlanTO extends BaseTO {
	 private int unitPriceOfSales;
	 private int salesAmount;
	 private String salesPlanNo;
	 private String description;
	 private String salesPlanDate;
	 private int sumPriceOfSales;
	 private String itemCode;
	 private String dueDateOfSales;
	 private String unitOfSales;
	 private String mpsApplyStatus;
	 private String itemName;

	 public int getUnitPriceOfSales() { 
		 return unitPriceOfSales;
	 }
	 public void setUnitPriceOfSales(int unitPriceOfSales) { 
		 this.unitPriceOfSales = unitPriceOfSales;
	 }
	 public int getSalesAmount() { 
		 return salesAmount;
	 }
	 public void setSalesAmount(int salesAmount) { 
		 this.salesAmount = salesAmount;
	 }
	 public String getSalesPlanNo() { 
		 return salesPlanNo;
	 }
	 public void setSalesPlanNo(String salesPlanNo) { 
		 this.salesPlanNo = salesPlanNo;
	 }
	 public String getDescription() { 
		 return description;
	 }
	 public void setDescription(String description) { 
		 this.description = description;
	 }
	 public String getSalesPlanDate() { 
		 return salesPlanDate;
	 }
	 public void setSalesPlanDate(String salesPlanDate) { 
		 this.salesPlanDate = salesPlanDate;
	 }
	 public int getSumPriceOfSales() { 
		 return sumPriceOfSales;
	 }
	 public void setSumPriceOfSales(int sumPriceOfSales) { 
		 this.sumPriceOfSales = sumPriceOfSales;
	 }
	 public String getItemCode() { 
		 return itemCode;
	 }
	 public void setItemCode(String itemCode) { 
		 this.itemCode = itemCode;
	 }
	 public String getDueDateOfSales() { 
		 return dueDateOfSales;
	 }
	 public void setDueDateOfSales(String dueDateOfSales) { 
		 this.dueDateOfSales = dueDateOfSales;
	 }
	 public String getUnitOfSales() { 
		 return unitOfSales;
	 }
	 public void setUnitOfSales(String unitOfSales) { 
		 this.unitOfSales = unitOfSales;
	 }
	 public String getMpsApplyStatus() { 
		 return mpsApplyStatus;
	 }
	 public void setMpsApplyStatus(String mpsApplyStatus) { 
		 this.mpsApplyStatus = mpsApplyStatus;
	 }
	 public String getItemName() { 
		 return itemName;
	 }
	 public void setItemName(String itemName) { 
		 this.itemName = itemName;
	 }
}