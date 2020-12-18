<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>에러 페이지</title>
<style>
</style>
</head>
<body>
에러 페이지

<script>


	$(document).ready(function() {
		
		if ("${requestScope.errorCode}" < 0) {

			alertError("${requestScope.errorTitle}", "${requestScope.errorMsg}");
			
		} else {
			
			$("#error-dialog").attr("style", "display:none");
		}
		
		
	});

</script>
</body>
</html>