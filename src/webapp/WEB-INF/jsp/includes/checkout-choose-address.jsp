<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- TODO: i18n --%>
<h2><s:text name="msgkey.title.createNewAddress"/></h2>
<s:form namespace="/checkout" action="address" theme="simple">
	<s:hidden name="checkoutId" value="%{checkoutId}"/>
	<s:hidden name="returnTo" value="%{returnTo}"/>
	<s:submit key="msgkey.action.add"/>
</s:form>

<%-- TODO: i18n --%>
<h2><s:text name="msgkey.title.selectPrevAddress"/></h2>
<s:iterator value="addresses" var="address" status="num">
	<%--<s:set var="address" value="%{#address}"/>--%>
	<jsp:include page="/WEB-INF/jsp/includes/address.jsp"/>
	<%-- TODO: Refactor: Duplication of form params. --%>
	<%-- TODO: Highlight address already selected in this C.O. process. --%>
	<%-- TODO: Highlight/show which address were selected for billing or shipping 
		in this C.O. process. --%>
	<table class="InlineHiddenTable">
		<tr>
			<td>
				<s:form namespace="/checkout" action="%{returnTo}!submit" theme="simple">
					<s:hidden name="checkoutId" value="%{checkoutId}"/>
					<s:hidden name="returnTo" value="%{returnTo}"/>
					<s:hidden name="addressId" value="%{#address.id}"/>
					<s:submit key="msgkey.action.useThisAddress"/>
				</s:form>
			</td>
			<td>
				<s:form namespace="/checkout" action="address" theme="simple">
					<s:hidden name="checkoutId" value="%{checkoutId}"/>
					<s:hidden name="returnTo" value="%{returnTo}"/>
					<s:hidden name="addressId" value="%{#address.id}"/>
					<s:submit key="msgkey.action.edit"/>
				</s:form>
			</td>
				<%-- TODO: Impl: Delete user address. --%>
				<%--
				<s:form namespace="/checkout" action="address" theme="simple">
					<s:hidden name="checkoutId" value="%{checkoutId}"/>
					<s:hidden name="returnTo" value="%{returnTo}"/>
					<s:hidden name="addressId" value="%{#address.id}"/>
					<s:submit key="msgkey.action.delete"/>
				</s:form>
				--%>
		</tr>
	</table>
	<p/>
</s:iterator>