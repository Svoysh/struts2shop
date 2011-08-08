<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.chooseShippingMethod"/></title>
	</head>
	<body>
		<s:iterator value="shippingMethods" var="shippingMethod" status="num">
			<%-- TODO: Highlight method already selected in this C.O. process. --%>
			<jsp:include page="/WEB-INF/jsp/includes/shippingMethod.jsp"/>
			<s:form namespace="/checkout" action="shipping-method!submit">
				<s:hidden name="checkoutId" value="%{checkoutId}"/>
				<s:hidden name="shippingMethodId" value="%{id}"/>
				<s:submit key="msgkey.action.useThisMethod"/>
			</s:form>
			<p/>
		</s:iterator>
	</body>
</html>