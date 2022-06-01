<%@page import="com.biperf.core.domain.enums.PromotionStatusType"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionBasics.do?method=display";
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), url, paramMap ) );
	%>
    <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
      <c:if test="${!hideEditLinks}"> 
      <a class="content-link" href="<c:out value="${viewUrl}"/>"><cms:contentText code="system.link" key="EDIT"/></a>
      </c:if>
    </beacon:authorize>
  </td>
</tr>
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label">
		     <cms:contentText code="promotion.basics" key="PROMOTION_ID"/>
		  </td>
		  <td class="content-field-review"><c:out value="${promotionOverviewForm.promotionId}" /></td>
		</tr>

		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'diy_quiz' }">
			<tr>
			  <td>&nbsp;</td>
			  <td class="content-field-label">
			     <cms:contentText code="promotion.basics" key="DIY_MASTER_NAME"/>
			  </td>
			  <td class="content-field-review"><c:out value="${promotionOverviewForm.promotionName}" /></td>
			</tr>
		</c:if>

		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'self_serv_incentives' }">
			<tr>
			  <td>&nbsp;</td>
			  <td class="content-field-label">
			     <cms:contentText code="promotion.ssi.basics" key="SSI_MASTER_NAME"/>
			  </td>
			  <td class="content-field-review"><c:out value="${promotionOverviewForm.promotionName}" /></td>
			</tr>
		</c:if>

		<c:if test="${ promotionOverviewForm.promotionTypeCode != 'goalquest' &&
		               promotionOverviewForm.promotionTypeCode != 'diy_quiz' &&
		               promotionOverviewForm.promotionTypeCode != 'engagement' &&
		               promotionOverviewForm.promotionTypeCode != 'challengepoint' &&
		                promotionOverviewForm.promotionTypeCode != 'wellness' &&
		                promotionOverviewForm.promotionTypeCode != 'throwdown' &&
		                promotionOverviewForm.promotionTypeCode != 'self_serv_incentives' }">
			<tr>
			  <td>&nbsp;</td>
			  <td class="content-field-label">
			    <c:choose>
			      <c:when test="${promotionOverviewForm.promotionTypeCode == 'quiz'}">
			        <cms:contentText code="promotion.overview" key="QUIZ"/>
			      </c:when>
			      <c:when test="${promotionOverviewForm.promotionTypeCode == 'survey'}">
			        Survey Form
			      </c:when>
			      <c:otherwise>
			        <c:if test="${ promotionOverviewForm.promotionTypeCode != 'self_serv_incentives' }"><cms:contentText code="promotion.basics" key="ACTIVITY_FORM"/></c:if>
			      </c:otherwise>
			    </c:choose>
			  </td>
			  <td class="content-field-review"><c:out value="${promotionOverviewForm.claimFormName}" /></td>
			</tr>
		</c:if>
		<tr>
		  <td>&nbsp;</td>
		  <c:if test="${ promotionOverviewForm.promotionTypeCode != 'survey' &&
		  promotionOverviewForm.promotionTypeCode != 'challengepoint' &&
		  promotionOverviewForm.promotionTypeCode != 'diy_quiz' &&
		  promotionOverviewForm.promotionTypeCode != 'engagement' &&
		  promotionOverviewForm.promotionTypeCode != 'wellness' &&
		  promotionOverviewForm.promotionTypeCode != 'self_serv_incentives'}">
		  <td class="content-field-label">
		    <cms:contentText code="promotion.basics" key="RECOGNITION_DATES"/>
		  </td>
		  </c:if>
		  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'diy_quiz' or promotionOverviewForm.promotionTypeCode == 'self_serv_incentives' }">
		  <td class="content-field-label">
		    <cms:contentText code="promotion.basics" key="AVAILABLE_DATES"/>
		  </td>
		  </c:if>
		  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'engagement' }">
		  <td class="content-field-label">
		    <cms:contentText code="promotion.basics" key="START_DATE"/>
		  </td>
		  </c:if>
		  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'survey' }">
		  <td class="content-field-label">
		    <cms:contentText code="promotion.basics" key="SURVEY_DATES"/>
		  </td>
		  </c:if>
		  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'wellness' }">
		  <td class="content-field-label">
		    <cms:contentText code="promotion.basics" key="WELLNESS_DATES"/>
		  </td>
		  </c:if>

		  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'challengepoint' }">
		  <td class="content-field-label">
		    <cms:contentText code="promotion.basics" key="CHALLENGEPOINT_DATES"/>
		  </td>
		  </c:if>
		  <td class="content-field-review" nowrap>
		  	<c:choose>
			  	<c:when test="${ promotionOverviewForm.promotionTypeCode == 'engagement' }">
			  		<c:out value="${promotionOverviewForm.claimSubmissionStartDate}" />
			  	</c:when>
			  	<c:otherwise>
			  	<c:out value="${promotionOverviewForm.claimSubmissionStartDate}" /> -
					<c:choose>
					  <c:when test="${promotionOverviewForm.claimSubmissionEndDate != ''}">
						<c:out value="${promotionOverviewForm.claimSubmissionEndDate}"/>
					  </c:when>
					  <c:otherwise>
						<cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
					  </c:otherwise>
					</c:choose>
			  	</c:otherwise>
			  </c:choose>
		  </td>
		</tr>

		<c:if test="${promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint'
		 || promotionOverviewForm.promotionTypeCode == 'throwdown'}">
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="TILE_DISPLAY"/>
		    </td>
		    <td class="content-field-review" nowrap>
		      <c:out value="${promotionOverviewForm.tileDisplayStartDate}"/> -
		      <c:out value="${promotionOverviewForm.tileDisplayEndDate}"/>
		    </td>
		  </tr>
		  </c:if>
		  <c:if test="${promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint'}">
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="GOAL_SELECTION_DATES"/>
		    </td>
		    <td class="content-field-review" nowrap>
		      <c:out value="${promotionOverviewForm.goalSelectionStartDate}"/> -
		      <c:out value="${promotionOverviewForm.goalSelectionEndDate}"/>
		    </td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="FINAL_PROCESS_DATE"/>
		    </td>
		    <td class="content-field-review" nowrap>
		      <c:out value="${promotionOverviewForm.finalProcessDate}"/>
		    </td>
		  </tr>
		  <c:if test="${promotionOverviewForm.promotionTypeCode == 'goalquest' }">
		    <tr>
		      <td>&nbsp;</td>
		      <td class="content-field-label">
		        <cms:contentText code="promotion.basics" key="PROGRESS_LOAD_TYPE"/>
		      </td>
		      <td class="content-field-review" nowrap>
		        <c:out value="${promotionOverviewForm.progressLoadTypeName}"/>
		      </td>
		  </tr>
		  </c:if>

		  <c:choose>
		  <c:when test="${ isPlateauPlatformOnly && promotionOverviewForm.promotionTypeCode eq 'quiz'}">
		  </c:when>
		  <c:otherwise>
		  <tr>
		  	<td>&nbsp;</td>
		  	<td class="content-field-label"><cms:contentText code="promotion.awards" key="TYPE"/></td>
		  	<td class="content-field-review"><c:out value="${promotionOverviewForm.awardTypeText}" /></td>
		  </tr>
		  </c:otherwise>
		  </c:choose>

		  <c:if test="${promotionOverviewForm.promotionTypeCode == 'challengepoint' }">
		  	<tr>
		      <td>&nbsp;</td>
		      <td class="content-field-label"><cms:contentText code="promotion.awards" key="CHALLENGEPOINT_AWARD_TYPE"/></td>
		      <td class="content-field-review"><c:out value="${promotionOverviewForm.challengePointAwardType}" /></td>
		    </tr>
		  </c:if>
		  <c:if test="${promotionOverviewForm.awardTypeText != 'Points' }">
		    <tr>
		      <td>&nbsp;</td>
		      <td class="content-field-label">
		        <cms:contentText code="promotion.basics" key="PROGRAM_ID"/>
		      </td>
		      <td class="content-field-review" nowrap>
		        <c:out value="${promotionOverviewForm.programId}"/>
		      </td>
		    </tr>
		  </c:if>
		</c:if>

		 <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' }">
		   <tr>
		     <td>&nbsp;</td>
		     <td class="content-field-label"><cms:contentText key="ALLOW_MOB_APP" code="promotion.basics"/></td>
		     <td class="content-field-review">
			     <c:choose>
			      <c:when test="${ promotionOverviewForm.mobAppEnabled == 'false' }">
			        <cms:contentText code="promotion.overview" key="FALSE" />
			      </c:when>
			      <c:otherwise>
			        <cms:contentText code="promotion.overview" key="TRUE" />
			      </c:otherwise>
			    </c:choose>
			  </td>
			</tr>
		  </c:if>

		<c:if test="${promotionOverviewForm.promotionTypeCode == 'engagement' }">
		   <tr>
		     <td>&nbsp;</td>
		     <td class="content-field-label" valign="top" nowrap>
		     	<cms:contentText key="ELIGIBLE_PROMOTIONS" code="promotion.basics"/>
		     </td>
		     <td class="content-field-review">
		     	<table>
		          <c:forEach items="${promotionOverviewForm.engagementEligiblePromotionList}" var="engagementEligiblePromo" varStatus="status">
		            <tr>
		              <td class="content-field-review">
		                <c:out value="${engagementEligiblePromo}"/>
		              </td>
		            </tr>
		          </c:forEach>
		        </table>
			 </td>
		    </tr>
		</c:if>

		<c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="PUBLICATION_UNTIL"/>
		    </td>
		    <td class="content-field-review" nowrap>
			   	<c:choose>
			    	<c:when test="${promotionOverviewForm.publicationDateActive }">
		    		  <c:out value="${promotionOverviewForm.publicationDate}"/>
			    	</c:when>
			    	<c:otherwise>
					  <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
			    	</c:otherwise>
			    </c:choose>
		    </td>

		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="AWARD_GROUP_TYPE"/>
		    </td>
		    <td class="content-field-review" nowrap>
		      <c:out value="${promotionOverviewForm.individualOrTeamBased}"/>
		    </td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="EVALUTION_TYPE"/>
		    </td>
		    <td class="content-field-review" nowrap>
		      <c:out value="${promotionOverviewForm.evaluated}"/>
		    </td>
		  </tr>
		   <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText key="IS_WHY_DEFAULT_ACTIVE" code="promotion.basics"/>
		    </td>
		    <td class="content-field-review" nowrap>
		      <c:out value="${promotionOverviewForm.whyNomination}"/>
		    </td>
		  </tr>
		</c:if>

		<c:if test="${promotionOverviewForm.promotionTypeCode == 'diy_quiz' || promotionOverviewForm.promotionTypeCode == 'survey'}">
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.basics" key="PROMOTION_STATUS"/></td>
		  <td class="content-field-review"><c:out value="${promotionOverviewForm.promotionStatusDesc}" /></td>
		</tr>
		</c:if>

		<c:if test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.ssi.basics" key="ALLOWED_CONTEST_TYPE"/></td>
		  <td class="content-field-review">${promotionOverviewForm.selectedContests}</td>
		</tr>
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.ssi.basics" key="DAYS_TO_ARCHIVE"/></td>
		  <td class="content-field-review">${promotionOverviewForm.daysToArchive}</td>
		</tr>
		</c:if>

		<c:if test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText key="MAXIMUM_CONTEST_TO_DISPLAY" code="promotion.ssi.basics"/></td>
		  <td class="content-field-review">${promotionOverviewForm.maxContestsToDisplay}</td>
		</tr>
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText key="CONTEST_GUIDE" code="promotion.ssi.basics"/></td>
		  <td class="content-field-review">${promotionOverviewForm.contestGuideUrl}</td>
		</tr>
		</c:if>

		<c:choose>
		<c:when test="${ isPlateauPlatformOnly && promotionOverviewForm.promotionTypeCode eq 'quiz'}">
		</c:when>
		<c:otherwise>
		<c:if test="${ promotionOverviewForm.promotionTypeCode != 'survey' && promotionOverviewForm.promotionTypeCode != 'nomination' && promotionOverviewForm.promotionTypeCode != 'goalquest'
			&& promotionOverviewForm.promotionTypeCode != 'challengepoint' && promotionOverviewForm.promotionTypeCode != 'diy_quiz' && promotionOverviewForm.promotionTypeCode != 'engagement'
			&& promotionOverviewForm.promotionTypeCode != 'self_serv_incentives'}">
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.awards" key="TYPE"/></td>
		  <td class="content-field-review"><c:out value="${promotionOverviewForm.awardTypeText}" /></td>
		</tr>
		</c:if>
		</c:otherwise>
		</c:choose>


		<c:choose>
		<c:when test="${ isPlateauPlatformOnly && promotionOverviewForm.promotionTypeCode eq 'quiz'}">
		</c:when>
		<c:otherwise>
		<c:if test="${promotionOverviewForm.promotionTypeCode != 'survey' && promotionOverviewForm.promotionTypeCode != 'diy_quiz' && promotionOverviewForm.promotionTypeCode != 'engagement' &&
		                promotionOverviewForm.promotionTypeCode != 'self_serv_incentives' && promotionOverviewForm.promotionTypeCode != 'nomination'}">
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.basics" key="TAXABLE"/></td>
		  <td class="content-field-review">
		    <c:choose>
		      <c:when test="${ promotionOverviewForm.taxable == 'false' }">
		        <cms:contentText code="promotion.overview" key="FALSE" />
		      </c:when>
		      <c:otherwise>
		        <cms:contentText code="promotion.overview" key="TRUE" />
		      </c:otherwise>
		    </c:choose>
		  </td>
		</tr>
		</c:if>
		</c:otherwise>
		</c:choose>


		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'survey' }">

		<tr>
		<td>&nbsp;</td>
		<td class="content-field-label">Reporting</td>
		  <td class="content-field-review">
		    <c:choose>
		      <c:when test="${ promotionOverviewForm.corpAndMngr == 'false' }">
		        <cms:contentText code="promotion.overview" key="CORPORATE_ONLY"/>
		      </c:when>
		      <c:otherwise>
		        <cms:contentText code="promotion.overview" key="CORPORATE_AND_MANAGERS"/>
		      </c:otherwise>
		    </c:choose>
		  </td>
		</tr>
		</c:if>

		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'throwdown' }">
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="PROMOTION_THEME"/>
		    </td>
		    <td class="content-field-review"><c:out value="${promotionOverviewForm.promotionTheme}" /></td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="UNEVEN_PLAY_SELECTION"/>
		    </td>
		    <td class="content-field-review"><c:out value="${promotionOverviewForm.unevenPlaySelection}" /></td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="DISPLAY_MATCH_PROGRESS"/>
		    </td>
		    <td class="content-field-review">
		     <c:choose>
		      <c:when test="${ promotionOverviewForm.displayTeamProgress == 'true' }">
		        <cms:contentText code="system.common.labels" key="TRUE_CAPS"/>
		      </c:when>
		      <c:otherwise>
		        <cms:contentText code="system.common.labels" key="FALSE_CAPS"/>
		      </c:otherwise>
		    </c:choose>
		    </td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="DAYS_PRIOR_TO_ROUND"/>
		    </td>
		    <td class="content-field-review"><c:out value="${promotionOverviewForm.daysPriorToRoundStartSchedule}" /></td>
		  </tr>
		  <tr>
		  <td>&nbsp;</td>
		   <td class="content-field-label">
		   <cms:contentText code="promotion.basics" key="SMACK_TALK_AVAILABLE"/>
		  </td>
		   <td class="content-field-review">
		  <c:choose>
		      <c:when test="${ promotionOverviewForm.smackTalkAvailable == 'true' }">
		        <cms:contentText code="system.common.labels" key="YES" />
		      </c:when>
		      <c:otherwise>
		        <cms:contentText code="system.common.labels" key="NO" />
		      </c:otherwise>
		    </c:choose>
		 </td>
		  </tr>
		</c:if>

		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="PROCESSING_MODE"/>
		    </td>
		    <td class="content-field-review"><c:out value="${promotionOverviewForm.processingMode}" /></td>
		  </tr>
		</c:if>
		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' ||  promotionOverviewForm.promotionTypeCode == 'wellness' }">
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.basics" key="METHOD_OF_ISSUANCE"/>
		    </td>
		    <td class="content-field-review"><c:out value="${promotionOverviewForm.processingMode}" /></td>
		  </tr>
		</c:if>
		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }">
		  <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText code="promotion.basics" key="INCLUDE_PURL"/>
		    </td>
		    <td class="content-field-review">
		      <c:choose>
		        <c:when test="${promotionOverviewForm.includePurl == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		    </td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText code="promotion.basics" key="INCLUDE_PURL_STANDARD_MSG"/>
		    </td>
		    <td class="content-field-review">
		      <c:choose>
		        <c:when test="${promotionOverviewForm.purlStandardMessageEnabled == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		    </td>
		  </tr>
		   <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText code="promotion.basics" key="ALLOW_PUBLIC_RECOGNITION"/>
		    </td>
		    <td class="content-field-review">
		      <c:choose>
		        <c:when test="${promotionOverviewForm.allowPublicRecognition == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		    </td>
		  </tr>
		  <c:if test="${promotionOverviewForm.includePurl == 'true'}">
		    <tr>
		     <td>&nbsp;</td>
		      <td valign="top" class="content-field-label">
		        <cms:contentText code="promotion.basics" key="PURL_MEDIA_TYPE"/>
		      </td>
		      <td class="content-field-review">
		        <c:out value="${promotionOverviewForm.purlPromotionMediaType}"/>
		      </td>
		    </tr>
		    <c:if test="${promotionOverviewForm.purlMediaValue != null }">
		     <tr>
		       <td>&nbsp;</td>
		         <td valign="top" class="content-field-label">
		           <cms:contentText code="promotion.basics" key="PURL_MEDIA_URL"/>
		         </td>
		        <td class="content-field-review">
		          <c:out value="${promotionOverviewForm.purlMediaValue}"/>
		      </td>
		    </tr>
		    </c:if>
		  </c:if>

		 <c:if test="${promotionOverviewForm.includePurl == 'false'}">
		  <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText key="RECOGNITION_SEND_DATE" code="promotion.basics"/>
		    </td>
		    <td class="content-field-review">
		      <c:choose>
		        <c:when test="${promotionOverviewForm.allowRecognitionSendDate == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		    </td>
		  </tr>
		  </c:if>

		  <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText code="promotion.basics" key="RECOGNITION_OPTIONS"/>
		    </td>
		    <td valign="top" class="content-field-review" nowrap>
		    <cms:contentText code="promotion.overview" key="PROMO_BASICS_RECOGNITION_COPY"/>&nbsp;=&nbsp;
		      <c:choose>
		        <c:when test="${promotionOverviewForm.copyRecipientManager == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		      </br>
		      <cms:contentText code="promotion.overview" key="PROMO_BASICS_RECOGNITION_COPY_OTHERS"/>&nbsp;=&nbsp;
		      <c:choose>
		        <c:when test="${promotionOverviewForm.copyOthers == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		      </br>
		    </td>
		 </tr>
		 <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText code="promotion.basics" key="ALLOW_MANAGER_AWARD"/>
		    </td>
		    <td valign="top" class="content-field-review" nowrap>
		      <c:choose>
		        <c:when test="${promotionOverviewForm.managerAwardPromotion == 'true'}">
							<%	Map parameterMap = new HashMap();
									parameterMap.put( "promotionId", temp.getManagerAwardPromotionId() );
									pageContext.setAttribute("promoUrl", ClientStateUtils.generateEncodedLink( "", "promotionOverview.do?method=display", parameterMap ) );
							%>
		          <a href="<c:out value="${promoUrl}"/>"><c:out value="${promotionOverviewForm.managerPromotionName}"/></a>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		    </td>
		  </tr>
		</c:if>
		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'quiz' }">
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.overview" key="ATTEMPTS_ALLOWED"/>
		    </td>
		    <td class="content-field-review"><c:out value="${promotionOverviewForm.attemptsAllowed}" /></td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td class="content-field-label">
		      <cms:contentText code="promotion.overview" key="INCLUDE_CERTIFICATE"/>
		    </td>
		    <td class="content-field-review">
		      <c:choose>
		        <c:when test="${promotionOverviewForm.includeCertificate == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		    </td>
		  </tr>
		</c:if>
		<c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination' }">
		  <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText code="promotion.basics" key="NOMINATION_OPTION"/>
		    </td>
		    <td valign="top" class="content-field-review" nowrap>
		      <cms:contentText code="promotion.basics" key="SELF_NOMINATION"/>&nbsp;=&nbsp;
		      <c:choose>
		        <c:when test="${promotionOverviewForm.selfNomination == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		      <br>
		      <cms:contentText key="ALLOW_PUBLIC_RECOGNITION" code="promotion.basics" />&nbsp;=&nbsp;
		      <c:choose>
		        <c:when test="${promotionOverviewForm.allowPublicRecognition == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		      <%-- Client customization wip#56492 start--%>
		      <br>
		      <cms:contentText code="promotion.basics" key="LEVEL_SELECTION_BY_APPROVER"/>&nbsp;=&nbsp;
		      <c:choose>
		        <c:when test="${promotionOverviewForm.levelSelectionByApprover == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		      <%-- Client customization wip#56492 end--%>
		      
		      <%-- Client customization wip#58122 start--%>
		      <br>
		      <cms:contentText code="promotion.basics" key="LEVEL_PAYOUT_BY_APPROVER"/>&nbsp;=&nbsp;
		      <c:choose>
		        <c:when test="${promotionOverviewForm.levelPayoutByApproverAvailable == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		      <%-- Client customization wip#58122 end--%>
		      
		    </td>
		  </tr>
		  <!--   view past winners     -->
		   <tr>
		    <td>&nbsp;</td>
		    <td valign="top" class="content-field-label">
		      <cms:contentText code="promotion.basics" key="VIEW_PAST_WINNERS"/>
		    </td>
		    <td valign="top" class="content-field-review" nowrap>

		      <c:choose>
		        <c:when test="${promotionOverviewForm.viewPastWinners == 'true'}">
		          <cms:contentText code="promotion.overview" key="TRUE"/>
		        </c:when>
		        <c:otherwise>
		          <cms:contentText code="promotion.overview" key="FALSE"/>
		        </c:otherwise>
		      </c:choose>
		    </td>
		  </tr>
		</c:if>		
		
		 <c:if test="${promotionOverviewForm.promotionTypeCode == 'goalquest'}">
				    <tr>
				  <td>&nbsp;</td>
				  <td class="content-field-label"><cms:contentText code="promotion.basics" key="INCLUDE_UNDER_ARMOUR"/></td>
				  <td class="content-field-review">
				    <c:choose>
				      <c:when test="${ promotionOverviewForm.allowUnderArmour == 'false' }">
				        <cms:contentText code="promotion.overview" key="FALSE" />
				      </c:when>
				      <c:otherwise>
				        <cms:contentText code="promotion.overview" key="TRUE" />
				      </c:otherwise>
				    </c:choose>
				  </td>
				</tr>
		 </c:if>
		
<tr class="form-blank-row"><td></td></tr>