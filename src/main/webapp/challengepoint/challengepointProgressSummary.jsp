<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf" %>
<c:set var="prec" value="${cpPaxBean.promotion.achievementPrecision.precision }" />
<script type="text/javascript">
window.onload=init;
function init()
{
hide('awardDetail','true');
}
  function hide(hiddenId, flag)
  {
    block = document.getElementById(hiddenId).style;
    if (flag == 'false')
        block.display = "block";
    else
        block.display = "none";
  }
</script>


<tiles:insert attribute="challengepoint.header" />
<div id="content-cp">
<div id="content-select">
<div class="clear"></div>
<strong>
&nbsp;&nbsp;&nbsp;<cms:contentText key="PROGRESS_HEADER" code="promotion.challengepoint.progress.summary"/>
&nbsp;<fmt:formatDate value='${cpPaxBean.progressSubmissionDate}' pattern='${JstlDateTimeTZPattern}'/>
</strong>

	<table class="crud-table" width="75%">
			<tr class="crud-table-row1">
			<td class="crud-content left-align" width="50%"><cms:contentText key="EARNINGS_BASIC_PROGRAM" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><c:out value="${cpPaxBean.beforeUnitLabel}" />
			<fmt:formatNumber value="${cpPaxBean.calculatedThreshold}" />
			<c:out value="${cpPaxBean.afterUnitLabel}" /></td>
			
			</tr>
			<tr class="crud-table-row2">
			<td class="crud-content left-align" width="50%">	<cms:contentTemplateText code="promotion.challengepoint.progress.summary"
	   key="AWARD_INCREMENT" 
	   args="${cpPaxBean.promotion.awardPerIncrement}"
	   delimiter=","/></td>
			<td class="crud-content left-align" width="25%"><c:out value="${cpPaxBean.beforeUnitLabel}" />
			<fmt:formatNumber value="${cpPaxBean.calculatedIncrementAmount}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
			<c:out value="${cpPaxBean.afterUnitLabel}" /></td>
		
			</tr>
			<tr class="crud-table-row1">
			<td class="crud-content left-align" width="50%"><cms:contentText key="ACTUAL_RESULTS" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><c:out value="${cpPaxBean.beforeUnitLabel}" />
			<fmt:formatNumber value="${cpPaxBean.paxLevel.currentValue}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/>
			<c:out value="${cpPaxBean.afterUnitLabel}" /></td>
			
			</tr>
			<tr class="crud-table-row2">
			<td class="crud-content left-align" width="50%"><cms:contentTemplateText code="promotion.challengepoint.progress.summary"
	   key="SELECTED_LEVEL" 
	   args="${cpPaxBean.challengepointLevel.levelName}"
	   delimiter=","/></td>
			<td class="crud-content left-align" width="25%"><c:out value="${cpPaxBean.beforeUnitLabel}" />
			<fmt:formatNumber value="${cpPaxBean.amountToAchieve}" />
			<c:out value="${cpPaxBean.afterUnitLabel}" /></td>
			
			</tr>
			<tr class="crud-table-row1">
			<td class="crud-content left-align" width="50%"><cms:contentText key="PROGRESS_PERCENT" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><c:out value="${cpPaxBean.percentAchieved}" />%</td>
			
			</tr>					
								
	</table><br/><br/><br/>
<div class="clear"></div>
<strong>
&nbsp;&nbsp;&nbsp;<cms:contentText key="AWARD_HEADER" code="promotion.challengepoint.progress.summary"/>
&nbsp;<fmt:formatDate value='${cpPaxBean.progressSubmissionDate}' pattern='${JstlDatePattern}'/>	
</strong>
  
	      <table class="crud-table" width="75%">
			<tr class="crud-table-row1">
			<td class="crud-content left-align" width="50%"><cms:contentText key="BASIC_AWARD_EARNED" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><fmt:formatNumber value="${cpPaxBean.totalBasicAwardEarned}" /></td>
			</tr>
			
			<c:if test="${cpPaxBean.promotion.challengePointAwardType.points}">
			<c:if test="${cpPaxBean.promotion.issueAwardsRun}">
			
			<tr class="crud-table-row2">
			<td class="crud-content left-align" width="50%"><cms:contentText key="CHALLENGEPOINT_AWARD_EARNED" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><fmt:formatNumber value="${cpPaxBean.challengepointAwardEarned}" /></td>
			</tr>
			
			<tr class="crud-table-row1">
			<td class="crud-content left-align" width="50%"><cms:contentText key="TOTAL_AWARD_EARNED" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><fmt:formatNumber value="${cpPaxBean.totalAwardEarned}" /></td>
			</tr>
			</c:if>
			</c:if>	
			
			<tr class="crud-table-row2">
			<td class="crud-content left-align" width="50%"><cms:contentText key="DEPOSITED" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><fmt:formatNumber value="${cpPaxBean.totalAwardDeposited}" /></td>
			</tr>
			<tr class="crud-table-row1">
			<td class="crud-content left-align" width="50%"><cms:contentText key="NOT_DEPOSITED" code="promotion.challengepoint.progress.summary"/></td>
			<td class="crud-content left-align" width="25%"><fmt:formatNumber value="${cpPaxBean.totalAwardEarnedNotDeposited}" /></td>
			</tr>					
				
					
	</table><br/><br/><br/>
<div class="clear"></div>
<a href="javascript:hide('awardDetail','false');" class="content-buttonstyle">
<cms:contentText key="DEPOSIT_AND_DATE" code="promotion.challengepoint.progress.summary"/>
</a>

<div id="awardDetail">
<c:if test="${!empty cpPaxBean.awards}">
<strong>
<cms:contentText key="AWARD_DETAIL_HEADER" code="promotion.challengepoint.progress.summary"/>
</strong><br/>
	<table class="crud-table" width="75%">
			<c:forEach var='award' items='${cpPaxBean.awards}'>
			<c:if test="${award.awardType != 'challengepoint' or !award.challengePointPromotion.challengePointAwardType.merchTravel }">
			<tr class="crud-table-row1">
			<td class="crud-content left-align" width="25%"><fmt:formatDate value="${award.auditCreateInfo.dateCreated}" pattern="${JstlDatePattern}" /></td>
		    <c:choose>
			<c:when test="${award.awardType == 'basic' and award.awardIssued == 0 }">
				<td class="crud-content left-align" width="25%"><%=com.biperf.core.domain.journal.Journal.CHALLENGEPOINT_BASIC_AWARD %></td>
			</c:when>
			<c:otherwise>
			<td class="crud-content left-align" width="25%"><c:out value="${award.journal.journalType}" /></td>
			</c:otherwise>
			</c:choose>
			<td class="crud-content left-align" width="25%"><fmt:formatNumber value="${award.awardIssued}" /></td>
			</tr>
			</c:if>
						
		</c:forEach>					
	</table>
</c:if>
<c:if test="${empty cpPaxBean.awards}">
<cms:contentText key="NO_AWARDS" code="promotion.challengepoint.progress.summary"/>
</c:if>
<a href="javascript:hide('awardDetail','true');" class="content-buttonstyle">
<cms:contentText key="HIDE_EARNINGS" code="promotion.challengepoint.progress.summary"/>
</a>	
</div>	

</div> <%-- End content select --%>
	<br />
<div class="clear"></div>
	<div id="content-select-buttons-2">
		<a href="javascript:window.print()" class="content-buttonstyle"><span><cms:contentText key="PRINT" code="system.button"/></span></a>    
		<a href="<%=RequestUtils.getBaseURI(request)%>/homePage.do" onclick='setDispatchAndSubmit("confirmChallengePoint");return false;'; class="content-buttonstyle"><span><cms:contentText key="BACK_TO_HOME" code="system.button"/></span></a>  
		<div class="clear"></div> <%-- end clear --%>
	</div>
	
	
	</div> <%-- end content --%>