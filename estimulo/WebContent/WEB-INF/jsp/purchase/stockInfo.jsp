<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>재고 (STOCK)</title>
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

#itemCodeSearchBox, #parentItemCodeSearchBox {
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

var orderNoList;  // 입고할때 넣어줄 orderNoList 초기화
var gridRowJson;  // json 형태의 데이터 받아와서 사용할때

$(document).ready(function() {
	
	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	$("input[type=radio]").checkboxradio();   // jqueryUI Checkboxradio 위젯 적용

	$("#warehousingDialog").dialog({
		title : '입고',
		autoOpen : false, 
		width : 1000,
		height : 700,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"닫기" : function() {
				$("#warehousingDialog").dialog("close");
			}
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
	
	$( "#tabs" ).tabs({
		//event: "mouseover" ,
	    collapsible: true
	      
	});

	$('#warehousingDateInputPicker').hide();
	
	initGrid();
	initEvent();
	showStockGrid();
	
});

function initGrid() {

	// stockGrid 시작
	$('#stockGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "창고코드","품목코드","품목명","단위","안전재고량",
			"재고량","입고예정재고량","투입예정재고량","납품예정재고량"], 
		colModel : [
			{ name: "warehouseCode", width: "80", resizable: true, align: "center" } ,
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "100", resizable: true, align: "center" } ,  
			{ name: "unitOfStock", width: "80", resizable: true, align: "center" } ,  
			{ name: "safetyAllowanceAmount", width: "80", resizable: true, align: "center" } ,  
			{ name: "stockAmount", width: "80", resizable: true, align: "center" } ,  
			{ name: "orderAmount", width: "80", resizable: true, align: "center" } ,  
			{ name: "inputAmount", width: "80", resizable: true, align: "center" } ,
			{ name: "deliveryAmount", width: "80", resizable: true, align: "center" }
		], 
		caption : '재고현황', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 100,   
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
		cache : false
	});
	// stockGrid 끝	

	// stockLogGrid 시작
	$('#stockLogGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "로그날짜", "품목코드", "품목명", "수량", "사유", 
			"cause" , "effect" ], 
		colModel : [ 
			{ name: "logDate", width: "120", resizable: true, align: "center"} ,
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,
			{ name: "itemName", width: "100", resizable: true, align: "center"} ,
			{ name: "amount", width: "80", resizable: true, align: "center"} ,
			{ name: "reason", width: "100", resizable: true, align: "center"} ,
			{ name: "cause", width: "110", resizable: true, align: "center"} ,
			{ name: "effect", width: "150", resizable: true, align: "center"}
		], 
		caption : '재고기록', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 270, 
		width : 1400,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : false,
		rowNum : 10,   // -1 : 모든 로우 한번에 표시, 그런데 잘 안먹히는 경우 많음
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
		pager : "#stockLogPager",
		cache : false
	}); 
	// stockLogGrid 끝
		
	// orderInfoGrid 그리드의 페이저 생성
	$('#stockLogGrid').navGrid("#stockLogPager", {
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
		colNames : ["선택","발주번호","발주날짜","상태","발주구분","품목코드",
			"품목명","수량"], 
		colModel : [
			{ name: "warehousingChk", width: "35", resizable: true, align: "center" ,
				formatter : function (cellvalue, options, rowObj) {
				// 여기서 체크박스 만들고 value값에 rowId 집어넣음
				var warehousingCheck = "<input type='checkbox' name='warehousingCheck' value="+JSON.stringify(options.rowId)+"/>";     
				return warehousingCheck;			
				}
			},
			{ name: "orderNo", width: "100", resizable: true, align: "center" } ,
			{ name: "orderDate", width: "100", resizable: true, align: "center" } ,  
			{ name: "orderInfoStatus", width: "100", resizable: true, align: "center" } ,  
			{ name: "orderSort", width: "100", resizable: true, align: "center" } ,  
			{ name: "itemCode", width: "100", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "100", resizable: true, align: "center" } ,  
			{ name: "orderAmount", width: "100", resizable: true, align: "center" } 			
		], 
		caption : '운송중인 발주품목', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 414,
		width : 940,
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
		pager : "#orderInfoPager",
		cache : false
	});
	// orderInfoGrid 끝
	
	// orderInfoGrid 그리드의 페이저 생성
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
	

	$('#warehousingDateRadio').on("click", function() {
		
		// 오늘 일자 생성
		var now = new Date();
		var today = now.getFullYear() + "-" +('0' + (now.getMonth() +1 )).slice(-2) + "-" + now.getDate();
		
		$('#warehousingDateInputPicker').val(today);
	
	});
	
	$('#inquiryStockLogButton').on("click", function() {

		showStockLogGrid();
		
	});
	
	$('#warehousingButton').on("click", function() {

		warehousing();
		
	});
	$('#allButton').on("click", function() {

		 if($('input[type=checkbox][name="warehousingCheck"]:checked').length!=0)
	         $("input:checkbox[name='warehousingCheck']").prop("checked",false);
	      else
	         $("input:checkbox[name='warehousingCheck']").prop("checked",true);
		
	
	});
	
	$('#warehousingDialogButton').on("click", function() {
		
		if( $('#warehousingDateInputPicker').val() == '입고일자' ) {
			alertError("사용자 에러", "입고일자로 등록할 일자를 선택하세요");
			return;
		}
			
		showWarehousingDialog();		
			
	});
	
}

function warehousing() {
	
	orderNoList = []; // orderNoList 초기화
	
	var checkedOrderRowIdList = $('input[type=checkbox][name=warehousingCheck]:checked');
	
	if(checkedOrderRowIdList.length == 0 ) {
		
		alertError("사용자 에러","입고할 품목을 선택하지 않았습니다");
		return;
		
	}
	
	$(checkedOrderRowIdList).each( function() {  // 체크된애들 반복문 시작
		
		var rowId = $(this).val(); 
		// 체크한 행의 로우아이디
		var orderNo = $('#orderInfoGrid').getRowData(rowId).orderNo ;  
		// 체크한 행의 발주 번호를 orderNo에 저장
		orderNoList.push(orderNo);   //orderNoList에 담음
		
	});
	
	var confirmMsg = "이하 OrderNo들에 대해 입고처리합니다.\r\n" + 
	"일자 : " + $('#warehousingDateInputPicker').val()  + "\r\n" 
	+ orderNoList +
	"\r\n계속하시겠습니까?"

	var confirmStatus = confirm(confirmMsg);
	
	// 확인누르면 ajax 시작~
	if( confirmStatus == true ) {
	
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/purchase/warehousing.do',
		data : {
			method : 'warehousing', 
			orderNoList : JSON.stringify(orderNoList)
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);	
			
			if(dataSet.errorCode < 0){
				alertError('실패',dataSet.errorMsg);
				return;
			}
			
			// Dialog 닫기
			$("#warehousingDialog").dialog("close");
			
			alertError("성공", "입고 처리 완료");
			
			// StockGrid 리로드
			showStockGrid();

		}					
	}); // ajax 끝
	
	}
	
}

function showStockGrid() {
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/purchase/searchStockList.do' ,
		async :false,
		data : {
			method : 'searchStockList'
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {

			console.log(dataSet);

			gridRowJson = dataSet.gridRowJson;  // gridRowJson : 그리드에 넣을 json 형식의 data
			
			// Grid에 Data 넣기
			$('#stockGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid');
		}
		
	});
	
}

function showStockLogGrid() {
	
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
		url : '${pageContext.request.contextPath}/purchase/searchStockLogList.do' ,
		data : {
			method : 'searchStockLogList',
			startDate : $("#StartDatePicker").val() ,
			endDate : $("#EndDatePicker").val() 
			},
		dataType : 'json', 
		cache : false, 
		async : false,
		success : function(dataSet) { 
			
			console.log(dataSet);
			gridRowJson = dataSet.gridRowJson;  // gridRowJson : 그리드에 넣을 json 형식의 data
			
			// Grid에 Data넣기
			$('#stockLogGrid')
				.jqGrid('setGridParam',{ datatype : 'jsonstring', datastr : gridRowJson })
				.trigger('reloadGrid');
		
	}});  // ajax 끝
	
}

function showWarehousingDialog(){
	
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/purchase/searchOrderInfoListOnDelivery.do',
		data : {
			method : 'searchOrderInfoListOnDelivery'
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {
			
			console.log(dataSet);
			var gridRowJson = dataSet.gridRowJson;
			
			$('#orderInfoGrid').jqGrid("clearGridData");
			// Data 넣기
			$('#orderInfoGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid'); 
			
			// Dialog Open
			$("#warehousingDialog").dialog("open");

		}					
	}); // ajax 끝
	
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
            		divisionCode: devisionCodeNo   
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
								alertError("사용자 에러","BOM 구성에 자기 자신인 품목을 등록할 수 없습니다");
								errorStatus = true;
								return;

							} else if( detailCode == itemCodeInList ) {
								alertError("사용자 에러","BOM 구성에 이미 있는 품목입니다");
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
    	<li><a href="#tabs-1">재고</a></li>
    	<li><a href="#tabs-2">재고로그</a></li>
   </ul>

	<div id="tabs-1">
		
		<fieldset style="display: inline;">
		    <legend>입고일자 선택 / 입고</legend>
			<label for="warehousingDateRadio">현재일자</label>
    		<input type="radio" name="warehousingDateCondition" 
    		value="warehousingDateAsToday" id="warehousingDateRadio">
			<input type="text" value="입고일자" id="warehousingDateInputPicker" />
		    <input type="button" value="입고" id="warehousingDialogButton" />
		
		</fieldset>
			
		<table id="stockGrid" ></table>

	</div>
	
	<div id="tabs-2">	
		
		<fieldset style="display: inline;">
		    <legend>재고로그 검색조건</legend>
		    	<input type="text" value="시작일" id="StartDatePicker" />
				<input type="text" value="종료일" id="EndDatePicker" />
		</fieldset>
		
		<fieldset style="display: inline;">
		    <legend>조회</legend>
		    	<input type="button" value="조회" id="inquiryStockLogButton"/>
		</fieldset>
		
		<table id="stockLogGrid" ></table>	
		<div id="stockLogPager"></div>
	</div>

</div>

<div id = "warehousingDialog">

	<fieldset style="display: inline;">
			<legend>체크리스트 입고</legend>
			<input type="button" value="전체선택" id="allButton">
			<input type="button" value="현재 체크된 발주품목 입고" id="warehousingButton">
	</fieldset>

	<table id = "orderInfoGrid"></table>
	<div id="orderInfoPager"></div>

</div>

<div id="codeDialog">
	<table id="codeGrid"></table>
</div>

</body>
</html>