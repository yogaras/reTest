<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MPS 등록/조회</title>
<style>

#startDatePicker, #endDatePicker {
	display: inline;
	width: 115px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
	text-align : center;
	
}

#contractDetailStartDatePicker, #contractDetailEndDatePicker, 
#salesPlanStartDatePicker, #salesPlanEndDatePicker {
	display: inline;
	width: 100px;
	transition: 0.6s;
	outline: none;
	height: 20px;
	font-size: 16px;
	text-align : center;
	
}
#contractDetail-dialog legend{
	color : #000000;
}
#contractDetail-dialog legend{
	color : #000000;
}
.l1 {
	color: #FFFFFF;
}
legend {
	color: #FFFFFF;
}	
.ui-datepicker{
	z-index: 10000 !important;
}

.ui-dialog { 
	z-index: 9999 !important ; 
	font-size:12px;
}

</style>

<script>

var lastSelected_mpsGrid_Id;   // 가장 나중에 선택한 MPS grid 의 행 id 
var lastSelected_mpsGrid_RowValue;   // 가장 나중에 선택한 MPS grid 의 행 값 

var lastSelected_contractDetailGrid_Id;  // 가장 나중에 선택한 견적상세 grid 의 행 id
var lastSelected_contractDetailGrid_RowValue;  // 가장 나중에 선택한 견적상세 grid 의 행 값

var lastSelected_salesPlanGrid_Id;  // 가장 나중에 선택한 판매계획 grid 의 행 id
var lastSelected_salesPlanGrid_RowValue;  // 가장 나중에 선택한 판매계획 grid 의 행 값

var previousCellValue;  // 수정 가능한 셀에서 수정 전의 셀 값 
var resultList = [];  // 최종적으로 컨트롤러로 보내는 JS 객체 배열 

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
	
	
	$('#contractDetail-dialog').hide();
	$('#salesPlan-dialog').hide();

	
	initGrid();
	initEvent();
	
});

function initGrid() {

	// mps 그리드 시작
	$('#mpsGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "주생산계획번호", "계획구분", "일련번호(수주/판매)", "수주상세일련번호", 
			"판매계획일련번호", "품목코드", "품목명", "단위", "계획일자", "계획수량", "납기일", 
			"예정마감일자", "MRP 적용상태", "비고" ], 
		colModel : [ //그리드 Body에 보여질 데이터의 속성
			{ name: "mpsNo", width: "100", resizable: true, align: "center"} ,
			{ name: "mpsPlanClassification", width: "65", resizable: true, align: "center" } ,
			{ name: "no", width: "120", resizable: true, align: "center" ,
				formatter : function (cellvalue, icol, rowObj) {
					
					var value = "";
					
					if( rowObj.contractDetailNo != null ) {     
					    value = rowObj.contractDetailNo;
					} else {
						value = rowObj.salesPlanNo;
					}
					
					return value;
					
				}
			},
			
			{ name: "contractDetailNo", width: "120", resizable: true, align: "center", hidden: true} ,
			{ name: "salesPlanNo", width: "120", resizable: true, align: "center", hidden: true} ,
			{ name: "itemCode", width: "70", resizable: true, align: "center"} ,
			{ name: "itemName", width: "100", resizable: true, align: "center"} ,
			{ name: "unitOfMps", width: "45", resizable: true, align: "center"} ,
			{ name: "mpsPlanDate", width: "65", resizable: true, align: "center", editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', //폼 편집에서 사용할 유형
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { //이 함수가 정의되면 해당 element object가 함수에 전달된다.
							$(element).datepicker({ 
								minDate : new Date() ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell(lastSelected_mpsGrid_Id,"mpsPlanDate",false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "mpsPlanAmount", width: "80", resizable: true, align: "center"} ,
			{ name: "dueDateOfMps", width: "65", resizable: true, align: "center", editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { 
							$(element).datepicker({ 
								minDate : new Date() ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell(lastSelected_mpsGrid_Id,"dueDateOfMps",false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "scheduledEndDate", width: "90", resizable: true, align: "center", editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { 
							$(element).datepicker({ 
								minDate : new Date() ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell(lastSelected_mpsGrid_Id,"scheduledEndDate",false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "mrpApplyStatus", width: "100", resizable: true, align: "center"} ,
			{ name: "description", width: "80", resizable: true, align: "center" } 

		], 
		caption : '주생산계획 (MPS)', // 그리드 상단의 제목
		sortname : 'mpsNo', //서버에서 처음 로드될 때 데이터가 정렬되는 컬럼 
		multiselect : false, //다중선택 
		multiboxonly : false,//multiselect옵션이 true일때 작동한다.multiboxonly옵션이 true인경우 checkbox를 클릭해야만 다중선택이 된다.(체크박스생성)
		viewrecords : false, //총 레코드 갯수 중에서 시작/끝 레코드 번호를 표시
		rownumWidth : 30, //row number 컬럼의 width
		height : 180, 
		width : 1000,
		autowidth :	true, //그리드의 상위 Element의 width로 자동으로 계산됨. 그리드가 생성될때 처음에만 수행
		shrinkToFit : false, //그리드의 width를 고려하여 컬럼의 width를 어떻게 재계산할지 정의한다.	이 값이 true이고 컬럼의 width도 설정된경우 모든 컬럼은 width의 비율만큼 조정된다.
		cellEdit : false,//셀의 에디트 가능여부 결정
		rowNum : 50,   // -1 : 모든 로우 한번에 표시, 그런데 잘 안먹히는 경우 많음
		scrollerbar: true, 
		//rowList : [ 10, 20, 30 ],
		viewrecords : true, //true설정 시 jqGrid는 쿼리의 총 레코드 갯수 중에서 시작/끝 레코드 번호를 표시한다.
		editurl : 'clientArray', //사용할 url을 정의한다.    clientArray = 개발자정의
		cellsubmit : 'clientArray',//셀의 컨텐츠가 저장될 위치를 지정한다.
		rownumbers : true, //true 설정 시 그리드의 왼쪽에 새로운 컬럼이 추가된다.이 컬럼의 목적은 1부터 시작하는 사용가능한 row의 숫자를 카운트		
		autoencode : true, //서버에서 가져온 데이터 인코딩 여부
		resizable : true,//컬럼의 resize 여부
		loadtext : '로딩중...', //데이터 로딩중 보여줄 문자열
		emptyrecords : '데이터가 없습니다.', //현재 또는 그리드에서 반환된 레코드 수가 0일 때 보여질 문자열.
		cache : false, 
		pager : '#mpsGridPager', //페이징 Element 지정 

		beforeEditCell(rowid, cellname, value, iRow, iCol){

			if( lastSelected_mpsGrid_Id != rowid ){
				lastSelected_mpsGrid_Id = rowid;
				lastSelected_mpsGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
		},
		
		onSelectRow : function(rowid) {   
	
			if( lastSelected_mpsGrid_Id != rowid ){
				lastSelected_mpsGrid_Id = rowid;
				lastSelected_mpsGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
		},
		
		onCellSelect : function(rowid, iCol, previousCellValue, e) {

			if( lastSelected_mpsGrid_Id != rowid ){
				lastSelected_mpsGrid_Id = rowid;
				lastSelected_mpsGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
		
		}
	}); // mps 그리드 끝
	
	// mps 그리드의 페이저 생성
	$('#mpsGrid').navGrid("#mpsGridPager", {
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
		colNames : [ "MPS 등록", "수주상세일련번호", "수주유형", "계획구분", "수주일자", "견적수량", "초기납품내역","제작수량", 
			"계획일자" , "출하예정일", "납기일", "거래처코드", "품목코드", "품목명", "단위", "비고"], 
		colModel : [
			{ name: "contractDetailCheck", width: "70", resizable: true, align: "center" ,
				formatter : function (cellvalue, options, rowObj) {
					
					var contractDetailCheckBox = "<input type='checkbox' name='contractDetailCheck' value=" + JSON.stringify(options.rowId) + "/>";     
					console.log(options)
				    return contractDetailCheckBox;
				     
				}
			},
			{ name: "contractDetailNo", width: "130", resizable: true, align: "center"} ,
			{ name: "contractType", width: "80", resizable: true, align: "center"} ,
			{ name: "planClassification", width: "10", resizable: true, align: "center" , hidden : true ,
				formatter : function (cellvalue, options, rowObj) {
					     
				    return "수주상세";
				     
				}
			},
			{ name: "contractDate", width: "110", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' } 
			} ,
			{ name: "estimateAmount", width: "110", resizable: true, align: "center", 
		        formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
        	} ,
			{ name: "stockAmountUse", width: "130", resizable: true, align: "center"},
			{ name: "productionRequirement", width: "80", resizable: true, align: "center",
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "mpsPlanDate", width: "110", resizable: true, align: "center" , editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { 
							var mindate = new Date();
							var maxdate = new Date();
							$(element).datepicker({ 
								minDate : mindate ,
								maxDate : maxdate ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell( lastSelected_contractDetailGrid_Id , "mpsPlanDate" ,  false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "scheduledEndDate", width: "110", resizable: true, align: "center" , editable: true,
			 // formatter: 'date',  // => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { 
							$(element).datepicker({ 
								minDate : new Date() ,
								maxDate : $(this).getCell( lastSelected_contractDetailGrid_Id , "dueDateOfContract" ) ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell( lastSelected_contractDetailGrid_Id , "scheduledEndDate" , false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "dueDateOfContract", width: "110", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' } 
			} ,
			{ name: "customerCode", width: "100", resizable: true, align: "center"} ,
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,
			{ name: "itemName", width: "150", resizable: true, align: "center"} ,
			{ name: "unitOfContract", width: "50", resizable: true, align: "center"} ,
			{ name: "description", width: "100", resizable: true, align: "center" , editable : true } 

		], 
		caption : 'MPS 등록 가능한 수주상세 목록', 
		sortname : 'contractDetailNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 300, 
		width : 1200,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 50, 
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
		cache : false , 
		pager : '#contractDetailGridPager',

		beforeEditCell(rowid, cellname, value, iRow, iCol){

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

		onSelectRow : function(rowid) {   
	
			if( lastSelected_contractDetailGrid_Id != rowid ){
				lastSelected_contractDetailGrid_Id = rowid;
				lastSelected_contractDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}			
		}, 

		onCellSelect : function(rowid, iCol, previousCellValue, e) { 
			
			if( lastSelected_contractDetailGrid_Id != rowid ){
				lastSelected_contractDetailGrid_Id = rowid;
				lastSelected_contractDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
		}
	}); // 수주 상세 그리드 끝
	
	// 수주상세 그리드의 페이저 생성
	$('#contractDetailGrid').navGrid("#contractDetainGridPager", {
		add : false,
		del : false,
		edit : false,
		search : false,
		refresh : true,
		view: true
	});
	
	// 판매계획 그리드 시작
	$('#salesPlanGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "MPS 등록", "판매계획일련번호", "계획구분", "판매계획일", "계획일자" , "출하예정일", 
			"판매마감일", "품목코드", "품목명", "단위", "계획수량", "비고"], 
		colModel : [
			{ name: "contractDetailCheck", width: "60", resizable: true, align: "center" ,
				formatter : function (cellvalue, options, rowObj) {
					
					var contractDetailCheckBox = "<input type='checkbox' name='salesPlanCheck' value=" + JSON.stringify(options.rowId) + "/>";     
				    return contractDetailCheckBox;
				     
				}
			},
			{ name: "salesPlanNo", width: "130", resizable: true, align: "center"} ,
			{ name: "planClassification", width: "10", resizable: true, align: "center" , hidden : true ,
				formatter : function (cellvalue, options, rowObj) {
					     
				    return "판매계획";
				     
				}
			},
			{ name: "salesPlanDate", width: "110", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' } 
			} ,
			{ name: "mpsPlanDate", width: "110", resizable: true, align: "center" , editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { 

							$(element).datepicker({ 
								minDate : new Date() ,
								maxDate : $(this).getCell( lastSelected_salesPlanGrid_Id , "dueDateOfSales" ) ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell(lastSelected_salesPlanGrid_Id, "mpsPlanDate" , false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "scheduledEndDate", width: "110", resizable: true, align: "center" , editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { 
							$(element).datepicker({ 
								minDate : new Date() ,
								maxDate : $(this).getCell( lastSelected_salesPlanGrid_Id , "dueDateOfSales" ) ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell(lastSelected_salesPlanGrid_Id , "scheduledEndDate" , false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "dueDateOfSales", width: "90", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' } 
			} ,
			{ name: "itemCode", width: "100", resizable: true, align: "center" } ,
			{ name: "itemName", width: "150", resizable: true, align: "center"} ,
			{ name: "unitOfSales", width: "70", resizable: true, align: "center"} ,
			{ name: "salesAmount", width: "70", resizable: true, align: "center",
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "description", width: "100", resizable: true, align: "center" , editable : true }

		], 
		caption : 'MPS 등록 가능한 판매계획 목록', 
		sortname : 'salesPlanNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 300, 
		width : 1200,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 50, 
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
		cache : false , 
		pager : '#salesPlanGridPager',

		beforeEditCell(rowid, cellname, value, iRow, iCol){

        	if(value == null || value == "" ) {
        		previousCellValue = " ";	
        	} else {
        		previousCellValue = value;
        	}
        	
			if( lastSelected_salesPlanGrid_Id != rowid ){
				lastSelected_salesPlanGrid_Id = rowid;
				lastSelected_salesPlanGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}			
		}, 
  
		onSelectRow: function(rowid) {   
	
			if( lastSelected_salesPlanGrid_Id != rowid ){
				lastSelected_salesPlanGrid_Id = rowid;
				lastSelected_salesPlanGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}			
		}, 

		onCellSelect : function(rowid, iCol, previousCellValue, e) { 
			
			if( lastSelected_salesPlanGrid_Id != rowid ){
				lastSelected_salesPlanGrid_Id = rowid;
				lastSelected_salesPlanGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
		}
	}); 
	// 판매 계획 그리드 끝
	
	// 판매계획 그리드의 페이저 생성
	$('#salesPlanGrid').navGrid("#salesPlanGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	
}

function initEvent() {
	
	$('#mpsSearchButton').on("click", function() {

		showMpsGrid();
		
	});
	
	$('#registerNewMpsButton').on("click", function() {

		var searchMpsCondition = $(':input:radio[name=searchMpsCondition]:checked').val(); 
		//수주상세,판매계획 중 체크한 라디오의 value를 구함. 수주상세=contractDetail, 판매계획=salesPlan
		
		if(  searchMpsCondition == null ) { //체크를 안하면 searchMpsCondition가 null
			
			alertError("사용자 에러" , "MPS 로 등록할 계획을 선택하세요");
			return;
			
		}
		
		
		if( searchMpsCondition == 'contractDetail' ) { //수주상세 라디오를 체크했을때
			
			openContractDetailDialog();
			
		} else if ( searchMpsCondition == 'salesPlan' ) { // 판매계획 라디오를 체크했을때
		
			openSalesPlanDialog();
			
		} 
		
		
	});
	
	$('#contractDetailSearchButton').on('click', function() {
		
		var searchContractDetailCondition = $(':input:radio[name=searchContractDetailCondition]:checked').val(); 
		//체크된 라디오 value.. 수주일자 = contractDate, 납기일 = dueDateOfContract
				
		if( searchContractDetailCondition == undefined ) { //체크된거 없으모
			
			alertError("사용자 에러", "수주상세 검색조건을 먼저 선택해 주세요");
			return;
			
		} else if( $('#contractDetailStartDatePicker').val() == "" || $('#contractDetailEndDatePicker').val() == "" ) {
			//시작일,종료일 중 하나라도 비었을때
			
			alertError("사용자 에러", "검색할 시작일과 종료일을 모두 입력하세요");
			return;
			
		}
		
		$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/production/searchContractDetailInMpsAvailable.do',
				data : {
					method : 'searchContractDetailListInMpsAvailable', 
					searchCondition : searchContractDetailCondition, //체크된 라디오 value.. 수주일자 = contractDate, 납기일 = dueDateOfContract
					startDate : $('#contractDetailStartDatePicker').val() ,
					endDate : $('#contractDetailEndDatePicker').val(),
					includeMrpApply : "includeMrpApply"    // MRP 적용된 주생산계획도 검색
					
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {
				
					console.log("@@@@@@@@@@@@ dataset : "+dataSet);
					alert(JSON.stringify(dataSet));
					if(dataSet.errorCode < 0){
						alertError('실패',dataSet.errorMsg);
						return;
					}
					
					var gridRowJson = dataSet.gridRowJson;
					
					if(gridRowJson.length == 0 ) {
						
						alertError("ㅜㅜ", "검색된 데이터가 없습니다");
						
					} else {
						
						$('#contractDetailGrid')
							.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
							.trigger('reloadGrid');
						console.log("@@@@@@@@@@@@@@@ gridRowJson : "+gridRowJson);
					}
				}
			});
		
	});
	

	$('#salesPlanSearchButton').on('click', function() {
		
		var searchSalesPlanCondition = $(':input:radio[name=searchSalesPlanCondition]:checked').val();
				
		if( searchSalesPlanCondition == undefined ) {
			alertError("사용자 에러", "판매계획 검색조건을 먼저 선택해 주세요");
			return;
		} else if( $('#salesPlanStartDatePicker').val() == "" || $('#salesPlanEndDatePicker').val() == "" ) {
			alertError("사용자 에러", "검색할 시작일과 종료일을 모두 입력하세요");
			return;
		}
		
		$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/production/searchSalesPlanInMpsAvailable.do' ,
				data : {
					method : 'searchSalesPlanListInMpsAvailable', 
					searchCondition : $(':input:radio[name=searchSalesPlanCondition]:checked').val() ,
					startDate : $('#salesPlanStartDatePicker').val() ,
					endDate : $('#salesPlanEndDatePicker').val()
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {
				
					console.log(dataSet);
					
					if(dataSet.errorCode < 0){
						alertError('실패',dataSet.errorMsg);
						return;
					}
					
					var gridRowJson = dataSet.gridRowJson;
					
					if(gridRowJson.length == 0 ) {
						alertError("ㅜㅜ", "검색된 데이터가 없습니다");
						
					} else {
						
						$('#salesPlanGrid')
							.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
							.trigger('reloadGrid');
						
					}
				}
			});
		
	});
	
	
	$('#contractDetailToMpsButton').on('click', function() { // 선택한 수주상세를 MPS에 등록

		var checkedRowIdList = $('input[type=checkbox][name=contractDetailCheck]:checked');
		console.log(checkedRowIdList)
		var resultRowIdList =[];  // 체크된 수주상세 행들 중 실제로 등록된 행들의 id 배열 
		
		if(checkedRowIdList.length == 0 ) {
			alertError("사용자 에러","체크된 수주상세가 없습니다");
			return;
		}
		
		var errorMsg = "";

		$(checkedRowIdList).each( function() {  // 체크된 수주 상세에 대해 반복문 시작

			var rowId = $(this).val();  // 체크한 수주상세 행의 id
			var rowValue = $('#contractDetailGrid').getRowData(rowId);  // 체크한 수주상세 행의 값
 
			// 계획일자, 출하예정일을 "-"로 나눈 배열
			var mpsPlanDateArr = rowValue.mpsPlanDate.split('-');
			var scheduledEndDateArr = rowValue.scheduledEndDate.split('-');
			
			// 계획일자, 출하예정일을 new Date() 함수로 날짜 타입으로 변환
	        var mpsPlanDate = new Date(mpsPlanDateArr[0], parseInt(mpsPlanDateArr[1])-1, mpsPlanDateArr[2]);
	        var scheduledEndDate = new Date(scheduledEndDateArr[0], parseInt(scheduledEndDateArr[1])-1, scheduledEndDateArr[2]);
	            
			
			if( rowValue.mpsPlanDate == "" ) {
				errorMsg += "수주상세 " + rowValue.contractDetailNo + " : 계획일자를 입력하지 않았습니다 </br>";
				
			} else if(rowValue.scheduledEndDate == "") {
				errorMsg += "수주상세 " + rowValue.contractDetailNo + " : 출하예정일을 입력하지 않았습니다 </br>";
				
			} else if( mpsPlanDate >= scheduledEndDate ) {
				errorMsg += "수주상세 " + rowValue.contractDetailNo + " : 계획일자는 출하예정일보다 같거나 클 수 없습니다 </br>";
				
			} else {//resultList는 젤 위체 선언 돼 있음
				resultList.push(rowValue); // 체크한 수주상세 행의 데이터
				resultRowIdList.push(rowId); //체크한 수주상세 행의 id
				alert(JSON.stringify(resultList));
			}
				
		});  // 반복문 끝
			console.log(resultList)
			console.log(resultRowIdList)
			
		if(errorMsg != "" ) {
			errorMsg += "</br/>MPS 저장목록에서 제외합니다";
			alertError("사용자 에러", errorMsg);	

		}
		
 
		if( resultList.length != 0 ) {

			var confirmMsg = "총 " + resultList.length + " 개의 수주상세를 주생산계획에 등록합니다. \r\n 계속하시겠습니까?";
			
			var confirmStatus = confirm(confirmMsg);
	
			if(confirmStatus == true) {
				
				$.ajax({
					type : 'POST',
					url : '${pageContext.request.contextPath}/production/convertContractDetailToMps.do' ,
					data : {
						method : 'convertContractDetailToMps', 
						batchList : JSON.stringify(resultList) // 체크한 수주상세 행의 데이터
					},
					dataType : 'json', 
					cache : false, 
					success : function(dataSet) {
						
						console.log(dataSet);

	 					if(dataSet.errorCode < 0){
	 						alertError('에러',dataSet.errorMsg);
	 						return;
						}
						
						$(resultRowIdList).each( function(index, rowId) {
							
							$('#contractDetailGrid').delRowData(rowId);  // 실제 등록된 행 삭제

						});
						
						var resultMsg = 
							"<  수주상세에서 MPS 등록 내역 >   <br/><br/>"
							+ "추가 : " 
							+ ( ( dataSet.result.INSERT.length != 0 ) ? dataSet.result.INSERT : "없음" ) + "</br></br>"
							+ "위와 같이 작업이 처리되었습니다";
							
						alertError("성공", resultMsg);
					}					
				});
				
			} else {
				
				alertError("^^", "취소되었습니다");

			}
			
		} 

		resultList = [];  // 초기화
	
	}); 
	
	
	$('#salesPlanToMpsButton').on('click', function() {

		var checkedRowIdList = $('input[type=checkbox][name=salesPlanCheck]:checked');

		var resultRowIdList =[];  // 체크된 수주상세 행들 중 실제로 등록된 행들의 id 배열 
		
		if(checkedRowIdList.length == 0 ) {
			alertError("사용자 에러","체크된 판매계획이 없습니다");
			return;
		}
		
		var errorMsg = "";

		$(checkedRowIdList).each( function() {  // 체크된 판매 계획에 대해 반복문 시작

			var rowId = $(this).val();  // 체크한 판매 계획 행의 id
			var rowValue = $('#salesPlanGrid').getRowData(rowId);  // 체크한 판매계획 행의 값

			// 계획일자, 출하예정일을 "-"로 나눈 배열
			var mpsPlanDateArr = rowValue.mpsPlanDate.split('-');
			var scheduledEndDateArr = rowValue.scheduledEndDate.split('-');
			
			// 계획일자, 출하예정일을 new Date() 함수로 날짜 타입으로 변환
	        var mpsPlanDate = new Date(mpsPlanDateArr[0], parseInt(mpsPlanDateArr[1])-1, mpsPlanDateArr[2]);
	        var scheduledEndDate = new Date(scheduledEndDateArr[0], parseInt(scheduledEndDateArr[1])-1, scheduledEndDateArr[2]);
	         console.log(mpsPlanDate);
	         console.log(scheduledEndDate);
			
			if( rowValue.mpsPlanDate == "" ) {
				errorMsg += "판매계획 " + rowValue.salesPlanNo + " : 계획일자를 입력하지 않았습니다 </br>";
				
			} else if(rowValue.scheduledEndDate == "") {
				errorMsg += "판매계획 " + rowValue.salesPlanNo + " : 출하예정일을 입력하지 않았습니다 </br>";
				
			} else if( mpsPlanDate >= scheduledEndDate ) {
				errorMsg += "판매계획 " + rowValue.salesPlanNo + " : 계획일자는 출하예정일보다 같거나 클 수 없습니다 </br>";
				
			} else {
				resultList.push(rowValue);
				resultRowIdList.push(rowId);
			}

		});  // 반복문 끝
		
		if(errorMsg != "" ) {
			errorMsg += "</br/>MPS 저장목록에서 제외합니다";
			alertError("사용자 에러", errorMsg);	

		}
		
 
		if( resultList.length != 0 ) {

			var confirmMsg = "총 " + resultList.length + " 개의 판매계획을 주생산계획에 등록합니다. \r\n 계속하시겠습니까?";
			
			var confirmStatus = confirm(confirmMsg);
	
			if(confirmStatus == true) {
				
				$.ajax({
					type : 'POST',
					url : '${pageContext.request.contextPath}/production/convertSalesPlanToMps.do' ,
					data : {
						method : 'convertSalesPlanToMps', 
						batchList : JSON.stringify(resultList)
					},
					dataType : 'json', 
					cache : false, 
					success : function(dataSet) {
						
						console.log(dataSet);
						
						if(dataSet.errorCode < 0){
							alertError('실패',dataSet.errorMsg);
							return;
						}
						
						$(resultRowIdList).each( function(index, rowId) {
							
							$('#salesPlanGrid').delRowData(rowId);  // 실제 등록된 행 삭제

						});
						
						var resultMsg = 
							"<  판매계획에서 MPS 등록 내역 >   <br/><br/>"
							+ "추가 : " 
							+ ( ( dataSet.result.INSERT.length != 0 ) ? dataSet.result.INSERT : "없음" ) + "</br></br>"
							+ "위와 같이 작업이 처리되었습니다";
							
						alertError("성공", resultMsg);
					}					
				});
				
			} else {
				
				alertError("^^", "취소되었습니다");	

			}
			
		} 

		resultList = [];  // 초기화
	
	});
}

function openContractDetailDialog() {
	
	$('#contractDetailGrid').jqGrid('clearGridData');//초기화

	$('#contractDetailStartDatePicker').val("시작일");
	$('#contractDetailEndDatePicker').val("종료일");
	
	$("#contractDetail-dialog").dialog({
		title : '수주 상세에서 MPS 등록',
		autoOpen : true,  // 함수 호출시 자동으로 열리도록 이거 주면 걍 열리는가 봄 
		width : 1250,
		height : 590,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {
				$("#contractDetail-dialog").dialog("close");

			}
		},
		
		open : function(event, ui) {
			
			$('#contractDetailStartDatePicker').datepicker({ 
            	changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$( '#contractDetailEndDatePicker' ).datepicker( "option", "minDate", selectedDate );
					//		종료일						달력			옵션준다		최소날짜		위에고른거
				}
            });
			
			$('#contractDetailEndDatePicker').datepicker({ 
            	changeMonth : true,
				numberOfMonths : 1
            });
			
		}
		
	});
	
}

function openSalesPlanDialog() {

	$('#salesPlanGrid').jqGrid('clearGridData');

	$('#salesPlanStartDatePicker').val("시작일");
	$('#salesPlanEndDatePicker').val("종료일");
	
	$("#salesPlan-dialog").dialog({
		title : '판매 계획에서 MPS 등록',
		autoOpen : true,  // 함수 호출시 자동으로 열리도록
		width : 1250,
		height : 590,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {
				$("#salesPlan-dialog").dialog("close");

			}
		},
		
		open : function(event, ui) {
			
			$('#salesPlanStartDatePicker').datepicker({ 
            	changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$( '#salesPlanEndDatePicker' ).datepicker( "option", "minDate", selectedDate );
				}
            });
			
			$('#salesPlanEndDatePicker').datepicker({ 
            	changeMonth : true,
				numberOfMonths : 1
            });
			
		}
		
	});
		
}


function showMpsGrid() {
	
	// $('#mpsGrid').jqGrid('clearGridData');
	
	// ajax 시작
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/production/searchMpsInfo.do' ,
		data : {
			method : 'searchMpsInfo',
			startDate : $("#startDatePicker").val() ,
			endDate : $("#endDatePicker").val() ,
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) { 
			console.log(dataSet);
			
			if(dataSet.errorCode < 0){
				alertError('실패',dataSet.errorMsg);
				return;
			}
			
			var gridRowJson = dataSet.gridRowJson;  // gridRowJson : 그리드에 넣을 json 형식의 data
			
			// mps Data 넣기
			$('#mpsGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid');
		
	}});  // ajax 끝
	
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

</script>
</head>
<body>
	<fieldset style="display: inline;">
	    <legend>MPS 로 등록할 계획 구분</legend>
    		<label for="contractDetailRadio">수주상세</label>
    		<input type="radio" name="searchMpsCondition" value="contractDetail" id="contractDetailRadio">
    		<label for="salesPlanRadio">판매계획</label>
    		<input type="radio" name="searchMpsCondition" value="salesPlan" id="salesPlanRadio">
	</fieldset>
	
	<fieldset style="display: inline;">
	    <legend>MPS 등록</legend>
		 	<input type="button" value="새로운 MPS 등록" id="registerNewMpsButton" />
  			
 	</fieldset>
	
	<fieldset style="display: inline;">
	    <legend>MPS 검색 (BY 계획일자)</legend>
			<input type="text" value="시작일" id="startDatePicker" />
			<input type="text" value="종료일" id="endDatePicker" />
			<input type="button" value="MPS 조회" id="mpsSearchButton" />
	</fieldset>
	

<table id="mpsGrid" ></table>
<div id="mpsGridPager"></div>

<div id="contractDetail-dialog">
	<fieldset style="display: inline;">
	    <legend>수주상세 검색조건</legend>
    		<label for="radio-1">수주일자</label>
    		<input type="radio" name="searchContractDetailCondition" value="contractDate" id="radio-1" />
    		<label for="radio-2">납기일</label>
    		<input type="radio" name="searchContractDetailCondition" value="dueDateOfContract" id="radio-2" />
	</fieldset>
	
	<fieldset style="display: inline;">
	    <legend>수주상세 검색</legend>
			<input type="text" value="시작일" id="contractDetailStartDatePicker" />
			<input type="text" value="종료일" id="contractDetailEndDatePicker" />
			<input type="button" value="검색" id="contractDetailSearchButton" style="font-size : 16px;"/>
	</fieldset>
	<input type="button" value="선택한 수주상세를 MPS 에 등록" id="contractDetailToMpsButton" style="font-size : 16px;" />
	
	<table id = "contractDetailGrid"></table>
	<div id = "contractDetailGridPager"></div>
</div>

<div id="salesPlan-dialog">
	<fieldset style="display: inline;">
	    <legend>판매계획 검색조건</legend>
    		<label for="radio-3">판매계획일</label>
    		<input type="radio" name="searchSalesPlanCondition" value="salesPlanDate" id="radio-3" />
    		<label for="radio-4">계획마감일</label>
    		<input type="radio" name="searchSalesPlanCondition" value="dueDateOfSales" id="radio-4" />
	</fieldset>
	
	<fieldset style="display: inline;">
	    <legend>판매계획 검색</legend>
			<input type="text" value="시작일" id="salesPlanStartDatePicker" />
			<input type="text" value="종료일" id="salesPlanEndDatePicker" />
			<input type="button" value="검색" id="salesPlanSearchButton" style="font-size : 16px;"/>
	</fieldset>
	<input type="button" value="선택한 판매계획을 MPS 에 등록" id="salesPlanToMpsButton" style="font-size : 16px;" />

	<table id = "salesPlanGrid"></table>
	<div id = "salesPlanGridPager"></div>
</div>

</body>
</html>