<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<s:if test="%{!isEmpty(summary)}">
	<div class="EntitySummaryBlock"><s:property value="summary"/></div>
</s:if>