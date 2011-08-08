<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>

<html>
	<head>
		<title>Orders placed for you</title>
	</head>
	<body>
		<%-- TODO: Add pager. --%>
		<%-- TODO: Remove cart preview from this page? (y) --%>
		
		<s:if test="isEmpty(orders)">
			No order placed.
		</s:if>
		<s:else>
			<f:topPager/>
			<table class="ListTable">
				<tr>
					<th><s:text name="msgkey.label.id"/></th>
					<th><s:text name="msgkey.label.number"/></th>
					<th><s:text name="msgkey.label.shopLogo"/></th>
					<th><s:text name="msgkey.label.shop"/></th>
					<th><s:text name="msgkey.label.buyer"/></th>
					<th>Paid</th>
					<%--
					<th>TODO: Status</th>
					--%>
					<th><s:text name="msgkey.label.productCount"/></th>
					<th>Total quantity</th>
					<th><s:text name="msgkey.label.totalPrice"/></th>
					<th>Placed</th>
					<th><s:text name="msgkey.label.updated"/></th>
					<th><s:text name="msgkey.label.actions"/></th>
				</tr>
				<s:iterator value="orders" status="num">
					<tr class="<s:property value="#num.odd == true ? 'Odd' : 'Even'"/>">
						<td class="Id"><s:property value="id"/></td>
						<td class="Number"><s:property value="number"/></td>
						<td>TODO</td>
						<td>
							<s:if test="shop != null">
								<s:set var="shopUrl" value="%{urlBuilder.getViewShop(shop)}"/>
								<s:a href="%{#shopUrl}"><s:property value="shop.name"/></s:a>
							</s:if>
							<s:else>
								<%-- TODO: i18n: "DELETED" --%>
								<s:property value="shopName"/>&nbsp;[DELETED]
							</s:else>
						</td>
						<td>
							<s:if test="customer != null">
								<s:url action="user" namespace="/" var="viewUserUrl">
									<s:param name="id" value="customer.id"/>
								</s:url>
								<s:a href="%{#viewUserUrl}"><s:property value="customer.fullName"/></s:a>
							</s:if>
							<s:else>
								<%-- TODO: i18n: --%>
								[DELETED]
							</s:else>
						</td>
						<td><s:property value="paid ? 'Yes' : 'No'"/></td>
						<td class="Number">
							<s:url action="order!items" namespace="/member" var="orderItemspUrl">
								<s:param name="id" value="id"/>
							</s:url>
							<s:a href="%{#orderItemspUrl}"><s:property value="itemCount"/></s:a>
						</td>
						<td class="Number"><s:property value="totalQuantity"/></td>
						<td class="Number"><s:property value="totalPrice"/>&nbsp;<s:property value="currency"/></td>
						<td class="Date"><s:date name="created"/></td>
						<td class="Date"><s:date name="updated"/></td>
						<td class="Actions">
							<ul>
								<li><s:a href="#TODO">TODO: View</s:a></li>
								<li><s:a href="#TODO">TODO: Request for cancel</s:a></li>
							</ul>
						</td>
					</tr>
				</s:iterator>
			</table>
			<f:bottomPager/>
		</s:else>
	</body>
</html>