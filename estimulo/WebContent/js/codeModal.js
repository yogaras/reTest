function showCompanyCodeAjax(dataSet) {
	
	// 회사코드 ajax 시작
	$.ajax({
		type : 'POST',
		url : '${pageContext.request.contextPath}/basicInfo/searchCompany.do',
		data : {

			// MultiActionController : 여기서는 MemberLoginController 의 searchCompanyCode 메서드 호출
			method : 'searchCompanyList',
		},		
		
		dataType : 'json',
		cache : false, //저장을 하지 않겠다
		success : function(dataSet) {
			console.log(dataSet);
	
			var gridRowJson = dataSet.gridRowJson; 
		}
	}); // 회사코드 ajax 끝
}
