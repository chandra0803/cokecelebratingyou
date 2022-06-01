<%-- Trx History -- Admin User's View -- Product Claim --%>

<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.claim.TransactionHistoryValueObject"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.participant.Participant"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table width="100%">
  <tr>
    <td class="subheadline">
      <c:url var="openClaimsUrl" value="transactionHistory.do" >
        <c:param name="open" value="true"/>
      </c:url>
      <c:url var="closedClaimsUrl" value="transactionHistory.do" >
        <c:param name="open" value="false"/>
      </c:url>
      <c:if test="${open}">
        <cms:contentText key="OPEN_CLAIMS" code="participant.transactionhistory"/>
        &nbsp;|&nbsp;
        <a href="javascript:setActionDispatchAndSubmit('<c:out value="${closedClaimsUrl}"/>','display');" class="content-link"><cms:contentText key="CLOSED_CLAIMS" code="participant.transactionhistory"/></a>
      </c:if>
      <c:if test="${!open}">
        <a href="javascript:setActionDispatchAndSubmit('<c:out value="${openClaimsUrl}"/>','display');" class="content-link"><cms:contentText key="OPEN_CLAIMS" code="participant.transactionhistory"/></a>
        &nbsp;|&nbsp;
        <cms:contentText key="CLOSED_CLAIMS" code="participant.transactionhistory"/>
      </c:if>
    </td>
  </tr>
</table>

<table width="100%">
  <tr>
    <td align="right">
			<%	Map parameterMap = new HashMap();
					TransactionHistoryValueObject temp;
			%>
      <display:table defaultsort="1" defaultorder="ascending" name="transactionHistoryClaimList" id="valueObject" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
      <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
	</display:setProperty>
	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        <display:column titleKey="participant.transactionhistory.CLAIM_NUMBER" headerClass="crud-table-header-row" class="crud-content left-align nowrap content-field-label-top" sortable="true" sortProperty="claim.claimNumber">
					<%	temp = (TransactionHistoryValueObject)pageContext.getAttribute("valueObject");	
						Participant pax2 = (Participant)request.getAttribute("participant");
						parameterMap.put( "userId", pax2.getId() );						
							parameterMap.put( "claimId", temp.getClaim().getId() );
							parameterMap.put( "claimNumber", temp.getClaim().getClaimNumber() );
							String x = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ),"open");
							
							pageContext.setAttribute("claimDetailUrl", ClientStateUtils.generateEncodedLink( "", "claimDetailsDisplay.do?callingScreen=transactions&open="+x, parameterMap, true ) );
					%>
          <c:if test="${valueObject.claim.open}">
            <a href="javascript:setActionDispatchAndSubmit('<c:out value="${claimDetailUrl}"/>','showClaimDetail');" class="crud-content-link">
              <c:out value="${valueObject.claim.claimNumber}"/>
            </a>
          </c:if>
          <c:if test="${!valueObject.claim.open}">
            <c:set var="submitDate">
              <fmt:formatDate value="${valueObject.claim.submissionDate}" pattern="${JstlDatePattern}" />
            </c:set>
						<%	Participant pax = (Participant)request.getAttribute("participant");
							parameterMap.put( "userId", pax.getId() );
                            parameterMap.put( "promotionType", temp.getClaim().getPromotion().getPromotionType().getCode() );			
                            pageContext.setAttribute("payoutsUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryClaimPayouts.do", parameterMap, true ) );
							parameterMap.put( "dateSubmitted", pageContext.getAttribute("submitDate") );
							pageContext.setAttribute("auditMessagesUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryAuditMessages.do", parameterMap, true ) );
							parameterMap.put("livePromotionType", "product_claim");
							pageContext.setAttribute("activitiesUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryActivities.do", parameterMap, true ) ); 
						%>
            <table width="350">
              <tr>
                <%-- Non-Claim related deposits - eg. File Load deposit - no claim details --%>
                <c:if test="${valueObject.claim.id == 0 }">
                 <c:choose>
                  <c:when test="${valueObject.discretionary}">
		        	<cms:contentText key="DISCRETIONARY_SUBMITTER" code="claims.history"/>
		      	  </c:when>
                  <c:when test="${valueObject.sweepstakes}">
		        	<cms:contentText key="SWEEPSTAKES_SUBMITTER" code="claims.history"/>
		      	  </c:when>
                  <c:when test="${valueObject.managerOverride}">
		        	<cms:contentText key="MGR_OVERRIDE_SUBMITTER" code="claims.history"/>
		      	  </c:when>
		      	  <c:when test="${valueObject.reversalDescription != null}">
			      	<c:out value="${valueObject.reversalDescription}"/>
			      </c:when>
                  <c:when test="${valueObject.stackRank}">
		        	<cms:contentText key="STACK_RANK_SUBMITTER" code="claims.history"/>
		      	  </c:when>
		      	  <c:otherwise>
		      	  	<%-- Fileload --%>
		        	<cms:contentText key="SYSTEM_SUBMITTER" code="claims.history"/>
		      	  </c:otherwise>
		      	 </c:choose>
                </c:if>

                <%--  Product Claims with claim details --%>
                <c:if test="${valueObject.claim.id > 0 }">
                  <c:choose>
                  	<c:when test="${valueObject.managerOverride}">
                  		<td align="left" class="crud-content top-align">
                  	   		<cms:contentText key="MGR_OVERRIDE_SUBMITTER" code="claims.history"/>
                  	   	</td>
		      	  	</c:when>
		      	  	<c:otherwise>
                  	<td align="left" class="crud-content top-align">
                    	<a href="javascript:setActionDispatchAndSubmit('<c:out value="${claimDetailUrl}"/>','showClaimDetail');" class="crud-content-link"><c:out value="${valueObject.claim.claimNumber}"/></a>
                  	</td>
		      	  	</c:otherwise>
		      	  </c:choose>
                  <td class="crud-content top-align">
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${auditMessagesUrl}"/>','showAuditMessages');" class="crud-content-link">
                      <cms:contentText key="AUDIT_MESSAGES" code="participant.transactionhistory"/>
                    </a>
                    <br/>
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${activitiesUrl}"/>','showActivities');" class="crud-content-link">
                      <cms:contentText key="ACTIVITIES" code="participant.transactionhistory"/>
                    </a>
                    <c:if test="${!valueObject.stackRankClaim}">
                    <br/>
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${payoutsUrl}"/>','showPayouts');" class="crud-content-link">
                      <cms:contentText key="PAYOUTS" code="participant.transactionhistory"/>
                    </a>
                    </c:if>
                  </td>
                </c:if>
              </tr>
            </table>
          </c:if>
        </display:column>

        <display:column titleKey="participant.transactionhistory.PROMOTION" property="claim.promotion.name" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true"/>

        <display:column titleKey="participant.transactionhistory.DATE_SUBMITTED" headerClass="crud-table-header-row" class="crud-content right-align top-align nowrap" sortable="true" sortProperty="submissionDate">
          <fmt:formatDate value="${valueObject.claim.submissionDate}" pattern="${JstlDatePattern}" />
          <c:if test="${valueObject.claim.submitter.id != participant.id}">
            <br>
            <c:choose>
              <c:when test="${ not empty valueObject.claim.submitter.lastName }">
	            <cms:contentText key="BY" code="participant.transactionhistory"/>
    	        &nbsp;
                <c:out value="${valueObject.claim.submitter.firstName}"/>&nbsp;<c:out value="${valueObject.claim.submitter.lastName}"/>
              </c:when>
              <c:when test="${ not empty valueObject.createdBy.nameLFMWithComma }">
	            <cms:contentText key="BY" code="participant.transactionhistory"/>
    	        &nbsp;
                <c:out value="${valueObject.createdBy.nameLFMWithComma}"/>
              </c:when>
            </c:choose>
          </c:if>
        </display:column>

        <display:column titleKey="participant.transactionhistory.SOLD_TO" property="claim.companyName" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true"/>

        <display:column sortProperty="earnings" sortable="true" titleKey="claims.list.CC_EARNINGS_COLUMN_HEADER" headerClass="crud-table-header-row " class="crud-content top-align left-align">
          <c:if test="${!valueObject.stackRankClaim}">
            <c:if test="${valueObject.earnings > 0 || valueObject.earnings < 0 }">
              <c:out value="${valueObject.earnings}"/>&nbsp;<c:out value="${valueObject.awardTypeName}"/>
            </c:if>
            <c:if test="${valueObject.earnings == null}">
              <cms:contentText key="DID_NOT_MEET_PAYOUT_CRITERIA" code="claims.list"/>
            </c:if>
          </c:if>
        </display:column>
      </display:table>
    </td>
  </tr>
</table>
 
<table width="100%">
  <tr>
    <td class="subheadline">
      <c:if test="${open}">
        <cms:contentText key="OPEN_CLAIMS" code="participant.transactionhistory"/>
        &nbsp;|&nbsp;
        <a href="javascript:setActionDispatchAndSubmit('<c:out value="${closedClaimsUrl}"/>','display');" class="content-link"><cms:contentText key="CLOSED_CLAIMS" code="participant.transactionhistory"/></a>
      </c:if>
      <c:if test="${!open}">
        <a href="javascript:setActionDispatchAndSubmit('<c:out value="${openClaimsUrl}"/>','display');" class="content-link"><cms:contentText key="OPEN_CLAIMS" code="participant.transactionhistory"/></a>
        &nbsp;|&nbsp;
        <cms:contentText key="CLOSED_CLAIMS" code="participant.transactionhistory"/>
      </c:if>
    </td>
  </tr>
</table>
     
