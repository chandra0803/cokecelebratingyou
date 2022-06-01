<!-- ======== APPROVALS: APPROVALS PENDING NOMINATIONS PAGE ======== -->
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.domain.claim.ClaimGroup"%>
<%@ page import="com.biperf.core.ui.approvals.ApprovalsNominationListForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<!-- ======== APPROVALS: APPROVALS PENDING NOMINATIONS PAGE ======== -->



<div id="approvalsPagePendingNominations" class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<h2><cms:contentText key="PAGE_TITLE" code="system.general" /></h2>
		</div>
	</div>
	<div id="pendingNominationsWrapper" class="approvalSearchWrapper">
		<div class="row-fluid">
			<div id="approvalsErrorBlock" class="span10 alert alert-block alert-error" style="display:none;">
				<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
		    	<ul>
		      		<html:messages id="actionMessage" >
		       			<c:set var="serverReturnedError" value="true"/>
		    			<li>${actionMessage}</li>
					</html:messages>
		     	</ul>
			</div>
			<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">

			<c:if test="${ saveOccurred }">
				<div id="approvalsSaveSuccessBlock" class="span10 alert alert-block alert-info">
					<h4><cms:contentText key="SAVE_OCCURRED" code="nomination.approval.list"/></h4>
				</div>
			</c:if>

			<div class="span6">
            	<h3><cms:contentText key="SEARCH_NOMINATIONS" code="nomination.approval.list"/></h3>
        	</div>
        	<div class="span6">
	            <ul class="export-tools fr">
	                <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT" /> <i class="icon-printer"></i></a></li>
	            </ul>
	        </div>
	    </div>

	    <div class="row-fluid">
	    	<div class="span12">
				<html:form styleId="approvalsSearchForm" action="approvalsNominationListMaintain.do?method=prepareUpdate&showActivity=true" styleClass="form-horizontal">

					<div class="control-group">

						<label class="control-label">
							<cms:contentText key="START_DATE" code="nomination.approval.list"/>
						</label>

						<div class="controls">
	                        <div class="input-append datepickerTrigger showTodayBtn"
	                            data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
	                            data-date-language="<%=UserManager.getUserLocale()%>"
	                            data-date-startdate=""
	                            data-date-todaydate=""
	                            data-date-autoclose="true">
	                            <input type="text" id="dateStart" name="startDate" value="${approvalsNominationListForm.startDate}" readonly="readonly" class="input-medium">
	                            <button class="btn awardDateIcon">
	                                <i class="icon-calendar"></i>
	                            </button>
	                        </div>


							<cms:contentText key="AND" code="nomination.approval.list"/>

	                        <div class="input-append datepickerTrigger showTodayBtn"
	                            data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
	                            data-date-language="<%=UserManager.getUserLocale()%>"
	                            data-date-startdate=""
	                            data-date-todaydate=""
	                            data-date-autoclose="true">
	                            <input type="text" id="dateEnd" name="endDate" value="${approvalsNominationListForm.endDate}" readonly="readonly" class="input-medium">
	                            <button class="btn awardDateIcon">
	                                <i class="icon-calendar"></i>
	                            </button>
	                        </div>
	                    </div>

					</div>

					<div class="control-group statusGroup">

						<label class="control-label">
							<cms:contentText key="STATUS" code="nomination.approval.list"/>
						</label>

						<div class="controls">
							<html:select property="filterApprovalStatusCode">
								<html:option value="">
									<cms:contentText code="system.general" key="SELECT_ONE" />
								</html:option>
								<html:options collection="approvalStatusTypes" property="code" labelProperty="name" />
							</html:select>
						</div>

					</div>

					<div class="control-group promotionGroup">

						<label class="control-label">
							<cms:contentText key="PROMOTION" code="nomination.approval.list"/>
						</label>

						<div class="controls">
							<html:select styleId="promotionSelected" property="promotionId">
								<html:option value="">
									<cms:contentText code="system.general" key="SELECT_ONE" />
								</html:option>
								<html:options collection="promotionList" property="id" labelProperty="name" />
							</html:select>
						</div>

					</div>

					<div class="control-group">

						<label class="control-label">
							&nbsp;
						</label>

						<div class="controls">
							<input type="submit" name="showNominations" value="<cms:contentText key="SHOW_ACTIVITY" code="nomination.approval.list"/>" class="btn btn-primary showActivityBtn"/>
							<c:set var="totalPages" value="${approvalsNominationListForm.listPageInfo.totalPages}"/>
							<c:set var="currentPage" value="${approvalsNominationListForm.listPageInfo.currentPage}"/>
							<input type="hidden" id="currentPage" name="approvalsNominationListForm.listPageInfo.currentPage" value="${currentPage}">
							<html:hidden property="requestedPage" styleId="requestedPage" value="1"/>
							<html:hidden property="listPageInfo.resultsPerPage"/>
						</div>

					</div>

				</html:form>

				<div class="pull-left">
					<h3><c:out value="${promotion.name}" /></h3>
					<%-- Client customization for WIP 58122 --%>
					<c:if test="${promotion.awardActive && !promotion.levelPayoutByApproverAvailable}">
						<p>
							<c:choose>
								<c:when test="${promotion.awardAmountTypeFixed}">
									<strong><cms:contentText key="AWARD" code="nomination.approval.list"/></strong>
									<span><c:out value="${promotion.awardAmountFixed}" /></span>
								</c:when>
								<c:otherwise>
									<strong><cms:contentText key="AWARD_RANGE" code="nomination.approval.list"/></strong>
									<span><c:out value="${promotion.awardAmountMin}" /> - <c:out value="${promotion.awardAmountMax}" /></span>
								</c:otherwise>
							</c:choose>
						</p>
					</c:if>
					<p>
						<strong><cms:contentText key="AUTO_NOTIFICATION_TIME" code="nomination.approval.list"/></strong>
						<span>
							<c:if test="${ !empty autoNotificationTimeOfDay}">
		                    	<fmt:formatDate value="${autoNotificationTimeOfDay}" pattern="hh:mm a zz"/>
		                 	</c:if>
		                  	<c:if test="${ empty autoNotificationTimeOfDay}">
		                    	<cms:contentText key="AUTO_NOTIFICATION_TIME_NOT_SPECIFIED" code="nomination.approval.list"/>
		               		</c:if>
		              	</span>
		        	</p>
				</div>

				<c:if test="${showExports}">
				<div class="row-fluid">
					<ul class="export-tools approvalsExportIconsWrapper pushDown">
                        <li class="export csv">
						    <a href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsNominationListMaintain.do?method=extractAsCsv&showActivity=true">
                                <span class="btn btn-inverse btn-compact btn-export-csv">
                                    <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                                </span>
                            </a>
                        </li>
                        <li class="export pdf">
						    <a href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsNominationListMaintain.do?method=extractAsPdf&showActivity=true">
                                <span class="btn btn-inverse btn-compact btn-export-pdf">
                                    <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                                </span>
                            </a>
                        </li>
					</ul>
				</div>
				</c:if>

				<html:form action="approvalsNominationListUpdate.do?method=saveApprovals">
					<div class="row-fluid">
						<c:if test="${totalPages > 1}">
							<div id="paginationControls" class="pagination pagination-right">
								<ul>
									<c:if test="${currentPage > 1}">
									    <li class="first" data-page="1">
									        <a href="#"><i class="icon-double-arrows-1-left"></i></a>
									    </li>

									    <li class="prev" data-page="${currentPage - 1}">
									        <a href="#"><i class="icon-arrow-1-left"></i><cms:contentText code="nomination.approval.list" key="PREV"/></a>
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
									        <a href="#"><cms:contentText code="nomination.approval.list" key="DISABLED_RANGE"/></a>
									    </li>

									    <li data-page="${totalPages}">
									        <a href="#">${totalPages}</a>
									    </li>
									</c:if>

									<c:if test="${currentPage < totalPages}">
									    <li class="next" data-page="${currentPage + 1}">
									        <a href="#"><cms:contentText code="nomination.approval.list" key="NEXT"/><i class="icon-arrow-1-right"></i></a>
									    </li>
									    <li class="last" data-page="${totalPages}">
									        <a href="#"><i class="icon-double-arrows-1-right"></i></a>
									    </li>
									</c:if>
								</ul>
							</div>
						</c:if>

					</div>

					<c:set var="pendingFilterOn" value="${ approvalsNominationListForm.filterApprovalStatusCode == 'pend' }"/>

					<table class="table table-striped" id="nominationApprovalTable">
                        <thead>
                            <tr>
                                <th><cms:contentText key="NOMINEE" code="nomination.approval.list"/></th>
                                <th><cms:contentText key="NODE_NAME" code="nomination.approval.list"/></th>
                                <th><cms:contentText key="NOMINATOR" code="nomination.approval.list"/></th>
                                <th><cms:contentText key="DATE_SUBMITTED" code="nomination.approval.list"/></th>
                                <c:choose>
                                    <c:when test="${pendingFilterOn}">
                                        <c:set var="showSelectAllLink" value="${fn:length(approvableList) > 1}"/>
                                        <th>
                                            <cms:contentText key="PENDING" code="nomination.approval.list"/>
                                            <c:if test="${showSelectAllLink}"><br /><a class="approvalsSelectAllPendingLink" id="selectAllPending"><cms:contentText key="SELECT_ALL" code="nomination.approval.list"/></a></c:if>
                                        </th>
                                        <th>
                                            <cms:contentText key="WINNER" code="nomination.approval.list"/>
                                            <c:if test="${showSelectAllLink}"><br /><a class="approvalsSelectAllWinnerLink" id="selectAllWinner"><cms:contentText key="SELECT_ALL" code="nomination.approval.list"/></a></c:if>
                                        </th>
                                        <th>
                                            <cms:contentText key="NON_WINNER" code="nomination.approval.list"/>
                                            <c:if test="${showSelectAllLink}"><br /><a class="approvalsSelectAllNonWinnerLink" id="selectAllNon-Winner"><cms:contentText key="SELECT_ALL" code="nomination.approval.list"/></a></c:if>
                                        </th>
                                    </c:when>
                                    <c:otherwise>
                                        <th><cms:contentText key="STATUS" code="nomination.approval.list"/></th>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${ promotion.levelSelectionByApprover }">
									<th><cms:contentText key="LEVEL" code="nomination.approval.list"/></th>
								</c:if>
								    <%-- Client customization for WIP 58122 --%>
								 <c:if test="${ promotion.levelPayoutByApproverAvailable && final_Approver }">
									<th><cms:contentText key="LEVEL2" code="nomination.approval.list"/></th>
								</c:if>
								 <c:if test="${ promotion.levelPayoutByApproverAvailable && final_Approver }">
									<th><cms:contentText key="AWARD_PER_PERSON" code="nomination.approval.list"/></th>
								</c:if>
								     <%-- Client customization for WIP 58122 --%>
								
                                 <c:if test="${ promotion.awardActive  && !promotion.levelPayoutByApproverAvailable}">
                                    <th>
                                        <c:choose>
                                            <c:when test="${promotion.awardAmountTypeFixed}">
                                                <cms:contentText key="AWARD_PER_PERSON" code="nomination.approval.list"/>
                                            </c:when>
                                            <c:otherwise>
                                                 <cms:contentText key="AWARD_PER_PERSON" code="nomination.approval.list"/>
                                                 <br>
                                                 <span class="noWrap">
                                                 <cms:contentText key="AWARD_RANGE_PER_PERSON" code="nomination.approval.list"/>
                                                <c:out value="${promotion.awardAmountMin}" /> - <c:out value="${promotion.awardAmountMax}" />
                                                </span>
                                            </c:otherwise>
                                        </c:choose>

                                    </th>
                                </c:if>
                                <th class="nowrap"><cms:contentText key="NOTIFICATION_DATE" code="nomination.approval.list"/></th>
                            </tr>
                        </thead>

                        <tbody>
							<c:if test="${fn:length(approvableList) > 1}">
							<%--
							<div style="display: none" class="tableSwipeHelp">
		                        <i class="icon-chevron-right"></i>
		                        <span><cms:contentText key="SWIPE_FOR_MORE" code="nomination.approval.list"/></span>
		                        <i class="icon-chevron-left"></i>
		                    </div>
		                    --%>
		                    </c:if>

							<c:choose>
								<c:when test="${fn:length(approvableList) < 1}">
									<tr>
										<td colspan="12">
											<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
										</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach var="approvable" items="${approvableList}">
										<c:set var="approvableUid" value="${approvable.approvableUid}" />

										<%	Map<String,Object> paramMap = new HashMap<String,Object>();
										  	Claim claim = null;
										    ClaimGroup claimGroup=null;

		                                   	ApprovalsNominationListForm tempForm = (ApprovalsNominationListForm)session.getAttribute( "approvalsNominationListForm" );
		                                    try
		                                    {
		                                      //could be a claim - as in independent noms
		                                      claim = (Claim)pageContext.getAttribute( "approvable" );
		                                      paramMap.put( "approvableId", claim.getId() );
		                                      paramMap.put( "promotionId", claim.getPromotion().getId() );
		                                      paramMap.put( "approvableTypeCode", claim.getApprovableType().getCode() );
		                                      paramMap.put( "viewApprovalStatusCode", tempForm.getFilterApprovalStatusCode() );
		                                    }
		                                    catch( ClassCastException e )
		                                    {
		                                      //or could be a claim group - as in cumulative nom
		                                      //transient claim group, so pass in dates, pax and node to rebuild group
		                                      claimGroup = (ClaimGroup)pageContext.getAttribute( "approvable" );
		                                      paramMap.put( "claimGroupPaxId", claimGroup.getParticipant().getId() );
		                                      paramMap.put( "claimGroupNodeId", claimGroup.getNode().getId() );
		                                      paramMap.put( "claimGroupStartDate", tempForm.getStartDate() );
		                                      paramMap.put( "claimGroupEndDate", tempForm.getEndDate() );
		                                      paramMap.put( "promotionId", claimGroup.getPromotion().getId() );
		                                      paramMap.put( "approvableTypeCode", claimGroup.getApprovableType().getCode() );
		                                      paramMap.put( "viewApprovalStatusCode", tempForm.getFilterApprovalStatusCode() );
		                                    }

		                                    pageContext.setAttribute( "approveClaimUrl", ClientStateUtils.generateEncodedLink( "", "approvalsNominationDetails.do?method=prepareUpdate", paramMap ) );
			                            %>

										<tr>
											<td>
												<c:if test="${ promotion.cumulative}">
													<a href="${approveClaimUrl}"><c:out value="${approvable.participant.nameLFMWithComma}" /></a>
												</c:if>
												<c:if test="${ !promotion.cumulative}">
													<%-- Will only ever be one --%>
													<c:forEach items="${approvable.claimRecipients}" var="claimRecipient" varStatus="claimRecipientStatus">
														<a href="${approveClaimUrl}">
															<c:out value="${claimRecipient.recipientDisplayName}" />
														</a>
														<span data-team-size="${approvable.teamMembersSize}" class="teamSizeValue">
														<c:if test="${approvable.team}">
															<br />
															<cms:contentTemplateText key="TEAM_SIZE" code="nomination.approval.list" args="${approvable.teamMembersSize}"/>
														</c:if>
														</span>												
													</c:forEach>
												</c:if>
											</td>
											<td>
												<c:if test="${ promotion.cumulative}">
													<c:out value="${approvable.node.name}" />
												</c:if>
												<c:if test="${ !promotion.cumulative}">
													<%-- Will only ever be one --%>
													<c:forEach items="${approvable.claimRecipients}" var="claimRecipient">
														<c:choose>
															<c:when test="${claimRecipient.node != null}">
																<c:set var="crNodeName" value="${claimRecipient.node.name}"/>
															</c:when>
															<c:otherwise>
																<c:set var="crNodeName" value="${approvable.node.name}"/>
															</c:otherwise>
														</c:choose>
													</c:forEach>
													<c:out value="${crNodeName}" />
												</c:if>
											</td>
											<td>
												<c:if test="${ promotion.cumulative}">
													<c:forEach items="${approvable.claims}" var="claimGroupClaim" varStatus="claimGroupClaimStatus">
														<c:if test="${!claimGroupClaimStatus.first}"><br /></c:if>
														<c:out value="${claimGroupClaim.submitter.nameLFMWithComma}" />
													</c:forEach>
												</c:if>
												<c:if test="${ !promotion.cumulative}">
													<c:out value="${approvable.submitter.nameLFMWithComma}" />
												</c:if>
											</td>
											<td>
												<c:if test="${ promotion.cumulative}">
													<c:forEach items="${approvable.claims}" var="claimGroupClaim" varStatus="claimGroupClaimStatus">
														<c:if test="${!claimGroupClaimStatus.first}"><br /></c:if>
														<fmt:formatDate value="${claimGroupClaim.submissionDate}" pattern="${JstlDatePattern}"/>
													</c:forEach>
												</c:if>
												<c:if test="${ !promotion.cumulative}">
													<fmt:formatDate value="${approvable.submissionDate}" pattern="${JstlDatePattern}"/>
												</c:if>
											</td>

											<c:choose>
												<c:when test="${ pendingFilterOn }">
													<td class="crud-content">
														<html:hidden property="nominationApprovalFormByApprovableUid(${approvableUid}).version" />
														<html:radio property="nominationApprovalFormByApprovableUid(${approvableUid}).approvalStatusType" value="pend" styleClass="pendingRadio rdochk"/>
													</td>
													<td class="crud-content">
														<html:radio property="nominationApprovalFormByApprovableUid(${approvableUid}).approvalStatusType" value="winner" styleId="radio-winner-${approvableUid}" styleClass="pendingWinner rdochk" />
													</td>
													<td class="crud-content">
														<html:radio property="nominationApprovalFormByApprovableUid(${approvableUid}).approvalStatusType" value="non_winner" styleId="radio-non_winner-${approvableUid}" styleClass="pendingNonWinner rdochk" />
													</td>
												</c:when>
												<c:otherwise>
													<td class="crud-content">
														<c:out value="${ filterApprovalStatusName }" />
													</td>
												</c:otherwise>
											</c:choose>
											<%-- client customization wip#56492 start --%>
											<c:if test="${ promotion.levelSelectionByApprover }">
												<td class="crud-content">
													<div class="controls">
														<html:select property="nominationApprovalFormByApprovableUid(${approvableUid}).level" styleClass="levelSelect">
															<html:option value="">
																<cms:contentText code="system.general" key="SELECT_ONE" />
															</html:option>
															<html:options collection="approverLevelTypes" property="code" labelProperty="name" />
														</html:select>
													</div>
												</td>
											</c:if>
											<%-- client customization wip#56492 end --%>
																						     <%-- Client customization for WIP 58122 --%>
														<c:set var="charIndex" value="${-1}" />
														<c:if test="${levelPayouts != null }">
														<logic:iterate name="levelPayouts" id="level">
														<c:set var="charIndex" value="${charIndex+1}" />
																<html:hidden property="level[${charIndex}].totalPoints"	styleClass="levelInputs"/>
							
														</logic:iterate>
														</c:if>
														<html:hidden property="capPerPax" 	styleClass="capPerPax"/>
											 <c:if test="${ promotion.levelPayoutByApproverAvailable && final_Approver }">
											 <td class="crud-content">
													<div class="controls validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;You must select level &quot;}">
														<html:select property="nominationApprovalFormByApprovableUid(${approvableUid}).level" styleClass="levelSelect">
															<html:option value="">
																<cms:contentText code="system.general" key="SELECT_ONE" />
															</html:option>
															<html:options collection="levelPayouts" property="levelId" labelProperty="levelDescription" />
														</html:select>
													</div>
												</td>											 
											 </c:if>
											 <c:if test="${ promotion.levelPayoutByApproverAvailable && final_Approver }">
											 <td class="crud-content nowrap">
													
												<html:text property="nominationApprovalFormByApprovableUid(${approvableUid}).awardQuantity" styleId="awardQuantity-${approvableUid}" size="7" readonly="true"/>
										
											  </td>
											</c:if>
									     <%-- Client customization for WIP 58122 --%>
											
											<%-- client customization wip#56492 end --%>
											<c:if test="${ promotion.awardActive  && !promotion.levelPayoutByApproverAvailable}">
												<td class="crud-content nowrap">
													<c:choose>
														<c:when test="${ pendingFilterOn }">
															<c:choose>
																<c:when test="${promotion.awardAmountTypeFixed}">
																<%-- client customization start --%>
																<c:forEach items="${approvable.claimRecipients}" var="claimRecipient" varStatus="claimRecipientStatus">
																	<c:choose>
																	<c:when test="${claimRecipient.optOut}">
																	    <cms:contentText key="OPT_OUT" code="client.nomination.approval"/>
																	</c:when>
																	     <%-- Client customization for WIP 58122 --%>
																	<c:when test="${promotion.levelPayoutByApproverAvailable}">
																	   <c:out value="0"/>
																	</c:when>
																	     <%-- Client customization for WIP 58122 --%>
																	<c:otherwise>
																		<c:out value="${promotion.awardAmountFixed}"/>
																	</c:otherwise>
																	</c:choose>
																</c:forEach>
																</c:when>
																<c:otherwise>
																    <c:choose>
																    <c:when test="${approvalsNominationListForm.nominationApprovalFormByApprovableUid[approvableUid].anyPaxNotOptedOut}">
																    <html:text property="nominationApprovalFormByApprovableUid(${approvableUid}).awardQuantity" styleClass="span1" styleId="awardQuantity-${approvableUid}" size="7" />
																	</c:when>
																	<c:otherwise>
																	<cms:contentText key="OPT_OUT" code="client.nomination.approval"/>
																	</c:otherwise>
																	</c:choose>
																</c:otherwise>
																<%-- client customization end --%>
															</c:choose>
														</c:when>
														<c:otherwise>
															<c:out value="${approvalsNominationListForm.nominationApprovalFormByApprovableUid[approvableUid].awardQuantity}" />
														</c:otherwise>
													</c:choose>
												</td>
											</c:if>

											<c:choose>
												<c:when test="${ pendingFilterOn }">
													<td class="crud-content nowrap notificationDate">
														<div class="input-append datepickerTrigger"
						                                	data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
						                                	data-date-language="<%=UserManager.getUserLocale()%>"
						                                	data-date-startdate="${tomorrowDate}"
						                                	data-date-todaydate=""
						                                	data-date-autoclose="true">
						                                    <html:text property="nominationApprovalFormByApprovableUid(${approvableUid}).notificationDate"
						                                    	readonly="true"  styleClass="date span1" />
						                                    <button class="btn awardDateIcon">
						                                        <i class="icon-calendar"></i>
						                                    </button>
						                                </div>
                                                        <button class="btn btn-icon clearDate">
                                                            <i class="icon-trash"></i>
                                                        </button>
													</td>
												</c:when>
												<c:otherwise>
													<td class="crud-content nowrap">
														<c:out value="${approvalsNominationListForm.nominationApprovalFormByApprovableUid[approvableUid].notificationDate}" />
													</td>
												</c:otherwise>
											</c:choose>

											<input type="hidden" id="conversionRate" style="display: none" value="1" />
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>

					<c:if test="${fn:length(approvableList) > 0}">
						<p><cms:contentText key="NOTIFICATION_DATE_DISCLAIMER" code="nomination.approval.list"/></p>
					    <beacon:authorize ifNotGranted="LOGIN_AS">
						  <c:if test="${ filterApprovalStatusCode=='pend'}">
							<button class="btn btn-primary btn-fullmobile"><cms:contentText code="system.button" key="SAVE" /></button>
						  </c:if>
						</beacon:authorize>
					</c:if>
					<a class="btn btn-fullmobile" href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsListPage.do"><cms:contentText code="system.button" key="CANCEL" /></a>
				</html:form>
			</div>
		</div>
	</div>
</div>

<script>
	var asmlp;
	$(document).ready(function(){
		//attach the view to an existing DOM element
		asmlp = new ApprovalsSearchModelView({
	    	el:$('#approvalsPagePendingNominations'),
	        pageNav : {
	        	back : {
	                text : '<cms:contentText key="BACK" code="system.button" />',
	                url : '${pageContext.request.contextPath}/participantProfilePage.do#tab/AlertsAndMessages'
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
