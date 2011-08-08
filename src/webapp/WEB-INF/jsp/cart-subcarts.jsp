<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.cartsByShops"/></title>
	</head>
	<body>
		<%-- TODO: Add pager. --%>
		<%-- TODO: Remove cart preview from this page? (y) --%>
		
		<s:if test="isEmpty(subcarts)">
			<s:text name="msgkey.text.yourCartIsEmpty"/>
			<%-- TODO: Print link to browse for "interesting" products. --%>
		</s:if>
		<s:else>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th>Shop ID</th>
					<th><s:text name="msgkey.label.shopLogo"/></th>
					<th><s:text name="msgkey.label.shop"/></th>
					<th><s:text name="msgkey.label.itemsInCart"/></th>
					<th><s:text name="msgkey.label.totalQuantity"/></th>
					<th><s:text name="msgkey.label.totalPrice"/></th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="subcarts" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="shop.id"/></td>
						<td>TODO</td>
						<td>
							<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
							<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
						</td>
						<td class="Number">
							<s:url namespace="/" action="cart!items" var="cartItemsUrl">
								<s:param name="shopId" value="%{shop.id}"/>
							</s:url>
							<s:a href="%{#cartItemsUrl}"><s:property value="itemCount"/></s:a>
						</td>
						<td class="Number"><s:property value="totalQuantity"/></td>
						<td class="Number"><s:property value="totalPrice"/>&nbsp;<s:property value="currency"/></td>
						<td class="Actions">
							<s:url namespace="/checkout" action="begin" var="beginCheckoutUrl">
								<s:param name="shopId" value="%{shop.id}"/>
							</s:url>
							<ul>
								<li><s:a href="%{#beginCheckoutUrl}"><s:text name="msgkey.action.beginCheckout"/></s:a></li>
								<li><s:a href="%{#cartItemsUrl}"><s:text name="msgkey.action.editCart"/></s:a></li>
								<li><s:a href="#TODO">TODO: <s:text name="msgkey.action.delete"/></s:a></li>
							</ul>
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:else>
	</body>
</html>