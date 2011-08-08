<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<%-- 
	NOTE: Required vars:
	- "#entityName" {String} ex: "product", "shop".
--%>

<%-- 
	TODO: Low: Print hint for summary: "You have 300 characters." 
	And update number with JS onkeypress and onchange. 
--%>
<h:formField fieldName="${entityName}.summary" inputId="summaryTextArea"
		labelKey="msgkey.label.summary">
	<s:textarea theme="simple" id="summaryTextArea" 
		cssClass="SummaryTextArea" name="%{entityName}.summary" 
		rows="8" cols="60" disabled="%{generateSummary}"/>
	<br/>
	<s:checkbox theme="simple" id="generateSummary" name="generateSummary" 
		onclick="APP.toggleEnabledElemByCheckBox('#summaryTextArea', this);"/>
	<label for="generateSummary"><s:text name="msgkey.text.autogenSummaryFromDesc"/></label>
</h:formField>
	
<s:textarea id="descTextArea" cssClass="HtmlEditorArea"
	name="description" key="msgkey.label.description"
	rows="20" cols="60"/>
	
<s:set var="entityName" value="%{null}"/>