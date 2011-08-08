<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Low: Change file name to "index.jsp". --%>
<html>
	<head>
		<%-- 
			TODO: The word "panel" sounds little stupid. Maybe "sector" or "section" or "zone"?... 
			A word must means "the part of the site".
			See also "/WEB-INF/jsp/buyer/home.jsp".
		--%>
		<title>Seller panel</title>
	</head>
	<body>
		<h2>Shops and Products</h2>
		<ul>
			<li><a href="<%= request.getContextPath() %>/member/shop!add">Add shop</a></li>
			<li><a href="<%= request.getContextPath() %>/member/shop!list">Shop list</a></li>
			<li><a href="<%= request.getContextPath() %>/member/product!add">Add product</a></li>
			<li><a href="<%= request.getContextPath() %>/member/product!list">Product list</a></li>
			<li><a href="<%= request.getContextPath() %>/member/category!add">Add category</a></li>
			<li><a href="<%= request.getContextPath() %>/member/category!list">Category list</a></li>
		</ul>
		<h2>Orders</h2>
		<ul>
			<li><a href="<%= request.getContextPath() %>/member/order!list">Orders placed for you</a></li>
		</ul>
		<p/>
		TODO: Suggest to fulfill user's contact info, etc. if not  filled yet.
		TODO: Suggest to create 1st shop if there is no shop yet. Give link to "Create shop" (add shop)
	</body>
</html>