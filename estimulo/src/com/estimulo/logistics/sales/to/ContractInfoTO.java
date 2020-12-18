package com.estimulo.logistics.sales.to;

import java.util.ArrayList;

import com.estimulo.base.to.BaseTO;

public class ContractInfoTO extends BaseTO {

	private String contractNo;
	private String estimateNo;
	private String contractType;
	private String contractTypeName;
	private String customerCode;
	private String customerName;
	private String estimateDate;
	private String contractDate;
	private String contractRequester;
	private String personCodeInCharge;
	private String empNameInCharge;
	private String description;
	private ArrayList<ContractDetailTO> contractDetailTOList;
	private String deliveryCompletionStatus;
	
	public String getDeliveryCompletionStatus() {
		return deliveryCompletionStatus;
	}

	public void setDeliveryCompletionStatus(String deliveryCompletionStatus) {
		this.deliveryCompletionStatus = deliveryCompletionStatus;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getEstimateNo() {
		return estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractTypeName() {
		return contractTypeName;
	}

	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(String estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getContractDate() {
		return contractDate;
	}

	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	public String getContractRequester() {
		return contractRequester;
	}

	public void setContractRequester(String contractRequester) {
		this.contractRequester = contractRequester;
	}

	public String getPersonCodeInCharge() {
		return personCodeInCharge;
	}

	public void setPersonCodeInCharge(String personCodeInCharge) {
		this.personCodeInCharge = personCodeInCharge;
	}

	public String getEmpNameInCharge() {
		return empNameInCharge;
	}

	public void setEmpNameInCharge(String empNameInCharge) {
		this.empNameInCharge = empNameInCharge;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<ContractDetailTO> getContractDetailTOList() {
		return contractDetailTOList;
	}

	public void setContractDetailTOList(ArrayList<ContractDetailTO> contractDetailTOList) {
		this.contractDetailTOList = contractDetailTOList;
	}

}
