<%@ page import="java.util.*" %>
<%-- page import="com.biperf.core.domain.participant.Participant" --%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.TransactionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@page import="com.objectpartners.cms.util.CmsResourceBundle"%>
<%@ include file="/include/taglib.jspf"%>

<%-- Recognition Sent | Recognitions Received top --%>
<table width="100%">
  <tr>
    <td class="subheadline">
      <c:url var="recognitionsSentUrl" value="transactionHistory.do" >
        <c:param name="mode" value="sent"/>
      </c:url>

      <c:url var="recognitionsReceivedUrl" value="transactionHistory.do" >
        <c:param name="mode" value="received"/>
      </c:url>

      <c:if test="${mode == 'sent'}">
        <cms:contentText key="RECOGNITIONS_SENT" code="${promotionTypeCode}"/>
        &nbsp;|&nbsp;
        <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${recognitionsReceivedUrl}', 'showActivity');"/>
        <a href="<c:out value='${tmp}'/>" class="content-link">
          <cms:contentText key="RECOGNITIONS_RECEIVED" code="${promotionTypeCode}"/>
        </a>
      </c:if>

      <c:if test="${mode == 'received'}">
        <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${recognitionsSentUrl}', 'showActivity');"/>
        <a href="<c:out value='${tmp}'/>" class="content-link">
          <cms:contentText key="RECOGNITIONS_SENT" code="${promotionTypeCode}"/>
        </a>
        &nbsp;|&nbsp;
        <cms:contentText key="RECOGNITIONS_RECEIVED" code="${promotionTypeCode}"/>
      </c:if>
    </td>
  </tr>
</table>
<%-- END Recognition Sent | Recognitions Received top --%>

<%-- Recognition Table --%>
<c:if test="${promotionType == 'recognition'}">
<table width="50%">
  <tr>
    <td align="right">
			
      <display:table defaultsort="1" defaultorder="ascending" name="recognitionHistoryValueObjects" id="valueObject" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
      <display:setProperty name="basic.msg.empty_list_row">
		<tr class="crud-content" align="left"><td colspan="{0}">
          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
        </td></tr>
		</display:setProperty>
		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        <%-- Promotion Name column --%>
        <display:column titleKey="recognition.history.PROMOTION" property="promotion.name" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>
        
        <%-- Transaction Date column --%>
        <%
           String dateTitle;
           if ("sent".equals(request.getAttribute("mode")))
           {
             dateTitle = CmsResourceBundle.getCmsBundle().getString( "recognition.history.DATE_SUBMITTED"); 
           } else 
           {
             dateTitle = CmsResourceBundle.getCmsBundle().getString( "recognition.history.DATE_RECEIVED"); 
           }
        %>
        <display:column title="<%=dateTitle %>" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="submissionDate">
          <table>
            <tr>
              <%-- Get client state to build the date link to go to the Recognition Detail page --%>
               <td class="crud-content">
				<%  	Map paramMap = new HashMap();
						RecognitionHistoryValueObject tempVO;
						tempVO = (RecognitionHistoryValueObject)pageContext.getAttribute("valueObject");		
						TransactionHistoryForm tempForm = (TransactionHistoryForm)request.getAttribute("transactionHistoryForm");
						paramMap.put( "userId", tempForm.getUserId() );
						paramMap.put( "promotionId", tempForm.getPromotionId() );
						paramMap.put( "promotionType", tempForm.getPromotionType() );
						paramMap.put( "startDate", tempForm.getStartDate() );
						paramMap.put( "endDate", tempForm.getEndDate() );
						paramMap.put( "proxyUser", tempForm.getProxyUserId() );
						paramMap.put( "mode", request.getAttribute("mode") );				
						if (tempVO.getClaim() != null)
						{
							paramMap.put( "id", tempVO.getClaim().getId() );
							paramMap.put( "claimRecipientId", tempVO.getClaimRecipient().getId() );
						}
						pageContext.setAttribute("recognitionDetailUrl", ClientStateUtils.generateEncodedLink( "", "showRecognitionTransactionDetail.do", paramMap, true ) );
				%>
                <c:choose>
                  <c:when test="${valueObject.claim == null}">
                    <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}" />
                  </c:when>
                  <c:otherwise>
                    <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${recognitionDetailUrl}', 'showRecognitionDetail');"/>
                    <a href="<c:out value='${tmp}'/>" class="crud-content-link">
                      <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}" />
                    </a>
                  </c:otherwise>
                </c:choose>

                	<c:if test="${valueObject.proxyUser != null}">
                   	<br>
                		<cms:contentText key="BY" code="recognition.history"/>&nbsp;<c:out value="${valueObject.proxyUser.lastName}"/>,&nbsp;<c:out value="${valueObject.proxyUser.firstName}"/>
                  </c:if>   
                  <c:if test="${mode == 'sent' and valueObject.submitter.id != participantId}">
                    <br>
                    <cms:contentText key="BY" code="${promotionTypeCode}"/>
                    &nbsp;
                    <c:out value="${valueObject.submitter.lastName}"/>,&nbsp;<c:out value="${valueObject.submitter.firstName}"/>&nbsp;<c:out value="${valueObject.submitter.middleName}"/>
                </c:if>
              </td>
              
              <td>&nbsp;</td>

			  <%-- Get client state to build the activities and payouts links  --%>
              <td class="crud-content">
				<%  	paramMap.put( "userId", tempForm.getUserId() );										
						paramMap.put( "promotionId", tempForm.getPromotionId() );
						paramMap.put( "promotionType", tempForm.getPromotionType() );
						paramMap.put( "startDate", tempForm.getStartDate() );
						paramMap.put( "endDate", tempForm.getEndDate() );
						paramMap.put( "livePromotionId", tempForm.getLivePromotionId() );
						paramMap.put( "livePromotionType", tempForm.getLivePromotionType() );
						paramMap.put( "liveStartDate", tempForm.getLiveStartDate() );
						paramMap.put( "liveEndDate", tempForm.getLiveEndDate() );
						paramMap.put( "mode", request.getAttribute("mode") );
						paramMap.put( "dateSubmitted", request.getAttribute("submitDate") );
						if (tempVO.getClaim() != null)
						{
							paramMap.put( "claimId", tempVO.getClaim().getId() );
							paramMap.put( "claimNumber", tempVO.getClaim().getClaimNumber() );
							paramMap.remove( "claimRecipientId" );
							paramMap.remove( "id" );
							pageContext.setAttribute("activitiesUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryActivities.do", paramMap, true ) );
						
							paramMap.remove( "dateSubmitted" );
							pageContext.setAttribute("payoutsUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryClaimPayouts.do", paramMap, true ) );
						}
				%>

				<%-- Sent view: activities link --%>
                <c:if test="${mode == 'sent'}">
                  <c:choose>
                    <c:when test="${valueObject.claim.open}">
                      <cms:contentText key="NOT_YET_APPROVED" code="${promotionTypeCode}"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${activitiesUrl}','showActivities');"/>
                      <a href="<c:out value='${tmp}'/>" class="crud-content-link">
                        <cms:contentText key="ACTIVITIES" code="participant.transactionhistory"/>
                      </a>
                    </c:otherwise>
                  </c:choose>
                </c:if>
				
				<%--  Received view: activities and payouts links --%>
				<c:if test="${mode == 'received'}">					
                  <c:choose>
                  	<%-- For non-claim Transactions (discretionary/fileload, etc) show payouts link only --%>
                    <c:when test="${valueObject.claim == null}">
                    	<%  Map parameterMap = new HashMap();
							parameterMap.put( "userId", tempVO.getRecipient().getId() );
							parameterMap.put( "promotionId", tempVO.getPromotion().getId() );
							parameterMap.put( "promotionType", tempVO.getPromotion().getPromotionType().getCode() );
							parameterMap.put( "startDate", tempForm.getStartDate() );
							parameterMap.put( "endDate", tempForm.getEndDate() );
							parameterMap.put( "mode", request.getAttribute("mode")  );
							parameterMap.put( "transactionId", tempVO.getJournalId() );
							parameterMap.put( "firstName", tempVO.getRecipient().getFirstName() );
							parameterMap.put( "lastName", tempVO.getRecipient().getLastName() );
							parameterMap.put( "middleName", tempVO.getRecipient().getMiddleName() );
							pageContext.setAttribute("singlePayoutUrl", ClientStateUtils.generateEncodedLink( "", "payoutTransactionDetailsDisplay.do?method=prepareUpdate&callingScreen=transactions", parameterMap, true ) );
						%>
                      <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${singlePayoutUrl}', 'prepareUpdate');"/>
                      <c:choose>
                        <c:when test="${valueObject.discretionary}">
                        	&nbsp;
                        </c:when>
                        <c:otherwise> 
	                      <a href="<c:out value='${tmp}'/>" class="crud-content-link">
	                        <cms:contentText key="PAYOUTS" code="participant.transactionhistory"/>
	                      </a>
	                    </c:otherwise>
	                   </c:choose>
                    </c:when>
                    <%-- For claim Transactions show both --%>
                    <c:otherwise>
                      <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${activitiesUrl}', 'showActivities');"/>
                      <a href="<c:out value='${tmp}'/>" class="crud-content-link">
                        <cms:contentText key="ACTIVITIES" code="participant.transactionhistory"/>
                      </a>
                      <br>
                      <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${payoutsUrl}', 'showPayouts');"/>
                      <a href="<c:out value='${tmp}'/>" class="crud-content-link">
                        <cms:contentText key="PAYOUTS" code="participant.transactionhistory"/>
                      </a>
                    </c:otherwise>
                  </c:choose>
                </c:if>

              </td>
            </tr>
          </table>
        </display:column>
                
        <%--  Send View: Receiver column --%>
        <c:if test="${mode == 'sent'}">
          <display:column titleKey="recognition.history.RECEIVER" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
            <c:out value="${valueObject.claimRecipient.recipientDisplayName}"/>
          </display:column>          
        </c:if>

		<%-- Received View: Sender column --%>
        <c:if test="${mode == 'received'}">
          <display:column titleKey="recognition.history.SENDER" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
            <c:choose>
              <c:when test="${valueObject.submitter.lastName == null}">
		      	<c:choose>
			      <c:when test="${valueObject.discretionary}">
			        <cms:contentText key="DISCRETIONARY_SUBMITTER" code="${promotionTypeCode}"/>
			      </c:when>
			      <c:when test="${valueObject.sweepstakes }">
			      	<cms:contentText key="SWEEPSTAKES_SUBMITTER" code="${promotionTypeCode}"/>
			      </c:when>
			      <c:when test="${valueObject.reversalDescription != null }">
			      	<c:out value="${valueObject.reversalDescription}"/>
			      </c:when>
			      <c:otherwise>
			        <%-- Fileload --%>
			        <cms:contentText key="SYSTEM_SUBMITTER" code="${promotionTypeCode}"/>
			      </c:otherwise>
			  	</c:choose>
              </c:when>
              <c:otherwise>
                <c:out value="${valueObject.submitter.nameLFMWithComma}"/>
              </c:otherwise>
            </c:choose>
          </display:column>
        </c:if>
        <%-- Award column --%>
        <display:column titleKey="recognition.history.AWARD" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">            
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
          <c:if test="${valueObject.awardQuantity > 0 || valueObject.awardQuantity < 0 }">
             <c:if test="${!valueObject.reversal}">
                 <fmt:formatNumber value="${valueObject.awardQuantity}"/> <c:out value="${valueObject.awardTypeName}"/>
             </c:if>
             <c:if test="${valueObject.reversal}">
                 <fmt:formatNumber value="${valueObject.awardQuantity}"/>&nbsp;<cms:contentText key="REVERSE_SYMBOL" code="participant.transactionhistory"/>&nbsp;<c:out value="${valueObject.awardTypeName}"/>
             </c:if>
          </c:if>
        </display:column>
        
        <%--  Send View: Status column --%>
        <c:if test="${mode == 'sent'}">
          <display:column titleKey="recognition.history.STATUS" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
            <c:out value="${valueObject.claimRecipient.approvalStatusType.name}"/>
          </display:column>
        </c:if>            


      </display:table>
    </td>
  </tr>
</table>
</c:if>

<%--  Nomination  --%>
<c:if test="${promotionType == 'nomination'}">
<%-- history table --%>
<%-- Senthil --%>
<table width="50%">
  <tr>
    <td align="right">
      <c:if test="${promotionType == 'nomination'}">
        <c:if test="${mode == 'received'}">
          <c:import url="/claim/nomrec/transHistNominationsReceived.jsp"/>
        </c:if>
        <c:if test="${mode == 'sent'}">
          <c:import url="/claim/nomrec/transHistNominationsSent.jsp"/>
        </c:if>
      </c:if>
    </td>
  </tr>
</table>
<%-- Senthil --%>
</c:if>

<%-- Recognition Sent | Recognitions Received bottom --%>
<table width="100%">
<tr>
  <td class="subheadline">
  <c:url var="recognitionsSentUrl" value="transactionHistory.do" >
  <c:param name="mode" value="sent"/>
</c:url>

<c:url var="recognitionsReceivedUrl" value="transactionHistory.do" >
  <c:param name="mode" value="received"/>
</c:url>

    <c:if test="${mode == 'sent'}">
      <cms:contentText key="RECOGNITIONS_SENT" code="${promotionTypeCode}"/>
      &nbsp;|&nbsp;
      <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${recognitionsReceivedUrl}', 'showActivity');"/>
      <a href='<c:out value="${tmp}"/>' class="content-link">
        <cms:contentText key="RECOGNITIONS_RECEIVED" code="${promotionTypeCode}"/>
      </a>
    </c:if>
    <c:if test="${mode == 'received'}">
      <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${recognitionsSentUrl}', 'showActivity');"/>
      <a href='<c:out value="${tmp}"/>' class="content-link">
        <cms:contentText key="RECOGNITIONS_SENT" code="${promotionTypeCode}"/>
      </a>
      &nbsp;|&nbsp;
      <cms:contentText key="RECOGNITIONS_RECEIVED" code="${promotionTypeCode}"/>
    </c:if>
  </td>
</tr>
</table>
<%-- END Recognition Sent | Recognitions Received bottom --%>
