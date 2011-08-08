<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<%-- TODO: Rethink: Duplication: See ShippingMethod views. --%>

<html>
	<head>
		<title>Payment method list</title>
	</head>
	<body>
		<s:if test="!isEmpty(paymentMethods)">
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th><s:text name="msgkey.label.name"/></th>
					<th><s:text name="msgkey.label.shop"/></th>
					<%-- TODO: Toggle text description with jQuery. See Blogger's post list. --%>
					<th><s:text name="msgkey.label.created"/></th>
					<th><s:text name="msgkey.label.updated"/></th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="paymentMethods" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td>
							<s:url action="payment-method" namespace="/member" var="paymentMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:a href="%{#paymentMethodUrl}"><s:property value="name"/></s:a>
						</td>
						<td>
							<s:url action="shop" namespace="/member" var="shopUrl">
								<s:param name="id" value="%{shop.id}"/>
							</s:url>
							<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
						</td>
						<td class="Date"><s:date name="created"/></td>
						<td class="Date"><s:date name="updated"/></td>
						<td class="Actions">
							<s:url action="payment-method" namespace="/member" var="viewPaymentMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="payment-method!edit" namespace="/member" var="editPaymentMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<s:url action="payment-method!deleteConfirm" namespace="/member" var="deletePaymentMethodUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<ul>
								<li><s:a href="%{#viewPaymentMethodUrl}"><s:text name="msgkey.action.view"/></s:a></li>
								<li><s:a href="%{#editPaymentMethodUrl}"><s:text name="msgkey.action.edit"/></s:a></li>
								<li><s:a href="%{#deletePaymentMethodUrl}"><s:text name="msgkey.action.delete"/></s:a></li>
							</ul>
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:if>
		<s:else>
			No payment method exists.
		</s:else>
	</body>
</html>
