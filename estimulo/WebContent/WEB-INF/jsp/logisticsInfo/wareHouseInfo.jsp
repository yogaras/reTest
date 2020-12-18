<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>창고 관리</title>

<style>
#tabs table {
	font-size: 11px;
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
	
	$(document).ready(function() {

		$("input[type=button], input[type=submit]").button(); // jqueryUI Button 위젯 적용

		$("input[type=radio]").checkboxradio(); // jqueryUI Checkboxradio 위젯 적용

		$("#tabs").tabs({
			//event: "mouseover" ,
			collapsible : false
		
		});
		initGrid();
		initevent();	
		$('#codeDialog').hide();

		
	
	});

	function initGrid() {
		$('#warehouseListGrid').jqGrid({
			   url:"local",
			   datatype : 'json',
			   datastr : gridRowJson,
			   colNames:["창고코드","창고이름","창고사용여부","설명서"],
			   colModel:[
			    {name:"warehouseCode",width:50, editable:false},
			    {name:"warehouseName",width:50, editable:true},
			    {name:"warehouseUseOrNot",width:50, editable:true},
			    {name:"description",width:50, editable:true}  
			   ], 
			   width:700, 
			   caption:"창고조회",
			   editurl : 'clientArray', 
			   cellsubmit : 'clientArray',
			   onSelectRow: function(id){
				   
					var codeUseCheck=$("#warehouseListGrid").getCell(id,2);

					if(codeUseCheck != 'N' && codeUseCheck != 'n') {
						
						showWarehouseGrid();

					} else {
						
						alertError("사용자 에러", "사용 가능한 창고가 아닙니다!");	
						
					}
				   
			   }
  
	});

	
	}

	

	function showWarehouseGrid() {
		
		$("#codeDialog").dialog({
			title : '창고세부정보',
			width : 500,
			height : 500,
			modal : true   // 폼 외부 클릭 못하게
		});
		
		$.jgrid.gridUnload("#codeGrid");

		$("#codeGrid").jqGrid({
	        url : "${pageContext.request.contextPath}/base/codeList.do",
	        datatype : "json",
	        jsonReader : { root: "detailCodeList" },
	        postData : { 
	    		method : "findDetailCodeList" ,
	    		divisionCode : "CL-01" 
	    	},
			colNames : [ '품목코드' , '품목명' , '재고량' ],
			colModel : [
				{ name : 'detailCode', width:100, align : "center",editable:false},
				{ name : 'detailCodeName', width:100, align : "center", editable:false},
				{ name : 'codeUseCheck', width:100, align : "center",editable:false},
			],
			width : 450,
			height : 300,
			caption : "창고세부정보",
			align : "center",
			viewrecords : true,
			rownumbers : true,
			onSelectRow : function(id) {

				var detailCode=$("#codeGrid").getCell(id, 1);
				var detailName=$("#codeGrid").getCell(id, 2);
				var codeUseCheck=$("#codeGrid").getCell(id, 3);
				console.log(detailCode)
				console.log(detailName)
				console.log(codeUseCheck)
				}
			});


	}

	function initevent() {

	$('#showWarehouseListButton').on("click",function() {
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
		
		
	});
}
</script>
</head>

<body>
	<div id="tabs">
			<fieldset style="display: inline;">
				<legend>조회</legend>
				<input type="button" value="창고목록조회" id="showWarehouseListButton">
			</fieldset>

			<fieldset style="display: inline;">
				<legend>등록</legend>
				<input type="button" value="창고등록하기" id="WarehouseRegistration">
			</fieldset>
			<table id="warehouseListGrid"></table>
			<table id="WarehouseGird"></table>
		</div>
		<div id="codeDialog">
			<table id="codeGrid"></table>
		</div>

</body>

</html>