<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.approvals.ApprovalsRecognitionListForm"%>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.domain.claim.ClaimRecipient" %>
<%@ page import="com.biperf.core.domain.promotion.PromoMerchCountry"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== APPROVALS: APPROVALS PENDING NOMINATIONS PAGE ======== -->

<div id="approvalsPagePendingNominations" class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<h2><cms:contentText key="TITLE" code="recognition.approval.list"/></h2>
		</div>
	</div>
	<div id="pendingNominationsWrapper" class="approvalSearchWrapper">
		<div class="row-fluid">
			<div class="span12">

				<div id="approvalsErrorBlock" class="alert alert-block alert-error" style="display:none;">
	                <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
	                <ul>
	                    <html:messages id="actionMessage" >
	         				<c:set var="serverReturnedError" value="true"/>
	    					<li>${actionMessage}</li>
						</html:messages>
	                </ul>
	            </div>

	            <c:if test="${ saveOccurred }">
					<div id="approvalsSaveSuccessBlock" class="alert alert-block alert-info">
						<h4><cms:contentText key="SAVE_OCCURRED" code="recognition.approval.list"/></h4>
					</div>
				</c:if>

            	<h3><cms:contentText code="recognition.approval.list" key="SEARCH_RECOGNITIONS"/></h3>
        	</div>
			<div class="span12">
	            <ul class="export-tools fr">
	                <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT" /> <i class="icon-printer"></i></a></li>
	            </ul>
	        </div>
	    </div>

		<div class="row-fluid">
	   		<div class="span12">
				<html:form styleId="approvalsSearchForm" styleClass="form-horizontal" action="approvalsRecognitionListMaintain.do?method=prepareUpdate">
					<div class="control-group">

						<label class="control-label">
							<cms:contentText code="recognition.approval.list" key="SUBMITTED_BETWEEN"/>
						</label>

						<div class="controls">
					        <div class="input-append input-append-inside datepickerTrigger showTodayBtn"
			                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
			                    data-date-language="<%=UserManager.getUserLocale()%>"
			                    data-date-startdate=""
			                    data-date-todaydate=""
			                    data-date-autoclose="true">
			                    <html:text property="startDate" styleId="dateStart" styleClass="input-medium" readonly="true" />
			                    <button class="btn awardDateIcon">
			                        <i class="icon-calendar"></i>
			                    </button>
			                </div>

							<cms:contentText code="recognition.approval.list" key="AND"/>

			                <div class="input-append input-append-inside datepickerTrigger showTodayBtn"
			                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
			                    data-date-language="<%=UserManager.getUserLocale()%>"
			                    data-date-startdate=""
			                    data-date-todaydate=""
			                    data-date-autoclose="true">
			                    <html:text property="endDate" styleId="dateEnd"  styleClass="input-medium" readonly="true"/>
			                    <button class="btn awardDateIcon">
			                        <i class="icon-calendar"></i>
			                    </button>
			                </div>
			            </div>

					</div>

					<div class="control-group promotionGroup">

						<label class="control-label">
							<cms:contentText code="recognition.approval.list" key="FOR_PROMO"/>
						</label>

						<div class="controls">
							<html:select property="promotionId" styleId="promotionSelected" >
				                <html:option value=""><cms:contentText code="recognition.approval.list" key="ALL_PROMOTIONS"/></html:option>
				                <html:options collection="promotionList" property="id" labelProperty="name" />
				      		</html:select>
			      		</div>

					</div>

					<div class="control-group">

						<label class="control-label">&nbsp;</label>

						<div class="controls">
							<html:submit styleClass="btn btn-primary showActivityBtn"><cms:contentText code="recognition.approval.list" key="SHOW_ACTIVITY"/></html:submit>

							<c:set var="totalPages" value="${approvalsRecognitionListForm.listPageInfo.totalPages}"/>
							<c:set var="currentPage" value="${approvalsRecognitionListForm.listPageInfo.currentPage}"/>
							<input type="hidden" id="currentPage" name="approvalsRecognitionListForm.listPageInfo.currentPage" value="${currentPage}">
							<html:hidden property="requestedPage" styleId="requestedPage" value="1"/>
							<html:hidden property="listPageInfo.resultsPerPage"/>
						</div>

					</div>
				</html:form>

				<div id="nominationsTableWrapper" class="nominationsTableWrapper">
					<div class="row-fluid">
						<ul class="export-tools approvalsExportIconsWrapper pushDown">
	                        <li class="export csv">
							    <a href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsRecognitionListMaintain.do?method=extractAsCsv">
	                                <span class="btn btn-inverse btn-compact btn-export-csv">
	                                    <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
	                                </span>
	                            </a>
	                        </li>
	                        <li class="export pdf">
							    <a href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsRecognitionListMaintain.do?method=extractAsPdf">
	                                <span class="btn btn-inverse btn-compact btn-export-pdf">
	                                    <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
	                                </span>
	                            </a>
	                        </li>
						</ul>
					</div>

					<div class="row-fluid">
						<c:if test="${totalPages > 1}">
							<div id="paginationControls" class="pagination pagination-right">
								<ul>
									<c:if test="${currentPage > 1}">
									    <li class="first" data-page="1">
									        <a href="#"><i class="icon-double-arrows-1-left"></i></a>
									    </li>

									    <li class="prev" data-page="${currentPage - 1}">
									        <a href="#"><i class="icon-arrow-1-left"></i><cms:contentText code="recognition.approval.list" key="PREV"/></a>
									    </li>
								    </c:if>

								    <c:if test="${currentPage > 2}">
									    <li data-page="${currentPage - 2}">
									        <a href="#">${currentPage - 2}</a>
									    </li>
								    </c:if>

								    <c:if test="${currentPage > 1}">
									    <li data-page="${currentPage - 1}">
									        <a href="#">${currentPage - 1}</a>
									    </li>
								    </c:if>

								    <li data-page="${currentPage}">
								        <a href="#">${currentPage}</a>
								    </li>

								    <c:if test="${currentPage < totalPages}">
									    <li data-page="${currentPage + 1}">
									        <a href="#">${currentPage + 1}</a>
									    </li>
								    </c:if>

								    <c:if test="${currentPage < (totalPages - 1)}">
									    <li data-page="${currentPage + 2}">
									        <a href="#">${currentPage + 2}</a>
									    </li>
									</c:if>

									<c:if test="${currentPage < (totalPages - 2)}">
									    <li class="disabled gap" data-page="">
									        <a href="#"><cms:contentText code="recognition.approval.list" key="DISABLED_RANGE"/></a>
									    </li>

									    <li data-page="${totalPages}">
									        <a href="#">${totalPages}</a>
									    </li>
								    </c:if>

									<c:if test="${currentPage < totalPages}">
									    <li class="next" data-page="${currentPage + 1}">
									        <a href="#"><cms:contentText code="recognition.approval.list" key="NEXT"/><i class="icon-arrow-1-right"></i></a>
									    </li>
									    <li class="last" data-page="${totalPages}">
									        <a href="#"><i class="icon-double-arrows-1-right"></i></a>
									    </li>
									</c:if>
								</ul>
							</div>
						</c:if>
					</div>

					<html:form styleId="approvalsCalcGiverForm" action="approvalsRecognitionListUpdate?method=saveApprovals">
						<table class="table table-striped">
							<thead>
								<tr>
									<th><cms:contentText code="recognition.approval.list" key="NOMINEE"/></th>
									<c:choose>
										<c:when test="${cashEnabledPromo}">
											<th><cms:contentText code="coke.cash.recognition" key="RECIPIENT_AWARD_VAL" /></th>
											<th><cms:contentText code="coke.cash.recognition" key="USD_AWARD_VAL" /></th>
										</c:when>
										<c:otherwise><th><cms:contentText code="recognition.approval.list" key="AWARD_LBL" /></th></c:otherwise>
									</c:choose>
									<th><cms:contentText code="recognition.approval.list" key="NOMINATOR"/></th>
									<th><cms:contentText code="recognition.approval.list" key="DATE_SUBMITTED"/></th>
									<th class="hasSelects"><cms:contentText code="recognition.approval.list" key="STATUS"/></th>
								</tr>
							</thead>

							<c:if test="${fn:length(approvables) > 1}">
							<div style="display: none" class="tableSwipeHelp">
		                        <i class="icon-arrow-1-right"></i>
		                        <span><cms:contentText code="recognition.approval.list" key="SWIPE_FOR_MORE"/></span>
		                        <i class="icon-arrow-1-left"></i>
		                    </div>
							</c:if>

							<tbody>
								<c:choose>
									<c:when test="${fn:length(approvables) < 1}">
										<tr>
											<td colspan="6">
												<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
											</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${approvables}" var="claim" varStatus="claimStatus">
											<tr>
												<c:forEach items="${claim.claimRecipients}" var="claimRecipient" varStatus="claimRecipientStatus">
													<%
														Map<String,Object> parameterMap = new HashMap<String,Object>();
												   		Claim temp = (Claim)pageContext.getAttribute( "claim" );
														parameterMap.put( "claimId", temp.getId().toString() );
														 pageContext.setAttribute( "approveClaimUrl", ClientStateUtils.generateEncodedLink( "", "approvalsRecognitionDetails.do?method=prepareUpdate", parameterMap ) );
													%>

													<td>
														<a href="${approveClaimUrl}"><c:out value="${claimRecipient.recipient.lastName}" />,&nbsp;<c:out value="${claimRecipient.recipient.firstName}" /></a>
													</td>

													<td>
														<c:choose>
														<c:when test="${cashEnabledPromo}">
																${claimRecipient.awardQuantity}&nbsp;${claimRecipient.cashCurrencyCode}		
															</c:when>
															<c:when test="${ claim.promotion.awardType.merchandiseAwardType }">
																<c:set var="claimRecipientIdString">${claimRecipient.id}</c:set>
																<c:set var="merchLevel" value="${approvalsRecognitionListForm.recognitionApprovalFormByClaimRecipientIdString[claimRecipientIdString].programLevel}" />
																<c:choose>
																	<c:when test="${merchLevel == null}">
																		<cms:contentText code="recognition.approval.list" key="NOT_SELECTED" />
																	</c:when>
																	<c:otherwise>
																		<c:out value="${merchLevel.displayLevelName} (${merchLevel.maxValue})"/>
																	</c:otherwise>
																</c:choose>
															</c:when>
															<c:when test="${ not empty claimRecipient.awardQuantity }">
																<c:out value="${claimRecipient.awardQuantity}" />
															</c:when>
															<c:when test="${ claimRecipient.awardQuantity == null && claim.promotion.awardActive}">
																 <cms:contentText code="recognition.approval.list" key="NOT_SELECTED" />
															</c:when>
															<c:otherwise>
															     <cms:contentText code="system.general" key="NOT_AVAILABLE" />
															</c:otherwise>
														</c:choose>
												    </td>
												    <c:choose>
														<c:when test="${cashEnabledPromo}">
															<td>${claimRecipient.displayUSDAwardQuantity}</td>
														</c:when>
													</c:choose>
												</c:forEach>
												<td>
													<c:out value="${claim.submitter.lastName}" />,&nbsp;
													<c:out value="${claim.submitter.firstName}" />
												</td>
												<td><fmt:formatDate value="${claim.submissionDate}" type="date" pattern="${JstlDatePattern}" /></td>

												<td class="crud-content hasSelects" nowrap="nowrap">
													<c:forEach items="${claim.claimRecipients}" var="claimRecipient" varStatus="claimRecipientStatus">
														<c:set var="claimRecipientIdString">${claimRecipient.id}</c:set>
														<c:set var="applyAllClassClaimId" value="applyAllClassClaimAndClaimRecipientId-${claim.id}" />
														<c:set var="applyAllClassClaimAndClaimRecipientId" value="${applyAllClassClaimId}-${claimRecipientStatus.index}" />
														<c:set var="showableClaimRecipientIndex" value="1" />
														<c:set var="formApprovalStatusTypeCode" value="${approvalsRecognitionListForm.recognitionApprovalFormByClaimRecipientIdString[claimRecipientIdString].approvalStatusType}" />
														<c:set var="deniedReasonTypes" value="${claim.promotion.deniedReasonCodeTypes}" />
														<c:set var="holdReasonTypes" value="${claim.promotion.heldReasonCodeTypes}" />
														<html:select styleId="${applyAllClassClaimAndClaimRecipientId}-status"
															property="recognitionApprovalFormByClaimRecipientIdString(${claimRecipient.id}).approvalStatusType">
															<c:set var="approvalOptionTypes" value="${claim.promotion.approvalOptionTypesSorted}" />
															<c:forEach var="approvalOption" items="${approvalOptionTypes}">
																<c:if test="${approvalOption.code != 'approv' or claimRecipient.recipient.optOutAwards or claim.promotion.calculator == null || claimRecipient.calculatorScore != null}">
																	<html:option value="${approvalOption.code}"><c:out value="${approvalOption.name}"/></html:option>
																</c:if>
															</c:forEach>
														</html:select>

														<html:select styleId="deny-${applyAllClassClaimAndClaimRecipientId}-reason"
															property="recognitionApprovalFormByClaimRecipientIdString(${claimRecipient.id}).denyPromotionApprovalOptionReasonType"
															disabled="${ formApprovalStatusTypeCode == 'pend' || formApprovalStatusTypeCode == 'approv' || formApprovalStatusTypeCode == 'hold' }">
															<html:option value="">
																<cms:contentText code="claims.product.approval" key="SELECT_RC" />
															</html:option>
															<html:options collection="deniedReasonTypes" property="code" labelProperty="name" />
														</html:select>

														<html:select styleId="onHoldReasonSelect"
															property="recognitionApprovalFormByClaimRecipientIdString(${claimRecipient.id}).holdPromotionApprovalOptionReasonType"
															disabled="${ formApprovalStatusTypeCode == 'pend' || formApprovalStatusTypeCode == 'approv' || formApprovalStatusTypeCode == 'deny' }">
															<html:option value="">
																<cms:contentText code="claims.product.approval" key="SELECT_RC" />
															</html:option>
															<html:options collection="holdReasonTypes" property="code" labelProperty="name" />
														</html:select>
													</c:forEach>
												</td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<beacon:authorize ifNotGranted="LOGIN_AS">
						  <html:submit styleClass="btn btn-primary btn-fullmobile"><cms:contentText code="system.button" key="SAVE"/></html:submit>
						</beacon:authorize>
						<a class="btn btn-fullmobile" href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsListPage.do"><cms:contentText code="system.button" key="CANCEL" /></a>
					</html:form>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
	<a href="#"><cms:contentText code="recognition.approval.list" key="SAME_FOR_ALL" /><br><cms:contentText code="recognition.approval.list" key="RECIPIENTS_LBL" /></a>
</div>

<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">

<script>
	$(document).ready(function() {

		//attach the view to an existing DOM element
		var asmlp = new ApprovalsSearchModelView({
			el:$('#approvalsPagePendingNominations'),
	        pageNav : {
	            back : {
	                text : '<cms:contentText key="BACK" code="system.button" />',
	                url : 'approvalsListPage.do'
	            },
	            home : {
	                text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
	            }
	        },
	        pageTitle : '<cms:contentText code="promotion.approvals" key="TITLE_NEW" />'
		});

	});
</script>
