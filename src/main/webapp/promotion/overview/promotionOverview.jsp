<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="promotionOverview"> 
  <html:hidden property="method"/>
  <html:hidden property="version"/>
  <html:hidden property="numberOfProducts"/>
  <beacon:client-state>
	<beacon:client-state-entry name="promotionId" value="${promotionOverviewForm.promotionId}"/>
	<beacon:client-state-entry name="claimFormId" value="${promotionOverviewForm.claimFormId}"/>
  </beacon:client-state>

  <table cellpadding="3" cellspacing="1" width="100%" >
    <tr>
      <td>
        <table width="100%">
          <tr>
            <td colspan="3">
              <table>
                <tr valign="top">
                  <td  class="headline"><c:out value="${promotionOverviewForm.promotionType}"/> <cms:contentText code="promotion.overview" key="HEADER"/></td>      
                </tr>
                <tr valign="top">
                  <td nowrap class="content-bold">
                    <c:out value="${promotionOverviewForm.promotionName}" />
                    <c:if test="${ ! empty promotionOverviewForm.parentPromotionId }">
                      <br>
                      <cms:contentText code="promotion.list" key="CHILD_OF"/> <c:out value="${promotionOverviewForm.parentPromotionName}"/>
                    </c:if>
                  </td>
                </tr>
                <tr valign="top">
                  <td class="content-instruction"><cms:contentText code="promotion.overview" key="INSTRUCTION"/></td>
                </tr>
                <c:if test="${ ! empty extractEmailMessage }">
	                <tr valign="top">
	                  <td class="content-instruction"><cms:contentText code="promotion.overview" key="EMAIL_GIVER_LIST_MESSAGE"/></td>
	                </tr>
	            </c:if>
	            <c:if test="${ rpmAudienceUpdated }">
	                <tr valign="top">
	                  <td class="content-instruction"><font color="red"><cms:contentText code="promotion.overview" key="RPM_WARNING"/></font></td>
	                </tr>
	            </c:if>
              </table>
            </td>
            <td></td>
            <td colspan="3">
              <table>
                <tr valign="top">
                  <%-- Only display the family section if this is a product claim promotion --%>
                  <c:if test="${promotionOverviewForm.promotionTypeCode == 'product_claim' }" >
                    <jsp:include page="promotionFamily.jsp"/>
                  </c:if>
                </tr>
              </table>
            </td>
          </tr>
        </table>

        <table width="100%">
          <tr class="form-blank-row"><td></td></tr>
          <tr>
            <td colspan="3"><cms:errors/></td>
          </tr>
          <tr>
            <td colspan="3" valign="top">
              <table>
                <%--  BASICS SECTION --%>
                <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_1"/>&nbsp;&nbsp;
                    <cms:contentText code="promotion.overview" key="BASICS_LABEL"/>&nbsp;&nbsp;
                    <jsp:include page="basicsSection.jsp"/>
                  </td>
                </tr>
                
                <%--  FORM RULES SECTION --%>
                <c:if test="${promotionOverviewForm.promotionTypeCode == 'product_claim' ||
                              promotionOverviewForm.promotionTypeCode == 'recognition' ||
                              promotionOverviewForm.promotionTypeCode == 'nomination'}">
                  <tr>
                    <td colspan="3" class="subheadline">
                      <cms:contentText code="promotion.overview" key="STEP_2"/>&nbsp;&nbsp;
                      <cms:contentText code="promotion.overview" key="FORM_RULES"/>&nbsp;&nbsp;
                      <jsp:include page="formRulesSection.jsp"/>
                    </td>
                  </tr>
                </c:if>

                <%--  AUDIENCE SECTION --%>
                <c:if test="${promotionOverviewForm.promotionTypeCode != 'engagement'}">
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:choose>
                      <c:when test="${promotionOverviewForm.promotionTypeCode == 'quiz' ||
                      				  promotionOverviewForm.promotionTypeCode == 'diy_quiz' ||
                                      promotionOverviewForm.promotionTypeCode == 'survey' ||
                                      promotionOverviewForm.promotionTypeCode == 'goalquest' ||  
                                      promotionOverviewForm.promotionTypeCode == 'challengepoint' ||
                                      promotionOverviewForm.promotionTypeCode == 'wellness' ||
                                      promotionOverviewForm.promotionTypeCode == 'throwdown' || 
                                      promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
                        <cms:contentText code="promotion.overview" key="STEP_2"/>
                      </c:when>
                      <c:otherwise>
                        <cms:contentText code="promotion.overview" key="STEP_3"/>
                      </c:otherwise>
                    </c:choose>
                    &nbsp;&nbsp;
                    <cms:contentText code="promotion.overview" key="AUDIENCES_LABEL"/>&nbsp;&nbsp;
                    <jsp:include page="audienceSection.jsp"/>
                  </td>
                </tr>
              </c:if>  
              <%-- COLUMN BREAK FOR RECOGNITION AND PRODUCT CLAIM = START A SECOND COLUMN NOW --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'survey' }">
                </table>
                </td>
                <td>&nbsp;</td>
                <td colspan="3" valign="top">
                <table>
              </c:if>
              
              <%--  KPM TARGETS SECTION --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'engagement'}">
                  <tr>
                    <td colspan="3" class="subheadline">
                      <cms:contentText code="promotion.overview" key="STEP_2"/>&nbsp;&nbsp;
                      <cms:contentText code="promotion.overview" key="KPM_TARGETS"/>&nbsp;&nbsp;
                      <jsp:include page="engagementBenchmarksSection.jsp"/>
                    </td>
                  </tr>
              </c:if>
                        
              <%-- PAYOUT SECTION --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'product_claim' }" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_4"/>&nbsp;&nbsp;
                    <cms:contentText code="promotion.overview" key="PRODUCTS_AND_PAYOUTS"/>&nbsp;&nbsp;
                    <jsp:include page="payoutsSection.jsp"/>
                  </td>
                </tr>
              </c:if>
                
              <%-- AWARDS SECTION --%>
              <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' ||
                             promotionOverviewForm.promotionTypeCode == 'quiz' ||                             
                             promotionOverviewForm.promotionTypeCode == 'wellness' ||
                             promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}">
                      <cms:contentText code="promotion.overview" key="STEP_4"/>
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'quiz' ||
                                  promotionOverviewForm.promotionTypeCode == 'wellness' ||
                                   promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
                      <cms:contentText code="promotion.overview" key="STEP_3"/>
                    </c:if>
                    &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="AWARDS"/>&nbsp;&nbsp;
                    <jsp:include page="awardsSection.jsp"/>
                  </td>
                </tr>
              </c:if>
                           
              <%-- GOALS AND PAYOUTS SECTION --%>
              <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' )}" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_3"/>
                    &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="GOALS_AND_PAYOUTS"/>&nbsp;&nbsp;
                    <jsp:include page="goalsSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              
               <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'challengepoint')}" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_3"/>
                    &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="CHALLENGEPOINTS_AND_PAYOUT"/>&nbsp;&nbsp;
                    <jsp:include page="challengepointPayoutSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              
              <%-- PAYOUTS AND AWARDS SECTION --%>
              <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'throwdown' )}" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_3"/>
                    &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="PAYOUTS"/>&nbsp;&nbsp;
                    <jsp:include page="throwdownPayoutSection.jsp"/>
                  </td>
                </tr>
              </c:if>              
              
              <%-- COLUMN BREAK FOR QUIZ = START A SECOND COLUMN NOW --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'quiz' }">
                </table>
                </td>
                <td>&nbsp;</td>
                <td colspan="3" valign="top">
                <table>
              </c:if>
              
              <%-- CELEBRATIONS SECTION --%>
              <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' && promotionOverviewForm.includeCelebrations }" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_5"/>
                    &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="CELEBRATIONS"/>&nbsp;&nbsp;
                    <jsp:include page="celebrationsSection.jsp"/>
                  </td>
                </tr>
              </c:if>
                    
              <%-- SWEEPSTAKES SECTION --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' ||
                            promotionOverviewForm.promotionTypeCode == 'product_claim' ||
                            promotionOverviewForm.promotionTypeCode == 'quiz' ||
                            promotionOverviewForm.promotionTypeCode == 'survey'}">
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:choose>
                      <c:when test="${ ( promotionOverviewForm.promotionTypeCode == 'recognition' && promotionOverviewForm.includeCelebrations ) }">
                        <cms:contentText code="promotion.overview" key="STEP_6"/>	
                      </c:when>
                      <c:when test="${ ( promotionOverviewForm.promotionTypeCode == 'recognition' && !promotionOverviewForm.includeCelebrations) }">
                        <cms:contentText code="promotion.overview" key="STEP_5"/>	
                      </c:when>
                    </c:choose>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'product_claim'}">
                      <cms:contentText code="promotion.overview" key="STEP_5"/>
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'quiz'}">
                      <cms:contentText code="promotion.overview" key="STEP_4"/>
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'survey'}">
                      <cms:contentText code="promotion.overview" key="STEP_3"/>
                    </c:if>                          
                    &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="SWEEPSTAKES"/>&nbsp;&nbsp;
                    <jsp:include page="sweepstakesSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              
              <%-- BEHAVIORS SECTION for nomination --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}" >
                <tr>
                  <td colspan="3" class="subheadline">
                        <cms:contentText code="promotion.overview" key="STEP_4"/>&nbsp;&nbsp;
                        <cms:contentText code="promotion.overview" key="BEHAVIORS"/>&nbsp;&nbsp;
                    <jsp:include page="behaviorsSection.jsp"/>
                  </td>
                </tr>
                
                <%-- eCARDS SECTION --%>
                <tr>
                  <td colspan="3" class="subheadline">
                       <cms:contentText code="promotion.overview" key="STEP_5"/>&nbsp;&nbsp;
                       <cms:contentText code="promotion.overview" key="ECARDS_CERTIFICATES"/>&nbsp;&nbsp;
                    <jsp:include page="eCardsCertificatesSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              

              <%-- COLUMN BREAK FOR RECOGNITION AND PRODUCT CLAIM = START A SECOND COLUMN NOW --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode != 'quiz' &&
                            promotionOverviewForm.promotionTypeCode != 'survey' &&
                            promotionOverviewForm.promotionTypeCode != 'wellness' }">
                </table>
                </td>
                <td>&nbsp;</td>
                <td colspan="3" valign="top">
                <table>
              </c:if>
              
              <%--  PARTNER PAYOUT SECTION --%>
		        <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
		        				  && promotionOverviewForm.partnerAudienceExists == 'true'}">
				     <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_4"/>&nbsp;&nbsp;
                    <cms:contentText code="promotion.overview" key="PARTNER_PAYOUT"/>&nbsp;&nbsp;
                    <jsp:include page="partnerPayoutSection.jsp"/>
                  </td>
                </tr>
			       <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_5"/>&nbsp;&nbsp;
                    <cms:contentText code="promotion.overview" key="MANAGER_OVERRIDE"/>&nbsp;&nbsp;
                    <jsp:include page="overrideSection.jsp"/>
                  </td>
                </tr>
              </c:if>
			   <%--  MANAGER OVERRIDE SECTION --%>
			     <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint') 
			     					&& promotionOverviewForm.partnerAudienceExists == 'false'}">
				   <tr>
                  <td colspan="3" class="subheadline">
                    <cms:contentText code="promotion.overview" key="STEP_4"/>&nbsp;&nbsp;
                    <cms:contentText code="promotion.overview" key="MANAGER_OVERRIDE"/>&nbsp;&nbsp;
                    <jsp:include page="overrideSection.jsp"/>
                  </td>
                </tr>
              </c:if>
                    
              <%-- BEHAVIORS SECTION  for recognition --%>
              <c:if test="${( promotionOverviewForm.promotionTypeCode == 'recognition'  && !isPurlIncluded ) }" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:choose>
                      <c:when test="${ ( promotionOverviewForm.includeCelebrations ) }">
                        <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp; 	
                      </c:when>
                      <c:when test="${ ( !promotionOverviewForm.includeCelebrations) }">
                        <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp; 	
                      </c:when>
                      <c:otherwise>
                        <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp; 	
                      </c:otherwise>
                    </c:choose>
                    <cms:contentText code="promotion.overview" key="BEHAVIORS"/>&nbsp;&nbsp;
                    <jsp:include page="behaviorsSection.jsp"/>
                  </td>
                </tr>
              </c:if>

                <%-- eCARDS SECTION --%>
             <c:if test="${ ( promotionOverviewForm.promotionTypeCode == 'recognition' && !isPurlIncluded ) }" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:choose>
                      <c:when test="${ ( promotionOverviewForm.includeCelebrations ) }">
                        <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp; 	
                      </c:when>
                      <c:when test="${ ( !promotionOverviewForm.includeCelebrations) }">
                        <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp; 	
                      </c:when>
                      <c:otherwise>
                        <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp; 	
                      </c:otherwise>
                    </c:choose>
                    <cms:contentText code="promotion.overview" key="ECARDS_CERTIFICATES"/>&nbsp;&nbsp;
                    <jsp:include page="eCardsCertificatesSection.jsp"/>
                  </td>
                </tr>
              </c:if>

			<%-- ACTIVITY SUBMISSION SECTION --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}" >
                <tr>
                  <td colspan="3" class="subheadline">
                      <cms:contentText code="promotion.overview" key="STEP_4"/>
                    &nbsp;&nbsp;<cms:contentText code="promotion.ssi.activitysubmission" key="ACTIVITY_SUB"/>&nbsp;&nbsp;
                    <jsp:include page="activitySubmissionSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              	
              <%--  APPROVAL SECTION --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode != 'quiz' && 
              				promotionOverviewForm.promotionTypeCode != 'diy_quiz' && 
              				promotionOverviewForm.promotionTypeCode != 'engagement' && 
                            promotionOverviewForm.promotionTypeCode != 'survey' &&
                            promotionOverviewForm.promotionTypeCode != 'challengepoint' &&
                            promotionOverviewForm.promotionTypeCode != 'goalquest' &&
                            promotionOverviewForm.promotionTypeCode != 'wellness' &&
                            promotionOverviewForm.promotionTypeCode != 'throwdown' }">
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim'}">
                      <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination'}">
                          <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
                      <cms:contentText code="promotion.overview" key="STEP_5"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}"> 
                     <c:choose>
                   		<c:when test="${isPurlIncluded}">
                   		  <c:choose>
                   		    <c:when test="${ promotionOverviewForm.includeCelebrations }">
                   		      <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp;
                   		    </c:when>
                   		    <c:otherwise>
                   		      <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                   		    </c:otherwise>
                   		  </c:choose>
                   		</c:when>
                   		<c:otherwise>
                   		  <c:choose>
                   		    <c:when test="${ promotionOverviewForm.includeCelebrations }">
                   		      <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
                   		    </c:when>
                   		    <c:otherwise>
                   		      <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp;
                   		    </c:otherwise>
                   		  </c:choose>
                    		
                   		</c:otherwise>
                  	 </c:choose>                      
                      <cms:contentText code="promotion.overview" key="APPROVALS_LABEL" />&nbsp;&nbsp;                
                      <jsp:include page="approvalSection.jsp"/>
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode != 'recognition'}">
	                    <cms:contentText code="promotion.overview" key="APPROVALS_LABEL" />&nbsp;&nbsp;                
	                    <jsp:include page="approvalSection.jsp"/>
	                </c:if>
                  </td>
                </tr>
              </c:if>
              
              <%-- NOMINATION AWARDS SECTION  --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
                 <tr>
              		<td colspan="3" class="subheadline">
                       <cms:contentText code="promotion.overview" key="STEP_7"/>
                       &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="AWARDS"/>&nbsp;&nbsp;
                    	<jsp:include page="awardsSection.jsp"/>
                  	</td>
                </tr>
              </c:if>
              
              <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
                 <tr>
                  <td colspan="3" class="subheadline">
                   <cms:contentText code="promotion.overview" key="STEP_8"/>
                            &nbsp;&nbsp;<cms:contentText code="promotion.overview" key="SWEEPSTAKES"/>&nbsp;&nbsp;
                    <jsp:include page="sweepstakesSection.jsp"/>
                  </td>
                 </tr>
              </c:if>
              
              <%-- PUBLIC RECOGNITION ADD ON  --%>
              <c:if test="${ ( promotionOverviewForm.promotionTypeCode == 'recognition' && !isPurlIncluded ) 
              			  || ( promotionOverviewForm.promotionTypeCode == 'nomination' ) }">
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}">
                      <c:choose> 
                        <c:when test="${ promotionOverviewForm.includeCelebrations }">
                          <cms:contentText code="promotion.overview" key="STEP_10"/>&nbsp;&nbsp;
                        </c:when>
                        <c:otherwise>
                          <c:choose>
		                    <c:when test="${ ( promotionOverviewForm.awardType != 'merchandise' && !isPurlIncluded ) }">                    
		                      <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
		                    </c:when>
		                    <c:otherwise>
	                          <c:choose>
		                        <c:when test="${ ( promotionOverviewForm.awardStructure == 'level' && !isPurlIncluded )}">
			                      <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
	    	                    </c:when>
	    	                    <c:otherwise>
			                      <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
	    	                    </c:otherwise>
	    	                  </c:choose>
		                    </c:otherwise>
		                  </c:choose>
                        </c:otherwise>
                      </c:choose>
                    </c:if>
                    
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
                          <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
                    </c:if>                    
                     <cms:contentText code="promotion.overview" key="PUBLIC_RECOGNITION"/>&nbsp;&nbsp;
                      <jsp:include page="publicRecognitionAddOnSection.jsp"/>
                  </td>
                </tr>
              </c:if>
                    
              <%-- NOTIFICATION SECTION --%>
               <c:if test="${promotionOverviewForm.promotionTypeCode != 'wellness' }">
              <tr>
                <td colspan="3" class="subheadline">
                  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
                    <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp;
                  </c:if>
                  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'engagement' }">
                    <cms:contentText code="promotion.overview" key="STEP_3"/>&nbsp;&nbsp;
                  </c:if>
                  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
                  	<cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                  </c:if>
                  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }">
                  <c:choose>
                   	<c:when test="${isPurlIncluded}">
                   	  <c:choose>
                   	    <c:when test="${ promotionOverviewForm.includeCelebrations }">
                   	      <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp;
                   	    </c:when>
                   	    <c:otherwise>
                   	      <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp;
                   	    </c:otherwise>
                   	  </c:choose>
                   	</c:when>
                   	<c:otherwise>
                   	  <c:choose>
                   	    <c:when test="${ promotionOverviewForm.includeCelebrations }">
                   	      <cms:contentText code="promotion.overview" key="STEP_11"/>&nbsp;&nbsp;
                   	    </c:when>
                   	    <c:otherwise>
                   	      <cms:contentText code="promotion.overview" key="STEP_10"/>&nbsp;&nbsp;
                   	    </c:otherwise>
                   	  </c:choose>
                   	</c:otherwise>
                   </c:choose>                    
	    	      </c:if>
                  <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
                        <cms:contentText code="promotion.overview" key="STEP_10"/>&nbsp;&nbsp;
                  </c:if>
                  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'quiz' }">
                    <cms:contentText code="promotion.overview" key="STEP_5"/>&nbsp;&nbsp;
                  </c:if>
                  <c:if test="${ promotionOverviewForm.promotionTypeCode == 'survey' || promotionOverviewForm.promotionTypeCode == 'throwdown'}">
                    <cms:contentText code="promotion.overview" key="STEP_4"/>&nbsp;&nbsp;
                  </c:if>                        
                 <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'true'}">
                    <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                 </c:if>
                 <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'false'}">
                    <cms:contentText code="promotion.overview" key="STEP_5"/>&nbsp;&nbsp;
                 </c:if>
                 <c:if test="${ promotionOverviewForm.promotionTypeCode == 'diy_quiz' }">
                    <cms:contentText code="promotion.overview" key="STEP_3"/>&nbsp;&nbsp;
                 </c:if>
               
                 <cms:contentText code="promotion.overview" key="NOTIFICATIONS_LABEL"/>&nbsp;&nbsp;
                 <jsp:include page="notificationSection.jsp"/>
                </td>
              </tr>
              </c:if>
              
              <%--  WEB RULES SECTION --%>
              <c:if test="${promotionOverviewForm.promotionTypeCode != 'survey' && promotionOverviewForm.promotionTypeCode != 'wellness' && promotionOverviewForm.promotionTypeCode != 'diy_quiz' && promotionOverviewForm.promotionTypeCode != 'engagement' && promotionOverviewForm.promotionTypeCode != 'self_serv_incentives' }">                    
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
                      <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}">
	                  <c:choose>
		                  <c:when test="${ ( promotionOverviewForm.awardType != 'merchandise' && !isPurlIncluded ) }"> 
		                    <c:choose>
		                      <c:when test="${ promotionOverviewForm.includeCelebrations }">
		                        <cms:contentText code="promotion.overview" key="STEP_12"/>&nbsp;&nbsp;
		                      </c:when>
		                      <c:otherwise>
		                        <cms:contentText code="promotion.overview" key="STEP_11"/>&nbsp;&nbsp;
		                      </c:otherwise>
		                    </c:choose>                   
		                  </c:when>
		                  <c:otherwise>
	                        <c:choose>
		                      <c:when test="${ ( promotionOverviewForm.awardStructure == 'level' && !isPurlIncluded )}">
		                        <c:choose>
		                          <c:when test="${ promotionOverviewForm.includeCelebrations }">
		                       		 <cms:contentText code="promotion.overview" key="STEP_12"/>&nbsp;&nbsp;
		                      	  </c:when>
		                      	  <c:otherwise>
		                        	<cms:contentText code="promotion.overview" key="STEP_11"/>&nbsp;&nbsp;
		                      	  </c:otherwise>
		                    	</c:choose>    
	    	                  </c:when>
	    	                  <c:otherwise>
	    	                  <c:choose>
	    	                   <c:when test="${isPurlIncluded}">
	    	                     <c:choose>
		                      	   <c:when test="${ promotionOverviewForm.includeCelebrations }">
		                      	     <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
		                     	   </c:when>
		                     	   <c:otherwise>
		                      	     <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp;
		                       	   </c:otherwise>
		                   	     </c:choose>    
			                   </c:when>
			                   <c:otherwise>
			                    <c:choose>
		                          <c:when test="${ promotionOverviewForm.includeCelebrations }">
		                            <cms:contentText code="promotion.overview" key="STEP_12"/>&nbsp;&nbsp;
		                          </c:when>
		                          <c:otherwise>
		                            <cms:contentText code="promotion.overview" key="STEP_11"/>&nbsp;&nbsp;
		                          </c:otherwise>
		                        </c:choose>    
			                   </c:otherwise>			                    
			                  </c:choose>
	    	                  </c:otherwise>
	    	                </c:choose>
		                  </c:otherwise>
		              </c:choose>
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
                          <cms:contentText code="promotion.overview" key="STEP_11"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'true'}">
                 	  <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp;
                 	</c:if>
                 	<c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'false'}">
                 	  <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                 	</c:if>
                       <c:if test="${ promotionOverviewForm.promotionTypeCode == 'quiz' }">
                      <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'throwdown'}">
                      <cms:contentText code="promotion.overview" key="STEP_5"/>&nbsp;&nbsp;
                    </c:if>
                   
                     <cms:contentText code="promotion.overview" key="RULES_TEXT"/>&nbsp;&nbsp;
                    <jsp:include page="webRulesSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              
              <%-- BILL CODE SECTION --%>
              <c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' ||
                             promotionOverviewForm.promotionTypeCode == 'quiz' ||
                             promotionOverviewForm.promotionTypeCode == 'product_claim' ||
                             promotionOverviewForm.promotionTypeCode == 'self_serv_incentives' ||
                             (promotionOverviewForm.promotionTypeCode == 'nomination' && promotionOverviewForm.awardType != 'other') ||
                             promotionOverviewForm.promotionTypeCode == 'goalquest'  ||
                             promotionOverviewForm.promotionTypeCode == 'challengepoint'}" >
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}">
                      <c:choose>
                        <c:when test="${ isPurlIncluded && !promotionOverviewForm.includeCelebrations }">
			              <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
			            </c:when>
			            <c:when test="${ isPurlIncluded && promotionOverviewForm.includeCelebrations }">
			              <cms:contentText code="promotion.overview" key="STEP_10"/>&nbsp;&nbsp;
			            </c:when>
			            <c:otherwise>
			              <c:choose>
		                      <c:when test="${ ( promotionOverviewForm.includeCelebrations )}">
			                    <cms:contentText code="promotion.overview" key="STEP_13"/>&nbsp;&nbsp;
	    	                  </c:when>
	    	                  <c:otherwise>
			                    <cms:contentText code="promotion.overview" key="STEP_12"/>&nbsp;&nbsp;
	    	                  </c:otherwise>
	    	                </c:choose>
			            </c:otherwise>
                      </c:choose>
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
                          <cms:contentText code="promotion.overview" key="STEP_12"/>&nbsp;&nbsp;
                    </c:if> 
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
                          <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp;
                    </c:if>                       
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'quiz'}">
                      <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
                      <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'true'}">
                 	  <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp;
                 	</c:if>
                 	<c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'false'}">
                 	  <cms:contentText code="promotion.overview" key="STEP_7"/>&nbsp;&nbsp;
                 	</c:if>
                    <cms:contentText code="promotion.bill.code" key="BILL_CODE" />&nbsp;&nbsp;
                    <jsp:include page="billCodeSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              
              <%--  TRANSLATION RULES SECTION --%>
              <c:if test="${displayTranslations && promotionOverviewForm.promotionTypeCode != 'diy_quiz' && promotionOverviewForm.promotionTypeCode != 'engagement' && promotionOverviewForm.promotionTypeCode != 'self_serv_incentives'}">                    
                <tr>
                  <td colspan="3" class="subheadline">
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'product_claim' }">
                      <cms:contentText code="promotion.overview" key="STEP_10"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}">
                      <c:choose>
                        <c:when test="${ isPurlIncluded && !promotionOverviewForm.includeCelebrations }">
			              <cms:contentText code="promotion.overview" key="STEP_10"/>&nbsp;&nbsp;
			            </c:when>
			            <c:when test="${ isPurlIncluded && promotionOverviewForm.includeCelebrations }">
			              <cms:contentText code="promotion.overview" key="STEP_11"/>&nbsp;&nbsp;
			            </c:when>
			            <c:otherwise>
			              <c:choose>
		                      <c:when test="${ ( promotionOverviewForm.includeCelebrations )}">
			                    <cms:contentText code="promotion.overview" key="STEP_14"/>&nbsp;&nbsp;
	    	                  </c:when>
	    	                  <c:otherwise>
			                    <cms:contentText code="promotion.overview" key="STEP_13"/>&nbsp;&nbsp;
	    	                  </c:otherwise>
	    	                </c:choose>
			            </c:otherwise>
                      </c:choose>
                    </c:if>
                    <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
                          <cms:contentText code="promotion.overview" key="STEP_13"/>&nbsp;&nbsp;
                    </c:if>                    
                    <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'true'}">                    
                     <cms:contentText code="promotion.overview" key="STEP_9"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ (promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint')
                 					&& promotionOverviewForm.partnerAudienceExists == 'false'}">
                    <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'quiz' }">
                      <cms:contentText code="promotion.overview" key="STEP_8"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'throwdown' }">
                      <cms:contentText code="promotion.overview" key="STEP_6"/>&nbsp;&nbsp;
                    </c:if>
                    <c:if test="${ promotionOverviewForm.promotionTypeCode == 'survey' }">
                      <cms:contentText code="promotion.overview" key="STEP_5"/>&nbsp;&nbsp;
                    </c:if>
                     <cms:contentText code="promotion.overview" key="TRANSLATION"/>&nbsp;&nbsp;
                    <jsp:include page="translationsSection.jsp"/>
                  </td>
                </tr>
              </c:if>
              
              
              <%--  WIZARD ORDER SECTION --%>
               <c:if test="${ promotionOverviewForm.promotionTypeCode == 'nomination'}" >                    
                <tr>
                  <td colspan="3" class="subheadline">
                          <cms:contentText code="promotion.overview" key="STEP_14"/>&nbsp;&nbsp;
                     <cms:contentText code="promotion.overview" key="WIZARD"/>&nbsp;&nbsp;
                    <jsp:include page="wizardOrderSection.jsp"/>
                  </td>
                </tr>
              </c:if>
             </table>
            </td>
          </tr>

          <tr class="form-buttonrow">
            <td colspan="7" align="center">
            <c:choose>
             <c:when test="${promotionOverviewForm.promotionTypeCode == 'challengepoint'}">
              <html:button property="backToPromo" styleClass="content-buttonstyle" onclick="callUrl('promotionListDisplay.do')" >
                <cms:contentText code="promotion.overview" key="VIEW_PROMOTION_LIST" />
              </html:button>
             </c:when>
             <c:otherwise>
              <html:button property="backToPromo" styleClass="content-buttonstyle" onclick="callUrl('promotionListDisplay.do')" >
                <cms:contentText code="promotion.overview" key="BACK_TO_PROMOS" />
              </html:button>
             </c:otherwise>
             </c:choose>
              <c:if test="${ promotionOverviewForm.promotionStatus == 'under_construction' }">
                &nbsp;&nbsp;&nbsp;&nbsp;
				 <c:choose>
					 <c:when test="${ promotionOverviewForm.processingMode == 'online' && promotionOverviewForm.promotionTypeCode == 'recognition'}">
						<html:button property="markAsComplete" styleClass="content-buttonstyle" onclick="callUrl('${validateAudienceViewUrl}')" >
						   <cms:contentText code="promotion.overview" key="MARK_AS_COMPLETE" />
						</html:button>
					  </c:when>
					  <c:otherwise>
	                	<html:button property="markAsComplete" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionOverviewComplete.do','markAsComplete')" >
	                  		<cms:contentText code="promotion.overview" key="MARK_AS_COMPLETE" />
	                    </html:button>
					  </c:otherwise>
				 </c:choose>
              </c:if>

			<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
              <c:if test="${ promotionOverviewForm.promotionStatus == 'live' && promotionOverviewForm.enableEndPromoOption == 'true' }">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <html:button property="markAsComplete" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionOverviewEnd.do','endPromo')" >
                  <cms:contentText code="promotion.overview" key="END_PROMO" />
                </html:button>
              </c:if>
              </beacon:authorize>

              <c:if test="${ promotionOverviewForm.promotionStatus == 'complete' }">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <html:button property="markAsComplete" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionOverviewLaunch.do','launchPromo')" >
                  <cms:contentText code="promotion.overview" key="LAUNCH_PROMO" />
                </html:button>
              </c:if>

				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
              <c:if test="${ empty promotionOverviewForm.parentPromotionId }">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <html:button property="copyPromo" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('copyPromotion.do','display');" >
                  <cms:contentText code="promotion.overview" key="COPY_PROMOTION" />
                </html:button>
                </c:if>
                </beacon:authorize>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  
</html:form>