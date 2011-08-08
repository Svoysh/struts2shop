<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<%-- TODO: Duplication: Refactor: See "/buyer/order-items". --%>
<html>
	<head>
		<title>Products in order #TODO: print order number</title>
	</head>
	<body>
		<%-- TODO: Add pager. --%>
		
		<s:if test="isEmpty(orderItems)">
			There are no products.
		</s:if>
		<s:else>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th>Product image</th>
					<th><s:text name="msgkey.label.product"/></th>
					<th><s:text name="msgkey.label.shop"/></th>
					<th><s:text name="msgkey.label.price"/></th>
					<th><s:text name="msgkey.label.quantity"/></th>
					<th><s:text name="msgkey.label.subtotal"/></th>
					<th>Ordered</th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="orderItems" status="num">
					<s:if test="product != null">
						<s:url action="product!view" namespace="/" var="viewProductUrl">
							<s:param name="productId" value="%{product.id}"/>
						</s:url>
					</s:if>
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td>
							<s:if test="image != null">
								<s:if test="product != null">
									<a class="ThumbnailLink" href="<s:property value="#viewProductUrl"/>">
										<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id + '/small/' + image.fileName + '.jpg'"/>">
									</a>
								</s:if>
								<s:else>
									<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id + '/small/' + image.fileName + '.jpg'"/>">
								</s:else>
							</s:if>
							<s:else>TODO: No image</s:else>
						</td>
						<td>
							<s:if test="product != null">
								<s:a href="%{#viewProductUrl}"><s:property value="name"/></s:a>
							</s:if>
							<s:else>
								<%-- TODO: i18n: "DELETED" --%>
								<s:property value="name"/>&nbsp;[DELETED]
							</s:else>
						</td>
						<td>
							<s:if test="shop != null">
								<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
								<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
							</s:if>
							<s:else>
								<%-- TODO: i18n: "DELETED" --%>
								<s:property value="shopName"/>&nbsp;[DELETED]
							</s:else>
						</td>
						<td class="Number"><s:property value="price"/>&nbsp;<s:property value="currency"/></td>
						<td class="Number"><s:property value="quantity"/></td>
						<td class="Number"><s:property value="subtotal"/>&nbsp;<s:property value="currency"/></td>
						<td class="Date"><s:date name="created"/></td>
						<td class="Actions">
							TODO: Smth useful.
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:else>
	</body>
</html>