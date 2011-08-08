<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<%-- TODO: DEVELOPMENT: Delete this action in production. --%>
<html>
	<head>
		<title>Test actions</title>
	</head>
	<body>
		<h2>Change admin password using Struts 2 + ...</h2>
		
		<h3>Tag handler</h3>
		<s:form action="test">
			<h:formField fieldName="currentPassword" required="true">
				<jsp:attribute name="label"><s:property value="%{'Password'}"/></jsp:attribute>
				<jsp:attribute name="labelKey">msgkey.label.currentPassword</jsp:attribute>
				<jsp:body>
					<input type="password" id="currentPassword" name="currentPassword" size="50"/>
					<a href="#nothing">Forgot your password?</a>
				</jsp:body>
			</h:formField>
			<h:formControlPanel>
				<s:submit cssClass="MainButton" value="Change"/>
			</h:formControlPanel>
		</s:form>
		
		<%--
		<h2>Change admin password using Struts 2 + ...</h2>
		
		<h3>JSP tag file</h3>
		<s:form action="test">
			<f:formField fieldName="currentPassword" required="true">
				<jsp:attribute name="label"><s:property value="%{'Hi, Struts 2!'}"/></jsp:attribute>
				<jsp:body>
					<input type="password" id="currentPassword" name="currentPassword" size="50"/>
					<a href="#nothing">Forgot your password?</a>
				</jsp:body>
			</f:formField>
			<f:formControlPanel>
				<s:submit cssClass="MainButton" value="Change"/>
			</f:formControlPanel>
		</s:form>
		--%>
		
		<%--
		<h2>Change admin password using...</h2>
		<h3>standard action invocation</h3>
		<s:form action="test">
			<s:textfield name="currentPassword" size="50" 
				key="msgkey.label.currentPassword"/>
			
			<f:formControlPanel>
				<s:submit cssClass="MainButton" value="Change"/>
			</f:formControlPanel>
		</s:form>
		
		<h3>dynamic method invocation</h3>
		<s:url var="publicChangeAdminPassword" action="test" method="publicChangeAdminPassword"/>
		<s:url var="protectedChangeAdminPassword" action="test" method="protectedChangeAdminPassword"/>
		<s:url var="privateChangeAdminPassword" action="test" method="privateChangeAdminPassword"/>
		<ul>
			<li><s:a href="%{#publicChangeAdminPassword}">publicChangeAdminPassword</s:a></li>
			<li><s:a href="%{#protectedChangeAdminPassword}">protectedChangeAdminPassword</s:a></li>
			<li><s:a href="%{#privateChangeAdminPassword}">privateChangeAdminPassword</s:a></li>
		</ul>
		--%>
		
		<%--
		<h2>Send test email</h2>
		<s:form action="test">
			<s:textfield label="To" name="to" size="50"/>
			<s:textfield label="Subject" name="subject" size="50"/>
			<s:textfield label="Name of recipient" name="name" size="50"/>
			
			<f:formControlPanel>
				<s:submit cssClass="MainButton" name="method:sendEmail" value="Send email"/>
			</f:formControlPanel>
		</s:form>
		--%>
	</body>
</html>