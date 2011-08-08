<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<%-- TODO: i18n --%>
		<title>User <s:property value="user.fullName"/></title>
	</head>
	<body>
		<s:if test="user == null">
			<%-- TODO: Print this text in title instead. --%>
			<%-- TODO: Print the link to User list. --%>
			<s:text name="msgkey.text.notFound.user"/>
		</s:if>
		<s:else>
			<s:push value="user">
				<table class="ViewTable">
					<tr>
						<th>Avatar</th>
						<td>TODO</td>
					</tr>
					<tr>
						<%-- TODO: Low: Rename label to simply "Name"? (y) --%>
						<th><s:text name="msgkey.label.fullName"/></th>
						<td><s:property value="fullName"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.email"/></th>
						<%-- 
							TODO: High: Spam protection: Print '@' as image or 
							create full user's email image with Java2D like on Facebook. 
						--%>
						<td><s:property value="email"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.firstName"/></th>
						<td><s:property value="firstName"/></td>
					</tr>
					<tr>
						<th><s:text name="msgkey.label.lastName"/></th>
						<td><s:property value="lastName"/></td>
					</tr>
					<%-- TODO: Inline address: Country, city, etc. --%>
					<s:if test="!isEmpty(shops)">
						<tr>
							<th><s:text name="msgkey.label.ownedShops"/></th>
							<td>
								<ul>
									<s:iterator value="shops" var="shop">
										<s:set var="shopUrl" value="%{urlBuilder.getViewShop(#shop)}"/>
										<li><s:a href="%{#shopUrl}"><s:property value="name"/></s:a></li>
									</s:iterator>
								</ul>
							</td>
						</tr>
					</s:if>
					<tr>
						<th><s:text name="msgkey.label.registeredOn"/></th>
						<%-- TODO: Low: Print 'registered' field (add such field to entity)? (y) --%>
						<td><s:date name="created"/></td>
					</tr>
				</table>
			</s:push>
		</s:else>
	</body>
</html>