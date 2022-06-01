<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.ChallengePointPromotion" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" >
	function disableAndSubmitForm(theform)
	{
		if (document.all || document.getElementById) 
		{
			for (i = 0; i < theform.length; i++)
			 {
				var tempobj = theform.elements[i];
				if (tempobj.type.toLowerCase() == "submit" ||
					tempobj.type.toLowerCase() == "button" || tempobj.type.toLowerCase() == "reset")
				tempobj.disabled = true;
			 }
			theform.submit();
		}
	}
</script>
<html:form styleId="contentForm" action="/challengepointProcessConfirmation">

<% ChallengePointPromotion promotion = (ChallengePointPromotion)request.getAttribute("promotion"); %>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
		<span class="headline"><cms:contentText key="TITLE" code="promotion.challengepoint.summary"/></span>
		<br/>
     	<span class="content-instruction">
			<b><c:out value="${promotion.upperCaseName}"/></b>
     	</span>		
		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="promotion.challengepoint.summary"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
     	
		<%--  START HTML needed with display table --%>
		<%-- Table --%>
		<table width="50%">
    	  <tr>
	    	<td align="right">
			  <display:table name="participantChallengepointAwardSummaryList" id="participantChallengepointAwardSummary" >
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>			
			  <COLGROUP span="4" width="25"></COLGROUP>
				<display:column titleKey="promotion.challengepoint.summary.PARTICIPANT_LEVELS" headerClass="crud-table-header-row" class="crud-content left-align" >
					<c:if test="${participantChallengepointAwardSummary.participantTotals == 'false'}">
						<cms:contentText key="${ participantChallengepointAwardSummary.goalLevel.goalLevelNameKey }" code="${participantChallengepointAwardSummary.goalLevel.goalLevelcmAssetCode }"/>
					</c:if>	
					<c:if test="${participantChallengepointAwardSummary.participantTotals == 'true'}">
						<b><cms:contentText key="PARTICIPANT_TOTALS" code="promotion.challengepoint.summary"/></b>
					</c:if>						
		        </display:column>	
		        <display:column titleKey="promotion.challengepoint.summary.TOTAL_SELECTED" headerClass="crud-table-header-row" class="crud-content right-align" >
					<fmt:formatNumber  value="${participantChallengepointAwardSummary.totalSelected}"/>
		        </display:column>
		        <display:column titleKey="promotion.challengepoint.summary.BASIC_AWARD_PENDING" headerClass="crud-table-header-row" class="crud-content right-align" >
					<fmt:formatNumber  value="${participantChallengepointAwardSummary.basicPending}"/>
		        </display:column>
		        <display:column titleKey="promotion.challengepoint.summary.BASIC_AWARD_DEPOSITED" headerClass="crud-table-header-row" class="crud-content right-align" >
					<fmt:formatNumber  value="${participantChallengepointAwardSummary.basicDeposited}"/>
		        </display:column>
		        <display:column titleKey="promotion.challengepoint.summary.BASIC_AWARD_EARNED" headerClass="crud-table-header-row" class="crud-content right-align" >
					<fmt:formatNumber  value="${participantChallengepointAwardSummary.basicEarned}"/>
		        </display:column>
		       
		        <c:if test="${promotion.afterFinalProcessDate == 'true' }">
				<display:column titleKey="promotion.challengepoint.summary.CHALLENGEPOINT_ACHIEVERS" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${participantChallengepointAwardSummary.totalAchieved}"/>
		        </display:column>
				<display:column titleKey="promotion.challengepoint.summary.CHALLENGEPOINT_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${participantChallengepointAwardSummary.totalAchievedAward}"/>
		        </display:column>
		        </c:if>
		       
		         <c:if test="${promotion.challengePointAwardType.points }">
				<display:column titleKey="promotion.challengepoint.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${participantChallengepointAwardSummary.totalAward}"/>
		        </display:column>	
		         </c:if>		        	
		      </display:table>		    
		    </td>
          </tr>
          <tr><td><p>&nbsp;</p></td></tr>
          
          <c:if test="${partnerSummaryAvailable == 'true'}">
    	  <tr>
	    	<td align="right">
			  <display:table name="partnerCPAwardSummaryList" id="partnerCPAwardSummary" >
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			    <COLGROUP span="4" width="25"></COLGROUP>
				<display:column titleKey="promotion.goalquest.summary.PARTNER_LEVELS" headerClass="crud-table-header-row" class="crud-content left-align" >
					<c:if test="${partnerCPAwardSummary.partnerTotals == 'false'}">
						<c:out value="${partnerCPAwardSummary.goalLevel.goalLevelName}"/>
					</c:if>
					<c:if test="${partnerCPAwardSummary.partnerTotals == 'true'}">
						<b><cms:contentText key="PARTNER_TOTALS" code="promotion.goalquest.summary"/></b>
					</c:if>
		        </display:column>
		        <display:column titleKey="promotion.goalquest.summary.TOTAL_SELECTED" headerClass="crud-table-header-row" class="crud-content right-align" >
					<fmt:formatNumber  value="${partnerCPAwardSummary.totalSelected}"/>
		        </display:column>
				<display:column titleKey="promotion.goalquest.summary.TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${partnerCPAwardSummary.totalAchieved}"/>
		        </display:column>
				<display:column titleKey="promotion.goalquest.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${partnerCPAwardSummary.totalAward}"/>
		        </display:column>
		      </display:table>
		    </td>
          </tr>
          </c:if>
          <tr><td><p>&nbsp;</p></td></tr>
          <c:if test="${ promotion.overrideStructure.code != 'stackrank' }">
	    	  <tr>
		    	<td align="right">
				  <display:table name="managerChallengepointAwardSummaryList" id="managerChallengepointAwardSummary" >
				  <display:setProperty name="basic.msg.empty_list_row">
						       			<tr class="crud-content" align="left"><td colspan="{0}">
	                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
	                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				  <COLGROUP span="4" width="25"></COLGROUP>
					<display:column titleKey="promotion.challengepoint.summary.MANAGER_OVERRIDE" headerClass="crud-table-header-row" class="crud-content left-align" >
						<c:choose>
							<c:when test="${managerChallengepointAwardSummary.managerTotals == 'true' && managerChallengepointAwardSummary.leveloneAward == 'true' }">
							  <cms:contentText key="MANAGER_OVERRIDE_LEVEL_ONE" code="promotion.goalquest.summary"/>
							</c:when>
							<c:when test="${managerChallengepointAwardSummary.managerTotals == 'true' && managerChallengepointAwardSummary.leveloneAward == 'false' }">
							  <cms:contentText key="MANAGER_OVERRIDE_LEVEL_TWO" code="promotion.goalquest.summary"/>
							</c:when>
							<c:otherwise>
							  <b><cms:contentText key="MANAGER_OVERRIDE_TOTALS" code="promotion.goalquest.summary"/></b>
							</c:otherwise>
						</c:choose>	
			        </display:column>
					<display:column titleKey="promotion.challengepoint.summary.TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
			              <fmt:formatNumber  value="${managerChallengepointAwardSummary.totalAchieved}"/>
			        </display:column>
					<display:column titleKey="promotion.challengepoint.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
			               <fmt:formatNumber  value="${managerChallengepointAwardSummary.totalAward}"/>
			        </display:column>			        	
			      </display:table>		    
			    </td>
	          </tr>
          </c:if>
          <c:if test="${ promotion.overrideStructure.code == 'stackrank' }">
	          <tr>
		    	<td align="right">
				  <display:table name="managerChallengepointAwardSummaryList" id="managerChallengepointAwardSummary" >
				  <display:setProperty name="basic.msg.empty_list_row">
						       			<tr class="crud-content" align="left"><td colspan="{0}">
	                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
	                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				  <COLGROUP span="4" width="25"></COLGROUP>
					<display:column titleKey="promotion.goalquest.summary.START_RANK" headerClass="crud-table-header-row" class="crud-content right-align" >
						<c:choose>
							<c:when test="${managerChallengepointAwardSummary.startRank ne null }">
								<b><c:out value="${ managerChallengepointAwardSummary.startRank }"/></b>
							</c:when>
							<c:otherwise>
								<b><cms:contentText key="MANAGER_OVERRIDE_TOTALS" code="promotion.goalquest.summary"/></b>
							</c:otherwise>
						</c:choose>
			        </display:column>
			        <display:column titleKey="promotion.goalquest.summary.END_RANK" headerClass="crud-table-header-row" class="crud-content right-align">
			        	<c:if test="${managerChallengepointAwardSummary.endRank ne null }">
							<b><c:out value="${ managerChallengepointAwardSummary.endRank }"/></b>
						</c:if>
			        </display:column>	
					<display:column titleKey="promotion.challengepoint.summary.TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
			              <fmt:formatNumber  value="${managerChallengepointAwardSummary.totalAchieved}"/>
			        </display:column>
					<display:column titleKey="promotion.challengepoint.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
			               <fmt:formatNumber  value="${managerChallengepointAwardSummary.totalAward}"/>
			        </display:column>			        	
			      </display:table>		    
			    </td>
	          </tr>
          </c:if>
          
          <tr><td><p>&nbsp;</p></td></tr>
    	  <tr>
	    	<td align="right">
			  <display:table name="grandTotalChallengepointAwardSummary" id="grandTotalChallengepointAwardSummary" >
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			  <COLGROUP span="4" width="25"></COLGROUP>
				<display:column  headerClass="crud-table-header-row" class="crud-content right-align" >
					<b><cms:contentText key="GRAND_TOTAL" code="promotion.challengepoint.summary"/></b>
		        </display:column>	
				<display:column titleKey="promotion.challengepoint.summary.GT_TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${grandTotalChallengepointAwardSummary.totalAchieved}"/>
		        </display:column>
				<display:column titleKey="promotion.challengepoint.summary.GT_TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${grandTotalChallengepointAwardSummary.totalAward}"/>
		        </display:column>			        	
		      </display:table>		    
		    </td>
          </tr>
        </table>
	</td>
	</tr>        
	</table>		
	    	<%-- Table --%>
		<%--  END HTML needed with display table --%>
		<table border="0" cellpadding="10" cellspacing="0" width="50%">		
			<tr align="center">
  	  			<td>
					<c:if test="${promotion.expired == 'false' and promotion.issueAwardsRun == 'false' and promotion.goalCollectionPeriodEnded == 'true' and promotion.beforeFinalProcessDate == 'true'}">  	  			
					<c:if test="${grandTotalChallengepointAwardSummary.basicPendingGTZero == 'true' }">
           			<html:button property="approveBtn" styleClass="content-buttonstyle" onclick="disableAndSubmitForm(this.form)">
						<cms:contentText key="APPROVE_BASIC_AWARD" code="promotion.challengepoint.summary"/>
					</html:button>
					</c:if>	
					</c:if>	
					<c:if test="${promotion.expired == 'false' and promotion.issueAwardsRun == 'false' and promotion.goalCollectionPeriodEnded == 'true' and promotion.afterFinalProcessDate == 'true'}">  	  			
           			<html:button property="approveBtn" styleClass="content-buttonstyle" onclick="disableAndSubmitForm(this.form)">
						<cms:contentText key="APPROVE_FINAL_AWARD" code="promotion.challengepoint.summary"/>
					</html:button>	
					</c:if>	
           	  	</td>
           	  	<td>
       				<html:button property="extractBtn" styleClass="content-buttonstyle" onclick="callUrl('challengepointExtractDetails.do')">
						<cms:contentText key="EXTRACT_RESULTS" code="promotion.challengepoint.summary" />
					</html:button>		
           	  	</td>
           	  	<td>
       				<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="callUrl('awardListDisplay.do')">
						<cms:contentText code="system.button" key="CANCEL" />
					</html:button>		
           	  	</td>           	  	
    		</tr>
		</table>    		
</html:form>
