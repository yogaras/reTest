package com.estimulo.logistics.sales.dao;

import java.util.ArrayList;

import com.estimulo.logistics.sales.to.EstimateTO;

public interface EstimateDAO {
	public ArrayList<EstimateTO> selectEstimateList(String dateSearchCondition, String startDate, String endDate);

	public EstimateTO selectEstimate(String estimateNo);

	public int selectEstimateCount(String estimateDate);

	public void insertEstimate(EstimateTO TO);

	public void updateEstimate(EstimateTO TO);

	public void changeContractStatusOfEstimate(String estimateNo, String contractStatus);

}
