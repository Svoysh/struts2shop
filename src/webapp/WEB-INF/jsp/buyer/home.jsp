<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Low: Change file name to "index.jsp". --%>
<html>
	<head>
		<%-- 
			TODO: "panel" sunds little stupid. Maybe "sector" or "section"?... 
			A word must means "the part of the site".
		--%>
		<title>Buyer panel</title>
	</head>
	<body>
		<h2>Orders</h2>
		<ul>
			<li><a href="<%= request.getContextPath() %>/buyer/order!list">Your orders</a></li>
			<li><a href="<%= request.getContextPath() %>/buyer/order!items">TODO: Your ordered products</a></li>
		</ul>
	</body>
</html>