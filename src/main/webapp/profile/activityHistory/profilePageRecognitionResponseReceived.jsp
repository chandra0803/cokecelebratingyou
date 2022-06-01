<%@ include file="/include/taglib.jspf" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject" %>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<%
    Map paramMapReceived = new HashMap();
	Map returnURLMapReceived = new HashMap();
    RecognitionHistoryValueObject tempReceived;
%>

<div id="recognitionsReceived" class="tab-pane fade active in">

			<c:if test="${isShowGraph}">
            <div class='row-fluid'>
                <div class='span12'>
                    <div class="profilePageActivityHistoryResultsGraph">
                        <h4><cms:contentText key="RECOGNITIONS_RECEIVED_COUNT" code="profile.activity.history" /><span class="asOfText"><cms:contentTemplateText key="AS_OF" code="profile.activity.history" args="${refreshDate}"/>&nbsp;<cms:contentText key="CST" code="profile.activity.history" /></span></h4>

                        <p><cms:contentText key="RECOGNITIONS_RECEIVED" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant"> ${recogntionReceivedCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${recogntionReceivedCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="ORG_UNIT_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant">${recogntionReceivedAverageForMyTeamCount} </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${recogntionReceivedAverageForMyTeamCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="COMPANY_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant"> ${recogntionReceviedAverageForAllPromotions} </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${recogntionReceviedAverageForAllPromotions}"></div>
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
                            <%
                              Map<String,Object> receivedMap = (Map)request.getAttribute("receiveSentValueObj");
                              
                              String printUrl = "profilePageActivityHistoryPrintPdf.do?method=printAsPdf&recog=received";
                              pageContext.setAttribute( "printPdfUrl", ClientStateUtils.generateEncodedLink( "", printUrl, receivedMap ) );
                              List list = (ArrayList)request.getAttribute("receivedvalueObjects");
      						  pageContext.setAttribute("size",list.size());
                            %>
                            <c:if test="${size > 0}">
                            <li class="export pdf">
                                <a href="<c:out value='${printPdfUrl}'/>" class="exportPdfDetailView">
                                    <span><cms:contentText key="DETAIL_VIEW" code="recognition.history"/></span>
                                    <span class="btn btn-inverse btn-compact btn-export-pdf">
                                        <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                                    </span>
                                </a>
                            </li>
                            </c:if>
                            <li class="export pdf">
                                <a href="layout.html" class="exportPdfButtonReceived" style="display:none">
                                    <span><cms:contentText key="SUMMARY_VIEW" code="recognition.history"/></span>
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

					<display:table defaultsort="2" defaultorder="descending" name="receivedvalueObjects" id="valueObject" class="table table-striped"
						   pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true">
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

						<%-- the name of the promotion --%>
						<display:column titleKey="recognition.history.PROMOTION" class="sortable promotionsColumn sorted ascending" sortable="true">
							<c:choose>
								<c:when test="${valueObject.isBadgePromotion }">
									<c:out value="${valueObject.promotion.promotionName}"></c:out>
								</c:when>
								<c:otherwise>
									<c:out value="${valueObject.promotion.name }"/>
								</c:otherwise>
							</c:choose>
						</display:column>

						<%-- the date that the claim was submitted --%>
						<display:column titleKey="recognition.history.DATE_SUBMITTED" class="sortable dateSentColumn unsorted" sortable="true" sortProperty="submissionDate" media="html">
						  <c:if test="${valueObject.claim != null }">
							<%
							returnURLMapReceived.put( "mode", "received" );
							returnURLMapReceived.put( "promotionId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionId" ) );
							returnURLMapReceived.put( "startDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "startDate" ) );
							returnURLMapReceived.put( "endDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "endDate" ) );
						    String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/activityHistory.do", returnURLMapReceived );
						    tempReceived = (RecognitionHistoryValueObject)pageContext.getAttribute("valueObject");
								RecognitionHistoryForm tempForm = (RecognitionHistoryForm)request.getAttribute("recognitionHistoryForm");
								paramMapReceived.put( "promotionTypeCode", tempForm.getPromotionTypeCode() );
								paramMapReceived.put( "queryPromotionId", tempForm.getPromotionId() );
								paramMapReceived.put( "queryStartDate", tempForm.getStartDate() );
								paramMapReceived.put( "queryEndDate", tempForm.getEndDate() );
								paramMapReceived.put( "mode", tempForm.getMode() );
								paramMapReceived.put( "claimId", tempReceived.getClaim().getId() );
								paramMapReceived.put( "claimRecipientId", tempReceived.getClaimRecipient().getId() );
								paramMapReceived.put( "returnURL", returnURL );
								paramMapReceived.put( "referralPage", "activityHistory" );
								pageContext.setAttribute("detailUrl", ClientStateUtils.generateEncodedLink( "", "claim/claimDetail.do", paramMapReceived ) );
							%>
						    <a href="<c:out value='${detailUrl}'/>" >
						  </c:if>
						  <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}" />
						  <c:if test="${valueObject.claim != null }">
						    </a>
						  </c:if>
						</display:column>

						<display:column titleKey="recognition.history.DATE_SUBMITTED" class="sortable dateSentColumn unsorted" sortable="true" sortProperty="submissionDate" media="csv excel pdf">
						    <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}" />
						</display:column>

						<%-- the name of the user who submitted the claim --%>
						<display:column titleKey="recognition.history.SENDER" class="sortable receiverColumn unsorted"  sortable="true">
						  <c:choose>
						  	<c:when test="${valueObject.isBadgePromotion }">
									<c:out value="${valueObject.submitter.lastName}" />
							</c:when>
						    <c:when test="${valueObject.submitter.lastName == null}">
						      <c:choose>
							    <c:when test="${valueObject.discretionary}">
							        <cms:contentText key="DISCRETIONARY_SUBMITTER" code="recognition.history"/>
							    </c:when>
							    <c:when test="${valueObject.sweepstakes}">
							        <cms:contentText key="SWEEPSTAKES_SUBMITTER" code="recognition.history"/>
							    </c:when>
							    <c:when test="${valueObject.reversalDescription != null }">
									<cms:contentText key="REVERSED" code="recognition.history"/>
								</c:when>
							    <c:otherwise>
							        <%-- file load --%>
							        <cms:contentText key="SYSTEM_SUBMITTER" code="recognition.history"/>
							    </c:otherwise>
							  </c:choose>
						    </c:when>
						    <c:otherwise>
						        <c:out value="${valueObject.submitter.nameLFMWithComma}"/>
						    </c:otherwise>
						  </c:choose>
						</display:column>

						<%-- the award amount --%>
						 <display:column titleKey="recognition.history.AWARD" class="sortable awardColumn unsorted" sortable="true">
						  <c:if test="${valueObject.merchGiftCodeActivityList != null }">
						    <c:forEach items="${valueObject.merchGiftCodeActivityList}" var="merchGiftCodeActivity">
						      <c:if test="${merchGiftCodeActivity.merchOrder != null }">
						        <c:choose>
						          <c:when test="${merchGiftCodeActivity.merchOrder.merchGiftCodeType.code == 'level' }">
						              <c:if test="${merchGiftCodeActivity.merchOrder.promoMerchProgramLevel != null }">
						                  <cms:contentText key="LEVEL_NAME" code="${ merchGiftCodeActivity.merchOrder.promoMerchProgramLevel.cmAssetKey}"/><br>
						              </c:if>
						          </c:when>
						          <c:otherwise>
						              <c:out value="${ merchGiftCodeActivity.merchOrder.productDescription}"/><br>
						          </c:otherwise>
						        </c:choose>
						      </c:if>
						    </c:forEach>
						  </c:if>
						  <c:if test="${ valueObject.awardQuantity > 0 || valueObject.awardQuantity < 0 }">
								<c:choose>
									<c:when test="${valueObject.isBadgePromotion }">
										<c:out value="${valueObject.awardQuantity}" />
										<c:out value="${valueObject.awardTypeName}" />
									</c:when>
									<c:otherwise>
										<%-- BugFix 17702 Remove the &nbsp; so that display tag exports Properly(There is an problem with parsing &nbsp; for exports) --%>
										<c:if test="${!valueObject.reversal}">
											<c:out value="${valueObject.awardQuantity}" /> <c:out value="${valueObject.awardTypeName}" />
										</c:if>
										<c:if test="${valueObject.reversal}">
											<c:out value="${valueObject.awardQuantity}" />&nbsp;<cms:contentText
												key="REVERSE_SYMBOL" code="participant.transactionhistory" />&nbsp;<c:out
												value="${valueObject.awardTypeName}" />
										</c:if>
									</c:otherwise>
								</c:choose>
						</c:if>
						</display:column>
					</display:table>
                </div>
            </div>
</div>
