package com.estimulo.hr.to;

import com.estimulo.base.to.BaseTO;

public class EmployeeBasicTO extends BaseTO {
	 private String companyCode;
	 private String empCode;
	 private String empName;
	 private String empEngName;
	 private String socialSecurityNumber;
	 private String hireDate;
	 private String retirementDate;
	 private String userOrNot;
	 private String birthDate;
	 private String gender;

	 public String getSocialSecurityNumber() { 
		 return socialSecurityNumber;
	 }
	 public void setSocialSecurityNumber(String socialSecurityNumber) { 
		 this.socialSecurityNumber = socialSecurityNumber;
	 }
	 public String getEmpName() { 
		 return empName;
	 }
	 public void setEmpName(String empName) { 
		 this.empName = empName;
	 }
	 public String getEmpEngName() { 
		 return empEngName;
	 }
	 public void setEmpEngName(String empEngName) { 
		 this.empEngName = empEngName;
	 }
	 public String getHireDate() { 
		 return hireDate;
	 }
	 public void setHireDate(String hireDate) { 
		 this.hireDate = hireDate;
	 }
	 public String getRetirementDate() { 
		 return retirementDate;
	 }
	 public void setRetirementDate(String retirementDate) { 
		 this.retirementDate = retirementDate;
	 }
	 public String getCompanyCode() { 
		 return companyCode;
	 }
	 public void setCompanyCode(String companyCode) { 
		 this.companyCode = companyCode;
	 }
	 public String getBirthDate() { 
		 return birthDate;
	 }
	 public void setBirthDate(String birthDate) { 
		 this.birthDate = birthDate;
	 }
	 public String getGender() { 
		 return gender;
	 }
	 public void setGender(String gender) { 
		 this.gender = gender;
	 }
	 public String getEmpCode() { 
		 return empCode;
	 }
	 public void setEmpCode(String empCode) { 
		 this.empCode = empCode;
	 }
	 public String getUserOrNot() { 
		 return userOrNot;
	 }
	 public void setUserOrNot(String userOrNot) { 
		 this.userOrNot = userOrNot;
	 }
}