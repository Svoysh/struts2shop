<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title>Successfully registered</title>
	</head>
	<body>
		<%-- TODO: Link to which page do provide: account, profile, member home? (profile) --%>
		Congratulations! You have been successfully registered as 
		<a href="<%= request.getContextPath() %>/member/home"><s:property value="currentUser.fullName"/></a>.
		<p/>
		TODO: Offer to submit additional user's info like: First name, Last name, Date of Birth, Addresses, Contacts...
		<p/>
		<%-- 
			TODO: Provide link to interesting entry point of site investigation. 
			      From where newly registered user can start to play around. 
			      Ex: "Create first shop" or "Buy smth product".
		--%>
		You can start from <a href="<%= request.getContextPath() %>/member/home">here</a>.
	</body>
</html>