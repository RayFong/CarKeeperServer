<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"  isELIgnored="false"%>

<!DOCTYPE>
<html>
	<head>
		<meta charset='UTF-8'>
		<meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>

        <title>Vin List</title>
        <script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
        <script src="<%=request.getContextPath()%>/resources/js/login.js"></script>
	</head>

	<body>

		<h2>Login</h2>
		<form id="login_form">
			<table border="0">
				<tr>
					<td>用户名：</td>
					<td colspan="10">
						<input id="username" name="username" type="text" />
					</td>
				</tr>
				<tr>
					<td>密码：</td>
					<td>
						<input id="passwd" name="passwd" type="password" />
					</td>
				</tr>
				<tr style="text-align:center">
					<td>
						<input id="login_submit" type="submit" title="提交" />
					</td>
					<td>
						<input type="reset" title="重置" />
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>