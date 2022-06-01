<%@page import="com.biperf.core.ui.claim.ProductClaimValueObject"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<%
    Map paramMapOpen = new HashMap();
	Map returnURLMapOpen = new HashMap();
	ProductClaimValueObject temp;
    Map parameterMap = new HashMap();
%>

<%
	Map clientStateMap = ClientStateUtils.getClientStateMap( request );
	String subid = ClientStateUtils.getParameterValue( request, clientStateMap, "submitterId" );
	if ( subid != null )
	{
	    pageContext.setAttribute( "submitterId", subid );
	}
	String proxid = ClientStateUtils.getParameterValue( request, clientStateMap, "proxyUserId" );
	if ( proxid != null )
	{
	    pageContext.setAttribute( "proxyUserId", proxid );
	}
%>

   <div id="productclaimOpen" class="tab-pane fade active in">

   			<c:if test="${isShowGraph}">
              <div class='row-fluid'>
                <div class='span12'>
                    <div class="profilePageActivityHistoryResultsGraph">
                        <h4><cms:contentText key="OPEN_CLAIMS_COUNT" code="profile.activity.history" /><span class="asOfText"><cms:contentTemplateText key="AS_OF" code="profile.activity.history" args="${refreshDate}"/>&nbsp;<cms:contentText key="CST" code="profile.activity.history" /></span></h4>
                        <p><cms:contentText key="CLAIMS_SUBMITTED" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant">${openClaimsSubmittedCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${openClaimsSubmittedCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="ORG_UNIT_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant"> ${openClaimsSubmittedAverageForMyTeamCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${openClaimsSubmittedAverageForMyTeamCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="COMPANY_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant">  ${openClaimsSubmittedAverageForMyCompanyCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${openClaimsSubmittedAverageForMyCompanyCount}"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
              </div>
            </c:if>
                <div class="row-fluid">
                  <div class="span12">

                  	<%-- populated via JS view, grabbing from display:table output --%>
	                <div class="clearfix">
	                    <ul class="export-tools fr" style="display:none">
	                        <li class="export pdf">
	                            <a href="layout.html" class="exportPdfButtonSent" style="display:none">
                                    <span class="btn btn-inverse btn-compact btn-export-pdf">
                                        <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                                    </span>
	                            </a>
	                        </li>
	                        <li class="export csv">
	                            <a href="layout.html" class="exportCsvButtonSent" style="display:none">
                                    <span class="btn btn-inverse btn-compact btn-export-csv">
                                        CSV <i class="icon-download-2"></i>
                                    </span>
	                            </a>
	                        </li>
	                        <%-- <li class="export xls">
	                            <a href="layout.html" class="exportXlsButtonSent" style="display:none">
                                    <span class="btn btn-inverse btn-compact btn-export-xls">
                                        XLS <i class="icon-download-2"></i>
                                    </span>
	                            </a>
	                        </li> --%>
	                    </ul>
	                </div>

                    <display:table defaultsort="1" defaultorder="ascending" name="openClaimList" id="valueObject" class="table table-striped"	pagesize="10"
						requestURI="<%= RequestUtils.getOriginalRequestURI(request) %>" export="true">
				<display:setProperty name="basic.msg.empty_list_row">
					<tr class="crud-content" align="left">
						<c:if test="${status!='hold'}">
							<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
									code="system.errors" /></td>
						</c:if>
						<c:if test="${status=='hold'}">
							<td class="text-error" colspan="{0}"><cms:contentText
									key="ACTIVITYHISTORY_ACCOUNT_ONHOLD" code="system.errors" /></td>
						</c:if>
					</tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
						<%-- Explicit displayTag.properties START --%>
						<display:setProperty name="export.xml" value="false" />
					 	<display:setProperty name="export.csv.label" value="CSV" />
					 	<display:setProperty name="export.csv.filename" value="export.csv" />
						<display:setProperty name="export.csv.include_header" value="true" />
						<display:setProperty name="export.csv.class" value="com.biperf.core.ui.utils.CustomCsvView" />
						<display:setProperty name="export.excel.label" value="XLS" />
						<display:setProperty name="export.pdf" value="true" />
						<display:setProperty name="export.pdf.filename" value="export.pdf" />
						<display:setProperty name="export.pdf.include_header" value="true" />
						<display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
						<display:setProperty name="export.banner" value="<table width='100%'><tr><td align='left'><span class='export'><input type='hidden' class='selectedMode' value='sent'>{0}</span></td></tr></table>" />
						<%-- Explicit displayTag.properties END --%>

						<display:column titleKey="claims.list.OC_CLAIM_NUMBER_COLUMN_HEADER" class="sortable promotionsColumn sorted ascending"	sortable="true" sortProperty="claimId" media="html">
						  <%
						  returnURLMapOpen.put( "mode", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "mode" ) );
						  returnURLMapOpen.put( "promotionId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionId" ) );
						  returnURLMapOpen.put( "startDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "startDate" ) );
						  returnURLMapOpen.put( "endDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "endDate" ) );
					        String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/activityHistory.do", returnURLMapOpen );
							temp = (ProductClaimValueObject)pageContext.getAttribute( "valueObject" );
							parameterMap.put( "claimId", temp.getClaimId() );
							parameterMap.put( "startDate", ClientStateUtils.getParameterValue( request, clientStateMap, "startDate" ) );
							parameterMap.put( "endDate", ClientStateUtils.getParameterValue( request, clientStateMap, "endDate" ) );
							parameterMap.put( "submitterId", ClientStateUtils.getParameterValue( request, clientStateMap, "submitterId" ) );
							parameterMap.put( "promotionId", ClientStateUtils.getParameterValue( request, clientStateMap, "promotionId" ) );
							parameterMap.put( "open", ClientStateUtils.getParameterValue( request, clientStateMap, "open" ) );
							parameterMap.put( "proxyUserId", ClientStateUtils.getParameterValue( request, clientStateMap, "proxyUserId" ) );
							parameterMap.put( "returnURL", returnURL );
					  	  %>
						  <c:choose>
							<c:when test="${param.callingScreen == 'activityReport' }">
							  <%
								pageContext.setAttribute( "claimDetailUrl", ClientStateUtils.generateEncodedLink( "", "claim/productClaimDetail.do?method=display", parameterMap ) );
							  %>
							</c:when>
							<c:otherwise>
							  <%
								pageContext.setAttribute( "claimDetailUrl", ClientStateUtils.generateEncodedLink( "", "claim/productClaimDetail.do?method=display", parameterMap ) );
							  %>
							</c:otherwise>
						  </c:choose>
						  <a href="<c:out value="${claimDetailUrl}"/>" class="crud-content-link">
						  	<c:out value="${valueObject.claimNumber}" />
						  </a>&nbsp;&nbsp;&nbsp;&nbsp;
                   		  <c:if test="${!(valueObject.approvalRound > 1) and ( (pageViewerUserId == valueObject.proxyUser.id) or (pageViewerUserId == valueObject.submitter.id))}">
							<%
							paramMapOpen.put( "claimId", temp.getClaimId() );
							paramMapOpen.put( "selectedNode", temp.getNode().getId() );
							paramMapOpen.put( "proxyUserId", ClientStateUtils.getParameterValue( request, clientStateMap, "proxyUserId" ) );
								pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( "", "claim/editClaimSubmission.do?method=showClaim", paramMapOpen ) );
							%>
							<a href="<c:out value="${editUrl}"/>" class="crud-content-link">
								<cms:contentText code="system.link" key="EDIT" />
							</a>
						  </c:if>
						</display:column>

						<display:column titleKey="claims.list.OC_CLAIM_NUMBER_COLUMN_HEADER" class="sortable promotionsColumn sorted ascending" media="csv excel pdf">
							<c:out value="${valueObject.claimNumber}" />
						</display:column>

						<display:column titleKey="claims.list.OC_DATE_SUBMITTED_COLUMN_HEADER" class="sortable dateSentColumn unsorted"  sortable="true" sortProperty="dateSubmitted">
							<fmt:formatDate value="${valueObject.dateSubmitted}" dateStyle="short" pattern="${JstlDatePattern}" />
							<c:if test="${ ! proxy}">
								<c:if test="${ valueObject.proxyUser != null}">
                        			&nbsp;<cms:contentText key="BY" code="claims.list" />
									<c:out value="${valueObject.proxyUser.firstName}" />&nbsp;<c:out value="${valueObject.proxyUser.lastName}" />
								</c:if>
							</c:if>
						</display:column>

						<display:column property="promotionName" sortable="true" titleKey="claims.list.OC_PROMOTION_COLUMN_HEADER" class="sortable promotionsColumn sorted ascending" />
					</display:table>
                  </div>
                </div>
            </div>
