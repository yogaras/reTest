<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>수주 조회/수정</title>
<style>

#startDatePicker, #endDatePicker{
	display: inline;
	width: 125px;
	padding-left: 1%;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none; 
	height: 30px;
	font-size: 20px;
}


#searchCustomerBox {
	display: inline;
	width: 150px;
	padding-left: 1%;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 18px;
}
legend {
	color: #FFFFFF;
}
.ui-datepicker{
	z-index: 9999 !important;
}

.ui-dialog { 
	z-index: 9999 !important ; 
	font-size:12px;
}

</style>
<script>

var lastSelected_contractGrid_Id;   // 가장 나중에 선택한 수주 grid 의 행 id 
var lastSelected_contractGrid_RowValue;   // 가장 나중에 선택한 수주 grid 의 행 값 

var lastSelected_contractDetailGrid_Id;    // 가장 나중에 선택한 수주상세 grid 의 행 id 
var lastSelected_contractDetailGrid_RowValue;   // 가장 나중에 선택된 (수정되기 전) 수주상세 grid 의 행 값 

var gridRowJson;  // 모든 그리드의 통합 Json Data => 배열 형식 : [ { ... } , { ... } , { ... } , ... ]

var searchCondition;  // 기간 검색 : 'searchByDate' , 거래처 검색 : 'searchByCustomer' 값으로 설정됨

var previousCellValue;  // 수정 가능한 셀에서 수정 전의 셀 값 
var resultList = [];  // 최종적으로 컨트롤러로 보내는 JS 객체 배열 

function showModalCode(codeName,divisionCode){
    var option="width=550; height=430; left=500; top=200; titlebar=no; toolbar=no,status=no,menubar=no,resizable=yes, location=no";
    window.open("${pageContext.request.contextPath}/basicInfo/codeModal.html?code="+codeName+"&divisionCode="+divisionCode,"newwins",option);

}

$(document).ready(function() {
	 
	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	$( "input[type=radio]" ).checkboxradio();   // jqueryUI Checkboxradio 위젯 적용

	$.datepicker.setDefaults({
		dateFormat : 'yy-mm-dd',
		prevText : '이전 달',
		nextText : '다음 달',
		monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월',
									'8월', '9월', '10월', '11월', '12월' ],
		monthNamesShort : [ '1월', '2월', '3월', '4월', '5월', '6월',
											'7월', '8월', '9월', '10월', '11월', '12월' ],
		dayNames : [ '일', '월', '화', '수', '목', '금', '토' ],
		dayNamesShort : [ '일', '월', '화', '수', '목', '금', '토' ],
		dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ],
		showMonthAfterYear : true,
		yearSuffix : '년'
	}); /*한글로 달력 보이기 위해서 */

	$("#startDatePicker").datepicker({
		//defaultDate : "+5d",
		changeMonth : true,
		numberOfMonths : 1,
		onClose : function(selectedDate) {
			$( "#endDatePicker" ).datepicker( "option", "minDate", selectedDate );
		}
	});		
	
	$("#endDatePicker").datepicker({
		changeMonth : true,
		numberOfMonths : 1
	});		
	
	$("#codeDialog").hide();
	$('#searchCustomerBox').hide();
	$("#InputDialog").hide();
	
	initGrid();
	initEvent();
	
});

function initGrid() {
	
	// 수주 그리드 시작
	$('#contractGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "수주일련번호", "견적일련번호", "수주유형분류", "거래처코드", "거래처명", "견적일자", "수주일자", 
			"수주요청자", "수주담당자명", "비고" ,"납품완료여부", "contractType","personCodeInCharge"], 
		colModel : [ 
			{ name: "contractNo", width: "100", resizable: true, align: "center"} ,
			{ name: "estimateNo", width: "100", resizable: true, align: "center"} ,
			{ name: "contractTypeName", width: "100", resizable: true, align: "center"} ,
			{ name: "customerCode", width: "80", resizable: true, align: "center", hidden : true } ,
			{ name: "customerName", width: "120", resizable: true, align: "center"} ,
			{ name: "contractDate", width: "90", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d', defaultValue:null }  } ,
			{ name: "contractDate", width: "90", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d', defaultValue:null }  } ,
			{ name: "contractRequester", width: "90", resizable: true, align: "center" } ,
			{ name: "empNameInCharge", width: "100", resizable: true, align: "center" } ,
			{ name: "description", width: "150", resizable: true, align: "center" } ,
			{ name: "deliveryCompletionStatus", width: "80", resizable: true, align: "center" } ,
			{ name: "contractType", width: "100", resizable: true, align: "center" , hidden : true },
			{ name: "personCodeInCharge", width: "100", resizable: true, align: "center" , hidden : true }

		], 
		caption : '수주',  //그리드 명
		sortname : 'contractNo', //정렬
		multiselect : false, //다중선택
		multiboxonly : false,
		viewrecords : false, //화면 하단에 총 몇개중에서 몇번째꺼를 보여주고 있는지에 대한 문자열을 표시할것인가에 대한 설정이다. 오른쪽하단표시
		rownumWidth : 30, 
		height : 100, 
		width : 1000,
		autowidth : true, // 자동으로 칼럼사이즈 조절 
		shrinkToFit : false,   /* RowWidth 고정 */
		cellEdit : false,
		rowNum : 50,   // -1 : 모든 로우 한번에 표시, 그런데 잘 안먹히는 경우 많음
		scrollerbar: true, 
		rowList : [ 10, 20, 30 ],
		viewrecords : true, 
		editurl : 'clientArray', 
		cellsubmit : 'clientArray',
		rownumbers : true, 
		autoencode : true, 
		resizable : true,
		loadtext : '로딩중...', 
		emptyrecords : '데이터가 없습니다.', 
		cache : false, 
		pager : '#contractGridPager',
		
        onSelectRow: function(rowid) {
			
			if( lastSelected_contractGrid_Id != rowid ){
			
				lastSelected_contractGrid_Id = rowid;
				lastSelected_contractGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			/* 데이터확인용
			alert("rowid : "+rowid+" , lastSelected_contractGrid_RowValue : "+lastSelected_contractGrid_RowValue);
				alert(JSON.stringify(lastSelected_contractGrid_RowValue));
				 */
				
			}
			
			showContractDetailGrid();
			 
		}

	}); // 수주 그리드 끝
	
	$('#contractGrid').navGrid("#contractGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	
	

	// 수주상세 그리드 시작
	$('#contractDetailGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ 
			"선택","수주상세일련번호","수주일련번호","품목코드","품목명","단위","납기일",
			"수주수량","재고사용량","필요제작수량","단가","합계액","처리상태","작업완료여부","납품완료여부","비고","상태","상태2"
		], 
		colModel : [ 
			{ name: "check", width: "50", resizable: true, align: "center" ,
				formatter : function (celvalue, icol, rowObj) {
				     var chk = "<input type='checkbox' name='chk' />";     
				     return chk; 
				}
			},
			{ name: "contractDetailNo", width: "110", resizable: true, align: "center"} ,
			{ name: "contractNo", width: "110", resizable: true, align: "center" } ,
			{ name: "itemCode", width: "70", resizable: true, align: "center" } ,
			{ name: "itemName", width: "120", resizable: true, align: "center"} ,
			{ name: "unitOfContract", width: "40", resizable: true, align: "center"} ,
			{ name: "dueDateOfContract", width: "90", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' }  } ,
			{ name: "estimateAmount", width: "70", resizable: true, align: "center",
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "stockAmountUse", width: "80", resizable: true, align: "center", 
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "productionRequirement", width: "80", resizable: true, align: "center", 
			        formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } , 
			{ name: "unitPriceOfContract", width: "80", resizable: true, align: "center", 
		        formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
      		} , 
      		{ name: "sumPriceOfContract", width: "80", resizable: true, align: "center", 
		        formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
      		} , 
      		{ name: "processingStatus", width: "60", resizable: true, align: "center" } ,
			{ name: "operationCompletedStatus", width: "60", resizable: true, align: "center" } ,
			{ name: "deliveryCompletionStatus", width: "60", resizable: true, align: "center" } ,
			{ name: "description", width: "80", resizable: true, align: "center" } ,
			{ name: "status", width: "80", resizable: true, align: "center" } ,
			{ name: "beforeStatus", width: "10", resizable: true, align: "center" , hidden: true} 
		], 
		caption : '수주상세', 
		sortname : 'contractDetailNo', 
		multiselect : false, 
		multiboxonly : true, //트루일시 체크박스  외 선택시 선택해제 
		viewrecords : true, 
		rownumWidth : 30, 
		height : 100, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 50,  
		scrollerbar: true, 
		rowList : [ 10, 20, 30 ],
		viewrecords : true, 
		editurl : 'clientArray',  //값 수정후 엔터치면 지정된 url로 날라감, 지금 이값은 editurl 안써짐
		cellsubmit : 'clientArray', //특정이벤트 실행시 데이터저장
		rownumbers : true, 
		autoencode : true, 
		resizable : true,
		loadtext : '로딩중...', 
		emptyrecords : '데이터가 없습니다.', 
		cache : false, 
		pager : '#contractDetailGridPager',
		beforeEditCell(rowid, cellname, value, iRow, iCol){ //편집가능한 셀에만 적용, 값의 유효성을 검사하기전에 호출된다.

			if( lastSelected_contractDetailGrid_Id != rowid ){
				lastSelected_contractDetailGrid_Id = rowid;
				lastSelected_contractDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
        	if(value == null || value == "" ) {
        		previousCellValue = " ";	
        	} else {
        		previousCellValue = value;
        	}
		},

		afterSaveCell(rowid, cellname, value, iRow, iCol){ //편집가능한 셀에만적용, 셀이 성공적으로 저장된 후 호출된다.

        	var status = $(this).getCell(rowid,"status");
        
        	if(status == 'DELETE') {
        	
        		alertError("사용자 에러", "삭제 예정인 행이었습니다 ^^ </br> 원래 값으로 돌릴께요");
    			$(this).setCell(rowid,cellname, previousCellValue);	

        	}  else if(status == 'NORMAL') {
        		
        		if( previousCellValue != value ) {
    				$(this).setCell(rowid,"status", "UPDATE");
        		}
        		
        	}
		},
       
		
		
		beforeSelectRow : function( rowid, event ) {
			
        	var beforeStatus = $(this).getCell(rowid,"beforeStatus");
        	var currentStatus = $(this).getCell(rowid,"status");

        	if($(event.target).is(":checkbox")) {
            	alert("beforeSelectRow 진입확인 rowid : "+rowid+", event : "+JSON.stringify(event));
                if($(event.target).is(":checked")) {
                	alert("beforeSelectRow 조건문진입확인");
        			$(this).setCell(rowid,"status", "DELETE");
        			
        			$(this).setCell(rowid,"beforeStatus", currentStatus);
                } else {
        			$(this).setCell(rowid,"status", beforeStatus);
                }
                
            }
		},
		
		onSelectRow : function(rowid) {   
	
		
			if( lastSelected_contractDetailGrid_Id != rowid ){
				alert("onSelectRow 진입확인 lastSelected_contractDetailGrid_Id:"+lastSelected_contractDetailGrid_Id);
							
				lastSelected_contractDetailGrid_Id = rowid;
				lastSelected_contractDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
		},
	
		onCellSelect : function(rowid, iCol, previousCellValue, e) {
			 
			if( lastSelected_contractDetailGrid_Id != rowid ){
				alert("onCellSelect 진입확인 rowid : "+rowid+", icol:"+iCol+", previousCellValue : "+previousCellValue);
				lastSelected_contractDetailGrid_Id = rowid;
				lastSelected_contractDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}

			var status = $(this).getCell(rowid,"status");
		
			if(status == "NORMAL" ){

                if(iCol == 4 || iCol == 5) {  // 품목코드, 품목명 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "IT-_I","완제품 및 반제품 검색");
                
                } else if (iCol == 6) {  // 단위 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "UT","단위 검색");

                }  else if( iCol == 8) {  
                	
                	showInputDialog(this ,rowid);
                	
                }  

            } else if ( ( status == "INSERT" || status == "UPDATE" )  ) {

                if(iCol == 4) {  // 품목코드 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "IT-_I","완제품 및 반제품 검색");
                		
                } else if (iCol == 6) {  // 단위 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "UT","단위 검색");

                }  else if(iCol == 8) {
                	
                	showInputDialog(this ,rowid);
                	
                }
                 
            } 
		}
		
 
	}); // 수주상세 그리드 끝

	$('#contractDetailGrid').navGrid("#contractDetailGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	
}

function initEvent() {
	
	$(':input:radio[name=searchCondition]').on("click", function() {
		
		searchCondition = $(':input:radio[name=searchCondition]:checked').val();
		
		if( searchCondition == 'searchByDate' ) { 
			
			$('#searchCustomerBox').hide();
			$('#startDatePicker').show();
			$('#endDatePicker').show();
			
		} else if(  searchCondition == 'searchByCustomer'  ) {

			$('#searchCustomerBox').show();
			$('#startDatePicker').hide();
			$('#endDatePicker').hide();

		}
	
	});
	
	
	
	$('#searchCustomerBox').on("click", function() {
		
		showModalCode("searchCustomerBox","CL-01");
		//showCodeDialogForInput(this, 'CL-01', '거래처 검색');
	
	});
	
	
	$('#contractSearchButton').on("click", function() {

		showContractGrid();
		
	});
	
	$('#contractAmountBox, #unitPriceOfContractBox').on("keyup", function() {
		
		var sum = $('#contractAmountBox').val() * $('#unitPriceOfContractBox').val();
		
		$('#sumPriceOfContractDiv').text(sum);
		
	});
	
	$('#pdfOpenButton').on("click", function() {
        window.open(
        		"${pageContext.request.contextPath}/base/report.html?method=contractReport&orderDraftNo=" + lastSelected_contractGrid_RowValue.contractNo, 
        		"window", "width=1600, height=1000"
        	
        );
    	
	});
	
	/* $('#excelSaveButton').on("click", function() {
        window.open(
        		"${pageContext.request.contextPath}/base/excel.html?method=contractExcel&orderDraft=" + JSON.stringify(lastSelected_contractGrid_RowValue)+"&filepath=C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\estimulo\\resources\\excel\\",
        		"window", "width=1600, height=1000"
        	
        );
    	
	}); */
	
	$('#contractDetailDeleteButton').on("click",function() {
		
		//가장 나중에 선택된 (수정되기 전) 수주상세 grid 의 행의 처리여부
		var processingStatus = lastSelected_contractDetailGrid_RowValue.processingStatus; 
		
		if( processingStatus != "" ) {

			var errorMsg = ( processingStatus == 'Y' ) ? "이미 MPS에 등록된 수주입니다." : "이미 취소된 견적입니다."
			alertError("사용자 에러",errorMsg);
			return;
			
		}
		
		var rowIdList =  $('#contractDetailGrid').jqGrid('getDataIDs');  // 견적상세 그리드의 전체 행 ID 배열
		
		var deleteCount = 0;

		$(rowIdList).each( function(index, rowId) {   // 전체 행에 대해 반복문 시작
			
			var rowObject = $('#contractDetailGrid').getRowData(rowId); // 한 행의 row 값 정보 객체
			
			var status = rowObject.status;
			
			if(status == 'DELETE' && rowObject.contractDetailNo != '저장시 지정됨' ) {
				
				resultList.push(rowObject);
				deleteCount++;
				
			} else if(status == 'DELETE' && rowObject.contractDetailNo == '저장시 지정됨' ) {
				
				$('#contractDetailGrid').delRowData(rowId);
				
			}
			
		});
				
		var confirmMsg = deleteCount + "개의 수주 상세를 삭제합니다. 계속하시겠습니까?";
		
		if( resultList.length != 0 && confirm(confirmMsg) ) {
			
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/sales/batchContractDetailListProcess.do' ,
				data : {
					method : 'batchListProcess', 
					batchList : JSON.stringify(resultList),
					contractNo : lastSelected_contractDetailGrid_RowValue.contractNo
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {
					
					console.log(dataSet);
					
					if(dataSet.errorCode < 0){
						
						alertError("실패",dataSet.errorMsg);
						return;
						
					}
					
					var resultMsg = "수주상세 </br>" + dataSet.result.DELETE + "</br>총 " + dataSet.result.DELETE.length + " 개가 삭제되었습니다";
					
					alertError("성공", resultMsg);

				}
			});
			
		} else if(resultList.length != 0 && !confirm(confirmMsg)) {
			
			alertError("^^", "취소되었습니다");
			
		} 

		resultList = [];   // 초기화
		
		// 견적, 견적상세 그리드 새로고침
		showContractGrid();
		showContractDetailGrid();
	});

	$('#batchSaveButton').on("click",function() {

		//가장 나중에 선택된 (수정되기 전) 수주상세 grid 의 행의 처리여부
		var processingStatus = lastSelected_contractDetailGrid_RowValue.processingStatus; 
		
		if( processingStatus != "" ) {

			var errorMsg = ( processingStatus == 'Y' ) ? "이미 MPS처리된 수주입니다." : "이미 취소된 견적입니다."
			alertError("사용자 에러",errorMsg);
			return;
			
		}
		var rowIdList =  $('#contractDetailGrid').jqGrid('getDataIDs'); 
		 
		var insertCount = 0;
		var updateCount = 0;
		var deleteCount = 0;

		var errorMsg = "< 제외 목록 > \r";  
		
		$(rowIdList).each( function(index, rowId) {   // 전체 행에 대해 반복문 시작
			
			var rowObject = $('#contractDetailGrid').getRowData(rowId); // 행의 row 값 정보 객체
			var status = rowObject.status;
			
			if(status == 'INSERT' ) {
				
				// 에러 출력함수는 실제로 실행되지는 않음 ㅜㅜ => 그냥 잘못 입력된 행들을 필터링해주는 역할
				if( rowObject.itemCode == '' ) {
					errorMsg += ( rowId + "행 : 품목코드 미입력 \r" );
				} else if(rowObject.unitOfContract == '' ) {
					errorMsg += ( rowId + "행 : 단위 미입력 \r" );
				} else if(rowObject.dueDateOfContract == '' ){
					errorMsg += ( rowId + "행 : 납기일 미입력 \r" );
				} else if(rowObject.estimateAmount == "0" || rowObject.unitPriceOfContract == "0") {
					errorMsg += ( rowId + "행 : 수주수량/수주단가 미입력 \r" );
				} else {
					
					resultList.push(rowObject);	
					insertCount++;
					
				}

			} else if (status == 'UPDATE') {
				
				resultList.push(rowObject);
				updateCount++;
				
			} else if (status == 'DELETE') {
				
				if(rowObject.contractDetailNo != '저장시 지정됨' ) {
					resultList.push(rowObject);
					deleteCount++;
				} else {
					$('#contractDetailGrid').delRowData(rowId);
				}
				
			}
		});
		
		var confirmMsg = 
			( ( errorMsg == "< 제외 목록 > \r" ) ? "" : errorMsg + "\r" ) + 
			"< 가능한 작업 목록 > \r" +
			( ( insertCount != 0 ) ? insertCount + "개의 수주 상세 추가 \n" : "" )+
			( ( updateCount != 0 ) ? updateCount + "개의 수주 상세 수정 \n" : "" ) +
			( ( deleteCount != 0 ) ? deleteCount + "개의 수주 상세 삭제 \n" : ""  )+
			"\r위와 같이 작업합니다. 계속하시겠습니까?"

		var confirmStatus = "";
		
		if(resultList.length != 0) {
			
			confirmStatus = confirm(confirmMsg);
				
		}

		
		if(resultList.length != 0 && confirmStatus) {
			
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/sales/batchContractDetailListProcess.do' ,
				async :false,
				data : {
					method : 'batchListProcess', 
					batchList : JSON.stringify(resultList),
					contractNo : lastSelected_contractDetailGrid_RowValue.contractNo
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {
					
					console.log(dataSet);
					
					var resultMsg = 
						"< 견적상세 작업 내역 >   <br/><br/>"
						+ "추가된 수주상세 : "
						+ ( ( dataSet.result.INSERT.length != 0 ) ? dataSet.result.INSERT : "없음" ) + "</br></br>"
						+ "수정된 수주상세 : " 
						+ ( ( dataSet.result.UPDATE.length != 0 ) ? dataSet.result.UPDATE : "없음" ) + "</br></br>"
						+ "삭제된 수주상세 : " 
						+ ( ( dataSet.result.DELETE.length != 0 ) ? dataSet.result.DELETE : "없음" ) + "</br></br>"
						+ "위와 같이 작업이 처리되었습니다";
						
					alertError("성공", resultMsg);
				}
			});  
			
		} else if(resultList.length != 0 && !confirmStatus) {
			
			alertError("^^", "취소되었습니다");
			
		} else if(resultList.length == 0) {
			
			alertError("^^", "추가/수정/삭제할 수주 상세가 없습니다");
		}

		resultList = [];   // 초기화
		
		// 수주,수주상세 그리드 새로고침
		showContractGrid();
		showContractDetailGrid();
		
	});
	
}


function showContractGrid() {
	 
	
	searchCondition = $(':input:radio[name=searchCondition]:checked').val();
	
	if( searchCondition == null ) {
		alertError("사용자 에러" , "검색 조건을 먼저 선택하세요")
		return; 
		
	} else if ( searchCondition == 'searchByDate' ) {
		
		if( $('#startDatePicker').val() == '시작일' || $('#startDatePicker').val() == '종료일' ) {
			alertError("사용자 에러" , "검색 시작일과 종료일을 입력하세요")
			return; 
						
		}
		
	} else if ( searchCondition == 'searchByCustomer' ) {
		
		if( $('#searchCustomerBox').val() == '거래처 검색' ) {
			alertError("사용자 에러" , "검색할 거래처를 먼저 선택하세요")
			return; 
			
		}
		
	}

	// 초기화
	$('#contractGrid').jqGrid('clearGridData');
	$('#contractDetailGrid').jqGrid('clearGridData');
	lastSelected_contractGrid_Id = "";
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/sales/searchContract.do' ,
		async :false,
		data : {
			method : 'searchContract', 
			searchCondition : $(':input:radio[name=searchCondition]:checked').val() , //searchByDate 기간검색 , searchByCustomer 거래처검색
			startDate : $('#startDatePicker').val(),
			endDate : $('#endDatePicker').val(),
			customerCode : $('#customerCodeBox').val()
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			console.log(dataSet);
			
			gridRowJson = dataSet.gridRowJson;
			alert(JSON.stringify(gridRowJson));
			if(gridRowJson.length != 0) {

				// 수주 Data 넣기
				$('#contractGrid')
					.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
					.trigger('reloadGrid');
				
				
				
			} else {
				alertError("ㅜㅜ" , "검색된 결과가 없습니다");
				
			}
			
			
		}
	});  
	
}

function showContractDetailGrid() {
	
	$('#contractDetailGrid').jqGrid('clearGridData');
	//현재 불러온 데이터를 초기화시켜주는 메소드입니다. 서버에 존재하는 데이터는 삭제되지 않습니다.


	
	$( gridRowJson ).each( function( index, obj )   {  // gridRowJson 의 전체 데이터에 대해 반복문 시작
		
		if( obj.contractNo == lastSelected_contractGrid_RowValue.contractNo ) {		
			//수주일련번호가 마지막으로선택한 수주일련번호랑같으면. 
	
// gridRowJson 배열 Data 중의 어떤 객체 ( obj ) 의 contractNo 가 가장 최근에 선택한 contractGrid 행의 contractNo 와 같으면 
			 
			  
			// obj 의 contractDetailTOList :  선택된 contractNo 에 해당하는 수주상세 Data 
			$('#contractDetailGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : obj.contractDetailTOList })
				.trigger('reloadGrid');
		}            //      .trigger-> 그리드 리로드
		
	});  // 반복문 끝	 
	
	/*
	
	// contractGrid 를 클릭할 때마다 수주상세 Data 를 따로 불러오는 경우의 ajax
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/sales/searchContractDetail.do' ,
		async :false,
		data : {
			method : 'searchContractDetail', 
			contractNo : lastSelected_contractGrid_RowValue.contractNo
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			console.log(dataSet);
			
			var gridRowJson = dataSet.gridRowJson;
			
			$('#contractDetailGrid').jqGrid('clearGridData');
			
			// 수주상세 Data 넣기
			$('#contractDetailGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson }).trigger('reloadGrid');
		}	
	});

	*/
	
}

function showInputDialog(grid ,rowid) {
	
	var contractAmount = $(grid).getCell(rowid, "estimateAmount");
	var unitPriceOfContract = $(grid).getCell(rowid, "unitPriceOfContract");
	var sumPriceOfContract = $(grid).getCell(rowid, "sumPriceOfContract");
	var stockAmountUse = $(grid).getCell(rowid, "stockAmountUse");
	
	$('#contractAmountBox').val(contractAmount);
	$('#unitPriceOfContractBox').val(unitPriceOfContract);
	$('#sumPriceOfContractDiv').text(sumPriceOfContract);
	
	$("#InputDialog").dialog({
		title : '견적수량 / 견적단가 입력',
		autoOpen : true,  
		width : 250,
		height : 250,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {
				
				if (stockAmountUse > $('#contractAmountBox').val() ){
					alertError("사용자 에러","재고사용량보다 수주수량을 낮게 변경할 수 없습니다.");
					return;
				}
				
				$(grid).setCell(rowid, "estimateAmount", $('#contractAmountBox').val() );
				$(grid).setCell(rowid, "unitPriceOfContract", $('#unitPriceOfContractBox').val() );
				$(grid).setCell(rowid, "sumPriceOfContract", $('#sumPriceOfContractDiv').text() );
				$(grid).setCell(rowid, "productionRequirement", ($('#contractAmountBox').val()-stockAmountUse));
				

				$("#InputDialog").dialog("close");
				checkRowChanged(lastSelected_contractDetailGrid_RowValue, grid, rowid);					

			},
			"취소" : function() {
				$("#InputDialog").dialog("close");
			}
		}
	});
	
	
}

function showCodeDialog(grid, rowid, iCol, divisionCodeNo, title){
	$("#codeDialog").dialog({
		title : '코드 검색' ,
		width:500,
		height:500,
		modal : true   // 폼 외부 클릭 못하게
	});
	
	$.jgrid.gridUnload("#codeGrid");
	
	$("#codeGrid").jqGrid({
            url: "${pageContext.request.contextPath}/base/codeList.do",
            datatype: "json",
            jsonReader: { root: "detailCodeList" },
            postData: { 
        		method: "findDetailCodeList" ,
        		divisionCode: divisionCodeNo   
        	},
			colNames:[ '상세코드번호' , '상세코드이름' , '사용여부' ],
			colModel:[
				{ name : 'detailCode', width:100, align : "center",editable:false},
				{ name : 'detailCodeName', width:100, align : "center", editable:false},
				{ name : 'codeUseCheck', width:100, align : "center",editable:false},
			],
			width: 450,
			height: 300,
			caption: title,
			align: "center",
			viewrecords:true,
			rownumbers: true,
			
			onSelectRow: function(id) {
				
				var detailCode=$("#codeGrid").getCell(id, 1);
				var detailName=$("#codeGrid").getCell(id, 2);
				var codeUseCheck=$("#codeGrid").getCell(id, 3);
				
				if(codeUseCheck != 'n' && codeUseCheck != 'N') {
					
					if(iCol == 4) {
						var ids = $(grid).getRowData();
						var errorStatus = false;
						$(ids).each(function(index, obj) {
							
							var itemCodeInList = obj.itemCode;
							
							if(detailCode == itemCodeInList) {
								alertError("사용자 에러","수주 상세에 이미 있는 품목입니다");
								errorStatus = true;
								return false;
							} 
						})
						
						if(!errorStatus) {
							$(grid).setCell(rowid, iCol, detailCode);					
							$(grid).setCell(rowid, iCol+1, detailName);	
						}
						
					} else if(iCol == 6) {
						$(grid).setCell(rowid, iCol, detailCode);
					}
					
					$("#codeDialog").dialog("close");
					checkRowChanged(lastSelected_contractDetailGrid_RowValue, grid, rowid);					

					
				} else {
					alertError("사용자 에러", "사용 가능한 코드가 아닙니다");
				}
			}
		});

}

function checkRowChanged(previousRowValue, grid, rowid) {

	var nextRowValue = $(grid).jqGrid('getRowData', rowid);
	var edited = false;
	
	if(nextRowValue.status == 'NORMAL') {
		for(var key in previousRowValue) {
			if(nextRowValue[key] != previousRowValue[key]) {
				edited = true;
			}
		}		
	}
	
	if(edited) {
		$(grid).setCell(rowid, "status", "UPDATE");
	}

}
								//this, 'CL-01', '거래처 검색'
/*  
 function showCodeDialogForInput(source, divisionCodeNo, title){
							
	$("#codeDialog").dialog({
		title : '코드 검색' ,
		width:500,
		height:500,
		modal : true   // 폼 외부 클릭 못하게
	});
	
	$.jgrid.gridUnload("#codeGrid");
	
	$("#codeGrid").jqGrid({
		
            url:"${pageContext.request.contextPath}/base/codeList.do",
            datatype: "json",
            jsonReader: { root: "detailCodeList" },
            postData: { 
            		method: "findDetailCodeList" ,
            		divisionCode: divisionCodeNo   
            },
			colNames:[ '상세코드번호' , '상세코드이름' , '사용여부' ],
			colModel:[
				{ name : 'detailCode', width:100, align : "center",editable:false},
				{ name : 'detailCodeName', width:100, align : "center", editable:false},
				{ name : 'codeUseCheck', width:100, align : "center",editable:false},
			],
			width: 450,
			height: 300,
			caption: title,
			align: "center",
			viewrecords:true,
			rownumbers: true,
			onSelectRow: function(id) {
				
				var detailCode=$("#codeGrid").getCell(id, 1);
				var detailName=$("#codeGrid").getCell(id, 2);
				var codeUseCheck=$("#codeGrid").getCell(id, 3);
				
				if(codeUseCheck != 'n' && codeUseCheck != 'N') {
					
					$(source).val(detailName);
					$('#customerCodeBox').val(detailCode);
					
					$("#codeDialog").dialog("close");			

				} else {
					alertError("사용자 에러", "사용 가능한 코드가 아닙니다");
				}
			}
		});

}

 */
</script>
</head>
<body>
	<fieldset style="display: inline;">
	    <legend>수주 검색 </legend>
    		<label for="radio-1">기간 검색</label>
    		<input type="radio" name="searchCondition" value="searchByDate" id="radio-1">
    		<label for="radio-2">거래처 검색</label>
    		<input type="radio" name="searchCondition" value="searchByCustomer" id="radio-2">
	</fieldset>
	
	<input type="text" value="시작일" id="startDatePicker" />
	<input type="text" value="종료일" id="endDatePicker" />
	<input type="text" value="거래처 검색" id="searchCustomerBox" />
	<input type="hidden" id="customerCodeBox" />
	
	<input type="button" value="수주 조회" id="contractSearchButton" />
	<input type="button" value="PDF 출력" id="pdfOpenButton" />
 
<table id="contractGrid" ></table>
<div id="contractGridPager"></div>
	
	<input type="button" value="수주상세추가" id="contractDetailInsertButton" />
	<input type="button" value="선택한상세삭제" id="contractDetailDeleteButton" />
	<input type="button" value="일괄저장" id="batchSaveButton" />

<table id="contractDetailGrid" ></table>
<div id="contractDetailGridPager"></div>

	<div id="InputDialog">
		<div>
			<label for="contractAmountBox" style="font-size: 20px; margin-right: 10px">수주수량</label> 
			<input type="text" id="contractAmountBox" /> <br /> 
			<label for="unitPriceOfContractBox" style="font-size: 20px; margin-right: 10px">수주단가</label> 
			<input type="text" id="unitPriceOfContractBox" /> <br /> 
			<label for="sumPriceOfContractDiv" style="font-size: 20px; margin-right: 10px">합계액 : </label>
			<div id="sumPriceOfContractDiv"></div>
		</div>
	</div>

<div id="codeDialog">
	<table id="codeGrid"></table>
</div>
</body>
</html>