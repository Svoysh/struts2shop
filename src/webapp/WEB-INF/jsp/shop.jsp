<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<%-- TODO: i18n --%>
		<title>Shop <s:property value="shop.name"/></title>
	</head>
	<body>
		<s:if test="shop == null">
			<%-- TODO: Print this text in title instead. --%>
			<%-- TODO: Print the link to User list. --%>
			<s:text name="msgkey.text.notFound.shop"/>
		</s:if>
		<s:else>
			<s:push value="shop">
				<h2><s:text name="msgkey.title.summary"/></h2>
				<table class="ViewTable">
					<tr>
						<th><s:text name="msgkey.label.shopLogo"/></th>
						<td>
							<s:if test="%{logo != null}">
								<%-- TODO: Duplication: Extract to include file? E.g. "print-shop-logo-s200.jsp". --%>
								<img src="<%= request.getContextPath() %>/uploads/images/shop-logos/<s:property value="shop.logo.id + '/s200/' + shop.logo.fileName + '.jpg'"/>">
							</s:if>
							<s:else><img src="<%= request.getContextPath() %>/images/no-shop-logo-s200.png"></s:else>
						</td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.name"/></th>
						<td><s:property value="name"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.summary"/></th>
						<td><s:property value="summary"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.country"/></th>
						<td><s:property value="country"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.currency"/></th>
						<td><s:property value="currency"/></td>
					</tr>
					<tr>
						<%-- TODO: Refactor: Duplication: See "/shop-list.jsp", "/member/shop-list.jsp". --%>
						<th><s:text name="msgkey.label.productCount"/></th>
						<td>
							<s:if test="productCount > 0">
								<s:url namespace="/" action="shop!products" var="shopProductsUrl">
									<s:param name="shopId" value="%{id}"/>
								</s:url>
								<s:a href="%{#shopProductsUrl}"><s:property value="productCount"/></s:a>
							</s:if>
							<s:else>
								<s:property value="productCount"/>
							</s:else>
						</td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.seller"/></th>
						<td>
							<%-- TODO: Duplication: Extract "linkToUserProfile" page decorator. --%>
							<s:url action="user" namespace="/" var="viewUserUrl">
								<s:param name="id" value="user.id"/>
							</s:url>
							<s:a href="%{#viewUserUrl}"><s:property value="user.fullName"/></s:a>
						</td>
					</tr>
				</table>
			</s:push>
			
			<p/>
			<h2><s:text name="msgkey.label.description"/></h2>
			<s:property value="shop.htmlDescription" escape="false"/>
						
			<p/>
			<h2><s:text name="msgkey.title.featuredProducts"/></h2>
			<s:if test="%{isEmpty(featuredProductVariants)}">
				<s:text name="msgkey.text.notExist.product"/>
			</s:if>
			<s:else>
				<table class="ListTable">
					<tr>
						<th><s:text name="msgkey.label.price"/></th>
						<th><s:text name="msgkey.label.image"/></th>
						<th><s:text name="msgkey.label.product"/></th>
					</tr>
					<s:iterator value="featuredProductVariants" status="num">
						<s:url action="product!view" namespace="/" var="viewProductUrl">
							<s:param name="productId" value="%{product.id}"/>
						</s:url>
						<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
							<td class="Price">
								<s:property value="price"/>&nbsp;<s:property value="currency"/>
								<%-- TODO: Need? (n)
								<br/>
								<s:property value="oldPrice"/>&nbsp;<s:property value="currency"/>
								--%>
							</td>
							<td>
								<a class="ThumbnailLink" href="<s:property value="#viewProductUrl"/>">
									<s:if test="titleImage != null">
										<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="titleImage.id + '/medium/' + titleImage.fileName + '.jpg'"/>">
									</s:if>
									<s:else>
										<img src="<%= request.getContextPath() %>/images/no-product-image-s100.png">
									</s:else>
								</a>
							</td>
							<td>
								<s:a cssClass="ProductNameLink" href="%{#viewProductUrl}"><s:property value="fullName"/></s:a>
								<jsp:include page="/WEB-INF/jsp/includes/summaryInListTable.jsp"/>
								<%-- TODO: Impl: Button "Add to Cart". --%>
							</td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			
			<p/>
			<h2><s:text name="msgkey.title.lastAddedProducts"/></h2>
			TODO: Impl: Last 10 or 20.
			
			<p/>
			<%-- TOOD: Low: UI: Add "add" action link if logged in user is shop owner --%>
			<h2><s:text name="msgkey.title.allShopProducts"/></h2>
			<%-- TODO: Impl: Page of products w/ pager? (y). --%>
			<s:url action="shop!products" namespace="/" var="shopProductsUrl">
				<s:param name="shopId" value="%{shop.id}"/>
			</s:url>
			<s:a href="%{#shopProductsUrl}"><s:text name="msgkey.link.viewAllShopProducts"/></s:a>
			
			<p/>
			<%-- TOOD: Low: UI: Add "add" action link if logged in user is shop owner --%>
			<h2><s:text name="msgkey.title.categories"/></h2>
			<s:if test="%{isEmpty(categories)}">
				<s:text name="msgkey.text.notExist.category"/>
			</s:if>
			<s:else>
				<table class="ListTable">
					<tr>
						<th><s:text name="msgkey.label.category"/></th>
						<th><s:text name="msgkey.label.productCount"/></th>
					</tr>
					<s:iterator value="categories" status="num">
						<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
							<td>
								<s:url namespace="/" action="shop!products" var="shopCategoryUrl">
									<s:param name="shopId" value="%{shop.id}"/>
									<s:param name="categoryName" value="%{name}"/>
								</s:url>
								<s:a href="%{#shopCategoryUrl}"><s:property value="name"/></s:a>
							</td>
							<td class="Number"><s:property value="productCount"/></td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			
			<s:push value="shop">
				<p/>
				<%-- TOOD: Low: UI: Add "add" action link if logged in user is shop owner --%>
				<h2><s:text name="msgkey.title.shippingMethods"/></h2>
				<s:if test="%{isEmpty(shippingMethods)}">
					<s:text name="msgkey.text.notExist.shippingMethod"/>
				</s:if>
				<s:else>
					<table class="ListTable">
						<tr>
							<th><s:text name="msgkey.label.description"/></th>
							<th><s:text name="msgkey.label.price"/></th>
						</tr>
						<s:iterator value="shippingMethods" status="num">
							<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
								<td>
									<div class="TitleInCell"><s:property value="name"/></div>
									<s:property value="htmlDesc" escape="false"/>
								</td>
								<td class="Price"><s:property value="price"/>&nbsp;<s:property value="currency"/></td>
							</tr>
						</s:iterator>
					</table>
				</s:else>
				
				<p/>
				<%-- TOOD: Low: UI: Add "add" action link if logged in user is shop owner --%>
				<h2><s:text name="msgkey.title.paymentMethods"/></h2>
				<s:if test="%{isEmpty(paymentMethods)}">
					<s:text name="msgkey.text.notExist.paymentMethod"/>
				</s:if>
				<s:else>
					<table class="ListTable">
						<tr>
							<th><s:text name="msgkey.label.description"/></th>
						</tr>
						<s:iterator value="paymentMethods" status="num">
							<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
								<td>
									<div class="TitleInCell"><s:property value="name"/></div>
									<s:property value="htmlDesc" escape="false"/>
								</td>
							</tr>
						</s:iterator>
					</table>
				</s:else>
			</s:push>
		</s:else>
	</body>
</html>