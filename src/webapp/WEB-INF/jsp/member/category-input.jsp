<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<s:if test="newEntity">
			<title><s:text name="msgkey.title.seller.newCategory"/></title>
		</s:if>
		<s:else>
			<title>Edit category <s:property value="category.name"/></title>
		</s:else>
	</head>
	<body>
		<s:if test="id != null && category == null">
			<s:text name="msgkey.text.notFound.category"/>
		</s:if>
		<s:else>
			<s:form action="category" namespace="/member">
				<s:textfield id="category_name" name="category.name" required="true" 
					key="msgkey.label.name"/>
				<h:formControlPanel>
					<s:if test="newEntity">
						<s:submit cssClass="MainButton" name="method:save" key="msgkey.action.save"/>
						<s:submit name="method:saveAdd" key="msgkey.action.saveAndAddMore"/>
					</s:if>
					<s:else>
						<s:hidden name="id" value="%{id}"/>
						<s:submit cssClass="MainButton" name="method:update" key="msgkey.action.update"/>
						<s:url action="category!deleteConfirm" namespace="/member" var="deleteUrl">
							<s:param name="id" value="%{id}"/>
						</s:url>
						<s:a href="%{#deleteUrl}"><s:text name="msgkey.action.delete"/></s:a>
					</s:else>
				</h:formControlPanel>
			</s:form>
		</s:else>
		<content tag="lastJavaScript">
			<script type="text/javascript">
				$(document).ready(function()
				{
					focusElemIfEmpty("category_name");
				});
			</script>
		</content>
	</body>
</html>