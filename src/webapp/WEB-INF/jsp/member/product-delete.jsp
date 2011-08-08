<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<%-- TODO: Rewrite to: "About to delete [entity-type] [entity-name]"? (y) --%>
		<title>Deleting product <s:property value="product.name"/></title>
	</head>
	<body>
		<%-- TODO: Make list URL as variable and reuse it below. Or move to include file. --%>
		<a href="<%= request.getContextPath() %>/member/product!list">Back to Products</a>
		<p/>
		Deleted product and its variants <strong>cannot be restored</strong>! 
		Do you really want to delete this product?
		<s:set var="formAction" value="'product'"/>
		<jsp:include page="/WEB-INF/jsp/includes/entity-delete-form.jsp"/>
		<p/>
		<a href="<%= request.getContextPath() %>/member/product!list">Back to Products</a>
	</body>
</html>