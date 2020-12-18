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

#tabs table{
	font-size:11px;
}

#tabs .ui-jqgrid .ui-widget-header {
		height: 30px;
		font-size: 1em;
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

	$( "#tabs" ).tabs({
		//event: "mouseover" ,
	    collapsible: true
	      
	});
	
	initGrid();
	initEvent();
	
});

function initGrid() {
	
	// 납품가능 수주 그리드 시작
	$('#deliverableContractGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "수주일련번호", "견적일련번호", "수주유형분류", "거래처코드", "거래처명", "견적일자", "수주일자", 
			"수주요청자", "수주담당자명", "비고" , "contractType","personCodeInCharge", "납품완료여부"], 
		colModel : [ 
			{ name: "contractNo", width: "100", resizable: true, align: "center"} ,
			{ name: "estimateNo", width: "100", resizable: true, align: "center"} ,
			{ name: "contractTypeName", width: "100", resizable: true, align: "center"} ,
			{ name: "customerCode", width: "80", resizable: true, align: "center", hidden : true } ,
			{ name: "customerName", width: "120", resizable: true, align: "center"} ,
			{ name: "estimateDate", width: "90", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d', defaultValue:null }  } ,
			{ name: "contractDate", width: "90", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d', defaultValue:null }  } ,
			{ name: "contractRequester", width: "90", resizable: true, align: "center" } ,
			{ name: "empNameInCharge", width: "100", resizable: true, align: "center" } ,
			{ name: "description", width: "150", resizable: true, align: "center" } ,
			{ name: "contractType", width: "100", resizable: true, align: "center" , hidden : true },
			{ name: "personCodeInCharge", width: "100", resizable: true, align: "center" , hidden : true },
			{ name: "deliveryCompletionStatus", width: "70", resizable: true, align: "center" , hidden : true  }
		], 
		caption : ' 납품 가능 수주 리스트 ', 
		sortname : 'contractNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : true, 
		rownumWidth : 30, 
		height : 100, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
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
		pager : '#deliverableContractGridPager',
		
        onSelectRow: function(rowid) {
			
			if( lastSelected_contractGrid_Id != rowid ){
				lastSelected_contractGrid_Id = rowid;
				lastSelected_contractGrid_RowValue = $(this).jqGrid('getRowData', rowid);
			}
			
			showDeliverableContractDetailGrid();
			
		}

	}); // 수주 그리드 끝
	
	$('#deliverableContractGrid').navGrid("#deliverableContractGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	


	// 납품가능 수주상세 그리드 시작
	$('#deliverableContractDetailGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ 
			"선택","수주상세일련번호","수주일련번호","품목코드","품목명","단위","납기일",
			"견적수량","재고사용량","필요제작수량","단가","합계액","처리상태","작업완료여부","납품완료여부","비고"
		], 
		colModel : [ 
			{ name: "contractDetailNo", width: "35", resizable: true, align: "center" ,
				formatter : function (cellvalue, options, rowObj) {
				// 여기서 체크박스 만들고 value값에 rowId 집어넣음
				var contractDetailCheck = "<input type='radio' name='contractDetailCheck' value="+JSON.stringify(options.rowId)+"/>";     
				return contractDetailCheck;			
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
			{ name: "description", width: "80", resizable: true, align: "center" } 
		], 
		caption : '납품 가능 수주리스트 상세', 
		sortname : 'contractDetailNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : true, 
		rownumWidth : 30, 
		height : 100, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : false,
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
		cache : false, 
		pager : '#deliverableContractDetailGridPager',
		
        onSelectRow: function(rowid) {
			
			if( lastSelected_contractDetailGrid_Id != rowid ){
				lastSelected_contracDetailGrid_Id = rowid;
				lastSelected_contracDetailGrid_RowValue = $(this).jqGrid('getRowData', id); 
			}
			
		}

	}); // 수주상세 그리드 끝
	
	$('#deliverableContractDetailGrid').navGrid("#deliverableContractDetailGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	
	// 납품현황 그리드 시작
	$('#deliveryInfoListGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "납품일련번호", "견적일련번호", "수주일련번호","수주상세일련번호","거래처코드"
			,"처리자코드","품목코드","품목명","단위","납품수량","단가","총액","납품날짜","배송지"
			], 
		colModel : [ 
			{ name: "deliveryNo", width: "100", resizable: true, align: "center"} ,
			{ name: "estimateNo", width: "100", resizable: true, align: "center"} ,
			{ name: "contractNo", width: "100", resizable: true, align: "center"} ,
			{ name: "contractDetailNo", width: "100", resizable: true, align: "center"} ,
			{ name: "customerCode", width: "70", resizable: true, align: "center"} ,
			{ name: "personCodeInCharge", width: "70", resizable: true, align: "center"} ,
			{ name: "itemCode", width: "70", resizable: true, align: "center"} ,
			{ name: "itemName", width: "100", resizable: true, align: "center"} ,
			{ name: "unitOfDelivery", width: "70", resizable: true, align: "center"} ,
			{ name: "deliveryAmount", width: "80", resizable: true, align: "center"} ,
			{ name: "unitPrice", width: "80", resizable: true, align: "center",
				formatter:'integer',formatoptions : { defaultValue: '0', thousandsSeparator: ',' } 
			} ,
			{ name: "sumPrice", width: "100", resizable: true, align: "center",
				formatter:'integer',formatoptions : { defaultValue: '0', thousandsSeparator: ',' } 
			} ,
			{ name: "deliverydate", width: "100", resizable: true, align: "center"} ,
			{ name: "deliveryPlaceName", width: "100", resizable: true, align: "center"} 
		], 
		caption : ' 납품 현황 리스트 ',
		multiselect : false, 
		multiboxonly : false,
		viewrecords : true, 
		rownumWidth : 30, 
		height : 350, 
		width : 1350,
		autowidth : false, 
		shrinkToFit : false, 
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
		pager : '#deliveryInfoListGridPager'
		
	}); // 납품현황 그리드 끝
	
	$('#deliveryInfoListGrid').navGrid("#deliveryInfoListGridPager", {
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
	
	$('#showDeliveryInfoButton').on("click", function() {
		
		showDeliveryInfoGrid();
	
	});
	
	
	$('#deliverableContractSearchButton').on("click", function() {

		showDeliverableContractGrid();
		
	});
	
	$('#deliveryButton').on("click", function() {

		deliver();
		
	});
	
}

function deliver(){
	
	
	
	var checkedContractDetailRowIdList = $('input[type=radio][name=contractDetailCheck]:checked');
	
	var contractDetailNo;
	var itemName;
	var estimateAmount;
	var processingStatus;
	var operationCompletedStatus;
	var deliveryCompletionStatus;
	
	if(checkedContractDetailRowIdList.length == 0 ) {
		
		alertError("사용자 에러","납품할 품목을 선택하지 않았습니다");
		return;
		
	}
	
	$(checkedContractDetailRowIdList).each( function() {  // 체크된애들 반복문 시작
		
		var rowId = $(this).val(); 
		var obj = $('#deliverableContractDetailGrid').getRowData(rowId);
		contractDetailNo = obj.contractDetailNo;  
 		itemName = obj.itemName;
		estimateAmount = obj.estimateAmount; // 견적수량
		processingStatus = obj.processingStatus; //처리상태	
		operationCompletedStatus = obj.operationCompletedStatus; //작업완료여부
		deliveryCompletionStatus = obj.deliveryCompletionStatus; //납부완료여부
		
	});
	
	if(processingStatus == ''){
		alertError("사용자에러","처리되지 않은 항목입니다. \r\n MPS계획수립부터 작업까지 완료해주세요. ");
		return;
	}
	else if(operationCompletedStatus == ''){
		alertError("사용자에러","작업이 완료되지 않은 항목입니다. \r\n 작업지시 및 작업완료까지 완료해주세요.");
		return;
	}
	else if(deliveryCompletionStatus == 'Y'){
		alertError("사용자에러","납품이 완료된 항목입니다. ^^");
		return;
	}
	
	var confirmMsg = "이하 항목에 대해 납품처리합니다.\r\n" + 
	"수주상세일련번호 : " + contractDetailNo + "\r\n" +
	"품목명 : " + itemName + "\r\n" +
	"수량 : " + estimateAmount + 
	"\r\n계속하시겠습니까?"

	var confirmStatus = confirm(confirmMsg);
	
	// 확인누르면 ajax 시작~
	if( confirmStatus == true ) {
	
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/sales/deliver.do',
		data : {
			method : 'deliver', 
			contractDetailNo : contractDetailNo //체크된 열의 수주상세번호를 가져감
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);	
			
			if(dataSet.errorCode < 0){
				alertError('실패',dataSet.errorMsg);
				return;
			}
			
			alertError("성공",dataSet.errorMsg); 
			
			showDeliverableContractGrid(); //납품가능주수조회

		}					
	}); // ajax 끝
	
	}
	
}

function showDeliveryInfoGrid(){
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/sales/searchDeliveryInfoList.do' ,
		async :false,
		data : {
			method : 'searchDeliveryInfoList'
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			
			gridRowJson = dataSet.gridRowJson;

			$('#deliveryInfoListGrid').jqGrid('clearGridData');
			
			// 수주 Data 넣기
			$('#deliveryInfoListGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid');
			
		}});  
	
	
}

function showDeliverableContractGrid() {
	
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
	$('#deliverableContractGrid').jqGrid('clearGridData');
	$('#deliverableContractDetailGrid').jqGrid('clearGridData');
	lastSelected_contractGrid_Id = "";
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/sales/searchDeliverableContractList.do' ,
		async :false,
		data : {
			method : 'searchDeliverableContractList', 
			searchCondition : $(':input:radio[name=searchCondition]:checked').val() ,//searchByDate=기간, searchByCustomer=거래처
			startDate : $('#startDatePicker').val(),
			endDate : $('#endDatePicker').val(),
			customerCode : $('#customerCodeBox').val()
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			
			gridRowJson = dataSet.gridRowJson;
			alert(JSON.stringify(dataSet.gridRowJson));
				// 수주 Data 넣기
				$('#deliverableContractGrid')
					.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
					.trigger('reloadGrid');
					
		}
	});  
	
}

function showDeliverableContractDetailGrid() {
	
	$('#deliverableContractDetailGrid').jqGrid('clearGridData');

	// gridRowJson 배열 Data 중의 어떤 객체 ( obj ) 의 contractNo 가 
	// 가장 최근에 선택한 contractGrid 행의 contractNo 와 같으면
	
	$( gridRowJson ).each( function( index, obj )   {  // gridRowJson 의 전체 데이터에 대해 반복문 시작
		
		if( obj.contractNo == lastSelected_contractGrid_RowValue.contractNo ) {
			
			// obj 의 contractDetailTOList : 선택된 contractNo 에 해당하는 수주상세 Data 
			$('#deliverableContractDetailGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : obj.contractDetailTOList })
				.trigger('reloadGrid');
			
		} 
		
	});  // 반복문 끝	
	
}

/* 
function showCodeDialogForInput(source, divisionCodeNo, title){
							   //this, 'CL-01', '거래처 검색'
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
<div id="tabs">
	
	<ul>
    	<li><a href="#tabs-1">납품</a></li>
    	<li><a href="#tabs-2">납품현황</a></li>
  </ul>
  <div id="tabs-1">
  
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
	
		<input type="button" value="납품 가능 수주 조회" id="deliverableContractSearchButton" />
		<input type="button" value="납품" id="deliveryButton" />
	 
		<table id="deliverableContractGrid" ></table>
		<div id="deliverableContractGridPager"></div>

		<table id="deliverableContractDetailGrid" ></table>
		<div id="deliverableContractDetailGridPager"></div>
		
	</div>
	
	<div id="tabs-2">
	
		<input type="button" value="납품 현황 조회" id="showDeliveryInfoButton" />
		
		<table id="deliveryInfoListGrid" ></table>
		<div id="deliveryInfoListGridPager"></div>

	</div>
	
</div>

<div id="codeDialog">
	<table id="codeGrid"></table>
</div>

</body>
</html>