<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.GoalQuestPromotion" %>
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
			callUrl('goalQuestProcessConfirmation.do');
		}
	}
</script>
<html:form styleId="contentForm" action="/goalQuestProcessConfirmation">

<% GoalQuestPromotion promotion = (GoalQuestPromotion)request.getAttribute("promotion"); %>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
		<span class="headline"><cms:contentText key="TITLE" code="promotion.goalquest.summary"/></span>
		<br/>
     	<span class="content-instruction">
			<b><c:out value="${promotion.upperCaseName}"/></b>
     	</span>		
		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="promotion.goalquest.summary"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
     	
		<%--  START HTML needed with display table --%>
		<%-- Table --%>
		<table width="50%">
    	  <tr>
	    	<td align="right">
			  <display:table name="participantGoalQuestAwardSummaryList" id="participantGoalQuestAwardSummary" >
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			  <COLGROUP span="4" width="25"></COLGROUP>
				<display:column titleKey="promotion.goalquest.summary.PARTICIPANT_LEVELS" headerClass="crud-table-header-row" class="crud-content left-align" >
					<c:if test="${participantGoalQuestAwardSummary.participantTotals == 'false'}">
						<c:out value="${participantGoalQuestAwardSummary.goalLevel.goalLevelName}"/>
					</c:if>	
					<c:if test="${participantGoalQuestAwardSummary.participantTotals == 'true'}">
						<b><cms:contentText key="PARTICIPANT_TOTALS" code="promotion.goalquest.summary"/></b>
					</c:if>						
		        </display:column>	
		        <display:column titleKey="promotion.goalquest.summary.TOTAL_SELECTED" headerClass="crud-table-header-row" class="crud-content right-align" >
					<fmt:formatNumber  value="${participantGoalQuestAwardSummary.totalSelected}"/>
		        </display:column>
				<display:column titleKey="promotion.goalquest.summary.TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${participantGoalQuestAwardSummary.totalAchieved}"/>
		        </display:column>
				<display:column titleKey="promotion.goalquest.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${participantGoalQuestAwardSummary.totalAward}"/>
		        </display:column>			        	
		      </display:table>		    
		    </td>
          </tr>
          
          <c:if test="${partnerSummaryAvailable == 'true'}">
    	  <tr>
	    	<td align="right">
			  <display:table name="partnerGoalQuestAwardSummaryList" id="partnerGoalQuestAwardSummary" >
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			  <COLGROUP span="4" width="25"></COLGROUP>
				<display:column titleKey="promotion.goalquest.summary.PARTNER_LEVELS" headerClass="crud-table-header-row" class="crud-content left-align" >
					<c:if test="${partnerGoalQuestAwardSummary.partnerTotals == 'false'}">
						<c:out value="${partnerGoalQuestAwardSummary.goalLevel.goalLevelName}"/>
					</c:if>
					<c:if test="${partnerGoalQuestAwardSummary.partnerTotals == 'true'}">
						<b><cms:contentText key="PARTNER_TOTALS" code="promotion.goalquest.summary"/></b>
					</c:if>
		        </display:column>
		        <display:column titleKey="promotion.goalquest.summary.TOTAL_SELECTED" headerClass="crud-table-header-row" class="crud-content right-align" >
					<fmt:formatNumber  value="${partnerGoalQuestAwardSummary.totalSelected}"/>
		        </display:column>
				<display:column titleKey="promotion.goalquest.summary.TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${partnerGoalQuestAwardSummary.totalAchieved}"/>
		        </display:column>
				<display:column titleKey="promotion.goalquest.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
		            <fmt:formatNumber  value="${partnerGoalQuestAwardSummary.totalAward}"/>
		        </display:column>
		      </display:table>
		    </td>
          </tr>
          </c:if>
          
          <c:if test="${ promotion.overrideStructure.code != 'stackrank' }">
            <tr>
		    	<td align="right">
				  <display:table name="managerGoalQuestAwardSummaryList" id="managerGoalQuestAwardSummary" >
				  <display:setProperty name="basic.msg.empty_list_row">
						       			<tr class="crud-content" align="left"><td colspan="{0}">
	                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
	                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="false"></display:setProperty>
				  <COLGROUP span="3" width="25"></COLGROUP>
					<display:column titleKey="promotion.goalquest.summary.MANAGER_OVERRIDE_LEVELS" headerClass="crud-table-header-row" class="crud-content left-align" >
						<c:choose>
							<c:when test="${managerGoalQuestAwardSummary.managerTotals == 'true' && managerGoalQuestAwardSummary.leveloneAward == 'true' }">
								<cms:contentText key="MANAGER_OVERRIDE_LEVEL_ONE" code="promotion.goalquest.summary"/>
							</c:when>
							<c:when test="${managerGoalQuestAwardSummary.managerTotals == 'true' && managerGoalQuestAwardSummary.leveloneAward == 'false' }">
								<cms:contentText key="MANAGER_OVERRIDE_LEVEL_TWO" code="promotion.goalquest.summary"/>
							</c:when>
							<c:otherwise>
								<b><cms:contentText key="MANAGER_OVERRIDE_TOTALS" code="promotion.goalquest.summary"/></b>
							</c:otherwise>
						</c:choose>
			        </display:column>	
			        
					<display:column titleKey="promotion.goalquest.summary.TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
						<c:if test="${ managerGoalQuestAwardSummary.totalAchieved != 0 }">
				            <fmt:formatNumber  value="${managerGoalQuestAwardSummary.totalAchieved}"/>
				        </c:if>
				    </display:column>
			       
					<display:column titleKey="promotion.goalquest.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
						<c:if test="${ managerGoalQuestAwardSummary.totalAward != 0 }">
				            <fmt:formatNumber  value="${managerGoalQuestAwardSummary.totalAward}"/>
				        </c:if>	
				    </display:column>	
			      </display:table>		    
			    </td>
            </tr>
          </c:if>
          
          <c:if test="${ promotion.overrideStructure.code == 'stackrank' }">
            <tr>
		    	<td align="right">
				  <display:table name="managerGoalQuestAwardSummaryList" id="managerGoalQuestAwardSummary" >
				  <display:setProperty name="basic.msg.empty_list_row">
						       			<tr class="crud-content" align="left"><td colspan="{0}">
	                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
	                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="false"></display:setProperty>
				  <COLGROUP span="3" width="25"></COLGROUP>
					<display:column titleKey="promotion.goalquest.summary.START_RANK" headerClass="crud-table-header-row" class="crud-content right-align" >
						<c:choose>
							<c:when test="${managerGoalQuestAwardSummary.startRank ne null }">
								<b><c:out value="${ managerGoalQuestAwardSummary.startRank }"/></b>
							</c:when>
							<c:otherwise>
								<b><cms:contentText key="MANAGER_OVERRIDE_TOTALS" code="promotion.goalquest.summary"/></b>
							</c:otherwise>
						</c:choose>
			        </display:column>	
			        <display:column titleKey="promotion.goalquest.summary.END_RANK" headerClass="crud-table-header-row" class="crud-content right-align">
			        	<c:if test="${managerGoalQuestAwardSummary.endRank ne null }">
							<b><c:out value="${ managerGoalQuestAwardSummary.endRank }"/></b>
						</c:if>
			        </display:column>
			        	
					<display:column titleKey="promotion.goalquest.summary.TOTAL_ACHIEVED" headerClass="crud-table-header-row" class="crud-content right-align">
						<c:if test="${ managerGoalQuestAwardSummary.totalAchieved != null }">
				            <fmt:formatNumber  value="${managerGoalQuestAwardSummary.totalAchieved}"/>
				        </c:if>
				    </display:column>
			       
					<display:column titleKey="promotion.goalquest.summary.TOTAL_AWARD" headerClass="crud-table-header-row" class="crud-content right-align">
						<c:if test="${ managerGoalQuestAwardSummary.totalAward != null }">
				            <fmt:formatNumber  value="${managerGoalQuestAwardSummary.totalAward}"/>
				        </c:if>	
				    </display:column>	
			      </display:table>		    
			    </td>
            </tr>
          </c:if>

    	  <tr>
	    	<td align="right">
			  <display:table name="grandTotalGoalQuestAwardSummary" id="grandTotalGoalQuestAwardSummary" >
			  <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			  <COLGROUP span="3" width="25"></COLGROUP>
				<display:column class="crud-content right-align" >
					<b><cms:contentText key="GRAND_TOTAL" code="promotion.goalquest.summary"/></b>
		        </display:column>	
				<display:column class="crud-content right-align">
		            <fmt:formatNumber  value="${grandTotalGoalQuestAwardSummary.totalAchieved}"/>
		        </display:column>
				<display:column class="crud-content right-align">
		            <fmt:formatNumber  value="${grandTotalGoalQuestAwardSummary.totalAward}"/>
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
					<c:if test="${promotion.expired == 'false' and promotion.afterFinalProcessingValid == 'true' and !promotion.issueAwardsRun}">  	  			
           			<html:button property="approveBtn" styleClass="content-buttonstyle" onclick="disableAndSubmitForm(this.form)">
						<cms:contentText key="APPROVE_AND_ISSUE_AWARDS" code="promotion.goalquest.summary"/>
					</html:button>	
					</c:if>	
           	  	</td>
           	  	<td>
       				<html:button property="extractBtn" styleClass="content-buttonstyle" onclick="callUrl('goalQuestExtractDetails.do')">
						<cms:contentText key="EXTRACT_DETAILS" code="promotion.goalquest.summary" />
					</html:button>		
           	  	</td>
           	  	<td>
       				<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="callUrl('goalQuestListDisplay.do')">
						<cms:contentText code="system.button" key="CANCEL" />
					</html:button>		
           	  	</td>           	  	
    		</tr>
		</table>    		
</html:form>
