<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<%-- TODO: Rewrite to: "About to delete [entity-type] [entity-name]"? (y) --%>
		<title>Deleting shop <s:property value="shop.name"/></title>
	</head>
	<body>
		<%-- TODO: Make list URL as variable and reuse it below. Or move to include file. --%>
		<a href="<%= request.getContextPath() %>/member/shop!list">Back to Shops</a>
		<p/>
		Deleted shop and its products <strong>cannot be restored</strong>! 
		Do you really want to delete this shop?
		<s:set var="formAction" value="'shop'"/>
		<jsp:include page="/WEB-INF/jsp/includes/entity-delete-form.jsp"/>
		<p/>
		<a href="<%= request.getContextPath() %>/member/shop!list">Back to Shops</a>
	</body>
</html>