<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="org.apache.commons.lang.StringUtils;" %>

<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title><decorator:title default="Welcome!"/> - Struts2Shop</title>
		<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath() %>/favicon.ico"/>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/printable.css"/>
		<%-- TODO: CSS: Make styles for printer. --%>
		<decorator:head/>
	</head>
	<body onload="window.print();">
		<h1><decorator:title/></h1>
		<hr noshade="noshade" size="1"/>
		
		<s:actionmessage/>
		<s:actionerror/>
		
		<p><decorator:body/></p>
		
		<hr noshade="noshade" size="1"/>
		<%
			// TODO: Move to function in AbstractAction.
			String originalUrl = new String();
			originalUrl = request.getRequestURI();
			String queryString = request.getQueryString();
			if (!StringUtils.isBlank(queryString)) 
			{
				// TODO: Enhancement: Replace by compiled patterns.
				
				// Remove case insensitive "printable=true", "printable=", 
				// "printable", etc. from URL parameter list.
				String filteredQueryString = queryString
					.replaceAll("(?i)(^|&)printable(|(=)|(=true))$", "")
					.replaceAll("(?i)^printable(|(=)|(=true))&", "")
					.replaceAll("(?i)&printable(|(=)|(=true))&", "&");
				
				if (!StringUtils.isBlank(filteredQueryString)) 
				{
					originalUrl += "?" + filteredQueryString;
				}
			}
		%>
		<a href="<%= originalUrl %>">Original version</a> | 
		<a href="<%= request.getContextPath() %>/">Struts2Shop</a>
	</body>
</html>
