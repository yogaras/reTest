<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CODE</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/loginform_util.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/loginform_main.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/loginform_modified.css" />
<script src="${pageContext.request.contextPath}/scripts/jquery/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery.jqGrid.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/codeModal.js"></script>
<script>

	var captionName;
   $(document)
         .ready(
        		 
        		 function() {
                     if ("${param.code}" == "companyCode") { //회사코드
                    	 compony();
                     } else if("${param.code}" == "workplaceCode"){ //사업장코드
                    	 workplace();
                     } else if("${param.code}" == "customerName" || "${param.code}" == "searchCustomerBox"){
                    	 captionName = "거래처 코드 검색";	
                    	 customerCode(captionName);
                     } else if("${param.code}" == "itemCode" || "${param.code}" == "itemName" || "${param.code}" == "itemCodeSearchBox" || "${param.code}"=="parentItemCodeSearchBox"){
                    	 captionName = "아이템 코드 검색";
                    	 customerCode(captionName);
                     } else if("${param.code}" == "unitOfEstimate"){
                    	 captionName = "단위 코드 검색";
                    	 customerCode(captionName);              
                     } else if("${param.code}" == "contractType" ){
                    	 captionName = "수주 유형 코드 검색";
                    	 customerCode(captionName);      
                     } else if("${param.code}" == "productionProcess"){
                    	 captionName = "생산 공정 코드 검색";
                    	 customerCode(captionName);
                     } 
        		 function compony() {
        			 $('#grid')
                     .jqGrid(
                           {
                              url : '${pageContext.request.contextPath}/basicInfo/searchCompany.do',
                              postData : {
                            	  
                                 method : "searchCompanyList"
                              },
                              datatype : 'json',
                              jsonReader : {
                                 root : 'gridRowJson'
                              },
                              colNames : [  "회사코드", " 회사명", " 회사구분", " 사업자번호"  ],
                              colModel : [ {
  								name : "companyCode",
  								width : "90",
  								resizable : true,
  								align : "center"
  							}, {
  								name : "companyName",
  								width : "200",
  								resizable : true,
  								align : "center"
  							}, {
  								name : "companyDivision",
  								width : "90",
  								resizable : true,
  								align : "center"
  							}, {
  								name : "businessLicenseNumber",
  								width : "120",
  								resizable : true,
  								align : "center"
  							}, ],  							  
  							  caption : '회사코드 검색', //그리드 상단제목
							  sortname : 'companyCode', //정렬 대상
							  height : 400,
							  width : 580,						  
							  responsive: true,							                       
							  cache : false,
							  onSelectRow : function(rowId) {
                                  if ("${param.code}" != "") {
                                     var codeNumber = 
                                        $("#grid").getCell(rowId,"companyCode"); //rowId에 해당하는 cell데이터 가져옴
                                     $("#${param.code}",opener.document).val(codeNumber);
                                  }

                                  window.close();
                               }
                     
        				 });
                     }
        		 function workplace() {
        			 $('#grid')
                     .jqGrid(
                           {
                              url : '${pageContext.request.contextPath}/basicInfo/searchWorkplace.do',
                              postData : {
                            	 companyCode : "COM-01",
                                 method : "searchWorkplaceList"
                              },
                              datatype : 'json',
                              jsonReader : {
                                 root : 'gridRowJson'
                              },
                              colNames : [  "회사코드", " 사업장코드",
									" 사업장명", " 사업장번호"  ],
                              colModel : [ {
									name : "companyCode",
									width : "90",
									resizable : true,
									align : "center"
								}, {
									name : "workplaceCode",
									width : "110",
									resizable : true,
									align : "center"
								}, {
									name : "workplaceName",
									width : "200",
									resizable : true,
									align : "center"
								}, {
									name : "businessLicenseNumber",
									width : "110",
									resizable : true,
									align : "center"
								} ],  							  
								caption : '사업장코드 검색',
								sortname : 'workplaceCode',
							    height : 400,
							    width : 580,	  
							    responsive: true,     
							  cache : false,								
							  onSelectRow : function(rowId) {
                                  if ("${param.code}" != "") {
                                     var codeNumber = 
                                        $("#grid").getCell(rowId,"workplaceCode"); //rowId에 해당하는 cell데이터 가져옴
                                     $("#${param.code}",opener.document).val(codeNumber);
                                  }
                                  window.close();
                               }
                     
        				 });
                     }
        		 function customerCode() {
        			 $("#grid")
        			 .jqGrid(
        					{
        			        url : "${pageContext.request.contextPath}/base/codeList.do",
        			        datatype : "json",
        			        jsonReader : { root: "detailCodeList" },
        			        postData : { 
        			    		method : "findDetailCodeList" ,
        			    		divisionCode : "${param.divisionCode}" //CL-01
        			    	},
        					colNames : [ '상세코드번호' , '상세코드이름' , '사용여부' ],
        					colModel : [
        						{ name : 'detailCode', width:100, align : "center",editable:false},
        						{ name : 'detailCodeName', width:100, align : "center", editable:false},
        						{ name : 'codeUseCheck', width:100, align : "center",editable:false},
        					],
        					caption : captionName,	        					
						    height : 400,
						    width : 580,	  
						    responsive: true,     
						 	cache : false,
						 	onSelectRow : function(rowId) {		
						 		var codeUseCheck= $("#grid").getCell(rowId,"codeUseCheck");
						 		var id=$("#estimateGrid",opener.document).getGridParam('selrow');
						 		var detailId=$("#estimateDetailGrid",opener.document).getGridParam('selrow');
						      if(codeUseCheck != 'N' && codeUseCheck != 'n'){
                              if ("${param.code}" == "customerName") { //거래처코드                        	  
                                 var detailName = 
                                   $("#grid").getCell(rowId,"detailCodeName");                                 
                                   $("#estimateGrid",opener.document).setCell(id,'customerName',detailName);	
                                   
                                 var detailCode = 
                                   $("#grid").getCell(rowId,"detailCode");
                                   $("#estimateGrid",opener.document).setCell(id,'customerCode',detailCode);  
                                 
                              }else if("${param.code}" == "searchCustomerBox"){ //거래처코드
                                 var detailName = 
                                   $("#grid").getCell(rowId,"detailCodeName");
                                   $("#${param.code}",opener.document).val(detailName);
                                      
                                 var detailCode = 
                                   $("#grid").getCell(rowId,"detailCode");	  
                                   $("#customerCodeBox",opener.document).val(detailCode);
                                 }else if("${param.code}" == "itemCode" || "${param.code}" == "itemName"){ //아이템코드
                            	  var detailCode = 
                                   $("#grid").getCell(rowId,"detailCode"); 
                            	  
                                   var detailName = 
                                   $("#grid").getCell(rowId,"detailCodeName");
                                   
                                  var ids = $("#estimateDetailGrid",opener.document).getRowData();
                             	  var errorStatus = false;
                                   $(ids).each(function(index, obj) {
                                      var itemCodeInList = obj.itemCode;
                                      if(detailCode == itemCodeInList) {
                                         alert("견적 상세에 이미 있는 품목입니다");
                                         errorStatus = true;
                                         return false;
                                      } 
                                   }) 
                              if(!errorStatus) {                                	  
                                      $("#estimateDetailGrid",opener.document).setCell(detailId, "itemCode", detailCode);               
                                      $("#estimateDetailGrid",opener.document).setCell(detailId, "itemName", detailName);                              
                                	  }
                                   
                              }else if("${param.code}" == "unitOfEstimate"){  //분류코드                           	
                            	  var detailCode = 
                                  $("#grid").getCell(rowId,"detailCode");                                     
                            	  $("#estimateDetailGrid",opener.document).setCell(detailId,"unitOfEstimate", detailCode);                             	
                              }else if("${param.code}" == "contractType" ){ //수주유형코드
                            	id=$("#contractCandidateGrid",opener.document).getGridParam('selrow');
                        	   var detailCode = 
                                   $("#grid").getCell(rowId,"detailCode");
                                   $("#contractCandidateGrid",opener.document).setCell(id,"contractType",detailCode);
                           	  }else if("${param.code}" == "productionProcess"){ //생산공정코드
                           		var detailName = 
                                    $("#grid").getCell(rowId,"detailCodeName");
                                    $("#${param.code}",opener.document).val(detailName);
                                       
                                  var detailCode = 
                                    $("#grid").getCell(rowId,"detailCode");	  
                                    $("#productionProcessCode",opener.document).val(detailCode);
                           	  }else if("${param.code}" == "itemCodeSearchBox" || "${param.code}" == "parentItemCodeSearchBox"){
                           		var detailCode = 			//BOM 아이템코드
                                    $("#grid").getCell(rowId,"detailCode");	  
                                    $("#${param.code}",opener.document).val(detailCode);
                           	  }                              
                              window.close();
                              $("#${param.code}",opener.document).trigger('reloadGrid');
                          	  }
                        	  else 
                              {
				               alert("사용자 에러 : 사용 가능한 코드가 아닙니다");    
				               window.close();
				           }
						 }
        			 });
        		 }
        	});
</script>
</head>
<body>
   <div>
      <table id="grid"></table>
   </div>
</body>
</html>