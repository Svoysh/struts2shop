<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title>
			<s:if test="%{product != null}">
				<s:property value="product.name"/>
			</s:if>
			<s:else>
				Product not found
			</s:else>
		</title>
	</head>
	<body>
		<s:if test="%{product != null}">
			<s:push value="product">
				<table class="HiddenTable">
					<tr class="TopRow">
						<td>
							<h2><s:text name="msgkey.title.images"/></h2>
							<s:if test="titleImage != null">
								<%-- TODO: Low: Integrate images w/ Fancybox jQuery plugin. --%>
								<table>
									<tr>
										<td>
											<s:iterator value="productImages" status="num">
												<a class="ThumbnailLink" href="<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id + '/' + image.fileName"/>"
													onmouseover="changeLargeThumbUrl('<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id"/>', '<s:property value="image.fileName"/>');">
													<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="image.id + '/small/' + image.fileName + '.jpg'"/>">
												</a><s:if test="!#num.last">&nbsp;</s:if>
											</s:iterator>
										</td>
									</tr>
									<tr>
										<td>
											<%-- CSS class "LargeThumbA" and "LargeThumbImg" used by jQuery to load large product image on mouser over event. --%>
											<a class="LargeThumbA ThumbnailLink" href="<%= request.getContextPath() %>/uploads/images/products/<s:property value="titleImage.id + '/' + titleImage.fileName"/>">
												<table><tr>
													<td class="LargeThumbFrame">
													<%-- TODO: Duplication: Extract to include file? E.g. "print-product-image-s300.jsp". --%>
													<img class="LargeThumbImg" src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="titleImage.id + '/large/' + titleImage.fileName + '.jpg'"/>">
													</td>
												</tr>
												</table>
											</a>
										</td>
									</tr>
								</table>
							</s:if>
							<s:else>
								<%-- TODO: Fix: Enhance. --%>
								<img src="<%= request.getContextPath() %>/images/no-product-image-s300.png">
							</s:else>
						</td>
						<td style="padding-left: 6px;">
							<h2><s:text name="msgkey.title.summary"/></h2>
							<s:property value="summary"/>
							
							<h2><s:text name="msgkey.title.productVariants"/></h2>
							<s:if test="%{!isEmpty(productVariants)}">
								<table class="ListTable">
									<tr>
										<th><s:text name="msgkey.label.productVariantName"/></th>
										<th><s:text name="msgkey.label.oldPrice"/></th>
										<th><s:text name="msgkey.label.price"/></th>
										<th><s:text name="msgkey.label.actions"/></th>
									</tr>
									<s:set var="i" value="1"/>
									<s:iterator value="productVariants" status="num">
										<s:if test="%{inStock}">
											<tr class="<s:property value="%{(#i % 2 != 0) ? 'Odd' : 'Even'}"/>">
												<td><s:property value="name"/></td>
												<td class="OldPrice">
													<s:if test="%{oldPrice != null}">
														<s:property value="oldPrice"/>&nbsp;<s:property value="currency"/>
													</s:if>
												</td>
												<td class="Price">
													<s:if test="%{price != null}">
														<s:property value="price"/>&nbsp;<s:property value="currency"/>
													</s:if>
												</td>
												<td class="AddToCartBlock">
													<s:if test="%{inStock}">
														<s:form namespace="/ajax" action="cart!addProductVariant" 
																theme="simple" onsubmit="return addToCart(this);">
															<s:hidden name="productVariantId" value="%{id}"/>
															<s:textfield cssClass="QuantityInput" name="quantity" value="1"/>
															<s:submit cssClass="CartButton" key="msgkey.action.addToCart"/>
														</s:form>
													</s:if>
												</td>
											</tr>
											<s:set var="i" value="#i + 1"/>
										</s:if>
									</s:iterator>
								</table>
							</s:if>
							<s:else><s:text name="msgkey.text.cannotBeAddedToCartBecauseProductHasNoVariants"/></s:else>
						</td>
					</tr>
				</table>
				
				<%-- TODO: Low: Maybe print desc into iframe to prevent overlapping of webapp CSS with desc CSS. --%>
				<h2><s:text name="msgkey.title.description"/></h2>
				<s:property value="htmlDescription" escape="false"/>
				
				<h2><s:text name="msgkey.title.shop"/></h2>
				<%-- TODO: Print shop logo and summary. See shop view. --%>
				<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
				<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
				
				<h2><s:text name="msgkey.title.categories"/></h2>
				<s:if test="%{!isEmpty(productCategories)}">
					<%-- FIXME: Categories must be sorted, but now (2009-07-01) are not. --%>
					<s:iterator value="productCategories" var="category" status="i">
						<s:url action="shop!products" var="viewCategoryUrl">
							<s:param name="shopId" value="%{shop.id}"/>
							<s:param name="categoryName" value="%{category.name}"/>
						</s:url>
						<s:a href="%{#viewCategoryUrl}"><s:property value="category.name"/></s:a><s:if test="%{!#i.last}">,&nbsp;</s:if>
					</s:iterator>
				</s:if>
				<s:else>
					<%-- TODO: i18n --%>
					No categories.
				</s:else>
				
			<%-- TODO: Links to prev and next products. --%>
			<%-- TODO: Print related products. --%>
			</s:push>
			
			<content tag="lastJavaScript">
				<script type="text/javascript">
					var $largeThumbA;
					var $largeThumbImg;
					$(document).ready(function()
					{
						$largeThumbA = $(".LargeThumbA");
						$largeThumbImg = $(".LargeThumbImg");
					});

					/**
					 * AJAX submit of form "Add to Cart".
					 * 
					 * @param Element form - form element to submit.
					 */
					function addToCart(form)
					{
						// TODO: Show feed back: "Adding..." and after signalize 
						//       that added: show popup w/ image of added product? (y)
						//       See jQuery plugin: jGrowl, for feedback popups.
						return nonCachingFormSubmit(form, "#cartPreview");
					}
					
					/**
					 * @param String imageBaseUrl;
					 * @param String imageName;
					 */
					function changeLargeThumbUrl(imageBaseUrl, imageName)
					{
						$largeThumbImg.attr("src", imageBaseUrl + "/large/" + imageName + ".jpg");
						$largeThumbA.attr("href", imageBaseUrl + "/" + imageName);

						// TODO: Switch "Selected" CSS class for overed small thumb. See black "CurrentPage" of pager.
					}
				</script>
			</content>
		</s:if>
	</body>
</html>