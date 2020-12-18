package com.estimulo.authorityManager.controller;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.estimulo.authorityManager.exception.*;
import com.estimulo.authorityManager.serviceFacade.AuthorityManagerServiceFacade;
import com.estimulo.authorityManager.serviceFacade.AuthorityManagerServiceFacadeImpl;
import com.estimulo.common.servlet.ModelAndView;
import com.estimulo.common.servlet.controller.MultiActionController;
import com.estimulo.hr.to.EmpInfoTO;

public class MemberLogInController extends MultiActionController {

	private static Logger logger = LoggerFactory.getLogger(MemberLogInController.class);

	private static AuthorityManagerServiceFacade authorityManagerSF = AuthorityManagerServiceFacadeImpl.getInstance();

	public ModelAndView LogInCheck(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("MemberLogInController :  LogInCheck");
		}

		String viewName = null;
		HashMap<String, Object> model = new HashMap<String, Object>();

		HttpSession session = request.getSession();

		String companyCode = request.getParameter("companyCode"); //COM-01
		String workplaceCode = request.getParameter("workplaceCode"); //BRC-01
		String inputId = request.getParameter("userId"); //1111
		String inputPassWord = request.getParameter("userPassWord"); //1111

		try {
			if (companyCode.equals("") || workplaceCode.equals("") || inputId.equals("") || inputPassWord.equals("")) {
				throw new DataNotInputException("데이터가 입력되지 않았습니다.");
			}

			EmpInfoTO TO = authorityManagerSF.accessToAuthority(companyCode, workplaceCode, inputId, inputPassWord);

			if (TO != null) {

				ServletContext application = request.getServletContext();	// 로그인 성공 했을 경우 그사람의 정보를 클라이언트가 종료되기전 까지 저장 해준다
				session.setAttribute("userId", TO.getUserId()); //유저ID
				session.setAttribute("empCode", TO.getEmpCode()); //사원번호
				session.setAttribute("empName", TO.getEmpName()); //사원이름
				session.setAttribute("deptCode", TO.getDeptCode()); //부서코드
				session.setAttribute("deptName", TO.getDeptName()); //부서명
				session.setAttribute("positionCode", TO.getPositionCode()); //직급코드
				session.setAttribute("positionName", TO.getPositionName()); //직급명
				session.setAttribute("companyCode", TO.getCompanyCode()); //회사코드
				session.setAttribute("workplaceCode", workplaceCode); //사업장코드
				session.setAttribute("workplaceName", TO.getWorkplaceName()); //사업장명
							
				System.out.println("		@EmpInfo : deptCode : "+TO.getDeptCode());
				System.out.println("		@EmpInfo : PositionCode : "+TO.getPositionCode());
				String menuCode = authorityManagerSF.getUserMenuCode(workplaceCode, TO.getDeptCode(),
						TO.getPositionCode(), application);
				session.setAttribute("menuCode", menuCode); //MENU - URI 맵핑 DB Data

				viewName = "redirect:" + request.getContextPath() + "/hello.html";
				System.out.println("로그인성공");
			}

		} catch (DataNotInputException e1) {
			e1.printStackTrace();
			model.put("errorCode", -1);
			model.put("errorMsg", e1.getMessage());
			viewName = "loginform";
		} catch (IdNotFoundException e2) {
			e2.printStackTrace();
			model.put("errorCode", -2);
			model.put("errorMsg", e2.getMessage());
			viewName = "loginform";
		} catch (PwNotFoundException e3) {
			e3.printStackTrace();
			model.put("errorCode", -3);
			model.put("errorMsg", e3.getMessage());
			viewName = "loginform";
		} catch (PwMissMatchException e4) {
			e4.printStackTrace();
			model.put("errorCode", -4);
			model.put("errorMsg", e4.getMessage());
			viewName = "loginform";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("errorCode", -5);
			model.put("errorMsg", e.getMessage());
			viewName = "loginform";
		}

		ModelAndView modelAndView = new ModelAndView(viewName, model);

		if (logger.isDebugEnabled()) {
			logger.debug("MemberLogInController :  LogInCheck");
		}
		return modelAndView;
	}

	
	
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().invalidate();
		String viewName = "loginform";
		ModelAndView modelAndView = new ModelAndView(viewName, null);

		return modelAndView;
	}

}