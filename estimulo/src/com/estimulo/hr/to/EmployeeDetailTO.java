package com.estimulo.hr.to;

import com.estimulo.base.to.BaseTO;

public class EmployeeDetailTO extends BaseTO {
	 private String companyCode;
	 private String empCode;
	 private int seq;
	 private String updateHistory;
	 private String updateDate;
	 private String workplaceCode;
	 private String deptCode;
	 private String positionCode;
	 private String userId;
	 private String phoneNumber;
	 private String email;
	 private String zipCode;
	 private String basicAddress;
	 private String detailAddress;
	 private String levelOfEducation;
	 private String image;

	 public String getUpdateHistory() { 
		 return updateHistory;
	 }
	 public void setUpdateHistory(String updateHistory) { 
		 this.updateHistory = updateHistory;
	 }
	 public String getUpdateDate() { 
		 return updateDate;
	 }
	 public void setUpdateDate(String updateDate) { 
		 this.updateDate = updateDate;
	 }
	 public String getDeptCode() { 
		 return deptCode;
	 }
	 public void setDeptCode(String deptCode) { 
		 this.deptCode = deptCode;
	 }
	 public String getPositionCode() { 
		 return positionCode;
	 }
	 public void setPositionCode(String positionCode) { 
		 this.positionCode = positionCode;
	 }
	 public String getZipCode() { 
		 return zipCode;
	 }
	 public void setZipCode(String zipCode) { 
		 this.zipCode = zipCode;
	 }
	 public String getUserId() { 
		 return userId;
	 }
	 public void setUserId(String userId) { 
		 this.userId = userId;
	 }
	 public String getLevelOfEducation() { 
		 return levelOfEducation;
	 }
	 public void setLevelOfEducation(String levelOfEducation) { 
		 this.levelOfEducation = levelOfEducation;
	 }
	 public String getEmpCode() { 
		 return empCode;
	 }
	 public void setEmpCode(String empCode) { 
		 this.empCode = empCode;
	 }
	 public String getEmail() { 
		 return email;
	 }
	 public void setEmail(String email) { 
		 this.email = email;
	 }
	 public String getDetailAddress() { 
		 return detailAddress;
	 }
	 public void setDetailAddress(String detailAddress) { 
		 this.detailAddress = detailAddress;
	 }
	 public String getImage() { 
		 return image;
	 }
	 public void setImage(String image) { 
		 this.image = image;
	 }
	 public String getWorkplaceCode() { 
		 return workplaceCode;
	 }
	 public void setWorkplaceCode(String workplaceCode) { 
		 this.workplaceCode = workplaceCode;
	 }
	 public String getBasicAddress() { 
		 return basicAddress;
	 }
	 public void setBasicAddress(String basicAddress) { 
		 this.basicAddress = basicAddress;
	 }
	 public String getCompanyCode() { 
		 return companyCode;
	 }
	 public void setCompanyCode(String companyCode) { 
		 this.companyCode = companyCode;
	 }
	 public String getPhoneNumber() { 
		 return phoneNumber;
	 }
	 public void setPhoneNumber(String phoneNumber) { 
		 this.phoneNumber = phoneNumber;
	 }
	 public int getSeq() { 
		 return seq;
	 }
	 public void setSeq(int seq) { 
		 this.seq = seq;
	 }
}