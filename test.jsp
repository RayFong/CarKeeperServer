<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/main.css" media="all" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/xcharts.css" media="all" />

		<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/d3.v3.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/xcharts.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/test.js"></script>
	</head>
	<body>
		<div class="axis">
			<figure style="height: 300px;" id="myChart" title="123"></figure>
		</div>
		<a id="h2t" href="#">http://www.jquery001.com</a>
		<a href="javascript:void(0);" onclick="javascript:Foo();">test</a>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			This content will only be visible to users who have
			the "supervisor" authority in their list of <tt>GrantedAuthority</tt>s.
		</sec:authorize>
	</body>
</html>