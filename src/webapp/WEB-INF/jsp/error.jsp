<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Separate into "error.jsp" for expected action errors, and "exception.jsp" for unhandled exceptions. --%>
<html>
	<head>
		<title><s:text name="msgkey.title.errorOccurred"/></title>
	</head>
	<body>
		<s:if test="%{!hasErrors()}">
			<p>
			<%-- TODO: i18n. --%>
			We're sorry, but an unexpected error has occurred. 
			Try to return to the previous action in a few seconds.
			</p>
		</s:if>
	</body>
</html>