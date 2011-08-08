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
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/main.css"/>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/struts.css"/>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/member.css"/>
		<decorator:head/>
	</head>
	
	<body>
		<div id="pageBlock" class="PageBlock">
			<jsp:include page="/WEB-INF/jsp/includes/header-member.jsp"/>
			
			<%-- TODO: Make as link (<a>) to this page. --%>
			<h1><decorator:title/></h1>
			
			<table class="PageContent" cellpadding="0" cellspacing="0">
				<tr>
					<td id="content">
						<table class="ContentTable">
							<tr>
								<td class="LeftContentColumn">
									<div class="LeftPanel">
										<h2 class="LeftPanelCaption"><s:text name="msgkey.label.actions"/></h2>
										<%-- TODO: Low: Add selection of current action in list. --%>
										<%-- TODO: Low: Use JSP XML syntax to fight against IE6 spaces issue and reduce network traffic. --%>
										<ul class="LeftPanelMenu"
											     ><li class="LeftPanelItem"><a class="LeftPanelLink First" href="<%= request.getContextPath() %>/member/shop!add"><s:text name="msgkey.leftMenu.seller.add.shop"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/shop!list"><s:text name="msgkey.leftMenu.seller.list.shop"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/product!add"><s:text name="msgkey.leftMenu.seller.add.product"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/product!list"><s:text name="msgkey.leftMenu.seller.list.product"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/category!add"><s:text name="msgkey.leftMenu.seller.add.category"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/category!list"><s:text name="msgkey.leftMenu.seller.list.category"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/shipping-method!add"><s:text name="msgkey.leftMenu.seller.add.shippingMethod"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/shipping-method!list"><s:text name="msgkey.leftMenu.seller.list.shippingMethod"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/payment-method!add"><s:text name="msgkey.leftMenu.seller.add.paymentMethod"/></a
											></li><li class="LeftPanelItem"><a class="LeftPanelLink" href="<%= request.getContextPath() %>/member/payment-method!list"><s:text name="msgkey.leftMenu.seller.list.paymentMethod"/></a
											></li
										></ul>
									</div>
								</td>
								<td class="CenterContentColumn">
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
							</tr>
						</table>
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
		
		
		<script type="text/javascript" 
			src="<%= request.getContextPath() %>/js/main.js"></script>
		
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