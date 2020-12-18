<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>작업지시 (WorkOrder)</title>
<style>

#workSiteLogSearch{
	display: inline;
	width: 115px;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
	text-align: center;
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

var mrpNo; // 모의 작업 지시 하기위해 사용하는 mrpNo

$(document).ready(function() {
	
	
	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	$("input[type=radio]").checkboxradio();   // jqueryUI Checkboxradio 위젯 적용
	
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
	
	$("#codeDialog").hide();
	
	$("#workSiteDialog").hide();
	
	$( "#tabs" ).tabs({
		//event: "mouseover" ,
	    collapsible: true
	      
	});
	
	$("#workSiteLogSearch").datepicker({
		changeMonth : true,//월 바꾸기 가능
		numberOfMonths : 1,//한번에 보여지는 개월 수
		maxDate : new Date() //최대날짜는 오늘까지만
	});	
	
	initGrid();
	initEvent();
	
});

function initGrid() {
	
	$('#workSiteGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "선택","작업지시일련번호","소요량전개번호","주생산계획번호","소요량취합번호",
			"품목분류","품목코드","품목명","단위","생산공정코드","생산공정명","작업장코드","작업장명","원재료검사","제품제작","제품검사"], 
		colModel : [
			{ name: "orderNoCheck", width: "35", resizable: true, align: "center" ,
				
				formatter : function (cellvalue, options, rowObj) {	
				var orderNoCheck = "<input type='radio' name='workSiteCheck' value="+JSON.stringify(options.rowId)+"/>";     
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
			{ name: "productionProcessCode", width: "80", resizable: true, align: "center" , hidden: true} ,
			{ name: "productionProcessName", width: "80", resizable: true, align: "center" } ,
			{ name: "workSiteCode", width: "80", resizable: true, align: "center" , hidden: true} ,
			{ name: "workStieName", width: "80", resizable: true, align: "center" } ,
			{ name: "inspectionStatus", width: "80", resizable: true, align: "center" } ,
			{ name: "productionStatus", width: "80", resizable: true, align: "center" } ,
			{ name: "completionStatus", width: "80", resizable: true, align: "center" } 
		], 
		caption : ' 작업장 현황 ', 
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
	
	$('#workSiteLog').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "작업지시번호","품목코드","품목명","생산공정코드","생산공정명","상황","작업장명","날짜" ], 
		colModel : [
			{ name: "workOrderNo", width: "100", resizable: true, align: "center" } ,
			{ name: "itemCode", width: "80", resizable: true, align: "center" } ,  
			{ name: "itemName", width: "110", resizable: true, align: "center" } ,  
			{ name: "productionProcessCode", width: "90", resizable: true, align: "center" } ,  
			{ name: "productionProcessName", width: "110", resizable: true, align: "center" } ,  
			{ name: "reaeson", width: "230", resizable: true, align: "center" } ,  
			{ name: "workSiteName", width: "80", resizable: true, align: "center" } ,
			{ name: "workDate", width: "120", resizable: true, align: "center" } 
		], 
		caption : ' 작업장 로그 ', 
		multiselect : false,
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 1400,
		autowidth : false, 
		shrinkToFit : false, 
		cellEdit : false,
		rowNum : 30,   
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
		pager : "#workSiteLogPager",
		cache : false
	});
	
	$('#workSiteLog').navGrid("#workSiteLogPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
}

function initEvent() {
	
	$("#workSiteList").on("click", function() { 
		showWorkOrderInfoListGrid("#workSiteGrid");
	});
	
	$("#workSiteRawMaterials").on("click", function() { 
		workSiteAction( this,$(this).val() ); //버튼주소, 버튼  값(이름) : 원재료검사,제품제작,판매제품검사
	});
	
	$("#workSiteProduction").on("click", function() { 
		workSiteAction( this,$(this).val() );
	});
	
	$("#workSiteExamine").on("click", function() { 
		workSiteAction( this,$(this).val() );
	});
	
	
	$("#completed").on("click", function() { 
		workCompleted();
	});
	
	$("#workSitelogList").on("click", function() { 
		workSitelogList();
	});

	
	function showWorkOrderInfoListGrid(gridName){ //작업장조회 , 원재료검사;제품제작;판매제품검사 완료 후 바로 호출됨
		
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

	
	function workSiteAction(grid,workSiteCourse) {		
		alert(workSiteCourse); 
		var wsc = workSiteCourse // 선택한 작업장 이름이 넘어옴 
		var query = null;
		var wscSize = wsc.length;
		//wscSize : 원재료검사6, 제품제작5, 판매제품검사7
		var rowId=$('input[type=radio][name=workSiteCheck]:checked').val();		
		var status=$("#workSiteGrid").getRowData(rowId)
		
		//inspectionStatus = 원재료검사상태 , productionStatus = 제품제작상태, completionStatus= 완료상태
		if(status.inspectionStatus == "Y" && status.productionStatus == "Y" && status.completionStatus == "Y"){
			alertError("에러","모든 작업이 끝났습니다. \n 작업완료 등록을 해주세요");
			return;
		}else if(status.inspectionStatus == "Y" && status.productionStatus == "Y" && wscSize == 5){
			alertError("에러","제품 제작은 끝났습니다 \n 판매제품 검사로 넘어가세요");
			return;
		}else if(status.inspectionStatus == "Y" && wscSize == 6){ 
			alertError("에러","원재료 검사는 끝났습니다 \n 제품제작으로 넘어가세요");
			return;
		}
		
		if(status.productionStatus != "Y" && wscSize == 7){
			alertError("에러","제품이 제작되지 않았습니다. \r 제품제작 을 해주세요.");
			return;
		}else if(status.inspectionStatus != "Y" && wscSize == 5){ 
			alertError("에러","원재료 검사가 시작되거나 끝나지 않았습니다.");
			return;
		}

		if(wscSize == 5){ // 제품제작일 경우
			query = "Production";
		} else if(wscSize == 6){ //원재료 검사일 경우
			query = "RawMaterials";
		} else if(wscSize == 7){ //판매제품 검사일경우
			query = "SiteExamine";
		}
		
		var rowId = $('input[type=radio][name=workSiteCheck]:checked').val();
		
		var workOrderNo = $('#workSiteGrid').getRowData(rowId).workOrderNo ; //작업지시일련번호
		var itemClassIfication = $('#workSiteGrid').getRowData(rowId).itemClassification ; //품목분류
	    if(workOrderNo == undefined){
	    	alertError('실패','작업할 제품을 눌러주세요');
			return;
	    }
		console.log(itemClassIfication)
	    $("#workSiteDialog").dialog({
			title : "작업장 현황",
			width : 1500,
			height : 500,
			modal : true   // 폼 외부 클릭 못하게
		});

	    $.jgrid.gridUnload("#workSiteSituationGrid");//다이얼로그 내부 작업현황 그리드 초기화
	    
		$("#workSiteSituationGrid").jqGrid({
	        url : "${pageContext.request.contextPath}/production/showWorkSiteSituation.do",
	        datatype : "json",
	        jsonReader : { root: "gridRowJson" },
	        postData : { 
	        	method : 'showWorkSiteSituation' ,  
				workSiteCourse : query , //원재료검사:RawMaterials,제품제작:Production,판매제품검사:SiteExamine
				workOrderNo : workOrderNo, //작업지시일련번호		
				itemClassIfication : itemClassIfication //품목분류:완제품,반제품,재공품		
	    	},
			colNames : [ '작업지시번호' , '소요량전개번호' , '주생산계획번호' , '작업장명' , '제작품목분류' , '제작품목코드' , '제작품목명' , '작업품목분류' , '작업품목코드' , '작업품목명' , '작업량' ],
			colModel : [
				{ name : 'workOrderNo', width:100, align : "center",editable:false},
				{ name : 'mrpNo', width:100, align : "center", editable:false},
				{ name : 'mpsNo', width:100, align : "center",editable:false},
				{ name : 'workSieteName', width:70, align : "center",editable:false},
				{ name : 'wdItem', width:80, align : "center",editable:false},
				{ name : 'parentItemCode', width:80, align : "center",editable:false},
				{ name : 'parentItemName', width:80, align : "center",editable:false},
				{ name : 'itemClassIfication', width:80, align : "center",editable:false},
				{ name : 'itemCode', width:80, align : "center",editable:false},
				{ name : 'itemName', width:80, align : "center",editable:false},
				{ name : 'requiredAmount', width:100, align : "center",editable:false},
			],
			width : 1500,
			height : 300,
			caption : '작업현황',
			align : "center",
			viewrecords : true,
			rownumbers : true,
			});

	}
	
	function workCompleted(){
		
		var itemCodeList = [];
		
		var now = new Date();
		var today = now.getFullYear() + "-" +('0' + (now.getMonth() +1 )).slice(-2) + "-" + ('0' + (now.getDate())).slice(-2);
		    
		var workSiteSituationList = $('#workSiteSituationGrid').getRowData(); //작업현황 그리드 데이터
		
		var workProduct = workSiteSituationList[0];
		
		$(workSiteSituationList).each(function(index,obj){
			itemCodeList.push(obj.itemCode)
		})
		alert(itemCodeList)//작업품목코드
		var confirmMsg = "완료날짜 : "+today+"\n"+
		"제품 "+(workProduct.itemClassIfication == "원재료" ? "검사" : "제작") +" 할 갯수"+ workSiteSituationList.length +"\n"
		+workProduct.wdItem+" : "+workProduct.parentItemName+"을 제작하기 위해 \n 작업을 하시겠습니까?"
		//workProduct.wdItem = 제작품목분류; 완제품,반제품,재공품
		if ( confirm(confirmMsg) ){
			  
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/production/workCompletion.do' ,
				data : {
					method : 'workCompletion',
					workOrderNo : workProduct.workOrderNo, //작업지시번호
					itemCode : workProduct.parentItemCode, //제작품목코드 DK-01 , DK-AP01
					itemCodeList : JSON.stringify(itemCodeList) //작업품목코드 
					},
				dataType : 'json', 
				cache : false, 
				async : false,
				success : function(dataSet) { 
					alert(JSON.stringify(dataSet));
					if(dataSet.errorCode<0) {
						alertError('실패',dataSet.errorMsg);
						return;
					} 
					alertError('성공',"완료");
					$("#workSiteDialog").dialog("close");
					showWorkOrderInfoListGrid("#workSiteGrid"); //workSiteGrid : 작업장현황
				
				}
			}); 
			
		}else{
			
			alertError('취소','취소');
			
		}
		
	}
	
}

function workSitelogList(){
	
	var workSiteLogDate=$("#workSiteLogSearch").val();
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/production/workSiteLog.do' ,
		data : {
			method : 'workSiteLogList',
			workSiteLogDate : workSiteLogDate
			},
		dataType : 'json', 
		cache : false, 
		async : false,
		success : function(dataSet) { 
			
			console.log(dataSet)
			
			if(dataSet.errorCode<0) {
				alertError('실패',dataSet.errorMsg);
				return;
			} 
			
			$("#workSiteLog")
			.jqGrid('setGridParam',{ datatype : 'local', data : dataSet.gridRowJson })
			.trigger('reloadGrid'); 
		
		} 
	}); 
	
}
</script>
</head>
<body>
<div id="tabs">
	<ul>
    	<li><a href="#tabs-1">작업장</a></li>
    	<li><a href="#tabs-2">작업장 로그</a></li>
  </ul>

	
	<div id="tabs-1">
	
		<fieldset style="display: inline;">
		    <legend> 작업장 조회 </legend>
				<input type="button" value="작업장 조회" id="workSiteList" />
		</fieldset>
	
		<fieldset style="display: inline;">
		    <legend> 제품 작업장 </legend>
				<input type="button" value="원재료 검사" id="workSiteRawMaterials" />
				<input type="button" value="제품 제작" id="workSiteProduction" />
				<input type="button" value="판매제품 검사" id="workSiteExamine" />
		</fieldset>
		
		<table id="workSiteGrid"></table>
		
	<div id="workSiteDialog">
		<fieldset style="display: inline;" >
		    <legend> 완료버튼 </legend>
			<input type="button" value="검사 및 제작 완료" id="completed" /> 
		</fieldset> 
		<table id="workSiteSituationGrid"></table>
	</div>
	   
	
	</div>
	
	<div id="tabs-2">
	     <fieldset style="display: inline;">
		    <legend> 작업장 로그 </legend>
		    	<input type="text" id="workSiteLogSearch" style="display: inline-block;"/>  
				<input type="button" value="작업장 로그 조회" id="workSitelogList" />
		</fieldset>
		
		<table id="workSiteLog"></table> 
		<div id="workSiteLogPager"></div>
	</div>
</div>
</body>
</html>



