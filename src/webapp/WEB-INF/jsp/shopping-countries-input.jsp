<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<title><s:text name="msgkey.title.selectShoppingCountries"/></title>
	</head>
	<body>
		<s:if test="%{isEmpty(countries)}">
			<s:text name="msgkey.text.notExist.shoppingCountries"/>
		</s:if>
		<s:else>
			<s:form namespace="/" action="shopping-countries" theme="simple">
				
				<%-- TODO: Replace by tag: "formFieldGroup". --%>
				<div class="FieldGroup">
					<div class="Caption">
						<table class="HiddenTable">
							<tr>
								<td class="LeftCell">
									<span class="Label">&nbsp;</span>
								</td>
								<td class="RightCell">
									<s:set var="hasElems" value="!isEmpty(shoppingCountries)"/>
									<div id="shoppingCountryForm-controls" class="FormCopierControls">
										<input type="button" onclick="shoppingCountryFormCopier.add(1);" value="<s:text name="msgkey.action.small.add"/>"/>
										<input type="button" onclick="shoppingCountryFormCopier.add(5);" value="<s:text name="msgkey.action.small.add5"/>"/>
										<input type="button" id="shoppingCountryForm-removeAllButton" 
											<s:property value="#hasElems ? '' : 'disabled=\"disabled\"'"/>
											onclick="shoppingCountryFormCopier.removeAll();" value="<s:text name="msgkey.action.small.removeAll"/>"/>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<ul id="shoppingCountryForm-block">
						<s:if test="#hasElems">
							<s:iterator value="shoppingCountries" status="num">
								<jsp:include page="/WEB-INF/jsp/includes/shoppingCountriesPattern.jsp"/>
							</s:iterator>
						</s:if>
					</ul>
				</div>
				
				<h:formControlPanel>
					<s:submit cssClass="MainButton" name="method:submit" key="msgkey.action.save"/>
				</h:formControlPanel>
			</s:form>
			
			<%-- 
				NOTE: Form patterns must be outside of the form. 
			--%>
			<div id="formPatterns" style="display: none;">
				<div id="shoppingCountryForm-pattern" class="FormPattern" style="display: none;">
					<jsp:include page="/WEB-INF/jsp/includes/shoppingCountriesPattern.jsp"/>
				</div>
			</div>
			
			<content tag="lastJavaScript">
				<script type="text/javascript">
					var shoppingCountryFormCopier;
					$(document).ready(function()
					{
						// Init form copiers.
						shoppingCountryFormCopier = new FormCopier("shoppingCountryForm");
					});
				</script>
			</content>
		</s:else>
	</body>
</html>