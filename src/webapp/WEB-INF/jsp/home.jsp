<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Low: Change file name to "index.jsp". --%>
<html>
	<head>
		<s:if test="loggedIn">
			<title>Welcome, <s:property value="currentUser.fullName"/>!</title>
		</s:if>
		<s:else>
			<title>Welcome!</title>
		</s:else>
	</head>
	<body>
		TODO: Smth interesting here, e.g. tell about this webapp (site).<br/>
		TODO: Last added shops.<br/>
		TODO: Last added products.<br/>
	</body>
</html>