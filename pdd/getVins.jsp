<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"  isELIgnored="false"%>

<!DOCTYPE>
<html>
	<head>
		<meta charset='UTF-8'>

		<title>Vin List</title>
		<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
		<script src='<%=request.getContextPath()%>/resources/js/vin.js'></script>
	</head>

	<body>

		<h2>Vin List</h2>
		<table border="1">
			<tr>
				<td>车辆识别码</td>
				<td>车型</td>
				<td>里程数</td>
			</tr>
			<c:forEach var="vin" items="${requestScope.vins}" varStatus="status">
				<tr class="vin_item">
					<td><a class="vin_detail_vin" href="${vin}/">${vin}</a></td>
					<td><input class="vin_detail_car_type" type="text" value="${requestScope.details[status.index].car_type}" /></td>
					<td><input class="vin_detail_miles" type="text" value="${requestScope.details[status.index].miles}" /></td>
				</tr>
			</c:forEach>
		</table>
		<input id="vin_detail_save" type="button" value="保存"/>
	</body>
</html>