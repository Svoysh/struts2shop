<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<%-- TODO: i18n --%>
		<title>Category <s:property value="category.name"/></title>
	</head>
	<body>
		<s:if test="category == null">
			<%-- TODO: Print this text in title instead. --%>
			<%-- TODO: Print the link to Category list. --%>
			<s:text name="msgkey.text.notFound.category"/>
		</s:if>
		<s:else>
			<s:push value="category">
				<table class="ViewTable">
					<tr>
						<th><s:text name="msgkey.label.id"/></th>
						<td><s:property value="id"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.name"/></th>
						<td><s:property value="name"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.productCount"/></th>
						<td><s:property value="productCount"/></td>
					</tr>
					<%-- TODO: Print seller link:
					<tr>
						<th><s:text name="msgkey.label.seller"/></th>
						<td><s:property value="seller"/></td>
					</tr> 
					--%>
					<%-- TODO: Created --%>
					<tr>
						<th><s:text name="msgkey.label.actions"/></th>
						<td>
							<s:url action="category!edit" namespace="/member" var="editCategoryUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="category!deleteConfirm" namespace="/member" var="deleteCategoryUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							
							<%-- TODO: Refactor: Replace by link to "Products by user ID and category name". --%>
							<s:url action="category!products" namespace="/" var="categoryProductsUrl">
								<s:param name="categoryName" value="%{name}"/>
							</s:url>
							
							<s:a href="%{#editCategoryUrl}"><s:text name="msgkey.action.edit"/></s:a>,
							<s:a href="%{#deleteCategoryUrl}"><s:text name="msgkey.action.delete"/></s:a>,
							<s:a href="%{#categoryProductsUrl}"><s:text name="msgkey.link.viewInAction"/></s:a>
						</td>
					</tr>
				</table>
			</s:push>
		</s:else>
	</body>
</html>