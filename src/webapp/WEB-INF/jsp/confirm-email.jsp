<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.confirmEmailAddress"/></title>
	</head>
	<body>
		<s:form action="confirm-email">
			<s:textfield id="code" name="code" required="true" key="msgkey.label.confirmEmail.code"/>	
			<h:formControlPanel>
				<s:submit cssClass="MainButton" name="method:submit" key="msgkey.action.confirm"/>
			</h:formControlPanel>
		</s:form>
		<p>
			<s:text name="msgkey.text.tryTheseActionsIfHaveNoConfirmationCode"/>
			<ul>
				<s:url var="sendConfirmEmailUrl" action="send-confirmation-email" namespace="/"/>
				<li><s:a href="%{#sendConfirmEmailUrl}"><s:text name="msgkey.link.resendConfirmationEmail"/></s:a></li>
				<li><a href="#">TODO: Members: Change your contact email.</a></li>
			</ul>
		<p/>
		<content tag="lastJavaScript">
			<script type="text/javascript">
				$(document).ready(function()
				{
					focusElemIfEmpty("code");
				});
			</script>
		</content>
	</body>
</html>