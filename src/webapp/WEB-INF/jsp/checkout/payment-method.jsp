<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.choosePaymentMethod"/></title>
	</head>
	<body>
		<s:iterator value="paymentMethods" var="paymentMethod" status="num">
			<%-- TODO: Highlight method already selected in this C.O. process. --%>
			<jsp:include page="/WEB-INF/jsp/includes/paymentMethod.jsp"/>
			<s:form namespace="/checkout" action="payment-method!submit">
				<s:hidden name="checkoutId" value="%{checkoutId}"/>
				<s:hidden name="paymentMethodId" value="%{id}"/>
				<s:submit key="msgkey.action.useThisMethod"/>
			</s:form>
			<p/>
		</s:iterator>
	</body>
</html>