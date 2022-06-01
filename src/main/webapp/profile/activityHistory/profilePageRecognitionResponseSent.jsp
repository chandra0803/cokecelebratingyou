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
    Map paramMapSent = new HashMap();
	Map returnURLMapSent = new HashMap();
    RecognitionHistoryValueObject tempSent;
    String promotionId="";
%>
<div id="recognitionsSent" class="tab-pane fade in">

        	<c:if test="${isShowGraph}">
            <div class='row-fluid'>
                <div class='span12'>
                    <div class="profilePageActivityHistoryResultsGraph">
                        <h4><cms:contentText key="RECOGNITIONS_SENT_COUNT" code="profile.activity.history" /><span class="asOfText"><cms:contentTemplateText key="AS_OF" code="profile.activity.history" args="${refreshDate}"/>&nbsp;<cms:contentText key="CST" code="profile.activity.history" /></span></h4>
                        <p><cms:contentText key="RECOGNITIONS_SENT" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant">${recogntionSentCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${recogntionSentCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="ORG_UNIT_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant"> ${recogntionSentAverageForMyTeamCount}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${recogntionSentAverageForMyTeamCount}"></div>
                                </div>
                            </div>
                        </div>
                        <p><cms:contentText key="COMPANY_AVERAGE" code="profile.activity.history" /></p>
                        <div class="row-fluid">
                            <div class="span4">
                            	<div class="quant">  ${recogntionSentAverageForAllPromotions}  </div>
                                <div class="progress progress-info">
                                    <div style="width: 0%" class="bar" data-complete="${recogntionSentAverageForAllPromotions}"></div>
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
                              Map<String,Object> sentMap = (Map)request.getAttribute("receiveSentValueObj");
                              
                              String printUrl = "profilePageActivityHistoryPrintPdf.do?method=printAsPdf&recog=sent";
                              pageContext.setAttribute( "printPdfUrl", ClientStateUtils.generateEncodedLink( "", printUrl, sentMap ) );
                              List list = (ArrayList)request.getAttribute("sentvalueObjects");
      						  pageContext.setAttribute("size",list.size());
                            %>
                            <c:if test="${size > 0}">
                            <li class="export pdf">
                                <a href="<c:out value='${printPdfUrl}'/>" class="exportPdfDetailView">
                                    <span><cms:contentText key="DETAIL_VIEW" code="recognition.history" /></span>
                                    <span class="btn btn-inverse btn-compact btn-export-pdf">
                                        <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                                    </span>
                                </a>
                            </li>
                            </c:if>
                            <li class="export pdf">
                                <a href="layout.html" class="exportPdfButtonSent" style="display:none">
                                    <span><cms:contentText key="SUMMARY_VIEW" code="recognition.history"/></span>
                                    <span class="btn btn-inverse btn-compact btn-export-pdf">
                                        <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                                    </span>
                                </a>
                            </li>
                            <li class="export csv">
                                <a href="layout.html" class="exportCsvButtonSent" style="display:none">
                                    <span class="btn btn-inverse btn-compact btn-export-csv">
                                        <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
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

				  	<display:table defaultsort="2" defaultorder="descending" class="table table-striped" name="sentvalueObjects" id="sentRecognition" pagesize="10" sort="list"
							requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true">
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

						<%-- the name of the promotion --%>
						<display:column titleKey="recognition.history.PROMOTION" property="promotion.name" class="sortable promotionsColumn sorted ascending" sortable="true" />

						<%-- the date that the claim was submitted --%>
						<display:column titleKey="recognition.history.DATE_SUBMITTED" class="sortable dateSentColumn unsorted" sortable="true" sortProperty="submissionDate" media="html">
						  <c:if test="${sentRecognition.claim != null }">
						  <%
							promotionId=ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionId" ) ;
							returnURLMapSent.put( "mode", "sent" );
							returnURLMapSent.put( "promotionId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionId" ) );
							returnURLMapSent.put( "startDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "startDate" ) );
							returnURLMapSent.put( "endDate", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "endDate" ) );
							    String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/activityHistory.do", returnURLMapSent );
							    tempSent = (RecognitionHistoryValueObject)pageContext.getAttribute( "sentRecognition" );
								RecognitionHistoryForm tempForm = (RecognitionHistoryForm)request.getAttribute( "recognitionHistoryForm" );
								paramMapSent.put( "promotionTypeCode", tempForm.getPromotionTypeCode() );
								paramMapSent.put( "queryPromotionId", tempForm.getPromotionId() );
								paramMapSent.put( "queryStartDate", tempForm.getStartDate() );
								paramMapSent.put( "queryEndDate", tempForm.getEndDate() );
								paramMapSent.put( "mode", tempForm.getMode() );
								paramMapSent.put( "claimId", tempSent.getClaim().getId() );
								paramMapSent.put( "claimRecipientId", tempSent.getClaimRecipient().getId() );
								paramMapSent.put( "returnURL", returnURL );
								paramMapSent.put( "referralPage", "activityHistory" );
								pageContext.setAttribute( "detailUrl", ClientStateUtils.generateEncodedLink( "", "claim/claimDetail.do", paramMapSent ) );
							%>
							<a href="<c:out value='${detailUrl}'/>" >
						  </c:if>
						  <fmt:formatDate value="${sentRecognition.submissionDate}" pattern="${JstlDatePattern}" />
						   <c:if test="${sentRecognition.claim != null }">
							</a>
						  </c:if>
						  <c:if test="${sentRecognition.proxyUser != null}">
							<br>
							<cms:contentText key="BY" code="recognition.history" />&nbsp;<c:out value="${sentRecognition.proxyUser.nameLFMWithComma}" />
						  </c:if>
						</display:column>

						<display:column titleKey="recognition.history.DATE_SUBMITTED" class="sortable dateSentColumn unsorted" sortable="true" sortProperty="submissionDate" media="csv excel pdf">
							<fmt:formatDate value="${sentRecognition.submissionDate}" pattern="${JstlDatePattern}" />
							<c:if test="${sentRecognition.submitter.id != recognitionHistoryForm.submitterId}">
								<cms:contentText key="BY" code="recognition.history" />
								<c:choose>
									<c:when test="${sentRecognition.proxyUser != null}">
										<c:out value="${sentRecognition.proxyUser.nameLFMWithComma}" />
									</c:when>
									<c:otherwise>
										<c:out value="${sentRecognition.submitter.nameLFMWithComma}" />
									</c:otherwise>
								</c:choose>
							</c:if>
						</display:column>

						<%-- the name of the recognized participant --%>
						<display:column titleKey="recognition.history.RECEIVER"  class="sortable receiverColumn unsorted" sortable="true">
							<c:out value="${sentRecognition.claimRecipient.recipientDisplayName}"/>
						</display:column>

						<%-- the award amount --%>
						<display:column titleKey="recognition.history.AWARD"  class="sortable awardColumn unsorted" sortable="true">
						  <c:if test="${sentRecognition.merchGiftCodeActivityList != null }">
							<c:forEach items="${sentRecognition.merchGiftCodeActivityList}" var="merchGiftCodeActivity">
							  <c:if test="${merchGiftCodeActivity.merchOrder != null }">
							    <c:if test="${merchGiftCodeActivity.merchOrder.merchGiftCodeType.code == 'product' or empty merchGiftCodeActivity.merchOrder.merchGiftCodeType   }">
							        <c:out value="${ merchGiftCodeActivity.merchOrder.productDescription}"/><br>
							    </c:if>
							    <c:if test="${merchGiftCodeActivity.merchOrder.merchGiftCodeType.code == 'level' }">
							        <c:if test="${merchGiftCodeActivity.merchOrder.promoMerchProgramLevel != null }">
							            <cms:contentText key="LEVEL_NAME" code="${ merchGiftCodeActivity.merchOrder.promoMerchProgramLevel.cmAssetKey}"/><br>
							        </c:if>
							    </c:if>
							  </c:if>
							</c:forEach>
						  </c:if>
						  <c:if test="${ sentRecognition.awardQuantity > 0 || sentRecognition.awardQuantity < 0 }">
							<c:if test="${!sentRecognition.reversal}">
							    <c:out value="${sentRecognition.awardQuantity}"/> <c:out value="${sentRecognition.awardTypeName}"/>
							</c:if>
							<c:if test="${sentRecognition.reversal}">
							    <c:out value="${sentRecognition.awardQuantity}"/>&nbsp;<cms:contentText key="REVERSE_SYMBOL" code="participant.transactionhistory"/>&nbsp;<c:out value="${sentRecognition.awardTypeName}"/>

							</c:if>
						  </c:if>
						</display:column>

						<%-- the status of the recognition claim --%>
						<display:column titleKey="recognition.history.STATUS" property="approvalStatusType.name" class="sortable statusColumn unsorted" sortable="true" />
					</display:table>
                </div>
            </div>
     </div>
