<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%-- <%@ page import="com.biperf.core.domain.participant.Participant"%> --%>
<%@ page import="com.biperf.core.domain.claim.Claim" %>
<%@ page import="com.biperf.core.ui.claim.TransactionHistoryForm"%>
<%@ include file="/include/taglib.jspf"%>

<%	Map<Object,Object> paramMap = new HashMap<>();
		RecognitionHistoryValueObject temp;
%>
<display:table defaultsort="1" defaultorder="ascending" name="valueObjects" id="valueObject" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true">
	<display:setProperty name="basic.msg.empty_list_row">
		<tr class="crud-content" align="left"><td colspan="{0}">
         <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
            </td></tr>
		</display:setProperty>
		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
  <%-- the name of the promotion --%>
  <display:column titleKey="nomination.history.PROMOTION" property="promotion.name" headerClass="crud-table-header-row" class="crud-content top-align left-align nowrap" sortable="true"/>

  <%-- the date that the claim was approved --%>
  <display:column titleKey="nomination.history.DATE_APPROVED" headerClass="crud-table-header-row" class="crud-content-link top-align left-align" sortable="true" sortProperty="submissionDate" media="html">
    <table>
  	  <tr>
  	    <%-- Date approved section BEGIN --%>
  	    <td class="crud-content">
		    <c:if test="${valueObject.claim != null || valueObject.claims != null }">
					<%  temp = (RecognitionHistoryValueObject)pageContext.getAttribute("valueObject");
							paramMap.put( "promotionTypeCode", request.getAttribute("promotionTypeCode") );
							paramMap.put( "mode", request.getAttribute("mode") );
							//Participant pax = (Participant)request.getAttribute("participant");
							if (temp.getClaim() != null)
							{
								paramMap.put( "claimId", temp.getClaim().getId() );
								paramMap.put( "claimRecipientId", temp.getClaimRecipient().getId() );
								paramMap.put( "userId", request.getAttribute("participantId"));
							}
							if (temp.getClaims() != null)
							{
								//Cumulative Nomination claims have one and only one claim in the claim group
								Set claims = temp.getClaims();
								if (claims != null)
								{
									Iterator iter = claims.iterator();
									if (iter.hasNext())
									{
										Claim cumulativeNominationClaim = (Claim)iter.next(); 
										paramMap.put( "claimId", cumulativeNominationClaim.getId() );
									}						
									paramMap.put( "claimRecipientId", temp.getRecipient().getId() );
									paramMap.put( "userId", request.getAttribute("participantId"));
								}
							}
							String detailUrl = ClientStateUtils.generateEncodedLink( "", "recognitionDetail.do", paramMap, true );
							pageContext.setAttribute("detailUrl", detailUrl );
					%>
		      <a href="javascript:setActionAndSubmit('<c:out value='${detailUrl}'/>');" class="content-link">
		    </c:if>
		    <fmt:formatDate value="${valueObject.approvalDate}" pattern="${JstlDatePattern}"/>
		    <c:if test="${valueObject.claim != null }">
		      </a>
		    </c:if>
  	    </td>
  	    <%-- Date approved section END --%>
  	    
  	    <td>&nbsp;</td>
  	    
  	    <%-- Payouts link section BEGIN --%>
  	    <td class="crud-content">
			<%  	RecognitionHistoryValueObject tempVO;
					tempVO = (RecognitionHistoryValueObject)pageContext.getAttribute("valueObject");		
					TransactionHistoryForm tempForm = (TransactionHistoryForm)request.getAttribute("transactionHistoryForm");
					
					if( tempVO.getClaim() == null )
					{
					  if( tempVO.getJournalId() != null )
					  {
					    Map<Object, Object> parameterMap = new HashMap<>();
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
					    if( tempVO.getClaimGroup() != null )
					    {
					      parameterMap.put( "claimGroupId", tempVO.getClaimGroup().getId() );
					    }
						pageContext.setAttribute("payoutsUrl", ClientStateUtils.generateEncodedLink( "", "payoutTransactionDetailsDisplay.do?method=prepareUpdate&callingScreen=transactions", parameterMap, true ) );
					  }
					}
					else {
					    paramMap.put( "userId", tempVO.getClaimRecipient().getRecipient().getId() );										
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
							paramMap.remove( "dateSubmitted" );
							pageContext.setAttribute("payoutsUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryClaimPayouts.do", paramMap, true ) );
						}
					}
			%>
			
		  <c:if test="${payoutsUrl != null}" >
		    <c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${payoutsUrl}', 'showPayouts');"/>
            <a href="<c:out value='${tmp}'/>" class="crud-content-link">
              <cms:contentText key="PAYOUTS" code="participant.transactionhistory"/>
            </a>
          </c:if>
  	    </td>
  	    <%-- Payouts link section END --%>
  	  </tr>
    </table>
  </display:column>

  <display:column titleKey="nomination.history.DATE_APPROVED" headerClass="crud-table-header-row" class="crud-content-link top-align left-align" sortable="true" sortProperty="submissionDate" media="csv excel pdf">
    <fmt:formatDate value="${valueObject.approvalDate}" pattern="${JstlDatePattern}"/>
  </display:column>

  <%-- the name of the nominators --%>
  <display:column titleKey="nomination.history.SENDER" headerClass="crud-table-header-row" class="crud-content top-align left-align nowrap" sortable="true">
    <c:if test="${valueObject.claims != null}">
      <c:forEach items="${valueObject.claims}" var="nominationClaim" varStatus="status">
        <c:out value="${nominationClaim.submitter.nameLFMWithComma}"/>&nbsp;(<c:out value="${nominationClaim.node.name}"/>)
        <c:if test="${!status.last}"><br/></c:if>
      </c:forEach>
    </c:if>
    <c:if test="${valueObject.claims == null}">
      <c:choose>
        <c:when test="${valueObject.submitter.lastName == null}">
	      	<c:choose>
		      <c:when test="${valueObject.discretionary}">
		        <cms:contentText key="DISCRETIONARY_SUBMITTER" code="nomination.history"/>
		      </c:when>
		      <c:when test="${valueObject.sweepstakes }">
		      	<cms:contentText key="SWEEPSTAKES_SUBMITTER" code="nomination.history"/>
		      </c:when>
		      <c:otherwise>
		        <cms:contentText key="SYSTEM_SUBMITTER" code="nomination.history"/>
		      </c:otherwise>
		  	</c:choose>
        </c:when>
        <c:otherwise>
          <c:out value="${valueObject.submitter.nameLFMWithComma}"/>
        </c:otherwise>
      </c:choose>
    </c:if>
  </display:column>
  
  <%-- the award won  --%>
  <display:column titleKey="recognition.history.AWARD" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">            
		<c:if test="${ valueObject.awardQuantity > 0 || valueObject.awardQuantity < 0 }">
		  <c:if test="${!valueObject.reversal}">
            <fmt:formatNumber value="${valueObject.awardQuantity}"/>&nbsp;<c:out value="${valueObject.awardTypeName}"/>
          </c:if>
          <c:if test="${valueObject.reversal}">
            <fmt:formatNumber value="${valueObject.awardQuantity}"/>&nbsp;<cms:contentText key="REVERSE_SYMBOL" code="participant.transactionhistory"/>&nbsp;<c:out value="${valueObject.awardTypeName}"/>
          </c:if>
        </c:if>                        	
  </display:column>

</display:table>
