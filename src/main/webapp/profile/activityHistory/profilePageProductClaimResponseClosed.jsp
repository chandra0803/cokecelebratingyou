<%@page import="com.biperf.core.ui.claim.ProductClaimValueObject"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<%
    Map paramMapClosed = new HashMap();
	Map returnURLMapClosed = new HashMap();
	ProductClaimValueObject temp2;
    Map map = new HashMap();
%>

<%
	Map clientStateMap1 = ClientStateUtils.getClientStateMap( request );
	String subid1 = ClientStateUtils.getParameterValue( request, clientStateMap1, "submitterId" );
	if ( subid1 != null )
	{
	    pageContext.setAttribute( "submitterId", subid1 );
	}
	String proxid1= ClientStateUtils.getParameterValue( request, clientStateMap1, "proxyUserId" );
	if ( proxid1!= null )
	{
	    pageContext.setAttribute( "proxyUserId", proxid1 );
	}
%>

     <div id="productclaimClosed" class="tab-pane fade in">

     		<c:if test="${isShowGraph}">
              <div class='row-fluid'>
                <div class='span12'>
                    <div class="profilePageActivityHistoryResultsGraph">
                        <h4><cms:contentText key="CLOSED_CLAIMS_COUNT" code="profile.activity.history" /><span class="asOfText"><cms:contentTemplateText key="AS_OF" code="profile.activity.history" args="${refreshDate}"/>&nbsp;<cms:contentText key="CST" code="profile.activity.history" /></span></h4>
                        <p><cms:contentText key="CLAIMS_SUBMITTED" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant">${closedClaimsSubmittedCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${closedClaimsSubmittedCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="ORG_UNIT_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant"> ${closedClaimsSubmittedAverageForMyTeamCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${closedClaimsSubmittedAverageForMyTeamCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="COMPANY_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant">  ${closedClaimsSubmittedAverageForMyCompanyCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${closedClaimsSubmittedAverageForMyCompanyCount}"></div>
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
	                            <a href="layout.html" class="exportPdfButtonReceived" style="display:none">
                                    <span class="btn btn-inverse btn-compact btn-export-pdf">
                                        <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                                    </span>
	                            </a>
	                        </li>
	                        <li class="export csv">
	                            <a href="layout.html" class="exportCsvButtonReceived" style="display:none">
                                    <span class="btn btn-inverse btn-compact btn-export-csv">
                                        <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                                    </span>
	                            </a>
	                        </li>
	                        <%-- <li class="export xls">
	                            <a href="layout.html" class="exportXlsButtonReceived" style="display:none">
                                    <span class="btn btn-inverse btn-compact btn-export-xls">
                                        XLS <i class="icon-download-2"></i>
                                    </span>
	                            </a>
	                        </li> --%>
	                    </ul>
	                </div>

                    <display:table defaultsort="1" defaultorder="ascending" name="closedClaimList" id="closedClaim" pagesize="10" class="table table-striped"
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
	  				  	<display:setProperty name="export.banner" value="<table width='100%'><tr><td align='left'><span class='export'><input type='hidden' class='selectedMode' value='received'>{0}</span></td></tr></table>" />
						<%-- Explicit displayTag.properties END --%>

						<display:column sortable="true" sortProperty="claimNumber" titleKey="claims.list.CC_CLAIM_NUMBER_COLUMN_HEADER" class="sortable promotionsColumn sorted ascending"  media="html">
							<%-- Non-Claim related deposits - eg. File Load, Discretionary Awards, reversal, sweepstakes, manager override - no claim details --%>
							<c:if test="${closedClaim.claimId == null }">
							  <c:choose>
								<c:when test="${closedClaim.discretionary}">
									<cms:contentText key="DISCRETIONARY_SUBMITTER" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.sweepstakes}">
									<cms:contentText key="SWEEPSTAKES_SUBMITTER" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.managerOverride}">
									<cms:contentText key="MGR_OVERRIDE_SUBMITTER" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.reversalDescription != null}">
									<cms:contentText key="REVERSED" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.stackRank}">
									<cms:contentText key="STACK_RANK_SUBMITTER" code="claims.history" />
								</c:when>
								<c:otherwise>
									<%-- Fileload --%>
									<cms:contentText key="SYSTEM_SUBMITTER" code="claims.history" />
								</c:otherwise>
							  </c:choose>
							</c:if>
							<%--  Product Claims with claim details --%>
							<c:if test="${closedClaim.claimId != null }">
							  <%
							  returnURLMapClosed.put( "mode", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "mode" ) );
							  returnURLMapClosed.put( "promotionId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionId" ) );
							  returnURLMapClosed.put( "startDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "startDate" ) );
							  returnURLMapClosed.put( "endDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "endDate" ) );
					         	String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/activityHistory.do", returnURLMapClosed );
								temp2 = (ProductClaimValueObject)pageContext.getAttribute( "closedClaim" );
								map.put( "claimId", temp2.getClaimId() );
								map.put( "returnURL", returnURL );
								map.put( "submitterId", ClientStateUtils.getParameterValue( request, clientStateMap1, "submitterId" ) );
								map.put( "open", ClientStateUtils.getParameterValue( request, clientStateMap1, "open" ) );
							  %>
							  <c:choose>
								<c:when test="${param.callingScreen == 'activityReport' }">
									<%
										pageContext.setAttribute( "detailUrl", ClientStateUtils.generateEncodedLink( "", "claim/productClaimDetail.do?method=display", map ) );
									%>
								</c:when>
								<c:otherwise>
									<%
										pageContext.setAttribute( "detailUrl", ClientStateUtils.generateEncodedLink( "", "claim/productClaimDetail.do?method=display", map ) );
									%>
								</c:otherwise>
							  </c:choose>
							  <c:choose>
								<c:when test="${closedClaim.managerOverride}">
									<cms:contentText key="MGR_OVERRIDE_SUBMITTER" code="claims.history" />
								</c:when>
								<c:otherwise>
									<a href="<c:out value="${detailUrl}"/>" class="crud-content-link">
										<c:out value="${closedClaim.claimNumber}" />
									</a>
								</c:otherwise>
							  </c:choose>
							</c:if>
						</display:column>

						<display:column sortable="true" sortProperty="claimNumber" titleKey="claims.list.CC_CLAIM_NUMBER_COLUMN_HEADER" class="sortable promotionsColumn sorted ascending" media="csv excel pdf">
							<c:if test="${closedClaim.claimId == null }">
							  <c:choose>
								<c:when test="${closedClaim.discretionary}">
									<cms:contentText key="DISCRETIONARY_SUBMITTER" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.sweepstakes}">
									<cms:contentText key="SWEEPSTAKES_SUBMITTER" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.managerOverride}">
									<cms:contentText key="MGR_OVERRIDE_SUBMITTER" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.reversalDescription != null}">
									<cms:contentText key="REVERSED" code="claims.history" />
								</c:when>
								<c:when test="${closedClaim.stackRank}">
									<cms:contentText key="STACK_RANK_SUBMITTER" code="claims.history" />
								</c:when>
								<c:otherwise>
									<%-- Fileload --%>
									<cms:contentText key="SYSTEM_SUBMITTER" code="claims.history" />
								</c:otherwise>
							  </c:choose>
							</c:if>
							<%--  Product Claims with claim details --%>
							<c:if test="${closedClaim.claimId != null }">
							  <c:choose>
								<c:when test="${closedClaim.managerOverride}">
									<cms:contentText key="MGR_OVERRIDE_SUBMITTER" code="claims.history" />
								</c:when>
								<c:otherwise>
									<c:out value="${closedClaim.claimNumber}" />
								</c:otherwise>
							  </c:choose>
							</c:if>
						</display:column>

						<display:column sortable="true" sortProperty="dateSubmitted" titleKey="claims.list.CC_DATE_SUBMITTED_COLUMN_HEADER" class="sortable dateSentColumn unsorted">
							<fmt:formatDate value="${closedClaim.dateSubmitted}" pattern="${JstlDatePattern}" />
							<c:if test="${ ! proxy}">
								<c:if test="${ closedClaim.proxyUser != null}">
                        			&nbsp;<cms:contentText key="BY" code="claims.list" />
									<c:out value="${closedClaim.proxyUser.firstName}" />&nbsp;<c:out value="${closedClaim.proxyUser.lastName}" />
								</c:if>
							</c:if>
						</display:column>

						<display:column property="promotionName" sortable="true" titleKey="claims.list.CC_PROMOTION_COLUMN_HEADER" class="sortable promotionsColumn sorted ascending" />

						<display:column sortProperty="earnings" sortable="true" titleKey="claims.list.CC_EARNINGS_COLUMN_HEADER" class="sortable dateSentColumn unsorted" media="html">
							<c:if test="${ closedClaim.earnings > 0 || closedClaim.earnings < 0 }">
								<c:out value="${closedClaim.earnings}" />  <c:out value="${closedClaim.awardTypeName}" />
							</c:if>
							<c:if test="${ closedClaim.earnings == null || closedClaim.earnings == 0  }">
								0  <c:out value="${closedClaim.awardTypeName}" />
                			</c:if>
						</display:column>

						<display:column sortProperty="earnings" sortable="true" titleKey="claims.list.CC_EARNINGS_COLUMN_HEADER" class="sortable dateSentColumn unsorted" media="csv excel pdf">
							<c:if test="${ closedClaim.earnings > 0 || closedClaim.earnings < 0 }">
								<c:out value="${closedClaim.earnings}" /> <c:out value="${closedClaim.awardTypeName}" />
							</c:if>
							<c:if test="${ closedClaim.earnings == null || closedClaim.earnings == 0  }">
								0  <c:out value="${closedClaim.awardTypeName}" />
                			</c:if>
						</display:column>
					</display:table>
                  </div>
                </div>
            </div>
