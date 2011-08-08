<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- NOTE: "#address" var must be defined in OGNL value stack. --%>

<s:if test="%{#address != null}">
	<s:push value="#address">
		<table class="ViewTable">
			<tr>
				<th><s:text name="msgkey.label.address.name"/></th>
				<td><s:property value="name"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.street"/></th>
				<td><s:property value="street"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.street2"/></th>
				<td><s:property value="street2"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.city"/></th>
				<td><s:property value="city"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.state"/></th>
				<td><s:property value="state"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.country"/></th>
				<td><s:property value="countryName"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.postalCode"/></th>
				<td><s:property value="postalCode"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.phone"/></th>
				<td><s:property value="phone"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.email"/></th>
				<td><s:property value="email"/></td>
			</tr>
			<%-- TODO: Need next commented block? (n) Delete. --%>
			<%--
			<tr>
				<th><s:text name="msgkey.label.address.latitude"/></th>
				<td><s:property value="latitude"/></td>
			</tr>
			<tr>
				<th><s:text name="msgkey.label.address.longitude"/></th>
				<td><s:property value="longitude"/></td>
			</tr>
			--%>
		</table>
	</s:push>
</s:if>