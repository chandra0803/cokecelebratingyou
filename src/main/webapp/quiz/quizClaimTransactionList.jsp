<%--  Trx History -- Admin User's View -- Quiz --%>

<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.claim.QuizHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.TransactionHistoryForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table>
  <tr>
    <td> 	
      <display:table defaultsort="1" defaultorder="ascending" name="quizValueAttemptedList" id="quizHistoryValueObject" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
	<display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
	</display:setProperty>
	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        <display:column titleKey="quiz.history.PROMOTION" headerClass="crud-table-header-row" class="crud-content-bold center-align" sortable="true">
          <c:out value="${quizHistoryValueObject.promotionName}"/> - <c:out value="${quizHistoryValueObject.quizAttempt}"/>
        </display:column>

        <display:column titleKey="quiz.history.DATE_COMPLETED" headerClass="crud-table-header-row" class="crud-content-bold center-align" sortable="true">
					<%	
							Map parameterMap = new HashMap();
							QuizHistoryValueObject temp;
                            TransactionHistoryForm temp2 = (TransactionHistoryForm)request.getAttribute( "transactionHistoryForm" );
							temp = (QuizHistoryValueObject)pageContext.getAttribute( "quizHistoryValueObject" );
                            parameterMap.put( "userId", temp2.getUserId() );
							parameterMap.put( "id", temp.getId() );
							parameterMap.put( "promotionId", temp2.getPromotionId() );
							parameterMap.put( "livePromotionId", temp2.getLivePromotionId() );
							pageContext.setAttribute("quizDetailUrl", ClientStateUtils.generateEncodedLink( "", "showQuizTransactionDetail.do", parameterMap, true ) );
							parameterMap.remove( "id" );
							parameterMap.remove( "promotionId" );
							parameterMap.remove( "livePromotionId" );
							parameterMap.put( "claimId", temp.getId() );
                            parameterMap.put( "promotionType", temp2.getPromotionType() );
							pageContext.setAttribute("auditMessagesUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryAuditMessages.do", parameterMap, true ) );
							pageContext.setAttribute("activitiesUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryActivities.do", parameterMap, true ) );
							pageContext.setAttribute("payoutsUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryClaimPayouts.do", parameterMap, true ) );
					%>
          <table>
            <tr>
              <c:choose>
                <%--  non quiz related deposit has no quiz details --%>
                <c:when test="${quizHistoryValueObject.nonClaimRelatedDeposits}">
                  <td class="crud-content">
                    <fmt:formatDate value="${quizHistoryValueObject.dateCompleted}" pattern="${JstlDatePattern}" />
                  </td>
                </c:when>
                <c:otherwise>
                  <td class="crud-content">
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${quizDetailUrl}"/>','showQuizDetail');" class="content-link">
                      <fmt:formatDate value="${quizHistoryValueObject.dateCompleted}" pattern="${JstlDatePattern}" />
                    </a>
                  </td>
                </c:otherwise>
              </c:choose>
              <td class="crud-content">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </td>
              <td class="crud-content">
                <c:choose>
                  <c:when test="${quizHistoryValueObject.nonClaimRelatedDeposits}">
                   <c:choose>
                    <c:when test="${quizHistoryValueObject.discretionary}">
		        		<cms:contentText key="DISCRETIONARY_SUBMITTER" code="claims.history"/>
		      	    </c:when>
	 		        <c:when test="${quizHistoryValueObject.sweepstakes }">
		      	  	  <cms:contentText key="SWEEPSTAKES_SUBMITTER" code="claims.history"/>
			        </c:when>
			        <c:when test="${quizHistoryValueObject.reversalDescription != null}">
			      		<c:out value="${quizHistoryValueObject.reversalDescription}"/>
			      	</c:when>
		      	    <c:otherwise>
		      	    	<%-- file load --%>
		        		<cms:contentText key="SYSTEM_SUBMITTER" code="claims.history"/>
		      	    </c:otherwise>
		      	   </c:choose>
                  </c:when>
                  <c:when test="${!quizHistoryValueObject.quizComplete}">
                  </c:when>
                  <c:when test="${!quizHistoryValueObject.passed}">
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${activitiesUrl}"/>','showActivities');" class="crud-content-link">
                      <cms:contentText key="ACTIVITIES" code="participant.transactionhistory"/>
                    </a>
                  </c:when>
                  <c:when test="${quizHistoryValueObject.passed}">
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${auditMessagesUrl}"/>','showAuditMessages');" class="crud-content-link">
                      <cms:contentText key="AUDIT_MESSAGES" code="participant.transactionhistory"/>
                    </a>
                    <br/>
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${activitiesUrl}"/>','showActivities');" class="crud-content-link">
                      <cms:contentText key="ACTIVITIES" code="participant.transactionhistory"/>
                    </a>
                    <br/>
                    <a href="javascript:setActionDispatchAndSubmit('<c:out value="${payoutsUrl}"/>','showPayouts');" class="crud-content-link">
                      <cms:contentText key="PAYOUTS" code="participant.transactionhistory"/>
                    </a>
                  </c:when>
                </c:choose>
              </td>
            </tr>
          </table>
        </display:column>

        <display:column titleKey="quiz.history.RESULT" headerClass="crud-table-header-row" class="crud-content-bold center-align" sortable="true">
          <c:choose>
            <c:when test="${quizHistoryValueObject.reversalDescription != null}">
			  <cms:contentText key="REVERSED" code="claims.history"/>
			</c:when>
            <c:when test="${quizHistoryValueObject.passed}">
              <cms:contentText code="quiz.history" key="PASS" />
            </c:when>
            <c:when test="${quizHistoryValueObject.quizComplete}">
              <cms:contentText code="quiz.history" key="DID_NOT_PASS" />
            </c:when>
            <c:when test="${quizHistoryValueObject.nonClaimRelatedDeposits}">
              <cms:contentText code="quiz.history.detail" key="AWARD" />
            </c:when>
            <c:otherwise>
              <cms:contentText code="quiz.history" key="INCOMPLETE" />
            </c:otherwise>
          </c:choose>
        </display:column>

        <%-- Displays quiz award(if exists on the journal) or Non quiz taking related award granted by journal type like file load or discretionary awards --%>
        <display:column titleKey="quiz.history.detail.AWARD" headerClass="crud-table-header-row" class="crud-content" sortable="true" >        	
        	<c:if test="${quizHistoryValueObject.awardQuantity > 0 || quizHistoryValueObject.awardQuantity < 0 }">
            	<fmt:formatNumber value="${ quizHistoryValueObject.awardQuantity }" />&nbsp;<c:out value="${quizHistoryValueObject.awardTypeName}"/>
            </c:if>                        	
        </display:column>	
      </display:table>
    </td>
  </tr>
</table>
