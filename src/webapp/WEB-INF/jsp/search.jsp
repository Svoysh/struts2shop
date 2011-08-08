<%--
	@author Alex Siman 2009-09-07
	@author Alex Siman 2009-12-30
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title>
			<s:text name="msgkey.title.searchResultsFor">
				<s:param value="%{query}"/>
			</s:text>
		</title>
	</head>
	<body>
		<s:url var="shoppingCountriesUrl" namespace="/" action="shopping-countries"/>
		<%-- TODO: i18n --%>
		<h2><s:text name="msgkey.title.selectShoppingCountries"/>
			(<s:a href="%{#shoppingCountriesUrl}"><s:text name="msgkey.action.change"/></s:a>)</h2>
		<s:if test="!isEmpty(countryNames)">
			<s:iterator value="countryNames" var="countryName" status="i">
				<s:property value="countryName"/><s:if test="%{!#i.last}">,&nbsp;</s:if>
			</s:iterator>
		</s:if>
		<s:else>
			<%-- TODO: i18n --%>
			<%-- TODO: Choose text: 
				- "Not selected" (now used)
				- "Not specified"
				- "Show results from any country" (maybe best) 
			--%>
			<em>Not selected</em>
		</s:else>
		<p/>
		
		<s:if test="isSearchIn('products')">
			<h2><s:text name="msgkey.title.products"/></h2>
			<s:if test="!isEmpty(products)">
				<f:topPager/>
				<%-- TOOD: Improve listing of products. See "Shop products" view. --%>
				<table class="ListTable">
					<tr>
						<th><s:text name="msgkey.label.price"/></th>
						<th><s:text name="msgkey.label.titleImage"/></th>
						<th><s:text name="msgkey.label.product"/></th>
						<th><s:text name="msgkey.label.country"/></th>
						<th><s:text name="msgkey.label.shop"/></th>
					</tr>
					<s:iterator value="products" status="num">
						<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
							<td>TODO</td>
							<td>TODO</td>
							<td>
								<s:url action="product!view" namespace="/" var="viewProductUrl">
									<s:param name="productId" value="%{id}"/>
								</s:url>
								<s:a href="%{#viewProductUrl}"><s:property value="name"/></s:a>
								<jsp:include page="/WEB-INF/jsp/includes/summaryInListTable.jsp"/>
								<%-- TODO: Impl: Button "Add to Cart". --%>
							</td>
							<td class="Country"><s:property value="shop.country"/></td>
							<td>
								<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
								<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
							</td>
						</tr>
					</s:iterator>
				</table>
				<f:bottomPager/>
			</s:if>
			<s:else>
				<s:text name="msgkey.text.search.notFound.products"/>
			</s:else>
		</s:if>
		<s:if test="isSearchIn('shops')">
			<h2><s:text name="msgkey.title.shops"/></h2>
			<s:if test="!isEmpty(shops)">
				<f:topPager/>
				<%-- TODO: Duplication: See [shop-list.jsp]. --%>
				<table class="ListTable">
					<tr>
						<%-- TODO: Enhance: Make sortable headers. --%>
						<%-- TODO: Low: Print row number (from pager) instead of ID. --%>
						<th><s:text name="msgkey.label.shopLogo"/></th>
						<th><s:text name="msgkey.label.shop"/></th>
						<th><s:text name="msgkey.label.country"/></th>
						<th><s:text name="msgkey.label.currency"/></th>
						<th><s:text name="msgkey.label.productCount"/></th>
						<th><s:text name="msgkey.label.seller"/></th>
						<th><s:text name="msgkey.label.created"/></th>
					</tr>
					<s:iterator value="shops" var="shop" status="num">
						<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
							<s:set var="shopUrl" value="%{urlBuilder.getViewShop(#shop)}"/>
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
							<td class="Country"><s:property value="country"/></td>
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
									TODO: <s:property value="productCount"/>
								</s:else>
							</td>
							<td>
								<s:url action="user" namespace="/" var="viewUserUrl">
									<s:param name="id" value="user.id"/>
								</s:url>
								<s:a href="%{#viewUserUrl}"><s:property value="user.fullName"/></s:a>
							</td>
							<td class="Date"><s:date name="created"/></td>
						</tr>
					</s:iterator>
				</table>
				<f:bottomPager/>
			</s:if>
			<s:else>
				<s:text name="msgkey.text.search.notFound.shops"/>
			</s:else>
		</s:if>
	</body>
</html>