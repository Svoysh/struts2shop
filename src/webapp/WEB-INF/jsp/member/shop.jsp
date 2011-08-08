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
			<%-- TODO: Print the link to Shop list. --%>
			<s:text name="msgkey.text.notFound.shop"/>
		</s:if>
		<s:else>
			<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
			<s:push value="shop">
				<table class="ViewTable">
					<tr>
						<th><s:text name="msgkey.label.id"/></th>
						<td><s:property value="id"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.shopLogo"/></th>
						<td>
							<s:if test="%{logo != null}">
								<%-- TODO: Duplication: Extract to include file? E.g. "print-shop-logo-s200.jsp". --%>
								<img src="<%= request.getContextPath() %>/uploads/images/shop-logos/<s:property value="shop.logo.id + '/s200/' + shop.logo.fileName + '.jpg'"/>">
							</s:if>
							<s:else>TODO: No logo.</s:else>
						</td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.name"/></th>
						<td><s:property value="name"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.urlName"/></th>
						<td class="UrlNameBlock">
							<s:if test="%{!isEmpty(urlName)}">
								<s:a href="%{#shopUrl}"><s:property value="%{urlBuilder.viewShopFullBase}"/>/<strong><s:property value="urlName"/></strong></s:a>
							</s:if>
						</td>
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
						<th><s:text name="msgkey.label.summary"/></th>
						<td><s:property value="summary"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.description"/></th>
						<td><s:property value="htmlDescription" escape="false"/></td>
					</tr>
					<tr>
						<%-- TODO: Refactor: Duplication: See "/shop-list.jsp", "/member/shop-list.jsp". --%>
						<th><s:text name="msgkey.label.productCount"/></th>
						<td>
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
							<s:url action="shop!edit" namespace="/member" var="editShopUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="shop!deleteConfirm" namespace="/member" var="deleteShopUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							
							<s:a href="%{#editShopUrl}"><s:text name="msgkey.action.edit"/></s:a>,
							<s:a href="%{#deleteShopUrl}"><s:text name="msgkey.action.delete"/></s:a>,
							<s:a href="%{#shopUrl}"><s:text name="msgkey.link.viewInAction"/></s:a>
						</td>
					</tr>
				</table>
			</s:push>
		</s:else>
	</body>
</html>