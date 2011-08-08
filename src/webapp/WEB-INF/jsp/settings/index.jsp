<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.settings"/></title>
	</head>
	<body>
		<h2><s:text name="msgkey.title.myAccount"/></h2>
		<ul>
			<s:if test="%{!currentUser.enabled}">
				<li>
					<s:url var="confirmEmailUrl" action="confirm-email" namespace="/"/>
					<s:a href="%{#confirmEmailUrl}"><s:text name="msgkey.link.confirmEmailAddress"/></s:a>
				</li>
				<li>
					<s:url var="sendConfirmEmailUrl" action="send-confirmation-email" namespace="/"/>
					<s:a href="%{#sendConfirmEmailUrl}"><s:text name="msgkey.link.resendConfirmationEmail"/></s:a>
				</li>
			</s:if>
			<li>
				<s:url var="changePasswordUrl" action="change-password" namespace="/settings"/>
				<s:a href="%{#changePasswordUrl}"><s:text name="msgkey.link.changePassword"/></s:a>
			</li>
			<li>
				<s:url var="shoppingCountriesUrl" action="shopping-countries" namespace="/"/>
				<s:a href="%{#shoppingCountriesUrl}"><s:text name="msgkey.link.settings.shoppingCountries"/></s:a>
			</li>
		</ul>
		<p/>
	</body>
</html>