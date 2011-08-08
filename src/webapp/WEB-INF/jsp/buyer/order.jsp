<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title>Order #<s:property value="order.number"/></title>
	</head>
	<body>
		<%-- TODO: Refactor: Duplication: See [checkout/review-order.jsp] --%>
		
		<a href="<%= request.getContextPath() %>/buyer/order!list">Back to orders</a>
		<s:if test="order == null">
			<%-- TODO: Print this text in title instead. --%>
			<%-- TODO: Print the link to Order list. --%>
			<p><s:text name="msgkey.text.notFound.order"/></p>
		</s:if>
		<s:else>
			<s:push value="order">
				<%-- TODO: Print buyer notes. --%>
				<table class="HiddenTable OrderInfoTable">
					<tr>
						<td class="OrderInfoCell">
							<h2><s:text name="msgkey.title.orderSummary"/></h2>
							<table class="ViewTable">
								<%-- TODO: Choose more appropriate and sensible order of fields to print. --%>
								<tr>
									<th><s:text name="msgkey.label.id"/></th>
									<td><s:property value="id"/></td>
								</tr>
								<tr>
									<th><s:text name="msgkey.label.number"/></th>
									<td><s:property value="number"/></td>
								</tr>
								<tr>
									<th>Ordered on</th>
									<td><s:date name="created"/></td>
								</tr>
								<tr>
									<th>Ordered at shop</th>
									<td>
										<%-- TODO: Duplication block: Extract include file. --%>
										<s:if test="shop != null">
											<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
											<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
										</s:if>
										<s:else>
											<%-- TODO: i18n: "DELETED" --%>
											<s:property value="shopName"/>&nbsp;[DELETED]
										</s:else>
									</td>
								</tr>
								<tr>
									<th><s:text name="msgkey.label.seller"/></th>
									<td>
										<%-- TODO: Duplication block: Extract include file. --%>
										<s:if test="shop != null">
											<s:url action="user" namespace="/" var="viewUserUrl">
												<s:param name="id" value="shop.user.id"/>
											</s:url>
											<s:a href="%{#viewUserUrl}"><s:property value="shop.user.fullName"/></s:a>
										</s:if>
										<s:else>[DELETED]</s:else>
									</td>
								</tr>
								<tr>
									<th>Email you specified</th>
									<td><s:property value="email"/></td>
								</tr>
								<tr>
									<th>Paid</th>
									<td><s:property value="paid ? 'Yes' : 'No'"/></td>
								</tr>
								<tr>
									<th>Status</th>
									<td><s:property value="status"/></td>
								</tr>
								<tr>
									<th><s:text name="msgkey.label.totalPrice"/></th>
									<td><s:property value="totalPrice"/>&nbsp;<s:property value="currency"/></td>
								</tr>
								<tr>
									<th><s:text name="msgkey.label.countOfOrderedProducts"/></th>
									<td><s:property value="itemCount"/></td>
								</tr>
								<tr>
									<th><s:text name="msgkey.label.totalQuantityOfOrderedProducts"/></th>
									<td><s:property value="totalQuantity"/></td>
								</tr>
								<tr>
									<th>Your notes</th>
									<td><s:property value="buyerNotes"/></td>
								</tr>
								<tr>
									<th>Seller notes</th>
									<td><s:property value="sellerNotes"/></td>
								</tr>
							</table>
						</td>
						<td class="OrderInfoCell">
							<h2><s:text name="msgkey.title.billingAddress"/></h2>
							<s:set var="address" value="%{billingAddress}"/>
							<jsp:include page="/WEB-INF/jsp/includes/address.jsp"/>
						</td>
						<td class="OrderInfoCell">
							<h2><s:text name="msgkey.title.shippingAddress"/></h2>
							<s:set var="address" value="%{shippingAddress}"/>
							<jsp:include page="/WEB-INF/jsp/includes/address.jsp"/>
						</td>
					</tr>
				</table>
				
				<s:if test="%{hasShippingMethod()}">
					<p/>
					<h2><s:text name="msgkey.title.shippingMethod"/></h2>
					<s:set var="shippingMethod" value="shippingMethod"/>
					<jsp:include page="/WEB-INF/jsp/includes/shippingMethod.jsp"/>
				</s:if>
				
				<s:if test="%{hasPaymentMethod()}">
					<p/>
					<h2><s:text name="msgkey.title.paymentMethod"/></h2>
					<s:set var="paymentMethod" value="paymentMethod"/>
					<jsp:include page="/WEB-INF/jsp/includes/paymentMethod.jsp"/>
				</s:if>
			</s:push>
		</s:else>
		<p/>
		<h2>Ordered products</h2>
		<s:if test="isEmpty(orderItems)">
			There are no products in this order.
			<%-- TODO: Print link to browse for "interesting" products. --%>
		</s:if>
		<s:else>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th>Product image</th>
					<th><s:text name="msgkey.label.product"/></th>
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
					<s:else>
						<s:url var="viewProductUrl" value="#"/>
					</s:else>
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td>
							<%-- TODO: Duplication: See [checkout/review-order.jsp] --%>
							<a class="ThumbnailLink" href="<s:property value="#viewProductUrl"/>">
								<s:if test="image != null">
									<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id + '/small/' + image.fileName + '.jpg'"/>">
								</s:if>
								<s:else>
									<img src="<%= request.getContextPath() %>/images/no-product-image-s40.png">
								</s:else>
							</a>
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
		<p/>
		<a href="<%= request.getContextPath() %>/buyer/product!list">Back to orders</a>
	</body>
</html>