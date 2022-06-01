<%@ page import="com.biperf.core.domain.enums.PromotionEmailNotificationType"%>
<%@page import="com.biperf.core.domain.enums.PromotionNotificationFrequencyType"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionNotification.do?method=display";
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), url, paramMap ) );
	%>
    <c:choose>
      <c:when test="${ promotionOverviewForm.promotionStatus == 'expired' }">
        <c:if test="${!hideEditLinks}"> 
        <a class="content-link" href="<c:out value="${viewUrl}"/>">
		  <cms:contentText code="system.link" key="VIEW" />
		</a>
        </c:if>
	  </c:when>
	  <c:otherwise>
		<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		  <c:if test="${!hideEditLinks}"> 
		  <a class="content-link" href="<c:out value="${viewUrl}"/>">
          	<cms:contentText code="system.link" key="EDIT" />
		  </a>
		  </c:if>
		</beacon:authorize>
	  </c:otherwise>
	</c:choose>
  </td>
</tr>
<c:set var="previousStep" scope="page" value="" />
<c:forEach items="${claimFormNotificationList}" var="claimFormNotification">
  <c:set var="currentStep" scope="page" value="${claimFormNotification.claimFormStepEmailNotification.claimFormStep.cmKeyForName}" />
  <%-- Only display notifications whose notification messages have been selected, not all available on the claim form step--%>
  <c:if test="${claimFormNotification.notificationMessageId > 0}">
	  <tr>
	    <td>&nbsp;</td>
	    <td valign="top" class="content-field-label" nowrap>
	      <c:choose>
	        <c:when test="${ currentStep != previousStep }" >
	          <cms:contentText code="${promotionOverviewForm.claimFormAsset}" key="${claimFormNotification.claimFormStepEmailNotification.claimFormStep.cmKeyForName}" /> 
	        </c:when>
	        <c:otherwise>
	          &nbsp;
	        </c:otherwise>
	      </c:choose>
	    </td>    
		<td valign="top" class="content-field-review" nowrap>
		  <c:out value="${claimFormNotification.claimFormStepEmailNotification.claimFormStepEmailNotificationType.name}"/>
		</td>   
	  </tr>
   </c:if>
  <c:set var="previousStep" scope="page" value="${claimFormNotification.claimFormStepEmailNotification.claimFormStep.cmKeyForName}" />
</c:forEach>

<c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint') }">
  <tr>
    <td>&nbsp;</td>
    <td valign="top" class="content-field-label" nowrap>
     	<cms:contentText code="promotion.webrules" key="GOAL_SELECTION_SURVEY"/>&nbsp;
    </td>
    <c:if test="${promotionSurveyNotifications}">
	    <td valign="top" class="content-field-review" nowrap>              
		    <cms:contentText code="promotion.notification" key="YES"/>
		    &nbsp;
	    </td> 
   	</c:if> 
   	<c:if test="${!promotionSurveyNotifications}">
    	<td valign="top" class="content-field-review" nowrap>              
		   	<cms:contentText code="promotion.notification" key="NO"/>
		    &nbsp;
	    </td> 
   	</c:if>
  </tr>
</c:if>

<c:if test="${!promotionOverviewForm.notificationExists}">
  <tr>
	<td>&nbsp;</td>
	<td class="content-field-review" nowrap>
	  <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
	</td>
  </tr>
</c:if>
	 	  
<c:choose>
<c:when test="${promotionOverviewForm.promotionTypeCode eq 'self_serv_incentives'}">
  	<c:if test="${not empty promotionOverviewForm.creatorNotificationsEmailCount}">
		<tr>
    		<td>&nbsp;</td>
    		<td valign="top" class="content-field-label" nowrap><cms:contentText key="CREATOR_NOTIFICATIONS" code="promotion.ssi.notifications"/></td>
    		<td valign="top" class="content-field-review" nowrap><&nbsp;<c:out value="${promotionOverviewForm.creatorNotificationsEmailCount}"/>&nbsp;></td>	      
  		</tr>
  	</c:if>	
  	<c:if test="${not empty promotionOverviewForm.approverNotificationsEmailCount}">
		<tr>
    		<td>&nbsp;</td>
    		<td valign="top" class="content-field-label" nowrap><cms:contentText key="APPROVER_NOTIFICATIONS" code="promotion.ssi.notifications"/></td>
    		<td valign="top" class="content-field-review" nowrap><&nbsp;<c:out value="${promotionOverviewForm.approverNotificationsEmailCount}"/>&nbsp;></td>	      
  		</tr>
  	</c:if>	
	<c:if test="${not empty promotionOverviewForm.awardThemNowContestEmailCount}">
		<tr>
    		<td>&nbsp;</td>
    		<td valign="top" class="content-field-label" nowrap><cms:contentText key="AWARD_THEM_NOW_CONTEST" code="promotion.ssi.notifications"/></td>
    		<td valign="top" class="content-field-review" nowrap><&nbsp;<c:out value="${promotionOverviewForm.awardThemNowContestEmailCount}"/>&nbsp;></td>	      
  		</tr>
  	</c:if>
  	<c:if test="${not empty promotionOverviewForm.doThisGetThatContestEmailCount}">
		<tr>
    		<td>&nbsp;</td>
    		<td valign="top" class="content-field-label" nowrap><cms:contentText key="DO_THIS_GET_CONTEST" code="promotion.ssi.notifications"/></td>
    		<td valign="top" class="content-field-review" nowrap><&nbsp;<c:out value="${promotionOverviewForm.doThisGetThatContestEmailCount}"/>&nbsp;></td>	      
  		</tr>
  	</c:if>
  	<c:if test="${not empty promotionOverviewForm.objectivesContestEmailCount}">
		<tr>
    		<td>&nbsp;</td>
    		<td valign="top" class="content-field-label" nowrap><cms:contentText key="OBJECTIVES_CONTEST" code="promotion.ssi.notifications"/></td>
    		<td valign="top" class="content-field-review" nowrap><&nbsp;<c:out value="${promotionOverviewForm.objectivesContestEmailCount}"/>&nbsp;></td>	      
  		</tr>
  	</c:if>
  	<c:if test="${not empty promotionOverviewForm.stackRankContestEmailCount}">
		<tr>
    		<td>&nbsp;</td>
    		<td valign="top" class="content-field-label" nowrap><cms:contentText key="STACK_RANK_CONTEST" code="promotion.ssi.notifications"/></td>
    		<td valign="top" class="content-field-review" nowrap><&nbsp;<c:out value="${promotionOverviewForm.stackRankContestEmailCount}"/>&nbsp;></td>	      
  		</tr>
  	</c:if>
  	<c:if test="${not empty promotionOverviewForm.stepItUpContestEmailCount}">
		<tr>
    		<td>&nbsp;</td>
    		<td valign="top" class="content-field-label" nowrap><cms:contentText key="STEP_IT_UP_CONTEST" code="promotion.ssi.notifications"/></td>
    		<td valign="top" class="content-field-review" nowrap><&nbsp;<c:out value="${promotionOverviewForm.stepItUpContestEmailCount}"/>&nbsp;></td>	      
  		</tr>
  	</c:if>
</c:when>
<c:otherwise>
<c:forEach items="${promoNotificationList}" var="promoNotification">
  <tr>
    <td>&nbsp;</td>
    <td valign="top" class="content-field-label" nowrap>
      <c:out value="${promoNotification.promotionEmailNotificationType.name}"/>
    </td>
    <td valign="top" class="content-field-review" nowrap>              
	  <c:choose>
	    <c:when test="${ promoNotification.notificationMessageId > 0 }" >
	      <cms:contentText code="promotion.notification" key="YES"/>
	      	&nbsp;
	        <c:if test="${promoNotification.promotionEmailNotificationType.code != 'non_redemption_reminder' || promoNotification.descriminator != 'every_days_after_issuance' }">
	      	  <c:out value="${promoNotification.numberOfDays}"/>	     
	        </c:if> 
	        <c:if test="${promoNotification.promotionEmailNotificationType.code == 'approver_reminder'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_AFTER"/>
	        </c:if>
	        <c:if test="${ promoNotification.promotionEmailNotificationType.code == 'pax_inactivity'  or promoNotification.promotionEmailNotificationType.code == 'pax_inactivity_nomination'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_INACTIVE"/>
	        </c:if>
	         <c:if test="${promoNotification.promotionEmailNotificationType.code == 'pax_inactivity_recognition'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_INACTIVE_RECOGNITION"/>
	        </c:if>
	        <c:if test="${ promoNotification.promotionEmailNotificationType.code == 'program_launch'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
	      	</c:if>
	        <c:if test="${ promoNotification.promotionEmailNotificationType.code == 'program_end'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
	      	</c:if>
	        <c:if test="${ promoNotification.promotionEmailNotificationType.code == 'goal_not_selected'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
	      	</c:if>
	        <c:if test="${ promoNotification.promotionEmailNotificationType.code == 'budget_sweep'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
	      	</c:if>
	      	<c:if test="${ promoNotification.promotionEmailNotificationType.code == 'challengepoint_not_selected'}">
	      		&nbsp;<cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
	      	</c:if>
	      	<c:if test="${ promoNotification.promotionEmailNotificationType.code == 'non_redemption_reminder'}">
	      		<c:if test="${promoNotification.descriminator == 'every_days_after_issuance' }">
	      			&nbsp;<cms:contentText code="promotion.notification" key="EVERY"/> 
	      			<c:out value="${promoNotification.numberOfDays}"/>&nbsp;
	      			<cms:contentText code="promotion.notification" key="DAYS_AFTER_ISSUANCE"/>
	      		</c:if>
	      		<c:if test="${promoNotification.descriminator == 'days_after_promo_end' }">
	      			&nbsp;<cms:contentText code="promotion.notification" key="DAYS_AFTER_PROMOTION_END"/>
	      		</c:if>
	      	</c:if>
	      	<c:if test="${ promoNotification.promotionEmailNotificationType.code == 'manager_kpm_metric_update' || promoNotification.promotionEmailNotificationType.code == 'participant_kpm_metric_update' }">
		      	<c:if test="${ promoNotification.promotionNotificationFrequencyType.code !=null}">
		      		&nbsp;<c:out value="${promoNotification.promotionNotificationFrequencyType.name}"></c:out>
		      	</c:if>   
	      	</c:if>
		</c:when>
		<c:otherwise>
	      <cms:contentText code="promotion.notification" key="NO"/>
	    </c:otherwise>
	  </c:choose>
	</td>	      
  </tr>
</c:forEach>
</c:otherwise>
</c:choose>	 		 	  
<tr class="form-blank-row"><td></td></tr>