<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.*" %>

<!-- ======== THROWDOWN PUBLIC PROFILE PLAYER STATS TAB ======== -->
<div id="tabPlayerStatsCont">
<c:if test="${not empty playerStats.matches}">
    <div class="row-fluid">
        <div class="span5">
            <div class="fl avatarContainer">
                <div class="fl avatarContainerBorder">
                <!-- NEED TO UPDATE JSP AVATAR WRAP -->
                 <span class="avatarwrap">
                        <!-- if profile photo -->
                        <c:if test="${ playerStats.self.avatarUrlSmall != null }">
                           <img class="avatar" height="160" width="160" alt="<c:out value="${pax.firstName}" /> <c:out value="${pax.lastName}" /> Picture" id="personalInformationAvatar" src="${playerStats.self.avatarUrlSmall}">
                        </c:if>
                        <c:if test="${ playerStats.self.avatarUrlSmall == null }">
                            <c:set var="fcName" value="${playerStats.self.team.participant.firstName.substring(0,1)}"/>
                            <c:set var="lcName" value="${playerStats.self.team.participant.lastName.substring(0,1)}"/>
                            <span class="avatar-initials">${fcName}${lcName}</span>
                        </c:if>
                    </span>
                </div>
            </div>
            <div class="td-player-info">
                <p class="firstName">${playerStats.self.team.participant.firstName}</p>
                <p class="lastName">${playerStats.self.team.participant.lastName}</p>
                <p class="tdRank"><cms:contentText key="RANK" code="participant.throwdownstats"/>
                <c:if test="${playerStats.self.displayRank != ''}">
                ${playerStats.self.displayRank}
                </c:if>
                <c:if test="${playerStats.self.displayRank == ''}">
                <i class="icon-ban-circle"></i>
                </c:if>
                </p>
                <ul class="td-wl-count">
                    <li><cms:contentText code="participant.throwdownstats" key="WIN_SHORT_FORM"/>:<span class="tdWins">${playerStats.self.stats.wins}</span></li>
                    <li><cms:contentText code="participant.throwdownstats" key="LOSS_SHORT_FORM"/>:<span class="tdLoses">${playerStats.self.stats.losses}</span></li>
                    <li><cms:contentText code="participant.throwdownstats" key="TIE_SHORT_FORM"/>:<span class="tdTies">${playerStats.self.stats.ties}</span></li>
                </ul>
                <c:choose>
                <c:when test="${playerStats.matches[0].asOfDate ne null && playerStats.matches[0].asOfDate ne ''}">
                <span><cms:contentText code="participant.throwdownstats" key="FROM" />  <c:out value="${playerStats.matches[0].promotionStartDate}"/> <cms:contentText code="participant.throwdownstats" key="TO" />  <c:out value="${playerStats.matches[0].asOfDate}" />
				</span>
				</c:when>
				<c:otherwise>
				<span> <cms:contentText code="participant.throwdownstats" key="NO_PROGRESS" /> </span>
				</c:otherwise>
				</c:choose>
            </div>
        </div>

        <div class="span7">
            <div class="td-badges">
                <ul class="td-match-badge-list">

				    <c:forEach items="${playerStats.self.badges}" var="badges">
						<c:forEach items="${badges.badgeDetails}" var="detail">
							<li>
							<img src="${detail.img}"/>
							<span class="td-gold-badge">${detail.badgeName}</span>
							<span class="td-badge-earned-date">${detail.dateEarned}</span>
							</li>
						</c:forEach>
					</c:forEach>
                </ul>
            </div>
        </div>
    </div><!-- /.row-fluid -->

    <div class="row-fluid td-player-schedule">
        <div class="span12">
            <h4><cms:contentText code="participant.throwdownstats" key="SCHEDULE"/></h4>

            <table class="td-matches-schedule table table-striped">
                <thead>
                    <tr>
                        <th><cms:contentText code="participant.throwdownstats" key="ROUND"/></th>
                        <th><cms:contentText code="participant.throwdownstats" key="ROUND_DATES"/></th>
                        <th><cms:contentText code="participant.throwdownstats" key="PLAYER"/></th>
                        <th class="td-record"><cms:contentText code="participant.throwdownstats" key="RECORD"/></th>
                        <th colspan="3"><cms:contentText code="participant.throwdownstats" key="SCORE"/></th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${playerStats.matches}" var="match">
                    <tr>
                        <td class="td-round-number">
                        	${match.round.roundNumber}
                        </td>
                        <td class="td-round-dates">
	                        <fmt:formatDate value="${match.round.startDate}" pattern="${JstlDatePattern}" />
						    <cms:contentText code="participant.throwdown.promo.detail" key="HYPHEN" /><br/>
						    <fmt:formatDate value="${match.round.endDate}" pattern="${JstlDatePattern}" />
					    </td>
                        <td>
                        	<img class="participant-list-avatar avatar" src="${match.primaryTeam.avatarUrl}" style="width: 30px;height: 30px"/>
							  <c:if test="${!match.primaryTeam.team.shadowPlayer}">
							    <p class="currentParticipantProfile" >${match.primaryTeam.team.participant.lastName},&nbsp;${match.primaryTeam.team.participant.firstName}</p>
							  </c:if>
							  <c:if test="${match.primaryTeam.team.shadowPlayer}">
							    <p><cms:contentText code="participant.throwdownstats" key="SHADOW_PLAYER"/></p>
							  </c:if>
							  <br/>
							  <c:if test="${match.opponentDecided}">
							  <img class="participant-list-avatar avatar" src="${match.secondaryTeam.avatarUrl}" style="width: 30px;height: 30px" />
							  <c:if test="${!match.secondaryTeam.team.shadowPlayer}">
							  <p>
							   <a class="profile-popover" href="#" data-participant-ids="[${match.secondaryTeam.team.participant.id}]">
							    ${match.secondaryTeam.team.participant.lastName},&nbsp;${match.secondaryTeam.team.participant.firstName}
							   </a>
							  </p>
							  </c:if>
							  <c:if test="${match.secondaryTeam.team.shadowPlayer}">
							    <p> <cms:contentText code="participant.throwdownstats" key="SHADOW_PLAYER"/> </p>
							  </c:if>
							  </c:if>
							  <c:if test="${match.roundYetToStart && !match.opponentDecided}">
							  	<img class="participant-list-avatar avatar" src="${match.avatarUrlForTBDPlayer}" style="width: 30px;height: 30px" />
							  	<p> <cms:contentText code="participant.throwdownstats" key="TBD_PLAYER"/> </p>
							  </c:if>
							  <c:if test="${!match.roundYetToStart && !match.opponentDecided}">
							  	<img class="participant-list-avatar avatar" src="${match.avatarUrlForTBDPlayer}" style="width: 30px;height: 30px" />
							  	<p> <cms:contentText code="participant.throwdownstats" key="NONE_PLAYER"/> </p>
							  </c:if>
						</td>
                        <td class="td-record">
	                        <c:if test="${!match.roundYetToStart  && match.opponentDecided}">
	                        <p><cms:contentText code="participant.throwdownstats" key="WIN_SHORT_FORM"/>:<span>${match.primaryTeam.stats.wins} </span><cms:contentText code="participant.throwdownstats" key="LOSS_SHORT_FORM"/>:<span>${match.primaryTeam.stats.losses} </span><cms:contentText code="participant.throwdownstats" key="TIE_SHORT_FORM"/>:<span>${match.primaryTeam.stats.ties}</span></p>
	                        <br/>
	                        <p><cms:contentText code="participant.throwdownstats" key="WIN_SHORT_FORM"/>:<span>${match.secondaryTeam.stats.wins} </span><cms:contentText code="participant.throwdownstats" key="LOSS_SHORT_FORM"/>:<span>${match.secondaryTeam.stats.losses} </span><cms:contentText code="participant.throwdownstats" key="TIE_SHORT_FORM"/>:<span>${match.secondaryTeam.stats.ties}</span></p>
							</c:if>
						</td>
                        <td colspan="2">
	                        <c:if test="${!match.roundYetToStart  && match.opponentDecided}">
								<p class="td-round-score"><c:out value="${match.primaryTeam.currentProgressForDisplayWithIndicator}" />
								<span class="td-win-tie"><c:out value="${match.primaryTeam.outcomeForDisplayShortForm}" /></span></p>
								<br/>
								<p class="td-round-score"><c:out value="${match.secondaryTeam.currentProgressForDisplayWithIndicator}" />
								<span class="td-win-tie"><c:out value="${match.secondaryTeam.outcomeForDisplayShortForm}" /></span></p>
							</c:if>
                        </td>
                        <td>
                           <c:if test="${match.opponentDecided}">
	                       <a href="${match.matchUrl}" class="btn btn-primary throwdown-match-detail-btn">
			                     <c:choose>
			                      <c:when test="${match.promotion.smackTalkAvailable }">
			       	               <cms:contentText code="participant.throwdownstats" key="SMACK_TALK" />
			       	              </c:when>
			                     <c:otherwise>
			                       <cms:contentText code="participant.throwdownstats" key="MATCH_DETAILS" />
			                     </c:otherwise>
			                    </c:choose>
			               </a>
			               <div class="app-col chevron"><a href="#"><i class="icon-arrow-1-right"></i></a></div>
			               </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div><!-- /.row-fluid -->
</c:if>
</div><!-- ./tabPlayerStatsCont -->
