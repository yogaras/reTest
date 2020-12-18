package com.estimulo.logistics.production.to;

import com.estimulo.base.to.BaseTO;

public class InputMaterialsTO extends BaseTO {
	 private String inputItemNo;
	 private String productionResultNo;
	 private String description;
	 private String itemCode;
	 private String unitOfInputMaterials;
	 private String warehouseCode;
	 private String itemName;
	 private int inputAmount;
	 private String inputDate;

	 public String getInputItemNo() { 
		 return inputItemNo;
	 }
	 public void setInputItemNo(String inputItemNo) { 
		 this.inputItemNo = inputItemNo;
	 }
	 public String getProductionResultNo() { 
		 return productionResultNo;
	 }
	 public void setProductionResultNo(String productionResultNo) { 
		 this.productionResultNo = productionResultNo;
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
	 public String getUnitOfInputMaterials() { 
		 return unitOfInputMaterials;
	 }
	 public void setUnitOfInputMaterials(String unitOfInputMaterials) { 
		 this.unitOfInputMaterials = unitOfInputMaterials;
	 }
	 public String getWarehouseCode() { 
		 return warehouseCode;
	 }
	 public void setWarehouseCode(String warehouseCode) { 
		 this.warehouseCode = warehouseCode;
	 }
	 public String getItemName() { 
		 return itemName;
	 }
	 public void setItemName(String itemName) { 
		 this.itemName = itemName;
	 }
	 public int getInputAmount() { 
		 return inputAmount;
	 }
	 public void setInputAmount(int inputAmount) { 
		 this.inputAmount = inputAmount;
	 }
	 public String getInputDate() { 
		 return inputDate;
	 }
	 public void setInputDate(String inputDate) { 
		 this.inputDate = inputDate;
	 }
}