<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>코드 관리</title>
<style>

#divisionCodeInputBox, #codeTypeInputBox, #codeNameInputBox {
	display: inline;
	width: 200px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
	text-align : center;
}


#descriptionInputBox {
	width: 350px;
	font-size: 20px;
	height: 70px;
	rows : 2;
	cols : 100;
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

var lastSelected_codeGrid_Id;   // 가장 나중에 선택한 코드 grid 의 행 id 
var lastSelected_codeGrid_RowValue;   // 가장 나중에 선택한 코드 grid 의 행 값 

var lastSelected_codeDetailGrid_Id;   // 가장 나중에 선택한 상세 코드 grid 의 행 id 
var lastSelected_codeDetailGrid_RowValue;   // 가장 나중에 선택한 상세 코드 grid 의 행 값 

var gridRowJson;  // 모든 그리드의 통합 Json Data => 배열 형식 : [ { ... } , { ... } , { ... } , ... ]

var previousCellValue;  // 수정 가능한 셀에서 수정 전의 셀 값 
var resultList = [];  // 최종적으로 컨트롤러로 보내는 JS 객체 배열 

var divisionCodeArr = []; 
var codeTypeArr = [];
var detailCodeArr = [];

$(document).ready(function() {
	
	$("input[type=button], input[type=submit]").button();   // jqueryUI Button 위젯 적용
	
	initGrid();
	initEvent();
	
	$('#newCodeInputDialog').hide();
	
	showCodeGrid();
	
	var codeList = $('#codeGrid').getRowData();  // 코드 grid 의 전체 데이터
	
	$( codeList ).each( function( index, obj )   {  // 코드 grid 의 전체 데이터에 대해 반복문 시작

		// 각각의 divisionCodeNo 을 divisionCodeArr 에 담기 : divisionCodeNo 는 중복되지 않으므로 그냥 담으면 됨
        divisionCodeArr.push( obj.divisionCodeNo );  
	
		// 중복된 codeType 을 제외하고 divisionCodeArr 에 담기
        if ($.inArray( obj.codeType.toUpperCase() , codeTypeArr ) == -1) { 
        	codeTypeArr.push( obj.codeType );  
        }
		
	});

	

});

function initGrid() {

	// 코드 그리드 시작
	$('#codeGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "삭제", "구분코드", "코드타입", "코드명", "변경 가능 여부", "설명" , "status", "beforeStatus" ,"deleteStatus"], 
		colModel : [
			
			{ name: "codeDeleteCheck", width: "40", resizable: true, align: "center" , hidden : true,
				
				formatter : function (cellvalue, options, rowObj) {
					
					var chk = '';
					
					if( rowObj.codeChangeAvailable == '변경가능' ) {
						
					    var chk = "<input type='checkbox' name='codeDeleteCheck' value=" +
				     		JSON.stringify(options.rowId) + " />";						

				     	return chk;
				     		
					} else if( rowObj.codeChangeAvailable == '변경불가능' ) {
						
						return '';
						
					}
				}
			
			},
			{ name: "divisionCodeNo", width: "60", resizable: true, align: "center" } ,
			{ name: "codeType", width: "60", resizable: true, align: "center" , editable: true } ,
			{ name: "divisionCodeName", width: "100", resizable: true, align: "center" , editable: true } ,
			{ name: "codeChangeAvailable", width: "90", resizable: true, align: "center" } ,
			{ name: "description", width: "90", resizable: true, align: "center", editable: true } ,
			{ name: "status", width: "80", resizable: true, align: "center" } ,
			{ name: "beforeStatus", width: "10", resizable: true, align: "center" , hidden: true } ,
			{ name: "deleteStatus", width: "10", resizable: true, align: "center" , hidden: true }
		], 
		caption : '코드', 
		//sortname : 'divisionCodeNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 230, 
		width : 610,
		autowidth : false, 
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
		pager : '#codeGridPager',

		beforeEditCell(rowid, cellname, value, iRow, iCol){
			
			if( lastSelected_codeGrid_Id != rowid ){
				lastSelected_codeGrid_Id = rowid;
				lastSelected_codeGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
        	if(value == null || value == "" ) {
        		previousCellValue = " ";	 // "" 이면 이전 값으로 돌리는 경우 setCell 함수가 안먹힘
        	} else {
        		previousCellValue = value;
        	}
        	
			showCodeDetailGrid();

        	
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
	    		
	    			case 'codeDeleteCheck' :
                    	
	        			$(this).setCell(rowid,"status", "DELETE");
	        			$(this).setCell(rowid,"beforeStatus", currentStatus);
	    				
	    				break;
	    				
	    		}
	        	
	    	} else if( $target.is(":checkbox") && !$target.is(":checked") ) {  // 체크 해제시
	    		
	    		switch(chekedColumn) {
	    		
	    			case 'codeDeleteCheck' :

	    				$(this).setCell(rowid,"status", beforeStatus);	    		
	    				break;

	    		}
	    		
	    	}
	    	
		},
		
		onSelectRow: function(rowid) {   
	
			if( lastSelected_codeGrid_Id != rowid ){
				lastSelected_codeGrid_Id = rowid;
				lastSelected_codeGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
			showCodeDetailGrid();
			
		},
		
		onCellSelect : function(rowid, iCol, previousCellValue, e) {
			
			if( lastSelected_codeGrid_Id != rowid ){
				lastSelected_codeGrid_Id = rowid;
				lastSelected_codeGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
			showCodeDetailGrid();
			
		}
		
	}); // 코드 그리드 끝
	

	// 코드 그리드 페이저
	$('#codeGrid').navGrid("#codeGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	
	
	// 상세코드 그리드 시작
	$('#codeDetailGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "삭제", "코드 사용잠금", "상세코드", "상세코드명", "설명", "codeUseCheck", "firstCodeUseCheck", 
			"status", "divisionCodeNo", "beforeStatus" ,"deleteStatus"], 
		colModel : [
			
			{ name: "detailCodeDeleteCheck", width: "40", resizable: true, align: "center" ,
				
				formatter : function (cellvalue, options, rowObj) {
					
					if( lastSelected_codeGrid_RowValue.codeChangeAvailable == '변경가능'  ) {
						
						var chk = "<input type='checkbox' name='detailCodeDeleteCheck' value=" +
				     		JSON.stringify(options.rowId) + " />";
				     	
				     	return chk;
						
					} else if ( lastSelected_codeGrid_RowValue.codeChangeAvailable == '변경불가능' ) {
						
						return '';
						
					}	     	
				}
			
			},
			{ name: "codeUseCheckBox", width: "80", resizable: true, align: "center" , 
				
				formatter : function (cellvalue, options, rowObj) {
					
					var chk;
					
					if(rowObj.codeUseCheck != 'N' && rowObj.status == 'NORMAL') {
						
						chk = "<input type='checkbox' name='codeUseCheckBox' value=" +
			     			JSON.stringify(options.rowId) + " />";
			     			
					} else if(rowObj.codeUseCheck == 'N' && rowObj.status == 'NORMAL'){
						
						chk = "<input type='checkbox' name='codeUseCheckBox' value=" +
			     			JSON.stringify(options.rowId) + "  checked/>";	
			     			
					} else if(rowObj.status == 'INSERT') {	
						
						chk = '';
						
					}
					
			     	return chk;
				}
			},
			{ name: "detailCode", width: "80", resizable: true, align: "center" , editable : true } ,
			{ name: "detailCodeName", width: "140", resizable: true, align: "center", editable : true } ,
			{ name: "description", width: "90", resizable: true, align: "center", editable: true , editable : true } ,
			{ name: "codeUseCheck", width: "90", resizable: true, align: "center" , hidden : true  } ,
			{ name: "firstCodeUseCheck", width: "90", resizable: true, align: "center" , hidden : true ,
				
				formatter : function (cellvalue, options, rowObj) {
					
			     	return ( rowObj.codeUseCheck == null ) ? "" : rowObj.codeUseCheck ;
				}
			
			},
			{ name: "status", width: "80", resizable: true, align: "center" } ,
			{ name: "divisionCodeNo", width: "60", resizable: true, align: "center" , hidden: true } ,
			{ name: "beforeStatus", width: "10", resizable: true, align: "center" , hidden: true } ,
			{ name: "deleteStatus", width: "10", resizable: true, align: "center" , hidden: true }
		], 
		caption : '상세코드', 
		sortname : 'detailCode', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 230, 
		width : 610,
		autowidth : false, 
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
		pager : '#codeDetailGridPager',

		beforeEditCell(rowid, cellname, value, iRow, iCol){

			if( lastSelected_codeDetailGrid_Id != rowid ){
				lastSelected_codeDetailGrid_Id = rowid;
				lastSelected_codeDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}

			if(value == null || value == "" ) {
        		previousCellValue = null;	 // "" 이면 이전 값으로 돌리는 경우 setCell 함수가 안먹힘
        	} else {
        		previousCellValue = value;
        	}
		},
		
		afterSaveCell(rowid, cellname, value, iRow, iCol){
			
        	var status = $(this).getCell(rowid,"status");

        	if( status == 'DELETE' ) {
        	
        		alertError("사용자 에러", "삭제 예정인 행이었습니다 ^^ </br> 원래 값으로 돌릴께요");
    			$(this).setCell(rowid,cellname, previousCellValue);	

        	} else if( status == 'NORMAL' && iCol != 3 ) {
        		
        		if( previousCellValue != value ) {
    				$(this).setCell(rowid,"status", "UPDATE");
        		}
        		
        	} else if( status == 'NORMAL' && iCol == 3 ) {  // NORMAL 행에서 상세코드 수정시 코드 중복 체크
        		
                if ($.inArray( value.toUpperCase() ,  detailCodeArr ) != -1) {  

                	alertError("ㅜㅜ", "중복된 코드입니다. 다른 코드를 입력하세요");
        			$(this).setCell(rowid,cellname, previousCellValue);	

                } else {
                	
                	if( previousCellValue != value ) {
                		
            			$(this).setCell(rowid,cellname, value.toUpperCase());	 // 소문자로 입력해도 대문자로 저장

        				$(this).setCell(rowid,"status", "UPDATE");
            		}
                	
                }
        		
        	} else if( ( status == 'INSERT' || status == 'UPDATE' ) && iCol == 3 ) {  // 추가 or 수정 행에서 상세코드 입력/수정시 코드 중복 체크
        		        		
                if ($.inArray( value.toUpperCase() ,  detailCodeArr ) != -1) {  

                	alertError("ㅜㅜ", "중복된 코드입니다. 다른 코드를 입력하세요");
                	
        			$(this).setCell(rowid,cellname, previousCellValue);	

                } else {
                	
        			$(this).setCell(rowid,cellname, value.toUpperCase());	 // 소문자로 입력해도 대문자로 저장

                	
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
        	var firstCodeUseCheck = $(this).getCell(rowid,"firstCodeUseCheck");
        	
	    	if ( $target.is(":checkbox") && $target.is(":checked") ) {  // 체크시

	    		switch(chekedColumn) {
	    		
	    			case 'detailCodeDeleteCheck' :
                    	
	        			$(this).setCell(rowid,"status", "DELETE");
	        			$(this).setCell(rowid,"beforeStatus", currentStatus);
	    				
	    				break;
	    			
	    			case 'codeUseCheckBox' :
	    				
	    				$(this).setCell(rowid, "codeUseCheck", "N");
	    				
	    				break;	
	    			
	    		}
	        	
	    	} else if( $target.is(":checkbox") && !$target.is(":checked") ) {  // 체크 해제시
	    		
	    		switch(chekedColumn) {
	    		
	    			case 'detailCodeDeleteCheck' :

	    				$(this).setCell(rowid,"status", beforeStatus);	    		
	    				break;

	    			case 'codeUseCheckBox' :
	    				
	    				$(this).setCell(rowid, "codeUseCheck", null);
	    					    				
	    				break;	
	    				
	    		}
	    		
	    	}
	    	
		},
		
		onSelectRow: function(rowid) {   
	
			if( lastSelected_codeDetailGrid_Id != rowid ){
				lastSelected_codeDetailGrid_Id = rowid;
				lastSelected_codeDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
		},
		
		onCellSelect : function(rowid, iCol, previousCellValue, e) {
			
			if( lastSelected_codeDetailGrid_Id != rowid ){
				lastSelected_codeDetailGrid_Id = rowid;
				lastSelected_codeDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
			
			var status = $(this).getCell(rowid,"status");

			if( status == 'NORMAL' ||  status == 'UPDATE' ) {
				
				
			} else if( status == 'INSERT' ) {
				
				
			}
			

			
		}
		
	}); // 상세코드 그리드 끝
	

	// 상세코드 그리드 페이저
	$('#codeDetailGrid').navGrid("#codeDetailGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	
	
}

function initEvent() {
	
	$('#codeInsertButton').on("click", function() {
		
		showNewCodeInputDialog();
		
	});
	

	$('#checkDivisionCodeDuplication').on("click", function() {
		
		if( $('#divisionCodeInputBox').val() == '' ) {
			alertError("ㅜㅜ", "새로 추가할 구분코드를 먼저 입력하세요")
			return;
		}

        if ($.inArray( $('#divisionCodeInputBox').val().toUpperCase() ,  divisionCodeArr ) != -1) {  

        	alertError("ㅜㅜ", "중복된 코드입니다. 다른 코드를 입력하세요");
        	return;

        } else {
        	alertError("^^", "사용 가능한 코드입니다  ^^");
        	return;
        	
        	
        }
		

	});
	
	
	
	$('#detailCodeInsertButton').on("click", function() {
		
		if( lastSelected_codeGrid_RowValue.codeChangeAvailable == '변경불가능') {
			alertError("사용자 에러" , "상세코드를 추가할 수 없는 코드입니다. </br> 관련 메뉴에서 직접 항목을 추가하세요. ^^");
			return;
		}
		
		var newRowNum = $('#codeDetailGrid').jqGrid('getDataIDs').length+1;  // 새로운 행 넘버
		
		$('#codeDetailGrid').addRowData(	newRowNum, 
				{ "divisionCodeNo" : lastSelected_codeGrid_RowValue.divisionCodeNo , 
				"status": "INSERT" , "deleteStatus":"LOCAL 삭제" } );
		
	});
	
	
	$('#changeCodeUseCheckButton').on("click", function() {
		
		var detailCodeList = $('#codeDetailGrid').getRowData();  // 상세코드 grid 의 전체 데이터
				
		var confirmMsg = "";
		
		$( detailCodeList ).each( function( index, obj )   {  // 코드 grid 의 전체 데이터에 대해 반복문 시작, obj 가 각각의 행
			
	        if ( obj.codeUseCheck != obj.firstCodeUseCheck ) {  
	        	
	        	obj.status = 'UPDATE';
	        	resultList.push(obj);

	        	if( obj.firstCodeUseCheck == '' ) {
	        		confirmMsg += obj.detailCodeName + ' 의 코드 사용 잠금 \r';
	        		
	        	} else if( obj.firstCodeUseCheck == 'N' ) {
	        		confirmMsg += obj.detailCodeName + ' 의 코드 사용 잠금 해제 \r';
	        		
	        	}
	        	
	        }

		});  // 반복문 끝

		
		var confirmStatus = "";
		
		if(resultList.length != 0) {
			confirmStatus = confirm(confirmMsg += "\r위와 같이 작업합니다. 계속하시겠습니까?");
				
		}
		
		if(resultList.length != 0 && confirmStatus) { 
			
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/base/batchListProcess.do' ,
				async :false,
				data : {
					method : 'batchListProcess', 
					'tableName' : 'CODE_DETAIL' ,
					batchList : JSON.stringify(resultList)
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {

					console.log(dataSet);
					var resultMsg = 
						"< 코드 사용잠금 작업 내역 >   <br/><br/>"
						+ "수정된 코드 : " 
						+ ( ( dataSet.result.UPDATE.length != 0 ) ? dataSet.result.UPDATE : "없음" ) + "</br></br>"
						+ "위와 같이 작업이 처리되었습니다";
						
					alertError("성공", resultMsg);
					
					showCodeGrid();
					showCodeDetailGrid();
				}
			});  
			
		} else if(resultList.length != 0 && !confirmStatus) {
			
			alertError("^^", "취소되었습니다");
			
		} else if(resultList.length == 0) {
			
			alertError("^^", "사용잠금을 변경할 코드가 없습니다");
		}

		resultList = [];   // 초기화

	});
	
	
	
	$('#codeBatchSaveButton , #detailCodeBatchSaveButton ').on("click", function(event) {

		var clickedButtonId = $( event.target ).attr("id");
		var tableName;
		var rowIdList;
		var grid;
		
		switch(clickedButtonId) {
			
			case 'codeBatchSaveButton' : 
			
				tableName = 'CODE';
				grid = $('#codeGrid');
				rowIdList =  $('#codeGrid').jqGrid('getDataIDs');   // 코드 그리드의 전체 행 ID 배열
				break;
			
			case 'detailCodeBatchSaveButton' : 
				
				tableName = 'CODE_DETAIL';
				grid = $('#codeDetailGrid');
				rowIdList =  $('#codeDetailGrid').jqGrid('getDataIDs');   // 상세 코드 그리드의 전체 행 ID 배열
				break;

		}

		var insertCount = 0;
		var updateCount = 0;
		var deleteCount = 0;

		var errorMsg = "< 제외 목록 > \r";  


		$(rowIdList).each( function(index, rowId) {   // 전체 행에 대해 반복문 시작
			
			var rowObject = grid.getRowData(rowId); // 행의 row 값 정보 객체

			var status = rowObject.status;
			
			if(status == 'INSERT' ) {
								
				if( ( tableName == 'CODE' && rowObject.divisionCodeNo == '' ) || 
						( tableName == 'CODE_DETAIL' && rowObject.detailCode == '' ) ) {
					errorMsg += ( rowId + "행 : 코드 미입력 \r" );
					
				} else if( ( tableName == 'CODE' && rowObject.divisionCodeName == '' ) || 
						( tableName == 'CODE_DETAIL' && rowObject.detailCodeName == '' ) ) {
					errorMsg += ( rowId + "행 : 코드명 미입력 \r" );

				} else if( tableName == 'CODE' && rowObject.itemClassification == '' ){
					errorMsg += ( rowId + "행 : 품목 분류 미입력 \r" );

				} else if( tableName == 'CODE' && rowObject.codeType == '' ){
					errorMsg += ( rowId + "행 : 코드타입 미입력 \r" );
					
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
					grid.delRowData(rowId);
				}
				
			}
		});
			
		var confirmMsg = 
			( ( errorMsg == "< 제외 목록 > \r" ) ? "" : errorMsg + "\r" ) + 
			"< 가능한 작업 목록 > \r" +
			( ( insertCount != 0 ) ? insertCount + "개의 코드 추가 \n" : "" ) +
			( ( updateCount != 0 ) ? updateCount + "개의 코드 수정 \n" : "" ) +
			( ( deleteCount != 0 ) ? deleteCount + "개의 코드 삭제 \n" : ""  ) +
			"\r위와 같이 작업합니다. 계속하시겠습니까?"

		var confirmStatus = "";
		
		if(resultList.length != 0) {
			confirmStatus = confirm(confirmMsg);
				
		}
		
		if(resultList.length != 0 && confirmStatus) {
			
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/base/batchListProcess.do' ,
				async :false,
				data : {
					method : 'batchListProcess', 
					'tableName' : tableName ,
					batchList : JSON.stringify(resultList)
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {

					console.log(dataSet);
					var resultMsg = 
						"< 코드 작업 내역 >   <br/><br/>"
						+ "추가된 코드 : "
						+ ( ( dataSet.result.INSERT.length != 0 ) ? dataSet.result.INSERT : "없음" ) + "</br></br>"
						+ "수정된 코드 : " 
						+ ( ( dataSet.result.UPDATE.length != 0 ) ? dataSet.result.UPDATE : "없음" ) + "</br></br>"
						+ "삭제된 코드 : " 
						+ ( ( dataSet.result.DELETE.length != 0 ) ? dataSet.result.DELETE : "없음" ) + "</br></br>"
						+ "위와 같이 작업이 처리되었습니다";
						
					alertError("성공", resultMsg);
					
					showCodeGrid(); 
					showCodeDetailGrid();
				}
			});  
			
		} else if(resultList.length != 0 && !confirmStatus) {
			
			alertError("^^", "취소되었습니다");
			
		} else if(resultList.length == 0) {
			
			alertError("^^", "추가/수정/삭제할 코드가 없습니다");
		}

		resultList = [];   // 초기화

		
	});

	
}



function showCodeGrid() {
	
	$('#codeGrid').jqGrid('clearGridData');

	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/base/searchCodeList.do' ,
		async :false,
		data : {
			method : 'findCodeList'
		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) {

			console.log(dataSet);

			gridRowJson = dataSet.codeList;  // gridRowJson : 모든 그리드에 넣을 json 형식의 data
			
			// 코드 Data 넣기
			$('#codeGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid');
		}
		
	});  
	
}

function showCodeDetailGrid() {
	
	detailCodeArr =[]   // 초기화
	
	$('#codeDetailGrid').jqGrid('clearGridData');

	if( lastSelected_codeGrid_RowValue.codeChangeAvailable == '변경불가능' ) {
		
		$('#codeDetailGrid')
		.jqGrid('setGridParam',{ cellEdit : false })
		.trigger('reloadGrid');
		
	} else { 
		
		$('#codeDetailGrid')
		.jqGrid('setGridParam',{ cellEdit : true })
		.trigger('reloadGrid');
		
	}

	$( gridRowJson ).each( function( index, obj )   {  // gridRowJson 의 전체 데이터에 대해 반복문 시작

		// gridRowJson 배열 Data 중의 어떤 객체 ( obj ) 의 divisionCodeNo 가 
		// 가장 최근에 선택한 codeGrid 행의 divisionCodeNo 와 같으면
		if( obj.divisionCodeNo == lastSelected_codeGrid_RowValue.divisionCodeNo ) {
			
			// obj 의 codeDetailTOList : 선택된 divisionCodeNo 에 해당하는 상세코드 Data 
			$('#codeDetailGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : obj.codeDetailTOList })
				.trigger('reloadGrid');
			
		} 
		
	});  // 반복문 끝
	

	/*
	
	// codeGrid 를 클릭할 때마다 상세코드 Data 를 따로 불러오는 경우의 ajax
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/base/searchCodeList.do' ,
		async :false,
		data : {
			method : 'findDetailCodeList',
			divisionCode : lastSelected_codeGrid_RowValue.divisionCodeNo
		},
		dataType : 'json', 
		async : false ,  // ajax 를 동기 방식으로 설정
		cache : false , 
		success : function(dataSet) {

			console.log(dataSet);

			var gridRowJson = dataSet.detailCodeList;  // gridRowJson : 그리드에 넣을 json 형식의 data
			
			// 코드 Data 넣기
			$('#codeDetailGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid');
		}
		
	});  
	*/
	
	
	var detailCodeList = $('#codeDetailGrid').getRowData();  // 상세코드 grid 의 전체 데이터
	
	$( detailCodeList ).each( function( index, obj )   {  // 코드 grid 의 전체 데이터에 대해 반복문 시작

        if ($.inArray( obj.detailCode.toUpperCase() , detailCodeArr ) == -1) {  
        	detailCodeArr.push( obj.detailCode );  
        }
	
		
	});  // 반복문 끝
	
	
}

function showNewCodeInputDialog() {
	
	$("#newCodeInputDialog").dialog({
		
		title : '새로운 코드 입력',
		autoOpen : true,  
		width : 480,
		height : 320,
		modal : true,   // 폼 외부 클릭 못하게
		
		open : function(event, ui) {
			
			$('#codeTypeInputBox').autocomplete({
				
				source : codeTypeArr ,
				autoFocus : true
				
			});
			
		},
		
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {

				var divisionCode = $('#divisionCodeInputBox').val();
				var codeType = $('#codeTypeInputBox').val();
				var codeName = $('#codeNameInputBox').val();
				var description = $('#descriptionInputBox').val();
				
				if( $.inArray( divisionCode.toUpperCase()  , divisionCodeArr ) != -1 ) {
					
					alertError("ㅜㅜ", "중복된 코드입니다. 다른 코드를 입력하세요")
					return;
					
				} else if( codeType == '' || codeName == '' ) {  
					
					alertError("ㅜㅜ", "코드타입 또는 코드명을 입력하지 않았습니다")
					return;

				}				

				var newCodeObj = { "divisionCodeNo" : divisionCode , "codeType" : codeType , 
						"divisionCodeName" : codeName , "codeChangeAvailable" : "변경가능" , 
						"description" : description , status : "INSERT" };

				resultList.push(newCodeObj);
				
				$.ajax({ 
					type : 'POST',
					url : '${pageContext.request.contextPath}/base/batchListProcess.do' ,
					async :false,
					data : {
						method : 'batchListProcess', 
						'tableName' : 'CODE' ,
						batchList : JSON.stringify(resultList)
					},
					dataType : 'json', 
					cache : false, 
					success : function(dataSet) {

						console.log(dataSet);
						var resultMsg = 
							"< 코드 작업 내역 >   <br/><br/>"
							+ "추가된 코드 : "
							+ ( ( dataSet.result.INSERT.length != 0 ) ? dataSet.result.INSERT : "없음" ) + "</br></br>"
							+ "위와 같이 작업이 처리되었습니다";
							
						alertError("성공", resultMsg);
						
						showCodeGrid();
						showDetailCodeGrid();

					}
				});  
								
				resultList = [];  // 초기화

				$("#newCodeInputDialog").dialog("close");

			},
			
			"취소" : function() {
				$("#newCodeInputDialog").dialog("close");
			}
			
		}
	});
	
}


</script>
</head>
<body>

<table>

	<tr>
		<td>
			<fieldset style="display: inline;">
	    		<legend>코드</legend>
					<input type="button" value="구분코드 추가" id="codeInsertButton" />
					<input type="button" value="구분코드 일괄저장" id="codeBatchSaveButton" />
			
			</fieldset>
		</td>
	
		<td>
			<fieldset style="display: inline;">
			    <legend>세부코드</legend>
					<input type="button" value="세부코드 추가" id="detailCodeInsertButton" />
					<input type="button" value="코드 사용여부 변경" id="changeCodeUseCheckButton" />
					<input type="button" value="세부코드 일괄저장" id="detailCodeBatchSaveButton" />
			
			</fieldset>
		</td>
	</tr>


	<tr>
		<td>	
			<table id="codeGrid"></table>
			<div id="codeGridPager"></div>

		</td>
		
		<td>
			<table id="codeDetailGrid"></table>
			<div id="codeDetailGridPager"></div>
			
		</td>
	</tr>

</table>


<div id="newCodeInputDialog">
	<table>
		<tr>
			<td><label for="divisionCodeInputBox" style="font-size: 20px; margin-right: 10px">구분코드</label></td>
			<td><input type="text" id="divisionCodeInputBox"/>&nbsp;&nbsp;
					<input type="button" value = "코드 중복 체크" id="checkDivisionCodeDuplication" style="font-size: 16px;"/></td>
		</tr>
		<tr>
			<td><label for="codeTypeInputBox" style="font-size: 20px; margin-right: 10px">코드타입</label></td>
			<td><input type="text" id="codeTypeInputBox"/></td>
		</tr>
		<tr>
			<td><label for="codeNameInputBox" style="font-size: 20px; margin-right: 10px">코드명</label></td>
			<td><input type="text" id="codeNameInputBox"/></td>
		</tr>
		<tr>
			<td><label for="descriptionInputBox" style="font-size: 20px; margin-right: 10px">설명</label></td>
			<td><textarea id="descriptionInputBox"></textarea></td>
		</tr>
	</table>
	
</div>

</body>
</html>