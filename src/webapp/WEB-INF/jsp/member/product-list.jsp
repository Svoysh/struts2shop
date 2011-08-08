<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<s:if test="%{shop != null}">
			<title>Product list by shop "<s:property value="%{shop.name}"/>"</title>
		</s:if>
		<s:else>
			<title>Product list</title>
		</s:else>
	</head>
	<body>
		<s:if test="!isEmpty(products)">
			<a href="<%= request.getContextPath() %>/member/product!add">Add product</a>
			<p/>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th>Title image</th>
					<th><s:text name="msgkey.label.name"/></th>
					<th><s:text name="msgkey.label.price"/></th>
					<%-- TODO: Toggle text description with jQuery. See Blogger's post list. --%>
					<th><s:text name="msgkey.label.shop"/></th>
					<th><s:text name="msgkey.label.created"/></th>
					<th><s:text name="msgkey.label.updated"/></th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="products" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td>TODO</td>
						<td>
							<s:url action="product" namespace="/member" var="productUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:a href="%{#productUrl}"><s:property value="name"/></s:a>
						</td>
						<td>TODO</td>
						<td>
							<s:url action="shop" namespace="/member" var="shopUrl">
								<s:param name="id" value="%{shop.id}"/>
							</s:url>
							<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
						</td>
						<td class="Date"><s:date name="created"/></td>
						<td class="Date"><s:date name="updated"/></td>
						<td class="Actions">
							<s:url action="product" namespace="/member" var="viewProductUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="product!edit" namespace="/member" var="editProductUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="product!deleteConfirm" namespace="/member" var="deleteProductUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<ul>
								<li><s:a href="%{#viewProductUrl}"><s:text name="msgkey.action.view"/></s:a></li>
								<li><s:a href="%{#editProductUrl}"><s:text name="msgkey.action.edit"/></s:a></li>
								<li><s:a href="%{#deleteProductUrl}"><s:text name="msgkey.action.delete"/></s:a></li>
							</ul>
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:if>
		<s:else>
			<s:text name="msgkey.text.notExist.product"/>
		</s:else>
		<p/>
		<a href="<%= request.getContextPath() %>/member/product!add">Add product</a>
	</body>
</html>
