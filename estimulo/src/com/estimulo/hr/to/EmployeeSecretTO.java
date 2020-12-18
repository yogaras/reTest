package com.estimulo.hr.to;

import com.estimulo.base.to.BaseTO;

public class EmployeeSecretTO extends BaseTO {
	
	 private String companyCode;
	 private String empCode;
	 private int seq;
	 private String userPassword;

	 public String getUserPassword() { 
		 return userPassword;
	 }
	 public void setUserPassword(String userPassword) { 
		 this.userPassword = userPassword;
	 }
	 public String getCompanyCode() { 
		 return companyCode;
	 }
	 public void setCompanyCode(String companyCode) { 
		 this.companyCode = companyCode;
	 }
	 public String getEmpCode() { 
		 return empCode;
	 }
	 public void setEmpCode(String empCode) { 
		 this.empCode = empCode;
	 }
	 public int getSeq() { 
		 return seq;
	 }
	 public void setSeq(int seq) { 
		 this.seq = seq;
	 }
}