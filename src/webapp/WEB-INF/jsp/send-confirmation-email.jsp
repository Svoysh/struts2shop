<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.sendConfirmationEmail"/></title>
	</head>
	<body>
		<s:form action="send-confirmation-email">
			<s:textfield id="email" name="email" required="true" key="msgkey.label.email"/>	
			<h:formControlPanel>
				<s:submit cssClass="MainButton" name="method:submit" key="msgkey.action.send"/>
				<a href="#">TODO: Members: Change your contact email.</a>
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