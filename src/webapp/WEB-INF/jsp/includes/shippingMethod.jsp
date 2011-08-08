<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- NOTE: "#shippingMethod" var must be defined in OGNL value stack. --%>

<s:if test="%{#shippingMethod != null}">
	<s:push value="#shippingMethod">
		<table class="ViewTable">
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
					<td><s:property value="htmlDesc" escape="false"/></td>
				</tr>
			</table>
	</s:push>
</s:if>