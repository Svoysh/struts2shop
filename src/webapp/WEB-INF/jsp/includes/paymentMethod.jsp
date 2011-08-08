<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- NOTE: "#paymentMethod" var must be defined in OGNL value stack. --%>

<s:if test="%{#paymentMethod != null}">
	<s:push value="#paymentMethod">
		<table class="ViewTable">
				<tr>
					<th><s:text name="msgkey.label.name"/></th>
					<td><s:property value="name"/></td>
				</tr>
				<tr>
					<th><s:text name="msgkey.label.description"/></th>
					<td><s:property value="htmlDesc" escape="false"/></td>
				</tr>
			</table>
	</s:push>
</s:if>