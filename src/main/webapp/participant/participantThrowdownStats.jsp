<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">

function getPlayerStats()
{
  setActionDispatchAndSubmit('throwdownParticipantStatistic.do','display')
}

</script>


<html:form styleId="contentForm" action="throwdownParticipantStatistic">
	<html:hidden property="method" value="display" />
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${participantThrowdownStatsForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="participant.throwdownstats" /></span>

		<br/>
        <beacon:username userId="${displayNameUserId}"/>
        <%--INSTRUCTIONS--%>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="participant.throwdownstats"/>
        </span>
        <br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>
	
		<cms:contentText key="PROMO_NAME" code="participant.promotions"/> : 
 		<html:select styleId="promotionId" property="promotionId" styleClass="content-field" onchange="getPlayerStats()">
    		<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
    		<c:forEach items="${participantThrowdownStatsForm.promotions}" var="promo">
        		<html:option value="${promo.promotion.id}">${promo.promotion.name}</html:option>
        	</c:forEach>
		</html:select>	
		<br/>
		<c:if test="${not empty participantThrowdownStatsForm.playerStats.matches}">
		<table>
			<tr>
				<td>
					<cms:contentText key="RANK" code="participant.throwdownstats"/>
				</td>
				<td>
	                <c:if test="${participantThrowdownStatsForm.playerStats.self.displayRank != ''}"> 
	                ${participantThrowdownStatsForm.playerStats.self.displayRank}
	                </c:if>
	                <c:if test="${participantThrowdownStatsForm.playerStats.self.displayRank == ''}"> 
	                <i class="icon-ban-circle"></i>
	                </c:if> 
				</td>
			</tr>
			<tr>
				<td>
					<cms:contentText key="BADGES" code="participant.throwdownstats"/>
				</td>
				<td>
					<table>
					<tr>
					<c:forEach items="${participantThrowdownStatsForm.playerStats.self.badges}" var="badges">
						<c:forEach items="${badges.badgeDetails}" var="detail">
						<td>
							<img src="${detail.img}"/>
							<span class="td-gold-badge">${detail.badgeName}</span>
							<span class="td-badge-earned-date">${detail.dateEarned}</span>
						</td>							
						</c:forEach>
					</c:forEach>
					</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<cms:contentText key="RECORD" code="participant.throwdownstats"/>
				</td>
				<td>
					<cms:contentText code="participant.throwdownstats" key="WIN_SHORT_FORM"/>${participantThrowdownStatsForm.playerStats.self.stats.wins} <cms:contentText code="participant.throwdownstats" key="LOSS_SHORT_FORM"/>${participantThrowdownStatsForm.playerStats.self.stats.losses} <cms:contentText code="participant.throwdownstats" key="TIE_SHORT_FORM"/>${participantThrowdownStatsForm.playerStats.self.stats.ties}
				</td>
			</tr>						
		</table>
		</c:if>

	</td>
	</tr>
  
  
             <c:if test="${not empty participantThrowdownStatsForm.playerStats.matches}">
             <table><tr><td><span class="headline"><cms:contentText code="participant.throwdownstats" key="SCHEDULE"/></span></td><tr></table><br />
		    <table class="td-matches-schedule table table-striped">
			<thead>
				<tr class="crud-table-row1">

					<th class="crud-table-header-row"><cms:contentText code="participant.throwdownstats" key="ROUND"/></th>
					
					<th class="crud-table-header-row"><cms:contentText code="participant.throwdownstats" key="ROUND_DATES"/></th>

					<th class="crud-table-header-row"><cms:contentText key="PLAYER" code="participant.throwdownstats" /></th>

					<th class="crud-table-header-row"><cms:contentText key="RECORD" code="participant.throwdownstats" /></th>

					<th class="crud-table-header-row"><cms:contentText key="SCORE" code="participant.throwdownstats" /></th>

				</tr>
			</thead>
			<tr>
				<td></td>
			</tr>
			<tbody>
			 <c:forEach items="${participantThrowdownStatsForm.playerStats.matches}" var="match">
			      <tr class="crud-table-row2">
			         <td  class="crud-content left-align top-align nowrap" width="10%">
					   ${match.round.roundNumber}
					 </td>
					  <td  class="crud-content left-align top-align nowrap" width="20%">
					   <fmt:formatDate value="${match.round.startDate}" pattern="${JstlDatePattern}" />
					   <cms:contentText code="participant.throwdown.promo.detail" key="HYPHEN" /><br/>
					   <fmt:formatDate value="${match.round.endDate}" pattern="${JstlDatePattern}" />
					  </td>
					  <td  class="crud-content left-align top-align nowrap" width="32%">
					  <img class="participant-list-avatar avatar" src="${match.primaryTeam.avatarUrl}" style="width: 30px;height: 30px"/>
							  <c:if test="${!match.primaryTeam.team.shadowPlayer}">
							    ${match.primaryTeam.team.participant.lastName},${match.primaryTeam.team.participant.firstName}
							  </c:if>
							  <c:if test="${match.primaryTeam.team.shadowPlayer}">
							    <cms:contentText code="participant.throwdownstats" key="SHADOW_PLAYER"/>
							  </c:if>	
							  <br/>
							  <br/>	
							  <c:if test="${match.opponentDecided}">			  
							  <img class="participant-list-avatar avatar" src="${match.secondaryTeam.avatarUrl}" style="width: 30px;height: 30px" />
							  <c:if test="${!match.secondaryTeam.team.shadowPlayer}">
							    ${match.secondaryTeam.team.participant.lastName},${match.secondaryTeam.team.participant.firstName}
							  </c:if>
							  <c:if test="${match.secondaryTeam.team.shadowPlayer}">
							    <cms:contentText code="participant.throwdownstats" key="SHADOW_PLAYER"/>
							  </c:if>
							  </c:if>
							  <c:if test="${match.roundYetToStart && !match.opponentDecided}">
							  	<img class="participant-list-avatar avatar" src="${match.avatarUrlForTBDPlayer}" style="width: 30px;height: 30px" />
							  	<cms:contentText code="participant.throwdownstats" key="TBD_PLAYER"/>
							  </c:if>	
							  <c:if test="${!match.roundYetToStart && !match.opponentDecided}">
							  	<img class="participant-list-avatar avatar" src="${match.avatarUrlForTBDPlayer}" style="width: 30px;height: 30px" />
							  	<cms:contentText code="participant.throwdownstats" key="NONE_PLAYER"/>
							  </c:if>			
					  </td>
					  <td  class="crud-content left-align top-align nowrap" width="30%">
					    <c:if test="${!match.roundYetToStart}">
	                        <p><cms:contentText code="participant.throwdownstats" key="WIN_SHORT_FORM"/>:<span>${match.primaryTeam.stats.wins} </span><cms:contentText code="participant.throwdownstats" key="LOSS_SHORT_FORM"/>:<span>${match.primaryTeam.stats.losses} </span><cms:contentText code="participant.throwdownstats" key="TIE_SHORT_FORM"/>:<span>${match.primaryTeam.stats.ties}</span></p>
	                        <br/>
	                        <br/>
	                        <p><cms:contentText code="participant.throwdownstats" key="WIN_SHORT_FORM"/>:<span>${match.secondaryTeam.stats.wins} </span><cms:contentText code="participant.throwdownstats" key="LOSS_SHORT_FORM"/>:<span>${match.secondaryTeam.stats.losses} </span><cms:contentText code="participant.throwdownstats" key="TIE_SHORT_FORM"/>:<span>${match.secondaryTeam.stats.ties}</span></p>
							</c:if>
					  </td>
					  <td  class="crud-content left-align top-align nowrap" width="30%">
	                        <c:if test="${!match.roundYetToStart}">
								<p class="td-round-score"><c:out value="${match.primaryTeam.currentProgressForDisplayWithIndicator}" /></p>
								<p class="td-win-tie"><c:out value="${match.primaryTeam.outcomeForDisplayShortForm}" /></p>
								<br/>
								<br/>
								<p class="td-round-score"><c:out value="${match.secondaryTeam.currentProgressForDisplayWithIndicator}" /></p>
								<p class="td-win-tie"><c:out value="${match.secondaryTeam.outcomeForDisplayShortForm}" /></p>								
							</c:if>
					  </td>
					  </tr>
			    </c:forEach>
			    </tbody>
			</table>
             </c:if>
             
           <tr class="form-buttonrow">
            <td>
            <table width="100%">
            <tr> 
            <td align="center">
              <html:cancel styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('../participant/participantDisplay.do','display')" >
                <cms:contentText key="BACK_TO_PAX_OVERVIEW" code="participant.promotions" />
              </html:cancel>
            </td>
            <tr>
          </table>
          </td>
          </tr>
</table>
</html:form>