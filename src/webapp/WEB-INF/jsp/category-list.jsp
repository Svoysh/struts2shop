<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.categories"/></title>
	</head>
	<body>
		<s:if test="isEmpty(categories)">
			<s:text name="msgkey.text.notExist.category"/>
		</s:if>
		<s:else>
			<f:topPager/>
			<%-- TODO: Duplication: See "shops" section of [search.jsp]. --%>	
			<table class="ListTable">
				<tr>
					<%-- TODO: Enhance: Make sortable headers. --%>
					<%-- TODO: Low: Print row number (from pager). --%>
					<th><s:text name="msgkey.label.name"/></th>
					<th><s:text name="msgkey.label.productCount"/></th>
				</tr>
				<s:iterator value="categories" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td>
							<s:url action="category!products" namespace="/" var="categoryProductsUrl">
								<s:param name="categoryName" value="%{name}"/>
							</s:url>
							<s:a href="%{#categoryProductsUrl}"><s:property value="name"/></s:a>
						</td>
						<%-- TODO: Print link to "Products by category name" if productCount > 0. --%>
						<td class="Number"><s:property value="productCount"/></td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:else>
	</body>
</html>
