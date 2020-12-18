<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>발주 (Order)</title>
<style>

#StartDatePicker, #EndDatePicker, #orderInfoStartDatePicker, #orderInfoEndDatePicker {
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

var gridRowJson;  // Json형태의데이터
var mrpNoList; // 발주하기위해 사용하는 mrpNoList

var standardUnitPrice; // 단가

$(document).ready(function() {
	
	
	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	$("input[type=radio]").checkboxradio();   // jqueryUI Checkboxradio 위젯 적용

	$("#order-dialog").dialog({
		title : '발주 시뮬레이션',
		autoOpen : false, 
		width : 1030,
		height : 550,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"닫기" : function() {
				$("#order-dialog").dialog("close");
			}
		},	
	
		open : function(event, ui) {
					
			$('#orderDateInputPicker').datepicker({ 
        		changeMonth : true,
				numberOfMonths : 1,
				minDate : new Date()
        	});		

			$('#orderDateInputPicker').hide();

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
		numberOfMonths : 2,
		onClose : function(selectedDate) {
			$( "#EndDatePicker" ).datepicker( "option", "minDate", selectedDate );
		}
	});		
	
	$("#orderInfoStartDatePicker").datepicker({
		changeMonth : true,
		numberOfMonths : 2,
		onClose : function(selectedDate) {
			$( "#orderInfoEndDatePicker" ).datepicker( "option", "minDate", selectedDate );
		}
	});	
	
	$("#EndDatePicker , #orderInfoEndDatePicker").datepicker({
		changeMonth : true,
		numberOfMonths : 2
	});		
	
	$("#codeDialog").hide();
	
	$( "#tabs" ).tabs({
		//event: "mouseover" ,
	    collapsible: true
	      
	});
	
	
	
	initGrid();
	initEvent();
	
});

function initGrid() {

	// orderListGrid 그리드 시작
	$('#orderListGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "선택","소요량취합번호","품목코드","품목명",
			"단위","필요수량","현재고량","발주기한","입고기한"], 
		colModel : [
			{ name: "mrpCheck", width: "35", resizable: true, align: "center" ,
				formatter : function (cellvalue, options, rowObj) {
				// 여기서 체크박스 만들고 value값에 rowId 집어넣음
			//celvalue=현재 formatter가 걸려있는 컬럼의 값,
            //options = options: {rowId: rid, colModel: cm} Element를 포함하는 객체  
            //					 rowId: row의 id  colModel: jqGrid의 colModel배열의 컬럼 속성 객체
            //rowObj = 해당 행의 데이터타입에서 결정된 형식으로 표현된 rowdata, 
            //첫번째 칼럼에 사용자설정fomatter를 써서 함수로 체크박스 만드는 행
				var orderCheck = "<input type='checkbox' name='mrpCheck' value="+JSON.stringify(options.rowId)+"/>";     
				return orderCheck;
					
				}
			},
			{ name: "mrpGatheringNo", width: "80", resizable: true, align: "center" } ,	
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "80", resizable: true, align: "center" } ,  
			{ name: "unitOfMrp", width: "80", resizable: true, align: "center" } ,  
			{ name: "requiredAmount", width: "80", resizable: true, align: "center" } ,  
			{ name: "stockAmount", width: "80", resizable: true, align: "center" } ,  
			{ name: "orderDate", width: "80", resizable: true, align: "center" } , 
			{ name: "requiredDate", width: "80", resizable: true, align: "center" }  			
		], 
		caption : '발주 리스트 ( MrpGathering 기반 )', 
		multiselect : false, 
		multiboxonly : true,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 10,   
		scrollerbar: true, 
		rowList : [ 10, 20, 30 ],
		viewrecords : true, 
		editurl : 'clientArray', //'clientArray'로 설정하면 데이터는 서버에 요청을 보내지 않고 나중에 수동저장을 위해 그리드에만 저장된다.
		cellsubmit : 'clientArray',//셀의 내용이 저장되는 위치를 결정 Ajax요청이 없으며 변경된 셀의 내용은 getChangedCells 메소드 또는 이벤트를 통해 얻을 수 있다.
/* 		cellSubmit  : 'clientArray'
 			1. formatCell: 편집모드에서 사용될 셀값을 변경가능
 			2. beforeEditCell: 셀이 편집모드로 변경되기 직전에 호출
			 3. afterEditCell: 입력요소가 생성된 직후에 호출
			 4. beforeSaveCell: 사용자가 셀을 저장하기전에 호출
			 5. beforeSubmitCell: 여기에 값을 저장할 수 있음
			 6. afterSaveCell: 셀의 값이 beforeSubmitCell에 의해 성공적으로 저장되면 호출
			 7. onSelectCell: 셀을 편집할 수 없을 때 호출 */
  
		rownumbers : true, 
		autoencode : true, 
		resizable : true,
		loadtext : '로딩중...', 
		emptyrecords : '데이터가 없습니다.',
		pager : '#orderListPager',
		cache : false
	});
	// orderListGrid 끝
	
	// orderList 그리드의 페이저 생성
	$('#orderListGrid').navGrid("#orderListPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});

	// orderDialog 그리드 시작
	$('#orderDialogGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "선택된 취합번호","아이템코드", "아이템이름", "단위", "총발주필요량", "사용가능재고량", 
			"실제발주필요량", "단가", "합계금액"], 
		colModel : [ 
			{ name: "mrpGatheringNo", width: "120", resizable: true, align: "center" ,hidden : true},
			{ name: "itemCode", width: "110", resizable: true, align: "center"} ,
			{ name: "itemName", width: "110", resizable: true, align: "center" } ,
			{ name: "unitOfMrp", width: "80", resizable: true, align: "center"} ,
			{ name: "requiredAmount", width: "80", resizable: true, align: "center"} ,
			{ name: "stockAmount", width: "80", resizable: true, align: "center"} ,
			{ name: "calculatedRequiredAmount", width: "110", resizable: true, align: "center", editable: true } ,
			{ name: "standardUnitPrice", width: "110", resizable: true, align: "center", editable: true } ,
			{ name: "sumPrice", width: "120", resizable: true, align: "center"},
			
		], 
		caption : 'Order Dialog', 
		//sortname : 'mpsNo',   // 여기를 지정하면 쿼리에서 정렬한 결과와 순서 달라짐
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 270, 
		width : 1000,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : false,
		rowNum : 50,   // -1 : 모든 로우 한번에 표시, 그런데 잘 안먹히는 경우 많음
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
		cache : false,
		pager : '#orderPager'
	}); // orderDialog 그리드 끝
	
	$('#orderDialogGrid').navGrid("#orderPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	// orderInfoGrid 그리드 시작
	$('#orderInfoGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "발주번호","발주날짜","현재상태","발주유형","품목코드",
			"품목명","발주수량"], 
		colModel : [
			{ name: "orderNo", width: "80", resizable: true, align: "center" } ,
			{ name: "orderDate", width: "80", resizable: true, align: "center" } ,  
			{ name: "orderInfoStatus", width: "80", resizable: true, align: "center" } ,  
			{ name: "orderSort", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "80", resizable: true, align: "center" } ,  
			{ name: "orderAmount", width: "80", resizable: true, align: "center" }			
		], 
		caption : '발주현황 리스트', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1370,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 15,   
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
		pager : '#orderInfoPager',
		cache : false
	});
	
	$('#orderInfoGrid').navGrid("#orderInfoPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
}

function initEvent() {
	 $('#checkAll').on("click",function(){
	      if($('input[type=checkbox][name=mrpCheck]:checked').length!=0)
	         $("input:checkbox[name='mrpCheck']").prop("checked",false);
	      else
	         $("input:checkbox[name='mrpCheck']").prop("checked",true);

	   });
	
	$('#itemCodeSearchBox').on("click", function() {
	      
		showCodeDialogForInput(this);
	      
	});
	
	$('#itemAmountBox').on("focus", function() {
	    
		if($('#itemCodeSearchBox').val() == "품목코드 검색"){

			$('#itemAmountBox').blur();
			alertError("실패","품목을 먼저 선택하셔야 합니다.");
			return;
			
		}
		
		if($('#itemCodeSearchBox').val() != "품목코드 검색"){
		
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/logisticsInfo/getStandardUnitPrice.do' ,
			data : {
				method : 'getStandardUnitPrice',
				itemCode : $('#itemCodeSearchBox').val()
			},
			dataType : 'json', 
			cache : false, 
			success : function(dataSet) {
				
				console.log(dataSet);	
				
				standardUnitPrice = dataSet.gridRowJson;
				
				
			}
			
		}); // ajax 끝
		
		}   
	});
	
	//order-Dialog dateInputPicker
	$('#orderDateAsTodayRadio').on("click", function() {
		
		// 오늘 일자 생성
		var now = new Date();
		var today = now.getFullYear() + "-" +('0' + (now.getMonth() +1 )).slice(-2) + "-" + now.getDate();
		
		$('#orderDateInputPicker').hide();
		$('#orderDateInputPicker').val(today);
	
	});
	
	$('#getOrderListButton').on("click", function() {

		showOrderListGrid();
		
	});
	
	$('#OrderDialogButton').on("click", function() {

		showOrderDialog();
		
	});
	
	$('#viewOrderInfoButton').on("click", function() {

		showOrderInfoGrid();
		
	});
	
	$('#orderButton').on("click", function() {
		var mrpGatheringData = $('#orderDialogGrid').getRowData();
		var mrpGatheringNoListArr = new Array();
		mrpGatheringNoListArr = $('#orderDialogGrid').getRowData();
		var mrpGatheringNoList=[]
		$.each(mrpGatheringData, function(index,item) {  
			
			mrpGatheringNoList.push(mrpGatheringNoListArr[index].mrpGatheringNo);
			
			//mrpGatheringNoList에 mrpGatheringNo들을 담음
		});
		
		console.log(mrpGatheringNoList);
		
		if( $('#orderDateInputPicker').val() == '발주일자' ) {
			alertError("사용자 에러", "발주일자를 선택하세요");
			return;
		}
			
		
		// orderDialog 의 전체 데이터
	
		


		
		
		var confirmMsg = "아래품목들에 대해 처리합니다.\r\n" + 
			"일자 : " + $('#orderDateInputPicker').val()  + "\r\n" +
			"\r\n계속하시겠습니까?" 
			
				
		
		var confirmStatus = confirm(confirmMsg);
	
		
		

		
		if( confirmStatus == true ) {
			
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/purchase/order.do' ,
				data : {
					method : 'order',
					mrpGatheringNoList : JSON.stringify(mrpGatheringNoList)
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {
	
					if(dataSet.errorCode < 0){
						alertError('실패',dataSet.errorMsg);
						return;
					}
					
					$("#order-dialog").dialog("close");
				
					alertError("성공","재고처리 및 발주완료");
					
					showOrderListGrid();

				}
			}); // ajax 끝
			
		}
		
	});
	
	//OptionOrderButton
	$('#OptionOrderButton').on("click",function(){
		
		
	if($("#itemCodeSearchBox").val()=="품목코드 검색"||$("#itemCodeSearchBox").val().length=='0'){
		alertError("사용자에러","품목코드를 입력하세요.");
	return;
	}else if($("#itemAmountBox").val()=="수량"||$("#itemAmountBox").val().length=='0' ){
		alertError("사용자에러","수량을 기입해주세요.");
	return;
	}
	      // 오늘 일자 생성
	      var now = new Date();
	      var today = now.getFullYear() + "-" +('0' + (now.getMonth() +1 )).slice(-2) + "-" + now.getDate();
	
	      var confirmMsg = "선택한 품목에 대해 처리합니다.\r\n" + 
	      "일자 : " + today   + "\r\n" +
	      "임의 발주 품목 : " +$("#itemCodeSearchBox").val() + "\r\n" +
	      "임의 발주량 : " +$("#itemAmountBox").val() + "\r\n" +
	      "단가 : " + standardUnitPrice + "\r\n" +
	      "합계액 : " + ($("#itemAmountBox").val()*standardUnitPrice) + "\r\n" +
	      "\r\n계속하시겠습니까?"
	            
	      var confirmStatus = confirm(confirmMsg);
	      
	      
	      if( confirmStatus == true ) {
	         $.ajax({
	            type : 'POST',
	            url : '${pageContext.request.contextPath}/purchase/optionOrder.do',
	            data : {
	               method : 'optionOrder',
	               itemCode : $("#itemCodeSearchBox").val(),
	               itemAmount : $("#itemAmountBox").val()
	            },
	            dataType : 'json',
	            cache : false,
	            success : function(dataSet){
	               
	               console.log(dataSet);
	               
	           		if(dataSet.errorCode < 0){
	           			
	        		alertError('실패',dataSet.errorMsg);
	        		return;
	        		
	        		}
	              
	               alertError("성공","임의발주완료");
	               
	            }
	            
	         }); // ajax 끝   
	      }
	   
	}); 

	$('#itemAmountBox').on("focus",function(){
		$('#itemAmountBox').val("");
	});


}////



function showOrderInfoGrid() {
	
	if($("#orderInfoStartDatePicker").val() == "시작일"){
		
		alertError("사용자에러","날짜를 선택해주세요!");
		return;
		
	}
	
	if($("#orderInfoEndDatePicker").val() == "종료일"){
		
		alertError("사용자에러","날짜를 선택해주세요!");
		return;
		
	}
	
	$.ajax({
		
		type : 'POST',
		url : '${pageContext.request.contextPath}/purchase/showOrderInfo.do',
		data : {
			method : 'showOrderInfo',
			startDate : $("#orderInfoStartDatePicker").val() ,
			endDate: $("#orderInfoEndDatePicker").val() 
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			var gridRowJson = dataSet.gridRowJson;

			$('#orderInfoGrid').jqGrid('clearGridData');		
			
			$('#orderInfoGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid'); 

		}					
	}); 
	
}

function showOrderDialog() {

	mrpGatheringNoList = []; // mrpNoList 초기화;
	
	$('#orderDialogGrid').jqGrid('clearGridData');
	
	var checkedMrpRowIdList = $('input[type=checkbox][name=mrpCheck]:checked');
	if(checkedMrpRowIdList.length == 0 ) {
		alertError("사용자 에러","발주할 물품을 선택하지 않았습니다"); 
		return;
		
	} 
	
	$(checkedMrpRowIdList).each( function() {  
		
		var rowId = $(this).val(); 
		var mrpGatheringNo = $('#orderListGrid').getRowData(rowId).mrpGatheringNo; 
		mrpGatheringNoList.push(mrpGatheringNo); 
		//소요량취합번호를 mrpGatheringNoList에 담음 
				}
	
	
			);
	

	
	$.ajax({
		
		type : 'POST',
		url : '${pageContext.request.contextPath}/purchase/showOrderDialog.do',
		data : {
			method : 'openOrderDialog', 
			mrpGatheringNoList : JSON.stringify(mrpGatheringNoList)
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			console.log(mrpGatheringNoList);
			
			if(dataSet.errorCode < 0){
	
				alertError('실패',dataSet.errorMsg);
				return;
				
			}
			
			gridRowJson = dataSet.gridRowJson;
			
			// 발주 시뮬레이션 Data 넣기
			$('#orderDialogGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid'); 
			
			// 발주 Dialog Open
			$("#order-dialog").dialog("open");

		}					
	}); // ajax 끝
	
}

function showOrderListGrid() {
	
	if($("#StartDatePicker").val() == "시작일"){
		
		alertError("사용자에러","날짜를 선택해주세요!");
		return;
		
	}
	
	if($("#EndDatePicker").val() == "종료일"){
		
		alertError("사용자에러","날짜를 선택해주세요!");
		return;
		
	}
	
	// ajax 시작
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/purchase/getOrderList.do' ,
		data : {
			method : 'getOrderList',
			startDate : $("#StartDatePicker").val() ,
			endDate : $("#EndDatePicker").val() 
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
			
			// Data 넣기
			$('#orderListGrid')
				.jqGrid('setGridParam',{ datatype : 'jsonstring', datastr : gridRowJson })
				.trigger('reloadGrid');
		
	}});  // ajax 끝
	
}

function showCodeDialogForInput(source, devisionCodeNo, detailCodeName){
	
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
            		divisionCode: "IT-MA"   
            	},
			colNames:[ '상세코드번호' , '상세코드이름' , '사용여부' ],
			colModel:[
				{ name : 'detailCode', width:100, align : "center",editable:false},
				{ name : 'detailCodeName', width:100, align : "center", editable:false},
				{ name : 'codeUseCheck', width:100, align : "center",editable:false},
			],
			width: 450,
			height: 300,
			caption: detailCodeName,
			align: "center",
			viewrecords:true,
			rownumbers: true,
			onSelectRow: function(id) {
				
				var detailCode=$("#codeGrid").getCell(id, 1);
				var detailName=$("#codeGrid").getCell(id, 2);
				var codeUseCheck=$("#codeGrid").getCell(id, 3);
				
				if(codeUseCheck != 'n' || codeUseCheck != 'N') {
					
					$(source).val(detailCode);
					
					$("#codeDialog").dialog("close");			

				} else {
					alertError("사용자 에러", "사용 가능한 코드가 아닙니다");
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

				if(codeUseCheck !='N' && codeUseCheck !='n' ) {
					
					if(iCol == 4 || iCol == 5) {  // 품목코드, 품목명 cell 클릭
						var ids = $(grid).getRowData();
						var errorStatus = false;
						
						$(ids).each(function(index, obj) {
							
							var itemCodeInList = obj.itemCode;
							
							if( detailCode == $('#parentItemCodeSearchBox').val() ) {
								alertError("사용자 에러","");
								errorStatus = true;
								return;

							} else if( detailCode == itemCodeInList ) {
								alertError("사용자 에러","");
								errorStatus = true;
								return;
								
							}
							
						});
						
						if(!errorStatus) {
							$(grid).setCell(rowid, "itemCode", detailCode);					
							$(grid).setCell(rowid, "itemName", detailName);	
						}
						
					} else if(iCol == 3) {  // 품목구분 cell 클릭
						$(grid).setCell(rowid, "itemClassificationName", detailName);
						$(grid).setCell(rowid, "itemClassification", detailCode);

					}
					

					$("#codeDialog").dialog("close");
					checkRowChanged(lastSelected_bomInfoGrid_RowValue, grid, rowid);					

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

</script>
</head>
<body>


<div id="tabs">
	<ul>
    	<li><a href="#tabs-1">발주</a></li>
    	<li><a href="#tabs-2">발주현황</a></li>
  </ul>
  
	<div id="tabs-1">
	<fieldset style="display: inline;">
            <legend>전체 선택</legend>
               <input type="button" value="전체 선택" id="checkAll" />
         </fieldset>
		<fieldset style="display: inline;">
		    <legend>취합발주 / 발주필요품목검색 (BY MRP_G)</legend>
				<input type="text" value="시작일" id="StartDatePicker" />
				<input type="text" value="종료일" id="EndDatePicker" />
				<input type="button" value="재고처리 / 발주필요 목록조회 " id="getOrderListButton" />
				<input type="button" value="모의재고처리 및 취합발주" id="OrderDialogButton" />
		</fieldset>
		
		<fieldset style="display: inline;">
         	<legend>임의발주 품목검색/수량 입력</legend>
            	<input type="text" value="품목코드 검색" id="itemCodeSearchBox" />
            	<input type="text" value="수량" id="itemAmountBox" />
            	<input type="button" value="임의 발주" id="OptionOrderButton" />
      	</fieldset> 
				
		
		<table id="orderListGrid" ></table>
		<div id="orderListPager"></div>

	</div>
	
	<div id="tabs-2">

	<fieldset style="display : inline;">
		<legend>발주현황조회</legend>
		<input type="text" value="시작일" id="orderInfoStartDatePicker" />
		<input type="text" value="종료일" id="orderInfoEndDatePicker" />
		<input type="button" value="발주현황조회" id="viewOrderInfoButton">
	</fieldset>
	
	<table id="orderInfoGrid" ></table>
	<div id="orderInfoPager"></div>
	</div>

</div>

<div id = "order-dialog">

	<fieldset style="display: inline;">
			<legend>발주 등록 일자</legend>
    			<label for="orderDateAsTodayRadio">현재일자</label>
    			<input type="radio" name="orderDateCondition" value="orderDateAsToday" 
    				id="orderDateAsTodayRadio">
				<input type="text" value="발주일자" id="orderDateInputPicker" />
	</fieldset>

	<fieldset style="display: inline;">
		<legend> 발주 </legend>
  			<input type="button" value="현재 전개된 결과 발주 및 재고처리" id="orderButton" />
  			
	</fieldset>
	
	<table id = "orderDialogGrid"></table>
	<div id = "orderPager"></div>

</div>

<div id="codeDialog">
	<table id="codeGrid"></table>
</div>

</body>
</html>