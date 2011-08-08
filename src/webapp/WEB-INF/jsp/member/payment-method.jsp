<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: Rethink: Duplication: See ShippingMethod views. --%>

<html>
	<head>
		<%-- TODO: i18n --%>
		<title>Payment method <s:property value="paymentMethod.name"/></title>
	</head>
	<body>
		<s:if test="paymentMethod == null">
			<%-- TODO: Print this text in title instead. --%>
			<%-- TODO: Print the link to Payment method list. --%>
			<s:text name="msgkey.text.notFound.paymentMethod"/>
		</s:if>
		<s:else>
			<s:push value="paymentMethod">
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
							<s:url action="payment-method!edit" namespace="/member" var="editPaymentMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="payment-method!deleteConfirm" namespace="/member" var="deletePaymentMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							
							<s:a href="%{#editPaymentMethodUrl}"><s:text name="msgkey.action.edit"/></s:a>,
							<s:a href="%{#deletePaymentMethodUrl}"><s:text name="msgkey.action.delete"/></s:a>
						</td>
					</tr>
				</table>
			</s:push>
		</s:else>
	</body>
</html>