<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>견적 등록</title>
<style>
#newEstimateDate, #estimateAmountBox, #unitPriceOfEstimateBox,
   #sumPriceOfEstimateDiv {
   display: inline;
   width: 115px;
   margin-bottom: 10px;
   transition: 0.6s;
   outline: none;
   height: 30px;
   font-size: 20px;
   text-align: center;
}
.l1 {
   color: #FFFFFF;
}
legend {
   color: #FFFFFF;
}
.ui-datepicker {
   z-index: 9999 !important;
}

.ui-dialog {
   z-index: 9999 !important;
   font-size: 12px;
}
</style>
<script>

function showModalCode(codeName,divisionCode){
    var option="width=550; height=430; left=500; top=200; titlebar=no; toolbar=no,status=no,menubar=no,resizable=yes, location=no";
    window.open("${pageContext.request.contextPath}/basicInfo/codeModal.html?code="+codeName+"&divisionCode="+divisionCode,"newwins",option);

}
var lastSelected_estimateDetailGrid_Id;    // 가장 나중에 선택한 견적상세 grid 의 행 id 
var lastSelected_estimateDetailGrid_RowValue;   // 가장 나중에 선택한 견적상세 grid 의 행 값 

var statusForInsertWorking = false;  // 현재 작업중인 견적 존재 여부

var standardUnitPrice; //
var estimateDate;   // 작업중인 견적에서 유효일자 값

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

   $("#newEstimateDate").datepicker({
      changeMonth : true,//월 바꾸기 가능
      numberOfMonths : 1,//한번에 보여지는 개월 수
      maxDate : new Date() //최대날짜는 오늘까지만
   });      
   

   $("#codeDialog").hide();

   $("#InputDialog").hide();
   
   initGrid();
   initEvent();
   
});

function initGrid() {
   
   // 견적 그리드 시작
   $('#estimateGrid').jqGrid({ 
      mtype : 'POST', //요청 메소드 정의
      datatype : 'local', //그리드를 채우는 데이터형식 local=array data
      colNames : [ "거래처명","거래처코드", "견적일자", "유효일자","견적담당자","견적담당자코드", "견적요청자", "비고", "status" ], //그리드 Header에 보여질 텍스트
      colModel : [ //그리드 body에 보여질 데이터의 속성
         { name: "customerName", width: "100", resizable: true, align: "center"} ,
         { name: "customerCode", width: "100", resizable: true, align: "center", hidden: true},//거래처코드 속성 hidden
         { name: "estimateDate", width: "100", resizable: true, align: "center" } ,
         { name: "effectiveDate", width: "100", resizable: true, align: "center", editable: true,
              formatter: 'date', //srcformat:반환해야 하는 날짜의 형식, newformat :새로운 날짜의 형식
              formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d', defaultValue:null },
              edittype: 'text', //폼 편집에서 사용할 유형
                editoptions: { size: 12, maxlength: 12, 
                  dataInit: function (element) {    //이 함수가 정의되면 해당 element object가 함수에 전달된다.            
                     var maxdate = new Date($('#newEstimateDate').val()); //Date객체 새로 만들어 안에 견적일자 담고
                     var ddate = maxdate.getDate()+14; //그 객체+14일 만들기
                     maxdate.setDate(ddate); // Date객체에 setDate으로 +14일 한것을 세팅                     
                     $(element).datepicker({ 
                        minDate : $('#newEstimateDate').val(),
                        maxDate : maxdate, // 일케하면 최소일자에서 더한 숫자만큼의 일수가 maxDate으로 설정
                        changeMonth: true, 
                        numberOfMonths: 1,
                        onClose: function(dateText, datepicker) {
                           //dateText=날짜 스트링, datepicker=달력
                           $('#estimateGrid').editCell(1, 3 ,false);   // iRow,iCol, true/false
                                                                           // $(this) .. => 안됨, 여기서 this 는 inputField 를 의미
                        }
                        })}
                }, 
                editrules: { date: true } // 서버에 값을 전송하기전에 validate 용으로 사용됨
         } ,
         { name: "personNameCharge", width: "100", resizable: true, align: "center"} ,
         { name: "personCodeInCharge", width: "100", resizable: true, align: "center", hidden: true} ,
         { name: "estimateRequester", width: "100", resizable: true, align: "center", editable: true } ,
         { name: "description", width: "150", resizable: true, align: "center", editable: true } ,
         { name: "status", width: "100", align: "center", editable: false}
         
      ], 
      caption : '견적', // 그리드 상단의 제목
      sortname : 'estimateNo', //서버에서 처음 로드될 때 데이터가 정렬되는 컬럼 
      multiselect : false, //grid 상단 column 영역에 checkbox가 생성
      multiboxonly : false, //multiselect옵션이 true일때 작동한다.multiboxonly옵션이 true인경우 checkbox를 클릭해야만 다중선택이 된다.
      viewrecords : true, //총 레코드 갯수 중에서 시작/끝 레코드 번호를 표시
      rownumWidth : 30, //row number 컬럼의 width
      height : 50, //   그리드의 높이
      width : 900, //그리드의 넓이
      autowidth : true, //그리드의 상위 Element의 width로 계산됨. 그리드가 생성될때 처음에만 수행
      shrinkToFit : false, //그리드의 width를 고려하여 컬럼의 width를 어떻게 재계산할지 정의한다.   이 값이 true이고 컬럼의 width도 설정된경우 모든 컬럼은 width의 비율만큼 조정된다.   
      cellEdit : true, //셀의 에디트 가능여부 결정
      rowNum : 50,   //    한페이지에 보여질 데이터(레코드) 갯수, -1 : 모든 로우 한번에 표시, 그런데 잘 안먹히는 경우 많음
      scrollerbar: true,
      //rowList : [ 10, 20, 30 ],
      editurl : 'clientArray', //'clientArray'로 설정하면 데이터는 서버에 요청을 보내지 않고 이벤트를 통한 처리를 위해 그리드에만 저장된다.
      cellsubmit : 'clientArray', //셀의 컨텐츠가 저장될 위치를 지정한다.
      rownumbers : true, //true 설정 시 그리드의 왼쪽에 새로운 컬럼이 추가된다.이 컬럼의 목적은 1부터 시작하는 사용가능한 row의 숫자를 카운트      
      autoencode : true, //서버에서 가져온 데이터 인코딩 여부
      resizable : true, //컬럼의 resize 여부
      loadtext : '로딩중...', //데이터 로딩중 보여줄 문자열
      emptyrecords : '데이터가 없습니다.',  //현재 또는 그리드에서 반환된 레코드 수가 0일 때 보여질 문자열.
      cache : false, 
      
      onCellSelect : function(rowid, iCol, cellcontent, e) {
         //견적 그리드 선택한 셀이 1번부터 숫자값으로 부여됨 icol에 부여됨.
         //rowid=선택한 셀의 행 번호, iCol=선택한 셀의 열 번호, cellcontent=선택한 셀의 값, e=클릭하는 이벤트 객체요소(?)
            if(iCol == 1) {  // 거래처 cell 클릭  // 거래처 코드칼럼이 첫번쨰이니까 iCol==1 참이된다
            //console.log(this, "estimateGrid", rowid , iCol , "CL-01","거래처 코드 검색");
               var colModel = $(this).jqGrid('getGridParam', 'colModel');
               var colName = colModel[iCol].name;   
               
               showModalCode(colName,"CL-01");
              
             //showCodeDialog(this, "estimateGrid", rowid , iCol , "CL-01","거래처 코드 검색");
            //showCodeDialog 호출


            } 

      },

   }); // 견적 그리드 끝
   

   // 견적 상세 그리드 시작
   $('#estimateDetailGrid').jqGrid({ 
      mtype : 'POST', 
      datatype : 'local',
      colNames : [ "품목코드", "품목명", "단위", "납기일", "견적수량", "견적단가", "합계액", "비고", "status", "beforeStatus"], 
      colModel : [
         { name: "itemCode", width: "100", resizable: true, align: "center"} ,
         { name: "itemName", width: "150", resizable: true, align: "center"} ,
         { name: "unitOfEstimate", width: "100", resizable: true, align: "center"} ,
         { name: "dueDateOfEstimate", width: "100", resizable: true, align: "center", editable: true,
//              formatter: 'date', 
//              formatoptions: { srcformat: 'ISO8601Long', newformat: 'Y-m-d' },
              edittype: 'text', 
                editoptions: { size: 12, maxlengh: 12, 
                  dataInit: function (element) { 
                     var mindate = new Date($('#newEstimateDate').val()); //Date객체 새로 만들어 안에 견적일자 담고
                     var mdate = mindate.getDate()+9; //그 객체+9일 만들기
                     mindate.setDate(mdate); // Date객체에 setDate으로 +9일 한것을 세팅
                     
                     var maxdate = new Date($('#newEstimateDate').val()); //Date객체 새로 만들어 안에 견적일자 담고
                     var mxdate = maxdate.getDate()+19; //그 객체+9일 만들기
                     maxdate.setDate(mxdate); // Date객체에 setDate으로 +9일 한것을 세팅   
                     $(element).datepicker({ 
                        minDate : mindate ,
                        maxDate : maxdate,
                        changeMonth: true, 
                        numberOfMonths: 1, 
                        onClose: function(dateText, datepicker) {
                           
                           $('#estimateDetailGrid').editCell(1, 5 ,false);   // iRow,iCol, true/false
                                                                                 // $(this) .. => 안됨, 여기서 this 는 inputField 를 의미
                           
                           $(this).editCell(lastSelected_estimateDetailGrid_Id, 4 , false);     // iRow,iCol, true/false
                        }
                        })}
                }, 
                editrules: { date: true } 
         } ,
         { name: "estimateAmount", width: "100", resizable: true, align: "center",
            formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
           } ,
         { name: "unitPriceOfEstimate", width: "100", resizable: true, align: "center",
            formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
           } ,
         { name: "sumPriceOfEstimate", width: "100", resizable: true, align: "center", 
                 formatter:'integer',formatoptions: { defaultValue: '0', thousandsSeparator: ',' }
           } ,    
         { name: "description", width: "100", resizable: true, align: "center", editable: true } ,
         { name: "status", width: "80", resizable: true, align: "center" } ,
         { name: "beforeStatus", width: "10", resizable: true, align: "center" , hidden: true } 
      ], 
      caption : '견적상세', 
      sortname : 'estimateDetailNo', 
      multiselect : true, 
      multiboxonly : false,
      viewrecords : true, 
      rownumWidth : 30, 
      height : 100, 
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
      cache : false,
       
      beforeEditCell(rowid, cellname, value, iRow, iCol){
         if( lastSelected_estimateDetailGrid_Id != rowid ){
            lastSelected_estimateDetailGrid_Id = rowid;
            lastSelected_estimateDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
         }
         
           if(value == null || value == "" ) {
              previousCellValue = null;    // "" 이면 이전 값으로 돌리는 경우 setCell 함수가 안먹힘
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
      
      onSelectRow : function(rowid) {
         
         if( lastSelected_estimateDetailGrid_Id != rowid ){
            lastSelected_estimateDetailGrid_Id = rowid;
            lastSelected_estimateDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
         }
         
      },
      
      onCellSelect : function(rowid, iCol, cellcontent, e) {
         var unit = $(this).getCell(rowid,"unitOfEstimate");
         if( lastSelected_estimateDetailGrid_Id != rowid ){
            lastSelected_estimateDetailGrid_Id = rowid;
            lastSelected_estimateDetailGrid_RowValue = $(this).jqGrid('getRowData', rowid); 
         }

         var status = $(this).getCell(rowid,"status");

            if(iCol == 2 || iCol == 3) {  // 품목코드 또는 품목명 cell 클릭
               
                var colModel = $(this).jqGrid('getGridParam', 'colModel');
                var colName = colModel[iCol].name; 
                showModalCode(colName,"IT-_I");
/*                
               showCodeDialog(this , "estimateDetailGrid",  rowid , iCol , "IT-_I","완제품 및 반제품 검색!!"); */
                  
            } else if (iCol == 4) {  // 단위 cell 클릭
                var colModel = $(this).jqGrid('getGridParam', 'colModel');
                var colName = colModel[iCol].name;                
               showModalCode(colName,"UT");
               
               /* 
               showCodeDialog(this ,"estimateDetailGrid",  rowid , iCol , "UT","단위 검색");
              */
            }
         
            else if( iCol == 6 || iCol == 7 || iCol == 8 ) {
               
               if(unit=="BOX"){ //onCellSelect 펑션 이벤트에 unit변수에 그리드 단위 cell의 값을 할당.
                           //클릭질 할때마다 발생하므로 단위를 바꾸더라도 ㅇㅋ;
                   $.ajax({
                       type : 'POST',
                       url : '${pageContext.request.contextPath}/logisticsInfo/getStandardUnitPrice.do' ,
                       data : {
                          method : 'getStandardUnitPriceBox',
                          itemCode : $(this).getCell(rowid,"itemCode")
                       },
                       dataType : 'json',
                       cache : false,
                       success : function(dataSet) {
                          
                          $('#unitPriceOfEstimateBox').val(dataSet.gridRowJson);
                          
                       }
                    });
                }
            
                else{
               $.ajax({
                  type : 'POST',
                  url : '${pageContext.request.contextPath}/logisticsInfo/getStandardUnitPrice.do' ,
                  data : {
                     method : 'getStandardUnitPrice',
                     itemCode : $(this).getCell(rowid,"itemCode")   //itemCode 는 품목코드 이다 
                  },
                  dataType : 'json',
                  cache : false,
                  success : function(dataSet) {
                     console.log(dataSet)
                     $('#unitPriceOfEstimateBox').val(dataSet.gridRowJson);
                     
                  }
               }); // ajax 끝   
                }  
               
               showInputDialog(this ,rowid);
               
            }
      }
   }); // 견적 상세 그리드 끝
}

function initEvent() {


   $('#estimateInsertButton').on("click", function() { //견적추가 버튼 클릭이벤트
      
      if( statusForInsertWorking != true && $('#newEstimateDate').val() != '' ) { 
         //statusForInsertWorking는 40줄에 false로 되어있다.
         //따라서 statusForInsertWorking가 false이고 견적일자가 비어있지 않을경우 이벤트 발생
         var nextRecordNum = <%--Number($("#estimateGrid").getGridParam("records"))+--%> 1; //견적추가를 했을때 견적테이블에 들어오는 행의 갯수
         //nextRecordNum 변수에 Number로 견적그리드의 실제레코드 수를 리턴받는다. 이벤트 발생이 레코드수는 0이므로 +1을 하면 변수에는 1이 담기게 된다.
         $('#estimateGrid').jqGrid('addRowData' , 1, //<=바로 위의 변수.
                           //addRowData행 만들기, 위 변수는 현재 그리드의 레코드수이다. 거기에 +1을 한것이므로 한 행을 추가하여 거기에 addRowData
            { "estimateDate": $('#newEstimateDate').val() , 
               "personCodeInCharge" : "${sessionScope.empCode}",   // 담당자 코드에 현재 접속중인 사원의 사원코드 입력
               "personNameCharge" : "${sessionScope.empName}",
               "status":"INSERT" }  );
         
         statusForInsertWorking = true ;// 현재 견적이 작업중으로 존재함 이됨
         
      } else if( $('#newEstimateDate').val() == '' ){
         alertError("사용자 에러","견적 일자를 입력하지 않았습니다");
      } else {
         alertError("사용자 에러","작업중인 견적이 있습니다");
      }
   });

   
   $('#estimateDetailInsertButton').on("click",function() { //견적상세추가 이벤트
      
      
      // 현재 작업중인 견적 그리드의 행 값
      var estimateValue = $('#estimateGrid').jqGrid('getRowData', 1);//견적그리드 1번행의 데이터를 가져옴
      if( statusForInsertWorking == false ) {
         alertError("사용자 에러","새로운 견적을 먼저 등록하세요");
      } else if(estimateValue.customerCode == "") {
         alertError("사용자 에러","거래처 코드를 먼저 등록하세요");
      } else if(estimateValue.effectiveDate == "") {
         alertError("사용자 에러","견적 유효일자를 먼저 입력하세요");
      }                                          //getDataIDs() 데이터의 ID를 가져오는 메소드, 즉 .length는 데이터의 길이(갯수)
       else {                                             //처음엔 추가 된 행이 없으니 값이 0, 그래서 +1
         var newRowNum = $('#estimateDetailGrid').jqGrid('getDataIDs').length+1;//견적상세누를떄마다 추가되는 행
         //alert(newRowNum); 
         //alert($('#estimateDetailGrid').jqGrid('getDataIDs'));  
         $('#estimateDetailGrid').addRowData(
               newRowNum,{ "status":"INSERT" });
      }
   });
   
   
   $('#estimateDetailDeleteButton').on("click",function() {
      
      var checkedDeleteList = $('#estimateDetailGrid').jqGrid('getGridParam','selarrrow'); 

      $(checkedDeleteList).each( function(index, chekedRowId) {
         var rowObject = $('#estimateDetailGrid').getRowData(chekedRowId); // 행의 row 값 정보 객체
           $("#estimateDetailGrid").delRowData(chekedRowId);
      });

   });
   
   
   $('#batchSaveButton').on("click",function() {
      
      var checkedDeleteList = $('#estimateDetailGrid').jqGrid('getGridParam','selarrrow'); 
                                                //selarrrow,muliselect 옵션을 true로 주고 선택 된 row를 배열로 가져옴 
      $(checkedDeleteList).each( function(index, chekedRowId) {

         $("#estimateDetailGrid").delRowData(chekedRowId); //행을삭제 , 데이터는 삭제되지 않습니다.

      });
      
      
      if(Number($("#estimateDetailGrid").getGridParam("records")) == 0) {
         alertError("사용자 에러", "견적 상세가 입력되지 않았습니다");
         return;
      }                    
      
      
      var rowIdList =  $('#estimateDetailGrid').jqGrid('getDataIDs');// 전체 rowid가져오기(배열로)
      
      // 사용자 유효성 검사 
      $(rowIdList).each( function(index, rowId) {
         var rowObject = $('#estimateDetailGrid').getRowData(rowId); // 행의 row 값 정보 객체

         if( rowObject.itemCode == "" ) {
            alertError("사용자 에러", "품목코드를 입력하지 않은 행이 있습니다 </br> 저장 목록에서 제외합니다");   return;            
         } else if(rowObject.unitOfEstimate == "" ) {
            alertError("사용자 에러", "단위를 입력하지 않은 행이 있습니다 </br> 저장 목록에서 제외합니다");
         } else if(rowObject.dueDateOfEstimate == ""){
            alertError("사용자 에러", "납기일을 입력하지 않은 행이 있습니다 </br> 저장 목록에서 제외합니다");
         } else if(rowObject.estimateAmount == "0" || rowObject.unitPriceOfEstimate == "0") {
            alertError("사용자 에러", "견적수량/견적단가를 입력하지 않은 행이 있습니다 </br> 저장 목록에서 제외합니다");
         } else {
            resultList.push(rowObject);   
         }
         
      });
   
      var newEstimateRowValue = $('#estimateGrid').getRowData(1); //견적그리드 첫행을 변수에 담음
      var newEstimateDetaillist = $('#estimateDetailGrid').getRowData(); //견적상세를 변수에 담음

      // 견적 Bean 이 될 견적정보 JS 객체에 estimateDetailBeanList 멤버속성 지정   
      newEstimateRowValue.estimateDetailTOList = newEstimateDetaillist;
      //견적 변수의 estimateDetailTOList에 견적상세를 담은 newEstimateDetaillist 넣음
      // newEstimateRowValue : { ...  estimateDetailTOList : [{1},{2},{3}] }
      alert(JSON.stringify(newEstimateRowValue));
      var confirmMsg = "견적일자 " + $('#newEstimateDate').val() 
         + " , 견적상세 " + Number($("#estimateDetailGrid").getGridParam("records")) 
         + "건을 추가합니다. \r\n계속하시겠습니까?" ; 
      
      if( confirm(confirmMsg) ) {
         console.log($('#estimateGrid').getRowData(1))
         console.log($('#estimateDetailGrid').getRowData())
         console.log(newEstimateRowValue)
         $.ajax({ 
            type : 'POST',
            url : '${pageContext.request.contextPath}/sales/addNewEstimate.do' ,
            async :false,
            data : {
               method : 'addNewEstimate', 
               estimateDate : $('#newEstimateDate').val() , //견적일자
               newEstimateInfo : JSON.stringify(newEstimateRowValue) // 견적과 견적상세가 담긴 배열
            },
            dataType : 'json', 
            cache : false, 
            success : function(dataSet) {
               
               if(dataSet.errorCode < 0){
                  
                  alertError('실패',dataSet.errorMsg);
                  return;
                  
               }
               
               var resultMsg = 
                  "< 견적 등록 내역 >   <br/><br/>"
                  + "새로운 견적번호 : " + dataSet.result.newEstimateNo + "</br></br>"
                  + "견적상세번호 : " + dataSet.result.INSERT  + "</br></br>"
                  + "위와 같이 작업이 처리되었습니다";
                  
               alertError("성공", resultMsg);
               
               statusForInsertWorking = false;
               $('#estimateGrid').jqGrid('clearGridData');  // 견적그리드 데이터 비우기
               $('#estimateDetailGrid').jqGrid('clearGridData');  // 견적상세그리드 데이터 비우기
               
            }
         });     
         
      }

   });
   
   $('#estimateAmountBox, #unitPriceOfEstimateBox').on("keyup", function() {
      var sum = $('#estimateAmountBox').val() * $('#unitPriceOfEstimateBox').val();
      
      $('#sumPriceOfEstimateDiv').text(sum);
   }
   );
   
}



<%--  function showCodeDialog(grid, gridName, rowid, iCol, divisionCodeNo, title){
               //this, "estimateGrid", rowid , iCol , "CL-01","거래처 코드 검색
   $("#codeDialog").dialog({
      title : '코드 검색',
      width : 500,
      height : 500,
      modal : true   // 폼 외부 클릭 못하게
   });
   
   $.jgrid.gridUnload("#codeGrid");//gridUnload()는 JQgrid 의 내용을 초기화(날려버릴때) 사용
   
   $.ajax({ 
      type : 'POST',
      url : "${pageContext.request.contextPath}/base/codeList.do" ,
      async :false,
      data : {
          method: "findDetailCodeList" ,
          divisionCode: divisionCodeNo   
      },
      dataType : 'json', 
      cache : false, 
      success : function(dataSet) {
         console.log(dataSet);
         
      }
   });
   
   $("#codeGrid").jqGrid({
        url : "${pageContext.request.contextPath}/base/codeList.do",
        datatype : "json",
        jsonReader : { root: "detailCodeList" },
        postData : { 
          method : "findDetailCodeList" ,
          divisionCode : divisionCodeNo //CL-01
       },
      colNames : [ '상세코드번호' , '상세코드이름' , '사용여부' ],
      colModel : [
         { name : 'detailCode', width:100, align : "center",editable:false},
         { name : 'detailCodeName', width:100, align : "center", editable:false},
         { name : 'codeUseCheck', width:100, align : "center",editable:false},
      ],
      width : 450,
      height : 300,
      caption : title,
      align : "center",
      viewrecords : true,
      rownumbers : true,
      onSelectRow : function(id) {
            
            
            var detailCode=$("#codeGrid").getCell(id, 1);
            var detailName=$("#codeGrid").getCell(id, 2);
            var codeUseCheck=$("#codeGrid").getCell(id, 3);

            if(codeUseCheck != 'N' && codeUseCheck != 'n') {
               
               switch(gridName) {
               
                  case "estimateGrid" :
                     
                     if(iCol == 1) {  // 견적에서 사업장 코드 셀 클릭시
                        $(grid).setCell(rowid, iCol, detailName);   //function 매개변수로 들어온 값
                        $(grid).setCell(rowid, 2, detailCode); //hidden 컬럼
                     } 

                  case "estimateDetailGrid" : 
                     
                     if(iCol == 2 || iCol == 3) {  // 견적 상세에서 품목코드, 품목명 셀 클릭시
                        
                        var ids = $(grid).getRowData();
                        var errorStatus = false;
                        $(ids).each(function(index, obj) {
                           var itemCodeInList = obj.itemCode;
                           if(detailCode == itemCodeInList) {
                              alertError("사용자 에러","견적 상세에 이미 있는 품목입니다");
                              errorStatus = true;
                              return false;
                           } 
                        })
                        
                        if(!errorStatus) {
                           $(grid).setCell(rowid, 2, detailCode);               
                           $(grid).setCell(rowid, 3, detailName);   
                        }

                     } else if(iCol == 4) {
                        $(grid).setCell(rowid, iCol, detailCode);   
                     }
                        
                  default : 
               }
               
               $("#codeDialog").dialog("close");   

            } else {
               alertError("사용자 에러", "사용 가능한 코드가 아닙니다");      
            }
         }
      });
}
 --%>


function showInputDialog(grid ,rowid) {
   var estimateAmount = $(grid).getCell(rowid, "estimateAmount");//받아온 견적수량
   var sumPriceOfEstimate = $(grid).getCell(rowid, "sumPriceOfEstimate");//합계금액   
   $('#estimateAmountBox').val(estimateAmount); //다이얼로그 견적수량에 셋팅
   $('#sumPriceOfEstimateDiv').text(sumPriceOfEstimate); //다이얼로그 합계액에 셋팅
   $("#InputDialog").dialog({
      title : '견적수량 / 견적단가 입력',
      autoOpen : false,  // 자동으로 열리지 않게
      width : 250,
      height : 250,
      modal : true,   // 폼 외부 클릭 못하게
      buttons : {  // 버튼 이벤트 적용
         "확인" : function() {
            $(grid).setCell(rowid, "estimateAmount", $('#estimateAmountBox').val() );
            $(grid).setCell(rowid, "unitPriceOfEstimate", $('#unitPriceOfEstimateBox').val() );
            $(grid).setCell(rowid, "sumPriceOfEstimate", $('#sumPriceOfEstimateDiv').text() );
            $("#InputDialog").dialog("close");
         },
         "취소" : function() {
            $("#InputDialog").dialog("close");
         }
      }
   });
   $("#InputDialog").dialog("open");  // 입력 창 열기
}
</script>
</head>
<body>

   <fieldset style="display: inline; width: 240px; height: 60px">
      <legend>견적 등록 </legend>
      <label class="l1" for="newEstimateDate" style="display: inline-block; font-size: 20px; margin-right: 5px">견적일자</label>
      <input type="text" id="newEstimateDate" style="display: inline-block;" />
      <!--    id="newEstimateDate"에 newEstimateDate애 는 css maket에 있는 위잿같은애다 -->
   </fieldset>

   <input type="button" value="견적추가" id="estimateInsertButton" />
   <!--    300번 줄에서 시작함 -->
   <input type="button" value="일괄저장" id="batchSaveButton" />
   <!--    355번 줄에서 시작함 -->
   <table id="estimateGrid"></table>
   <!--    견적그리드(=견적테이블임) -->

   <input type="button" value="견적상세추가" id="estimateDetailInsertButton" />
   <!--    322번 줄에서 시작함 -->
   <input type="button" value="체크한 상세 삭제" id="estimateDetailDeleteButton" />
   <!--    343번 줄에서 시작함 -->

   <table id="estimateDetailGrid"></table>
   <!--    견적상세그리드(=견적상세테이블임) -->

   <div id="InputDialog">
      <div>
         <label 
            style="font-size: 20px; margin-right: 10px">견적수량</label> <input
            type="text" id="estimateAmountBox" /> <br />
         <!--          440번 줄에서 시작함 -->
         <label for="unitPriceOfEstimateBox"
            style="font-size: 20px; margin-right: 10px">견적단가</label> <input
            type="text" id="unitPriceOfEstimateBox" /> <br />
         <!--          440번 줄에서 시작함 -->
         <label for="unitPriceOfEstimateBox"
            style="font-size: 20px; margin-right: 10px">합계액 : </label>
         <div id="sumPriceOfEstimateDiv"></div>
         <!--          443번 줄에서 시작함 -->
      </div>
   </div>

   <div id="codeDialog">
      <table id="codeGrid"></table>
   </div>

</body>
</html>