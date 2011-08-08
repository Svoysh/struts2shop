<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.users"/></title>
	</head>
	<body>
		<s:if test="isEmpty(users)">
			No user exists.
		</s:if>
		<s:else>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<%-- TODO: Enhance: Make sortable headers. --%>
					<%-- TODO: Low: Print row number (from pager) instead of ID. --%>
					<th>Avatar</th>
					<%-- TODO: Low: Rename label to simply "Name"? (y) --%>
					<th><s:text name="msgkey.label.fullName"/></th>
					<%-- TODO: Inline address: Country, city, etc. --%>
					<th><s:text name="msgkey.label.ownedShops"/></th>
					<th>Registered</th>
					<s:if test="loggedIn">
						<th><s:text name="msgkey.label.actions"/></th>
					</s:if>
				</tr>
				<s:iterator value="users" var="user" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td>TODO</td>
						<td>
							<s:url action="user" namespace="/" var="viewUserUrl">
								<s:param name="id" value="%{id}"/>
							</s:url>
							<%-- TODO: Low: Make <strong> if it's current user. --%>
							<s:a href="%{#viewUserUrl}"><s:property value="fullName"/></s:a>
						</td>
						<td class="Number">
							<%-- TODO: Print link to "Shops by user ID" if user owned shops > 0. --%>
							<s:property value="shops != null ? shops.size : 0"/>
						</td>
						<td class="Date"><s:date name="created"/></td>
						<s:if test="loggedIn">
							<td class="Actions">
								<s:if test="#user.equals(currentUser)">
									TODO: Change password, etc.
								</s:if>
							</td>
						</s:if>
					</tr>
				</s:iterator>
			</table>
			
			<f:bottomPager/>
		</s:else>
	</body>
</html>
