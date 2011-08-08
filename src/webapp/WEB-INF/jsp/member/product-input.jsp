<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<html>
	<head>
		<s:if test="newEntity">
			<title><s:text name="msgkey.title.seller.newProduct"/></title>
		</s:if>
		<s:else>
			<title>Edit product <s:property value="product.name"/></title>
		</s:else>
	</head>
	<body>
		<%-- TODO: Suggest to create 1-st shop if shops == null --%>
		
		<s:if test="id != null && product == null">
			<s:text name="msgkey.text.notFound.product"/>
		</s:if>
		<s:else>
			<%-- TODO: Test: add [namespace="/member"] attribute? (y) --%>
			<s:form action="product" enctype="multipart/form-data" method="POST" onsubmit="onFormSubmit();">
				<h2><s:text name="msgkey.title.mainInfo"/></h2>
				<%-- TODO: Enhancement: print link "Edit" for selected shop. (Updated with select.onchange) --%>
				<s:select id="shopId" name="shopId" required="true" 
					list="getShops()" listKey="id" listValue="name + ' (' + currency + ')'"
					headerKey="" headerValue="- Select shop -"
					onchange="changeShop();"
					key="msgkey.label.shop"/>
				
				<s:textfield id="product_name" name="product.name" required="true"
					key="msgkey.label.name"/>
				
				<s:set var="entityName" value="%{'product'}"/>
				<jsp:include page="/WEB-INF/jsp/includes/htmlDescFields.jsp"/>
				
				<%-- TODO: Replace by tag: "formFieldGroup". --%>
				<div class="FieldGroup">
					<div class="Caption">
						<table class="HiddenTable">
							<tr>
								<td class="LeftCell">
									<span class="Label"><s:text name="msgkey.title.images"/></span>
								</td>
								<td class="RightCell">
									<div id="imageForm-controls" class="FormCopierControls">
										<input type="button" onclick="imageFormCopier.add(1);" value="<s:text name="msgkey.action.small.add"/>"/>
										<input type="button" onclick="imageFormCopier.add(5);" value="<s:text name="msgkey.action.small.add5"/>"/>
										<input type="button" id="imageForm-removeAllButton" disabled="disabled"
											onclick="imageFormCopier.removeAll();" value="<s:text name="msgkey.action.small.removeAll"/>"/>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div id="imageForm-block">
						<%-- TODO: High: Load images from persistent product 
							(on update action). --%>
					</div>
				</div>
				
				<%-- TODO: Replace by tag: "formFieldGroup". --%>
				<div class="FieldGroup">
					<div class="Caption">
						<table class="HiddenTable">
							<tr>
								<td class="LeftCell">
									<span class="Label"><s:text name="msgkey.title.productVariants"/></span>
								</td>
								<td class="RightCell">
									<s:set var="hasVariants" value="!isEmpty(variants)"/>
									<div id="variantForm-controls" class="FormCopierControls">
										<input type="button" onclick="variantFormCopier.add(1);" value="<s:text name="msgkey.action.small.add"/>"/>
										<input type="button" onclick="variantFormCopier.add(5);" value="<s:text name="msgkey.action.small.add5"/>"/>
										<input type="button" id="variantForm-removeAllButton" 
											<s:property value="#hasVariants ? '' : 'disabled=\"disabled\"'"/>
											onclick="variantFormCopier.removeAll();" value="<s:text name="msgkey.action.small.removeAll"/>"/>
										<input type="button" onclick="variantFormCopier.toggleAll();" 
											value="<s:text name="msgkey.action.small.reorder"/>"/>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<ul id="variantForm-block" class="DndList">
						<s:if test="#hasVariants">
							<s:iterator value="variants" status="num">
								<jsp:include page="/WEB-INF/jsp/includes/productVariantPattern.jsp"/>
							</s:iterator>
						</s:if>
					</ul>
				</div>
				
				<%-- TODO: Low: UI: Print out previously entered categories as list of checkboxes? --%>
				<%-- TODO: Low: UI: Print out all shop categories as list of checkboxes? --%>
				<%-- TODO: Low: UI: Add autosuggest to category name input? --%>
				
				<%-- TODO: Replace by tag: "formFieldGroup". --%>
				<div class="FieldGroup">
					<div class="Caption">
						<table class="HiddenTable">
							<tr>
								<td class="LeftCell">
									<span class="Label"><s:text name="msgkey.title.categories"/></span>
								</td>
								<td class="RightCell">
									<s:set var="hasCategories" value="product.productCategories != null && product.productCategories.size > 0"/>
									<div id="categoryForm-controls" class="FormCopierControls">
										<input type="button" onclick="categoryFormCopier.add(1);" value="<s:text name="msgkey.action.small.add"/>"/>
										<input type="button" onclick="categoryFormCopier.add(5);" value="<s:text name="msgkey.action.small.add5"/>"/>
										<input type="button" id="categoryForm-removeAllButton" 
											<s:property value="#hasCategories ? '' : 'disabled=\"disabled\"'"/>
											onclick="categoryFormCopier.removeAll();" value="<s:text name="msgkey.action.small.removeAll"/>"/>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div id="categoryForm-block">
						<s:if test="#hasCategories">
							<s:iterator value="product.productCategories" status="num">
								<s:set var="i" value="#num"/>
								<div id="categoryForm-<s:property value="#i"/>">
									<s:push value="category">
										<s:hidden name="categories[%{#i}].id" value="%{id}"/>								
										<s:set var="categoryId" value="'categories_' + #i + '_name'"/>
										<s:set var="categoryName" value="'categories[' + #i + '].name'"/>
										<h:formField required="true" labelKey="msgkey.label.category">
											<jsp:attribute name="fieldName"><s:property value="%{#categoryName}"/></jsp:attribute>
											<jsp:attribute name="inputId"><s:property value="%{#categoryId}"/></jsp:attribute>
											<jsp:body>
												<input type="text" id="<s:property value="#categoryId"/>" 
													name="<s:property value="#categoryName"/>"/>
												<input type="button" value="remove" 
													onclick="categoryFormCopier.remove(<s:property value="#i"/>);"/>
											</jsp:body>
										</h:formField>
									</s:push>
								</div>
								<s:set var="i" value="null"/>
							</s:iterator>
						</s:if>
					</div>
				</div>

				<h:formControlPanel>
					<s:if test="newEntity">
						<s:submit cssClass="MainButton" name="method:save" key="msgkey.action.save"/>
						<s:submit name="method:saveAdd" key="msgkey.action.saveAndAddMore"/>
					</s:if>
					<s:else>
						<s:hidden name="id" value="%{id}"/>
						<s:submit cssClass="MainButton" name="method:update" key="msgkey.action.update"/>
						<s:url action="product!deleteConfirm" namespace="/member" var="deleteUrl">
							<s:param name="id" value="%{id}"/>
						</s:url>
						<s:a href="%{#deleteUrl}"><s:text name="msgkey.action.delete"/></s:a>
					</s:else>
				</h:formControlPanel>
			</s:form>
		</s:else>
		
<%-- ======================== Form Patterns =============================== --%>
		
		<%-- 
			NOTE: Form patterns must be outside of the form. 
		--%>
		<div id="formPatterns" style="display: none;">
			<div id="imageForm-pattern" class="FormPattern" style="display: none;">
				<div id="imageForm-%id%" style="display: none;">
					<h:formField fieldName="images" inputId="images_%id%"
							labelKey="msgkey.label.image">
						<table class="InlineHiddenTable">
							<tr>
								<td><s:file id="images_%id%" name="images" theme="simple"/></td>
								<td><input type="button" id="imageForm-removeButton-%id%" 
									value="<s:text name="msgkey.action.small.remove"/>"/></td>
							</tr>
						</table>
					</h:formField>
				</div>
			</div>
			<div id="variantForm-pattern" class="FormPattern" style="display: none;">
				<jsp:include page="/WEB-INF/jsp/includes/productVariantPattern.jsp"/>
			</div>
			<div id="categoryForm-pattern" class="FormPattern" style="display: none;">
				<div id="categoryForm-%id%" style="display: none;">
					<h:formField fieldName="categories[%id%].name" inputId="categories_%id%_name"
							labelKey="msgkey.label.category">
						<table class="InlineHiddenTable">
							<tr>
								<td><s:textfield id="categories_%id%_name" name="categories[%id%].name" 
									theme="simple"/></td>
								<td><input type="button" id="categoryForm-removeButton-%id%" 
									value="<s:text name="msgkey.action.small.remove"/>"/></td>
							</tr>
						</table>
					</h:formField>
				</div>
			</div>
		</div>
		
		<content tag="lastJavaScript">
			<script type="text/javascript" 
				src="<%= request.getContextPath() %>/lib/fckeditor/2.6.4.1/fckeditor.js">
			</script>
			<%-- TODO: PROD: Include only required jQuery UI libs? (y) --%>
			<script type="text/javascript" 
				src="<%= request.getContextPath() %>/lib/jquery-ui/1.7.2/js/jquery-ui-1.7.2.custom.min.js">
			</script>
			<script type="text/javascript">
				var imageFormCopier;
				var variantFormCopier;
				var categoryFormCopier;

				var shopId = <s:property value="null != shopId ? shopId : 0"/>;
				var currency = '<s:property value="shop.currencyCode"/>';

				// Example: {"12":"USD","40":"EUR","210":"RUB"}
				// key - shop ID;
				// value - currency code.
				var shopIdToCurrencyJsonMap = <s:property value="shopIdToCurrencyJsonMap" escape="false"/>;
				
				$(document).ready(function()
				{
					focusElemIfEmpty("product_name");

					// Init FCKeditor for product description field.
					APP.fckeditorBasePath = "<%= request.getContextPath() %>/lib/fckeditor/2.6.4.1/";
					createHtmlEditor("descTextArea");

					// Init form copiers.
					imageFormCopier = new FormCopier("imageForm");
					variantFormCopier = new FormCopier("variantForm");
					categoryFormCopier = new FormCopier("categoryForm");

					// Add drag-n-drop feature to list of product variants.
					variantFormCopier.$formBlock.sortable(
					{
						opacity: 0.7,
//						placeholder: 'PlaceHolder',
						revert: true, 
						tolerance: 'pointer'
					});
					variantFormCopier.onAdd = function($newForm)
					{
						$newForm.sortable('option', 'handle', '.Handle');
					};
					variantFormCopier.getSummary = function(serial)
					{
						// TODO: Low: Do not print empty values? (mb)
						var currencyBox = " <span class='CurrencyBox'>" + currency + "</span>; ";
						return "[" + 
							$("#variants_" + serial + "_name").val() + "; " + 
							$("#variants_" + serial + "_price").val() + currencyBox + 
							$("#variants_" + serial + "_oldPrice").val() + currencyBox + 
							$("#variants_" + serial + "_quantity").val() + "]";
					};
				});

				function changeShop()
				{
					shopId = $("#shopId").val();
					var pCurrency = shopIdToCurrencyJsonMap[shopId];
					updateCurrency(pCurrency);
				}
				
				/**
				 * {String} pCurrency - currency code;
				 */
				function updateCurrency(pCurrency)
				{
					currency = pCurrency;
					$(".CurrencyBox").text(currency);
					variantFormCopier.reloadPattern();
				}

			 	/**
				 * {int} number - number of quantity block;
				 */
				function toggleQuantityBlock(number)
				{
					var showQuntity = false;
					var showTrace = true;
					var $inStockListbox = $("#variants_" + number + "_inStock");
					var inStock = "true" === $inStockListbox.val();
					if (inStock) {
						var $traceStockCheckbox = $("#variants_" + number + "_traceStock");
						var checked = $traceStockCheckbox.attr("checked");
						if (checked) {
							showQuntity = true;
						}
					}
					else {
						showTrace = false;
					}
					$("#variants_" + number + "_traceStockBlock").toggle(showTrace);
					$("#variants_" + number + "_quantityBlock").toggle(showQuntity);
				}
				
				/**
				 * Set serial numbers of product variants accordingly to 
				 * position of each variant in DOM.
				 */
				function onFormSubmit()
				{
					// "#variantForm-block" must be specified in selector to 
					// prevent counting of variant number from form pattern.
					$("#variantForm-block .VariantNumberInput").map(function(index)
					{
						$(this).val(index);
					});
				}
			</script>
		</content>
	</body>
</html>