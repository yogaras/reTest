<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css" />
<script src="${pageContext.request.contextPath}/js/menu.js"></script>

<style>
	
	#error-dialog {
		z-index: 10002 !important;
		display: none;
		font-size: 1.1em;
		color: black;
	}
	
	h3 {
		color : white;
	}
	
</style>
<script>

$(document).ready(function() {
	$("#logOutBtn").button();
	$("#logOutBtn").click(function(){
         location.href="${pageContext.request.contextPath}/login.do?method=logout"
        		 });
      $("#log").css({"text-align" : "right" });
});

	$(document).ready(function() {

		$("#cssmenu").menumaker({
			title : "Menu",
			format : "multitoggle"
		});
		
	});

	//에러 메시지 폼인 error-dialog 를 전담하여 보여주는 함수
	function alertError(title, message) {
	
		// error-dialog 보이게 하기
		$("#error-dialog").attr("style", "display:block");

		$("#error-dialog").dialog({   // jqueryUI dialog 위젯 적용
			autoOpen : true,  // 자동으로 열리도록
			modal : true,     // 외부 클릭 못하게
			title : title,   // error-dialog 폼 제목
			width : 'auto',
			height : 'auto',
			position : {    // 폼 열릴 때 위치
				my : "center center",  
				at : "center-120 center-30"   // 폼 열릴 때, 대강 화면 중앙에 오도록
			},
			buttons : {  // 버튼 이벤트 적용
				"확인" : function() {
					$("#error-dialog").attr("style", "display:none");
					$("#error-dialog").dialog("close");
				}
			}
		});
		$("#error-dialog #errorMsg").html(message);

	}
	
	
</script>

<!--  사용자 정보 Div -->
<table style="width: 100%;">
	<tr>
		<td style="padding-left: 20px">
		<h1><font color="white"></font></h1>
		
		<a href="${pageContext.request.contextPath}/hello.html">
		</a>
		</td>
		<td style="padding-left: 567px; text-align: left;">
			<h3>
				WORKPLACE : ${sessionScope.workplaceName} &emsp;&emsp; 
				DEPT : ${sessionScope.deptName} &emsp;&emsp;
			</h3>
			<h3>
				POSITION : ${sessionScope.positionName} &emsp;&emsp; 
				NAME : ${sessionScope.empName} &emsp;&emsp; 
				ID : ${sessionScope.userId} &emsp;&emsp;  
				접속자 : ${userCount} 명 &emsp;&emsp;
				<input type="button" id="logOutBtn" value="로그아웃">
			</h3>
			
			
		</td>
	</tr>
</table>

<!--  메뉴 div -->
<div id="cssmenu">${sessionScope.menuCode}</div>

<!-- 에러폼 div -->
<div id="error-dialog">
	<p id="errorMsg"></p>
</div> 
