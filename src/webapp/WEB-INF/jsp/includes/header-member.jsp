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
				<%-- TODO: Need to add [accept-charset="UTF-8"] attribute to the form? --%>
				<form action="search" class="SearchForm" method="GET">
					<input class="SearchTextBox" type="text" name="q" value="TODO: search"
						maxlength="2048" title="Type in search query"/>
					<s:submit name="submit" key="msgkey.action.search"/>
				</form>
			</td>
		</tr>
	</table>
	
	<%-- TODO: Refactor to list ("ul" tag). --%>
	<table class="Menu" cellpadding="0" cellspacing="0">
		<tr>
			<td class="CommonMenu">
				<ul class="MenuItems">
					<li><a class="MenuLink" href="<%= request.getContextPath() %>/"><s:text name="msgkey.menu.main"/></a></li>
				</ul>
			</td>
			<td class="AccountMenu">
				<ul class="MenuItems">
					<%-- TODO: Separate account related links to another UI block. --%>
					<s:if test="loggedIn">
						<s:set var="settingsCss" value="namespace.equals('/settings') ? 'Selected' : ''"/>
						<s:set var="sellerCss" value="namespace.equals('/member') ? 'Selected' : ''"/>
						<s:set var="buyerCss" value="namespace.equals('/buyer') ? 'Selected' : ''"/>
						
						<li><a class="LogoutLink" href="<%= request.getContextPath() %>/logout"><s:text name="msgkey.menu.logout"/></a></li>
						<li><a class="MenuLink <s:property value="#settingsCss"/>" href="<%= request.getContextPath() %>/settings/index"><s:text name="msgkey.menu.settings"/></a></li>
						<li><a class="MenuLink <s:property value="#sellerCss"/>" href="<%= request.getContextPath() %>/member/home"><s:text name="msgkey.menu.sellerPanel"/></a></li>
						<li><a class="MenuLink <s:property value="#buyerCss"/>" href="<%= request.getContextPath() %>/buyer/home"><s:text name="msgkey.menu.buyerPanel"/></a></li>
						<%-- Replace by link to user's profile (page where are: email, password, addresses...). --%>
						<li><a class="MenuLink" href="<%= request.getContextPath() %>/member/home"><strong><s:property value="currentUser.fullName"/></strong></a></li>
					</s:if>
					<s:else>
						<li><a class="MenuLink" href="<%= request.getContextPath() %>/register"><s:text name="msgkey.menu.register"/></a></li>
						<li><a class="MenuLink" href="<%= request.getContextPath() %>/login"><s:text name="msgkey.menu.login"/></a></li>
					</s:else>
				</ul>
			</td>
		</tr>
	</table>
</div>