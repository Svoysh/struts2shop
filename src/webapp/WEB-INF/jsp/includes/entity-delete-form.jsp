<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>

<p/>
<s:form action="%{#formAction}">
	<s:hidden name="id" value="%{id}"/>
	<s:submit id="yesButton" cssClass="MainButton" name="method:delete" key="msgkey.action.yes"/>
	<s:submit key="msgkey.action.no"/>
</s:form>
		
<content tag="lastJavaScript">
	<script type="text/javascript">
		$(document).ready(function()
		{
			focusElem("yesButton");
		});
	</script>
</content>