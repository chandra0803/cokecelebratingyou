<%@page import="com.biperf.core.domain.claim.Claim"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.domain.claim.NominationClaim"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<%
    Map paramMapReceived = new HashMap();
	Map returnURLMapReceived = new HashMap();
    RecognitionHistoryValueObject tempReceived;
%>

    <div id="nominationsReceived" class="tab-pane fade in">


          	<c:if test="${isShowGraph}">
            <div class='row-fluid'>
              <div class='span12'>
                <div class="profilePageActivityHistoryResultsGraph">
                    <h4><cms:contentText key="NOMINATIONS_RECEIVED_COUNT" code="profile.activity.history" /><span class="asOfText"><cms:contentTemplateText key="AS_OF" code="profile.activity.history" args="${refreshDate}" />&nbsp;<cms:contentText key="CST" code="profile.activity.history" /></span></h4>
                    <p><cms:contentText key="NOMINATIONS_RECEIVED" code="profile.activity.history" /></p>
                    <div class="row-fluid">
                        <div class="span4">
                        	<div class="quant">${nominationReceivedCount}</div>
                            <div class="progress progress-info">
                                <div style="width: 0%" class="bar" data-complete="${nominationReceivedCount}"></div>
                            </div>
                        </div>
                    </div>
                    <p><cms:contentText key="TEAM_AVERAGE" code="profile.activity.history" /></p>
                    <div class="row-fluid">
                         <div class="span4">
                        	<div class="quant">${nominationReceivedCountAveargeForMyTeamCount}</div>
                            <div class="progress progress-info">
                                <div style="width: 0%" class="bar" data-complete="${nominationReceivedCountAveargeForMyTeamCount}"></div>
                            </div>
                        </div>
                    </div>
                    <p><cms:contentText key="COMPANY_AVERAGE" code="profile.activity.history" /></p>
                    <div class="row-fluid">
                        <div class="span4">
                        	<div class="quant">${nominationReceivedAveargeForMyCompanyCount}</div>
                            <div class="progress progress-info">
                                <div style="width: 0%" class="bar" data-complete="${nominationReceivedAveargeForMyCompanyCount}"></div>
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

                <display:table defaultsort="1" defaultorder="ascending" class="table table-striped" name="receivedvalueObjects" id="receivedvalueObjects" pagesize="10" sort="list"
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
	  				<display:setProperty name="export.excel.label" value="XLS" />
	  				<display:setProperty name="export.pdf" value="true" />
	  				<display:setProperty name="export.pdf.filename" value="export.pdf" />
	  				<display:setProperty name="export.pdf.include_header" value="true" />
	  				<display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
	  				<display:setProperty name="export.banner" value="<table width='100%'><tr><td align='left'><span class='export'><input type='hidden' class='selectedMode' value='received'>{0}</span></td></tr></table>" />
					<%-- Explicit displayTag.properties END --%>
					<%-- the name of the promotion --%>
					<display:column titleKey="nomination.history.PROMOTION" property="promotion.name" class="sortable promotionsColumn sorted ascending"  sortable="true" />

					<%-- the date that the claim was approved --%>
					<display:column titleKey="nomination.history.DATE_APPROVED" class="sortable dateSentColumn unsorted"  sortable="true" sortProperty="submissionDate" media="html">
					  <c:if test="${receivedvalueObjects.claim != null || receivedvalueObjects.claims != null}">
						<%
							returnURLMapReceived.put( "mode", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "mode" ) );
							returnURLMapReceived.put( "promotionId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionId" ) );
							returnURLMapReceived.put( "startDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "startDate" ) );
							returnURLMapReceived.put( "endDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "endDate" ) );
							String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/activityHistory.do", returnURLMapReceived );

         					tempReceived = (RecognitionHistoryValueObject)pageContext.getAttribute( "receivedvalueObjects" );
			      			RecognitionHistoryForm tempForm = (RecognitionHistoryForm)request.getAttribute( "recognitionHistoryForm" );
			      			paramMapReceived.put( "promotionTypeCode", tempForm.getPromotionTypeCode() );
			      			paramMapReceived.put( "queryPromotionId", tempForm.getPromotionId() );
			      			paramMapReceived.put( "queryStartDate", tempForm.getStartDate() );
			      			paramMapReceived.put( "queryEndDate", tempForm.getEndDate() );
			      			paramMapReceived.put( "mode", tempForm.getMode() );
			      			if( tempReceived.getClaim() != null )
			      			  paramMapReceived.put( "claimId", tempReceived.getClaim().getId() );
			      			else
			      			{
			      	        for( Iterator nominationClaimIter = tempReceived.getClaims().iterator(); nominationClaimIter.hasNext(); )
			      	        {
			      	          NominationClaim nominationClaim =  (NominationClaim)nominationClaimIter.next();
			      	          paramMapReceived.put( "claimId", nominationClaim.getId() );
			      	        }
			      			}
			      		    if( tempReceived.getClaimRecipient() != null )
			      			  paramMapReceived.put( "claimRecipientId", tempReceived.getClaimRecipient().getId() );
			      			paramMapReceived.put( "returnURL", returnURL );
			      			paramMapReceived.put( "referralPage", "activityHistory" );
			      			paramMapReceived.put( "activityId", tempForm.getActivityId( ) );
			      			paramMapReceived.put( "winnerId", tempForm.getSubmitterId() );
			      			paramMapReceived.put( "promotionId", tempReceived.getPromotion().getId(  ) );
			      			paramMapReceived.put( "approvalRound", tempReceived.getApprovalRound( ) );
			      			pageContext.setAttribute( "detailUrl", ClientStateUtils.generateEncodedLink( "", "nomination/viewNominationPastWinnersList.do?method=nominationWinnerDetailsPage", paramMapReceived ) );
						%>
						<a href="<c:out value='${detailUrl}'/>" >
					  </c:if>
					  <fmt:formatDate value="${receivedvalueObjects.approvalDate}" pattern="${JstlDatePattern}" />
					  <c:if test="${receivedvalueObjects.claim != null }">
						</a>
					  </c:if>
					</display:column>

					<display:column titleKey="nomination.history.DATE_APPROVED" class="sortable dateSentColumn unsorted"  sortable="true" sortProperty="submissionDate" media="csv excel pdf">
					  <fmt:formatDate value="${receivedvalueObjects.approvalDate}" pattern="${JstlDatePattern}" />
					</display:column>

					<%-- the name of the nominators --%>
					<display:column titleKey="nomination.history.SENDER" class="sortable receiverColumn unsorted"  sortable="true">
					  <%-- claim related --%>
					  <c:if test="${receivedvalueObjects.claims != null}">
						<c:forEach items="${receivedvalueObjects.claims}" var="nominationClaim" varStatus="status"><c:out value="${nominationClaim.submitter.nameLFMWithComma}" />&nbsp;(<c:out value="${nominationClaim.node.name}" />)<c:if test="${!status.last}">;<br /></c:if></c:forEach>
					  </c:if>
					  <%-- non claim related --%>
					  <c:if test="${receivedvalueObjects.claims == null}">
						<c:choose>
						  <c:when test="${receivedvalueObjects.submitter.lastName == null}">
							<c:choose>
							  <c:when test="${receivedvalueObjects.discretionary}">
								<cms:contentText key="DISCRETIONARY_SUBMITTER" code="nomination.history" />
							  </c:when>
							  <c:when test="${receivedvalueObjects.sweepstakes}">
								<cms:contentText key="SWEEPSTAKES_SUBMITTER" code="recognition.history" />
							  </c:when>
							  <c:otherwise>
								<cms:contentText key="SYSTEM_SUBMITTER" code="nomination.history" />
							  </c:otherwise>
							</c:choose>
						  </c:when>
						  <c:otherwise>
							<c:out value="${receivedvalueObjects.submitter.nameLFMWithComma}" />
						  </c:otherwise>
						</c:choose>
					  </c:if>
					</display:column>

					<%-- the award amount html media--%>
					<display:column titleKey="recognition.history.AWARD" class="sortable awardColumn unsorted"  sortable="true" media="html">
					  		<c:choose>
							  <c:when test="${receivedvalueObjects.awardTypeCode == 'points' && receivedvalueObjects.awardQuantity gt 0 }">
								<c:out value="${receivedvalueObjects.awardQuantity}" />&nbsp;<c:out value="${receivedvalueObjects.awardTypeName}" />
							  </c:when>
							  <c:when test="${receivedvalueObjects.awardTypeCode == 'cash' && receivedvalueObjects.cashAwardQuantity != null && receivedvalueObjects.cashAwardQuantity.unscaledValue() gt 0}">
								<c:out value="${receivedvalueObjects.cashAwardQuantity}" />&nbsp;<c:out value="${receivedvalueObjects.currencyCode}" />
							  </c:when>
							  <c:otherwise>
								<c:out value="${receivedvalueObjects.payoutDescription}" />
							  </c:otherwise>
							</c:choose>
					</display:column>

					<%-- the award amount csv excel pdf media--%>
					<display:column titleKey="recognition.history.AWARD" class="sortable awardColumn unsorted"  sortable="true" media="csv excel pdf">
							<c:choose>
							  <c:when test="${receivedvalueObjects.awardTypeCode == 'points' && receivedvalueObjects.awardQuantity gt 0 }">
								<c:out value="${receivedvalueObjects.awardQuantity}" />&nbsp;<c:out value="${receivedvalueObjects.awardTypeName}" />
							  </c:when>
							  <c:when test="${receivedvalueObjects.awardTypeCode == 'cash' && receivedvalueObjects.cashAwardQuantity != null && receivedvalueObjects.cashAwardQuantity.unscaledValue() gt 0}">
								<c:out value="${receivedvalueObjects.cashAwardQuantity}" />&nbsp;<c:out value="${receivedvalueObjects.currencyCode}" />
							  </c:when>
							  <c:otherwise>
								<c:out value="${receivedvalueObjects.payoutDescription}" />
							  </c:otherwise>
							</c:choose>
					</display:column>
				</display:table>
              </div>
            </div>
          </div>
