<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>품목 관리</title>
<style>

#lossRateInputBox, #leadTimeInputBox, #standardUnitPriceInputBox {
	display: inline;
	width: 115px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
	text-align : center;
}

#itemCodeInputBox, #itemNameInputBox{
	display: inline;
	width: 300px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
	text-align : center;
}

#descriptionInputBox {
	width: 300px;
	font-size: 20px;
	height: 70px;
	rows : 2;
	cols : 100;
}

#searchDetailCodeName {
	display: inline;
	width: 135px;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 18px;
	text-align : center;
	
}

#minPriceBox, #maxPriceBox {
	display: inline;
	width: 100px;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 18px;
	text-align : center;
	
}


#minPriceInputBox, #maxPriceInputBox{
	display: inline;
	width: 115px;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
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

var lastSelected_itemGrid_Id;   // 가장 나중에 선택한 품목 grid 의 행 id 
var lastSelected_itemGrid_RowValue;   // 가장 나중에 선택한 품목 grid 의 행 값 

var previousCellValue;  // 수정 가능한 셀에서 수정 전의 셀 값 
var searchConditionValue;    // 품목 검색 조건 => "ALL" : 전체 , "ITEM_CLASSIFICATION" : 품목분류 , 
									//"ITEM_GROUP_CODE" : 품목군, "STANDARD_UNIT_PRICE" : 표준단가
var resultList = [];  // 최종적으로 컨트롤러로 보내는 JS 객체 배열 


$(document).ready(function() {
		
	searchConditionValue = 'ALL';  // 최초 페이지 로딩시 전체 조회가 가능하도록 설정
	
	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	
    $( "#searchConditionBox" ).selectmenu({
    	
		width: 180 ,
		
		change: function (event, data) {
    		
			searchConditionValue = data.item.value;
    		
			switch(searchConditionValue) {
    		
    			case 'ALL' :
    				
    				$('#searchDetailCodeName').hide();
    				$('#minPriceBox').hide();
    				$('#maxPriceBox').hide();
    			
    				break;
    			
    			case 'ITEM_CLASSIFICATION' :
    				
    				$('#searchDetailCodeName').val('품목 분류');
    				
    				$('#searchDetailCodeName').show();
    				$('#minPriceBox').hide();
    				$('#maxPriceBox').hide();
    				
    				break;
    				
    			case 'ITEM_GROUP_CODE' :
    				
    				$('#searchDetailCodeName').val('품목군');
    				
    				$('#searchDetailCodeName').show();
    				$('#minPriceBox').hide();
    				$('#maxPriceBox').hide();
    				
    				break;
    		
    			case 'STANDARD_UNIT_PRICE' :
    				
    				$('#minPriceBox').val('최저가');
    				$('#maxPriceBox').val('최고가');
    				
    				$('#searchDetailCodeName').hide();
    				$('#minPriceBox').show();
    				$('#maxPriceBox').show();
    				
    				break;

    		}
		
		}
    	
    });
	
	$('#searchDetailCodeName').hide();
	
	$('#minPriceBox').hide();
	
	$('#maxPriceBox').hide();
	
	$("#codeDialog").hide();

	$("#InputDialog").hide();
	
	$("#priceInputDialog").hide();
	
	$("#itemInfoInputDialog").hide();
	
	
	initGrid();
	initEvent();
	
});

function initGrid() {

	// 품목 그리드 시작
	$('#itemGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "삭제", "코드 사용잠금", "품목코드", "품목명", "품목군코드", "품목 분류", "단위",  "손실율", 
			"리드타임", "표준단가", "비고", "status", "codeUseCheck", "beforeStatus" ,"deleteStatus"], 
		colModel : [
			{ name: "itemDeleteCheck", width: "50", resizable: true, align: "center" ,
				
				formatter : function (cellvalue, options, rowObj) {
					
				     var chk = "<input type='checkbox' name='itemDeleteCheck' value=" +
			     		JSON.stringify(options.rowId) + " />";
			     	return chk;
			     	
				}
			
			},
			{ name: "itemUseCheck", width: "100", resizable: true, align: "center" , 
				
				formatter : function (cellvalue, options, rowObj) {
					
					var chk = '';
					if(rowObj.codeUseCheck != 'N' ) {
						chk = "<input type='checkbox' name='itemUseCheck' value=" +
			     			JSON.stringify(options.rowId) + " />";
					} else {
						chk = "<input type='checkbox' name='itemUseCheck' value=" +
			     			JSON.stringify(options.rowId) + "  checked/>";	
					}
					
			     	return chk;
				}
			},
			{ name: "itemCode", width: "90", resizable: true, align: "center"} ,
			{ name: "itemName", width: "120", resizable: true, align: "center" } ,
			{ name: "itemGroupCode", width: "90", resizable: true, align: "center"} ,
			{ name: "itemClassification", width: "90", resizable: true, align: "center"} ,
			{ name: "unitOfStock", width: "70", resizable: true, align: "center"} ,
			{ name: "lossRate", width: "90", resizable: true, align: "center" , 
				formatter : function (cellvalue, options, rowObj) {

					if( cellvalue == null || cellvalue == '0' ) {
						
						return '';
						
					} else {
						
						return parseFloat(cellvalue);
						
					}
					
				}
			} ,
			{ name: "leadTime", width: "80", resizable: true, align: "center", 
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "standardUnitPrice", width: "80", resizable: true, align: "center", 
			        formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "description", width: "120", resizable: true, align: "center", editable: true } ,
			{ name: "status", width: "80", resizable: true, align: "center" } ,
			{ name: "codeUseCheck", width: "110", resizable: true, align: "center" , hidden: true } ,
			{ name: "beforeStatus", width: "10", resizable: true, align: "center" , hidden: true } ,
			{ name: "deleteStatus", width: "10", resizable: true, align: "center" , hidden: true }
		], 
		caption : '품목', 
		sortname : 'itemCode', 
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
		pager : '#itemGridPager',

		beforeEditCell(rowid, cellname, value, iRow, iCol){

        	if(value == null || value == "" ) {
        		previousCellValue = null;	 // "" 이면 이전 값으로 돌리는 경우 setCell 함수가 안먹힘
        	} else {
        		previousCellValue = value;
        	}
		},
		
		afterSaveCell(rowid, cellname, value, iRow, iCol){

        	var status = $(this).getCell(rowid,"status");

        	if(status == 'DELETE') {
        	
        		alertError("사용자 에러", "삭제 예정인 행이었습니다 ^^ </br> 원래 값으로 돌릴께요");
    			$(this).setCell(rowid,cellname, previousCellValue);	

        	} else if(status == 'NORMAL') {
        		
        		if( previousCellValue != value ) {
    				$(this).setCell(rowid,"status", "UPDATE");
        		}
        		
        	}
		},
		
		beforeSelectRow : function( rowid, event ) {
			
			var $target = $(event.target);
		    var $td = $target.closest("td");
	        var iCol = $.jgrid.getCellIndex($td[0]);
	        var colModel = $(this).jqGrid("getGridParam", "colModel");
		    var chekedColumn = colModel[iCol].name;
		    
	        var beforeStatus = $(this).getCell(rowid,"beforeStatus");
        	var currentStatus = $(this).getCell(rowid,"status");

	    	if ( $target.is(":checkbox") && $target.is(":checked") ) {  // 체크시

	    		switch(chekedColumn) {
	    		
	    			case 'itemDeleteCheck' :
                    	
	        			$(this).setCell(rowid,"status", "DELETE");
	        			$(this).setCell(rowid,"beforeStatus", currentStatus);
	    				
	    				break;
	    
	    		}
	        	
	    	} else if( $target.is(":checkbox") && !$target.is(":checked") ) {  // 체크 해제시
	    		
	    		switch(chekedColumn) {
	    		
	    			case 'itemDeleteCheck' :

	    				$(this).setCell(rowid,"status", beforeStatus);	    		
	    				break;
	    		
	    		}
	    		
	    	}
	    	
		},
		
		onSelectRow: function(rowid) {   
	
			if( lastSelected_itemGrid_Id != rowid ){
				lastSelected_itemGrid_Id = rowid;
				lastSelected_itemGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
		},
		
		onCellSelect : function(rowid, iCol, previousCellValue, e) {
			
			if( lastSelected_itemGrid_Id != rowid ){
				lastSelected_itemGrid_Id = rowid;
				lastSelected_itemGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
			var status = $(this).getCell(rowid,"status");			
				
			if( status == 'NORMAL' ||  status == 'UPDATE' ) {
				
				if( iCol == 5 ) {  // 품목군코드 cell 클릭
	            	
	            	showCodeDialog(this ,rowid , iCol , "IT-GROUP","품목군 검색")
	            
	            } else if( iCol == 6 ) {  // 품목 분류 cell 클릭
				
	            	showCodeDialog(this ,rowid , iCol , "IT","품목 분류 검색")
				
	        	} else if( iCol == 7 ) {  // 단위 cell 클릭

	            	showCodeDialog(this ,rowid , iCol , "UT","단위 검색");
	            	
	            } else if( iCol == 3 || iCol == 4 || iCol == 8 || iCol == 9 || iCol == 10 || iCol == 11 ) {  // 품목코드,  품목명, 손실율, 리드타임, 표준단가, 비고 클릭

	            	showItemInfoInputDialog(this, rowid);
	            	
	            } 
				
			} else if( status == 'INSERT' ) {
				
				if( iCol == 3 ) { // 품목코드 cell 클릭 : 'INSERT' 일 때만 실행됨
					
					// 원래는 여기에 중복된 품목코드인지 아닌지 확인하는 내용이 들어갸야 함
					
				} else if( iCol == 5 ) {  // 품목군코드 cell 클릭
	            	
	            	showCodeDialog(this ,rowid , iCol , "IT-GROUP","품목군 검색")
	            
	            } else if( iCol == 6 ) {  // 품목 분류 cell 클릭
				
	            	showCodeDialog(this ,rowid , iCol , "IT","품목 분류 검색")
				
	        	} else if( iCol == 7 ) {  // 단위 cell 클릭

	            	showCodeDialog(this ,rowid , iCol , "UT","단위 검색");
	            	
	            } else if( iCol == 3 || iCol == 4 || iCol == 8 || iCol == 9 || iCol == 10 || iCol == 11 ) {  // 품목코드,  품목명, 손실율, 리드타임, 표준단가, 비고 클릭

	            	showItemInfoInputDialog(this, rowid);
	            	
	            } 
				
			}
			

			
		}
		
	}); // 품목 그리드 끝
	

	// 품목 그리드 페이저
	$('#itemGrid').navGrid("#itemGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	
	
}

function initEvent() {
	
	$('#itemSearchButton').on("click" , function() {
		
		showItemGrid();

	});

	
	$('#searchDetailCodeName').on("click" , function() {
		
		var searchCode;
		
		switch (searchConditionValue) {
		
			case 'ITEM_CLASSIFICATION' :
				
				searchCode = 'IT';

				break;
				
		   	case 'ITEM_GROUP_CODE' : 
		   		
				searchCode = 'IT-GROUP';

				break;
		               
		}
		
		showCodeDialog(null, null, null, searchCode, '코드 검색');

	});

	
	$('#minPriceBox , #maxPriceBox').on("click" , function() { 

		showPriceInputDialog();
		
	});
	   
	
	$('#itemInsertButton').on("click",function() { 
		
		var newRowNum = $('#itemGrid').jqGrid('getDataIDs').length+1;  // 새로운 행 넘버

		$('#itemGrid').addRowData(	newRowNum, 
				{ "status":"INSERT" , "lossRate" : null , "deleteStatus":"LOCAL 삭제" } );
		
	});
	
	
	$('#useCheckChangeButton').on("click",function() {
		
		var confirmMsg = "";
		
		$ (  'input:checkbox[name="itemUseCheck"]'  ).each ( function( index ) {  // index 는 0 부터 시작, rowid = index + 1

			var itemClassification = $('#itemGrid').getCell( index + 1 , "itemClassification");
			var itemCode = $('#itemGrid').getCell( index + 1 , "itemCode");
			var codeUseCheck = $('#itemGrid').getCell( index + 1 , "codeUseCheck");

			if( this.checked == false && codeUseCheck == 'N' ) {
			
				confirmMsg += "품목코드 " + itemCode + " 사용잠금해제 \r"
				resultList.push( { divisionCodeNo : itemClassification , detailCode : itemCode , codeUseCheck : null } );				
				
			} else if ( this.checked == true  && codeUseCheck == '') {
				
				confirmMsg += "품목코드 " + itemCode + " 사용잠금 \r"
				resultList.push( { divisionCodeNo : itemClassification , detailCode : itemCode , codeUseCheck : 'N' } );
			}
	
		});
			
		var confirmStatus = "";
			
		if(resultList.length != 0) {

			confirmMsg += "\r위와 같이 작업합니다. 계속하시겠습니까?";
			confirmStatus = confirm(confirmMsg);

		}
			
		if(resultList.length != 0 && confirmStatus) {

		$.ajax({ 
			type : 'POST',
			url : '${pageContext.request.contextPath}/base/changeCodeUseCheckProcess.do' ,
			async :false,
			data : {
				method : 'changeCodeUseCheckProcess', 
				batchList : JSON.stringify(resultList)
			},
			dataType : 'json', 
			cache : false, 
			success : function(dataSet) {

				console.log(dataSet);
				var resultMsg = 
					"< 사용여부 변경 내역 >   <br/><br/>"
					+ "잠금해제된 품목코드 : "
					+ ( ( dataSet.result.USE.length != 0 ) ? dataSet.result.USE : "없음" ) + "</br></br>"
					+ "잠금된 품목코드 : " 
					+ ( ( dataSet.result.NOT_USE.length != 0 ) ? dataSet.result.NOT_USE : "없음" ) + "</br></br>"
					+ "위와 같이 작업이 처리되었습니다";
							
				alertError("성공", resultMsg);
						
				showItemGrid();  // 품목 그리드 새로고침
				
			}
		});  
						
		} else if(resultList.length != 0 && !confirmStatus) {
				
			alertError("^^" , "취소되었습니다")
				
		} else if(resultList.length == 0) {
				
			alertError("^^" , "사용 여부를 변경할 품목이 없습니다.")
				
		}
			
		resultList = [];  // 초기화
		
	});
	

	$('#batchSaveButton').on("click",function() {

		var rowIdList =  $('#itemGrid').jqGrid('getDataIDs');   // 품목 그리드의 전체 행 ID 배열
				
		var insertCount = 0;
		var updateCount = 0;
		var deleteCount = 0;

		var errorMsg = "< 제외 목록 > \r";  
		
		$(rowIdList).each( function(index, rowId) {   // 전체 행에 대해 반복문 시작
			
			var rowObject = $('#itemGrid').getRowData(rowId); // 행의 row 값 정보 객체
			var status = rowObject.status;
			
			if(status == 'INSERT' ) {
				
				if( rowObject.itemCode == '' ) {
					errorMsg += ( rowId + "행 : 품목코드 미입력 \r" );
					
				} else if(rowObject.itemName == '' ) {
					errorMsg += ( rowId + "행 : 품목명 미입력 \r" );

				} else if(rowObject.itemClassification == '' ){
					errorMsg += ( rowId + "행 : 품목 분류 미입력 \r" );

				} else if(rowObject.unitOfStock == '' ){
					errorMsg += ( rowId + "행 : 단위 미입력 \r" );
					
				} else if(rowObject.leadTime == '' ){
					errorMsg += ( rowId + "행 : 리드타임 미입력 \r" );
					
				} else if(rowObject.standardUnitPrice == '' ){
					errorMsg += ( rowId + "행 : 표준단가 미입력 \r" );
					
				} else {
					resultList.push(rowObject);	
					insertCount++;
				}

			} else if (status == 'UPDATE') {
				
				resultList.push(rowObject);
				updateCount++;
				
			} else if (status == 'DELETE') {
				
				if(rowObject.deleteStatus != 'LOCAL 삭제' ) {
					resultList.push(rowObject);
					deleteCount++;
				} else {
					$('#itemGrid').delRowData(rowId);
				}
				
			}
		});

		var confirmMsg = 
			( ( errorMsg == "< 제외 목록 > \r" ) ? "" : errorMsg + "\r" ) + 
			"< 가능한 작업 목록 > \r" +
			( ( insertCount != 0 ) ? insertCount + "개의 품목 추가 \n" : "" ) +
			( ( updateCount != 0 ) ? updateCount + "개의 품목 수정 \n" : "" ) +
			( ( deleteCount != 0 ) ? deleteCount + "개의 품목 삭제 \n" : ""  ) +
			"\r위와 같이 작업합니다. 계속하시겠습니까?"

		var confirmStatus = "";
		
		if(resultList.length != 0) {
			confirmStatus = confirm(confirmMsg);

		}
		
		if(resultList.length != 0 && confirmStatus) {
			
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/logisticsInfo/batchItemListProcess.do' ,
				async :false,
				data : {
					method : 'batchListProcess', 
					batchList : JSON.stringify(resultList)
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {

					console.log(dataSet);
					var resultMsg = 
						"< 품목 작업 내역 >   <br/><br/>"
						+ "추가된 품목코드 : "
						+ ( ( dataSet.result.INSERT.length != 0 ) ? dataSet.result.INSERT : "없음" ) + "</br></br>"
						+ "수정된 품목코드 : " 
						+ ( ( dataSet.result.UPDATE.length != 0 ) ? dataSet.result.UPDATE : "없음" ) + "</br></br>"
						+ "삭제된 품목코드 :  : " 
						+ ( ( dataSet.result.DELETE.length != 0 ) ? dataSet.result.DELETE : "없음" ) + "</br></br>"
						+ "위와 같이 작업이 처리되었습니다";
						
					alertError("성공", resultMsg);
					
					
					showItemGrid();  // 품목 그리드 새로고침
				}
			});  
			
		} else if(resultList.length != 0 && !confirmStatus) {
			
			alertError("^^", "취소되었습니다");
			
		} else if(resultList.length == 0) {
			
			alertError("^^", "추가/수정/삭제할 품목이 없습니다");
		}

		resultList = [];   // 초기화
		
		
	});

	
}


function showItemGrid() {
	
	$('#itemGrid').jqGrid('clearGridData');
		
	switch(searchConditionValue) {
	
		case 'ALL' :
	
			break;
	
		case 'ITEM_CLASSIFICATION' :
		
			if( $('#searchDetailCodeName').val() == '품목 분류'  ) {
				
				alertError( "사용자 에러" , "품목 분류를 먼저 선택하세요" ) ;
				return;

			} 
			
			break;
		
		case 'ITEM_GROUP_CODE' :
		
			if ( $('#searchDetailCodeName').val() == '품목군' ) {
				
				alertError( "사용자 에러" , "품목군을 먼저 선택하세요" ) ;
				return;

			}
			
			break;
			
		case 'STANDARD_UNIT_PRICE' :

			if ( $('#minPriceBox').val() == '최저가' || $('#maxPriceBox').val() == '최고가' ) {
				
				alertError( "사용자 에러" , "최저가 / 최고가를 먼저 입력하세요" ) ;
				return;			

			}
			
			break;

	}

	
	// ajax 시작
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/logisticsInfo/searchItem.do' ,
		data : {
			method : 'searchItem' ,
			searchCondition :  searchConditionValue ,
			itemClassification :  $('#searchDetailCode').val() ,
			itemGroupCode :  $('#searchDetailCode').val() ,
			minPrice :  $('#minPriceBox').val() ,
			maxPrice : $('#maxPriceBox').val() ,
				
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) { 
			console.log(dataSet);
			var gridRowJson = dataSet.gridRowJson;  // gridRowJson : 그리드에 넣을 json 형식의 data
			
			// 품목 Data 넣기
			$('#itemGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid');
		
	}});  // ajax 끝
	
}


function showItemInfoInputDialog(grid, rowid) {
	
	var itemCode = $(grid).getCell(rowid, "itemCode");
	var itemName = $(grid).getCell(rowid, "itemName");
	var lossRate = $(grid).getCell(rowid, "lossRate");
	var leadTime = $(grid).getCell(rowid, "leadTime");
	var standardUnitPrice = $(grid).getCell(rowid, "standardUnitPrice");
	var description = $(grid).getCell(rowid, "description");
	
	$('#itemCodeInputBox').val(itemCode);
	$('#itemNameInputBox').val(itemName);
	$('#lossRateInputBox').val(lossRate);
	$('#leadTimeInputBox').val(leadTime);
	$('#standardUnitPriceInputBox').val(standardUnitPrice);
	$('#descriptionInputBox').val(description);

	$("#itemInfoInputDialog").dialog({
		title : '품목 정보 입력',
		autoOpen : true,  
		width : 430,
		height : 400,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {

				var itemCode = $('#itemCodeInputBox').val();
				var itemName = $('#itemNameInputBox').val();
				var lossRate = parseFloat( Number( $('#lossRateInputBox').val() ) );
				var leadTime = Number( $('#leadTimeInputBox').val() );
				var standardUnitPrice = Number( $('#standardUnitPriceInputBox').val() );
				var description = $('#descriptionInputBox').val();
				
				if( isNaN(lossRate) || isNaN(leadTime) || isNaN(standardUnitPrice) ) {
					
					alertError("사용자 에러", "숫자가 아닌 값이 있습니다");
					return;
					
				} else if( lossRate > 1 ) {
					
					alertError("사용자 에러", "손실율은 1 보다 클 수 없습니다");
					return;
					
				} else if( leadTime == 0 ) {
					
					alertError("사용자 에러", "리드타임이 0 일 수는 없습니다");
					return;
					
				} else if(  $.trim( itemCode ) == '' ) {
					
					alertError("사용자 에러", "품목코드를 입력하지 않았습니다");
					return;
					
				} else if(  $.trim( itemName ) == '' ) {
					
					alertError("사용자 에러", "품목명을 입력하지 않았습니다");
					return;
					
				} 
				
				$(grid).setCell(rowid, "itemCode", itemCode );
				$(grid).setCell(rowid, "itemName", itemName );
				$(grid).setCell(rowid, "lossRate", lossRate );
				$(grid).setCell(rowid, "leadTime", leadTime );
				$(grid).setCell(rowid, "standardUnitPrice", standardUnitPrice );
				$(grid).setCell(rowid, "description", description );
				
				$("#itemInfoInputDialog").dialog("close");
				checkRowChanged(lastSelected_itemGrid_RowValue, grid, rowid);					

			},
			
			"취소" : function() {
				$("#itemInfoInputDialog").dialog("close");
			}
			
		}
	});
	
}


function showPriceInputDialog() {
	
	 $('#minPriceInputBox').val("");
	 $('#maxPriceInputBox').val("");
	
	$("#priceInputDialog").dialog({
		title : '최저가 / 최고가 입력',
		autoOpen : true,  
		width : 230,
		height : 200,
		modal : true,   // 폼 외부 클릭 못하게
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {

				var minPrice = $('#minPriceInputBox').val();
				var maxPrice = $('#maxPriceInputBox').val();
				
				
				if( isNaN(minPrice) || isNaN(maxPrice) ) {
					
					alertError("사용자 에러", "숫자가 아닌 값이 있습니다");
					return;
					
				} else if( Number(minPrice) >  Number(maxPrice) ) {
					
					alertError("사용자 에러", "최저가격이 최고가격보다 큽니다");
					return;
					
				}
				
				$('#minPriceBox').val(minPrice);
				$('#maxPriceBox').val(maxPrice);

				$("#priceInputDialog").dialog("close");
				
			},
			"취소" : function() {
				$("#priceInputDialog").dialog("close");
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
				
				if(codeUseCheck != 'n' && codeUseCheck != 'N') {
					
					if( grid != null ) {
						
						$(grid).setCell(rowid, iCol, detailCode);
						
					} else { 
						
						$('#searchDetailCodeName').val(detailName);
						$('#searchDetailCode').val(detailCode);
						
					}
					
					$("#codeDialog").dialog("close");
					checkRowChanged(lastSelected_itemGrid_RowValue, grid, rowid);					

					
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
		<legend>품목 검색 조건</legend>
			<select name="searchCondition" id="searchConditionBox">
				<option value="ALL">전체</option>
				<option value="ITEM_CLASSIFICATION">품목분류</option>
				<option value="ITEM_GROUP_CODE">품목군</option>
				<option value="STANDARD_UNIT_PRICE">표준단가</option>
		    </select>
		    
		<input type="text" id = "searchDetailCodeName" />
		<input type="text" value="최저 가격" id = "minPriceBox"/>
		<input type="text" value="최대 가격" id = "maxPriceBox" />
		<input type="button" value="품목 조회" id="itemSearchButton" />
		<input type="hidden" id = "searchDetailCode" />
		    
	</fieldset>
 
	<fieldset style="display: inline;">
	    <legend>품목 추가/수정/삭제</legend>
		 	<input type="button" value="새로운 품목 추가" id="itemInsertButton" />
  			<input type="button" value="품목 사용여부 변경" id="useCheckChangeButton" />
  			<input type="button" value="일괄저장" id="batchSaveButton" />
  			
 	</fieldset>
 	
<table id="itemGrid" ></table>
<div id="itemGridPager"></div>

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


<div id="itemInfoInputDialog">
	<table>
		<tr>
			<td><label for="itemCodeInputBox" style="font-size: 20px; margin-right: 10px">품목코드</label></td>
			<td><input type="text" id="itemCodeInputBox"/></td>
		</tr>
		<tr>
			<td><label for="itemNameInputBox" style="font-size: 20px; margin-right: 10px">품목명</label></td>
			<td><input type="text" id="itemNameInputBox"/></td>
		</tr>
		<tr>
			<td><label for="lossRateInputBox" style="font-size: 20px; margin-right: 10px">손실율</label></td>
			<td><input type="text" id="lossRateInputBox"/></td>
		</tr>
		<tr>
			<td><label for="leadTimeInputBox" style="font-size: 20px; margin-right: 10px">리드타임</label></td>
			<td><input type="text" id="leadTimeInputBox"/></td>
		</tr>
		<tr>
			<td><label for="standardUnitPriceInputBox" style="font-size: 20px; margin-right: 10px">표준단가</label></td>
			<td><input type="text" id="standardUnitPriceInputBox"/></td>
		</tr>
		<tr>
			<td><label for="descriptionInputBox" style="font-size: 20px; margin-right: 10px">비고</label></td>
			<td><textarea id="descriptionInputBox"></textarea></td>
		</tr>
	</table>
	
</div>


<div id="priceInputDialog">
	<div>
		<label for="minPriceInputBox" style="font-size: 20px; margin-right: 10px">최저가</label>
			<input type="text" id="minPriceInputBox"/> <br/>
		<label for="maxPriceInputBox" style="font-size: 20px; margin-right: 10px">최고가</label>
			<input type="text" id="maxPriceInputBox"/> <br/>
	</div>
</div>


<div id="codeDialog">
	<table id="codeGrid"></table>
</div>

</body>
</html>