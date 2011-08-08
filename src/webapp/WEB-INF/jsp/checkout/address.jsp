<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<s:if test="%{newAddress}"><title><s:text name="msgkey.title.newAddress"/></title></s:if>
		<s:else><title><s:text name="msgkey.title.editAddress"/></title></s:else>
	</head>
	<body>
		<s:form namespace="/checkout" action="address">
			<s:hidden name="checkoutId" value="%{checkoutId}"/>
			<s:hidden name="returnTo" value="%{returnTo}"/>
			
			<s:textfield key="msgkey.label.address.name" name="address.name"/>
			<s:select id="address_country" required="true" 
				key="msgkey.label.address.country" name="address.country"
				list="countries" listKey="code" listValue="fullName"
				headerKey="" headerValue="- Select country -"/>
			<s:textfield key="msgkey.label.address.street" name="address.street" required="true"/>
			<s:textfield key="msgkey.label.address.street2" name="address.street2"/>
			<s:textfield key="msgkey.label.address.city" name="address.city" required="true"/>
			<s:textfield key="msgkey.label.address.state" name="address.state"/>
			<s:textfield key="msgkey.label.address.postalCode" name="address.postalCode"/>
			<s:textfield key="msgkey.label.address.phone" name="address.phone"/>
			<s:textfield key="msgkey.label.email" name="address.email"/>
			
			<%-- TODO: Low: Add Google Map here. Set address' latitude & longtitude. --%>
			
			<h:formControlPanel>
				<s:if test="%{newAddress}">
					<s:submit cssClass="MainButton" name="method:save" key="msgkey.action.save"/>
				</s:if>
				<s:else>
					<s:hidden name="addressId" value="%{addressId}"/>
					<s:submit cssClass="MainButton" name="method:update" key="msgkey.action.update"/>
				</s:else>
				<%-- TODO: Need here "Back" batton? (mb, see on practice) --%>
			</h:formControlPanel>
		</s:form>
		<content tag="lastJavaScript">
			<script type="text/javascript">
				$(document).ready(function()
				{
					focusElemIfEmpty("address_country");
				});
			</script>
		</content>
	</body>
</html>