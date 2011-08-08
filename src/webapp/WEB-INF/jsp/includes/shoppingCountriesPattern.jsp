<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<%-- NOTE: "#num.index" var may be defined in OGNL value stack. --%>

<s:set var="inForeach" value="%{#num.index != null}"/>
<s:set var="i" value="%{#inForeach ? #num.index : '%id%'}"/>

<li id="shoppingCountryForm-<s:property value="#i"/>">
	<s:if test="%{#inForeach}"><s:hidden name="shoppingCountries[%{#i}].id" value="%{id}"/></s:if>
	
	<%-- TODO: Low: Remove next commented? (y, if not used) --%>
	<%--<s:hidden cssClass="ShoppingCountryNumberInput" name="shoppingCountries[%{#i}].number" value="0"/>--%>
	
	<h:formField fieldName="shoppingCountries[%{#i}].countryCode" labelKey="msgkey.label.country">
		<s:select name="shoppingCountries[%{#i}].countryCode" 
			list="countries" listKey="code" listValue="fullName"
			headerKey="" headerValue="- %{getText('msgkey.option.defailt.choose.country')} -"
			theme="simple"/>
		&nbsp;
		<input type="button" id="shoppingCountryForm-removeButton-%id%" 
			value="<s:text name="msgkey.action.small.remove"/>" 
			onclick="shoppingCountryFormCopier.remove(<s:property value="#i"/>);"/>
	</h:formField>
</li>

<s:set var="inForeach" value="%{null}"/>
<s:set var="i" value="%{null}"/>