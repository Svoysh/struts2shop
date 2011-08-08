<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title>
			<s:text name="msgkey.title.cartItemsByShop">
				<s:param value="%{shop.name}"/>
			</s:text>
		</title>
	</head>
	<body>
		<%-- TODO: Add pager. --%>
		<%-- TODO: Remove cart preview from this page? (y) --%>
		
		<s:if test="isEmpty(cartItems)">
			<s:text name="msgkey.text.noCartItemsByShop"/>
			<%-- TODO: Print link to browse for "interesting" products. --%>
		</s:if>
		<s:else>
			<s:form action="cart!update" namespace="/" theme="simple">
				<f:topPager/>
				<table class="ListTable">
					<tr>
						<th>Item ID</th>
						<th>Product image</th>
						<th><s:text name="msgkey.label.product"/></th>
						<th><s:text name="msgkey.label.shop"/></th>
						<th><s:text name="msgkey.label.price"/></th>
						<th><s:text name="msgkey.label.quantity"/></th>
						<th><s:text name="msgkey.label.subtotal"/></th>
						<th><s:text name="msgkey.label.addedToCartOn"/></th>
						<th><s:text name="msgkey.label.actions"/></th>
					</tr>
					<s:iterator value="cartItems" status="num">
						<s:url action="product!view" namespace="/" var="viewProductUrl">
							<s:param name="productId" value="%{product.id}"/>
						</s:url>
						<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
							<td class="Id"><s:property value="id"/></td>
							<td>
								<s:if test="image != null">
									<a class="ThumbnailLink" href="<s:property value="#viewProductUrl"/>">
										<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id + '/small/' + image.fileName + '.jpg'"/>">
									</a>
								</s:if>
								<s:else>TODO: No image</s:else>
							</td>
							<td>
								<s:a href="%{#viewProductUrl}"><s:property value="name"/></s:a>
							</td>
							<td>
								<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
								<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
							</td>
							<td class="Number"><s:property value="price"/>&nbsp;<s:property value="currency"/></td>
							<td class="Number">
								<s:hidden name="cartItemIds" value="%{id}"/>
								<s:textfield cssClass="QuantityInput" name="quantities" value="%{quantity}"/>
							</td>
							<td class="Number"><s:property value="subtotal"/>&nbsp;<s:property value="currency"/></td>
							<td class="Date"><s:date name="created"/></td>
							<td class="Actions">
								<ul>
									<s:url action="cart!remove" namespace="/" var="removeFromCartUrl">
										<s:param name="shopId" value="%{shopId}"/>
										<s:param name="cartItemId" value="%{id}"/>
									</s:url>
									<li><s:a href="%{#removeFromCartUrl}"><s:text name="msgkey.action.remove"/></s:a></li>
								</ul>
							</td>
						</tr>
					</s:iterator>
					<%-- TODO: Print total. --%>
				</table>
				<f:bottomPager/>
				
				<p/>
				<s:hidden name="shopId" value="%{shopId}"/>
				<s:submit name="method:update" key="msgkey.action.updateCart"/>
				&nbsp;<s:text name="msgkey.text.or"/>&nbsp;
				<s:url namespace="/checkout" action="begin" var="beginCheckoutUrl">
					<s:param name="shopId" value="%{shopId}"/>
				</s:url>
				<s:a href="%{#beginCheckoutUrl}"><s:text name="msgkey.action.beginCheckout"/></s:a><br/>
			</s:form>
		</s:else>
	</body>
</html>