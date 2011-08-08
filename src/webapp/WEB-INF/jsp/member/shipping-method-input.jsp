<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<%-- TODO: Rethink: Duplication: See PaymentMethod views. --%>

<html>
	<head>
		<s:if test="newEntity">
			<title><s:text name="msgkey.title.seller.newShippingMethod"/></title>
		</s:if>
		<s:else>
			<title>Edit shipping method <s:property value="shippingMethod.name"/></title>
		</s:else>
	</head>
	<body>
		<s:if test="id != null && shippingMethod == null">
			<s:text name="msgkey.text.notFound.shippingMethod"/>
		</s:if>
		<s:else>
			<s:form action="shipping-method" namespace="/member">
				
				<%-- TODO: Enhancement: print link "Edit" for selected shop. (Updated with select.onchange) --%>
				<s:select name="shopId" required="true" 
					list="getShops()" listKey="id" listValue="name + ' (' + currency + ')'"
					headerKey="" headerValue="- Select shop -"
					key="msgkey.label.shop"/>
				
				<s:textfield id="shippingMethod_name" name="shippingMethod.name" required="true" 
					key="msgkey.label.name"/>
				
				<%-- TODO: Update "currency" w/ jQuery from selected shop. --%>
				<h:formField fieldName="shippingMethod.price" inputId="shippingMethod_price"
						labelKey="msgkey.label.price">
					<s:textfield name="shippingMethod.price" required="true" theme="simple"/>&nbsp;
					<s:property value="shippingMethod.currency"/>
				</h:formField>
				
				<%-- TODO: Low: Wrap by FCKeditor? (mb, y) --%>
				<s:textarea id="shippingMethod_description" cssClass="SmallHtmlEditorArea"
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
						<s:url action="shipping-method!deleteConfirm" namespace="/member" var="deleteUrl">
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
					focusElemIfEmpty("shippingMethod_name");
				});
			</script>
		</content>
	</body>
</html>