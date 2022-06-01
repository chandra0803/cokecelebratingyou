<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.value.FormattedValueBean"%>
<%@ page import="com.biperf.core.ui.homepage.QuickSearchForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="SEARCH_RESULTS" code="system.general"/></span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
		<c:choose>
			<c:when test="${paxSearch}">
          		<cms:contentText key="QUICKSEARCH_RESULTS_INSTRUCTIONS" code="system.general"/>
			</c:when>
			<c:otherwise>
          		<cms:contentText key="QUICKSEARCH_RESULTS_CLAIM_INSTRUCTIONS" code="system.general"/>
			</c:otherwise>
		</c:choose>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
			</td>
		</tr>
		<tr>    
			<td>
				<%  Map parameterMap = new HashMap();
						FormattedValueBean temp;
				%>
				<c:if test="${fn:length(quickSearchResults)!=0}">
				<display:table defaultsort="1" defaultorder="ascending" sort="external" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" name="quickSearchResults" id="formatterBean" style="width: 100%" partialList="true" pagesize="40" size="size">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
      		<c:forEach var="formattedValueBean" items="${formatterBean.formattedValueBeans}" varStatus="formattedValueBeanStatus">
      		    <c:set var="sort" value="true"/>
      		    <c:if test="${paxSearch && formattedValueBeanStatus.index == 1}">
      		    	<%-- Dont allow sort for email --%>
	      		    <c:set var="sort" value="false"/>
	      		</c:if>
	      		<c:choose>
	      			<c:when test="${paxSearch && formattedValueBeanStatus.index == 3}">
						<beacon:authorize ifAnyGranted="PROJ_MGR">
							<display:column titleKey="${quickSearchHeaderFullKeys[formattedValueBeanStatus.index] }" headerClass="crud-table-header-row" sortable="${sort}" >
						               	<c:out value="${formattedValueBean.value}"/>
			        		</display:column>
			        	</beacon:authorize>
					</c:when>
					<c:otherwise>	      			
						<display:column titleKey="${quickSearchHeaderFullKeys[formattedValueBeanStatus.index] }" headerClass="crud-table-header-row" sortable="${sort}" >
			         		<c:choose>
				           		<c:when test="${!empty formattedValueBean.id}">
												<%  temp = (FormattedValueBean)pageContext.getAttribute("formattedValueBean");
														QuickSearchForm tempForm1 = (QuickSearchForm)request.getAttribute("quickSearchForm");
														parameterMap.put( "quickSearchDomainId", temp.getId() );
														parameterMap.put( "quickSearchSearchForFieldCode", tempForm1.getQuickSearchSearchForFieldCode() );
														pageContext.setAttribute("quickSearchDetailUrl", ClientStateUtils.generateEncodedLink( "", "quickSearch.do?method=selectResult", parameterMap ) );
												%>
									<INPUT TYPE=HIDDEN NAME='<c:out value="${formattedValueBean.value}"/>'>
					               	<a href="<c:out value="${quickSearchDetailUrl}"/>"><c:out value="${formattedValueBean.value}"/></a>
				           		</c:when>
				           		<c:otherwise>
					               	<c:out value="${formattedValueBean.value}"/>
				           		</c:otherwise>
			         		</c:choose>
		        		</display:column>
 					</c:otherwise>
					</c:choose>
     		</c:forEach>
    		</display:table>
    		</c:if>
    	</td>
		</tr>		
	</table>
	<c:if test="${fn:length(quickSearchResults)==0}">
	    		<table id="formatterBean" style="width: 100%">
					<thead>
						<tr>
						<c:choose>
							<c:when test="${paxSearch}">
								<th class="crud-table-header-row"><cms:contentText key="NAME" code="home.quicksearch.participant.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="EMAIL" code="home.quicksearch.participant.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="USERNAME" code="home.quicksearch.participant.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="BANK_ACCOUNT" code="home.quicksearch.participant.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="STATUS" code="home.quicksearch.participant.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="COUNTRY" code="home.quicksearch.participant.headers"/></th>
							</c:when>
							<c:otherwise>
								<th class="crud-table-header-row"><cms:contentText key="CLAIM_NUMBER" code="home.quicksearch.claim.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="STATUS" code="home.quicksearch.claim.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="SUBMIT_DATE" code="home.quicksearch.claim.headers"/></th>
								<th class="crud-table-header-row"><cms:contentText key="SUBMITTER_NAME" code="home.quicksearch.claim.headers"/></th>
							</c:otherwise>
						</c:choose>
					    </tr>
				    </thead>
					<tbody>
							<tr class="crud-content" align="left">
								<td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       			</td>
                       		</tr>
					</tbody>
		</table>
	</c:if>