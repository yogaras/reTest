<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>수주 등록</title>
<style>

#startDatePicker, #endDatePicker {
	display: inline;
	width: 115px;
	padding-left: 1%;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
	font-size: 20px;
}
legend {
	color: #FFFFFF;
}
.ui-datepicker{
	z-index: 9999 !important;
}

.ui-dialog { 
	z-index: 9999 !important; 
	font-size:12px;
}

</style>
<script>

function showModalCode(codeName,divisionCode){
    var option="width=550; height=430; left=500; top=200; titlebar=no; toolbar=no,status=no,menubar=no,resizable=yes, location=no";
    window.open("${pageContext.request.contextPath}/basicInfo/codeModal.html?code="+codeName+"&divisionCode="+divisionCode,"newwins",option);

}
var lastSelected_contractCandidateGrid_Id;   // 가장 나중에 선택한 수주후보 grid 의 행 id 
var lastSelected_contractCandidateGrid_RowValue;   // 가장 나중에 선택한 수주후보 grid 의 행 값

var lastSelected_estimateDetailGrid_Id;    // 가장 나중에 선택한 견적상세 grid 의 행 id 
var lastSelected_estimateDetailGrid_RowValue;   // 가장 나중에 선택된 (수정되기 전) 견적상세 grid 의 행 값 

var gridRowJson;  // 모든 그리드의 통합 Json Data => 배열 형식 : [ { ... } , { ... } , { ... } , ... ]


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
	
	initGrid();
	initEvent();
	
});

function initGrid() {
	
	// 수주등록 그리드 시작
	$('#contractCandidateGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "", "견적일련번호", "수주유형분류", "거래처코드", "견적일자", "수주요청자", "유효일자","견적담당자코드", "비고"], 
		colModel : [ 
			{ name: "check", width: "50", resizable: true, align: "center" ,
				//celvalue=현재 formatter가 걸려있는 컬럼의 값, rowObj = 해당 행의 데이터, 
				//option = {rowId , colModel} 요소 1) rowId : row의 id, 2) colModel : jqGrid의 colModel 배열의 형식				  
				formatter : function (celvalue, options, rowObj) {
				     var chk = "<input type='radio' name='chk' value=" + JSON.stringify(options.rowId) +" />";     
				     return chk; 
				}
			},
			{ name: "estimateNo", width: "100", resizable: true, align: "center"} ,
			{ name: "contractType", width: "90", resizable: true, align: "center"} ,
			{ name: "customerCode", width: "80", resizable: true, align: "center"} ,
			{ name: "estimateDate", width: "90", resizable: true, align: "center", 
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d', defaultValue:null }  } ,
			{ name: "contractRequester", width: "90", resizable: true, align: "center", editable : true } ,
			{ name: "effectiveDate", width: "90", resizable: true, align: "center",
				  formatter: 'date', 
				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d', defaultValue:null } } ,
			{ name: "personCodeInCharge", width: "100", resizable: true, align: "center" } ,
			{ name: "description", width: "130", resizable: true, align: "center", editable: true }
		], 
		caption : '수주 가능 견적 조회', 
		sortname : 'estimateNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : true, 
		rownumWidth : 30, 
		height : 100, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : false,   // onSelectRow 이벤트 실행되도록 false : 수주유형분류,  수주요청자 cell 클릭시는 true 로 바뀜
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
		pager : '#contractCandidateGridPager',
		
		onSelectRow: function(rowid) { 
			
        	if( lastSelected_contractCandidateGrid_Id != rowid ){
				lastSelected_contractCandidateGrid_Id = rowid;
				lastSelected_contractCandidateGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
        	}
        	
        	showEstimateDetailGrid();
        	
		}, 
		
		onCellSelect : function(rowid, iCol, previousCellValue, e) { 
	
        	if( lastSelected_contractCandidateGrid_Id != rowid ){
				lastSelected_contractCandidateGrid_Id = rowid;
				lastSelected_contractCandidateGrid_RowValue = $(this).jqGrid('getRowData', rowid);
        	}      
			        	
        	if(iCol == 3) {
        		
        		var colModel = $(this).jqGrid('getGridParam', 'colModel');
                var colName = colModel[iCol].name; 
                showModalCode(colName,"CT");
				
            	//showCodeDialog(this ,rowid , iCol , "CT","수주 유형 분류")

			} if(iCol == 6 || iCol == 9) {
				
				$(this).jqGrid('setGridParam',{ cellEdit : true });  // editable : true 인 셀 클릭시 cellEdit 변경

			} else {
				
				$(this).jqGrid('setGridParam',{ cellEdit : false });
				
			}

		}


	}); // 수주후보 그리드 끝
	
	// 수주후보 그리드 페이저
	$('#contractCandidateGrid').navGrid("#contractCandidateGridPager", {
		add : false,
		del : false,
		edit : false,
		search : true,
		refresh : true,
		view: true
	});
	
	// 견적 상세 그리드 시작 => 수주 등록 전에 납기일만 변경 가능함
	$('#estimateDetailGrid').jqGrid({ 
		mtype : 'POST', 
		datatype : 'local',
		colNames : [ "견적상세일련번호", "품목코드", "품목명", "단위", "납기일", 
			"견적수량", "견적단가", "합계액", "비고"], 
		colModel : [
			{ name: "estimateDetailNo", width: "110", resizable: true, align: "center"} ,
			{ name: "itemCode", width: "70", resizable: true, align: "center" } ,
			{ name: "itemName", width: "150", resizable: true, align: "center"} ,
			{ name: "unitOfEstimate", width: "40", resizable: true, align: "center"} ,
			{ name: "dueDateOfEstimate", width: "70", resizable: true, align: "center", editable: true,
//				  formatter: 'date',   => 주석 처리 : 여기 지정되면 사용자가 값을 미입력시 걸러주지 못함
//				  formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },  
				  edittype: 'text', 
		          editoptions: { size: 12, maxlengh: 12, 
						dataInit: function (element) {
							
							var checkedRowId = $('input[type=radio][name=chk]:checked').val();
							var checked_contractCandidateGrid_RowValue = $('#contractCandidateGrid').jqGrid('getRowData', checkedRowId);
							
							$(element).datepicker({ 
								minDate : checked_contractCandidateGrid_RowValue.estimateDate ,
								maxDate : checked_contractCandidateGrid_RowValue.effectiveDate ,
								changeMonth: true, 
								numberOfMonths: 1, 
								onClose: function(dateText, datepicker) {
									$(this).editCell(checkedRowId,"dueDateOfEstimate",false); 
								}
		                  })}
		          }, 
		          editrules: { date: true } 
			} ,
			{ name: "estimateAmount", width: "70", resizable: true, align: "center",
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "unitPriceOfEstimate", width: "80", resizable: true, align: "center", 
				formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } ,
			{ name: "sumPriceOfEstimate", width: "80", resizable: true, align: "center", 
			        formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
	        } , 	
			{ name: "description", width: "100", resizable: true, align: "center", editable: true } 

		], 
		caption : '견적상세', 
		sortname : 'estimateDetailNo', 
		multiselect : false, 
		multiboxonly : false,
		viewrecords : false, 
		rownumWidth : 30, 
		height : 130, 
		width : 1000,
		autowidth : true, 
		shrinkToFit : false, 
		cellEdit : true,
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
		cache : false ,  
		
		onSelectRow: function(rowid) {   
	
			if( lastSelected_estimateDetailGrid_Id != rowid ){
				lastSelected_estimateDetailGrid_Id = rowid;
				lastSelected_estimateDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}			
		}, 

		onCellSelect : function(rowid, iCol, previousCellValue, e) { 
			
			if( lastSelected_estimateDetailGrid_Id != rowid ){
				lastSelected_estimateDetailGrid_Id = rowid;
				lastSelected_estimateDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
			}
		}
	}); // 견적 상세 그리드 끝
	
}

function initEvent() {

	$('#contractCandidateSearchButton').on("click", function() { //수주가능견적조회 버튼 이벤트
	
		var searchCondition = $(':input:radio[name=searchDateCondition]:checked').val()
		//val() 이라는 메서드 떄문에 searchByDate(일자검색) 아니면 searchByPeriod(기간검색) 가 담긴다.
													

		// 초기화
		$('#contractCandidateGrid').jqGrid('clearGridData');
		$('#estimateDetailGrid').jqGrid('clearGridData');
		lastSelected_contractCandidateGrid_Id = "";
		
		// ajax 시작
		$.ajax({ 
			type : 'POST',
			url : '${pageContext.request.contextPath}/sales/searchEstimateInContractAvailable.do', 
			data : {
				method : 'searchEstimateInContractAvailable', //실행할 메소드 명 
				startDate : $("#startDatePicker").val() , //시작일자 변수에
				endDate : (searchCondition == 'searchByDate' ) ? $("#startDatePicker").val() : $("#endDatePicker").val() 
				//삼항연산자   searchCondition이 일자검색 라디오와 같으면  ?   시작일자의 value가 변수에 담김 : 아니면 종료일자가 변수에 담김.
			},
			dataType : 'json', 
			cache : false, 
			success : function(dataSet) {				
				gridRowJson = dataSet.gridRowJson;  // gridRowJson : 그리드에 넣을 json 형식의 data				
				if($('#startDatePicker').val()=='검색일자'){
					alertError('날짜오류','검색일자를 입력해주시오.');
				}else if($('#startDatePicker').val()==''){
					alertError('날짜오류','날짜를 삽입하여주십시오.');					
				}
				
				
				if( gridRowJson.length != 0 ) {
					
					// 견적Data 넣기
					
					$('#contractCandidateGrid')
						.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
						.trigger('reloadGrid');
					
				}else{					
				alertError('ㅜㅜ','조회된 데이터가 없습니다');
				}
				

			
		}});  // ajax 끝
		
	});	
	
	$('#searchByDateRadio').on("click", function() {
		
		$('#endDatePicker').hide();
		$('#startDatePicker').val("검색일자");
	});
	
	$('#searchByPeriodRadio').on("click", function() {
		
		$('#startDatePicker').val("시작일");
		$('#endDatePicker').val("종료일");
		$('#endDatePicker').show();		
	});

	$('#contractBatchInsertButton').on("click", function() {
		
		var checkedRowId = $('input[type=radio][name=chk]:checked').val();
		
		var checkedEstimateValue = $('#contractCandidateGrid').jqGrid('getRowData', checkedRowId);
		

		// 데이터 유효성 검사
		if(checkedRowId == undefined || checkedRowId == "" ) {
			
			alertError("사용자 에러", "수주 등록할 견적을 선택하세요");
			return;
			
		} else if(checkedEstimateValue.contractType == "") {

			alertError("사용자 에러", "수주 유형을 선택하지 않았습니다");
			return;
			
		}
		
		var confirmMsg = "견적번호 " +  checkedEstimateValue.estimateNo + " 을 수주 등록합니다. \r\n"
			+ "계속하겠습니까?";
		
		if( confirm(confirmMsg) ) {
			
			var estimateDetailList = $("#estimateDetailGrid").getRowData();
			alert(JSON.stringify(checkedEstimateValue));
			checkedEstimateValue.contractDetailTOList = estimateDetailList;  // 컨트롤러에서 수주상세 리스트로 변환됨				
			alert(JSON.stringify(checkedEstimateValue));
			// 오늘 일자 문자열 생성 : '2018-01-01' 형식
			var now = new Date(); //getFullYear() = 주어진 날짜의 현지시간 기준 연도 반환 2020-04-27
			var today = now.getFullYear() + "-" +('0' + (now.getMonth() +1 )).slice(-2) + "-" + ('0' + now.getDate()).slice(-2);		
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/sales/addNewContract.do' ,
				async :false,
				data : {
					method : 'addNewContract', 
					batchList : JSON.stringify(checkedEstimateValue),
					personCodeInCharge : "${sessionScope.empCode}",
					contractDate : 	today			
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) {
					console.log(dataSet);
					
					$('#contractCandidateGrid').delRowData(checkedRowId);
					$('#estimateDetailGrid').jqGrid('clearGridData');
					
					var errorMsg = "</br>";
					
 					if(dataSet.errorCode < 0){
 						alertError('에러',dataSet.errorMsg);
 						return;
					}
					
					if(dataSet.errorMsg != null){
						errorMsg = errorMsg + dataSet.errorMsg;
					}
					
					alertError("성공!", "견적번호 " + checkedEstimateValue.estimateNo +
							" 가 </br> 수주등록되었습니다 "
							+ errorMsg
					);
						
				}
			});  
			
		} else {
			
			alertError("^^", "취소되었습니다");
			
		}
		
	});

	
	$('#estimateCancleButton').on("click", function() {

		var checkedRowId = $('input[type=radio][name=chk]:checked').val();
		
		if(checkedRowId == undefined || checkedRowId == "" ) {
			
			alertError("사용자 에러", "취소할 견적을 선택하세요");
			return;
		} 
		
		var checkedEstimateValue = $('#contractCandidateGrid').jqGrid('getRowData', checkedRowId);
		
		var confirmMsg = "견적번호 " +  checkedEstimateValue.estimateNo + " 을 취소합니다. \r\n" +
			"이후 수주 가능 목록에서 조회되지 않습니다. \r\n취소하시겠습니까?";
		
		if( confirm(confirmMsg) ) {
			
			$.ajax({ 
				type : 'POST',
				url : '${pageContext.request.contextPath}/sales/cancleEstimate.do' ,
				data : {
					method : 'cancleEstimate', 
					estimateNo : $('#contractCandidateGrid').jqGrid('getRowData', checkedRowId).estimateNo
				},
				dataType : 'json', 
				cache : false, 
				success : function(dataSet) { 

					console.log(dataSet);
					$('#contractCandidateGrid').delRowData(checkedRowId);
					$('#estimateDetailGrid').jqGrid('clearGridData');
					
					var cancledEstimateNo = dataSet.cancledEstimateNo;
					alertError("성공!", "견적번호 " + cancledEstimateNo + " 가 취소되었습니다");
					
				}
			});	
			
		} else {
			
			alertError("^^", "취소되었습니다");

		}
		
	});
	
}

function showEstimateDetailGrid() {
	
	// 초기화
	$('#estimateDetailGrid').jqGrid('clearGridData');
	
	var checkedRowId = $('input[type=radio][name=chk]:checked').val();

	// gridRowJson 배열 Data 중의 어떤 객체 ( obj ) 의 estimateNo 가 
	// estimateGrid 의 체크된 행 의 estimateNo 와 같으면
	
	$( gridRowJson ).each( function( index, obj )   {  // gridRowJson 의 전체 데이터에 대해 반복문 시작
		
		if( obj.estimateNo == $('#contractCandidateGrid').jqGrid('getRowData', checkedRowId).estimateNo ) {
			
			// obj 의 estimateDetailTOList : 선택된 estimateNo 에 해당하는 견적상세 Data 
			$('#estimateDetailGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : obj.estimateDetailTOList })
				.trigger('reloadGrid');
			
		} 
		
	});  // 반복문 끝	
	
	
	
	/*
	
	// 수주후보 그리드를 클릭할 때마다 견적상세 Data 를 따로 불러오는 경우의 ajax
	
	$.ajax({ 
		type : 'POST',
		url : '${pageContext.request.contextPath}/sales/searchEstimate.do' ,
		data : {
			method : 'searchEstimateDetailInfo', 
			estimateNo : $('#contractCandidateGrid').jqGrid('getRowData', checkedRowId).estimateNo 

		},
		dataType : 'json', 
		cache : false, 
		success : function(dataSet) { 

			console.log(dataSet);
			var gridRowJson = dataSet.gridRowJson;
			
			$('#estimateDetailGrid').jqGrid('clearGridData');

			// 견적상세Data 넣기
			$('#estimateDetailGrid')
				.jqGrid('setGridParam',{ datatype : 'local', data : gridRowJson })
				.trigger('reloadGrid');
		}
	});	
	
	*/
}


					   
/* 
function showCodeDialog(grid, rowid, iCol, divisionCodeNo, title){
	$("#codeDialog").dialog({
		title : '코드 검색' ,
		width:500,
		height:500,
		modal : true   // 폼 외부 클릭 못하게
	});

	$.jgrid.gridUnload("#codeGrid");
	//그리드 초기화
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
				
				if(codeUseCheck != 'N' && codeUseCheck != 'n') {
					
					$(grid).setCell(rowid, iCol, detailCode);					
					
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
	<fieldset style="display: inline;">
	    <legend>수주 가능한 견적 검색조건 </legend>
    		<label for="searchByDateRadio">일자 검색</label>
    		<input type="radio" name="searchDateCondition" value="searchByDate" id="searchByDateRadio">
    		<label for="searchByPeriodRadio">기간 검색</label>
    		<input type="radio" name="searchDateCondition" value="searchByPeriod" id="searchByPeriodRadio">
	</fieldset>
	
	<input type="text" value="시작일" id="startDatePicker" />
	<input type="text" value="종료일" id="endDatePicker" />
	<input type="button" value="수주가능견적조회" id="contractCandidateSearchButton" />
	
	<fieldset style="display: inline;">
	    <legend>수주 등록 / 견적 취소</legend>
		 	<input type="button" value="수주등록" id="contractBatchInsertButton" />
  			<input type="button" value="견적취소" id="estimateCancleButton" />
 	</fieldset>
 
<table id="contractCandidateGrid" ></table>
<div id="contractCandidateGridPager"></div>

<table id="estimateDetailGrid" ></table>
<div id="estimateDetailGridPager"></div>

<div id="codeDialog">
	<table id="codeGrid"></table>
</div>

</body>
</html>