<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Refactor: Duplication: See decorator [main.jsp]. --%>
<div class="CartCaption"><s:text name="msgkey.title.cart"/></div>
<div id="cartPreviewBlock" class="CartPerPage">
	<s:if test="%{isEmpty(cartItems)}">
		<div class="CartItemsWrapper"><s:text name="msgkey.text.yourCartIsEmpty"/></div>
		<%-- TODO: Print link to browse for "interesting" products. --%>
	</s:if>
	<s:else>
		<%-- TODO: Print "Edit (cart items)" link/button. --%>
		<%-- TODO: Print "Proceed to Checkout" must refer to cart items filtered by first shop from list of shop of cart items. --%>
		<s:url action="cart!subcarts" namespace="/" var="proceedToCheckoutUrl"></s:url>
		<s:a cssClass="CheckoutButton" href="%{#proceedToCheckoutUrl}"><s:text name="msgkey.action.proceedToCheckout"/></s:a>
		
		<%-- TODO: Enhance: Add button "Clear (cart)". Practically proven. --%>
		
		<div class="CartItemsWrapper">
			<table class="CartItems">
				<s:iterator value="%{cartItems}">
					<s:url action="product!view" namespace="/" var="viewProductUrl">
						<s:param name="productId" value="%{product.id}"/>
					</s:url>
					<tr>
						<td class="ImageCell">
							<a class="ThumbnailLink" href="<s:property value="#viewProductUrl"/>">
								<s:if test="image != null">
									<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id + '/small/' + image.fileName + '.jpg'"/>">
								</s:if>
								<s:else>
									<img src="<%= request.getContextPath() %>/images/no-product-image-s40.png">
								</s:else>
							</a>
						</td>
						<td class="InfoCell">
							<div>
								<div class="ProductName"><s:a href="%{#viewProductUrl}"><s:property value="name"/></s:a></div>
								<div><s:property value="quantity"/>&nbsp;&times;&nbsp;<span class="ProductPrice"><s:property value="price"/>&nbsp;<s:property value="currency"/></span></div>
							</div>
							<div class="RemoveLink">
								<s:form namespace="/ajax" action="cart!deleteCartItem" 
										theme="simple" onsubmit="return removeFromCart(this);">
									<s:hidden name="cartItemId" value="%{id}"/>
									<%-- TODO: Low: UI: Replace button "remove" by compact square like: [x] --%>
									<s:submit type="submit" key="msgkey.action.small.removeFromCart"/>
								</s:form>
							</div>
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>
		
		<s:a cssClass="CheckoutButton" href="%{#proceedToCheckoutUrl}"><s:text name="msgkey.action.proceedToCheckout"/></s:a>
	</s:else>
</div>
<script type="text/javascript">
	/**
	 * AJAX submit of form "Remove from Cart".
	 * 
	 * @param Element form - form element to submit.
	 */
	function removeFromCart(form)
	{
		// TODO: Show feed back: "Removing..." and after signalize that removed.
		return nonCachingFormSubmit(form, "#cartPreview");
	}
</script>