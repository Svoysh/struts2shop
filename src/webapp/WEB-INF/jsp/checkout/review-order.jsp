<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.reviewOrder"/></title>
	</head>
	<body>
		<%-- TODO: Refactor: Duplication: See table in [buyer/order.jsp] --%>
		<s:push value="order">
			<h2><s:text name="msgkey.title.billingAddress"/></h2>
			<s:set var="address" value="%{billingAddress}"/>
			<jsp:include page="/WEB-INF/jsp/includes/address.jsp"/>
			
			<p/>
			<h2><s:text name="msgkey.title.shippingAddress"/></h2>
			<s:set var="address" value="%{shippingAddress}"/>
			<jsp:include page="/WEB-INF/jsp/includes/address.jsp"/>
			
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
			
			<p/>
			<h2><s:text name="msgkey.title.products"/></h2>
			<table class="ListTable">
				<tr>
					<th>&nbsp;</th>
					<th><s:text name="msgkey.label.product"/></th>
					<th><s:text name="msgkey.label.price"/></th>
					<th><s:text name="msgkey.label.quantity"/></th>
					<th><s:text name="msgkey.label.subtotal"/></th>
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
						<td>
							<%-- TODO: Duplication: See [buyer/order.jsp] --%>
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
					</tr>
				</s:iterator>
			</table>
			
			<p/>
			<h2><s:text name="msgkey.title.orderSummary"/></h2>
			<table class="ViewTable">
				<%-- TODO: Use email field? (mb) --%>
				<%--<tr>
					<th>Email you specified</th>
					<td><s:property value="email"/></td>
				</tr>--%>
				<tr>
					<th><s:text name="msgkey.label.countOfOrderedProducts"/></th>
					<td><s:property value="itemCount"/></td>
				</tr>
				<tr>
					<th><s:text name="msgkey.label.totalQuantityOfOrderedProducts"/></th>
					<td><s:property value="totalQuantity"/></td>
				</tr>
				<tr>
					<th><s:text name="msgkey.label.totalPrice"/></th>
					<td><s:property value="totalPrice"/>&nbsp;<s:property value="currency"/></td>
				</tr>
			</table>
		</s:push>
		
		<p/>
		<s:form namespace="/checkout" action="place-order">
			<s:hidden name="checkoutId" value="%{checkoutId}"/>
			<%-- TODO: Low: Wrap by FCKeditor? (n) --%>
			<s:textarea cssClass="SmallHtmlEditorArea"
				name="buyerNotes" key="msgkey.label.checkout.buyerNotes"
				rows="10" cols="60"/>
			<s:submit key="msgkey.action.placeOrder"/>
		</s:form>
	</body>
</html>