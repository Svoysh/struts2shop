<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.shops"/></title>
	</head>
	<body>
		<s:if test="%{isEmpty(shops)}">
			<s:text name="msgkey.text.notExist.shop"/>
		</s:if>
		<s:else>
			<f:topPager/>
			<%-- TODO: Duplication: See "shops" section of [search.jsp]. --%>
			<table class="ListTable">
				<tr>
					<%-- TODO: Enhance: Make sortable headers. --%>
					<%-- TODO: Low: Print row number (from pager) instead of ID. --%>
					<th><s:text name="msgkey.label.id"/></th>
					<th><s:text name="msgkey.label.shopLogo"/></th>
					<th><s:text name="msgkey.label.shop"/></th>
					<th><s:text name="msgkey.label.currency"/></th>
					<th><s:text name="msgkey.label.productCount"/></th>
					<th><s:text name="msgkey.label.seller"/></th>
					<th><s:text name="msgkey.label.created"/></th>
					
					<%-- TODO: Usability: Remove actions block? (y) --%>
					<s:if test="loggedIn">
						<th><s:text name="msgkey.label.actions"/></th>
					</s:if>
				</tr>
				<s:iterator value="shops" var="shop" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<s:set var="shopUrl" value="%{urlBuilder.getViewShop(#shop)}"/>
						<td class="Id"><s:property value="id"/></td>
						<td>
							<a class="ThumbnailLink" href="<s:property value="#shopUrl"/>">
								<s:if test="%{logo != null}">
									<%-- TODO: Insert logo into beautiful frame, like product image. --%>
									<%-- TODO: Duplication: Extract to include file? E.g. "print-shop-logo-s200.jsp". --%>
									<%-- TODO: Create thumbs of s40: <img src="<%= request.getContextPath() %>/uploads/images/shop-logos/<s:property value="logo.id + '/s200/' + logo.fileName + '.jpg'"/>"> --%>
								</s:if>
								<s:else>
									<img src="<%= request.getContextPath() %>/images/no-shop-logo-s40.png">
								</s:else>
							</a>
						</td>
						<td>
							<s:a href="%{#shopUrl}"><s:property value="name"/></s:a>
							<jsp:include page="/WEB-INF/jsp/includes/summaryInListTable.jsp"/>
						</td>
						<td class="Currency"><s:property value="currency"/></td>
						<td class="Number">
							<%-- TODO: Refactor: Duplication: See "/member/shop-list.jsp", "/member/shop.jsp". --%>
							<s:if test="productCount > 0">
								<s:url action="shop!products" namespace="/" var="shopProductsUrl">
									<s:param name="shopId" value="%{id}"/>
								</s:url>
								<s:a href="%{#shopProductsUrl}"><s:property value="productCount"/></s:a>
							</s:if>
							<s:else>
								<s:property value="productCount"/>
							</s:else>
						</td>
						<td>
							<s:url action="user" namespace="/" var="viewUserUrl">
								<s:param name="id" value="user.id"/>
							</s:url>
							<s:a href="%{#viewUserUrl}"><s:property value="user.fullName"/></s:a>
						</td>
						<td class="Date"><s:date name="created"/></td>
						
						<%-- TODO: Usability: Remove actions block? (y) --%>
						<s:if test="loggedIn">
							<td class="Actions">
								<s:if test="user.equals(currentUser)">
									<s:url action="shop" namespace="/member" var="viewShopUrl">
										<s:param name="id" value="%{id}"/>
									</s:url>
									<s:url action="shop!edit" namespace="/member" var="editShopUrl">
										<s:param name="id" value="%{id}"/>
									</s:url>
									<s:url action="shop!deleteConfirm" namespace="/member" var="deleteShopUrl">
										<s:param name="id" value="%{id}"/>
									</s:url>
									<s:url action="product!add" namespace="/member" var="addProductUrl">
										<s:param name="shopId" value="%{id}"/>
									</s:url>
									<ul>
										<li><s:a href="%{#viewShopUrl}"><s:text name="msgkey.action.view"/></s:a></li>
										<li><s:a href="%{#editShopUrl}"><s:text name="msgkey.action.edit"/></s:a></li>
										<li><s:a href="%{#deleteShopUrl}"><s:text name="msgkey.action.delete"/></s:a></li>
										<li><s:a href="%{#addProductUrl}">Add product</s:a></li>
									</ul>
								</s:if>
							</td>
						</s:if>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:else>
	</body>
</html>
