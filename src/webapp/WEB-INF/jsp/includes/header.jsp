<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="Header">
	<table class="HeaderTable">
		<tr>
			<td class="HomeLinkBlock">
				<a class="HomeLink" href="<%= request.getContextPath() %>/">Struts2Shop</a>
			</td>
			<td>
				<%-- TODO: Duplication: See [./header.jsp] --%>
				<s:form namespace="/" action="change-locale" theme="simple" method="GET">
					<s:text name="msgkey.label.siteLocale"/>:&nbsp;
					<s:select name="localeCode" theme="simple" 
						list="getLocales()" listKey="code" listValue="name"
						onchange="this.form.submit();"/>
				</s:form>
			</td>
			<td class="SearchFormBlock">
				<%-- TODO: Need to add [accept-charset="UTF-8"] attribute to the form? (n) --%>
				<s:form action="search" namespace="/" theme="simple" 
					cssClass="SearchForm" method="GET">
					<%-- TODO: Impl: Search w/in current shop or any of its category. --%>
					<%-- TODO: Low: Replace "select" by group of checkboxes. --%>
					<select name="section" title="Search in">
						<s:set var="isSearchAction" value="action.equals('search')"/>
						<s:set var="selected" value="'selected=\"selected\"'"/>
						<option value="products" <s:property escape="false" value="(#isSearchAction && isSearchIn('products')) ? #selected : ''"/>><s:text name="msgkey.option.searchFor.products"/></option>
						<option value="shops" <s:property escape="false" value="(#isSearchAction && isSearchIn('shops')) ? #selected : ''"/>><s:text name="msgkey.option.searchFor.shops"/></option>
					</select>
					<s:textfield cssClass="SearchTextBox" 
						name="query" maxlength="2048" title="Search for"/>
					<s:submit name="submit" key="msgkey.action.search"/>
				</s:form>
			</td>
		</tr>
	</table>
	
	<%-- TODO: Refactor to list ("ul" tag). --%>
	<table class="Menu" cellpadding="0" cellspacing="0">
		<tr>
			<td class="CommonMenu">
				<s:set var="mainCss" value="(action.equals('home') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
				<s:set var="shopsCss" value="(action.equals('shop') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
				<s:set var="categoriesCss" value="(action.equals('category') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
				<s:set var="productsCss" value="(action.equals('product') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
				<s:set var="usersCss" value="(action.equals('user') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
				<s:set var="cartCss" value="(action.equals('cart') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
				
				<ul class="MenuItems">
					<li><a class="MenuLink <s:property value="#mainCss"/>" href="<%= request.getContextPath() %>/"><s:text name="msgkey.menu.main"/></a></li>
					<li><a class="MenuLink <s:property value="#shopsCss"/>" href="<%= request.getContextPath() %>/shop!list"><s:text name="msgkey.menu.shops"/></a></li>
					<li><a class="MenuLink <s:property value="#categoriesCss"/>" href="<%= request.getContextPath() %>/category!list"><s:text name="msgkey.menu.categories"/></a></li>
					<li><a class="MenuLink <s:property value="#productsCss"/>" href="<%= request.getContextPath() %>/product!list">TODO: <s:text name="msgkey.menu.products"/></a></li>
					<li><a class="MenuLink <s:property value="#usersCss"/>" href="<%= request.getContextPath() %>/user!list"><s:text name="msgkey.menu.users"/></a></li>
					<li><a class="MenuLink CartMenuLink <s:property value="#cartCss"/>" href="<%= request.getContextPath() %>/cart!subcarts"><s:text name="msgkey.menu.cart"/></a></li>
				</ul>
			</td>
			<td class="AccountMenu">
				<ul class="MenuItems">
					<%-- TODO: Separate account related links to another UI block. --%>
					<s:if test="loggedIn">
						<li><a class="LogoutLink" href="<%= request.getContextPath() %>/logout"><s:text name="msgkey.menu.logout"/></a></li>
						<li><a class="MenuLink" href="<%= request.getContextPath() %>/settings/index"><s:text name="msgkey.menu.settings"/></a></li>
						<li><a class="MenuLink" href="<%= request.getContextPath() %>/member/home"><s:text name="msgkey.menu.sellerPanel"/></a></li>
						<li><a class="MenuLink" href="<%= request.getContextPath() %>/buyer/home"><s:text name="msgkey.menu.buyerPanel"/></a></li>
						<%-- Replace by link to user's profile (email, password, addresses...). --%>
						<li><a class="MenuLink" href="<%= request.getContextPath() %>/member/home"><strong><s:property value="currentUser.fullName"/></strong></a></li>
					</s:if>
					<s:else>
						<s:set var="loginCss" value="(action.equals('login') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
						<s:set var="registerCss" value="(action.equals('register') && namespace.equals(\"/\")) ? 'Selected' : ''"/>
				
						<li><a class="MenuLink <s:property value="#registerCss"/>" href="<%= request.getContextPath() %>/register"><s:text name="msgkey.menu.register"/></a></li>
						<li><a class="MenuLink <s:property value="#loginCss"/>" href="<%= request.getContextPath() %>/login"><s:text name="msgkey.menu.login"/></a></li>
					</s:else>
				</ul>
			</td>
		</tr>
	</table>
</div>