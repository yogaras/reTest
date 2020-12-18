<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SHRich</title>  
<script>
	$(document).ready(function() {
	$("input[type=button], input[type=submit], input[type=reset]").button(); 

	});
	</script>
	


<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.css" /> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.structure.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.theme.min.css" /> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid-bootstrap-ui.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid-bootstrap.css" /> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid-bootstrap4.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css" /> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jq.css"/>
<script src="${pageContext.request.contextPath}/scripts/jquery/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.js"></script>


<!-- 버튼ui -->
<script src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery.ui.button.min.js"></script>



<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery.jqGrid.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-kr.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-en.js"></script>


<decorator:head />
<style>
body {
    background-image:url("${pageContext.request.contextPath}/scripts/images/univer.jpg");
    background-size:cover;
    background-attachment:scroll;
	margin: 0 auto;
}

#topmenu {
	margin: 5px 5px 5px 5px;
	padding: 5px 5px 5px 5px;
	height: 20%;
	min-width: 95%;
}

#wrap {
	height: 100%;
	min-width: 100%;
}

#contents {
	width: 100%;
	height: 100%;
	float: left;
}

#section {
	margin: 5px 5px 5px 5px;
	padding: 5px 5px 5px 5px;
	width: 95%;
	float: center;
	min-width: 80%;
}

#bottom {
	text-align: center;
	min-width: 80%;
}

</style>
</head>
<body>
	<div id="topmenu">
		<jsp:include page="top.jsp" />
	</div>
	<div id="wrap" style="z-index: -1">
		<div id="contents">
			<div id="section">
				<decorator:body />
			</div>
		</div>
	</div>
	<div id="bottom">
		<jsp:include page="bottom.jsp" />
	</div>
</body>
</html>