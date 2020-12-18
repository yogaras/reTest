<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>창고 관리</title>

<style>

#tabs table{
	font-size:11px;
}

#tabs .ui-jqgrid .ui-widget-header {
		height: 30px;
		font-size: 1em;
}

.small_Btn {
 width: auto;
 height: auto;
 font-size: 15px;
}
.ui-jqgrid-view {
 font-size: 0.8em;
}

</style>

<script type="text/javascript">

var gridRowJson;

$(document).ready(function(){

	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	
	$("input[type=radio]").checkboxradio();   // jqueryUI Checkboxradio 위젯 적용
	
	$( "#tabs" ).tabs({
		//event: "mouseover" ,
	    collapsible: true
	      
	});
	
  	initGrid();
  	initEvent();
  
}); 

function initGrid(){
	
	// orderListGrid 그리드 시작
	$('#warehouseListGrid').jqGrid({ 
		datatype : 'json',
		datastr : gridRowJson,
		colNames : [ "창고코드","창고이름","사용여부","비고" ], 
		colModel : [
			{ name: "warehouseCode", width: "120", resizable: true, align: "center" } ,
			{ name: "warehouseName", width: "120", resizable: true, align: "center" } ,  
			{ name: "warehouseUseOrNot", width: "80", resizable: true, align: "center" } , 
			{ name: "description", width: "120", resizable: true, align: "center" } 			
		], 
		caption : ' 창고 리스트  ', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 250, 
		width : 550,
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
		cache : false
	});

}

function initEvent(){
	
	$('#showWarehouseListButton').on("click", function() {
			
		showWarehouseGrid();
			
	});

}

function showWarehouseGrid(){

	$.ajax({
	   type : 'POST',
	   url : '${pageContext.request.contextPath}/logisticsInfo/warehouseInfo.do',
	   data : {
	    method : "getWarehouseList",
	   },
	   async : false,
	   dataType : 'json',
	   cache : false,
	   success : function(dataSet) {
		   
	    console.log(dataSet);
	    
	    var gridRowJson = dataSet.gridRowJson;

		// 그리드 초기화
		$('#warehouseListGrid').jqGrid('clearGridData');		
	
		// 작업지시 시뮬레이션 Data 넣기
		$('#warehouseListGrid')
			.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
			.trigger('reloadGrid'); 
	    
	   	}
	  
	  });// ajax 끝
 
}	  
	  
</script>
</head> 

<body>
 <div id="tabs">
 
 	<ul>
    	<li><a href="#tabs-1">창고조회</a></li>
    </ul>
 	
 	<div id="tabs-1">
  		<fieldset style="display: inline;">
  		
  		<input type="button" value="창고목록조회" id="showWarehouseListButton">
  		
  		</fieldset>
  		<table id="warehouseListGrid"></table>
	</div>
 </div>
  
 <div id="codeDialog">
  <table id="codeGrid"></table>
 </div>

</body>

</html>