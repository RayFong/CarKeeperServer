<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"  isELIgnored="false"%>

<!DOCTYPE>
<html>
	<head>
		<meta charset='UTF-8'>

		<title>Vin List</title>
		<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
	</head>

	<body>

		<h2>Routes List For <c:out value="${requestScope.vin}" /></h2>
		<table border="5">
			<tr>
				<td>Id</td>
				<td>Start Time</td>
				<td>Duration(Sec.)</td>
			</tr>
		<c:forEach var="route" items="${requestScope.routes}" varStatus="status">
			<fmt:formatDate value="${route.start_time}" var="fmtDate" pattern="MM/dd/yyyy HH:mm:ss"/>
			<tr>
				<td><a href="route/${route.route_id}/"><c:out value="${status.index}" /></a></td>
				<td><c:out value="${fmtDate}" /></td>
				<td><c:out value="${route.duration}" /></td>
			</tr>
		</c:forEach>
		</table>
	</body>
</html>