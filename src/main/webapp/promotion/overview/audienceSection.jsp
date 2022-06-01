<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionAudience.do?method=display";
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

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label" valign="top" nowrap>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' ||promotionOverviewForm.promotionTypeCode == 'survey' }">
      <cms:contentText code="promotion.overview" key="ELIGIBLE_SUBMITTORS"/>
    </c:if>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }">
      <cms:contentText code="promotion.audience" key="GIVERS"/>
    </c:if>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'quiz' || promotionOverviewForm.promotionTypeCode == 'diy_quiz' || promotionOverviewForm.promotionTypeCode == 'wellness' }">
      <cms:contentText code="promotion.audience" key="PARTICIPANTS"/>
    </c:if>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination' }">
      <cms:contentText code="promotion.audience" key="NOMINATORS"/>
    </c:if>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'goalquest' ||promotionOverviewForm.promotionTypeCode == 'challengepoint'}">
      <cms:contentText code="promotion.audience" key="EXISTING_PAXS"/>
    </c:if>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'throwdown'}">
      <cms:contentText code="promotion.audience" key="SPECTATORS"/>
    </c:if>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'engagement'}">
      <cms:contentText code="promotion.audience" key="ELIGIBLE_AUDIENCES"/>
    </c:if>
    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
      <cms:contentText code="promotion.ssi.audience" key="CREATOR_AUDIENCE"/>
    </c:if>
  </td>
  <td class="content-field-review" nowrap valign="top">
    <c:choose>
      <c:when test="${ promotionOverviewForm.allPaxAsSubmitters == 'true' }">
        <cms:contentText code="promotion.audience" key="ALL_ACTIVE_PAX"/>
      </c:when>
      <c:when test="${ promotionOverviewForm.entireParentAudience == 'true' }">
        <cms:contentText code="promotion.audience" key="ENTIRE_PARENT"/>
      </c:when>
      <c:when test="${ promotionOverviewForm.selfEnrollOnly == 'true' }">
        <cms:contentText code="promotion.audience" key="ALL_PAX_SELF_ENROLL"/>
      </c:when>
      <c:otherwise>
        <c:if test="${!promotionOverviewForm.submitterAudienceExists}">		  			
		  <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>		  				  			
		</c:if>		  
        <table>
          <c:forEach items="${promotionOverviewForm.submitterAudienceList}" var="promoSubmitterAudience" varStatus="status">
            <tr>
              <td class="content-field-review">
                <c:out value="${promoSubmitterAudience.audience.name}"/>
              </td>
              <td class="content-field-review">                
                  &lt;&nbsp;
                  <c:out value="${promoSubmitterAudience.audience.size}"/>
                  &nbsp;&gt;                                        
              </td>
            </tr>
          </c:forEach>
        </table>
      </c:otherwise>
    </c:choose>
  </td>
</tr>
<c:if test="${ promotionOverviewForm.promotionTypeCode != 'goalquest' && promotionOverviewForm.promotionTypeCode != 'challengepoint' && promotionOverviewForm.promotionTypeCode != 'diy_quiz' }">
<c:if test="${promotionOverviewForm.promotionTypeCode != 'quiz' && promotionOverviewForm.promotionTypeCode != 'wellness' && promotionOverviewForm.promotionTypeCode != 'survey'
              && promotionOverviewForm.promotionTypeCode != 'throwdown' && promotionOverviewForm.promotionTypeCode != 'engagement'}">
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label" valign="top" nowrap>
      <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
        <cms:contentText code="promotion.overview" key="ELIGIBLE_TEAM_MEMBERS"/>
      </c:if>
      <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }">
        <cms:contentText code="promotion.audience" key="RECEIVERS"/>
      </c:if>
      <c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination' }">
        <cms:contentText code="promotion.audience" key="NOMINEES"/>
      </c:if>
      <c:if test="${ promotionOverviewForm.promotionTypeCode == 'goalquest' ||promotionOverviewForm.promotionTypeCode == 'challengepoint' }">
        <cms:contentText code="promotion.audience" key="SELF_ENROLL_PAXS"/>
      </c:if>
      <c:if test="${ promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
        <cms:contentText code="promotion.ssi.audience" key="PARTICIPANT_AUDIENCE"/>
      </c:if>
    </td>
    <td class="content-field-review" colspan="2" nowrap>
      <c:choose>
		<c:when test="${ promotionOverviewForm.allPaxAsReceivers == 'true' }">
          <cms:contentText code="promotion.audience" key="ALL_ACTIVE_PAX"/>
        </c:when>
		<c:when test="${ promotionOverviewForm.teamSameAsSubmitters == 'true' }">
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
            <cms:contentText code="promotion.audience" key="SAME_AS_SUBMITTER"/>
          </c:if>
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }">
            <cms:contentText code="promotion.audience" key="SAME_AS_GIVER"/>
          </c:if>
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination' }">
            <cms:contentText code="promotion.audience" key="SAME_AS_NOMINATOR"/>
          </c:if>
        </c:when>
        <c:when test="${ promotionOverviewForm.teamFromSubmitterNode == 'true' }">
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
            <cms:contentText code="promotion.audience" key="ALL_PAX_FROM_NODE"/>
          </c:if>
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }">
            <cms:contentText code="promotion.audience" key="ALL_PAX_FROM_GIVER_NODE"/>
          </c:if>
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination' }">
            <cms:contentText code="promotion.audience" key="ALL_PAX_FROM_NOMINATOR_NODE"/>
          </c:if>
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'self_serv_incentives' }">
            <cms:contentText code="promotion.ssi.audience" key="CREATOR_ORG_ONLY"/>
          </c:if>
        </c:when>
        <c:when test="${ promotionOverviewForm.teamFromSubmitterNodeBelow == 'true' }">
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }">
            <cms:contentText code="promotion.audience" key="ALL_PAX_FROM_GIVER_NODE_BELOW"/>
          </c:if>
          <c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination' }">
           <cms:contentText code="promotion.audience" key="ALL_PAX_FROM_NOMINATOR_NODE_BELOW"/>
          </c:if>
           <c:if test="${ promotionOverviewForm.promotionTypeCode == 'self_serv_incentives' }">
            <cms:contentText code="promotion.ssi.audience" key="ORG_AND_BELOW"/>
          </c:if>
        </c:when>
        <c:when test="${ promotionOverviewForm.teamSpecifyAudience == 'true' }">
          <c:if test="${!promotionOverviewForm.teamAudienceExists}">	
		    <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
		  </c:if>			  		
          <table>
            <c:forEach items="${promotionOverviewForm.teamAudienceList}" var="promoTeamAudience" varStatus="status">
              <tr>
                <td class="content-field-review">
                  <c:out value="${promoTeamAudience.audience.name}"/>
                </td>
                <td class="content-field-review">                  
                    &lt;&nbsp;
                    <c:out value="${promoTeamAudience.audience.size}"/>
                    &nbsp;&gt;                                       
                </td>
              </tr>
            </c:forEach>
          </table>
        </c:when>
        <c:otherwise>
        	<c:choose>
        		<c:when test="${ promotionOverviewForm.promotionTypeCode ne 'self_serv_incentives' }"><cms:contentText code="promotion.audience" key="TEAM_NOT_COLLECTED"/></c:when>
        	</c:choose>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
</c:if>
</c:if>

<c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}">
<%-- Commented out to enable this option in a later release
<c:if test="${promotionOverviewForm.awardType == 'points'}">
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label" valign="top" nowrap>
    <cms:contentText code="promotion.overview" key="RECOGNITION_OPEN_ENROLLMENT"/>
  </td>
  <td class="content-field-review" nowrap valign="top">
    <c:out value="${promotionOverviewForm.openEnrollmentEnabled}"/>
  </td>
</tr>
</c:if>
--%>

	<tr>
	  <td>&nbsp;</td>
	  <td class="content-field-label" valign="top" nowrap>
	    <cms:contentText code="promotion.overview" key="SELF_RECOGNITION"/>
	  </td>
	  <td class="content-field-review" nowrap valign="top">
	    <c:out value="${promotionOverviewForm.selfRecognitionEnabled}"/>
	  </td>
	</tr>

</c:if>

 <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')  && promotionOverviewForm.partnerAudienceExists == 'true' && promotionOverviewForm.preSelectedPartner == null}">
	<tr>
	 <td>&nbsp;</td>
	 <td class="content-field-label" valign="top" nowrap>
	 <cms:contentText code="promotion.overview" key="PARTNERS"/>
	 </td>
	 <td class="content-field-review" colspan="2" nowrap>
	 <c:if test="${ ! promotionOverviewForm.partnerAudienceExists || promotionOverviewForm.partnerAudienceList == null || empty promotionOverviewForm.partnerAudienceList  }">	
	    <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
	 </c:if>
	 <c:if test="${promotionOverviewForm.partnerAudienceExists && ( promotionOverviewForm.partnerAudienceList != null || ! empty promotionOverviewForm.partnerAudienceList ) }">
	 
	 <table>
	    <c:forEach items="${promotionOverviewForm.partnerAudienceList}" var="promoPartnerAudience" varStatus="status">
	    <tr>
	       <td class="content-field-review">
	            <c:out value="${promoPartnerAudience.audience.name}"/>
	       </td>
	       <td class="content-field-review">                  
	        &lt;&nbsp;
	        <c:out value="${promoPartnerAudience.audience.size}"/>
	        &nbsp;&gt;                                       
	        </td>
	        </tr>
	        </c:forEach>
	 </table>
	</c:if>
	 </td>
	</tr>
 </c:if>
 
<c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')  && promotionOverviewForm.partnerAudienceExists == 'true'  && promotionOverviewForm.preSelectedPartner != null }">
	<tr>
	 <td>&nbsp;</td>
	 <td class="content-field-label" valign="top" nowrap>
	 <cms:contentText code="promotion.overview" key="PARTNERS"/>
	 </td>
	 <td class="content-field-review" colspan="2" nowrap>
	 <c:choose>
	 <c:when test = "${promotionOverviewForm.preSelectedPartner == 'nodebasedpartners'}">
	 	<cms:contentText key="NODE_BASED_PARTNERS" code="promotion.audience"/>
	 </c:when>
	 <c:otherwise>
	 	<cms:contentText key="USER_CHARACTERISTICS" code="promotion.audience"/>
	 </c:otherwise>
	 </c:choose>
	  </td>
	 </tr>
</c:if> 
 
<c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')  && promotionOverviewForm.partnerAudienceExists == 'true'  && promotionOverviewForm.autoCompletePartners == 'true' }">
	<tr>
	 <td>&nbsp;</td>
	 <td class="content-field-label" valign="top" nowrap>
	 <cms:contentText code="promotion.audience" key="PARTNER_DISPLAY"/>
	 </td>
	 <td class="content-field-review" colspan="2" nowrap>
	 <cms:contentText key="PRE_SELECTED_PARTNERS" code="promotion.audience"/>
	  </td>
	 </tr>
</c:if>
 
<c:if test="${ promotionOverviewForm.promotionTypeCode == 'throwdown' }" >
<tr>
 <td>&nbsp;</td>
 <td class="content-field-label" valign="top" nowrap>
 <cms:contentText code="promotion.audience" key="COMPETITORS"/>
 </td>

<td class="content-field-review" nowrap valign="top">

  <c:if test="${!promotionOverviewForm.divisionAudienceExists}">
	<cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
  </c:if>	  
  
  <c:if test="${promotionOverviewForm.divisionAudienceExists}">
    <table>
    <c:forEach items="${promotionOverviewForm.divisionAudienceMap}" var="division">
     <tr>
      <td class="content-field-review" colspan="2">
       <cms:contentText code="promotion.payout.throwdown" key="DIVISION_NAME"/> : <c:out value="${division.key}"/>
      </td>
     </tr>
	   <c:forEach items="${division.value}" var="divCompAudience">
	   <tr>
	   <td class="content-field-review">
       <c:out value="${divCompAudience.audience.name}"/>
       </td>	   
	   <td class="content-field-review">
	   &lt;&nbsp;
       <c:out value="${divCompAudience.audience.size}"/>
       &nbsp;&gt;                                       
       </td>
       </tr>
       </c:forEach>
    </c:forEach>
    </table>
  </c:if>
</td>
  	    
</tr>
</c:if>
		
		<%  Map paramMap2 = new HashMap();
			PromotionOverviewForm tempForm = (PromotionOverviewForm)request.getSession().getAttribute("promotionOverviewForm");
			paramMap2.put( "promotionId",tempForm.getPromotionId() );
			pageContext.setAttribute("viewGoalRegCodeURL", ClientStateUtils.generateEncodedLink( "", "viewGoalRegistrationCodes.do", paramMap2) );
			pageContext.setAttribute("viewChallengePointRegCodeURL", ClientStateUtils.generateEncodedLink( "", "viewChallengePointRegistrationCodes.do", paramMap2) );
		%>

<c:if test="${promotionOverviewForm.promotionTypeCode == 'goalquest'}">
	<c:if test="${promotionOverviewForm.selfEnrollment}">
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
	    <td class="content-field-label" valign="top" nowrap>
	    <html:button property="viewRegCodes" styleClass="content-buttonstyle" onclick="javascript:popUpWin('${viewGoalRegCodeURL}', 'console', 750, 600, false, true);" >
             <cms:contentText code="promotion.audience" key="VIEW_REG_CODES" />
        </html:button>
    	</td>
    </c:if>
</c:if>
<c:if test="${promotionOverviewForm.promotionTypeCode == 'challengepoint' }">
	<c:if test="${promotionOverviewForm.selfEnrollment}">
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
	    <td class="content-field-label" valign="top" nowrap>
	    <html:button property="viewRegCodes" styleClass="content-buttonstyle" onclick="javascript:popUpWin('${viewChallengePointRegCodeURL}', 'console', 750, 600, false, true);" >
             <cms:contentText code="promotion.audience" key="VIEW_REG_CODES" />
        </html:button>
    	</td>
    </c:if>
</c:if>
<tr class="form-blank-row"><td></td></tr>