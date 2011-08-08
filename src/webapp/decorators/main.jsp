<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/css/main.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/css/struts.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/lib/jquery-plugins/fancybox/1.2.6/jquery.fancybox.css"/>
		<decorator:head/>
	</head>
	
	<body>
		<div id="pageBlock" class="PageBlock">
			<jsp:include page="/WEB-INF/jsp/includes/header.jsp"/>
			
			<%-- TODO: PRODUCTION: If JS disabled print to user warning.
				
				Below is warning from Goolge Nexus One page:
				"It looks like you have JavaScript disabled in your browser. 
				This page requires Javascript to work. Learn how to enable 
				Javascript in your browser."
				
				And the link on words "enable Javascript":
				https://www.google.com/adsense/support/bin/answer.py?answer=12654
			--%>
			<%-- TODO: Make as link (<a>) to this page. --%>
			<h1><decorator:title/></h1>
			
			<table class="PageContent" cellpadding="0" cellspacing="0">
				<tr>
					<td id="content">
						<%-- TODO: Include page "../includes/requirements.jsp". --%>
						
						<s:actionmessage escape="false"/>
						<s:actionerror escape="false"/>
						
						<decorator:body/>
						
						<%-- Construct URL from page request and append 'printable=true' param. --%>
						<%
							// TODO: Move to function in AbstractAction.
							StringBuilder printUrl = new StringBuilder();
							printUrl.append(request.getRequestURI());
							printUrl.append("?printable=true");
							if (request.getQueryString() != null) 
							{
								printUrl.append('&');
								printUrl.append(request.getQueryString());
							}
						%>
						<p align="right">[<a href="<%= printUrl.toString() %>"><s:text name="msgkey.link.printableVersion"/></a>]</p>
					</td>
					<td class="RightContent">
						<div id="cartPreview">
							<%-- TODO: Refactor: Duplication: See [cart-last-added.jsp]. --%>
							<%-- TODO: i18n: --%>
							<div class="CartCaption"><s:text name="msgkey.title.cart"/></div>
							<div id="cartPreviewBlock" class="CartPerPage">
								<div class="CartItemsWrapper"><s:text name="msgkey.text.loading"/></div>
							</div>
						</div>
					</td>
				</tr>
			</table>
			
			<jsp:include page="/WEB-INF/jsp/includes/footer.jsp"/>
		</div>
		
		<%-- 
			SiteMesh will include content of tag "content" with tag="firstJavaScript".
			Ex:
			<content tag="firstJavaScript">
				<script type="text/javascript">
					alert("Hello firstJavaScript");
				</script>
			</content>
		--%>
		<decorator:getProperty property="page.firstJavaScript"/>
		
		<%-- TODO: Include JS libs only where it required? (y) --%>
		<%-- TODO: DEVELOPMENT --%>
		<%--
		<script type="text/javascript" 
			src="<%= request.getContextPath() %>/lib/jquery/1.3.2/jquery-1.3.2.js"></script>
		--%>
		
		<%-- TODO: PRODUCTION --%>
		<%----%>
		<script type="text/javascript" 
			src="<%= request.getContextPath() %>/lib/jquery/1.3.2/jquery-1.3.2.min.js"></script>
		
		<%-- Used to submit form via AJAX. --%>
		<%-- TODO: Page speed: Use on pages that only required this plugin, 
		     like pages w/ button "Add to cart". --%>
		<script type="text/javascript" 
			src="<%= request.getContextPath() %>/lib/jquery-plugins/form/2.36/jquery.form.min.js"></script>
		
		<%-- Used to select shopping countries. --%>
		<script type="text/javascript" 
			src="<%= request.getContextPath() %>/lib/jquery-plugins/fancybox/1.2.6/jquery.fancybox.pack.js"></script>
		
		<script type="text/javascript" 
			src="<%= request.getContextPath() %>/js/main.js"></script>
		
		<s:url namespace="/ajax" action="cart!lastAdded" var="cartPreviewUrl"/>
		<script type="text/javascript">
			$(document).ready(function()
			{
				var cartPreviewUrl = '<s:property value="%{#cartPreviewUrl}"/>';
				nonCachingLoad(cartPreviewUrl, "#cartPreview");
			});
		</script>

		<%-- 
			SiteMesh will include content of tag "content" with tag="lastJavaScript".
			Ex:
			<content tag="lastJavaScript">
				<script type="text/javascript">
					alert("Hello lastJavaScript");
				</script>
			</content>
		--%>
		<decorator:getProperty property="page.lastJavaScript"/>
	</body>
</html>