<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.changePassword"/></title>
	</head>
	<body>
		<s:form action="change-password" namespace="/settings">
			<h:formField fieldName="currentPassword" required="true" 
					labelKey="msgkey.label.currentPassword">
				<input type="password" id="currentPassword" name="currentPassword"/>
				<a href="#TODO: reset-password"><s:text name="msgkey.link.question.forgotYourPassword"/></a>
			</h:formField>
			
			<s:password name="newPassword" required="true" key="msgkey.label.newPassword"/>
			<s:password name="newPasswordControl" required="true" key="msgkey.label.retypeNewPassword"/>
			
			<h:formControlPanel>
				<s:submit cssClass="MainButton" name="method:submit" key="msgkey.action.change"/>
			</h:formControlPanel>
		</s:form>
		
		<content tag="lastJavaScript">
			<script type="text/javascript">
				$(document).ready(function()
				{
					focusElemIfEmpty("currentPassword");
				});
			</script>
		</content>
	</body>
</html>