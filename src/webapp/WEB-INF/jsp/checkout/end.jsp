<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.success.orderPlaced"/></title>
	</head>
	<body>
		<%-- TODO: i18n --%>
		Congratulations! You order placed successfully.
		<p/>You order number is: <strong><s:property value="orderNumber"/></strong>
		<%-- TODO: Print order summary, etc. --%>
		<p/>
		<s:if test="!loggedIn">
			TODO: Propose to save address and order info by registering new account. 
			Use current user and expand it on registration.
		</s:if>
		<s:else>
			TODO: Suppose some page to go next. Ex. link to "View order" in buyer panel.
			TODO: If cart yet has items from another shops, then suppose to return to cart.
		</s:else>
	</body>
</html>