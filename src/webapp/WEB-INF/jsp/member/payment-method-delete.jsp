<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Rethink: Duplication: See ShippingMethod views. --%>

<html>
	<head>
		<%-- TODO: Rewrite to: "About to delete [entity-type] [entity-name]"? (y) --%>
		<title>Deleting payment method <s:property value="paymentMethod.name"/></title>
	</head>
	<body>
		Deleted payment method <strong>cannot be restored</strong>! 
		Do you really want to delete this payment method?
		<s:set var="formAction" value="'payment-method'"/>
		<jsp:include page="/WEB-INF/jsp/includes/entity-delete-form.jsp"/>
	</body>
</html>