<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="h" uri="/custom-tags" %>

<%-- NOTE: "#num.index" var may be defined in OGNL value stack. --%>

<s:set var="inForeach" value="%{#num.index != null}"/>
<s:set var="i" value="%{#inForeach ? #num.index : '%id%'}"/>

<li id="variantForm-<s:property value="#i"/>" class="DndItem">
	<div class="Handle">
		<table class="HiddenTable">
			<tr>
				<td class="LeftCell">
					<span class="HandleTitle"><s:text name="msgkey.title.productVariant"/></span>
					<span id="variantForm-summary-<s:property value="#i"/>" 
						class="HandleSummary"></span>
				</td>
				<td class="RightCell">
					<input type="button" id="variantForm-removeButton-%id%" 
						value="<s:text name="msgkey.action.small.remove"/>" 
						onclick="variantFormCopier.remove(<s:property value="#i"/>);"/>
					<input type="button" value="<s:text name="msgkey.action.small.toggle"/>" 
						onclick="variantFormCopier.toggle(<s:property value="#i"/>);"/>
				</td>
			</tr>
		</table>
	</div>
	<div id="variantForm-content-<s:property value="#i"/>" class="Content">
		<s:if test="%{#inForeach}"><s:hidden name="variants[%{#i}].id" value="%{id}"/></s:if>
		<s:hidden cssClass="VariantNumberInput" name="variants[%{#i}].number" value="0"/>
		<s:textfield id="variants_%{#i}_name" name="variants[%{#i}].name" value="%{#inForeach ? name : ''}" required="true" key="msgkey.label.productVariantName"/>
		
		<h:formField fieldName="variants[%{#i}].price" labelKey="msgkey.label.price">
			<s:textfield id="variants_%{#i}_price" name="variants[%{#i}].price" value="%{#inForeach ? price : ''}" required="true" theme="simple"/>&nbsp;<span class="CurrencyBox"><s:property value="shop.currencyCode"/></span>
		</h:formField>
		
		<h:formField fieldName="variants[%{#i}].oldPrice" labelKey="msgkey.label.oldPrice">
			<s:textfield id="variants_%{#i}_oldPrice" name="variants[%{#i}].oldPrice" value="%{#inForeach ? oldPrice : ''}" theme="simple"/>&nbsp;<span class="CurrencyBox"><s:property value="shop.currencyCode"/></span>
		</h:formField>
		
		<h:formField inputId="variants_%{#i}_inStock" labelKey="msgkey.label.inStockAvailability">
			<select id="variants_<s:property value="%{#i}"/>_inStock" name="variants[<s:property value="%{#i}"/>].inStock"
					onchange="toggleQuantityBlock(<s:property value="%{#i}"/>);">
				<option value="true" <s:property value="%{(#inForeach && inStock) ? 'selected=\"selected\"' : ''}"/>><s:text name="msgkey.option.productVariant.inStock"/></option>
				<option value="false" <s:property value="%{(#inForeach && !inStock) ? 'selected=\"selected\"' : ''}"/>><s:text name="msgkey.option.productVariant.notInStock"/></option>
			</select>
			<br/>
			<s:div id="variants_%{#i}_traceStockBlock" cssStyle="%{(#inForeach && !inStock) ? 'display: none;' : ''}">
				<s:checkbox id="variants_%{#i}_traceStock" name="variants[%{#i}].traceStock" value="%{#inForeach ? traceStock : ''}" theme="simple" 
					onclick="toggleQuantityBlock(%{#i});"/>
				<label for="variants_<s:property value="%{#i}"/>_traceStock"><s:text name="msgkey.option.productVariant.traceStock"/></label>
			</s:div>
		</h:formField>
		
		<s:div id="variants_%{#i}_quantityBlock" cssStyle="%{(#inForeach && needQuantity()) ? '' : 'display: none;'}">
			<h:formField fieldName="variants[%{#i}].quantity" labelKey="msgkey.label.inStockQuantity">
				<s:textfield id="variants_%{#i}_quantity" name="variants[%{#i}].quantity" value="%{#inForeach ? quantity : ''}" theme="simple"/>
				<br/>
				<s:checkbox id="variants_%{#i}_soldOutable" name="variants[%{#i}].soldOutable" value="%{#inForeach ? soldOutable : ''}" theme="simple"/>
				<label for="variants_<s:property value="%{#i}"/>_soldOutable"><s:text name="msgkey.option.productVariant.soldOutable"/></label>
			</h:formField>
		</s:div>
		
		<h:formField fieldName="variants[%{#i}].featured">
			<s:checkbox id="variants_%{#i}_featured" name="variants[%{#i}].featured" value="%{#inForeach ? featured : ''}" theme="simple"/>
			<label for="variants_<s:property value="%{#i}"/>_featured"><s:text name="msgkey.option.productVariant.featured"/></label>
		</h:formField>
	</div>
</li>

<s:set var="inForeach" value="%{null}"/>
<s:set var="i" value="%{null}"/>