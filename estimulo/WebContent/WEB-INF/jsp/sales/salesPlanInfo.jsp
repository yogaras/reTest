<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>판매 계획</title>
<style>

#startDatePicker, #endDatePicker, 
#salesAmountBox, #unitPriceOfSalesBox, #sumPriceOfSalesDiv {
	display: inline;
	width: 105px;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 18px;
	text-align : center;
	
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

var lastSelected_salesPlanGrid_Id;   // 가장 나중에 선택한 판매계획 grid 의 행 id 
var lastSelected_salesPlanGrid_RowValue;   // 가장 나중에 선택한 판매계획 grid 의 행 값 

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
	
	$("#codeDialog").attr("style", "display:none");

	$("#InputDialog").attr("style", "display:none");
	
	initGrid();
	initEvent();
	
});

function initGrid() {

	// 판매계획 그리드 시작
	$('#salesPlanGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "삭제", "판매계획일련번호", "품목코드", "품목명", "단위", "판매계획일",  
			"계획수량", "계획단가", "합계액", "판매마감일", "MPS 적용여부", "비고", 
			"status", "beforeStatus" ] , 
		colModel : [
			{ name: "check", width: "50", resizable: true, align: "center" ,
				formatter : function (cellvalue, options, rowObj) {
					
					if(rowObj.mpsApplyStatus == 'y' || rowObj.mpsApplyStatus == 'Y' ) {
						
						return '';
						
					} else {
						
						var chk = "<input type='checkbox' name='chk' value=" + JSON.stringify(options.rowId) + " />";						
						 
						return chk;	
						
					}		
				}
			},
			{ name: "salesPlanNo", width: "110", resizable: true, align: "center"} ,
			{ name: "itemCode", width: "70", resizable: true, align: "center" } ,
			{ name: "itemName", width: "150", resizable: true, align: "center"} ,
			{ name: "unitOfSales", width: "40", resizable: true, align: "center"} ,
			{ name: "salesPlanDate", width: "70", resizable: true, align: "center", editable: true,
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
									$(this).editCell(lastSelected_salesPlanGrid_Id,"salesPlanDate",false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			
			
			{ name: "salesAmount", width: "70", resizable: true, align: "center",
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "unitPriceOfSales", width: "80", resizable: true, align: "center", 
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "sumPriceOfSales", width: "80", resizable: true, align: "center", 
			        formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } , 	
	        { name: "dueDateOfSales", width: "70", resizable: true, align: "center", editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) { 
							$(element).datepicker({ 
								minDate : $(this).getCell( lastSelected_salesPlanGrid_Id , "salesPlanDate" ) ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$('#salesPlanGrid').editCell(lastSelected_salesPlanGrid_Id, 10, false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
	        { name: "mpsApplyStatus", width: "100", resizable: true, align: "center" } ,
			{ name: "description", width: "100", resizable: true, align: "center", editable: true } ,
			{ name: "status", width: "80", resizable: true, align: "center" } ,
			{ name: "beforeStatus", width: "10", resizable: true, align: "center" , hidden: true} 

		], 
		caption : '판매계획', 
		sortname : 'salesPlanNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 200, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : true,
		rowNum : 50,  
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
		
		beforeEditCell(rowid, cellname, value, iRow, iCol){

        	if(value == null || value == "" ) {
        		previousCellValue = null;
        	} else {
        		previousCellValue = value;
        	}
		},
		
		afterEditCell : function(rowid, cellname, value, iRow, iCol){
			
			// cellEdit : true 일 때 마우스가 Input 에서 focus 를 벗어 났을 때 Cell Save가 일어나게 함
		    $("#"+rowid+"_"+cellname).blur(function(){
		        $(this).jqGrid("saveCell",iRow,iCol);
		    });
		    
		},
		
		afterSaveCell(rowid, cellname, value, iRow, iCol){

        	var status = $(this).getCell(rowid,"status");
        	var mpsApplyStatus = $(this).getCell(rowid,"mpsApplyStatus");

        	if(status == 'DELETE') {
        	
        		alertError("사용자 에러", "삭제 예정인 행이었습니다 ^^ </br> 원래 값으로 돌릴께요");
    			$(this).setCell(rowid,cellname, previousCellValue);	

        	} else if(mpsApplyStatus == 'Y') {  

        		alertError("사용자 에러", "MPS 가 이미 적용되어 수정할 수 없습니다 ^^ </br> 원래 값으로 돌릴께요");
    			$(this).setCell(rowid,cellname, previousCellValue);	

        		
			} else if(status == 'NORMAL') {
        		
        		if( previousCellValue != value ) {
    				$(this).setCell(rowid,"status", "UPDATE");
        		}
        		
        	}
		},
		
		beforeSelectRow : function( rowid, event ) {
			
        	var beforeStatus = $(this).getCell(rowid,"beforeStatus");
        	var currentStatus = $(this).getCell(rowid,"status");
        	
        	if($(event.target).is(":checkbox")) {
            	
                if($(event.target).is(":checked")) {
                    	
        			$(this).setCell(rowid,"status", "DELETE");
        			$(this).setCell(rowid,"beforeStatus", currentStatus);

                } else {
        			$(this).setCell(rowid,"status", beforeStatus);
                                
                }
                
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
		
			var status = $(this).getCell(rowid,"status");
			var mpsApplyStatus = lastSelected_salesPlanGrid_RowValue.mpsApplyStatus; 


			if(mpsApplyStatus == 'Y'){
				
				$(this).jqGrid('setGridParam', { cellEdit : false })
					.trigger('reloadGrid');
				
			} else {

				$(this).jqGrid('setGridParam', { cellEdit : true})
					.trigger('reloadGrid');
				
			}
			

			if( status == "NORMAL" && mpsApplyStatus != 'Y' ){

				if(iCol == 3 || iCol == 4) {  // 품목코드, 품목명 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "IT-_I","완제품 및 반제품 검색")
                
                } else if (iCol == 5) {  // 단위 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "UT","단위 검색");

                } else if( iCol == 7 || iCol == 8 || iCol == 9 ) {  

                	showInputDialog(this ,rowid);
                	
                } 

            } else if ( ( status == "INSERT" || status == "UPDATE" )  ) {

                if(iCol == 3) {  // 품목코드 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "IT-_I","완제품 및 반제품 검색");
                		
                } else if (iCol == 5) {  // 단위 cell 클릭
                	
                	showCodeDialog(this ,rowid , iCol , "UT","단위 검색");

                } else if( iCol == 7 || iCol == 8 || iCol == 9 ) {  

                	showInputDialog(this ,rowid);
                	
                }
                
            } 
		}
	}); // 판매계획 그리드 끝
}

function initEvent() {
	
	$('#salesPlanSearchButton').on("click",function() {

		showSalesPlanGrid();
		
	});
	
	$('#salesPlanInsertButton').on("click",function() {

		var newRowNum = $('#salesPlanGrid').jqGrid('getDataIDs').length+1;  // 새로운 행 넘버
		
		$('#salesPlanGrid').addRowData(
				newRowNum, 
				{ "salesPlanNo":"저장시 지정됨", "status":"INSERT" , "deleteStatus":"LOCAL 삭제"} );

	});

	
	$('#salesPlanDeleteButton').on("click",function() {
		
		var rowIdList =  $('#salesPlanGrid').jqGrid('getDataIDs');  // 판매계획 그리드의 전체 행 ID 배열
		
		var deleteCount = 0;

		$(rowIdList).each( function(index, rowId) {   // 전체 행에 대해 반복문 시작

			var rowObject = $('#salesPlanGrid').getRowData(rowId); // 한 행의 row 값 정보 객체
			
			var status = rowObject.status;
			
			if(status == 'DELETE' && rowObject.salesPlanNo != '저장시 지정됨' ) {
				
				resultList.push(rowObject);
				deleteCount++;
				
			} else if(status == 'DELETE' && rowObject.salesPlanNo == '저장시 지정됨' ) {
				
				$('#salesPlanGrid').delRowData(rowId);
			
			}
			
		});
			
		
		if( resultList.length != 0 ) {
	
			var confirmMsg = deleteCount + "개의 판매계획을 삭제합니다. 계속하시겠습니까?";
			
			var confirmStatus = confirm(confirmMsg);
			
			if( confirmStatus == true ) {
				
				$.ajax({ 
					type : 'POST',
					url : '${pageContext.request.contextPath}/sales/batchSalesPlanListProcess.do' ,
					data : {
						method : 'batchListProcess', 
						batchList : JSON.stringify(resultList),
					},
					dataType : 'json', 
					cache : false, 
					success : function(dataSet) {
						
						console.log(dataSet);

						var resultMsg = "판매계획 </br>" + dataSet.result.DELETE + "</br>총 " + dataSet.result.DELETE.length + " 개가 삭제되었습니다";
						alertError("성공", resultMsg);
						
					}
				});				
			} else {
				
				alertError("^^", "취소되었습니다");	
				
			}
						
		}

		resultList = [];   // 초기화
		showSalesPlanGrid();

	});

	$('#batchSaveButton').on("click",function() {
		
		var rowIdList =  $('#salesPlanGrid').jqGrid('getDataIDs'); 
		
		var insertCount = 0;
		var updateCount = 0;
		var deleteCount = 0;

		var errorMsg = "< 제외 목록 > \r";  
		
		$(rowIdList).each( function(index, rowId) {   // 전체 행에 대해 반복문 시작
			
			var rowObject = $('#salesPlanGrid').getRowData(rowId); // 행의 row 값 정보 객체
			var status = rowObject.status;
			
			if(status == 'INSERT' ) {
				
				if( rowObject.itemCode == "" ) {
					errorMsg += ( rowId + "행 : 품목코드 미입력 \r" );
				} else if(rowObject.salesPlanDate == "" ) {
					errorMsg += ( rowId + "행 : 판매계획일 미입력 \r" );
				} else if(rowObject.unitOfSales == "" ) {
					errorMsg += ( rowId + "행 : 단위 미입력 \r" );
				} else if(rowObject.dueDateOfSales == ""){
					errorMsg += ( rowId + "행 : 납기일 미입력 \r" );
				} else if(rowObject.estimateAmount == "0" || rowObject.unitPriceOfEstimate == "0") {
					errorMsg += ( rowId + "행 : 계획수량/계획단가 미입력 \r" );
				} else {
					resultList.push(rowObject);	
					insertCount++;
				}

			} else if (status == 'UPDATE') {
				
				resultList.push(rowObject);
				updateCount++;
				
			} else if (status == 'DELETE') {
				
				if(rowObject.salesPlanNo != '저장시 지정됨' ) {
					resultList.push(rowObject);
					deleteCount++;
				} else {
					$('#salesPlanGrid').delRowData(rowId);

				}
				
			}
		});
		
		//alert(JSON.stringify(resultList));
		
		if(resultList.length != 0) {
			
			var confirmMsg = 
				( ( errorMsg == "< 제외 목록 > \r" ) ? "" : errorMsg + "\r" ) + 
				"< 가능한 작업 목록 > \r" +
				( ( insertCount != 0 ) ? insertCount + "개의 판매 계획 추가 \n" : "" ) +
				( ( updateCount != 0 ) ? updateCount + "개의 판매 계획 수정 \n" : "" ) +
				( ( deleteCount != 0 ) ? deleteCount + "개의 판매 계획 삭제 \n" : ""  ) +
				"\r위와 같이 작업합니다. 계속하시겠습니까?"
			
			var confirmStatus = confirm(confirmMsg);

			if( confirmStatus == true ) {
				$.ajax({ 
					type : 'POST',
					url : '${pageContext.request.contextPath}/sales/batchSalesPlanListProcess.do' ,
					async :false,
					data : {
						method : 'batchListProcess', 
						batchList : JSON.stringify(resultList),
					},
					dataType : 'json', 
					cache : false, 
					success : function(dataSet) {

						console.log(dataSet);
						var resultMsg = 
							"< 판매계획 작업 내역 >   <br/><br/>"
							+ "추가 : "
							+ ( ( dataSet.result.INSERT.length != 0 ) ? dataSet.result.INSERT : "없음" ) + "</br></br>"
							+ "수정 : " 
							+ ( ( dataSet.result.UPDATE.length != 0 ) ? dataSet.result.UPDATE : "없음" ) + "</br></br>"
							+ "삭제 : " 
							+ ( ( dataSet.result.DELETE.length != 0 ) ? dataSet.result.DELETE : "없음" ) + "</br></br>"
							+ "위와 같이 작업이 처리되었습니다";
							
						alertError("성공", resultMsg);
					}
				});  // ajax 끝
			} else {
				
				alertError("^^", "취소되었습니다");	

			}
			
		} else if(resultList.length == 0) {
			
			alertError("^^", "추가/수정/삭제할 판매 계획이 없습니다");
		}

		resultList = [];   // 초기화
		
		showSalesPlanGrid();
		
	});
	

	$('#salesAmountBox, #unitPriceOfSalesBox').on("keyup", function() {
		var sum = $('#salesAmountBox').val() * $('#unitPriceOfSalesBox').val();
		
		$('#sumPriceOfSalesDiv').text(sum);
	});
	
}


function showSalesPlanGrid() {
	
	$('#salesPlanGrid').jqGrid('clearGridData');
	
	// ajax 시작
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/sales/searchSalesPlan.do' ,
		data : {
			method : 'searchSalesPlanInfo', 
			startDate : $("#startDatePicker").val() ,
			endDate : $("#endDatePicker").val() ,
			dateSearchCondition : $(':input:radio[name=searchDateCondition]:checked').val()
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) { 
			console.log(dataSet);
			var gridRowJson = dataSet.gridRowJson;  // gridRowJson : 그리드에 넣을 json 형식의 data
			
			
			if( gridRowJson.length != 0 ) {
				
				// 판매계획 Data 넣기
				$('#salesPlanGrid')
					.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
					.trigger('reloadGrid');
				
			} else {
				alertError('ㅜㅜ','조회된 데이터가 없습니다');
				
			}
			
			
	}});  // ajax 끝
	
}


function showInputDialog(grid ,rowid) {
	
	var salesAmount = $(grid).getCell(rowid, "salesAmount");
	var unitPriceOfSales = $(grid).getCell(rowid, "unitPriceOfSales");
	var sumPriceOfSales = $(grid).getCell(rowid, "sumPriceOfSales");
	
	$('#salesAmountBox').val(salesAmount);
	
	$('#unitPriceOfSalesBox').val(unitPriceOfSales);

	$('#sumPriceOfSalesDiv').text(sumPriceOfSales);

	$("#InputDialog").dialog({
		title : '계획수량 / 계획단가 입력',
		autoOpen : true,  
		width : 250,
		height : 250,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {
				
				$(grid).setCell(rowid, "salesAmount", $('#salesAmountBox').val() );
				$(grid).setCell(rowid, "unitPriceOfSales", $('#unitPriceOfSalesBox').val() );
				$(grid).setCell(rowid, "sumPriceOfSales", $('#sumPriceOfSalesDiv').text() );
				
				$("#InputDialog").dialog("close");
				checkRowChanged(lastSelected_salesPlanGrid_RowValue, grid, rowid);					

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
					
					if(iCol == 3) {	
						$(grid).setCell(rowid, iCol, detailCode);					
						$(grid).setCell(rowid, iCol+1, detailName);	
					
					} else if(iCol == 5) {
						$(grid).setCell(rowid, iCol, detailCode);
					}
					
					$("#codeDialog").dialog("close");
					checkRowChanged(lastSelected_salesPlanGrid_RowValue, grid, rowid);					

					
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
	<fieldset style="display: inline;">
	    <legend>판매계획 검색 </legend>
    		<label for="radio-1">판매계획일</label>
    		<input type="radio" name="searchDateCondition" value="salesPlanDate" id="radio-1">
    		<label for="radio-2">판매마감일</label>
    		<input type="radio" name="searchDateCondition" value="dueDateOfSales" id="radio-2">
	</fieldset>
	
	<input type="text" value="시작일" id="startDatePicker" />
	<input type="text" value="종료일" id="endDatePicker" />
	<input type="button" value="판매계획조회" id="salesPlanSearchButton" />
 
	<fieldset style="display: inline;">
	    <legend>판매계획 추가/삭제/저장</legend>
		 	<input type="button" value="새로운계획추가" id="salesPlanInsertButton" />
  			<input type="button" value="선택한계획삭제" id="salesPlanDeleteButton" />
  			<input type="button" value="일괄저장" id="batchSaveButton" />
  			
 	</fieldset>
<table id="salesPlanGrid" ></table>
<div id="salesPlanGridPager"></div>


<div id="codeDialog">
	<table id="codeGrid"></table>
</div>

<div id="InputDialog">
	<div>
		<label for="salesAmountBox" style="font-size: 20px; margin-right: 10px">계획수량</label>
			<input type="text" id="salesAmountBox"/> <br/>
		<label for="unitPriceOfSalesBox" style="font-size: 20px; margin-right: 10px">계획단가</label>
			<input type="text" id="unitPriceOfSalesBox"/> <br/>
		<label for="sumPriceOfSalesDiv" style="font-size: 20px; margin-right: 10px">합계액 : </label>
			<div id="sumPriceOfSalesDiv"></div>
	</div>
</div>


</body>
</html>