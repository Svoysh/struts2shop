<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<%-- TODO: Rewrite to: "About to delete [entity-type] [entity-name]"? (y) --%>
		<title>Deleting category <s:property value="category.name"/></title>
	</head>
	<body>
		<%-- TODO: Make list URL as variable and reuse it below. Or move to include file. --%>
		<a href="<%= request.getContextPath() %>/member/category!list">Back to Categories</a>
		<p/>
		Deleted category and products in it <strong>cannot be restored</strong>! 
		Do you really want to delete this category?
		<s:set var="formAction" value="'category'"/>
		<jsp:include page="/WEB-INF/jsp/includes/entity-delete-form.jsp"/>
		<p/>
		<a href="<%= request.getContextPath() %>/member/category!list">Back to Categories</a>
	</body>
</html>