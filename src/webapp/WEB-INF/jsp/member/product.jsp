<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<%-- TODO: i18n --%>
		<title>Product <s:property value="product.name"/></title>
	</head>
	<body>
		<s:if test="product == null">
			<%-- TODO: Print this text in title instead. --%>
			<%-- TODO: Print the link to Product list. --%>
			<s:text name="msgkey.text.notFound.product"/>
		</s:if>
		<s:else>
			<s:push value="product">
				<table class="ViewTable">
					<tr>
						<th><s:text name="msgkey.label.id"/></th>
						<td><s:property value="id"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.titleImage"/></th>
						<td>TODO</td>
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
						<th><s:text name="msgkey.label.description"/></th>
						<td><s:property value="htmlDescription" escape="false"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.productVariants"/></th>
						<td>
							<s:if test="!isEmpty(productVariants)">
								<ul>
									<s:iterator value="productVariants">
										<%-- TODO: Link to product variant. --%>
										<%-- TODO: UI: Make more beauty. --%>
										<li><s:property value="name"/>&nbsp;<s:property value="price"/>&nbsp;<s:property value="currency"/></li>
									</s:iterator>
								</ul>
							</s:if>
						</td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.categories"/></th>
						<td>
							<s:if test="!isEmpty(productCategories)">
								<ul>
									<s:iterator value="productCategories">
										<%-- TODO: Link to category. --%>
										<li><s:property value="category.name"/></li>
									</s:iterator>
								</ul>
							</s:if>
						</td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.shop"/></th>
						<td>
							<s:url action="shop" namespace="/member" var="shopUrl">
								<s:param name="id" value="%{shop.id}"/>
							</s:url>
							<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
						</td>
					</tr>
					<%-- TODO: Created --%>
					<tr>
						<th><s:text name="msgkey.label.actions"/></th>
						<td>
							<s:url action="product!edit" namespace="/member" var="editProductUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="product!deleteConfirm" namespace="/member" var="deleteProductUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="product!view" namespace="/" var="viewProductUrl">
								<s:param name="productId" value="%{id}"/>
							</s:url>
									
							<s:a href="%{#editProductUrl}"><s:text name="msgkey.action.edit"/></s:a>,
							<s:a href="%{#deleteProductUrl}"><s:text name="msgkey.action.delete"/></s:a>,
							<s:a href="%{#viewProductUrl}"><s:text name="msgkey.link.viewInAction"/></s:a>
						</td>
					</tr>
				</table>
			</s:push>
		</s:else>
	</body>
</html>