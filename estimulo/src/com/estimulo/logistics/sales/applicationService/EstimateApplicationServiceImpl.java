package com.estimulo.logistics.sales.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.common.exception.DataAccessException;
import com.estimulo.logistics.sales.dao.EstimateDAO;
import com.estimulo.logistics.sales.dao.EstimateDAOImpl;
import com.estimulo.logistics.sales.dao.EstimateDetailDAO;
import com.estimulo.logistics.sales.dao.EstimateDetailDAOImpl;
import com.estimulo.logistics.sales.to.EstimateDetailTO;
import com.estimulo.logistics.sales.to.EstimateTO;

public class EstimateApplicationServiceImpl implements EstimateApplicationService {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(EstimateApplicationServiceImpl.class);

	// 싱글톤
	private static EstimateApplicationService instance = new EstimateApplicationServiceImpl();

	private EstimateApplicationServiceImpl() {
	}

	public static EstimateApplicationService getInstance() {

		if (logger.isDebugEnabled()) {
			logger.debug("@ EstimateApplicationService 객체접근");
		}

		return instance;
	}

	// 참조변수 선언
	private static EstimateDAO estimateDAO = EstimateDAOImpl.getInstance();
	private static EstimateDetailDAO estimateDetailDAO = EstimateDetailDAOImpl.getInstance();

	public ArrayList<EstimateTO> getEstimateList(String dateSearchCondition, String startDate, String endDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : getEstimateList 시작");
		}

		ArrayList<EstimateTO> estimateTOList = null;

		try {

			estimateTOList = estimateDAO.selectEstimateList(dateSearchCondition, startDate, endDate);
			
			for (EstimateTO bean : estimateTOList) {

				bean.setEstimateDetailTOList(estimateDetailDAO.selectEstimateDetailList(bean.getEstimateNo()));

			}
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : getEstimateList 종료");
		}
		return estimateTOList;
	}

	public ArrayList<EstimateDetailTO> getEstimateDetailList(String estimateNo) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : getEstimateDetailList 시작");
		}

		ArrayList<EstimateDetailTO> estimateDetailTOList = null;

		try {

			estimateDetailTOList = estimateDetailDAO.selectEstimateDetailList(estimateNo);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : getEstimateDetailList 종료");
		}
		return estimateDetailTOList;

	}

	public String getNewEstimateNo(String estimateDate) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : getNewEstimateNo 시작");
		}

		StringBuffer newEstimateNo = null;

		try {

			int i = estimateDAO.selectEstimateCount(estimateDate); //1

			newEstimateNo = new StringBuffer();
			newEstimateNo.append("ES");
			newEstimateNo.append(estimateDate.replace("-", "")); //ES20200422
			newEstimateNo.append(String.format("%02d", i));		   //ES2020042201<-01이 붙는다  format("%02d", i) 내용은 왼족부터 0을 붙히고 숫자크기를 2칸 (MAX 99) 으로 지정 그러면 왼쪽에는 0 이 붙고 i = 1 이니까 01이 되는것 이다 
			System.out.println("newEstimateNo = " + newEstimateNo);// ES + estimateDate(-뺀거) + 날짜같은거 카운팅숫자
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : getNewEstimateNo 종료");
		}
		return newEstimateNo.toString();
	}

	public HashMap<String, Object> addNewEstimate(String estimateDate, EstimateTO newEstimateBean) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : addNewEstimate 시작");
		}

		HashMap<String, Object> resultMap = null;

		try {

			String newEstimateNo = getNewEstimateNo(estimateDate);		//ES2020042201
			System.out.println("newEstimateNo = " + newEstimateNo);
			// 새로운 견적일련번호 생성

			newEstimateBean.setEstimateNo(newEstimateNo);
			// 뷰단에서 보내온 견적 Bean 에 새로운 견적일련번호 set

			estimateDAO.insertEstimate(newEstimateBean);
			// 견적 Bean 을 Insert
			ArrayList<EstimateDetailTO> estimateDetailTOList = newEstimateBean.getEstimateDetailTOList();
			// 견적상세 List
			StringBuffer newEstimateDetailNo = null;

			int i = 1;
			//견적상세 bean
			for (EstimateDetailTO bean : estimateDetailTOList) {

				// 앞서 생성된 견적 일련번호 set
				bean.setEstimateNo(newEstimateNo);

				// ( 앞에서 생성한 견적일련번호-01 ,02 , 03 .... ) 형식으로 견적상세일련번호 생성
				newEstimateDetailNo = new StringBuffer();

				newEstimateDetailNo.append(newEstimateNo);
				newEstimateDetailNo.append("-");
				newEstimateDetailNo.append(String.format("%02d", i++)); // 두자리 숫자, INSERT 될 때마다 하나씩 증가    ES2020010401-01,ES20200104-02....

				// 생성된 견적상세 일련번호 set
				bean.setEstimateDetailNo(newEstimateDetailNo.toString());

			}

			// 견적상세List 를 batchListProcess 로 Insert, 결과 맵 반환
			resultMap = batchEstimateDetailListProcess(estimateDetailTOList);

			// 결과 맵에 "estimateNo" 키값으로 새로 생성된 견적일련번호 저장
			resultMap.put("newEstimateNo", newEstimateNo);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : addNewEstimate 종료");
		}
		return resultMap;
		// 새로 생성된 견적일련번호,견적상세일련번호를 저장
	}

	public HashMap<String, Object> batchEstimateDetailListProcess(ArrayList<EstimateDetailTO> estimateDetailTOList) {

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : batchEstimateDetailListProcess 시작");
		}

		HashMap<String, Object> resultMap = new HashMap<>();

		// 추가/수정/삭제된 견적상세일련번호를 담을 ArrayList
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		try {

			// 첫 번째 반복문 : INSERT 만 먼저 처리 => DELETE 를 먼저 하면 새로운 번호가 기존에 매겨졌던 번호로 매겨질 수 있음,
			// UPDATE 는 상관없음
			for (EstimateDetailTO bean : estimateDetailTOList) {

				String status = bean.getStatus();

				// 새로운 견적상세일련번호를 담을 StringBuffer
				StringBuffer newEstimateDetailNo = new StringBuffer();
				if (logger.isDebugEnabled()) {
					logger.debug("EstimateApplicationServiceImpl : batchEstimateDetailListProcess 시작"+bean.getEstimateDetailNo());
				}
				switch (status) {

				case "INSERT":

					// 견적상세 일련번호 양식 : "ES20180101-01"
					// 기존에 등록된 견적에서 새로운 견적상세를 입력하는 경우 = > 견적상세일련번호가 '저장시 지정됨' => "ES" 로 시작하지 않음
					if (!bean.getEstimateDetailNo().startsWith("ES")) {

						// 견적상세 Bean 의 견적번호로 등록된 견적상세번호 중 마지막 번호 + 1 을 DAO 로 가져옴 : 새로운 견적상세번호
						int newNo = estimateDetailDAO.selectEstimateDetailCount(bean.getEstimateNo());

						// 새로운 견적상세일련번호 생성
						newEstimateDetailNo.append(bean.getEstimateNo());
						newEstimateDetailNo.append("-");
						newEstimateDetailNo.append(String.format("%02d", newNo));

						// 견적상세일련번호 set
						bean.setEstimateDetailNo(newEstimateDetailNo.toString());
					}

					estimateDetailDAO.insertEstimateDetail(bean);
					insertList.add(bean.getEstimateDetailNo());		//ES2020010301-01 <= 이다 -01 은 첫번째 견적이라서 -01 이다.
					break;

				}

			}

			// 두 번째 반복문 : UPDATE, DELETE 처리

			for (EstimateDetailTO bean : estimateDetailTOList) {

				String status = bean.getStatus();

				switch (status) {

				case "UPDATE":

					estimateDetailDAO.updateEstimateDetail(bean);
					updateList.add(bean.getEstimateDetailNo());
					break;

				case "DELETE":

					estimateDetailDAO.deleteEstimateDetail(bean);
					deleteList.add(bean.getEstimateDetailNo());

					break;

				default:

				}

			}

			resultMap.put("INSERT", insertList);
			resultMap.put("UPDATE", updateList);
			resultMap.put("DELETE", deleteList);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("EstimateApplicationServiceImpl : batchEstimateDetailListProcess 종료");
		}
		return resultMap;

	}

}
