<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<title>Login</title>
	</head>
	<body>
		<s:form action="login">
			<s:textfield id="email" name="email" required="true" key="msgkey.label.email"/>
			
			<%-- TODO: Or maybe use email input w/ link to register. (n) --%>
			<%--
			<h:formField fieldName="email" required="true" labelKey="msgkey.label.email">
				<input type="text" id="email" name="email"/>
				<s:url var="registerUrl" action="register" namespace="/"/>
				<s:a href="%{#registerUrl}">Not registered?</s:a>
			</h:formField>
			--%>
			
			<h:formField fieldName="password" required="true" labelKey="msgkey.label.password">
				<input type="password" id="password" name="password"/>
				<a href="#TODO: reset-password"><s:text name="msgkey.link.question.forgotYourPassword"/></a>
			</h:formField>
			
			<%-- 
				TODO: Low: Possible labels for "remember me":
				- "Remember my login."
				- "Remember my login on this browser."
				- "Remember my login on this computer."
			--%>
			<%-- TODO: After test: Remove attribute 'password' of tag 't:formField'. --%>
			<h:formField fieldName="rememberMe">
				<s:checkbox id="rememberMe" name="rememberMe" theme="simple"/>
				<label for="rememberMe"><s:text name="msgkey.text.rememberThisLogin"/></label>
			</h:formField>
			
			<h:formControlPanel>
				<s:submit cssClass="MainButton" name="method:submit" key="msgkey.action.login"/>
				<s:url var="registerUrl" action="register" namespace="/"/>
				<s:a href="%{#registerUrl}"><s:text name="msgkey.link.question.notRegistered"/></s:a>
			</h:formControlPanel>
		</s:form>
		
		<content tag="lastJavaScript">
			<script type="text/javascript">
				$(document).ready(function()
				{
					focusElemIfEmpty("email");
				});
			</script>
		</content>
	</body>
</html>