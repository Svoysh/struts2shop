<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ tag language="java" pageEncoding="UTF-8" description="Pager for scrollable results." %>

<%@ attribute name="position" description="Position of this pager. Can be: 'top', 'bottom'." %>

<%-- TODO: Enhance: Limit pages links: What if page count is 50, 100, 1000, etc.? --%>

<s:if test="%{#attr.position.equals('top')}">
	<s:set var="pagerClass" value="%{'TopPager'}"/>
</s:if>
<s:elseif test="%{#attr.position.equals('bottom')}">
	<s:set var="pagerClass" value="%{'BottomPager'}"/>
</s:elseif>
<s:else>
	<%-- TODO: Low: CSS: 'DefaultPager'. --%>
	<s:set var="pagerClass" value="%{'TopPager'}"/>
</s:else>

<div class="<s:property value="%{#pagerClass}"/>">
	<s:if test="%{pager.pages > 1}">
		<table class="PagerTable HiddenTable">
			<tr class="PagerPagesRow">
				<td class="PageCountCell">
					<s:text name="msgkey.text.pageOfAllPages">
						<s:param value="%{pager.page}"/>
						<s:param value="%{pager.pages}"/>
					</s:text>
				</td>
				<td class="PagerPagesCell">
					<ul class="PagerPagesList">
						<s:if test="%{pager.page > 1}">
							<li class="PageItem">
								<s:url var="firstPageUrl" value="%{pager.urlWithParams}">
									<s:param name="page" value="%{1}"/>
									<s:param name="results" value="%{pager.maxResults}"/>
								</s:url>
								<s:a cssClass="PageLink" href="%{#firstPageUrl}" title="%{getText('msgkey.link.page.first')}">&laquo;</s:a>
							</li>
							<li class="PageItem">
								<s:url var="prevPageUrl" value="%{pager.urlWithParams}">
									<s:param name="page" value="%{pager.page - 1}"/>
									<s:param name="results" value="%{pager.maxResults}"/>
								</s:url>
								<s:a cssClass="PageLink" href="%{#prevPageUrl}" title="%{getText('msgkey.link.page.previous')}">&lsaquo;</s:a>
							</li>
						</s:if>
						<s:else>
							<li class="PageItem"><span class="CurrentPage" title="<s:text name="msgkey.link.page.first"/>">&laquo;</span></li>
							<li class="PageItem"><span class="CurrentPage" title="<s:text name="msgkey.link.page.previous"/>">&lsaquo;</span></li>
						</s:else>
						
						<s:iterator var="iPage" value="%{pager.pageListForPrint}">
							<s:if test="%{#iPage == pager.page}">
								<li class="PageItem"><span class="CurrentPage"><s:property value="%{#iPage}"/></span></li>
							</s:if>
							<s:else>
								<li class="PageItem">
									<s:url var="iPageUrl" value="%{pager.urlWithParams}">
										<s:param name="page" value="%{#iPage}"/>
										<s:param name="results" value="%{pager.maxResults}"/>
									</s:url>
									<s:a cssClass="PageLink" href="%{#iPageUrl}">
										<s:property value="%{#iPage}"/>
									</s:a>
								</li>
							</s:else>
						</s:iterator>
						
						<s:if test="%{pager.page < pager.pages}">
							<li class="PageItem">
								<s:url var="nextPageUrl" value="%{pager.urlWithParams}">
									<s:param name="page" value="%{pager.page + 1}"/>
									<s:param name="results" value="%{pager.maxResults}"/>
								</s:url>
								<s:a cssClass="PageLink" href="%{#nextPageUrl}" title="%{getText('msgkey.link.page.next')}">&rsaquo;</s:a>
							</li>
							<li class="PageItem">
								<s:url var="lastPageUrl" value="%{pager.urlWithParams}">
									<s:param name="page" value="%{pager.pages}"/>
									<s:param name="results" value="%{pager.maxResults}"/>
								</s:url>
								<s:a cssClass="PageLink" href="%{#lastPageUrl}" title="%{getText('msgkey.link.page.last')}">&raquo;</s:a>
							</li>
						</s:if>
						<s:else>
							<li class="PageItem"><span class="CurrentPage" title="<s:text name="msgkey.link.page.next"/>">&rsaquo;</span></li>
							<li class="PageItem"><span class="CurrentPage" title="<s:text name="msgkey.link.page.last"/>">&raquo;</span></li>
						</s:else>
					</ul>
				</td>
				<td class="GoToPageCell">
					<form action="<%= request.getContextPath() %><s:property value="%{pager.url}"/>" method="get">
						<s:iterator var="param" value="%{pager.parameters}">
							<s:hidden name="%{#param.key}" value="%{#param.value}"/>
						</s:iterator>
						<span><s:text name="msgkey.label.goToPage"/></span>
						<input type="text" class="GoToPageTextBox" name="page" value="" size="2"/>
						<s:hidden name="results" value="%{pager.maxResults}"/>
						<s:submit name="goButton" key="msgkey.action.goToPage.go" theme="simple"/>
					</form>
				</td>
			</tr>
		</table>
	</s:if>
	
	<%-- TODO: Low: UI: Add "Go to page" use case. See eBay pager. --%>
	
	<div>
		<table class="HiddenTable" width="100%">
			<tr>
				<td class="LeftCell">
					<s:text name="msgkey.text.resultsOfAllResults">
						<s:param value="%{pager.firstResultNumber}"/>
						<s:param value="%{pager.lastResultNumber}"/>
						<s:param value="%{pager.allResults}"/>
					</s:text>
				</td>
				<td class="RightCell">
					<form action="<%= request.getContextPath() %><s:property value="%{pager.url}"/>" method="get">
						<s:iterator var="param" value="%{pager.parameters}">
							<s:hidden name="%{#param.key}" value="%{#param.value}"/>
						</s:iterator>
						<s:hidden name="page" value="%{pager.page}"/>
						<span><s:text name="msgkey.label.resultsPerPage"/></span>
						<select name="results" size="1" onchange="this.form.submit();">
							<s:iterator var="maxResults" value="%{pager.maxResultsList}">
								<s:if test="%{pager.maxResults == #maxResults}">
									<option value="<s:property value="%{#maxResults}"/>" selected="selected">
										<s:property value="%{#maxResults}"/>
									</option>
								</s:if>
								<s:else>
									<option value="<s:property value="%{#maxResults}"/>">
										<s:property value="%{#maxResults}"/>
									</option>
								</s:else>
							</s:iterator>
							<%-- 
								TODO: Low: UI: Add option "Another value..." and show TextBox 
								with "Go" button if selected this option.
							--%>
						</select>
					</form>
				</td>
			</tr>
		</table>
	</div>
</div>

<%-- Clean used vars. --%>
<s:set var="pagerClass" value="%{null}"/>