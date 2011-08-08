<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<%-- TODO: Rethink: Duplication: See ShippingMethod views. --%>

<html>
	<head>
		<s:if test="newEntity">
			<title><s:text name="msgkey.title.seller.newPaymentMethod"/></title>
		</s:if>
		<s:else>
			<title>Edit payment method <s:property value="paymentMethod.name"/></title>
		</s:else>
	</head>
	<body>
		<s:if test="id != null && paymentMethod == null">
			<s:text name="msgkey.text.notFound.paymentMethod"/>
		</s:if>
		<s:else>
			<s:form action="payment-method" namespace="/member">
				
				<%-- TODO: Enhancement: print link "Edit" for selected shop. (Updated with select.onchange) --%>
				<s:select name="shopId" required="true" 
					list="getShops()" listKey="id" listValue="name + ' (' + currency + ')'"
					headerKey="" headerValue="- Select shop -"
					key="msgkey.label.shop"/>
				
				<s:textfield id="paymentMethod_name" name="paymentMethod.name" required="true" 
					key="msgkey.label.name"/>
				
				<%-- TODO: Low: Wrap by FCKeditor? (mb, y) --%>
				<s:textarea id="paymentMethod_description" cssClass="SmallHtmlEditorArea"
					name="description" key="msgkey.label.description"
					rows="10" cols="60"/>
				
				<h:formControlPanel>
					<s:if test="newEntity">
						<s:submit cssClass="MainButton" name="method:save" key="msgkey.action.save"/>
						<s:submit name="method:saveAdd" key="msgkey.action.saveAndAddMore"/>
					</s:if>
					<s:else>
						<s:hidden name="id" value="%{id}"/>
						<s:submit cssClass="MainButton" name="method:update" key="msgkey.action.update"/>
						<s:url action="payment-method!deleteConfirm" namespace="/member" var="deleteUrl">
							<s:param name="id" value="%{id}"/>
						</s:url>
						<s:a href="%{#deleteUrl}"><s:text name="msgkey.action.delete"/></s:a>
					</s:else>
				</h:formControlPanel>
			</s:form>
		</s:else>
		<content tag="lastJavaScript">
			<script type="text/javascript">
				$(document).ready(function()
				{
					focusElemIfEmpty("paymentMethod_name");
				});
			</script>
		</content>
	</body>
</html>