<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<title>Register</title>
	</head>
	<body>
		<s:form action="register">
			<%-- 
				TODO: Request only: 
				- First name;
				- Last name;
				- Email;
				- Password (w/o "Retype password"? (y)).
				
				See registration on LinkedIn or Facebook.
			--%>
			<%-- TODO: Low: Require only one of "firstName" and "lastName". --%>
			<s:textfield id="firstName" name="firstName" required="true" key="msgkey.label.firstName"/>
			<s:textfield name="lastName" required="true" key="msgkey.label.lastName"/>
			<s:textfield id="email" name="email" required="true" key="msgkey.label.email"/>
			
			<%-- TODO: Low: Print hint/recommendation to create strong password: letters, numbers, special keys. --%>
			<%-- TODO: Low: JS: Impl or use existent password meter. (jQuery plugin?) --%>
			<s:password name="password" required="true" key="msgkey.label.password"/>
			<s:password name="passwordControl" required="true" key="msgkey.label.retypePassword"/>	
			
			<h:formControlPanel>
				<s:submit cssClass="MainButton" name="method:submit" key="msgkey.action.register"/>
				<s:url var="loginUrl" action="login" namespace="/"/>
				<s:a href="%{#loginUrl}"><s:text name="msgkey.link.question.alreadyRegistered"/></s:a>
				<p/>
				<s:url var="termsOfServiceUrl" value="%{'/html/termsOfService.html'}"/>
				<s:url var="privacyPolicyUrl" value="%{'/html/privacyPolicy.html'}"/>
				<%-- 
					TODO: Issue: i18n: In russian text of links (terms and privacy) 
					must be склонены по падежам:
						... согласны с ПолитикОЙ ... УсловияМИ ...
				--%>
				<%-- TODO: i18n: Externalize this big message. Include links (<a>) into message. --%>
				By clicking button &quot;<strong><s:text name="msgkey.action.register"/></strong>&quot;, 
				you are agreeing to <s:a href="%{#termsOfServiceUrl}"><s:text name="msgkey.link.termsOfService"/></s:a>  
				and <s:a href="%{#privacyPolicyUrl}"><s:text name="msgkey.link.privacyPolicy"/></s:a>.
			</h:formControlPanel>
		</s:form>
		
		<content tag="lastJavaScript">
			<script type="text/javascript">
				$(document).ready(function()
				{
					// TODO: Focus on "firstName" instead of "email"? (n)
//					focusElemIfEmpty("firstName");
					focusElemIfEmpty("email");
				});
			</script>
		</content>
	</body>
</html>