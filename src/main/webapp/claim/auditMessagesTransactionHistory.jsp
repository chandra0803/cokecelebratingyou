<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="transactionHistory">
	<html:hidden property="method" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${transactionHistoryForm.userId}"/>
	 	<beacon:client-state-entry name="livePromotionId" value="${transactionHistoryForm.livePromotionId}"/>
	 	<beacon:client-state-entry name="livePromotionType" value="${transactionHistoryForm.livePromotionType}"/>
	 	<beacon:client-state-entry name="liveStartDate" value="${transactionHistoryForm.liveStartDate}"/>
	 	<beacon:client-state-entry name="liveEndDate" value="${transactionHistoryForm.liveEndDate}"/>
	 	<beacon:client-state-entry name="promotionId" value="${transactionHistoryForm.promotionId}"/>
	 	<beacon:client-state-entry name="promotionType" value="${transactionHistoryForm.promotionType}"/>
	 	<beacon:client-state-entry name="startDate" value="${transactionHistoryForm.startDate}"/>
	 	<beacon:client-state-entry name="endDate" value="${transactionHistoryForm.endDate}"/>
	</beacon:client-state>

	<table cellpadding="3" cellspacing="1" >
		<tr>
			<td valign="top">         
				<span class="headline"><cms:contentText key="TITLE" code="participant.auditmessages"/></span>
				<%-- Subheadline --%>
				<br/>
				<span class="subheadline">
					<c:out value="${participant.lastName}" />,&nbsp;
					<c:out value="${participant.firstName}" />&nbsp;
					<c:if test="${participant.middleName != null}">
						<c:out value="${participant.middleName}" />
					</c:if>
				</span>
				<%-- End Subheadline --%>
				<br/>
				<span class="subheadline">
					<cms:contentText key="CLAIM_NUMBER" code="participant.auditmessages"/>
					&nbsp;<c:out value="${claim.claimNumber}"/>	
				</span>
				<br/>
				<span class="subheadline">
					<cms:contentText key="SUBMITTED" code="participant.auditmessages"/>&nbsp;
					<fmt:formatDate value="${claim.submissionDate}" pattern="${JstlDatePattern}" />
				</span>
				<%--INSTRUCTIONS--%>
				<br/><br/>
				<span class="content-instruction">
					<cms:contentText key="INSTRUCTIONS" code="participant.auditmessages"/>
				</span>
				<br/><br/>
				<%--END INSTRUCTIONS--%>
		     	
				<cms:errors/>
			</td>
		</tr>
		<tr>
			<td>
				<display:table defaultsort="1" defaultorder="ascending" name="failureMessages" id="audit" style="width: 100%">
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					<display:setProperty name="basic.msg.empty_list">
						<tr class="crud-content" align="left"><td colspan="{0}">
              <cms:contentText key="NOTHING_TO_DISPLAY" code="participant.auditmessages"/>
            </td></tr>
					</display:setProperty>
					<display:column titleKey="participant.auditmessages.FAILURE_MESSAGES" property="reasonText" headerClass="crud-table-header-row" class="crud-content-bold left-align"/>
				</display:table>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<display:table defaultsort="1" defaultorder="ascending" name="successMessages" id="audit" style="width: 100%">
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					<display:setProperty name="basic.msg.empty_list">
						<tr class="crud-content" align="left"><td colspan="{0}">
              <cms:contentText key="NOTHING_TO_DISPLAY" code="participant.auditmessages"/>
            </td></tr>
					</display:setProperty>
					<display:column titleKey="participant.auditmessages.SUCCESS_MESSAGES" property="reasonText" headerClass="crud-table-header-row" class="crud-content-bold left-align"/>
				</display:table>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
	 	<tr>
	 		<td align="center">
				<%  Map paramMap = new HashMap();
						//These could be individual request parameters or part of the current clientState parameter						
						Map clientStateMap = ClientStateUtils.getClientStateMap(request);
						paramMap.put( "userId", ClientStateUtils.getParameterValue(request, clientStateMap, "userId") );
						paramMap.put( "promotionId", ClientStateUtils.getParameterValue(request, clientStateMap, "promotionId") );
						paramMap.put( "promotionType", ClientStateUtils.getParameterValue(request, clientStateMap, "promotionType") );
						paramMap.put( "startDate", ClientStateUtils.getParameterValue(request, clientStateMap, "startDate") );
						paramMap.put( "endDate", ClientStateUtils.getParameterValue(request, clientStateMap, "endDate") );
						paramMap.put( "open", ClientStateUtils.getParameterValue(request, clientStateMap, "open") );
						paramMap.put( "mode", ClientStateUtils.getParameterValue(request, clientStateMap, "mode") );
						paramMap.put( "livePromotionId", ClientStateUtils.getParameterValue(request, clientStateMap, "livePromotionId") );
						paramMap.put( "livePromotionType", ClientStateUtils.getParameterValue(request, clientStateMap, "livePromotionType") );
						paramMap.put( "liveStartDate", ClientStateUtils.getParameterValue(request, clientStateMap, "liveStartDate") );
						paramMap.put( "liveEndDate", ClientStateUtils.getParameterValue(request, clientStateMap, "liveEndDate") );
						pageContext.setAttribute("returnToHistoryTransactionsUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistory.do?open="+request.getParameter("open"), paramMap ) );
				%>
				<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('${returnToHistoryTransactionsUrl}','showActivity')">
					<cms:contentText key="BACK_TO_TRANSACTION_LIST" code="participant.auditmessages"/>
				</html:submit>
	 		</td>
	 	</tr>
	</table>
</html:form>