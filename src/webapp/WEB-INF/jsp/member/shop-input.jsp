<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<s:if test="newEntity">
			<title><s:text name="msgkey.title.seller.newShop"/></title>
		</s:if>
		<s:else>
			<title>Edit shop <s:property value="shop.name"/></title>
		</s:else>
	</head>
	<body>
		<s:if test="id != null && shop == null">
			<s:text name="msgkey.text.notFound.shop"/>
		</s:if>
		<s:else>
			<%-- TODO: Test: add [namespace="/member"] attribute? (y) --%>
			<s:form action="shop" enctype="multipart/form-data" method="POST">
				<s:textfield id="shop_name" name="shop.name" required="true" 
					key="msgkey.label.name"/>
				
				<h:formField inputId="shop_urlName" fieldName="shop.urlName" labelKey="msgkey.label.urlName">
					<div class="UrlNameBlock"><s:property value="%{urlBuilder.viewShopFullBase}"/>/<s:textfield 
						id="shop_urlName" name="shop.urlName" theme="simple"/></div>
					<div class="FormFieldHint"><s:text name="msgkey.text.formFieldHint.validUrlName"/></div>
				</h:formField>
				
				<%-- TODO: Low: Country label must be more descriptive. Current label is "Country". --%>
				<s:select name="shop.countryCode" required="true" 
					list="countries" listKey="code" listValue="fullName"
					headerKey="" headerValue="- %{getText('msgkey.option.defailt.choose.country')} -"
					key="msgkey.label.country"/>
				
				<s:select name="shop.currency" required="true" 
					list="currencies" listKey="code" listValue="fullName"
					headerKey="" headerValue="- %{getText('msgkey.option.defailt.choose.currency')} -"
					key="msgkey.label.currency"/>
				
				<%-- TODO: Low: Make logo editing more beautiful. --%>
				<h:formField inputId="shop_logo" fieldName="logo" labelKey="msgkey.label.logo">
					<s:hidden id="removeLogo" name="deleteLogo" value="false"/>
					<s:set var="shopHasLogo" value="%{shop.logo != null}"/>
					<div id="uploadLogoBox" <s:property escape="false" value="%{#shopHasLogo ? 'style=\"display: none;\"' : ''}"/>>
						<s:file id="shop_logo" name="logo" theme="simple"/>&nbsp;
						<%--<a href="#" onclick="cancelChangeLogo();"><s:text name="msgkey.action.cancel"/></a>--%>
					</div>
					<s:if test="#shopHasLogo">
						<div id="logoBox">
							<div>
								<a href="#" onclick="changeLogo();"><s:text name="msgkey.action.change"/></a>,
								<a href="#" onclick="removeLogo();"><s:text name="msgkey.action.delete"/></a>
							</div>
							<div style="margin-top: 6px;">
								<%-- TODO: Duplication: Extract to include file? E.g. "print-shop-logo-s200.jsp". --%>
								<s:url var="shopLogoS200Url" value="%{'/uploads/images/shop-logos/' + shop.logo.id + '/s200/' + shop.logo.fileName + '.jpg'}"></s:url>
								<img src="<s:property value="%{#shopLogoS200Url}"/>">
							</div>
						</div>
					</s:if>
				</h:formField>
				
				<s:set var="entityName" value="%{'shop'}"/>
				<jsp:include page="/WEB-INF/jsp/includes/htmlDescFields.jsp"/>
				
				<h:formControlPanel>
					<s:if test="newEntity">
						<s:submit cssClass="MainButton" name="method:save" key="msgkey.action.save"/>
						<s:submit name="method:saveAdd" key="msgkey.action.saveAndAddMore"/>
					</s:if>
					<s:else>
						<s:hidden name="id" value="%{id}"/>
						<s:submit cssClass="MainButton" name="method:update" key="msgkey.action.update"/>
						<s:url action="shop!deleteConfirm" namespace="/member" var="deleteUrl">
							<s:param name="id" value="%{id}"/>
						</s:url>
						<s:a href="%{#deleteUrl}"><s:text name="msgkey.action.delete"/></s:a>
					</s:else>
				</h:formControlPanel>
			</s:form>
		</s:else>
		<content tag="lastJavaScript">
			<script type="text/javascript" 
				src="<%= request.getContextPath() %>/lib/fckeditor/2.6.4.1/fckeditor.js">
			</script>
			<script type="text/javascript">
				$(document).ready(function()
				{
					focusElemIfEmpty("shop_name");

					// Init FCKeditor for shop description field.
					APP.fckeditorBasePath = "<%= request.getContextPath() %>/lib/fckeditor/2.6.4.1/";
					createHtmlEditor("descTextArea");
				});

				// Do not use "delete" word in function names. It is JS keyword.
				function removeLogo()
				{
					$("#removeLogo").val("true");
					changeLogo();
				}

				function changeLogo()
				{
					$("#logoBox").hide();
					$("#uploadLogoBox").show();
				}
				
				function cancelChangeLogo()
				{
					$("#uploadLogoBox").hide();
					$("#logoBox").show();
				}
			</script>
		</content>
	</body>
</html>