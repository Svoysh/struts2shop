<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Rethink: Duplication: See PaymentMethod views. --%>

<html>
	<head>
		<%-- TODO: Rewrite to: "About to delete [entity-type] [entity-name]"? (y) --%>
		<title>Deleting shipping method <s:property value="shippingMethod.name"/></title>
	</head>
	<body>
		Deleted shipping method <strong>cannot be restored</strong>! 
		Do you really want to delete this shipping method?
		<s:set var="formAction" value="'shipping-method'"/>
		<jsp:include page="/WEB-INF/jsp/includes/entity-delete-form.jsp"/>
	</body>
</html>