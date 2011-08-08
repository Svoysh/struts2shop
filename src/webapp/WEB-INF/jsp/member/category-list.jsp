<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title>Category list</title>
	</head>
	<body>
		<s:if test="!isEmpty(categories)">
			<a href="<%= request.getContextPath() %>/member/category!add">Add category</a>
			<p/>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th><s:text name="msgkey.label.name"/></th>
					<th><s:text name="msgkey.label.productCount"/></th>
					<%-- TODO: <th><s:text name="msgkey.label.seller"/></th> --%>
					<th><s:text name="msgkey.label.created"/></th>
					<th><s:text name="msgkey.label.updated"/></th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="categories" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td>
							<s:url action="category" namespace="/member" var="categoryUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:a href="%{#categoryUrl}"><s:property value="name"/></s:a>
						</td>
						<td class="Number"><s:property value="productCount"/></td>
						<%--TODO: <td>Link to <s:property value="seller.name"/></td> --%>
						<td class="Date"><s:date name="created"/></td>
						<td class="Date"><s:date name="updated"/></td>
						<td class="Actions">
							<s:url action="category" namespace="/member" var="viewCategoryUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="category!edit" namespace="/member" var="editCategoryUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="category!deleteConfirm" namespace="/member" var="deleteCategoryUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="product!add" namespace="/member" var="addProductUrl">
								<s:param name="categoryName" value="%{name}"/>
							</s:url>
							<ul>
								<li><s:a href="%{#viewCategoryUrl}"><s:text name="msgkey.action.view"/></s:a></li>
								<li><s:a href="%{#editCategoryUrl}"><s:text name="msgkey.action.edit"/></s:a></li>
								<li><s:a href="%{#deleteCategoryUrl}"><s:text name="msgkey.action.delete"/></s:a></li>
								<li><s:a href="%{#addProductUrl}">TODO: Struts Action: Add product</s:a></li>
							</ul>
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:if>
		<s:else>
			<s:text name="msgkey.text.notExist.category"/>
		</s:else>
		<p/>
		<a href="<%= request.getContextPath() %>/member/category!add">Add category</a>
	</body>
</html>
