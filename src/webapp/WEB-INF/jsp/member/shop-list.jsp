<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title>Shop list</title>
	</head>
	<body>
		<s:if test="!isEmpty(shops)">
			<s:url var="addShopUrl" namespace="/member" action="shop" method="add"></s:url>
			<s:a href="%{#addShopUrl}"><s:property value="name"/></s:a>
			<a href="<%= request.getContextPath() %>/member/shop!add">Add shop</a>
			<p/>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th><s:text name="msgkey.label.shopLogo"/></th>
					<th><s:text name="msgkey.label.name"/></th>
					<th><s:text name="msgkey.label.country"/></th>
					<th><s:text name="msgkey.label.currency"/></th>
					<th><s:text name="msgkey.label.productCount"/></th>
					<th><s:text name="msgkey.label.created"/></th>
					<th><s:text name="msgkey.label.updated"/></th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="shops" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td>TODO</td>
						<td>
							<s:url action="shop" namespace="/member" var="shopUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:a href="%{#shopUrl}"><s:property value="name"/></s:a>
						</td>
						<td class="Country"><s:property value="country"/></td>
						<td class="Currency"><s:property value="currency"/></td>
						<td class="Number">
							<%-- TODO: Refactor: Duplication: See "/shop-list.jsp", "/member/shop.jsp". --%>
							<s:if test="productCount > 0">
								<s:url action="product!list" namespace="/member" var="productsByShopIdUrl">
									<s:param name="shopId" value="%{id}"/>
								</s:url>
								<s:a href="%{#productsByShopIdUrl}"><s:property value="productCount"/></s:a>
							</s:if>
							<s:else>
								<s:property value="productCount"/>
							</s:else>
						</td>
						<td class="Date"><s:date name="created"/></td>
						<td class="Date"><s:date name="updated"/></td>
						<td class="Actions">
							<s:url action="shop" namespace="/member" var="viewShopUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="shop!edit" namespace="/member" var="editShopUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="shop!deleteConfirm" namespace="/member" var="deleteShopUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="product!add" namespace="/member" var="addProductUrl">
								<s:param name="shopId" value="%{id}"/>
							</s:url>
							<ul>
								<li><s:a href="%{#viewShopUrl}"><s:text name="msgkey.action.view"/></s:a></li>
								<li><s:a href="%{#editShopUrl}"><s:text name="msgkey.action.edit"/></s:a></li>
								<li><s:a href="%{#deleteShopUrl}"><s:text name="msgkey.action.delete"/></s:a></li>
								<li><s:a href="%{#addProductUrl}">Add product</s:a></li>
							</ul>
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:if>
		<s:else>
			<s:text name="msgkey.text.notExist.shop"/>
		</s:else>
		<p/>
		<a href="<%= request.getContextPath() %>/member/shop!add">Add shop</a>
	</body>
</html>
