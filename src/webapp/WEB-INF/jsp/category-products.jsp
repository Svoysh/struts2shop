<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<%-- TODO: Refactor: Has duplications: See "/shop-products.jsp". --%>
<html>
	<head>
		<%-- TODO: Grammar: "Products OF category" or "Products FROM category"? --%>
		<title>Products by category <s:property value="category.name"/></title>
	</head>
	<body>
		<%-- TODO: Add pager. --%>
		<%-- TODO: Move these if/elses to title. --%>
		<s:if test="category == null">
			<h1>Category not found</h1>
			Such category does not exist or was removed.
		</s:if>
		<s:elseif test="isEmpty(products)">
			<h1>No product exists</h1>
			<%-- TODO: Low: Create link "Add product to category". --%>
		</s:elseif>
		<s:else>
			<table class="ContentTable">
				<tr>
					<td class="LeftContentColumn">
						<div class="LeftPanel">
							<h2 class="LeftPanelCaption">Product categories</h2>
							<div class="LeftPanelBody">
								<table class="CategoryTable">
									<s:if test="isEmpty(categories)">
										<tr><td class="Odd"><s:text name="msgkey.text.notExist.category"/></td></tr>
									</s:if>
									<s:else>
										<s:iterator value="categories" status="row">
											<tr class="<s:property value="#row.odd ? 'Odd' : 'Even'"/>">
												<td>
													<s:url action="category!products" namespace="/" var="categoryProductsUrl">
														<s:param name="categoryName" value="%{name}"/>
														<s:param name="page" value="%{pager.page}"/>
														<s:param name="results" value="%{pager.maxResults}"/>
													</s:url>
													<s:if test="categoryName != null && name.equalsIgnoreCase(categoryName)">
														<s:a href="%{#categoryProductsUrl}"><strong><s:property value="name"/></strong></s:a>
													</s:if>
													<s:else>
														<s:a href="%{#categoryProductsUrl}"><s:property value="name"/></s:a>
													</s:else>
												</td>
												<td class="ProductCount"><s:property value="productCount"/></td>
											</tr>
										</s:iterator>
									</s:else>
								</table>
							</div>
						</div>
					</td>
					
					<%-- TODO: Refactor: Product list duplication in "shop-products.jsp". --%>
					<td class="CenterContentColumn">
						<f:topPager/>
						
						<s:set var="first" value="%{pager.firstResultNumber}"/>
						<s:set var="last" value="%{pager.lastResultNumber}"/>
						
						<h2 class="TableHeader">Products</h2>
						<div class="ProductList">
							<table class="ProductTable">
								<s:iterator value="%{products}" status="row">
								
									<s:if test="%{#row.first}">
										<s:set var="rowClass" value="'FirstProductRow'"/>
									</s:if>
									<s:elseif test="%{#row.odd}">
										<s:set var="rowClass" value="'OddProductRow'"/>
									</s:elseif>
									<s:elseif test="%{#row.even}">
										<s:set var="rowClass" value="'EvenProductRow'"/>
									</s:elseif>
									
									<%-- TODO: Low: Enhance: Print actions for logged in user and for product owner. --%>
									
									<s:url action="product!view" namespace="/" var="viewProductUrl">
										<s:param name="productId" value="%{id}"/>
									</s:url>
					
									<tr class="<s:property value="#rowClass"/>">
										<td class="LeftProductCell">
											<span class="RowNumber"><s:property value="#first + #row.index"/>.</span>
										</td>
										<td class="ThumbnailCell">
											<s:if test="titleImage != null">
												<a class="ThumbnailLink" href="<s:property value="#viewProductUrl"/>">
													<img src="<%= request.getContextPath() %>/uploads/images/products/<s:property value="titleImage.id + '/medium/' + titleImage.fileName + '.jpg'"/>">
												</a>
											</s:if>
											<s:else>TODO: No image</s:else>
										</td>
										<td>
											<div class="ProductNameBlock">
												<s:a cssClass="ProductNameLink" href="%{#viewProductUrl}"><s:property value="name"/></s:a>
											</div>
											<jsp:include page="/WEB-INF/jsp/includes/summaryInListTable.jsp"/>
											
											<%-- TODO: Fix & Update to Seam. --%>
											<%--
											<ui:include src="layout/addToCart.xhtml"/>
											--%>
										</td>
										<td class="RightProductCell">
											<div class="Price">
												<s:if test="%{(minPrice != null) and (minPrice == maxPrice)}">
													<s:property value="minPrice"/>&nbsp;<s:property value="currency"/>
												</s:if>
												<s:elseif test="%{(minPrice != null) and (maxPrice != null)}">
													<%-- Use &#150; (short dash) or &#151; (long dash) as price separator. --%>
													<s:property value="minPrice"/>&nbsp;<s:property value="currency"/> &#150; 
													<s:property value="maxPrice"/>&nbsp;<s:property value="currency"/>
												</s:elseif>
												<s:else>
													<%-- TODO: What if product has no price? Print "-"? --%>
												</s:else>
											</div>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<f:bottomPager/>
					</td>
				</tr>
			</table>
		</s:else>
	</body>
</html>