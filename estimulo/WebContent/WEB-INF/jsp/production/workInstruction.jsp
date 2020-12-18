<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>작업지시 (WorkOrder)</title>
<style>

#StartDatePicker, #EndDatePicker {
	display: inline;
	width: 115px;
	transition: 0.6s;
	outline: none;
	height: 25px;
	font-size: 16px;
	text-align : center;
	
}

#itemCodeSearchBox, #itemAmountBox, #parentItemCodeSearchBox {
	display: inline;
	width: 160px;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
	text-align : center;
	
}

#tabs table{
	font-size:11px;
}

#tabs .ui-jqgrid .ui-widget-header {
		height: 30px;
		font-size: 1em;
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
function showModalCode(codeName,divisionCode){
    var option="width=550; height=430; left=500; top=200; titlebar=no; toolbar=no,status=no,menubar=no,resizable=yes, location=no";
    window.open("${pageContext.request.contextPath}/basicInfo/codeModal.html?code="+codeName+"&divisionCode="+divisionCode,"newwins",option);
}
var gridRowJson;  // Json형태의데이터

var mrpNo; // 모의 작업 지시 하기위해 사용하는 mrpNo

$(document).ready(function() {
	
	
	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	$("input[type=radio]").checkboxradio();   // jqueryUI Checkboxradio 위젯 적용

	$("#workOrderDialog").dialog({
		title : '작업지시 시뮬레이션',
		autoOpen : false, 
		width : 1400,
		height : 530,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"닫기" : function() {
				$("#workOrderDialog").dialog("close");
			}
		},	
	
		open : function(event, ui) {
					
			$('#workOrderDateInputPicker').datepicker({ 
        		changeMonth : true,
				numberOfMonths : 1,
				minDate : new Date()
        	});		

			$('#workOrderDateInputPicker').hide();

		}
	
	});
	
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
	
	$("#StartDatePicker").datepicker({
		changeMonth : true,
		numberOfMonths : 1,
		onClose : function(selectedDate) {
			$( "#EndDatePicker" ).datepicker( "option", "minDate", selectedDate );
		}
	});		
	
	$("#EndDatePicker").datepicker({
		changeMonth : true,
		numberOfMonths : 1
	});		
	
	$("#codeDialog").hide();
	
	$("#workSiteDialog").hide();
	
	$( "#tabs" ).tabs({
		//event: "mouseover" ,
	    collapsible: true
	      
	});
	
	
	initGrid();
	initEvent();
	
});

function initGrid() {

	// orderListGrid 그리드 시작
	$('#workOrderListGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "선택","소요량전개번호","주생산계획번호","소요량취합번호","품목분류",
			"품목코드","품목명","단위","필요수량","작업지시기한","작업완료기한"], 
		colModel : [
			{ name: "mrpCheck", width: "35", resizable: true, align: "center" ,
				formatter : function (cellvalue, options, rowObj) {
				// 여기서 체크박스 만들고 value값에 rowId 집어넣음
				var mrpCheck = "<input type='radio' name='mrpCheck' value="+JSON.stringify(options.rowId)+"/>";     
				return mrpCheck;
					
				}
			},
			{ name: "mrpNo", width: "80", resizable: true, align: "center" } ,
			{ name: "mpsNo", width: "80", resizable: true, align: "center" } ,  
			{ name: "mrpGatheringNo", width: "80", resizable: true, align: "center" } , 
			{ name: "itemClassification", width: "80", resizable: true, align: "center" } , 
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "110", resizable: true, align: "center" } ,  
			{ name: "unitOfMrp", width: "80", resizable: true, align: "center" } ,
			{ name: "requiredAmount", width: "80", resizable: true, align: "center" } ,
			{ name: "orderDate", width: "80", resizable: true, align: "center" } , 
			{ name: "requiredDate", width: "80", resizable: true, align: "center" }  			
		], 
		caption : ' 작업지시 필요 리스트 ( Mrp 기반 ) ', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1250,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 10,   
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
		pager : '#workOrderListPager',
		cache : false
	});
	// orderListGrid 끝
	
	// orderList 그리드의 페이저 생성
	$('#workOrderListGrid').navGrid("#workOrderListPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	// workOrderSimulationGrid 그리드 시작
	$('#workOrderSimulationGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "소요량전개번호","주생산계획번호","소요량취합번호","품목분류",
			"품목코드","품목명","단위","재고량(투입예정재고)","재고소요/제품제작수량",
			"재고량(재고소요이후)","작업지시기한","작업완료기한"], 
		colModel : [
			{ name: "mrpNo", width: "100", resizable: true, align: "center" } ,
			{ name: "mpsNo", width: "100", resizable: true, align: "center" } ,  
			{ name: "mrpGatheringNo", width: "100", resizable: true, align: "center" } , 
			{ name: "itemClassification", width: "80", resizable: true, align: "center" } , 
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "110", resizable: true, align: "center" } ,  
			{ name: "unitOfMrp", width: "80", resizable: true, align: "center" } ,
			{ name: "inputAmount", width: "140", resizable: true, align: "right" } ,
			{ name: "requiredAmount", width: "150", resizable: true, align: "right" } , 
			{ name: "stockAfterWork", width: "140", resizable: true, align: "right" },
			{ name: "orderDate", width: "88", resizable: true, align: "center" },
			{ name: "requiredDate", width: "88", resizable: true, align: "center" }  			
		], 
		caption : ' 작업지시 모의전개 ( Mrp 기반 ) ', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1380,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : true,
		// rowNum : 10,   
		scrollerbar: true, 
		// rowList : [ 10, 20, 30 ],
		viewrecords : true, 
		editurl : 'clientArray', 
		cellsubmit : 'clientArray',
		rownumbers : true, 
		autoencode : true, 
		resizable : true,
		loadtext : '로딩중...', 
		emptyrecords : '데이터가 없습니다.',
		cache : false
	});
	// workOrderSimulationGrid 끝

	// workOrderListInfoGrid 그리드 시작
	$('#workOrderListInfoGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "선택","작업지시일련번호","소요량전개번호","주생산계획번호","소요량취합번호",
			"품목분류","품목코드","품목명","단위","지시수량","생산공정코드","생산공정명","작업장코드","작업장명","완료상태","작업완료된수량"], 
		colModel : [
			{ name: "orderNoCheck", width: "35", resizable: true, align: "center" ,
				
				formatter : function (cellvalue, options, rowObj) {	
				var orderNoCheck = "<input type='radio' name='orderNoCheck' value="+JSON.stringify(options.rowId)+"/>";     
				return orderNoCheck;
				}
			
			},
			{ name: "workOrderNo", width: "100", resizable: true, align: "center" } ,
			{ name: "mrpNo", width: "100", resizable: true, align: "center" } ,
			{ name: "mpsNo", width: "100", resizable: true, align: "center" } ,  
			{ name: "mrpGatheringNo", width: "100", resizable: true, align: "center" } , 
			{ name: "itemClassification", width: "80", resizable: true, align: "center" } , 
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "110", resizable: true, align: "center" } ,  
			{ name: "unitOfMrp", width: "80", resizable: true, align: "center" } ,
			{ name: "requiredAmount", width: "80", resizable: true, align: "center" } ,
			{ name: "productionProcessCode", width: "80", resizable: true, align: "center" , hidden: true} ,
			{ name: "productionProcessName", width: "80", resizable: true, align: "center" } ,
			{ name: "workSiteCode", width: "80", resizable: true, align: "center" , hidden: true} ,
			{ name: "workStieName", width: "80", resizable: true, align: "center" } ,
			{ name: "completionStatus", width: "80", resizable: true, align: "center" } ,
			{ name: "actualCompletionAmount", width: "100", resizable: true, align: "center" , editable: true,
				 edittype: 'text', 
		         editoptions: { size: 12, maxlengh: 12}
			}
		], 
		caption : ' 작업지시현황 ',    
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1400,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : true,
		//rowNum : 10,   
		scrollerbar: true, 
		//rowList : [ 10, 20, 30 ],
		viewrecords : true, 
		editurl : 'clientArray', 
		cellsubmit : 'clientArray',
		rownumbers : true, 
		autoencode : true, 
		resizable : true,
		loadtext : '로딩중...', 
		emptyrecords : '데이터가 없습니다.',
		cache : false
	});
	// workOrderListInfoGrid 끝
	
	//  productionPerformanceInfoGrid 그리드 시작
	$('#productionPerformanceInfoGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "생산완료날짜","작업지시일련번호","주생산계획번호","수주상세일련번호",
			"품목구분","품목코드","품목명","단위","작업지시수량","실제제작수량","공정성공율"], 
		colModel : [
			{ name: "workOrderCompletionDate", width: "80", resizable: true, align: "center" } ,
			{ name: "workOrderNo", width: "100", resizable: true, align: "center" } ,  
			{ name: "mpsNo", width: "100", resizable: true, align: "center" } , 
			{ name: "contractDetailNo", width: "100", resizable: true, align: "center" } , 
			{ name: "itemClassification", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemCode", width: "110", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "80", resizable: true, align: "center" } ,
			{ name: "unit", width: "80", resizable: true, align: "center" } ,
			{ name: "workOrderAmount", width: "80", resizable: true, align: "center" } , 
			{ name: "actualCompletionAmount", width: "80", resizable: true, align: "center" } , 			
			{ name: "workSuccessRate", width: "80", resizable: true, align: "center" }  			
		], 
		caption : ' 생산실적 리스트 ', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1400,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 10,   
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
		pager : '#productionPerformanceInfoPager',
		cache : false
	});
	// productionPerformanceInfoGrid 끝
	
	// productionPerformanceInfoGrid 그리드의 페이저 생성
	$('#productionPerformanceInfoGrid').navGrid("#productionPerformanceInfoPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
}

function initEvent() {
		
	$('#showWorkOrderSimulationByMrpButton').on("click", function() {
		
		showWorkOrderSimulationByMrpGrid();
		
	});
	
	$('#showProductionPerformanceInfoButton').on("click", function() {
		
		showProductionPerformanceInfoGrid();
		
	});
	
	$('#workOrderCompletionButton').on("click", function() {
		
		workOrderCompletion();
		
	});
	
	//workOrderDialog dateInputPicker
	$('#workOrderDateAsTodayRadio').on("click", function() {
		
		// 오늘 일자 생성
		var now = new Date();
		var today = now.getFullYear() + "-" +('0' + (now.getMonth() +1 )).slice(-2) + "-" + now.getDate();
		
		$('#workOrderDateInputPicker').hide();
		$('#workOrderDateInputPicker').val(today);
	
	});
	
	$('#getWorkOrderListButton').on("click", function() {

		showWorkOrderListGrid();
		
	});
	
	
	$('#getWorkOrderInfoListButton').on("click", function() {

		showWorkOrderInfoListGrid("#workOrderListInfoGrid");
		
	});
	
	$('#productionProcess').on("click", function() {

        showModalCode("productionProcess","PP");//생산공정코드
	});

	
	// 작업지시 
	$('#workOrderButton').on("click", function() {
		
		if( $('#workOrderDateInputPicker').val() == '작업지시일자' ) {
			alertError("사용자 에러", "작업지시일자를 선택하세요");
			return;
		}
				
		// workOrderSimulationGrid 의 전체 데이터
		var simulRowData = $('#workOrderSimulationGrid').getRowData();
		
		console.log(simulRowData);

		var status;
		
		$(simulRowData).each( function(index,obj) { 
			
			//alert(obj.stockAfterWork); 
			if(obj.stockAfterWork == "재고부족"){	
				alertError("재고부족","재고가 부족합니다. \r\n" +
							"재고가 충분할 때 작업지시 해주세요 ^^");
				status = true;			
			}
			
		});

		if(status){
			return;
		}
		
		var confirmMsg = "작업시지를 내립니다.\r\n" + 
			"일자 : " + $('#workOrderDateInputPicker').val()  + "\r\n" +
			"\r\n계속하시겠습니까?";
		
		var confirmStatus = confirm(confirmMsg); //확인창 띄워서 그 결과를 담음
		
		if( confirmStatus == true ) {
			
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/production/workOrder.do' ,
				data : {
					
					method : 'workOrder',
					workPlaceCode : "${sessionScope.workplaceCode}",
					productionProcessCode : $("#productionProcessCode").val()
					
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {
					
					console.log(dataSet);	
					
					$("#workOrderDialog").dialog("close");
					
					var resultMsg ="작업지시완료 => 작업장에서 검사 및 제작 상황을 볼 수 있습니다.";
					
					alertError("성공", resultMsg);
					
					showWorkOrderListGrid();

				}
			}); // ajax 끝
			
		}
		
	}); // 작업지시 끝
	
	
}

function showProductionPerformanceInfoGrid(){
		
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/production/getProductionPerformanceInfoList.do',
		data : {
			method : 'getProductionPerformanceInfoList'
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			var gridRowJson = dataSet.gridRowJson;

			// 그리드 초기화
			$('#productionPerformanceInfoGrid').jqGrid('clearGridData');		
			
			// 작업지시 시뮬레이션 Data 넣기
			$('#productionPerformanceInfoGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid'); 
				
		}					
	}); // ajax 끝
		
}

function workOrderCompletion(){
	
	var workOrder;
	var status;
	var actualCompletionAmount;
	var checkedRow = $('input[type=radio][name=orderNoCheck]:checked');
	if(checkedRow.length == 0 ) {
		alertError("사용자 에러","작업완료할 물품을 선택하지 않았습니다");
		return;
	}
	
	$(checkedRow).each( function() { 
		
		var rowId = $(this).val(); 
		workOrder = $('#workOrderListInfoGrid').getRowData(rowId);
		actualCompletionAmount = $('#workOrderListInfoGrid').getRowData(rowId).actualCompletionAmount;
		if(workOrder.completionStatus == ''){
			alertError("사용자에러","작업공정이 다 끝난거같지 않습니다. \n작업장을 방문해 주세요! ");
			status = true;
		}
		else if ( actualCompletionAmount == ""){ 		
			alertError("사용자에러","작업완료수량을 적지 않았습니다.");
			status = true;
		}
		
	});
	
	if(status){
		return;
	}
	
	var confirmMsg = "작업을 완료합니다.\r\n" + 
	"작업일련번호 : " + workOrder.workOrderNo  + "\r\n" +
	"작업완료된수량 : " + actualCompletionAmount  + "\r\n" +
	"\r\n계속하시겠습니까?";

	var confirmStatus = confirm(confirmMsg);
	
	if( confirmStatus == true ) {
		
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/production/workOrderCompletion.do',
		data : {
			method : 'workOrderCompletion', 
			workOrderNo : workOrder.workOrderNo , //작업지시일련번호
			actualCompletionAmount : actualCompletionAmount //입력한 작업완료수량
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
		
			if(dataSet.errorCode < 0){
				alertError('실패',dataSet.errorMsg);
				return;
			}

			alertError("성공",workOrder.workOrderNo+" 작업완료");  
			
			showWorkOrderInfoListGrid();
			
		}					
	}); // ajax 끝
	
	}
}

function showWorkOrderInfoListGrid(gridName){
	
	$('#workOrderListInfoGrid').jqGrid('clearGridData');
	
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/production/showWorkOrderInfoList.do',
		data : {
			method : 'showWorkOrderInfoList'
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			var gridRowJson = dataSet.gridRowJson;

			// 그리드 초기화
			$(gridName).jqGrid('clearGridData');		
			
			// 작업지시 시뮬레이션 Data 넣기
			$(gridName)
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid'); 
			
		}					
	}); // ajax 끝
	
}


function showWorkOrderSimulationByMrpGrid() {
	
	$('#workOrderSimulationGrid').jqGrid('clearGridData');
	$("#productionProcess").val("");
	
	var checkedMrpRowIdList = $('input[type=radio][name=mrpCheck]:checked');
	if(checkedMrpRowIdList.length == 0 ) {
		alertError("사용자 에러","모의 작업지시할 물품을 선택하지 않았습니다");
		return;
	}
	
	$(checkedMrpRowIdList).each( function() { 
		
		var rowId = $(this).val(); 
		mrpNo = $('#workOrderListGrid').getRowData(rowId).mrpNo ; //소요량전개번호
		
	});
	
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/production/showWorkOrderDialog.do',
		data : {
			method : 'showWorkOrderDialog', 
			mrpNo : mrpNo //체크된 row의 소요량전개번호를 가져감
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			
			if(dataSet.errorCode < 0){
				alertError('실패',dataSet.errorMsg);
				return;
			}
			
			gridRowJson = dataSet.gridRowJson;
			
			
			// 작업지시 시뮬레이션 Data 넣기
			$('#workOrderSimulationGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid'); 
			
			// 작업지시 Dialog Open
			$("#workOrderDialog").dialog("open");

		}					
	}); // ajax 끝
	
}

function showWorkOrderListGrid() {
	
	// ajax 시작
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/production/getWorkOrderableMrpList.do' ,
		data : {
			method : 'getWorkOrderableMrpList'
			},
		dataType : 'json', 
		cache : false, 
		async : false,
		success : function(dataSet) { 
			
			console.log(dataSet);
			
			if(dataSet.errorCode < 0){
				alertError('실패',dataSet.errorMsg);
				return;
			}
			
			gridRowJson = dataSet.gridRowJson;  // gridRowJson : 그리드에 넣을 json 형식의 data
			
			$('#workOrderListGrid').jqGrid('clearGridData');
			// Data 넣기
			$('#workOrderListGrid')
				.jqGrid('setGridParam',{ datatype : 'jsonstring', datastr : gridRowJson })
				.trigger('reloadGrid');
		
		}
	});  // ajax 끝
	
}
/* 
function showProductionProcessListGrid() {
	
	$("#codeDialog").dialog({
		title : '코드 검색',
		width : 500,
		height : 500,
		modal : true   // 폼 외부 클릭 못하게
	});
	
	$.jgrid.gridUnload("#codeGrid");
	
	$("#codeGrid").jqGrid({
        url : "${pageContext.request.contextPath}/base/codeList.do",
        datatype : "json",
        jsonReader : { root: "detailCodeList" },//받아온 제이손의 키값 detailCodeList을 읽어들임
        postData : { 
    		method : "findDetailCodeList" ,
    		divisionCode : "PP"
    	},
		colNames : [ '상세코드번호' , '상세코드이름' , '사용여부' ],
		colModel : [
			{ name : 'detailCode', width:100, align : "center",editable:false},
			{ name : 'detailCodeName', width:100, align : "center", editable:false},
			{ name : 'codeUseCheck', width:100, align : "center",editable:false},
		],
		width : 450,
		height : 300,
		caption : "생산공정코드",
		align : "center",
		viewrecords : true,
		rownumbers : true,
		onSelectRow : function(id) {
			
				
				var detailCode=$("#codeGrid").getCell(id, 1);
				var detailName=$("#codeGrid").getCell(id, 2);
				var codeUseCheck=$("#codeGrid").getCell(id, 3);
				
				if(codeUseCheck != 'N' && codeUseCheck != 'n') {
					
							$("#productionProcess").val(detailName);					
							$("#productionProcessCode").val(detailCode);					
							$("#codeDialog").dialog("close");	
					
				} else {
					alertError("사용자 에러", "사용 가능한 코드가 아닙니다");		
				}
			}
		});
	
} */

</script>
</head>
<body>


<div id="tabs">
	<ul>
    	<li><a href="#tabs-1">작업지시</a></li>
    	<li><a href="#tabs-2">작업지시현황</a></li>
    	<li><a href="#tabs-3">생산실적관리</a></li>
  </ul>

	<div id="tabs-1">
	
		<fieldset style="display: inline;">
		    <legend>작업지시 필요품목 검색  * 작업지시 (BY MRP) </legend>
				<input type="button" value="작업지시필요목록조회 " id="getWorkOrderListButton" />
				<input type="button" value="모의 작업지시 (BY MRP)" id="showWorkOrderSimulationByMrpButton" />
		</fieldset>
				
		<table id="workOrderListGrid"></table>
		<div id="workOrderListPager"></div>
		
	<div id="codeDialog">
		<table id="codeGrid"></table>
	</div>

	</div>
	
	<div id="tabs-2">

		<fieldset style="display: inline;">
		    <legend>작업지시현황 조회 및 작업완료 등록</legend>
				<input type="button" value="작업지시현황조회 " id="getWorkOrderInfoListButton" />
				<input type="button" value="작업완료등록" id="workOrderCompletionButton" />
		</fieldset>
				
		<table id="workOrderListInfoGrid" ></table>
		
	</div>
	
	<div id="tabs-3">
	
		<fieldset style="display: inline;">
		  	 	<legend>생산실적 조회</legend>
				<input type="button" value="생산실적조회 " id="showProductionPerformanceInfoButton" />
		</fieldset>
				
		<table id="productionPerformanceInfoGrid" ></table>
		<div id="productionPerformanceInfoPager"></div>
	
	</div>
	


</div>

<div id = "workOrderDialog">

	<fieldset style="display: inline;">
			<legend>작업지시 등록 일자</legend>
    			<label for="workOrderDateAsTodayRadio">현재일자</label>
    			<input type="radio" 
    			name="workOrderDateCondition" 
    			value="workOrderDateAsToday" 
    			id="workOrderDateAsTodayRadio">
				<input type="text" value="작업지시일자" id="workOrderDateInputPicker" />
	</fieldset>
		
		<fieldset style="display: inline;">
			<legend> 작업지시 </legend>
	  			<input type="button" value="현재 모의전개된 결과 작업지시" id="workOrderButton" />
		</fieldset>
		
		<fieldset style="display: inline;">
			<legend> 사업장 및 생산공정 </legend>
	  			<input type="text" placeholder="사업장코드" id="workPlaceName" value="${sessionScope.workplaceName}" />
	  			<input type="text" placeholder="생산공정코드" id="productionProcess" />
	  			<input type="hidden" id="productionProcessCode" value="" />
		</fieldset>
	
	<table id = "workOrderSimulationGrid"></table>

</div>

</body>
</html>