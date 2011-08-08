<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<%-- TODO: Rethink: Duplication: See PaymentMethod views. --%>

<html>
	<head>
		<title>Shipping method list</title>
	</head>
	<body>
		<s:if test="!isEmpty(shippingMethods)">
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th><s:text name="msgkey.label.name"/></th>
					<th><s:text name="msgkey.label.price"/></th>
					<th><s:text name="msgkey.label.shop"/></th>
					<%-- TODO: Toggle text description with jQuery. See Blogger's post list. --%>
					<th><s:text name="msgkey.label.created"/></th>
					<th><s:text name="msgkey.label.updated"/></th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="shippingMethods" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td>
							<s:url action="shipping-method" namespace="/member" var="shippingMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:a href="%{#shippingMethodUrl}"><s:property value="name"/></s:a>
						</td>
						<td class="Price"><s:property value="price"/>&nbsp;<s:property value="currency"/></td>
						<td>
							<s:url action="shop" namespace="/member" var="shopUrl">
								<s:param name="id" value="%{shop.id}"/>
							</s:url>
							<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
						</td>
						<td class="Date"><s:date name="created"/></td>
						<td class="Date"><s:date name="updated"/></td>
						<td class="Actions">
							<s:url action="shipping-method" namespace="/member" var="viewShippingMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="shipping-method!edit" namespace="/member" var="editShippingMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="shipping-method!deleteConfirm" namespace="/member" var="deleteShippingMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<ul>
								<li><s:a href="%{#viewShippingMethodUrl}"><s:text name="msgkey.action.view"/></s:a></li>
								<li><s:a href="%{#editShippingMethodUrl}"><s:text name="msgkey.action.edit"/></s:a></li>
								<li><s:a href="%{#deleteShippingMethodUrl}"><s:text name="msgkey.action.delete"/></s:a></li>
							</ul>
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:if>
		<s:else>
			No shipping method exists.
		</s:else>
	</body>
</html>
