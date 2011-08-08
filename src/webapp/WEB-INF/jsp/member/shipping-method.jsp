<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Rethink: Duplication: See PaymentMethod views. --%>

<html>
	<head>
		<%-- TODO: i18n --%>
		<title>Shipping method <s:property value="shippingMethod.name"/></title>
	</head>
	<body>
		<s:if test="shippingMethod == null">
			<%-- TODO: Print this text in title instead. --%>
			<%-- TODO: Print the link to Shipping method list. --%>
			<s:text name="msgkey.text.notFound.shippingMethod"/>
		</s:if>
		<s:else>
			<s:push value="shippingMethod">
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
						<th><s:text name="msgkey.label.price"/></th>
						<td><s:property value="price"/>&nbsp;<s:property value="currency"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.description"/></th>
						<td><s:property value="htmlDescription" escape="false"/></td>
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
							<s:url action="shipping-method!edit" namespace="/member" var="editShippingMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="shipping-method!deleteConfirm" namespace="/member" var="deleteShippingMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							
							<s:a href="%{#editShippingMethodUrl}"><s:text name="msgkey.action.edit"/></s:a>,
							<s:a href="%{#deleteShippingMethodUrl}"><s:text name="msgkey.action.delete"/></s:a>
						</td>
					</tr>
				</table>
			</s:push>
		</s:else>
	</body>
</html>