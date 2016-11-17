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
		<meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>

		<title>Pdd Cmd Datas</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/main.css" media="all" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/pdd_data.css" media="all" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/xcharts.css" media="all" />
		
		<script src='<%=request.getContextPath()%>/resources/js/jquery.min.js'></script>
		<script src="<%=request.getContextPath()%>/resources/js/d3.v3.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/xcharts.min.js"></script>
		<script src='<%=request.getContextPath()%>/resources/js/pdd_data.js'></script>
	</head>

	<body>
		<div id="pddDataShow">
			<div id="pddDataPos">
				<div id="cmdList">
					<h2>Pdd Cmd List</h2>
					<input id="pdd_data_route_id" type="hidden" value="<c:out value="${requestScope.route_id}" />">
					<c:forEach var="cmd" items="${requestScope.cmds}" varStatus="status">
						<h5><a href="javascript:void(0);" title="<c:out value="${cmd.key}" />"><c:out value="${cmd.value}" /></a></h5>
					</c:forEach>
				</div>
				<div id="chart">
					<div id="chart_param">
						<p>
							【Start Time: <input id="route_start_time" type="text" value="0">sec.】
							&nbsp;&nbsp;&nbsp;&nbsp;
							【End Time:<input id="route_end_time" type="text" value="0">sec.】
						</p>
					</div>
					<div class="axis">
						<figure style="height: 300px;" id="myChart"></figure>
					</div>
				</div>
				<div style="clear:both;"></div>
			</div>
		</div>

	</body>
</html>