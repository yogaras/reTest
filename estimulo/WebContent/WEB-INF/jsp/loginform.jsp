<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>First Project</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/loginform_util.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/loginform_main.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/loginform_modified.css" />
<script src="${pageContext.request.contextPath}/scripts/jquery/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery.jqGrid.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/codeModal.js"></script>
<style>
input[type=text], input[type=password] {
	display: inline;
	width: 350px;
	padding-left: 1%;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 50px;
}

.center {
	text-align : center;
}

.ui-jqgrid .ui-jqgrid-hdiv {
	font-size: 0.8em;
	height: 33px;
}

.ui-jqgrid .ui-widget-header {
	height: 33px;
	font-size: 0.9em;
}

.ui-jqgrid .ui-jqgrid-bdiv {
	overflow-x: auto;
	overflow-y: scroll;
}

body {
	background-image: url("${pageContext.request.contextPath}/scripts/images/732.jpg");
	background-size: cover;
	background-attachment: scroll;
}

#insertSource {
	margin-top: 12%;
	margin-left: 40%;
}
</style>
<script>
	

<%session.invalidate();%>
	// session 초기화
	// 여기서는 안쓰지만, 그냥 넣은 변수들
	var lastSelected_CompanyCodeGrid_Id; // 회사코드 grid 에서 마지막 선택한 row 의 id
	var lastSelected_WorkplaceCodeGrid_Id; // 사업장코드 grid 에서 마지막 선택한 row 의 id
	var lastSelected_CompanyCodeGrid_RowValue; // 회사코드 grid 에서 마지막 선택한 row 의 data
	var lastSelected_WorkplaceCodeGrid_RowValue; // 사업장코드 grid 에서 마지막 선택한 row 의 data
	
	function showModalCode(codeName,divisionCode){
    	var option="width=550; height=430; left=500; top=200; titlebar=no; toolbar=no,status=no,menubar=no,resizable=yes, location=no";
    	window.open("${pageContext.request.contextPath}/basicInfo/codeModal.html?code="+codeName+"&divisionCode="+divisionCode,"newwins",option);
	}
	
	$(document).ready(function() {
		// 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
		var userInputId = getCookie("userInputId");
		$("input[name='userId']").val(userInputId);

		if ($("input[name='userId']").val() != "") { // 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
			$("#ckb1").attr("checked", true); // ID 저장하기를 체크 상태로 두기.
			}
		
				$("#ckb1").change(function() { // 체크박스에 변화가 있다면,
				if ($("#ckb1").is(":checked")) { // ID 저장하기 체크했을 때,
				var userInputId = $("input[name='userId']").val();
				setCookie("userInputId",userInputId, 7); // 7일 동안 쿠키 보관
				} else { // ID 저장하기 체크 해제 시,
				deleteCookie("userInputId");
				}
				});
				
				// ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
				$("input[name='userId']").keyup(function() { // ID 입력 칸에 ID를 입력할 때,
				if ($("#ckb1").is(":checked")) { // ID 저장하기를 체크한 상태라면,
				var userInputId = $("input[name='userId']").val();
				setCookie("userInputId",userInputId, 7); // 7일 동안 쿠키 보관
				}
				});

				// jqueryUI 버튼 위젯 적용
				$("input[type=button], input[type=submit], input[type=reset]").button(); 

				// MemberLogInController 의 LogInCheck 메서드에서 보낸 errorCode 가 음수인 경우, 즉 로그인 실패
				if ("${requestScope.errorCode}" < 0) {
				alertError("로그인 에러","로그인을 실패하였습니다. </br> ${requestScope.errorMsg}");
				} 
				else{
				// $("#error-dialog").attr("style", "display:none");
				}
				// 회사코드, 사업장코드 보여주는 dialog-form 에 jqueryUI dialog 위젯 적용
				$("#dialog-form").dialog({
				title : '코드 검색',
				autoOpen : false, // 자동으로 열리지 않게
				width : 630,
				height : 570,
				modal : true, // 폼 외부 클릭 못하게
				buttons : { // 버튼 이벤트 적용
				"확인" : function() {
				// 회사코드 grid 표시된 상태
				if ($("#companyCodeGridDiv").css("display") == 'block') {			
					// id : companyCodeGrid 에서 선택된 로우의 id		
					var id = $("#companyCodeGrid").jqGrid(
					'getGridParam','selrow'
					);
				
					if (id) { // id 가 존재하면, 즉 선택된 로우가 있을 때 
					// companyCodeGrid 에서 선택된 로우의 data
					var rowValue = $("#companyCodeGrid").jqGrid('getRowData',id);
					// 선택된 로우의 회사코드 값을 companyCode 텍스트박스에 넣기
					// rowValue -- {"companyCode":"COM-01","companyName":"(주)세계전자","companyDivision":"법인","businessLicenseNumber":"1208125847"}
					$("#companyCode").val(rowValue.companyCode);
					$("#dialog-form").dialog("close"); // 폼 닫기
					} else { // id 없음 : 즉, 선택된 로우가 없으면
					alertError("입력 에러",
					"회사를 선택한 후 확인 버튼을 눌러주세요 ~_~");
					}
					// 사업장코드 grid 표시된 상태	
				} else if ($("#workplaceCodeGridDiv").css("display") == 'block') {
					var id = $("#workplaceCodeGrid").jqGrid('getGridParam','selrow');
					
					if (id) {
						var rowValue = $("#workplaceCodeGrid").jqGrid('getRowData',id);
						$("#workplaceCode").val(rowValue.workplaceCode);
						$("#dialog-form").dialog("close");} 
					else {alertError("입력 에러","사업장을 선택한 후 확인 버튼을 눌러주세요 ~_~");}
					}
					},
					"취소" : function() {
					$("#dialog-form").dialog("close");
					}
					}
					});
						initEvent(); // 이벤트 적용 함수
						showCompanyCodeGrid(); // 회사 코드 grid 세팅 함수
					});

	
	function setCookie(cookieName, value, exdays) {
		var exdate = new Date(); //현재 시간과 날짜
		exdate.setDate(exdate.getDate() + exdays);
		var cookieValue = escape(value) //* , ; 등 특수기호를 제외한 문자 인코딩
				+ ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());//toGMTString():현지 시간을 그리니티 표준시간으로 변환
		console.log(cookieValue);												//Sat, 09 May 2020 02:42:25 GMT
		document.cookie = cookieName + "=" + cookieValue; //쿠키생성
	}

	function deleteCookie(cookieName) {
		var expireDate = new Date();
		expireDate.setDate(expireDate.getDate() - 1);
		document.cookie = cookieName + "= " + "; expires="
				+ expireDate.toGMTString();
	}

	function getCookie(cookieName) {
		cookieName = cookieName + '=';
		var cookieData = document.cookie;	//name=value ; userInputId=1111	
		var start = cookieData.indexOf(cookieName); // 0을 반환
		var cookieValue = '';
		if (start != -1) {
			start += cookieName.length; //userInputId= , 12
			var end = cookieData.indexOf(';', start); //-1
			if (end == -1)
				end = cookieData.length;
			cookieValue = cookieData.substring(start, end);
		}
		return unescape(cookieValue);
	}

	// $(document).ready(function(){
	// initEvent();
	// }) 이렇게 되어있다.
	function initEvent() {
		$("#companyCodeSearchButton").on("mouseover" , function(){
			$("#companyCodeSearchButton").css("background","skyblue")
			});
		$("#workplaceCodeSearchButton").on("mouseover" , function(){
			$("#workplaceCodeSearchButton").css("background","skyblue")
			});
		$("#logInButton").on("mouseover" , function(){
			$("#logInButton").css("background","skyblue")
			});
		
		$("#resetButton").on("mouseover" , function(){
			$("#resetButton").css("background","skyblue")
			});
		
		$("#apiButton").on("mouseover" , function(){
			$("#apiButton").css("background","skyblue")
			});
		
	
		
		$("input[type=button], input[type=submit], input[type=reset]").on("mouseout" , function(){
			$("input[type=button], input[type=submit], input[type=reset]").css("background","#D5D5D5")
			});
		
		
		
		
		
		
		
		
	
		// 회사코드 검색 버튼 클릭시
		$("#companyCodeSearchButton").on("click", function() {
			/* var option="width=550; height=430; left=500; top=200; titlebar=no; toolbar=no,status=no,menubar=no,resizable=yes, location=no";
			window.open("${pageContext.request.contextPath}/basicInfo/codeModal.html?code=companyCode","newwins",option);				 
				 */
			showModalCode("companyCode");
			//$("#companyCodeGridDiv").show();			
			//$("#workplaceCodeGridDiv").hide(); // 사업장 코드 grid 있는 div 숨김
			//$("#openapiGridDiv").hide(); // 회사코드 grid 있는 div 숨기고,			
			//$("#dialog-form").dialog("open"); // 코드 검색 창 열기
		});

		// 사업장코드 검색 버튼 클릭시
		$("#workplaceCodeSearchButton").on("click", function() {

			// companyCode 텍스트박스에 입력된 값이 없음 : 사업장 검색 불가능 => 에러 띄우기
			if ($("#companyCode").val() == "") {
				alertError("입력 에러", "회사 코드를 먼저 입력하세요 ~_~");

			} else {
				/* var option="width=550; height=430; left=500; top=200; titlebar=no; toolbar=no,status=no,menubar=no,resizable=yes, location=no";
				window.open("${pageContext.request.contextPath}/basicInfo/codeModal.html?code=workplaceCode","newwins",option);				  */
				showModalCode("workplaceCode");
				/*$("#openapiGridDiv").hide(); // 회사코드 grid 있는 div 숨기고,
				$("#companyCodeGridDiv").hide(); // 회사코드 grid 있는 div 숨기고,
				$("#workplaceCodeGridDiv").show(); // 사업장 grid 코드 있는 div 보여주기

				// 사업장 코드 grid 보여주기
				showWorkplaceCodeGrid();

				$("#dialog-form").dialog("open"); // 코드 검색 창 열기*/
			}
		});
		
		//미세먼지수치확인클릭시
		$("#apiButton").on("click", function() {
			
			$("#companyCodeGridDiv").hide(); // 회사코드 grid 있는 div 보여주고,
			$("#workplaceCodeGridDiv").hide(); // 사업장 코드 grid 있는 div 숨김
			$("#openapiGridDiv").show(); // 고 grid 있는 div 숨기고,
			openapiCodeGrid();
			$("#dialog-form").dialog("open"); // 코드 검색 창 열기
		});
		

	}

	// 에러 메시지 폼인 error-dialog 를 전담하여 보여주는 함수
	function alertError(title, message) {

		// error-dialog 보이게 하기
		$("#error-dialog").attr("style", "display:block");

		$("#error-dialog").dialog({ // jqueryUI dialog 위젯 적용
			autoOpen : true, // 자동으로 열리도록
			modal : true, // 외부 클릭 못하게
			title : title, // error-dialog 폼 제목
			width : 'auto',
			height : 'auto',
			position : { // 폼 열릴 때 위치
				my : "center center",
				at : "center-70 center-50" // 폼 열릴 때, 대강 화면 중앙에 오도록
			},
			buttons : { // 버튼 이벤트 적용
				"확인" : function() {
					$("#error-dialog").attr("style", "display:none");
					$("#error-dialog").dialog("close");
				}
			}
		});
		


		// error-dialog 안의 errorMsg p 태그에 에러 메시지 적용
		$("#error-dialog #errorMsg").html(message);
	}
	
	
		// $(document).ready(function(){
		// showCompanyCodeGrid();	
		// }) 이렇게 되어있다.
	function showCompanyCodeGrid() {
		
		// 회사코드 ajax 시작
	$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/basicInfo/searchCompany.do',
			data : {

				// MultiActionController : 여기서는 MemberLoginController 의 searchCompanyCode 메서드 호출
				method : 'searchCompanyList',
			},
			
			
			dataType : 'json',
			cache : false, //저장을 하지 않겠다
			success : function(dataSet) {
				console.log(dataSet);
				//gridRowJson : 그리드에 넣을 json 형식의 data	
				var gridRowJson = dataSet.gridRowJson; // 회사코드 grid 시작
				//alert(JSON.stringify(gridRowJson[0].companyTelNumber));
				//alert();
				$('#companyCodeGrid').jqGrid(
						{
							mtype : 'POST',
							datatype : 'local',
							colNames : [ "회사코드", " 회사명", " 회사구분", " 사업자번호" ],
							colModel : [ {
								name : "companyCode",
								width : "90",
								resizable : true,
								align : "center"
							}, {
								name : "companyName",
								width : "200",
								resizable : true,
								align : "center"
							}, {
								name : "companyDivision",
								width : "90",
								resizable : true,
								align : "center"
							}, {
								name : "businessLicenseNumber",
								width : "120",
								resizable : true,
								align : "center"
							}, ],
							caption : '회사코드 검색', //그리드 상단제목
							sortname : 'companyCode', //정렬 대상
							multiselect : false,
							multiboxonly : false,
							viewrecords : true, //화면 하단에 총 몇개중에서 몇번째꺼를 보여주고 있는지에 대한 문자열을 표시할것인가에 대한 설정
							rownumWidth : 30, //줄번호 size
							height : 300,
							width : 580,
							autowidth : false,
							shrinkToFit : true, //true인경우 그리드 width에 맞춰짐
							cellEdit : false,
							rowNum : 10, // -1 : 모든 로우 한번에 표시, 그런데 잘 안먹히는 경우 많음
							scrollerbar : true,
							//rowList : [ 10, 20, 30 ],
							viewrecords : true, 
							editurl : 'clientArray', //'clientArray'로 설정하면 데이터는 서버에 요청을 보내지 않고 이벤트를 통한 처리를 위해 그리드에만 저장된다.
							cellsubmit : 'clientArray', //셀의 컨텐츠가 저장될 위치를 지정한다.
							rownumbers : true, //true 설정 시 그리드의 왼쪽에 새로운 컬럼이 추가된다.이 컬럼의 목적은 1부터 시작하는 사용가능한 row의 숫자를 카운트
							autoencode : true, //서버에서 가져온 데이터 인코딩여부
							resizable : true, //컬럼의 resize 여부
							loadtext : '로딩중...',
							emptyrecords : '데이터가 없습니다.',
							cache : false,

							// grid 의 로우 선택시 이벤트 : 여기서는 안쓰지만, 그냥 적용해 봤음
							onSelectRow : function(id) {
								if (lastSelected_CompanyCodeGrid_Id != id) {
									lastSelected_CompanyCodeGrid_Id = id;
									lastSelected_CompanyCodeGrid_RowValue = $(
											'#companyCodeGrid').jqGrid(
											'getRowData', id);
								}
							}
						}); // 회사코드 grid 끝
				showCompanyCodeAjax(dataSet);
				// 회사코드 Data 넣기
				$('#companyCodeGrid').jqGrid('setGridParam', {
					datatype : 'local', //local=array data
					data : dataSet
				}).trigger('reloadGrid');

			}
		}); // 회사코드 ajax 끝*/
	}
 
	/*function showWorkplaceCodeGrid() {

		$.jgrid.gridUnload("#workplaceCodeGrid"); // 사업장 코드 grid 완전 초기화

		// 사업장코드 ajax 시작
		$.ajax({
					type : 'POST',
					url : '${pageContext.request.contextPath}/basicInfo/searchWorkplace.do',
					data : {
						companyCode : $("#companyCode").val(), // 주의, 변수의 값을 넘길 때는 "" 나 '' 있으면 안됨!!

						// MultiActionController : 여기서는 MemberLoginController 의 searchWorkplaceCode 메서드 호출
						method : 'searchWorkplaceList'
					},
					dataType : 'json',
					cache : false,
					success : function(dataSet) {
						console.log(dataSet);
						var gridRowJson = dataSet.gridRowJson;
					
						// 사업장코드 grid 시작
						$('#workplaceCodeGrid').jqGrid({
											mtype : 'POST',
											datatype : 'local',
											colNames : [ "회사코드", " 사업장코드",
													" 사업장명", " 사업장번호" ],
											colModel : [ {
												name : "companyCode",
												width : "90",
												resizable : true,
												align : "center"
											}, {
												name : "workplaceCode",
												width : "110",
												resizable : true,
												align : "center"
											}, {
												name : "workplaceName",
												width : "200",
												resizable : true,
												align : "center"
											}, {
												name : "businessLicenseNumber",
												width : "110",
												resizable : true,
												align : "center"
											} ],
											caption : '사업장코드 검색',
											sortname : 'workplaceCode',
											multiselect : false,
											multiboxonly : false,
											viewrecords : true,
											rownumWidth : 30,
											height : 300,
											width : 580,
											autowidth : false,
											shrinkToFit : true,
											cellEdit : false, // true가되어야 수정가능하다
											rowNum : 10,
											scrollerbar : true,
											// rowList : [ 10, 20, 30 ],
											viewrecords : true,
											editurl : 'clientArray',
											cellsubmit : 'clientArray',
											rownumbers : true,
											autoencode : true,
											resizable : true,
											loadtext : '로딩중...',
											emptyrecords : '데이터가 없습니다.',
											cache : false,
											pager : '#workplaceCodeGridPager', // pager 적용시에는 반드시 이 옵션 있어야 함
											onSelectRow : function(id) {
												if (lastSelected_WorkplaceCodeGrid_Id != id) {
													lastSelected_WorkplaceCodeGrid_Id = id;
													lastSelected_WorkplaceCodeGrid_RowValue = $(
															'#workplaceCodeGrid')
															.jqGrid(
																	'getRowData',
																	id);
												}
											}
										}); // 사업장코드 grid 끝

						// 사업장코드 pager 설정
						$('#workplaceCodeGrid').navGrid(
								'#workplaceCodeGridPager', {
									add : false,
									del : false,
									edit : false,
									search : true,
									refresh : true,
									view : true
								});

						// 사업장코드 Data 넣기
						$('#workplaceCodeGrid').jqGrid('setGridParam', {
							datatype : 'local',
							data : gridRowJson
							}).trigger('reloadGrid');

					}
				}); // 사업장코드 ajax 끝

	}*/
	function openapiCodeGrid() {
	
		$.jgrid.gridUnload("#openapiCodeGrid"); // 공공api 코드 grid 완전 초기화

		// 공공api ajax시작
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/openapi.do',
			dataType : 'json',
			cache : false, //저장을 하지 않겠다
				success : function(obj) {
				let dataSet=JSON.parse(obj.gridRowJson);
						let gridRowJson = dataSet.items.item;
						console.log(gridRowJson)
			
/*   @테스트용 주석 
alert("openapiCodeGrid 진입확인");
alert(JSON.stringify(gridRowJson));
 */						
 
						// 공공api grid 시작
						$('#openapiCodeGrid').jqGrid({
											mtype : 'POST',
											datatype : 'local',
											colNames : [   " 사상자수",
													"사망자수", "중상자수" , "경상자수" ],
											colModel : [ 
												 {
												name : "line_string",
												width : "400",
												resizable : true,
												align : "center"
											}, {
												name : "anals_value",
												width : "110",
												resizable : true,
												align : "center"
											}, {
												name : "anals_grd",
												width : "110",
												resizable : true,
												align : "center"
											},{
												name : "sl_dnv_cnt",
												width : "110",
												resizable : true,
												align : "center"
											}
											],
											caption : '서울특별시 강동구 ',
											sortname : 'districtName',
											multiselect : false,
											multiboxonly : false,
											viewrecords : true,
											rownumWidth : 50,
											height : 450,
											width : 580,
											autowidth : true,
											shrinkToFit : true,
											cellEdit : false, // true가되어야 수정가능하다
											rowNum : 10,
											scrollerbar : true,
											// rowList : [ 10, 20, 30 ],
											viewrecords : true,
											editurl : 'clientArray',
											cellsubmit : 'clientArray',
											rownumbers : true,
											autoencode : true,
											resizable : true,
											loadtext : '로딩중...',
											emptyrecords : '데이터가 없습니다.',
											cache : false,
											pager : '#openapiGridPager', // pager 적용시에는 반드시 이 옵션 있어야 함
										}); // 공공api grid 끝

						// 공공api pager 설정
						$('#openapiCodeGrid').navGrid(
								'#openapiCodeGridPager', {
									add : false,
									del : false,
									edit : false,
									search : true,
									refresh : true,
									view : true
								});

						// 공공api Data 넣기
						$('#openapiCodeGrid').jqGrid('setGridParam', {
							datatype : 'local',
							data : gridRowJson
							}).trigger('reloadGrid');

					}
				}); // 공공 ajax 끝

	}
	
	
	
/* 	
	function showapi() {

		// 공공api ajax 시작
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/openapi.do',
			dataType : 'json',
			cache : false, //저장을 하지 않겠다
			success : function(obj) {
				let o=JSON.parse(obj.gridRowJson);
				
				
		alert(o.list[0]);
			}
		}); // 회사코드 ajax 끝
	}
	 */
</script>
</head>
<body>

	<div id="insertSource">
		<h1 style="margin-left: 115px">
			<font color="white">1st Project</font>
		</h1>
		<br>
		<form method="post" action="${pageContext.request.contextPath}/login.do?method=LogInCheck">
			
			<h3><input type="text" placeholder="회사코드" class="center" name="companyCode" id="companyCode"> 
			&nbsp; 
			<input type="button" value="회사코드검색" id="companyCodeSearchButton" class="ev"><br></h3>
			
			<h3><input type="text" placeholder="사업장코드" class="center" name="workplaceCode" id="workplaceCode"> 
			&nbsp; 
			<input type="button" value="사업장코드검색" id="workplaceCodeSearchButton" class="ev"><br></h3>
			
			<input type="text" placeholder="사원ID" class="center" name="userId"><br>
			<input type="password" placeholder="비밀번호" class="center" name="userPassWord"><br>
			<h3>
			<input type="submit" value="로그인" id="logInButton" class="ev" style="margin-left: 15px">
			<input type="reset" value="초기화" id="resetButton" class="ev">
			<input id="ckb1" type="checkbox" name="rmb"> 
			<label for="ckb1"><font color="white">ID 기억하기</font></label>
			<br/><br/>
			<input type="reset" value="서울시 강동구 교통사고 인명피해 정보" id="apiButton" class="ev"></h3>
		
		
		
		</form>
	</div>
	<!-- 코드 grid 보여주는 div -->
	<div id="dialog-form">

		<div id="companyCodeGridDiv">
			<table id="companyCodeGrid"></table>
			<!-- 회사 코드  보여주는 grid 적용 -->
		</div>

		<div id="workplaceCodeGridDiv">
			<table id="workplaceCodeGrid"></table>
			<!-- 사업장 코드  보여주는 grid 적용-->
			<div id="workplaceCodeGridPager"></div>
		</div>





		<div id="openapiGridDiv">
			<table id="openapiCodeGrid"></table>
			<!-- 공공  보여주는 grid 적용-->
			<div id="openapiGridPager"></div>
		</div>

	</div>

	<!-- 에러 메시지 보여주는 div -->
	<div id="error-dialog" style="display: none;">
		<p id="errorMsg" style="font-size: 1.1em; color: black"></p>
	</div>
</body>
</html>